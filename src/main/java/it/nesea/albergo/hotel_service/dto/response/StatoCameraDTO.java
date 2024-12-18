package it.nesea.albergo.hotel_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatoCameraDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 8767172377150008240L;
    private Integer id;
    private String stato;
}
