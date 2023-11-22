package com.ex.laos.dao;

import java.util.ArrayList;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ex.laos.dto.dam.DamObservationDto;

@Mapper
public interface DamDao {

	Map<String, String> getDamId(String damName);

	void insertDamObservationDtoList(ArrayList<DamObservationDto> list);

	void upsertLastFiveDaysDamObservationDtoList(ArrayList<DamObservationDto> list);
}
