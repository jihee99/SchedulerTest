package com.ex.laos.common.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ex.laos.common.dto.MailTokenDto;

@Mapper
public interface MailDao {

	void insertPasswordUpdateHistory(MailTokenDto mailTokenDto);

	void updatePasswordByUsername(String username, String password);

}
