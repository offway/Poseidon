package cn.offway.Poseidon.service;


import cn.offway.Poseidon.domain.PhTemplateConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 杂志模板配置Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-21 14:46:27 Exp $
 */
public interface PhTemplateConfigService {

    PhTemplateConfig save(PhTemplateConfig phTemplateConfig);

    PhTemplateConfig findOne(Long id);

    void delete(Long id);

    List<PhTemplateConfig> save(List<PhTemplateConfig> entities);

    Page<PhTemplateConfig> list(Pageable pageable, String pid);
}
