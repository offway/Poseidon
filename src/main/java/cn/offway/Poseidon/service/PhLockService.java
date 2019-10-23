package cn.offway.Poseidon.service;


import java.util.List;

import cn.offway.Poseidon.domain.PhLock;

/**
 * 解锁条件表Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-21 14:46:27 Exp $
 */
public interface PhLockService{

    PhLock save(PhLock phLock);
	
    PhLock findOne(Long id);

    void delete(Long id);

    List<PhLock> save(List<PhLock> entities);

    PhLock findByGoodsIdAndTemplateTypeAndTemplateId(Long goodsId, String type, Long templateId);
}
