package com.ex.laos.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DamDao {

	Map<String, String> getDamId(String damName);

}
