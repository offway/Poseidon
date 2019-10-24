package cn.offway.Poseidon.repository;

import cn.offway.Poseidon.domain.PhTemplate2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * 杂志模版2Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-21 14:46:27 Exp $
 */
public interface PhTemplate2Repository extends JpaRepository<PhTemplate2, Long>, JpaSpecificationExecutor<PhTemplate2> {
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM `ph_template_2` WHERE (`pid` = ?1)")
    void deleteByPid(Long pid);
}
