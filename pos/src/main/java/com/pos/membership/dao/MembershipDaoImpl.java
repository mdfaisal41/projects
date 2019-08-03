package com.pos.membership.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.aes.protection.CipherUtils;
import com.pos.common.RemoveNull;
import com.pos.hr.model.EmployeeInformation;
import com.pos.membership.model.Membership;

@Repository("MembershipDao")
public class MembershipDaoImpl implements MembershipDao{

	private SimpleJdbcCall simpleJdbcCall;
	private JdbcTemplate jdbcTemplate;
	CipherUtils oCipherUtils = new CipherUtils();
	@Autowired
	private DataSource dataSource;
	RemoveNull oRemoveNull = new RemoveNull();
	
	
	public Membership addMemberSave(Membership membership) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		Membership oAddMemberSave = new Membership();
		try {
			simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("PRO_ADD_MEMBER_SAVE");

			Map<String, Object> inParamMap = new HashMap<String, Object>();

			inParamMap.put("P_MEMBER_ID", oCipherUtils.decrypt(oRemoveNull.nullRemove(membership.getEncMemberId())));
			inParamMap.put("P_MEMBER_NAME", membership.getMemberName());
			inParamMap.put("P_KNOWN_AS", membership.getKnownAs());
			inParamMap.put("P_FATHERS_NAME", membership.getFathersName());
			inParamMap.put("P_MOTHERS_NAME", membership.getMothersName());
			inParamMap.put("P_GENDER_ID", membership.getGenderId());
			inParamMap.put("P_DATE_OF_BIRTH", membership.getDateOfBirth());
			inParamMap.put("P_RELIGION_ID", membership.getReligionId());
			inParamMap.put("P_MARITAL_STATUS_ID", membership.getMaritalStatusId());
			inParamMap.put("P_NID_NUMBER", membership.getNationalIdNo());
			inParamMap.put("P_JOINING_DATE", membership.getJoiningDate());
			inParamMap.put("P_CONTACT_NO", membership.getContactNo());
			inParamMap.put("P_EMAIL_ADDRESS", membership.getEmailAddress());
			inParamMap.put("P_ENABLED_YN", membership.getEnabledYN());
			inParamMap.put("P_UPDATE_BY", membership.getUpdateBy());
			
			Map<String, Object> outParamMap = simpleJdbcCall.execute(new MapSqlParameterSource().addValues(inParamMap));

			oAddMemberSave.setMessage(oRemoveNull.nullRemove((String) outParamMap.get("P_MESSAGE")));
			oAddMemberSave.setMessageCode(oRemoveNull.nullRemove((String) outParamMap.get("P_MESSAGE_CODE")));
			oAddMemberSave.setEncMemberId(oRemoveNull.nullRemove(oCipherUtils.encrypt((String) outParamMap.get("P_NEW_MEMBER_ID"))));
			
			//System.out.println(" P_NEW_EMP_ID "+ (String) outParamMap.get("P_NEW_EMP_ID"));

		} catch (Exception ex) {
			ex.printStackTrace();
			oAddMemberSave.setMessage("Error Occured!!!");
			oAddMemberSave.setMessageCode("0000");
		}
		return oAddMemberSave;
	}
	
	
	
	public List<Membership> getMemberList(Membership membership) {
		jdbcTemplate = new JdbcTemplate(dataSource);
		List<Membership> oMembershipList = new ArrayList<Membership>();

		NamedParameterJdbcTemplate npjt = new NamedParameterJdbcTemplate(jdbcTemplate);

		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("SELECT MEMBER_ID, ");
		sBuilder.append("MEMBER_NAME, ");
		sBuilder.append("KNOWN_AS, ");
		sBuilder.append("MEMBER_NO, ");
		sBuilder.append("CONTACT_NO, ");
		sBuilder.append("PRESENT_ADDRESS ");
		sBuilder.append("FROM MEMBERSHIP ");
		sBuilder.append("WHERE ENABLED_YN = 'Y' ");
		sBuilder.append("ORDER BY MEMBER_NO ");

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		 //System.out.println(sBuilder);

		List<Map<String, Object>> rows = npjt.queryForList(sBuilder.toString(), paramSource);

		for (@SuppressWarnings("rawtypes")
		Map row : rows) {
			Membership oMembership = new Membership();

			oMembership.setEncMemberId(oCipherUtils.encrypt(oRemoveNull.nullRemove(String.valueOf(row.get("MEMBER_ID")))));
			oMembership.setMemberId(oRemoveNull.nullRemove(String.valueOf(row.get("MEMBER_ID"))));
			oMembership.setMemberName(oRemoveNull.nullRemove(String.valueOf(row.get("MEMBER_NAME"))));
			oMembership.setKnownAs(oRemoveNull.nullRemove(String.valueOf(row.get("KNOWN_AS"))));
			oMembership.setMemberNo(oRemoveNull.nullRemove(String.valueOf(row.get("MEMBER_NO"))));
			oMembership.setContactNo(oRemoveNull.nullRemove(String.valueOf(row.get("CONTACT_NO"))));
			oMembership.setAddress(oRemoveNull.nullRemove(String.valueOf(row.get("PRESENT_ADDRESS"))));
			oMembershipList.add(oMembership);
		}
		return oMembershipList;
	}
	
	
	
}
