package cn.offway.Poseidon.repository;

import cn.offway.Poseidon.domain.PhShowImageFilter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 返图筛选Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-04-04 15:18:00 Exp $
 */
public interface PhShowImageFilterRepository extends JpaRepository<PhShowImageFilter,Long>,JpaSpecificationExecutor<PhShowImageFilter> {

	int countByShowImage(String showImage);
}
