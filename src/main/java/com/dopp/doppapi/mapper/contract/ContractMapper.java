package com.dopp.doppapi.mapper.contract;

import com.dopp.doppapi.dto.contract.ContractDto;
import com.dopp.doppapi.dto.contract.ContractListRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ContractMapper {
    List<ContractDto> selectContractList(ContractListRequest request);
}
