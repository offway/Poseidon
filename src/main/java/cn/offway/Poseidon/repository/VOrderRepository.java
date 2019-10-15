package cn.offway.Poseidon.repository;

import cn.offway.Poseidon.domain.VOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.lang.String;


/**
 * VIEWRepository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-02-12 11:26:00 Exp $
 */
public interface VOrderRepository extends JpaRepository<VOrder,Long>,JpaSpecificationExecutor<VOrder> {

	VOrder findByOrderNo(String orderNo);
}
