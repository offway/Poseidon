package cn.offway.Poseidon.controller;

import cn.offway.Poseidon.domain.PhLock;
import cn.offway.Poseidon.domain.PhTemplate4;
import cn.offway.Poseidon.domain.PhTemplateConfig;
import cn.offway.Poseidon.properties.QiniuProperties;
import cn.offway.Poseidon.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

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
    public String users(ModelMap map, String goodsId, String lockId) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        map.addAttribute("goodsId", goodsId);
        return "template4";
    }

    @ResponseBody
    @RequestMapping("/template4_save")
    public boolean save(PhTemplate4 template4, PhLock lock) {
        template4.setCreateTime(new Date());
        template4 = template4Service.save(template4);
        lock.setCreateTime(new Date());
        lock.setTemplateType("3");
        lock.setTemplateId(template4.getId());
        lockService.save(lock);
        PhTemplateConfig templateConfig = new PhTemplateConfig();
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
        templateConfig.setCreateTime(new Date());
        templateConfigService.save(templateConfig);
        return true;
    }

}
