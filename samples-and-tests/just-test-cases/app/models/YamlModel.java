package models;

import yalp.*;
import yalp.db.jpa.*;
import yalp.data.validation.*;

import javax.persistence.*;
import java.util.*;

@Entity
public class YamlModel extends GenericModel {

    @Id
    public Long id;
    public String name;
    public byte[] binaryData;
    
}