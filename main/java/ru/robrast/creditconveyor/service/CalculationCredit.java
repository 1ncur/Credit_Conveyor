package ru.robrast.creditconveyor.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.robrast.creditconveyor.dto.CreditDTO;
import ru.robrast.creditconveyor.dto.EmploymentDTO;
import ru.robrast.creditconveyor.dto.PaymentScheduleElementDTO;
import ru.robrast.creditconveyor.dto.ScoringDataDTO;
import ru.robrast.creditconveyor.exception.RejectionException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("unused")
@Slf4j
@Service
public class CalculationCredit {

        public CreditDTO generateCreditDataDTO(ScoringDataDTO request){
            CreditDTO creditOffer = new CreditDTO();
            BigDecimal loan_rate = BigDecimal.valueOf(15.5);
            BigDecimal amount = request.getAmount();
            if (request.getEmployment().getEmploymentStatus().equals(EmploymentDTO.EmploymentStatus.UNEMPLOYED))
                loan_rate = loan_rate.add(BigDecimal.valueOf(3));
            else if (request.getEmployment().getEmploymentStatus().equals(EmploymentDTO.EmploymentStatus.SELF_EMPLOYED))
                loan_rate = loan_rate.add(BigDecimal.valueOf(1));


            if (request.getEmployment().getPosition().equals(EmploymentDTO.Position.TOP_MANAGER))
                loan_rate = loan_rate.subtract(BigDecimal.valueOf(2));
            else if (request.getEmployment().getPosition().equals(EmploymentDTO.Position.MIDDLE_MANAGER))
                loan_rate = loan_rate.subtract(BigDecimal.valueOf(4));

            if (amount.compareTo(request.getEmployment().getSalary().multiply(BigDecimal.valueOf(20))) > 0) {
                log.debug("искл");
                throw new RejectionException("Вам отказано в кредите");
            }

            if (request.getMaritalStatus().equals(ScoringDataDTO.MaritalStatus.MARRIED))
                loan_rate = loan_rate.subtract(BigDecimal.valueOf(3));
            else if (request.getMaritalStatus().equals(ScoringDataDTO.MaritalStatus.UNMARRIED))
                loan_rate = loan_rate.add(BigDecimal.valueOf(1));

            if(request.getDependentAmount() > 1)
                loan_rate = loan_rate.add(BigDecimal.valueOf(1));

            LocalDate now = LocalDate.now();
            if (request.getBirthdate().until(now).getYears() < 20)
                throw new RejectionException("Вам отказано в кредите");
            if (request.getBirthdate().until(now).getYears() >= 60)
                throw new RejectionException("Вам отказано в кредите");


            if (request.getGender().equals(ScoringDataDTO.Gender.FEMALE) && (request.getBirthdate().until(now).getYears() <= 60) && (request.getBirthdate().until(now).getYears() >= 35))
                loan_rate = loan_rate.subtract(BigDecimal.valueOf(3));
            else if (request.getGender().equals(ScoringDataDTO.Gender.MALE) && (request.getBirthdate().until(now).getYears() <= 55) && (request.getBirthdate().until(now).getYears() >= 30))
                loan_rate = loan_rate.subtract(BigDecimal.valueOf(3));
            else if (request.getGender().equals(ScoringDataDTO.Gender.NONBINARY))
                loan_rate = loan_rate.add(BigDecimal.valueOf(3));

            if (request.getEmployment().getWorkExperienceCurrent() < 3)
                throw new RejectionException("Вам отказано в кредите");
            if (request.getEmployment().getWorkExperienceTotal() < 12)
                throw new RejectionException("Вам отказано в кредите");

            BigDecimal monthlyPercents = loan_rate.divide(new BigDecimal(1200), 6, RoundingMode.UP);
            BigDecimal temporarycoeff = monthlyPercents.add(new BigDecimal(1)).pow(request.getTerm());
            BigDecimal coeff = (monthlyPercents.multiply(temporarycoeff)).divide(temporarycoeff.subtract(new BigDecimal(1)), 10, RoundingMode.CEILING);
            BigDecimal monthlyPayment = amount.multiply(coeff);
            BigDecimal totalAmount = monthlyPayment.multiply(BigDecimal.valueOf(request.getTerm()));
            if (request.getIsInsuranceEnabled()) {
                BigDecimal insuranceForYear = monthlyPayment.multiply(BigDecimal.valueOf(0.24));
                BigDecimal insuranceForAllTime = insuranceForYear.multiply(BigDecimal.valueOf(request.getTerm()).divide(BigDecimal.valueOf(12), RoundingMode.UP));
                totalAmount = totalAmount.add(insuranceForAllTime);
            }
            BigDecimal psk = (totalAmount.divide(request.getAmount(), RoundingMode.HALF_DOWN)).subtract(new BigDecimal(1)).divide(new BigDecimal(request.getTerm()), RoundingMode.HALF_DOWN).multiply(new BigDecimal(1200));

            creditOffer.setPaymentSchedule(generatePaymentSchedule(request, monthlyPayment, loan_rate, amount));
            creditOffer.setRate(loan_rate);
            creditOffer.setAmount(request.getAmount());
            creditOffer.setTerm(request.getTerm());
            creditOffer.setMonthlyPayment(monthlyPayment.setScale(2, RoundingMode.UP));
            creditOffer.setPsk(psk);
            creditOffer.setIsSalaryClient(request.getIsSalaryClient());
            creditOffer.setIsInsuranceEnabled(request.getIsInsuranceEnabled());


            return creditOffer;
        }
    private List<PaymentScheduleElementDTO> generatePaymentSchedule(ScoringDataDTO request, BigDecimal monthlyPayment, BigDecimal rate, BigDecimal amount){
            rate = rate.multiply(BigDecimal.valueOf(0.01));
            List<PaymentScheduleElementDTO> PaymentSchedule = new ArrayList<>();
            Integer term = request.getTerm();
            BigDecimal remainingDebt = amount;
            LocalDate date = LocalDate.now().plusMonths(1);
            for (Integer i = 1; i <= term; i++){
                PaymentScheduleElementDTO paymentScheduleElementDTO = new PaymentScheduleElementDTO();
                date = date.plusMonths(1);
                BigDecimal interestPayment = remainingDebt.multiply(rate).multiply(BigDecimal.valueOf(date.lengthOfMonth()).divide(BigDecimal.valueOf(date.lengthOfYear()), 10, RoundingMode.CEILING));
                BigDecimal debtPayment = monthlyPayment.subtract(interestPayment);
                remainingDebt = remainingDebt.subtract(debtPayment);
                paymentScheduleElementDTO.setTotalPayment(monthlyPayment.setScale(2, RoundingMode.UP));
                paymentScheduleElementDTO.setNumber(i);
                paymentScheduleElementDTO.setDate(date);
                paymentScheduleElementDTO.setDebtPayment(debtPayment.setScale(2, RoundingMode.UP));
                paymentScheduleElementDTO.setRemainingDebt(remainingDebt.setScale(2, RoundingMode.UP));
                paymentScheduleElementDTO.setInterestPayment(interestPayment.setScale(2, RoundingMode.UP));
                PaymentSchedule.add(paymentScheduleElementDTO);
            }

            return PaymentSchedule;
        }

}
