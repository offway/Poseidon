package cn.offway.Poseidon.controller;

import cn.offway.Poseidon.properties.QiniuProperties;
import cn.offway.Poseidon.service.PhLockService;
import cn.offway.Poseidon.service.PhTemplate3Service;
import cn.offway.Poseidon.service.PhTemplateConfigService;
import cn.offway.Poseidon.service.PhTemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
}
