package ru.robrast.creditconveyor.service;

import org.springframework.stereotype.Service;
import ru.robrast.creditconveyor.dto.LoanApplicationRequestDTO;
import ru.robrast.creditconveyor.dto.LoanOfferDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
@Service
public class Listof4xLoanOffer {

    private static LoanApplicationRequestDTO request1;

    public static List<LoanOfferDTO> generateLoanOffer(LoanApplicationRequestDTO request){
        request1 = request;
        List<LoanOfferDTO> offers = new ArrayList<>();
        offers.add(CompletionLoanOffer(false , false, request1.getAmount(), request1.getTerm()));
        offers.add(CompletionLoanOffer(false , true, request1.getAmount(), request1.getTerm()));
        offers.add(CompletionLoanOffer(true , false, request1.getAmount(), request1.getTerm()));
        offers.add(CompletionLoanOffer(true , true, request1.getAmount(), request1.getTerm()));
        return offers;
    }
    private static LoanOfferDTO CompletionLoanOffer(boolean isInsuranceEnabled, boolean isSalaryClient, BigDecimal amount, Integer term){
        LoanOfferDTO loanOffer = new LoanOfferDTO();
        double loan_rate = 13.5;
        if (isSalaryClient)
            loan_rate -= 1;
        else loan_rate +=1;
        if (isInsuranceEnabled)
            loan_rate -=1.5;
        else loan_rate +=1.5;
        loanOffer.setRequestedAmount(amount);
        loanOffer.setTerm(term);
        loanOffer.setIsInsuranceEnabled(isInsuranceEnabled);
        loanOffer.setIsSalaryClient(isSalaryClient);
        loanOffer.setRate(BigDecimal.valueOf(loan_rate));
        loanOffer.setTotalAmount(loanOffer.getRequestedAmount().multiply(BigDecimal.valueOf(loan_rate)));
        loanOffer.setMonthlyPayment(loanOffer.getTotalAmount().divide(BigDecimal.valueOf(term), RoundingMode.UP));
        return loanOffer;
    }




}
