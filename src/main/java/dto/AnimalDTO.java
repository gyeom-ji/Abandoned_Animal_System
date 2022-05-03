package dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.sql.Blob;

@Getter
@Setter
@ToString
public class AnimalDTO {
    private int animal_pk;
    private String animal_kind;
    private String animal_sex;
    private String animal_age;
    private String animal_color;
    private String animal_feature;
    private String animal_breed;
    private boolean animal_neuter;
    private String animal_img;
}
