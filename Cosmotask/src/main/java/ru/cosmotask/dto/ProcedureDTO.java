package ru.cosmotask.dto;

import lombok.Data;
import ru.cosmotask.model.Procedure;

@Data
public class ProcedureDTO {
    private String id;
    private String type;
    private String price;

    public Procedure convertToProcedure() {
        Procedure procedure = new Procedure();
        procedure.setId(getId());
        procedure.setType(getType());
        procedure.setPrice(getPrice());
        return procedure;
    }

}
