package ru.robrast.creditconveyor.controller.rename;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.robrast.creditconveyor.dto.LoanApplicationRequestDTO;
import ru.robrast.creditconveyor.dto.LoanOfferDTO;
import ru.robrast.creditconveyor.service.Listof4xLoanOffer;

import java.util.List;

@SuppressWarnings("unused")
@RestController
public class OffersController {
    @PostMapping("/conveyor/offers")
    public List<LoanOfferDTO> generateOffers(@RequestBody LoanApplicationRequestDTO request) {
            return Listof4xLoanOffer.generateLoanOffer(request);

    }
}


