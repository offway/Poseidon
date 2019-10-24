package cn.offway.Poseidon.service;


import cn.offway.Poseidon.domain.PhTemplate2;

import java.util.List;

/**
 * 杂志模版2Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-21 14:46:27 Exp $
 */
public interface PhTemplate2Service {

    PhTemplate2 save(PhTemplate2 phTemplate2);

    PhTemplate2 findOne(Long id);

    void delete(Long id);

    void deleteByPid(Long pid);

    List<PhTemplate2> save(List<PhTemplate2> entities);
}
