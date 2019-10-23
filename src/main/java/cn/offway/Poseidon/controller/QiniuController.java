package cn.offway.Poseidon.controller;

import cn.offway.Poseidon.domain.PhTemplate4;
import cn.offway.Poseidon.service.PhTemplate4Service;
import cn.offway.Poseidon.service.PhTemplate5Service;
import cn.offway.Poseidon.service.PhTemplateService;
import cn.offway.Poseidon.service.QiniuService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cn.offway.Poseidon.properties.QiniuProperties;

/**
 * 七牛
 * @author wn
 *
 */
@RestController
@RequestMapping("/qiniu")
public class QiniuController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private QiniuProperties qiniuProperties;
	
	@Autowired
	private QiniuService qiniuService;

	@Autowired
	private PhTemplate5Service template5Service;

	@GetMapping("/token")
	public String token(){
		return qiniuService.token();
	}

	@GetMapping("/token_video")
	public String tokenVideo(@RequestParam(name = "isVideo", required = false) String isVideo){
		return qiniuService.tokenVideo(isVideo);
	}
	
	@PostMapping("/delete")
	public boolean delete(String url){
		return qiniuService.qiniuDelete(url.replace(qiniuProperties.getUrl()+"/", ""));
	}

	@ResponseBody
	@PostMapping("/avthumb")
	public String avthumb(@RequestBody String content){
		logger.info("七牛视频处理结果："+content);
		JSONObject jsonObject = JSON.parseObject(content);
		JSONArray jsonArray =  jsonObject.getJSONArray("items");
		String key = jsonArray.getJSONObject(0).getString("key");
		System.out.println(key);
		String inputKey = jsonObject.getString("inputKey");
		template5Service.updateVideoUrl("http://qiniu.offway.cn/"+inputKey,"http://qiniu.offway.cn/"+key);
		return "success";
	}
}
