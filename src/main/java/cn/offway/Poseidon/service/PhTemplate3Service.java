package cn.offway.Poseidon.service;


import java.util.List;

import cn.offway.Poseidon.domain.PhTemplate3;

/**
 * 杂志模版3Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-21 14:46:27 Exp $
 */
public interface PhTemplate3Service{

    PhTemplate3 save(PhTemplate3 phTemplate3);
	
    PhTemplate3 findOne(Long id);

    void delete(Long id);

    List<PhTemplate3> save(List<PhTemplate3> entities);
}
