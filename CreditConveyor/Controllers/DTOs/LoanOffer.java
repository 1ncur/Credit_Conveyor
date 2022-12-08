package ru.robrast.CreditConveyor.Controllers.DTOs;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LoanOffer {
    private Long applicationId;
    private BigDecimal requestedAmount;
    private BigDecimal totalAmount;
    private Integer term;
    private BigDecimal monthlyPayment;
    private BigDecimal rate;
    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;

}
