package com.dopp.doppapi.mapper.sample;

import com.dopp.doppapi.dto.sample.SampleDto;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface SampleMapper {
    List<SampleDto> selectSample();
}

