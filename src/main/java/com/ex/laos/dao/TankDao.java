package com.ex.laos.dao;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TankDao {

	Map<String, BigDecimal> getTankInputParametersByDamId(String damId);

}
