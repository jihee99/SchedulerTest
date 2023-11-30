package com.ex.laos.common.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ex.laos.common.dto.MemberFormDto;

@Mapper
public interface MemberDao {

	void save(MemberFormDto member);

	MemberFormDto findByUserId(String username);

}
