package com.ex.laos.common.dao;

import java.util.Map;
import java.util.UUID;

import org.apache.ibatis.annotations.Mapper;

import com.ex.laos.common.dto.MemberDto;

@Mapper
public interface MemberDao {

	void insertMember(MemberDto member);

	MemberDto findByUserId(String username);

	void insertPasswordUpdateHistory(Map<String, String> data);

	void updatePasswordByUsername(String username, String password);

}
