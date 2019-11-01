package cn.offway.Poseidon.service;


import java.util.List;

import cn.offway.Poseidon.domain.PhUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 用户信息Service接口
 *
 * @author wn
 * @version $v: 1.0.0, $time:2019-10-30 14:42:36 Exp $
 */
public interface PhUserService {

    PhUser save(PhUser phUser);

    PhUser findOne(Long id);

    void delete(Long id);

    List<PhUser> save(List<PhUser> entities);

    Page<PhUser> findByPage(String nickname, String unionid, String phone, String isVirtual, Pageable page);

    int getMaxId();
}
