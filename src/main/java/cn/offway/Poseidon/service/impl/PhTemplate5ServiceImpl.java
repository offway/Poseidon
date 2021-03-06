package cn.offway.Poseidon.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.offway.Poseidon.service.PhTemplate5Service;

import cn.offway.Poseidon.domain.PhTemplate5;
import cn.offway.Poseidon.repository.PhTemplate5Repository;


/**
 * 杂志模版5Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-21 14:46:27 Exp $
 */
@Service
public class PhTemplate5ServiceImpl implements PhTemplate5Service {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhTemplate5Repository phTemplate5Repository;
	
	@Override
	public PhTemplate5 save(PhTemplate5 phTemplate5){
		return phTemplate5Repository.save(phTemplate5);
	}
	
	@Override
	public PhTemplate5 findOne(Long id){
		return phTemplate5Repository.findOne(id);
	}

	@Override
	public void delete(Long id){
		phTemplate5Repository.delete(id);
	}

	@Override
	public List<PhTemplate5> save(List<PhTemplate5> entities){
		return phTemplate5Repository.save(entities);
	}

	@Override
	public void updateVideoUrl(String video, String url){
		phTemplate5Repository.updateVideoUrl(video,url);
	}
}
