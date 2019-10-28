package cn.offway.Poseidon.controller;

import cn.offway.Poseidon.domain.PhTemplate;
import cn.offway.Poseidon.properties.QiniuProperties;
import cn.offway.Poseidon.service.PhTemplateService;
import cn.offway.Poseidon.service.QiniuService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


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
    private QiniuService qiniuService;


    /**
     * 商品
     */
    @RequestMapping("/goods.html")
    public String goods(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        map.addAttribute("token", qiniuService.token());
        return "goods";
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
    @RequestMapping("/goods-save")
    public boolean save(PhTemplate template) {
        template.setCreateTime(new Date());
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
}
