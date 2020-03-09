package cn.offway.Poseidon.controller;

import cn.offway.Poseidon.domain.PhLock;
import cn.offway.Poseidon.domain.PhReadcode;
import cn.offway.Poseidon.domain.PhTemplate;
import cn.offway.Poseidon.properties.QiniuProperties;
import cn.offway.Poseidon.service.PhLockService;
import cn.offway.Poseidon.service.PhReadcodeService;
import cn.offway.Poseidon.service.PhTemplateService;
import cn.offway.Poseidon.service.QiniuService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.*;


/**
 * 商品管理
 *
 * @author wn
 */
@Controller
public class GoodsController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private QiniuProperties qiniuProperties;

    @Autowired
    private PhTemplateService templateService;

    @Autowired
    private PhReadcodeService readcodeService;

    @Autowired
    private PhLockService lockService;

    @Autowired
    private QiniuService qiniuService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private final static String KEY_RANK = "GOODS_SELL_RANK";


    /**
     * 商品
     */
    @RequestMapping("/goods.html")
    public String goods(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        map.addAttribute("token", qiniuService.token());
        return "goods";
    }

    @RequestMapping("/readCode.html")
    public String code(ModelMap map, String id) {
        map.addAttribute("id", id);
        return "readCode";
    }

    /**
     * 查询数据
     */
    @ResponseBody
    @RequestMapping("/goods-data")
    public Map<String, Object> goodsData(HttpServletRequest request) {
        //paging start
        String sortCol = request.getParameter("iSortCol_0");
        String sortName = request.getParameter("mDataProp_" + sortCol);
        String sortDir = request.getParameter("sSortDir_0");
        int sEcho = Integer.parseInt(request.getParameter("sEcho"));
        int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
        int iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
        PageRequest pr = new PageRequest(iDisplayStart == 0 ? 0 : iDisplayStart / iDisplayLength, iDisplayLength < 0 ? 9999999 : iDisplayLength, Direction.fromString(sortDir), sortName);
        //paging end
        Page<PhTemplate> pages = templateService.list(pr);
        // 为操作次数加1，必须这样做
        int initEcho = sEcho + 1;
        Map<String, Object> map = new HashMap<>();
        map.put("sEcho", initEcho);
        map.put("iTotalRecords", pages.getTotalElements());//数据总条数  
        map.put("iTotalDisplayRecords", pages.getTotalElements());//显示的条数  
        map.put("aData", pages.getContent());//数据集合 
        return map;
    }

    @ResponseBody
    @RequestMapping("/readCode-data")
    public Map<String, Object> codeData(HttpServletRequest request, int sEcho, int iDisplayStart, int iDisplayLength, long gid) {
        //paging start
        String sortCol = request.getParameter("iSortCol_0");
        String sortName = request.getParameter("mDataProp_" + sortCol);
        String sortDir = request.getParameter("sSortDir_0");
        PageRequest pr = new PageRequest(iDisplayStart == 0 ? 0 : iDisplayStart / iDisplayLength, iDisplayLength < 0 ? 9999999 : iDisplayLength, Direction.fromString(sortDir), sortName);
        //paging end
        Page<PhReadcode> pages = readcodeService.findByBuyersIdAndBooksId(null, 0L, gid, pr);
        // 为操作次数加1，必须这样做
        int initEcho = sEcho + 1;
        Map<String, Object> map = new HashMap<>();
        map.put("sEcho", initEcho);
        map.put("iTotalRecords", pages.getTotalElements());//数据总条数
        map.put("iTotalDisplayRecords", pages.getTotalElements());//显示的条数
        map.put("aData", pages.getContent());//数据集合
        return map;
    }

    @ResponseBody
    @RequestMapping("/goods-rank")
    public LinkedList<Map<String, Object>> getRank(String gid) {
        LinkedList<Map<String, Object>> list = new LinkedList<>();
        String key = MessageFormat.format("{0}_{1}", KEY_RANK, gid);
        if (stringRedisTemplate.hasKey(key)) {
            for (ZSetOperations.TypedTuple<String> obj : stringRedisTemplate.opsForZSet().reverseRangeWithScores(key, 0, 100)) {
                Map<String, Object> m = new HashMap<>();
                m.put("userId", obj.getValue());
                m.put("count", obj.getScore().intValue());
                list.add(m);
            }
        }
        return list;
    }

    @ResponseBody
    @RequestMapping("/goods-rank-add")
    public boolean addRank(String gid, @RequestParam(name = "userId") String[] uid, @RequestParam(name = "count") double[] count) {
        double total = 0;
        String key = MessageFormat.format("{0}_{1}", KEY_RANK, gid);
        if (uid.length == count.length) {
            for (int i = 0; i < uid.length; i++) {
                stringRedisTemplate.opsForZSet().add(key, uid[i], count[i]);
                total += count[i];
            }
        }
        //更新总订阅数
        PhTemplate template = templateService.findOne(Long.valueOf(gid));
        template.setSubscribeSum(template.getSubscribeSum() + (long) total);
        templateService.save(template);
        //检查解锁情况
        List<PhLock> locks = lockService.findAllByGoodsId(template.getId());
        List<PhLock> lockList = new ArrayList<>();
        for (PhLock lock : locks) {
            if ("1".equals(lock.getUnlock()) && (template.getSubscribeSum() >= lock.getSubscribeCount())) {
                lock.setUnlock("0");
                lockList.add(lock);
            }
        }
        if (lockList.size() > 0) {
            lockService.save(lockList);
        }
        return true;
    }

    @ResponseBody
    @RequestMapping("/goods-save")
    public boolean save(PhTemplate template, @RequestParam(name = "imageUrl") String[] imageUrls) {
        template.setCreateTime(new Date());
        template.setReadingNumber(0L);
        template.setImageUrl(StringUtils.join(imageUrls, ","));
        if (template.getId() != null) {
            PhTemplate saved = templateService.findOne(template.getId());
            if (saved != null) {
                template.setCreateTime(saved.getCreateTime());
            }
        }
        templateService.save(template);
        return true;
    }

    @ResponseBody
    @RequestMapping("/goods-one")
    public PhTemplate findOne(Long id) {
        return templateService.findOne(id);
    }

    @RequestMapping("/goods-update")
    @ResponseBody
    @Transactional
    public boolean goodsUpdate(@RequestParam("ids[]") Long[] ids, String status) {
        for (long id : ids) {
            PhTemplate template = templateService.findOne(id);
            if (template != null && StringUtils.isNotBlank(status)) {
                /* 状态[0-上架,1-下架] **/
                template.setState(status);
                templateService.save(template);
            }
        }
        return true;
    }

    @RequestMapping("/goods-delete")
    @ResponseBody
    @Transactional
    public boolean goodsDelete(@RequestParam("ids[]") Long[] ids) {
        for (long id : ids) {
            templateService.delete(id);
        }
        return true;
    }

    @ResponseBody
    @RequestMapping("/goods-generateCode")
    public boolean generateCode(Long id, int count) {
        List<PhReadcode> codeList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            String uuid = RandomStringUtils.randomAlphanumeric(10).toUpperCase();
            PhReadcode code = new PhReadcode();
            code.setBooksId(id);
            code.setState("0");
            code.setCode(uuid);
            code.setBuyersId(0L);
            code.setCreateTime(new Date());
            code.setRemark("后台生成");
            codeList.add(code);
        }
        if (codeList.isEmpty()) {
            return false;
        } else {
            readcodeService.save(codeList);
            return true;
        }
    }
}
