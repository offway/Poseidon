package cn.offway.Poseidon.service.impl;

import cn.offway.Poseidon.domain.PhUser;
import cn.offway.Poseidon.repository.PhUserRepository;
import cn.offway.Poseidon.service.PhUserService;
import org.apache.commons.lang3.StringUtils;
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
import java.util.Optional;


/**
 * 用户信息Service接口实现
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-30 14:42:36 Exp $
 */
@Service
public class PhUserServiceImpl implements PhUserService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PhUserRepository phUserRepository;

    @Override
    public PhUser save(PhUser phUser) {
        return phUserRepository.save(phUser);
    }

    @Override
    public PhUser findOne(Long id) {
        return phUserRepository.findOne(id);
    }

    @Override
    public void delete(Long id) {
        phUserRepository.delete(id);
    }

    @Override
    public List<PhUser> save(List<PhUser> entities) {
        return phUserRepository.save(entities);
    }

    @Override
    public int getMaxId() {
        Optional<Integer> ret = phUserRepository.getMax();
        return ret.isPresent() ? ret.get() : 0;
    }

    @Override
    public List<PhUser> getFakers() {
        return phUserRepository.findAll(new Specification<PhUser>() {
            @Override
            public Predicate toPredicate(Root<PhUser> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> params = new ArrayList<Predicate>();
                params.add(cb.greaterThanOrEqualTo(root.get("id"), 50000));
                Predicate[] predicates = new Predicate[params.size()];
                query.where(params.toArray(predicates));
                return null;
            }
        });
    }

    @Override
    public Page<PhUser> findByPage(final String nickname, final String unionid, final String phone, String isVirtual, Pageable page) {
        return phUserRepository.findAll(new Specification<PhUser>() {
            @Override
            public Predicate toPredicate(Root<PhUser> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> params = new ArrayList<Predicate>();

                if (StringUtils.isNotBlank(nickname)) {
                    params.add(criteriaBuilder.like(root.get("nickname"), "%" + nickname + "%"));
                }

                if (StringUtils.isNotBlank(phone)) {
                    params.add(criteriaBuilder.equal(root.get("phone"), phone));
                }

                if (StringUtils.isNotBlank(unionid)) {
                    params.add(criteriaBuilder.equal(root.get("unionid"), unionid));
                }

                if (StringUtils.isNotBlank(isVirtual)) {
                    if ("1".equals(isVirtual)) {
                        params.add(criteriaBuilder.greaterThanOrEqualTo(root.get("id"), 50000));
                    } else {
                        params.add(criteriaBuilder.lessThan(root.get("id"), 50000));
                    }
                }
                Predicate[] predicates = new Predicate[params.size()];
                criteriaQuery.where(params.toArray(predicates));
                return null;
            }
        }, page);
    }
}
