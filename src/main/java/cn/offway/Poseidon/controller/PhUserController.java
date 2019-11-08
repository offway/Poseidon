package cn.offway.Poseidon.controller;

import cn.offway.Poseidon.domain.PhUser;
import cn.offway.Poseidon.properties.QiniuProperties;
import cn.offway.Poseidon.service.PhUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户查询
 *
 * @author wn
 */
@Controller
public class PhUserController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhUserService userService;

    @Autowired
    private QiniuProperties qiniuProperties;

    @RequestMapping("/phUsers.html")
    public String phUsers(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        return "phUsers";
    }

    /**
     * 查询数据
     */
    @ResponseBody
    @RequestMapping("/phUsers-data")
    public Map<String, Object> phUsersData(HttpServletRequest request, String nickname, String unionid, String phone, String isVirtual) {
        String sortCol = request.getParameter("iSortCol_0");
        String sortName = request.getParameter("mDataProp_" + sortCol);
        String sortDir = request.getParameter("sSortDir_0");
        int sEcho = Integer.parseInt(request.getParameter("sEcho"));
        int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
        int iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
        PageRequest pr = new PageRequest(iDisplayStart == 0 ? 0 : iDisplayStart / iDisplayLength, iDisplayLength < 0 ? 9999999 : iDisplayLength, Direction.fromString(sortDir), sortName);
        Page<PhUser> pages = userService.findByPage(nickname.trim(), unionid.trim(), phone.trim(), isVirtual, pr);
        // 为操作次数加1，必须这样做
        int initEcho = sEcho + 1;
        Map<String, Object> map = new HashMap<>();
        map.put("sEcho", initEcho);
        map.put("iTotalRecords", pages.getTotalElements());//数据总条数  
        map.put("iTotalDisplayRecords", pages.getTotalElements());//显示的条数  
        map.put("aData", pages.getContent());//数据集合 
        return map;
    }

    @ResponseBody
    @RequestMapping("/phUsers-save")
    public boolean save(PhUser user) {
        if (user.getId() == null) {
            int maxId = userService.getMaxId();
            if (maxId >= 50000) {
                user.setId(maxId + 1L);
            } else {
                user.setId(50000L);
            }
            user.setCreateTime(new Date());
        } else {
            PhUser userSaved = userService.findOne(user.getId());
            if (userSaved != null) {
                user.setCreateTime(userSaved.getCreateTime());
            }
        }
        userService.save(user);
        return true;
    }

    @ResponseBody
    @RequestMapping("/phUsers-get")
    public PhUser get(long id) {
        return userService.findOne(id);
    }

    @ResponseBody
    @RequestMapping("/phUsers-get-faker")
    public List<PhUser> getFaker() {
        return userService.getFakers();
    }

    @ResponseBody
    @RequestMapping("/phUsers-del")
    @Transactional
    public boolean del(@RequestParam(name = "ids[]") long[] ids) {
        for (long id : ids) {
            userService.delete(id);
        }
        return true;
    }
}
