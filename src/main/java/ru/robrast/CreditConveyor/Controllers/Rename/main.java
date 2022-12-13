package ru.robrast.CreditConveyor.Controllers.Rename;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.robrast.CreditConveyor.Controllers.DTOs.LoanApplicationRequest;
import ru.robrast.CreditConveyor.Controllers.Prescoring;

import java.util.ArrayList;
import java.util.List;


@RestController
public class main {
    @PostMapping("/conveyor/offers")
    public List<ResponseEntity> generateOffers(@RequestBody LoanApplicationRequest request) {
        try {
            Prescoring.Verify_data(request);
            return Listof4xLoanOffer.GenerateLoanOffer(request);
        } catch (Exception e) {
            System.out.println(e);
            List<ResponseEntity> exception = new ArrayList<>();
            exception.add(new ResponseEntity(String.valueOf(e).split(":")[1], HttpStatus.BAD_REQUEST));
            return exception;
        }
    }
}

