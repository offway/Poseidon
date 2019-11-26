package cn.offway.Poseidon.controller;

import cn.offway.Poseidon.domain.PhLock;
import cn.offway.Poseidon.domain.PhTemplate;
import cn.offway.Poseidon.domain.PhTemplate3;
import cn.offway.Poseidon.domain.PhTemplateConfig;
import cn.offway.Poseidon.properties.QiniuProperties;
import cn.offway.Poseidon.service.PhLockService;
import cn.offway.Poseidon.service.PhTemplate3Service;
import cn.offway.Poseidon.service.PhTemplateConfigService;
import cn.offway.Poseidon.service.PhTemplateService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class Template3Controller {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private QiniuProperties qiniuProperties;
    @Autowired
    private PhTemplate3Service template3Service;
    @Autowired
    private PhTemplateConfigService templateConfigService;
    @Autowired
    private PhTemplateService templateService;
    @Autowired
    private PhLockService lockService;

    @RequestMapping("/template3.html")
    public String index(ModelMap map, String pid, String gid, @RequestParam(required = false, defaultValue = "") String id) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        map.addAttribute("pid", pid);
        map.addAttribute("gid", gid);
        map.addAttribute("id", id);
        return "template3";
    }

    @ResponseBody
    @RequestMapping("/template3_save")
    @Transactional
    public Object save(Long pid, Long gid, PhTemplate3 template3, Long islock, String subscribeCount, String promptTextLock) {
        PhTemplate template = templateService.findOne(gid);
        PhTemplateConfig templateConfig = templateConfigService.findOne(pid);
        if (template != null && templateConfig != null) {
            template3.setCreateTime(new Date());
            template3.setGoodsId(gid);
            // if edit
            if (template3.getId() != null) {
                PhTemplate3 template3Saved = template3Service.findOne(template3.getId());
                if (template3Saved != null) {
                    template3.setCreateTime(template3Saved.getCreateTime());
                    template3.setRemark(template3Saved.getRemark());
                }
            }
            PhTemplate3 savedObj = template3Service.save(template3);
            // update config
            templateConfig.setTemplateId(String.valueOf(savedObj.getId()));
            if (StringUtils.isNotBlank(subscribeCount)) {
                templateConfig.setConditionsRemark(MessageFormat.format("订阅数达到{0}本即可解锁～", subscribeCount));
            } else {
                templateConfig.setConditionsRemark(null);
            }
            //templateConfig.setSort(0L);
            templateConfigService.save(templateConfig);
            // update lock
            //模版类型:[0-1号模板,1-2号模板,2-3号模板,3-4号模板,4-5号模板]
            PhLock lock = lockService.findByGoodsIdAndTemplateTypeAndConfigId(gid, "2", pid);
            if (lock == null) {
                lock = new PhLock();
                lock.setCreateTime(new Date());
                lock.setTemplateType("2");
                lock.setPid(pid);
                lock.setGoodsId(gid);
            }
            //是否解锁[0-否,1-是]
            lock.setIslock(String.valueOf(islock));
            if (StringUtils.isNotBlank(subscribeCount)) {
                lock.setSubscribeCount(Long.valueOf(subscribeCount));
            } else {
                lock.setSubscribeCount(null);
            }
            if (StringUtils.isNotBlank(promptTextLock)) {
                lock.setPromptText(promptTextLock);
            } else {
                lock.setPromptText("");
            }
            lockService.save(lock);
            return savedObj.getId();
        }
        return "";
    }

    @ResponseBody
    @RequestMapping("/template3_get")
    public Map<String, Object> get(Long pid, Long gid, Long id) {
        Map<String, Object> data = new HashMap<>();
        data.put("data", template3Service.findOne(id));
        //模版类型:[0-1号模板,1-2号模板,2-3号模板,3-4号模板,4-5号模板]
        data.put("lock", lockService.findByGoodsIdAndTemplateTypeAndConfigId(gid, "2", pid));
        return data;
    }
}
