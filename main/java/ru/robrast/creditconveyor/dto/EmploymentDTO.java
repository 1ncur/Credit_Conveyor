package ru.robrast.creditconveyor.dto;

import lombok.Data;

import java.math.BigDecimal;
@SuppressWarnings("unused")
@Data
public class EmploymentDTO {
    private EmploymentStatus employmentStatus;
    private String employerINN;
    private BigDecimal salary;
    private Position position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;

    public enum EmploymentStatus {
        EMPLOYED,
        UNEMPLOYED,
        BUSINESS_OWNER,
        SELF_EMPLOYED
    }

    public enum Position{
        MANAGER,
        MIDDLE_MANAGER,
        TOP_MANAGER

    }
}

