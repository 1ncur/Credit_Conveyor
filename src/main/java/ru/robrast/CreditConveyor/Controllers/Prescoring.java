package ru.robrast.CreditConveyor.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import ru.robrast.CreditConveyor.Controllers.DTOs.LoanApplicationRequest;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Prescoring{
    public static void Verify_data(LoanApplicationRequest request) {
        Verify_fullName(request.getFirstName().length());
        Verify_fullName(request.getLastName().length());
        Verify_fullName(request.getMiddleName().length());
        Verify_Term(request.getTerm());
        Verify_Amount(request.getAmount());
        Verify_PassportSeries(request.getPassportSeries().length());
        Verify_PassportNumber(request.getPassportNumber().length());
        Verify_Email(request.getEmail());
        Verify_birthdate(request.getBirthdate());

    }

    private static void Verify_fullName(Integer value) {
        if (value >= 30 || value < 2)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Введите корректные ФИО!", null);
    }
    private static void Verify_Term(Integer value) {
        if (value < 6)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Минимальный срок - 6 месяцев!", null);
    }
    private static void Verify_Amount(BigDecimal value) {
        if (value.doubleValue() < 10000 )
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Минимальная сумма 10000!", null);
    }
    private static void Verify_PassportSeries(Integer value) {
        if (value != 4)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Введите корректную серию паспорта(Первые 4 цифры)!", null);
    }
    private static void Verify_PassportNumber(Integer value) {
        if (value != 6)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Введите корректный номер паспорта(Последние 6 цифр)!", null);
    }

    private static void Verify_birthdate(LocalDate value) {
        LocalDate now = LocalDate.now();
        if (now.getYear() - value.getYear() < 18)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Кредиты выдаются с 18 лет!", null);
        else if (now.getYear() - value.getYear() == 18) {
                if (now.getMonthValue() - value.getMonthValue() < 0 && now.getDayOfMonth() - value.getDayOfMonth() <1    )
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Кредиты выдаются с 18 лет!", null);
            }
        if (now.getYear() - value.getYear() > 70)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Кредиты выдаются до 70 лет!", null);
        else if (now.getYear() - value.getYear() == 70) {
            if (now.getMonthValue() - value.getMonthValue() > 0 && now.getDayOfMonth() - value.getDayOfMonth() >0    )
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Кредиты выдаются до 70 лет!", null);
        }
    }
   private static void Verify_Email(String value) {
        if (!value.matches("^\\w{2,50}@\\w{2,30}.\\w{2,20}$"))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Введите корректную почту!", null);
    }

}