package ru.robrast.CreditConveyor.Controllers.DTOs;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Employment {
    private Enum employmentStatus;
    private String employerINN;
    private BigDecimal salary;
    private Enum position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;

}
