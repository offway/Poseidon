package cn.offway.Poseidon.controller;

import cn.offway.Poseidon.domain.PhBanner;
import cn.offway.Poseidon.domain.PhBannerHistory;
import cn.offway.Poseidon.properties.QiniuProperties;
import cn.offway.Poseidon.repository.PhBannerHistoryRepository;
import cn.offway.Poseidon.service.PhBannerHistoryService;
import cn.offway.Poseidon.service.PhBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

@Controller
@RequestMapping
public class BannerController {
    @Autowired
    private PhBannerService bannerService;
    @Autowired
    private QiniuProperties qiniuProperties;
    @Autowired
    private PhBannerHistoryService bannerHistoryService;
    @Autowired
    private PhBannerHistoryRepository bannerHistoryRepository;

    @RequestMapping("/banner.html")
    public String index(ModelMap map) {
        map.addAttribute("qiniuUrl", qiniuProperties.getUrl());
        return "banner_index";
    }

    @RequestMapping("/banner_list")
    @ResponseBody
    public Map<String, Object> getAll(HttpServletRequest request, String id, String remark) {
        int sEcho = Integer.parseInt(request.getParameter("sEcho"));
        int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
        int iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "status"), new Sort.Order("sort"));
        Page<PhBanner> pages = bannerService.findAll(id, remark, new PageRequest(iDisplayStart == 0 ? 0 : iDisplayStart / iDisplayLength, iDisplayLength < 0 ? 9999999 : iDisplayLength, sort));
        int initEcho = sEcho + 1;
        Map<String, Object> map = new HashMap<>();
        map.put("sEcho", initEcho);
        map.put("iTotalRecords", pages.getTotalElements());//数据总条数
        map.put("iTotalDisplayRecords", pages.getTotalElements());//显示的条数
        map.put("aData", pages.getContent());//数据集合
        return map;
    }

    private void saveHistory(PhBanner banner) {
        PhBannerHistory history = new PhBannerHistory();
        history.setBanner(banner.getRemark());
        history.setBannerId(banner.getRedirectId());
        history.setBannerImg(banner.getBanner());
        history.setBeginTime(banner.getBeginTime());
        history.setEndTime(banner.getEndTime());
        history.setCreateTime(new Date());
        bannerHistoryService.save(history);
    }

    @Transactional
    @RequestMapping("/banner_save")
    @ResponseBody
    public boolean save(PhBanner banner) {
        if (banner.getId() == null) {
            banner.setStatus("0");
            banner.setSort(null);
            if (null == banner.getRedirectId()) {
                banner.setRedirectId("");
            }
            banner.setCreateTime(new Date());
            saveHistory(banner);
        } else {
            PhBanner bannerSaved = bannerService.findOne(banner.getId());
            banner.setStatus(bannerSaved.getStatus());
            banner.setSort(bannerSaved.getSort());
            banner.setCreateTime(bannerSaved.getCreateTime());
            if (Math.abs(banner.getBeginTime().compareTo(bannerSaved.getBeginTime())) + Math.abs(banner.getEndTime().compareTo(bannerSaved.getEndTime())) != 0) {
                saveHistory(banner);
            }
        }
        bannerService.save(banner);
        return true;
    }

    @RequestMapping("/banner_listHistoryRank")
    @ResponseBody
    public List<PhBannerHistory> listHistoryRank() {
        return bannerHistoryRepository.listRank();
    }

    @RequestMapping("/banner_listHistorySub")
    @ResponseBody
    public List<PhBannerHistory> listHistorySub(String id) {
        return bannerHistoryService.findList(id);
    }

    @RequestMapping("/banner_get")
    @ResponseBody
    public PhBanner get(@RequestParam("id") Long id) {
        return bannerService.findOne(id);
    }

    @RequestMapping("/banner_del")
    @ResponseBody
    public boolean delete(@RequestParam("ids[]") Long[] ids) {
        for (long id : ids) {
            bannerService.delete(id);
        }
        return true;
    }

    @ResponseBody
    @RequestMapping("/banner_top")
    public boolean top(Long id, Long sort) {
        PhBanner banner = bannerService.findOne(id);
        if (banner != null) {
            bannerService.resort(sort);
            banner.setSort(sort);
            bannerService.save(banner);
        }
        return true;
    }

    @RequestMapping("/banner_up")
    @ResponseBody
    public boolean up(Long id) {
        PhBanner banner = bannerService.findOne(id);
        long latest = bannerService.getMaxSort();
        banner.setStatus("1");
        banner.setSort(latest + 1);
        bannerService.save(banner);
        return true;
    }

    @RequestMapping("/banner_down")
    @ResponseBody
    public boolean down(Long id) {
        PhBanner banner = bannerService.findOne(id);
        banner.setStatus("0");
        banner.setSort(null);
        bannerService.save(banner);
        return true;
    }
}
