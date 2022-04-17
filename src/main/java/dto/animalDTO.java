package dto;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@Getter
@Setter
public class animalDTO {
    private int animal_pk;
    private String animal_kind;
    private String animal_sex;
    private String animal_age;
    private String animal_color;
    private String animal_feature;
    private String animal_breed;
    private boolean animal_neuter;
    private Image animal_img;
}
