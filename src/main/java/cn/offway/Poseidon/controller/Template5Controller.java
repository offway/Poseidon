package cn.offway.Poseidon.controller;

import cn.offway.Poseidon.domain.PhLock;
import cn.offway.Poseidon.domain.PhTemplate4;
import cn.offway.Poseidon.domain.PhTemplate5;
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
public class Template5Controller {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private QiniuProperties qiniuProperties;

    @Autowired
    private PhTemplate5Service template5Service;

    @Autowired
    private PhLockService lockService;

    @Autowired
    private PhTemplateConfigService templateConfigService;

    @RequestMapping("/template5.html")
    public String users(ModelMap map, String goodsId,String templateId,String templateConfigId) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        map.addAttribute("goodsId", goodsId);
        map.addAttribute("templateId",templateId);
        map.addAttribute("templateConfigId",templateConfigId);
        return "template5";
    }

    @ResponseBody
    @RequestMapping("/template5_save")
    @Transactional
    public boolean save(PhTemplate5 template5, PhLock lock,String templateId,String lockId,String templateConfigId) {
        PhTemplateConfig templateConfig = new PhTemplateConfig();
        if (!"".equals(templateId) && !"".equals(lockId)){
            template5.setId(Long.valueOf(templateId));
            lock.setId(Long.valueOf(lockId));
            templateConfig.setId(Long.valueOf(templateConfigId));
        }else {
            template5.setCreateTime(new Date());
            templateConfig.setCreateTime(new Date());
            lock.setCreateTime(new Date());
        }
        if (!"".equals(templateConfigId)){
            templateConfig = templateConfigService.findOne(Long.valueOf(templateConfigId));
        }
        template5 = template5Service.save(template5);
        templateConfig.setGoodsId(template5.getGoodsId());
        templateConfig.setName("template5");
        templateConfig.setTemplateId(template5.getId().toString());
        //templateConfig.setSort(0L);
        if ("0".equals(lock.getIslock())){
            templateConfig.setIslock("0");
            templateConfig.setConditions("0");
        }else {
            templateConfig.setIslock("1");
            templateConfig.setConditions("1");
            templateConfig.setConditionsRemark("订阅数达到"+lock.getSubscribeCount().toString()+"本即可解锁～");
        }
        templateConfig.setStatus("0");
        templateConfigService.save(templateConfig);
        lock.setTemplateType("4");
        lock.setTemplateId(template5.getId());
        lock.setPid(templateConfig.getId());
        lockService.save(lock);
        return true;
    }

    @ResponseBody
    @RequestMapping("/template5_findone")
    public Map<String,Object> findone(String templateId,String goodsId){
        Map<String,Object> map =new HashMap<>();
        PhTemplate5 template5 = template5Service.findOne(Long.valueOf(templateId));
        map.put("template5",template5);
        PhLock lock = lockService.findByGoodsIdAndTemplateTypeAndTemplateId(Long.valueOf(goodsId),"4",Long.valueOf(templateId));
        map.put("lock",lock);
        return map;
    }

}
