package cn.offway.Poseidon.service;


import java.util.List;

import cn.offway.Poseidon.domain.PhTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-21 14:46:27 Exp $
 */
public interface PhTemplateService {

    PhTemplate save(PhTemplate phTemplate);

    PhTemplate findOne(Long id);

    void delete(Long id);

    List<PhTemplate> save(List<PhTemplate> entities);

    Page<PhTemplate> list(Pageable pageable);
}
