package com.dopp.doppapi.service.sample;

import com.dopp.doppapi.dto.sample.SampleDto;
import com.dopp.doppapi.mapper.sample.SampleMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SampleService {
    private final SampleMapper sampleMapper;

    public SampleService(SampleMapper sampleMapper) {
        this.sampleMapper = sampleMapper;
    }

    public List<SampleDto> selectSample() {
        return sampleMapper.selectSample();
    }
}
