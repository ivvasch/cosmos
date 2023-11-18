package ru.cosmotask.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.cosmotask.dto.ProcedureDTO;

import java.util.regex.Matcher;

@Data
@Document(collection = "t_procedure")
public class Procedure {
    @Id
    private String id;
    @NotBlank
    private String type;
    private String price;

    public void setPrice(String price) {
        String replaced = price.replace(",", ".");
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("^([1-9]\\d*)+(.\\d{1,2})?$");
        Matcher matcher = pattern.matcher(replaced);
        if (matcher.find()) {
            this.price = replaced;
        } else {
            this.price = "";
        }
    }

    public ProcedureDTO convertToDto() {
        ProcedureDTO dto = new ProcedureDTO();
        dto.setId(getId());
        dto.setType(getType());
        dto.setPrice(getPrice());
        return dto;
    }
}
