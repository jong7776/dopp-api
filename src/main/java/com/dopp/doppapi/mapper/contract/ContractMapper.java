package com.dopp.doppapi.mapper.contract;

import com.dopp.doppapi.dto.contract.ContractDto;
import com.dopp.doppapi.dto.contract.ContractListRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ContractMapper {
    List<ContractDto> selectContractList(ContractListRequest request);
    void insertContractList(@Param("list") List<ContractDto> list, @Param("loginId") String loginId);
    void insertContract(ContractDto contractDto);
    void updateContract(ContractDto contractDto);
    void deleteContractList(@Param("list") List<Long> contractIds);
    void deleteContractByYear(ContractDto contractDto);
}
