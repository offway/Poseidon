package cn.offway.Poseidon.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.*;
import javax.persistence.criteria.CriteriaBuilder.In;

import cn.offway.Poseidon.domain.PhOrderExpressInfo;
import cn.offway.Poseidon.domain.PhOrderGoods;
import cn.offway.Poseidon.dto.sf.ReqAddOrder;
import cn.offway.Poseidon.service.PhOrderExpressInfoService;
import cn.offway.Poseidon.service.SfExpressService;
import cn.offway.Poseidon.utils.JsonResult;
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

import cn.offway.Poseidon.domain.PhOrderInfo;
import cn.offway.Poseidon.repository.PhOrderGoodsRepository;
import cn.offway.Poseidon.repository.PhOrderInfoRepository;
import cn.offway.Poseidon.service.PhOrderInfoService;


/**
 * 订单Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
@Service
public class PhOrderInfoServiceImpl implements PhOrderInfoService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PhOrderInfoRepository phOrderInfoRepository;
	
	@Autowired
	private SfExpressService sfExpressService;
	
	@Autowired
	private PhOrderExpressInfoService phOrderExpressInfoService;
	
	@Autowired
	private PhOrderGoodsRepository phOrderGoodsRepository;
	
	@Override
	public PhOrderInfo save(PhOrderInfo phOrderInfo){
		return phOrderInfoRepository.save(phOrderInfo);
	}
	
	@Override
	public PhOrderInfo findOne(Long id){
		return phOrderInfoRepository.findOne(id);
	}
	
	@Override
	public String generateOrderNo(String prefix){
		int count = phOrderInfoRepository.hasOrder(prefix);
		if(count == 0){
			phOrderInfoRepository.insert(prefix);
		}
		return phOrderInfoRepository.generateOrderNo(prefix);
	}
	
	@Override
	public PhOrderInfo findByOrderNo(String orderNo){
		return phOrderInfoRepository.findByOrderNo(orderNo);
	}

	@Override
	public Page<PhOrderInfo> findByPage(final String sku, final String isUpload, final String realName, final String position, final String orderNo, final String unionid, final String status, final Long brandId, final String isOffway, final List<Long> brandIds, String users, Pageable page) {
		return phOrderInfoRepository.findAll(new Specification<PhOrderInfo>() {
			
			@Override
			public Predicate toPredicate(Root<PhOrderInfo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				List<Predicate> params = new ArrayList<Predicate>();
				
				if(StringUtils.isNotBlank(orderNo)){
					params.add(criteriaBuilder.equal(root.get("orderNo"), orderNo));
				}
				
				if(StringUtils.isNotBlank(isUpload)){
					params.add(criteriaBuilder.equal(root.get("isUpload"), isUpload));
				}
				
				if(StringUtils.isNotBlank(realName)){
					params.add(criteriaBuilder.like(root.get("realName"), "%"+realName+"%"));
				}
				
				if(StringUtils.isNotBlank(position)){
					params.add(criteriaBuilder.like(root.get("position"), "%"+position+"%"));
				}
				
				if(StringUtils.isNotBlank(unionid)){
					params.add(criteriaBuilder.equal(root.get("unionid"), unionid));
				}
				
				if(StringUtils.isNotBlank(status)){
					params.add(criteriaBuilder.equal(root.get("status"), status));
				}
				
//				if(null != brandId){
//					params.add(criteriaBuilder.equal(root.get("brandId"), brandId));
//				}
				
				if(StringUtils.isNotBlank(isOffway)){
					params.add(criteriaBuilder.equal(root.get("isOffway"), isOffway));
				}

				if (StringUtils.isNotBlank(users)) {
					params.add(criteriaBuilder.like(root.get("users"), "%" + users + "%"));
				}

				if(StringUtils.isNotBlank(sku)){
					Subquery<PhOrderGoods> subquery = criteriaQuery.subquery(PhOrderGoods.class);
					Root<PhOrderGoods> subRoot = subquery.from(PhOrderGoods.class);
					subquery.select(subRoot);
					subquery.where(
							criteriaBuilder.equal(root.get("orderNo"), subRoot.get("orderNo")),
							criteriaBuilder.like(subRoot.get("sku"), "%"+sku+"%")
					);
					params.add(criteriaBuilder.exists(subquery));
				}
				
				if(null == brandId && CollectionUtils.isNotEmpty(brandIds)){
					Subquery<PhOrderGoods> subquery = criteriaQuery.subquery(PhOrderGoods.class);
					Root<PhOrderGoods> subRoot = subquery.from(PhOrderGoods.class);
					subquery.select(subRoot);
					In<Object> in = criteriaBuilder.in(subRoot.get("brandId"));
					for (Object brandId : brandIds) {
						in.value(brandId);
					}
					subquery.where(
							in,
							criteriaBuilder.equal(subRoot.get("orderNo"), root.get("orderNo"))
					);
					params.add(criteriaBuilder.exists(subquery));
				}

				if(null != brandId){
					Subquery<PhOrderGoods> subquery = criteriaQuery.subquery(PhOrderGoods.class);
					Root<PhOrderGoods> subRoot = subquery.from(PhOrderGoods.class);
					subquery.select(subRoot);
					subquery.where(
							criteriaBuilder.equal(subRoot.get("brandId"), brandId),
							criteriaBuilder.equal(subRoot.get("orderNo"), root.get("orderNo"))
					);
					params.add(criteriaBuilder.exists(subquery));
				}
				
				
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
				return null;
			}
		}, page);
	}
	
	
	@Override
	public JsonResult saveOrder(String orderNo){
		/**
		 * 1.修改订单状态 
		 * 2.快递预约上门
		 */
		
		PhOrderInfo phOrderInfo = findByOrderNo(orderNo);
		if("1".equals(phOrderInfo.getStatus())){
			return new JsonResult("500", "订单已发货！", null);
		}
		
		
		PhOrderExpressInfo phOrderExpressInfo = phOrderExpressInfoService.findByOrderNoAndType(orderNo, "0");
		phOrderExpressInfo.setExpressOrderNo(generateOrderNo("SF"));
		
		ReqAddOrder addOrder = new ReqAddOrder();
		addOrder.setD_address(phOrderExpressInfo.getToContent());
		addOrder.setD_contact(phOrderExpressInfo.getToRealName());
		addOrder.setD_tel(phOrderExpressInfo.getToPhone());
		addOrder.setJ_province(phOrderExpressInfo.getFromProvince());
		addOrder.setJ_city(phOrderExpressInfo.getFromCity());
		addOrder.setJ_county(phOrderExpressInfo.getFromCounty());
		addOrder.setJ_address(phOrderExpressInfo.getFromContent());
		addOrder.setJ_contact(phOrderExpressInfo.getFromRealName());
		addOrder.setJ_tel(phOrderExpressInfo.getFromPhone());
		addOrder.setOrder_source("OFFWAY");
		addOrder.setOrder_id(phOrderExpressInfo.getExpressOrderNo());
		addOrder.setPay_method("2");//付款方式：1:寄方付2:收方付3:第三方付
		addOrder.setRemark("");
		addOrder.setSendstarttime("");
		JsonResult result = sfExpressService.addOrder(addOrder);
		if("200".equals(result.getCode())){
			String mailNo = String.valueOf(result.getData());
			phOrderExpressInfo.setMailNo(mailNo);
			phOrderExpressInfo.setStatus("1");//已下单
			phOrderExpressInfoService.save(phOrderExpressInfo);
			phOrderInfo.setStatus("1");
			save(phOrderInfo);
		}
		return result;
		
	}
	
	
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void cancel(String orderNo) throws Exception{
		
		PhOrderInfo phOrderInfo =  findByOrderNo(orderNo);
		phOrderInfo.setStatus("4");
		save(phOrderInfo);
		
		phOrderGoodsRepository.updateStock(orderNo);
	
	}
	
	
}
