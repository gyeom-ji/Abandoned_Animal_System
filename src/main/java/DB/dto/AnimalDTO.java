package DB.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.awt.*;
import java.io.*;
import java.sql.Blob;

@Getter
@Setter
@ToString
public class AnimalDTO implements Serializable {
    private long animal_pk;
    private String animal_kind;
    private String animal_sex;
    private String animal_age;
    private String animal_color;
    private String animal_feature;
    private String animal_breed;
    private String animal_neuter;
    private String animal_img;


}
