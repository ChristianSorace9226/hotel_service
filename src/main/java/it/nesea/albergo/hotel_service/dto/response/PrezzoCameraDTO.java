package it.nesea.albergo.hotel_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrezzoCameraDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 3916634733009689102L;

    private Integer idTipo;
    private BigDecimal prezzoTotale;
    private Integer numeroOccupanti;

}
