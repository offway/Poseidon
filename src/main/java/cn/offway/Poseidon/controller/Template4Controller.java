package cn.offway.Poseidon.controller;

import cn.offway.Poseidon.domain.PhLock;
import cn.offway.Poseidon.domain.PhTemplate1;
import cn.offway.Poseidon.domain.PhTemplate4;
import cn.offway.Poseidon.domain.PhTemplateConfig;
import cn.offway.Poseidon.properties.QiniuProperties;
import cn.offway.Poseidon.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户管理
 *
 * @author wn
 */
@Controller
public class Template4Controller {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private QiniuProperties qiniuProperties;

    @Autowired
    private PhTemplate4Service template4Service;

    @Autowired
    private PhLockService lockService;

    @Autowired
    private PhTemplateConfigService templateConfigService;

    @RequestMapping("/template4.html")
    public String users(ModelMap map, String goodsId,String templateId,String templateConfigId) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        map.addAttribute("goodsId", goodsId);
        map.addAttribute("templateId",templateId);
        map.addAttribute("templateConfigId",templateConfigId);
        return "template4";
    }

    @ResponseBody
    @RequestMapping("/template4_save")
    @Transactional
    public boolean save(PhTemplate4 template4, PhLock lock,String templateId,String lockId,String templateConfigId) {
        PhTemplateConfig templateConfig = new PhTemplateConfig();
        if (!"".equals(templateId) && !"".equals(lockId)){
            template4.setId(Long.valueOf(templateId));
            lock.setId(Long.valueOf(lockId));
            templateConfig.setId(Long.valueOf(templateConfigId));
        }else {
            template4.setCreateTime(new Date());
            templateConfig.setCreateTime(new Date());
            lock.setCreateTime(new Date());
        }
        template4 = template4Service.save(template4);
        templateConfig.setGoodsId(template4.getGoodsId());
        templateConfig.setName("template4");
        templateConfig.setTemplateId(template4.getId().toString());
        templateConfig.setSort(0L);
        if ("1".equals(lock.getIslock())){
            templateConfig.setIslock("0");
            templateConfig.setConditions("0");
        }else {
            templateConfig.setIslock("1");
            templateConfig.setConditions("1");
            templateConfig.setConditionsRemark("达到"+lock.getSubscribeCount().toString()+"人后解锁");
        }
        templateConfig.setStatus("0");
        templateConfigService.save(templateConfig);
        lock.setTemplateType("3");
        lock.setPid(templateConfig.getId());
        lock.setTemplateId(template4.getId());
        lockService.save(lock);
        return true;
    }

    @ResponseBody
    @RequestMapping("/template4_findone")
    public Map<String,Object> findone(String templateId,String goodsId){
        Map<String,Object> map = new HashMap<>();
        PhTemplate4 template4 = template4Service.findOne(Long.valueOf(templateId));
        map.put("template4",template4);
        PhLock lock = lockService.findByGoodsIdAndTemplateTypeAndTemplateId(Long.valueOf(goodsId),"3",Long.valueOf(templateId));
        map.put("lock",lock);
        return map;
    }

}
