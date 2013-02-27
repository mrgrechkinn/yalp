package models;

import yalp.*;
import yalp.db.jpa.*;
import yalp.data.validation.*;

import javax.persistence.*;
import java.util.*;

@Entity
public class A extends GenericModel {

    @Id
    public Long id;

    @ManyToOne(cascade=CascadeType.ALL)
    @Valid
    public B b;

}