package cn.offway.Poseidon.repository;

import cn.offway.Poseidon.domain.PhBannerHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Banner 历史Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-10 14:00:40 Exp $
 */
public interface PhBannerHistoryRepository extends JpaRepository<PhBannerHistory, Long>, JpaSpecificationExecutor<PhBannerHistory> {
    @Query(nativeQuery = true, value = "SELECT count(`banner_id`) as banner_id,`banner`,`banner_id` as id,`banner_img`,`create_time`,`begin_time`,`end_time` FROM ph_banner_history group by banner_id order by count(`banner_id`) desc")
    List<PhBannerHistory> listRank();
}
