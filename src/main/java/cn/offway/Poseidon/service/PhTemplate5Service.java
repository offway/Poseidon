package cn.offway.Poseidon.service;


import java.util.List;

import cn.offway.Poseidon.domain.PhTemplate5;

/**
 * 杂志模版5Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-21 14:46:27 Exp $
 */
public interface PhTemplate5Service{

    PhTemplate5 save(PhTemplate5 phTemplate5);
	
    PhTemplate5 findOne(Long id);

    void delete(Long id);

    List<PhTemplate5> save(List<PhTemplate5> entities);

    void updateVideoUrl(String video, String url);
}
