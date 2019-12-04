package cn.offway.Poseidon.controller;

import cn.offway.Poseidon.domain.PhLock;
import cn.offway.Poseidon.domain.PhTemplate1;
import cn.offway.Poseidon.domain.PhTemplate4;
import cn.offway.Poseidon.domain.PhTemplateConfig;
import cn.offway.Poseidon.properties.QiniuProperties;
import cn.offway.Poseidon.service.PhLockService;
import cn.offway.Poseidon.service.PhTemplate1Service;
import cn.offway.Poseidon.service.PhTemplateConfigService;
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
public class Template1Controller {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private QiniuProperties qiniuProperties;

    @Autowired
    private PhTemplate1Service template1Service;

    @Autowired
    private PhLockService lockService;

    @Autowired
    private PhTemplateConfigService templateConfigService;

    @RequestMapping("/template1.html")
    public String users(ModelMap map,String goodsId,String templateId,String templateConfigId) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        map.addAttribute("goodsId",goodsId);
        map.addAttribute("templateId",templateId);
        map.addAttribute("templateConfigId",templateConfigId);
        return "template1";
    }

    @ResponseBody
    @RequestMapping("/template1_save")
    @Transactional
    public boolean save(PhTemplate1 template1, PhLock lock,String templateId,String lockId,String templateConfigId){
        PhTemplateConfig templateConfig = new PhTemplateConfig();
        if (!"".equals(templateId) && !"".equals(lockId)){
            template1.setId(Long.valueOf(templateId));
            lock.setId(Long.valueOf(lockId));
            templateConfig.setId(Long.valueOf(templateConfigId));
        }else {
            template1.setCreateTime(new Date());
            templateConfig.setCreateTime(new Date());
            lock.setCreateTime(new Date());
        }
        if (!"".equals(templateConfigId)){
            templateConfig = templateConfigService.findOne(Long.valueOf(templateConfigId));
        }
        PhTemplate1 template1save = template1Service.save(template1);
        templateConfig.setGoodsId(template1.getGoodsId());
        templateConfig.setName("template1");
        templateConfig.setTemplateId(template1.getId().toString());
        //templateConfig.setSort(0L);
        if ("0".equals(lock.getIslock())){
            templateConfig.setIslock("0");
            templateConfig.setConditions("0");
        }else {
            lock.setContentType("0");
            templateConfig.setIslock("1");
            templateConfig.setConditions("1");
            templateConfig.setConditionsRemark("订阅数达到"+lock.getSubscribeCount().toString()+"本即可解锁～");
        }
        templateConfig.setStatus("0");
        templateConfig = templateConfigService.save(templateConfig);
        lock.setTemplateType("0");
        lock.setPid(templateConfig.getId());
        lock.setTemplateId(template1save.getId());
        lockService.save(lock);
        return true;
    }

    @ResponseBody
    @RequestMapping("/template1_findone")
    public Map<String,Object> findone(String templateId,String goodsId){
        Map<String,Object> map = new HashMap<>();
        PhTemplate1 template1 = template1Service.findOne(Long.valueOf(templateId));
        map.put("template1",template1);
        PhLock lock = lockService.findByGoodsIdAndTemplateTypeAndTemplateId(Long.valueOf(goodsId),"0",Long.valueOf(templateId));
        map.put("lock",lock);
        return map;
    }

}
