package com.tanglover.mall.api;

import com.tanglover.mall.bean.Member;
import com.tanglover.mall.service.MemberService;
import com.tanglover.mall.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author TangXu
 * @create 2020-04-05 15:24
 * @description:
 */
@RestController
public class MemberApi extends BaseApi {

    @Autowired
    MemberService memberService;

    @RequestMapping("/member/register")
    public Map<String, Object> register(HttpServletRequest request) {
        Member member = (Member) HttpUtils.parseObject(request, Member.class);
        return returnSuccess(memberService.addMember(member));
    }

    @RequestMapping("/member/update")
    public Map<String, Object> update(HttpServletRequest request) {
        Member member = (Member) HttpUtils.parseObject(request, Member.class);
        return returnSuccess(memberService.updateMember(member));
    }

}