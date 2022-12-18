package ru.robrast.creditconveyor.service;

import org.springframework.stereotype.Service;
import ru.robrast.creditconveyor.dto.CreditDTO;
import ru.robrast.creditconveyor.dto.ScoringDataDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("unused")
@Service
public class CalculationCredit {
        private final BigDecimal insuranceForYear = BigDecimal.valueOf(30000);

        public List<CreditDTO> generateCreditDataDTO(ScoringDataDTO request){
            List<CreditDTO> CreditOffers = new ArrayList<>();
            CreditOffers.add(CompletionCreditDataDTO(request.getAmount(), request.getTerm(),request.getIsInsuranceEnabled()));
            return CreditOffers;
        }
        private CreditDTO CompletionCreditDataDTO(BigDecimal amount, Integer term, boolean isInsuranceEnable){
            CreditDTO creditOffer = new CreditDTO();
            BigDecimal loan_rate = BigDecimal.valueOf(12.5);
            creditOffer.setAmount(amount);
            creditOffer.setTerm(term);
            creditOffer.setRate(loan_rate);
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
            return creditOffer;
        }
}