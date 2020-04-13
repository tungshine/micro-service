package com.tanglover.mall.service;

import com.tanglover.mall.bean.Member;
import com.tanglover.mall.service.mapper.MemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author TangXu
 * @create 2020-04-05 14:46
 * @description: 会员
 */
@Service
public class MemberService {

    @Autowired
    MemberMapper memberMapper;

    public int addMember(Member member) {
        int add = memberMapper.add(member);
        return add;
    }

    public int updateMember(Member member) {
        int update = memberMapper.update(member);
        return update;
    }
}