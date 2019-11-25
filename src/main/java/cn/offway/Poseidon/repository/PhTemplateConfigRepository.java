package cn.offway.Poseidon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.offway.Poseidon.domain.PhTemplateConfig;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 杂志模板配置Repository接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-21 14:46:27 Exp $
 */
public interface PhTemplateConfigRepository extends JpaRepository<PhTemplateConfig,Long>,JpaSpecificationExecutor<PhTemplateConfig> {

	/** 此处写一些自定义的方法 **/
	List<PhTemplateConfig> findByGoodsId(Long id);

	@Query(nativeQuery=true,value="select sort from ph_template_config where goods_id = ?1 ORDER BY sort DESC LIMIT 1")
	Long findByMaxSort(Long id);
}
