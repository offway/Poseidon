package cn.offway.Poseidon.service.impl;

import cn.offway.Poseidon.domain.PhLock;
import cn.offway.Poseidon.repository.PhLockRepository;
import cn.offway.Poseidon.service.PhLockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 解锁条件表Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-21 14:46:27 Exp $
 */
@Service
public class PhLockServiceImpl implements PhLockService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhLockRepository phLockRepository;

    @Override
    public PhLock save(PhLock phLock) {
        return phLockRepository.save(phLock);
    }

    @Override
    public PhLock findOne(Long id) {
        return phLockRepository.findOne(id);
    }

    @Override
    public void delete(Long id) {
        phLockRepository.delete(id);
    }

    @Override
    public List<PhLock> save(List<PhLock> entities) {
        return phLockRepository.save(entities);
    }

    @Override
    public PhLock findByGoodsIdAndTemplateTypeAndTemplateId(Long goodsId, String type, Long templateId) {
        return phLockRepository.findByGoodsIdAndTemplateTypeAndTemplateId(goodsId, type, templateId);
    }

    @Override
    public List<PhLock> findAllByGoodsId(Long id) {
        return phLockRepository.findByGoodsId(id);
    }

    @Override
    public PhLock findByGoodsIdAndTemplateTypeAndConfigId(Long goodsId, String type, Long configId) {
        return phLockRepository.findByGoodsIdAndTemplateTypeAndPid(goodsId, type, configId);
    }
}
