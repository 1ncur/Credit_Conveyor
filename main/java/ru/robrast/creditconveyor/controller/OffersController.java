package ru.robrast.creditconveyor.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.robrast.creditconveyor.dto.LoanApplicationRequestDTO;
import ru.robrast.creditconveyor.dto.LoanOfferDTO;
import ru.robrast.creditconveyor.service.LoanOfferService;
import ru.robrast.creditconveyor.service.PrescoringService;


import java.util.List;

@SuppressWarnings("unused")
@RestController
public class OffersController   {

    private final LoanOfferService loanOfferService;
    private final PrescoringService prescoringService;

    @Autowired
    public OffersController(LoanOfferService loanOfferService, PrescoringService prescoringService){
        this.loanOfferService = loanOfferService;
        this.prescoringService = prescoringService;
    }
    @PostMapping("/conveyor/offers")
    public List<LoanOfferDTO> generateOffers(@RequestBody LoanApplicationRequestDTO request) {
        return loanOfferService.generateLoanOffer(request);
    }
 /*   public void Warn(@RequestBody LoanApplicationRequestDTO request){
        prescoringService.verifyData(request);
    }
  */
}
