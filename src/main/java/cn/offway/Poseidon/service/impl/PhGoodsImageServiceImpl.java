package cn.offway.Poseidon.service.impl;

import java.util.List;

import cn.offway.Poseidon.domain.PhGoodsImage;
import cn.offway.Poseidon.service.PhGoodsImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.offway.Poseidon.repository.PhGoodsImageRepository;


/**
 * 商品图片Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
@Service
public class PhGoodsImageServiceImpl implements PhGoodsImageService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhGoodsImageRepository phGoodsImageRepository;
	
	@Override
	public PhGoodsImage save(PhGoodsImage phGoodsImage){
		return phGoodsImageRepository.save(phGoodsImage);
	}
	
	@Override
	public PhGoodsImage findOne(Long id){
		return phGoodsImageRepository.findOne(id);
	}
	
	@Override
	public void delete(PhGoodsImage phGoodsImage){
		phGoodsImageRepository.delete(phGoodsImage);
	}
	
	@Override
	public void delete(List<PhGoodsImage> phGoodsImages){
		phGoodsImageRepository.delete(phGoodsImages);
	}
	
	@Override
	public int deleteByGoodsIds(List<Long> goodsIds){
		return phGoodsImageRepository.deleteByGoodsIds(goodsIds);
	}
	
	@Override
	public List<PhGoodsImage> save(List<PhGoodsImage> phGoodsImages){
		return phGoodsImageRepository.save(phGoodsImages);
	}
	
	@Override
	public List<PhGoodsImage> findByGoodsId(Long goodsId){
		return phGoodsImageRepository.findByGoodsId(goodsId);
	}
	
}
