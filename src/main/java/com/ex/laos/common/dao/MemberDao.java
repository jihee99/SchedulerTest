package com.ex.laos.common.dao;

import org.apache.ibatis.annotations.Mapper;

import com.ex.laos.common.dto.MemberDto;

@Mapper
public interface MemberDao {

	void insertMember(MemberDto member);

	MemberDto findByUserId(String username);

}
