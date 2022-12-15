package ru.robrast.creditconveyor.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.robrast.creditconveyor.dto.LoanApplicationRequestDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
@Service
public class PrescoringService {
    public void verifyData(LoanApplicationRequestDTO request) {
        verifyFullName(request.getFirstName().length());
        verifyFullName(request.getLastName().length());
        verifyFullName(request.getMiddleName().length());
        verifyTerm(request.getTerm());
        verifyAmount(request.getAmount());
        verifyPassportSeries(request.getPassportSeries());
        verifyPassportNumber(request.getPassportNumber());
        verifyEmail(request.getEmail());
        verifyBirthdate(request.getBirthdate());

    }

    private void verifyFullName(Integer value) {
        if (value >= 30 || value < 2)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Введите корректные ФИО!", null);
    }
    private void verifyTerm(Integer value) {
        if (value < 6)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Минимальный срок - 6 месяцев!", null);
    }
    private void verifyAmount(BigDecimal value) {
        if (value.compareTo(new BigDecimal(10000)) > 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Минимальная сумма 10000!", null);
    }
    private void verifyPassportSeries(String value) {
        if (Pattern.matches("\\d{4}", value))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Введите корректную серию паспорта(Первые 4 цифры)!", null);
    }
    private void verifyPassportNumber(String value) {
        if (Pattern.matches("\\d{6}", value))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Введите корректный номер паспорта(Последние 6 цифр)!", null);
    }


    private void verifyBirthdate(LocalDate value) {
        LocalDate now = LocalDate.now();
        if (value.until(now).getYears() < 18)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Кредиты выдаются с 18 лет!", null);
        if (value.until(now).getYears() >= 70)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Кредиты выдаются до 70 лет!", null);



    }
   private void verifyEmail(String value) {
        if (!value.matches("^\\w{2,50}@\\w{2,30}.\\w{2,20}$"))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Введите корректную почту!", null);
    }

}