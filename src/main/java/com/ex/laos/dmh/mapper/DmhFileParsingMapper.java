package com.ex.laos.dmh.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ex.laos.dmh.dto.DmhEvaporation;
import com.ex.laos.dmh.dto.DmhRainfall;
import com.ex.laos.dmh.dto.DmhWaterlevel;
import com.ex.laos.dmh.dto.DmhEvaporationDownload;
import com.ex.laos.dmh.dto.DmhRainfallDownload;
import com.ex.laos.dmh.dto.DmhWaterlevelDownload;

@Mapper
public interface DmhFileParsingMapper {
	void insertTnEvaporation(DmhEvaporation vo);
	void insertTnRainfall(DmhRainfall vo);
	void insertTnWaterlevel(DmhWaterlevel vo);


	List<Map<String, String>> getDmhTnEvaporationData();
	List<Map<String, String>> getDmhTnRainfallData();
	List<Map<String, String>> getDmhTnWaterlevelData();

	List<DmhEvaporationDownload> getDmhTnEvaporation();
	List<DmhRainfallDownload> getDmhTnRainfall();
	List<DmhWaterlevelDownload> getDmhTnWaterlevel();

}
