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
    private final BigDecimal salaryClientDiscount = BigDecimal.valueOf(1);
    private final BigDecimal insuranceEnableDiscount = BigDecimal.valueOf(1.5);


    public List<LoanOfferDTO> generateLoanOffer(LoanApplicationRequestDTO request){
        List<LoanOfferDTO> offers = new ArrayList<>();
        offers.add(CompletionLoanOffer(false , false, request.getAmount(), request.getTerm()));
        offers.add(CompletionLoanOffer(false , true, request.getAmount(), request.getTerm()));
        offers.add(CompletionLoanOffer(true , false, request.getAmount(), request.getTerm()));
        offers.add(CompletionLoanOffer(true , true, request.getAmount(), request.getTerm()));
        return offers;
    }
    private LoanOfferDTO CompletionLoanOffer(boolean isInsuranceEnabled, boolean isSalaryClient, BigDecimal amount, Integer term){
        LoanOfferDTO loanOffer = new LoanOfferDTO();
        BigDecimal loan_rate = BigDecimal.valueOf(13.5);
        if (isSalaryClient)
            loan_rate = loan_rate.subtract(insuranceEnableDiscount);
        else loan_rate = loan_rate.add(insuranceEnableDiscount);
        if (isInsuranceEnabled)
            loan_rate = loan_rate.subtract(salaryClientDiscount);
        else loan_rate = loan_rate.add(salaryClientDiscount);
        loanOffer.setRequestedAmount(amount);
        loanOffer.setTerm(term);
        loanOffer.setIsInsuranceEnabled(isInsuranceEnabled);
        loanOffer.setIsSalaryClient(isSalaryClient);
        loanOffer.setRate(loan_rate);
        loanOffer.setTotalAmount(loanOffer.getRequestedAmount().multiply(loan_rate));
        loanOffer.setMonthlyPayment(loanOffer.getTotalAmount().divide(BigDecimal.valueOf(term), RoundingMode.UP));
        return loanOffer;
    }




}
