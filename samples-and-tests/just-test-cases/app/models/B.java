package models;

import yalp.*;
import yalp.db.jpa.*;
import yalp.data.validation.*;

import javax.persistence.*;
import java.util.*;

@Entity
public class B extends GenericModel {

    @Id
    public Long id;

    @MaxSize(10)
    public String name;

}
