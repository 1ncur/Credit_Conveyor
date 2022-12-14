package ru.robrast.creditconveyor.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;
@Data
public class LoanApplicationRequestDTO {
    private BigDecimal amount;
    private Integer term;
    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
    private LocalDate birthdate;
    private String passportSeries;
    private String passportNumber;
}
