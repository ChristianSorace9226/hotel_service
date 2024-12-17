package it.nesea.albergo.hotel_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TipoCameraDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -6103806601671373824L;
    private Integer id;
    private String tipo;
}