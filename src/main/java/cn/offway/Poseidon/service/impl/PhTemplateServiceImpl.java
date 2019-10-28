package cn.offway.Poseidon.service.impl;

import cn.offway.Poseidon.domain.PhTemplate;
import cn.offway.Poseidon.repository.PhTemplateRepository;
import cn.offway.Poseidon.service.PhTemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-21 14:46:27 Exp $
 */
@Service
public class PhTemplateServiceImpl implements PhTemplateService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhTemplateRepository phTemplateRepository;

    @Override
    public PhTemplate save(PhTemplate phTemplate) {
        return phTemplateRepository.save(phTemplate);
    }

    @Override
    public PhTemplate findOne(Long id) {
        return phTemplateRepository.findOne(id);
    }

    @Override
    public void delete(Long id) {
        phTemplateRepository.delete(id);
    }

    @Override
    public Page<PhTemplate> list(Pageable pageable) {
        return phTemplateRepository.findAll(pageable);
    }

    @Override
    public List<PhTemplate> save(List<PhTemplate> entities) {
        return phTemplateRepository.save(entities);
    }
}
