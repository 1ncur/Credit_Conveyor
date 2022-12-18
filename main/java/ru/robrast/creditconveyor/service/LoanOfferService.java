package ru.robrast.creditconveyor.service;

import org.springframework.stereotype.Service;
import ru.robrast.creditconveyor.dto.LoanApplicationRequestDTO;
import ru.robrast.creditconveyor.dto.LoanOfferDTO;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
@Service
public class LoanOfferService {
    private final BigDecimal salaryClientDiscount = BigDecimal.valueOf(1.0);
    private final BigDecimal insuranceEnableDiscount = BigDecimal.valueOf(1.5);


    public List<LoanOfferDTO> generateLoanOffer(LoanApplicationRequestDTO request){
        List<LoanOfferDTO> offers = new ArrayList<>();
        offers.add(CompletionLoanOffer(false, false, request.getAmount(), request.getTerm()));
        offers.add(CompletionLoanOffer(false, true, request.getAmount(), request.getTerm()));
        offers.add(CompletionLoanOffer(true, false, request.getAmount(), request.getTerm()));
        offers.add(CompletionLoanOffer(true, true, request.getAmount(), request.getTerm()));
        return offers;
    }
    private LoanOfferDTO CompletionLoanOffer(boolean isInsuranceEnabled, boolean isSalaryClient, BigDecimal amount, Integer term){
        LoanOfferDTO loanOffer = new LoanOfferDTO();
        BigDecimal loan_rate = BigDecimal.valueOf(12.5);
        if (isSalaryClient) loan_rate = loan_rate.subtract(insuranceEnableDiscount);
        else loan_rate = loan_rate.add(insuranceEnableDiscount);
        if (isInsuranceEnabled) loan_rate = loan_rate.subtract(salaryClientDiscount);
        else  loan_rate = loan_rate.add(salaryClientDiscount);
        loanOffer.setRequestedAmount(amount);
        loanOffer.setTerm(term);
        loanOffer.setIsInsuranceEnabled(isInsuranceEnabled);
        loanOffer.setIsSalaryClient(isSalaryClient);
        loanOffer.setRate(loan_rate);
        BigDecimal monthlyPercents = loan_rate.divide(new BigDecimal(1200), 6, RoundingMode.UP);
        BigDecimal temporarycoeff = monthlyPercents.add(new BigDecimal(1)).pow(term);
        BigDecimal coeff = (monthlyPercents.multiply(temporarycoeff)).divide(temporarycoeff.subtract(new BigDecimal(1)), 10, RoundingMode.CEILING);
        BigDecimal monthlyPayment = amount.multiply(coeff);
        BigDecimal totalAmount = monthlyPayment.multiply(BigDecimal.valueOf(term));
        loanOffer.setTotalAmount(totalAmount.setScale(2, RoundingMode.UP));
        loanOffer.setMonthlyPayment(monthlyPayment.setScale(2, RoundingMode.UP));
        return loanOffer;
    }
}
