package cn.offway.Poseidon.controller;

import cn.offway.Poseidon.service.QiniuService;
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
}
