package cn.offway.Poseidon.repository;

import java.util.List;

import javax.transaction.Transactional;

import cn.offway.Poseidon.domain.PhRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2018-10-15 16:49:00 Exp $
 */
public interface PhRoleRepository extends JpaRepository<PhRole,Long>,JpaSpecificationExecutor<PhRole> {

	@Transactional
	@Modifying
	@Query(nativeQuery=true,value="delete from ph_role where id in(?1)")
	int deleteByRoleIds(List<Long> ids);
}
