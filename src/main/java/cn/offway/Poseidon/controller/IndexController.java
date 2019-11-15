package cn.offway.Poseidon.controller;

import cn.offway.Poseidon.domain.PhAdmin;
import cn.offway.Poseidon.domain.PhResource;
import cn.offway.Poseidon.domain.PhTemplate;
import cn.offway.Poseidon.service.PhTemplateService;
import cn.offway.Poseidon.service.PhUserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping
public class IndexController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private PhTemplateService templateService;
    @Autowired
    private PhUserInfoService userInfoService;

    /**
     * 登录页面
     */
    @RequestMapping("/login.html")
    public String login() {
        return "login";
    }


    @RequestMapping("/resoures")
    @ResponseBody
    public List<PhResource> resoures(@AuthenticationPrincipal PhAdmin admin) {
        return admin.getResources();
    }


    /**
     * 首页
     */
    @RequestMapping("/")
    public String index(ModelMap map) {
        long bookNumber = 0, subscribeSum = 0, readingNumber = 0, soldNumber = 0;
        for (PhTemplate template : templateService.list()) {
            bookNumber++;
            subscribeSum += template.getSubscribeSum() != null ? template.getSubscribeSum() : 0;
            readingNumber += template.getReadingNumber() != null ? template.getReadingNumber() : 0;
            soldNumber += template.getSoldNumber() != null ? template.getSoldNumber() : 0;
        }
        map.addAttribute("bookNumber", bookNumber);
        map.addAttribute("subscribeSum", subscribeSum);
        map.addAttribute("readingNumber", readingNumber);
        map.addAttribute("soldNumber", soldNumber);
        return "home";
    }


}
