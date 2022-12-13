package ru.robrast.CreditConveyor.Controllers.DTOs;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ScoringData {
    private BigDecimal amount;
    private Integer term;
    private String firstName;
    private String lastName;
    private String middleName;
    private Enum gender;
    private LocalDate birthdate;
    private String passportSeries;
    private String passportNumber;
    private LocalDate passportIssueDate;
    private String passportIssueBranch;
    private Enum maritalStatus;
    private Integer dependentAmount;
    private Employment employment;
    private String account;
    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;
}
