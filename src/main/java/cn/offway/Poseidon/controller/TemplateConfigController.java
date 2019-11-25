package cn.offway.Poseidon.controller;

import cn.offway.Poseidon.domain.PhConfig;
import cn.offway.Poseidon.domain.PhTemplateConfig;
import cn.offway.Poseidon.properties.QiniuProperties;
import cn.offway.Poseidon.service.PhTemplateConfigService;
import cn.offway.Poseidon.service.QiniuService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class TemplateConfigController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private QiniuProperties qiniuProperties;

    @Autowired
    private PhTemplateConfigService templateConfigService;

    @Autowired
    private QiniuService qiniuService;


    /**
     * 商品
     */
    @RequestMapping("/config.html")
    public String index(ModelMap map, String pid) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        map.addAttribute("token", qiniuService.token());
        map.addAttribute("pid", pid);
        return "templateConfig";
    }

    /**
     * 查询数据
     */
    @ResponseBody
    @RequestMapping("/config-data")
    public Map<String, Object> list(HttpServletRequest request, String pid) {
        //paging start
        String sortCol = request.getParameter("iSortCol_0");
        String sortName = request.getParameter("mDataProp_" + sortCol);
        String sortDir = request.getParameter("sSortDir_0");
        int sEcho = Integer.parseInt(request.getParameter("sEcho"));
        int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
        int iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
        PageRequest pr = new PageRequest(iDisplayStart == 0 ? 0 : iDisplayStart / iDisplayLength, iDisplayLength < 0 ? 9999999 : iDisplayLength, Sort.Direction.fromString(sortDir), sortName);
        //paging end
        Page<PhTemplateConfig> pages = templateConfigService.list(pr, pid);
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
    @RequestMapping("/config-save")
    public boolean save(PhTemplateConfig config) {
        config.setCreateTime(new Date());
        if (config.getId() != null) {
            PhTemplateConfig saved = templateConfigService.findOne(config.getId());
            if (saved != null) {
                config.setCreateTime(saved.getCreateTime());
            }
        }
        Long sort = 0L;
        if (null == config.getSort() || config.getSort() == 0L){
            sort = templateConfigService.findByMaxSort(config.getGoodsId());
            if (null == sort){
                config.setSort(1L);
            }else {
                config.setSort(sort+1L);
            }
        }
        templateConfigService.save(config);
        return true;
    }

    @ResponseBody
    @RequestMapping("/config-one")
    public PhTemplateConfig findOne(Long id) {
        return templateConfigService.findOne(id);
    }

    @RequestMapping("/config-update")
    @ResponseBody
    @Transactional
    public boolean update(@RequestParam("ids[]") Long[] ids, String status) {
        for (long id : ids) {
            PhTemplateConfig config = templateConfigService.findOne(id);
            if (config != null && StringUtils.isNotBlank(status)) {
                /* 启用状态[0-启用,1-不启用] **/
                config.setStatus(status);
                templateConfigService.save(config);
            }
        }
        return true;
    }

    @RequestMapping("/config-delete")
    @ResponseBody
    @Transactional
    public boolean delete(@RequestParam("ids[]") Long[] ids) {
        for (long id : ids) {
            templateConfigService.delete(id);
            //TODO 还需要删除 子模版数据、模板锁数据
        }
        return true;
    }

    @RequestMapping("/config-sorting")
    @ResponseBody
    @Transactional
    public boolean sorting(Long id,Long sort){
        List<PhTemplateConfig> templateConfigList = new ArrayList<>();
        PhTemplateConfig config = templateConfigService.findOne(id);
        List<PhTemplateConfig> templateConfigs = templateConfigService.findByGoodsIdList(config.getGoodsId());
        for (PhTemplateConfig templateConfig : templateConfigs) {
            if (templateConfig.getId()==id){
                templateConfig.setSort(sort);
                templateConfigService.save(templateConfig);
            }else if (templateConfig.getSort()>=sort){
                templateConfig.setSort(templateConfig.getSort()+1);
                templateConfigList.add(templateConfig);
            }
        }
        templateConfigService.save(templateConfigList);
        return true;
    }
}
