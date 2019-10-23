package cn.offway.Poseidon.controller;

import cn.offway.Poseidon.domain.PhLock;
import cn.offway.Poseidon.domain.PhTemplate1;
import cn.offway.Poseidon.domain.PhTemplateConfig;
import cn.offway.Poseidon.properties.QiniuProperties;
import cn.offway.Poseidon.service.PhLockService;
import cn.offway.Poseidon.service.PhTemplate1Service;
import cn.offway.Poseidon.service.PhTemplateConfigService;
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
    public String users(ModelMap map,String goodsId,String lockId) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        map.addAttribute("goodsId",goodsId);
        return "template1";
    }

    @ResponseBody
    @RequestMapping("/template1_save")
    public boolean save(PhTemplate1 template1, PhLock lock){
        template1.setCreateTime(new Date());
        PhTemplate1 template1save = template1Service.save(template1);
        lock.setCreateTime(new Date());
        lock.setTemplateType("0");
        lock.setTemplateId(template1save.getId());
        lockService.save(lock);
        PhTemplateConfig templateConfig = new PhTemplateConfig();
        templateConfig.setGoodsId(template1.getGoodsId());
        templateConfig.setName("template1");
        templateConfig.setTemplateId(template1.getId());
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
