package cn.offway.Poseidon.service;


import java.util.List;

import cn.offway.Poseidon.domain.PhOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 电子刊购买订单Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-31 11:38:54 Exp $
 */
public interface PhOrderService{

    PhOrder save(PhOrder phOrder);
	
    PhOrder findOne(Long id);

    void delete(Long id);

    List<PhOrder> save(List<PhOrder> entities);

    Page<PhOrder> findAllByPage(String userId, String orderNo, String status, Pageable page);
}
