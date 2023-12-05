package com.ex.laos.common.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ex.laos.common.dto.MailTokenDto;

@Mapper
public interface MailDao {

	void updateDisablePasswordChangeRequest(MailTokenDto mailTokenDto);

	void insertPasswordUpdateHistory(MailTokenDto mailTokenDto);

	MailTokenDto selectTokenByPswdChgId(String token) ;

	void updatePasswordByUsername(String username, String password);


}
