package cn.offway.Poseidon.controller;

import cn.offway.Poseidon.properties.QiniuProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @RequestMapping("/template1.html")
    public String users(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        return "template1";
    }

}
