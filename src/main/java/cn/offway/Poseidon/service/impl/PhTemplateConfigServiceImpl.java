package cn.offway.Poseidon.service.impl;

import cn.offway.Poseidon.domain.PhTemplateConfig;
import cn.offway.Poseidon.repository.PhTemplateConfigRepository;
import cn.offway.Poseidon.service.PhTemplateConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;


/**
 * 杂志模板配置Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-21 14:46:27 Exp $
 */
@Service
public class PhTemplateConfigServiceImpl implements PhTemplateConfigService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhTemplateConfigRepository phTemplateConfigRepository;

    @Override
    public PhTemplateConfig save(PhTemplateConfig phTemplateConfig) {
        return phTemplateConfigRepository.save(phTemplateConfig);
    }

    @Override
    public PhTemplateConfig findOne(Long id) {
        return phTemplateConfigRepository.findOne(id);
    }

    @Override
    public void delete(Long id) {
        phTemplateConfigRepository.delete(id);
    }

    @Override
    public Page<PhTemplateConfig> list(Pageable pageable, String pid) {
        return phTemplateConfigRepository.findAll(new Specification<PhTemplateConfig>() {
            @Override
            public Predicate toPredicate(Root<PhTemplateConfig> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> params = new ArrayList<>();
                params.add(cb.equal(root.get("goodsId"), pid));
                Predicate[] predicates = new Predicate[params.size()];
                query.where(params.toArray(predicates));
                return null;
            }
        }, pageable);
    }

    @Override
    public List<PhTemplateConfig> save(List<PhTemplateConfig> entities) {
        return phTemplateConfigRepository.save(entities);
    }

    @Override
    public List<PhTemplateConfig> findByGoodsIdList(Long id){
        return phTemplateConfigRepository.findByGoodsId(id);
    }
}
