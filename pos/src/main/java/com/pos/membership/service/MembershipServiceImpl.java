package com.pos.membership.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.pos.membership.dao.MembershipDao;
import com.pos.membership.model.Membership;

@Service("membershipService")
public class MembershipServiceImpl implements MembershipService{

	@Autowired
	private MembershipDao membershipDao;

	public Membership addMemberSave(Membership membership) {
		return membershipDao.addMemberSave(membership);
	}
}
