package cn.offway.Poseidon.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.offway.Poseidon.domain.PhAdmin;
import cn.offway.Poseidon.service.PhAdminService;
import cn.offway.Poseidon.service.PhBrandService;
import cn.offway.Poseidon.service.PhBrandadminService;
import cn.offway.Poseidon.service.PhRoleadminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * 用户管理
 * @author wn
 *
 */
@Controller
public class UserController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private PhAdminService phAdminService;
	
	@Autowired
	private PhRoleadminService phRoleadminService;
	
	@Autowired
	private PhBrandService phBrandService;
	
	@Autowired
	private PhBrandadminService phBrandadminService;

	@RequestMapping("/users.html")
	public String users(ModelMap map){
		map.addAttribute("brands", phBrandService.findAll());
		return "users";
	}
	
	/**
	 * 查询数据
	 * @param request
	 * @param code
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/users-data")
	public Map<String, Object> usersData(HttpServletRequest request,String username,String nickname){
		
		String sortCol = request.getParameter("iSortCol_0");
		String sortName = request.getParameter("mDataProp_"+sortCol);
		String sortDir = request.getParameter("sSortDir_0");
		int sEcho = Integer.parseInt(request.getParameter("sEcho"));
		int iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
		int iDisplayLength  = Integer.parseInt(request.getParameter("iDisplayLength"));
		Page<PhAdmin> pages = phAdminService.findByPage(username,nickname, new PageRequest(iDisplayStart==0?0:iDisplayStart/iDisplayLength, iDisplayLength<0?9999999:iDisplayLength,Direction.fromString(sortDir),sortName));
		 // 为操作次数加1，必须这样做  
        int initEcho = sEcho + 1;  
        Map<String, Object> map = new HashMap<>();
		map.put("sEcho", initEcho);  
        map.put("iTotalRecords", pages.getTotalElements());//数据总条数  
        map.put("iTotalDisplayRecords", pages.getTotalElements());//显示的条数  
        map.put("aData", pages.getContent());//数据集合 
		return map;
	}
	
	/**
	 * 删除用户
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@PostMapping("/users-delete")
	public boolean delete(String ids){
		try {
			phAdminService.deleteAdmin(ids);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除用户异常,用户id:{}",ids,e);
			return false;
		}
	}
	
	@ResponseBody
	@PostMapping("/users-save")
	public boolean save(
			PhAdmin phAdmin,
			@RequestParam(required = false, value="roles[]") Long[] roles,
			@RequestParam(required = false) String brandIds){
		try {
			phAdminService.save(phAdmin, roles,"".equals(brandIds)?null:brandIds.split(","));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("保存用户异常,phRole:{},resources:{}",JSON.toJSONString(phAdmin,SerializerFeature.WriteMapNullValue),roles,e);
			return false;
		}
	}
	
	@ResponseBody
	@GetMapping("/users-one")
	public Map<String, Object> usersOne(Long id){
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("user", phAdminService.findOne(id));
		resultMap.put("roleIds", phRoleadminService.findRoleIdByAdminId(id));
		resultMap.put("brandIds", phBrandadminService.findBrandIdByAdminId(id));

		return resultMap;
	}
	
	@ResponseBody
	@PostMapping("/users-resetPwd")
	public boolean resetPwd(Long id){
		try {
			phAdminService.resetPwd(id);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("重置密码异常,id:{}",id,e);
			return false;
		}
	}
	
	@ResponseBody
	@PostMapping("/users-editPwd")
	public boolean editPwd(@AuthenticationPrincipal PhAdmin admin,String password,String newpassword){
		try {
			return phAdminService.editPwd(admin.getId(), password, newpassword);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("修改密码异常,id:{}",admin.getId(),e);
			return false;
		}
	}
	
	
}
