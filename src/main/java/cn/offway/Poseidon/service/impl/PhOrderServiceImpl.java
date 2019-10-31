package cn.offway.Poseidon.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.Poseidon.service.PhOrderService;

import cn.offway.Poseidon.domain.PhOrder;
import cn.offway.Poseidon.repository.PhOrderRepository;


/**
 * 电子刊购买订单Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-31 11:38:54 Exp $
 */
@Service
public class PhOrderServiceImpl implements PhOrderService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhOrderRepository phOrderRepository;
	
	@Override
	public PhOrder save(PhOrder phOrder){
		return phOrderRepository.save(phOrder);
	}
	
	@Override
	public PhOrder findOne(Long id){
		return phOrderRepository.findOne(id);
	}

	@Override
	public void delete(Long id){
		phOrderRepository.delete(id);
	}

	@Override
	public List<PhOrder> save(List<PhOrder> entities){
		return phOrderRepository.save(entities);
	}
}
