package cn.offway.Poseidon.repository;

import cn.offway.Poseidon.domain.PhUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * 用户信息Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-30 14:42:36 Exp $
 */
public interface PhUserRepository extends JpaRepository<PhUser, Long>, JpaSpecificationExecutor<PhUser> {
    @Query(nativeQuery = true, value = "SELECT max(id) as id FROM ph_user")
    Optional<Integer> getMax();
}
