package cn.offway.Poseidon.service.impl;

import cn.offway.Poseidon.domain.PhWardrobe;
import cn.offway.Poseidon.service.PhWardrobeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.offway.Poseidon.repository.PhWardrobeRepository;


/**
 * 衣柜Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
@Service
public class PhWardrobeServiceImpl implements PhWardrobeService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhWardrobeRepository phWardrobeRepository;
	
	@Override
	public PhWardrobe save(PhWardrobe phWardrobe){
		return phWardrobeRepository.save(phWardrobe);
	}
	
	@Override
	public PhWardrobe findOne(Long id){
		return phWardrobeRepository.findOne(id);
	}
}
