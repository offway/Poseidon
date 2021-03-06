package cn.offway.Poseidon.service.impl;

import java.util.List;

import cn.offway.Poseidon.domain.PhBrandadmin;
import cn.offway.Poseidon.service.PhBrandadminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.offway.Poseidon.repository.PhBrandadminRepository;


/**
 * Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-01 11:26:00 Exp $
 */
@Service
public class PhBrandadminServiceImpl implements PhBrandadminService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhBrandadminRepository phBrandadminRepository;
	
	@Override
	public PhBrandadmin save(PhBrandadmin phBrandadmin){
		return phBrandadminRepository.save(phBrandadmin);
	}
	
	@Override
	public PhBrandadmin findOne(Long id){
		return phBrandadminRepository.findOne(id);
	}
	
	@Override
	public List<Long> findBrandIdByAdminId(Long adminId){
		return phBrandadminRepository.findBrandIdByAdminId(adminId);
	}
}
