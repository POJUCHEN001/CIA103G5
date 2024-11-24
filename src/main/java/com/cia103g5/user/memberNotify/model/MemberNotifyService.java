package com.cia103g5.user.memberNotify.model;

import com.cia103g5.user.member.model.MemberVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberNotifyService {

    @Autowired
    private MemberNotifyRepository memberNotifyDao;

    public void save(MemberNotifyVo memberNotify) {
        memberNotifyDao.save(memberNotify);
    }

    public List<MemberNotifyVo> findByMember(MemberVO member) {
        return memberNotifyDao.findByMember(member);
    }

    public Page<MemberNotifyVo> findByMemberPages(MemberVO member, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "notifiedTime"));
        return memberNotifyDao.findByMember(member, pageable);
    }

}
