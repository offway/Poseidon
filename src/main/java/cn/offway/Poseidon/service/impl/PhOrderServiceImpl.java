package cn.offway.Poseidon.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import cn.offway.Poseidon.service.PhOrderService;

import cn.offway.Poseidon.domain.PhOrder;
import cn.offway.Poseidon.repository.PhOrderRepository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;


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

	@Override
	public Page<PhOrder> findAllByPage(final String userId, final String orderNo, final String status, Pageable page){
		return phOrderRepository.findAll(new Specification<PhOrder>() {
			@Override
			public Predicate toPredicate(Root<PhOrder> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				List<Predicate> params = new ArrayList<Predicate>();
				if(StringUtils.isNotBlank(orderNo)){
					params.add(criteriaBuilder.equal(root.get("orderNo"), orderNo));
				}
				if(StringUtils.isNotBlank(status)){
					params.add(criteriaBuilder.equal(root.get("status"), status));
				}
				if(StringUtils.isNotBlank(userId)){
					params.add(criteriaBuilder.equal(root.get("userId"), userId));
				}
				Predicate[] predicates = new Predicate[params.size()];
				criteriaQuery.where(params.toArray(predicates));
				return null;
			}
		},page);
	}
}
