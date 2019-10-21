package cn.offway.Poseidon.service;


import java.util.List;

import cn.offway.Poseidon.domain.PhTemplate4;

/**
 * 杂志模版4Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-21 14:46:27 Exp $
 */
public interface PhTemplate4Service{

    PhTemplate4 save(PhTemplate4 phTemplate4);
	
    PhTemplate4 findOne(Long id);

    void delete(Long id);

    List<PhTemplate4> save(List<PhTemplate4> entities);
}
