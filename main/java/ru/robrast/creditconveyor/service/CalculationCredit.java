package ru.robrast.creditconveyor.service;


import org.springframework.stereotype.Service;
import ru.robrast.creditconveyor.dto.CreditDTO;
import ru.robrast.creditconveyor.dto.EmploymentDTO;
import ru.robrast.creditconveyor.dto.ScoringDataDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("unused")
@Service
public class CalculationCredit {
        private final BigDecimal insuranceForYear = BigDecimal.valueOf(30000);
        public boolean rejection = false;


        public List<CreditDTO> generateCreditDataDTO(ScoringDataDTO request){
            List<CreditDTO> CreditOffers = new ArrayList<>();
            CreditOffers.add(CompletionCreditDataDTO(request.getAmount(), request.getTerm(),request.getIsInsuranceEnabled(),
                    request.getEmployment().getEmploymentStatus(), request.getEmployment().getPosition(), request.getEmployment().getSalary(),
                    request.getMaritalStatus(), request.getDependentAmount(), request.getBirthdate(), request.getGender(),
                    request.getEmployment().getWorkExperienceCurrent(), request.getEmployment().getWorkExperienceTotal()));
            return CreditOffers;
        }
        private CreditDTO CompletionCreditDataDTO(BigDecimal amount, Integer term, boolean isInsuranceEnable, EmploymentDTO.EmploymentStatus employmentStatus,
                                                  EmploymentDTO.Position position, BigDecimal salary, ScoringDataDTO.MaritalStatus maritalStatus,
                                                  Integer dependentAmount, LocalDate birthDate, ScoringDataDTO.Gender gender,
                                                  Integer workExperienceCurrent, Integer workExperienceTotal){
            CreditDTO creditOffer = new CreditDTO();
            BigDecimal loan_rate = BigDecimal.valueOf(12.5);
            creditOffer.setAmount(amount);
            creditOffer.setTerm(term);
            BigDecimal monthlyPercents = loan_rate.divide(new BigDecimal(1200), 6, RoundingMode.UP);
            BigDecimal temporarycoeff = monthlyPercents.add(new BigDecimal(1)).pow(term);
            BigDecimal coeff = (monthlyPercents.multiply(temporarycoeff)).divide(temporarycoeff.subtract(new BigDecimal(1)), 10, RoundingMode.CEILING);
            BigDecimal monthlyPayment = amount.multiply(coeff);
            BigDecimal totalAmount = monthlyPayment.multiply(BigDecimal.valueOf(term));
            if (isInsuranceEnable) {
                BigDecimal insuranceForAllTime = insuranceForYear.multiply(BigDecimal.valueOf(term).divide(BigDecimal.valueOf(12), RoundingMode.UP));
                totalAmount = totalAmount.add(insuranceForAllTime);
            }
            BigDecimal psk = (totalAmount.divide(amount, RoundingMode.HALF_DOWN)).subtract(new BigDecimal(1)).divide(new BigDecimal(term), RoundingMode.HALF_DOWN).multiply(new BigDecimal(1200));
            creditOffer.setMonthlyPayment(monthlyPayment.setScale(2, RoundingMode.UP));
            creditOffer.setPsk(psk);
            if (employmentStatus.equals(EmploymentDTO.EmploymentStatus.UNEMPLOYED))
                loan_rate = loan_rate.add(BigDecimal.valueOf(3));
            else if (employmentStatus.equals(EmploymentDTO.EmploymentStatus.SELF_EMPLOYED))
                loan_rate = loan_rate.add(BigDecimal.valueOf(1));


            if (position.equals(EmploymentDTO.Position.TOP_MANAGER))
                loan_rate = loan_rate.subtract(BigDecimal.valueOf(2));
            else if (position.equals(EmploymentDTO.Position.MIDDLE_MANAGER))
                loan_rate = loan_rate.subtract(BigDecimal.valueOf(4));

            if (amount.compareTo(salary.multiply(BigDecimal.valueOf(20))) > 0)
                rejection = true;

            if (maritalStatus.equals(ScoringDataDTO.MaritalStatus.MARRIED))
                loan_rate = loan_rate.subtract(BigDecimal.valueOf(3));
            else if (maritalStatus.equals(ScoringDataDTO.MaritalStatus.UNMARRIED))
                loan_rate = loan_rate.add(BigDecimal.valueOf(1));

            if(dependentAmount > 1)
                loan_rate = loan_rate.add(BigDecimal.valueOf(1));

            LocalDate now = LocalDate.now();
            if (birthDate.until(now).getYears() < 20)
                rejection = true;
            if (birthDate.until(now).getYears() >= 60)
                rejection = true;


            if (gender.equals(ScoringDataDTO.Gender.FEMALE) && (birthDate.until(now).getYears() <= 60) && (birthDate.until(now).getYears() >= 35))
                loan_rate = loan_rate.subtract(BigDecimal.valueOf(3));
            else if (gender.equals(ScoringDataDTO.Gender.MALE) && (birthDate.until(now).getYears() <= 55) && (birthDate.until(now).getYears() >= 30))
                loan_rate = loan_rate.subtract(BigDecimal.valueOf(3));
            else if (gender.equals(ScoringDataDTO.Gender.NONBINARY))
                loan_rate = loan_rate.add(BigDecimal.valueOf(3));

            if (workExperienceCurrent < 3)
                rejection = true;
            if (workExperienceTotal < 12)
                rejection = true;



            creditOffer.setRate(loan_rate);
            return creditOffer;
        }
}
