package cn.offway.Poseidon.controller;

import java.util.List;

import cn.offway.Poseidon.domain.PhAdmin;
import cn.offway.Poseidon.domain.PhResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping
public class IndexController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 登录页面
	 * @return
	 */
	@RequestMapping("/login.html")
	public String login(){
		return "login";
	}
	
	
	
	
	@RequestMapping("/resoures")
	@ResponseBody
	public List<PhResource> resoures(@AuthenticationPrincipal PhAdmin admin){
		return admin.getResources();
	}
	
	
	
	/**
	 * 首页
	 * @return
	 */
	@RequestMapping("/")
	public String index(){
		return "home";
	}
	
	
	
}
