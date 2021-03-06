package cn.offway.Poseidon.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.CriteriaBuilder.In;

import cn.offway.Poseidon.domain.PhGoodsStock;
import cn.offway.Poseidon.domain.PhOrderGoods;
import cn.offway.Poseidon.service.PhGoodsStockService;
import cn.offway.Poseidon.service.PhOrderGoodsService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.offway.Poseidon.service.PhOrderInfoService;
import cn.offway.Poseidon.domain.PhOrderInfo;
import cn.offway.Poseidon.repository.PhGoodsStockRepository;


/**
 * 商品库存Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
@Service
public class PhGoodsStockServiceImpl implements PhGoodsStockService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhGoodsStockRepository phGoodsStockRepository;
	
	@Autowired
	private PhOrderGoodsService phOrderGoodsService;
	
	@Autowired
	private PhOrderInfoService phOrderInfoService;
	
	@Override
	public PhGoodsStock save(PhGoodsStock phGoodsStock){
		return phGoodsStockRepository.save(phGoodsStock);
	}
	
	@Override
	public PhGoodsStock findOne(Long id){
		return phGoodsStockRepository.findOne(id);
	}
	
	@Override
	public String findImage(String color,Long goodsId){
		return phGoodsStockRepository.findImage(color, goodsId);
	}
	
	@Override
	public int countByGoodsIdAndColorAndSize(Long goodsId,String color,String size){
		return phGoodsStockRepository.countByGoodsIdAndColorAndSize(goodsId, color, size);
	}
	
	@Override
	public int updateImage(Long goodsId,String color,String image){
		return phGoodsStockRepository.updateImage(goodsId, color, image);
	}
	
	@Override
	public  int deleteByGoodsIds(List<Long> goodsIds){
		return phGoodsStockRepository.deleteByGoodsIds(goodsIds);
	}
	
	@Override
	public int deleteByIds(List<Long> ids){
		return phGoodsStockRepository.deleteByIds(ids);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
	public boolean updateStock(String orderNo) throws Exception{
		List<PhOrderGoods> orderGoods =  phOrderGoodsService.findByOrderNo(orderNo);
		int i = 0;
		for (PhOrderGoods phOrderGoods : orderGoods) {
			i += phGoodsStockRepository.updateStock(phOrderGoods.getGoodsId(),phOrderGoods.getColor(),phOrderGoods.getSize());
		}
		if(i==orderGoods.size()){
			PhOrderInfo phOrderInfo = phOrderInfoService.findByOrderNo(orderNo);
			phOrderInfo.setStatus("3");
			phOrderInfo.setReceiptTime(new Date());
			phOrderInfoService.save(phOrderInfo);
			return true;
		}
		throw new Exception("加库存失败,orderNo="+orderNo);
		
	}
	
	@Override
	public Page<PhGoodsStock> findByPage(final String sku,final Long brandId,final String brandName,final Long goodsId,final String goodsName,
			final String isOffway,final String color,final String size,final List<Long> brandIds,Pageable page){
		return phGoodsStockRepository.findAll(new Specification<PhGoodsStock>() {
			
			@Override
			public Predicate toPredicate(Root<PhGoodsStock> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				List<Predicate> params = new ArrayList<Predicate>();
				
				if(StringUtils.isNotBlank(brandName)){
					params.add(criteriaBuilder.like(root.get("brandName"), "%"+brandName+"%"));
				}
				
				if(StringUtils.isNotBlank(sku)){
					params.add(criteriaBuilder.like(root.get("sku"), "%"+sku+"%"));
				}
				
				if(CollectionUtils.isNotEmpty(brandIds)){
					In<Object> in = criteriaBuilder.in(root.get("brandId"));
					for (Object brandId : brandIds) {
						in.value(brandId);
					}
					params.add(in);
				}
				
				
				if(null != brandId){
					params.add(criteriaBuilder.equal(root.get("brandId"), brandId));
				}
				
				if(StringUtils.isNotBlank(color)){
					params.add(criteriaBuilder.equal(root.get("color"), color));
				}
				
				if(StringUtils.isNotBlank(size)){
					params.add(criteriaBuilder.equal(root.get("size"), size));
				}
				
				if(null !=goodsId){
					params.add(criteriaBuilder.equal(root.get("goodsId"), goodsId));
				}
				
				if(StringUtils.isNotBlank(isOffway)){
					params.add(criteriaBuilder.equal(root.get("isOffway"), isOffway));
				}
				
				if(StringUtils.isNotBlank(goodsName)){
					params.add(criteriaBuilder.like(root.get("goodsName"), "%"+goodsName+"%"));
				}
				
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
				return null;
			}
		}, page);
	}
	
	
}
