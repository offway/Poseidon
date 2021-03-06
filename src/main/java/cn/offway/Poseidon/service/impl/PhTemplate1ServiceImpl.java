package cn.offway.Poseidon.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.Poseidon.service.PhTemplate1Service;

import cn.offway.Poseidon.domain.PhTemplate1;
import cn.offway.Poseidon.repository.PhTemplate1Repository;


/**
 * 杂志模版1Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-21 14:46:27 Exp $
 */
@Service
public class PhTemplate1ServiceImpl implements PhTemplate1Service {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhTemplate1Repository phTemplate1Repository;
	
	@Override
	public PhTemplate1 save(PhTemplate1 phTemplate1){
		return phTemplate1Repository.save(phTemplate1);
	}
	
	@Override
	public PhTemplate1 findOne(Long id){
		return phTemplate1Repository.findOne(id);
	}

	@Override
	public void delete(Long id){
		phTemplate1Repository.delete(id);
	}

	@Override
	public List<PhTemplate1> save(List<PhTemplate1> entities){
		return phTemplate1Repository.save(entities);
	}
}
