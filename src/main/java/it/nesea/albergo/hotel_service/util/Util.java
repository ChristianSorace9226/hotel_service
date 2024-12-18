package it.nesea.albergo.hotel_service.util;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class Util {

    public BigDecimal calcolaPercentuale(int totale, int parte) {
        if (totale == 0) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(parte * 100.0 / totale);
    }
}
