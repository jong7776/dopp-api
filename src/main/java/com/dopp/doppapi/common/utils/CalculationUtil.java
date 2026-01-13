package com.dopp.doppapi.common.utils;

import com.dopp.doppapi.dto.common.MonthlyAmountDto;

import java.util.List;
import java.util.function.Supplier;

public class CalculationUtil {

    public static <T extends MonthlyAmountDto> void addTotalRow(List<T> list, Supplier<T> newInstanceSupplier, String nameFieldSetterName) {
        if (list == null || list.isEmpty()) {
            return;
        }

        long totalAmount = 0;
        long[] monthlyTotals = new long[12];

        for (T dto : list) {
            totalAmount += dto.getTotalAmount() != null ? dto.getTotalAmount() : 0;
            monthlyTotals[0] += dto.getM01() != null ? dto.getM01() : 0;
            monthlyTotals[1] += dto.getM02() != null ? dto.getM02() : 0;
            monthlyTotals[2] += dto.getM03() != null ? dto.getM03() : 0;
            monthlyTotals[3] += dto.getM04() != null ? dto.getM04() : 0;
            monthlyTotals[4] += dto.getM05() != null ? dto.getM05() : 0;
            monthlyTotals[5] += dto.getM06() != null ? dto.getM06() : 0;
            monthlyTotals[6] += dto.getM07() != null ? dto.getM07() : 0;
            monthlyTotals[7] += dto.getM08() != null ? dto.getM08() : 0;
            monthlyTotals[8] += dto.getM09() != null ? dto.getM09() : 0;
            monthlyTotals[9] += dto.getM10() != null ? dto.getM10() : 0;
            monthlyTotals[10] += dto.getM11() != null ? dto.getM11() : 0;
            monthlyTotals[11] += dto.getM12() != null ? dto.getM12() : 0;
        }

        T totalDto = newInstanceSupplier.get();
        
        // Reflection or specific interface method to set name could be used, 
        // but since the name field varies (contractName vs expenseName), 
        // we might need a consumer or just handle it outside if we want to be purely generic without reflection.
        // However, the user asked to refactor addTotalRow which sets "합계".
        // Let's use a Consumer to set the name.
        
        totalDto.setTotalAmount(totalAmount);
        totalDto.setM01(monthlyTotals[0]);
        totalDto.setM02(monthlyTotals[1]);
        totalDto.setM03(monthlyTotals[2]);
        totalDto.setM04(monthlyTotals[3]);
        totalDto.setM05(monthlyTotals[4]);
        totalDto.setM06(monthlyTotals[5]);
        totalDto.setM07(monthlyTotals[6]);
        totalDto.setM08(monthlyTotals[7]);
        totalDto.setM09(monthlyTotals[8]);
        totalDto.setM10(monthlyTotals[9]);
        totalDto.setM11(monthlyTotals[10]);
        totalDto.setM12(monthlyTotals[11]);

        list.add(totalDto);
    }
    
    public static <T extends MonthlyAmountDto> void addTotalRow(List<T> list, Supplier<T> newInstanceSupplier, java.util.function.Consumer<T> nameSetter) {
        if (list == null || list.isEmpty()) {
            return;
        }

        long totalAmount = 0;
        long[] monthlyTotals = new long[12];

        for (T dto : list) {
            totalAmount += dto.getTotalAmount() != null ? dto.getTotalAmount() : 0;
            monthlyTotals[0] += dto.getM01() != null ? dto.getM01() : 0;
            monthlyTotals[1] += dto.getM02() != null ? dto.getM02() : 0;
            monthlyTotals[2] += dto.getM03() != null ? dto.getM03() : 0;
            monthlyTotals[3] += dto.getM04() != null ? dto.getM04() : 0;
            monthlyTotals[4] += dto.getM05() != null ? dto.getM05() : 0;
            monthlyTotals[5] += dto.getM06() != null ? dto.getM06() : 0;
            monthlyTotals[6] += dto.getM07() != null ? dto.getM07() : 0;
            monthlyTotals[7] += dto.getM08() != null ? dto.getM08() : 0;
            monthlyTotals[8] += dto.getM09() != null ? dto.getM09() : 0;
            monthlyTotals[9] += dto.getM10() != null ? dto.getM10() : 0;
            monthlyTotals[10] += dto.getM11() != null ? dto.getM11() : 0;
            monthlyTotals[11] += dto.getM12() != null ? dto.getM12() : 0;
        }

        T totalDto = newInstanceSupplier.get();
        nameSetter.accept(totalDto);
        
        totalDto.setTotalAmount(totalAmount);
        totalDto.setM01(monthlyTotals[0]);
        totalDto.setM02(monthlyTotals[1]);
        totalDto.setM03(monthlyTotals[2]);
        totalDto.setM04(monthlyTotals[3]);
        totalDto.setM05(monthlyTotals[4]);
        totalDto.setM06(monthlyTotals[5]);
        totalDto.setM07(monthlyTotals[6]);
        totalDto.setM08(monthlyTotals[7]);
        totalDto.setM09(monthlyTotals[8]);
        totalDto.setM10(monthlyTotals[9]);
        totalDto.setM11(monthlyTotals[10]);
        totalDto.setM12(monthlyTotals[11]);

        list.add(totalDto);
    }
}
