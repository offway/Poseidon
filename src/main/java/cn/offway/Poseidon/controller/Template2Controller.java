package cn.offway.Poseidon.controller;

import cn.offway.Poseidon.domain.PhLock;
import cn.offway.Poseidon.domain.PhTemplate;
import cn.offway.Poseidon.domain.PhTemplate2;
import cn.offway.Poseidon.domain.PhTemplateConfig;
import cn.offway.Poseidon.properties.QiniuProperties;
import cn.offway.Poseidon.service.PhLockService;
import cn.offway.Poseidon.service.PhTemplate2Service;
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
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class Template2Controller {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private QiniuProperties qiniuProperties;
    @Autowired
    private PhTemplate2Service template2Service;
    @Autowired
    private PhTemplateConfigService templateConfigService;
    @Autowired
    private PhTemplateService templateService;
    @Autowired
    private PhLockService lockService;

    @RequestMapping("/template2.html")
    public String index(ModelMap map, String pid, String gid) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        map.addAttribute("pid", pid);
        map.addAttribute("gid", gid);
        return "template2";
    }

    @ResponseBody
    @RequestMapping("/template2_save")
    @Transactional
    public boolean save(Long pid, Long gid, String[] type, String[] way, String[] imageUrl, String[] promptText, String[] remark, String[] location, Long islock, String subscribeCount, String promptTextLock) {
        PhTemplate template = templateService.findOne(gid);
        PhTemplateConfig templateConfig = templateConfigService.findOne(pid);
        if (template != null && templateConfig != null) {
            if (type.length - way.length + imageUrl.length - remark.length + location.length == location.length) {
                // clear items
                template2Service.deleteByPid(pid);
                // add new items
                List<Long> ids = new ArrayList<>();
                for (int i = 0; i < type.length; i++) {
                    PhTemplate2 template2 = new PhTemplate2();
                    template2.setCreateTime(new Date());
                    template2.setGoodsId(gid);
                    template2.setPid(pid);
                    template2.setType(type[i]);
                    template2.setWay(way[i]);
                    template2.setImageUrl(imageUrl[i]);
                    template2.setPromptText(promptText[i]);
                    template2.setRemark(remark[i]);
                    template2.setLocation(location[i]);
                    PhTemplate2 savedObj = template2Service.save(template2);
                    ids.add(savedObj.getId());
                }
                // update config
                templateConfig.setTemplateId(StringUtils.join(ids, ","));
                templateConfigService.save(templateConfig);
                // update lock
                //模版类型:[0-1号模板,1-2号模板,2-3号模板,3-4号模板,4-5号模板]
                PhLock lock = lockService.findByGoodsIdAndTemplateTypeAndConfigId(gid, "1", pid);
                if (lock == null) {
                    lock = new PhLock();
                    lock.setCreateTime(new Date());
                    lock.setTemplateType("1");
                    lock.setPid(pid);
                    lock.setGoodsId(gid);
                }
                //是否解锁[0-否,1-是]
                lock.setIslock(String.valueOf(islock));
                if (StringUtils.isNotBlank(subscribeCount)) {
                    lock.setSubscribeCount(Long.valueOf(subscribeCount));
                }
                if (StringUtils.isNotBlank(promptTextLock)) {
                    lock.setPromptText(promptTextLock);
                }
                lockService.save(lock);
                return true;
            }
        }
        return false;
    }

}
