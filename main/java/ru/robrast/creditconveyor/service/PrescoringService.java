package ru.robrast.creditconveyor.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.robrast.creditconveyor.dto.LoanApplicationRequestDTO;
import ru.robrast.creditconveyor.exception.RejectionException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.regex.Pattern;
@Slf4j
@SuppressWarnings("unused")
@Service
public class PrescoringService {
    public void verifyData(LoanApplicationRequestDTO request) {
        verifyFullName(request.getFirstName());
        verifyFullName(request.getLastName());
        verifyFullName(request.getMiddleName());
        verifyTerm(request.getTerm());
        verifyAmount(request.getAmount());
        verifyPassportSeries(request.getPassportSeries());
        verifyPassportNumber(request.getPassportNumber());
        verifyEmail(request.getEmail());
        verifyBirthdate(request.getBirthdate());

    }

    private void verifyFullName(String value) {
        if (!Pattern.matches("\\w{2,30}", value)) {
            log.debug("Некорректно введены ФИО");
            throw new RejectionException("Введите корректные ФИО!");
        }
    }
    private void verifyTerm(Integer value) {
        if (value < 6){
            log.debug("Срок менее 6 месяцев");
            throw new RejectionException("Минимальный срок - 6 месяцев!");
        }
    }
    private void verifyAmount(BigDecimal value) {
        if (value.compareTo(new BigDecimal(10000)) < 0){
            log.debug("Сумма менее 10000");
            throw new RejectionException("Минимальная сумма 10000!");
        }
    }
    private void verifyPassportSeries(String value) {
        if (!Pattern.matches("\\d{4}", value)){
            log.debug("Некорректная серия паспорта");
            throw new RejectionException("Введите корректную серию паспорта(Первые 4 цифры)!");
        }
    }
    private void verifyPassportNumber(String value) {
        if (!Pattern.matches("\\d{6}", value)){
            log.debug("Некорректный номер паспорта");
            throw new RejectionException("Введите корректный номер паспорта(Последние 6 цифр)!");
        }
    }


    private void verifyBirthdate(LocalDate value) {
        LocalDate now = LocalDate.now();
        if (value.until(now).getYears() < 18) {
            log.debug("Младше 18 лет");
            throw new RejectionException("Кредиты выдаются с 18 лет!");
        }
        if (value.until(now).getYears() >= 70) {
            log.debug("Старше 70 лет");
            throw new RejectionException("Кредиты выдаются до 70 лет!");
        }
    }
   private void verifyEmail(String value) {
        if (!value.matches("^\\w{2,50}@\\w{2,30}.\\w{2,20}$")){
            log.debug("Некорректный формат почты");
            throw new RejectionException("Введите корректную почту!");
        }
    }

}