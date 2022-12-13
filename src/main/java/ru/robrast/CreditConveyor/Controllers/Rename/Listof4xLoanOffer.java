package ru.robrast.CreditConveyor.Controllers.Rename;

import ru.robrast.CreditConveyor.Controllers.DTOs.LoanApplicationRequest;
import ru.robrast.CreditConveyor.Controllers.DTOs.LoanOffer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class Listof4xLoanOffer {

    private static LoanApplicationRequest request1;

    public static List GenerateLoanOffer(LoanApplicationRequest request){
        request1 = request;
        List<LoanOffer> offers = new ArrayList<>();
        offers.add(CompletionLoanOffer(false , false, request1.getAmount(), request1.getTerm()));
        offers.add(CompletionLoanOffer(false , true, request1.getAmount(), request1.getTerm()));
        offers.add(CompletionLoanOffer(true , false, request1.getAmount(), request1.getTerm()));
        offers.add(CompletionLoanOffer(true , true, request1.getAmount(), request1.getTerm()));
        return offers;
    }
    private static LoanOffer CompletionLoanOffer(boolean isInsuranceEnabled, boolean isSalaryClient, BigDecimal amount, Integer term){
        LoanOffer loanOffer = new LoanOffer();
        Double initial_loan_rate = 13.5;
        Double loan_rate = initial_loan_rate;
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
