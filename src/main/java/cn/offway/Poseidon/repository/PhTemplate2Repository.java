package cn.offway.Poseidon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.offway.Poseidon.domain.PhTemplate2;

/**
 * 杂志模版2Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-21 14:46:27 Exp $
 */
public interface PhTemplate2Repository extends JpaRepository<PhTemplate2,Long>,JpaSpecificationExecutor<PhTemplate2> {

	/** 此处写一些自定义的方法 **/
}
