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
    public String users(ModelMap map, String goodsId, String lockId) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        map.addAttribute("goodsId", goodsId);
        return "template5";
    }

    @ResponseBody
    @RequestMapping("/template5_save")
    public boolean save(PhTemplate5 template5, PhLock lock) {
        template5.setCreateTime(new Date());
        template5 = template5Service.save(template5);
        lock.setCreateTime(new Date());
        lock.setTemplateType("4");
        lock.setTemplateId(template5.getId());
        lockService.save(lock);
        PhTemplateConfig templateConfig = new PhTemplateConfig();
        templateConfig.setGoodsId(template5.getGoodsId());
        templateConfig.setName("template5");
        templateConfig.setTemplateId(template5.getId());
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
