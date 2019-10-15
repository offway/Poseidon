package cn.offway.Poseidon.repository;

import cn.offway.Poseidon.domain.PhFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 反馈表Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-09-25 15:10:52 Exp $
 */
public interface PhFeedbackRepository extends JpaRepository<PhFeedback,Long>,JpaSpecificationExecutor<PhFeedback> {

	/** 此处写一些自定义的方法 **/
}
