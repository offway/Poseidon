/**
 * 
 */
package cn.offway.Poseidon.authentication;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.offway.Poseidon.domain.PhAdmin;
import cn.offway.Poseidon.domain.PhResource;
import cn.offway.Poseidon.repository.PhAdminRepository;
import cn.offway.Poseidon.service.PhBrandadminService;
import cn.offway.Poseidon.service.PhResourceService;
import cn.offway.Poseidon.service.PhRoleadminService;
import cn.offway.Poseidon.utils.TreeParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author wn
 *
 */
@Component
@Transactional
public class RbacUserDetailsService implements UserDetailsService {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private PhAdminRepository phAdminRepository;
	
	@Autowired
	private PhResourceService phResourceService;
	
	@Autowired
	private PhBrandadminService phBrandadminService;
	
	@Autowired
	private PhRoleadminService phRoleadminService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.core.userdetails.UserDetailsService#
	 * loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("表单登录用户名:" + username);
		PhAdmin admin = phAdminRepository.findByUsername(username);
		if(null == admin){
			throw new UsernameNotFoundException("用户名不正确");
		}
		//菜单
		List<PhResource> resources = phResourceService.findByAdminId(admin.getId());
		admin.setResources(TreeParser.getTreeList(0L, resources));
		
		//权限
		Set<String> urls = new HashSet<>();
		for (PhResource phResource : resources) {
			urls.add(phResource.getLink());
		}
		
		admin.setUrls(urls);
		
		//用户品牌
		admin.setBrandIds(phBrandadminService.findBrandIdByAdminId(admin.getId())); 
		
		//用户角色
		admin.setRoleIds(phRoleadminService.findRoleIdByAdminId(admin.getId()));
		
		
		return admin;
	}

}
