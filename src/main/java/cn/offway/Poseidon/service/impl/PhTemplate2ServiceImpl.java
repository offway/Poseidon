package cn.offway.Poseidon.service.impl;

import cn.offway.Poseidon.domain.PhTemplate2;
import cn.offway.Poseidon.repository.PhTemplate2Repository;
import cn.offway.Poseidon.service.PhTemplate2Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;


/**
 * 杂志模版2Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-21 14:46:27 Exp $
 */
@Service
public class PhTemplate2ServiceImpl implements PhTemplate2Service {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhTemplate2Repository phTemplate2Repository;

    @Override
    public PhTemplate2 save(PhTemplate2 phTemplate2) {
        return phTemplate2Repository.save(phTemplate2);
    }

    @Override
    public PhTemplate2 findOne(Long id) {
        return phTemplate2Repository.findOne(id);
    }

    @Override
    public void delete(Long id) {
        phTemplate2Repository.delete(id);
    }

    @Override
    public void deleteByPid(Long pid) {
        phTemplate2Repository.deleteByPid(pid);
    }

    @Override
    public List<PhTemplate2> findByPid(Long pid) {
        return phTemplate2Repository.findAll(new Specification<PhTemplate2>() {
            @Override
            public Predicate toPredicate(Root<PhTemplate2> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> params = new ArrayList<>();
                params.add(cb.equal(root.get("pid"), pid));
                Predicate[] predicates = new Predicate[params.size()];
                query.where(params.toArray(predicates));
                return null;
            }
        });
    }

    @Override
    public List<PhTemplate2> save(List<PhTemplate2> entities) {
        return phTemplate2Repository.save(entities);
    }
}
