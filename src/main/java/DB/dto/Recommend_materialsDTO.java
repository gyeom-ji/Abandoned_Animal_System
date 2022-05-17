package DB.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.awt.*;

@Getter
@Setter
@ToString
public class Recommend_materialsDTO {
    private long recommended_materials_pk;
    private String materials_name;
    private String materials_type;
    private String materials_url;
    private String materials_feature;
    private String materials_img;
    private String materials_animal_kind;
    private String materials_animal_breed;
    private String materials_animal_age;

}
