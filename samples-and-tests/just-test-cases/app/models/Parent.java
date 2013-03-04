package models;

import yalp.*;
import yalp.db.jpa.*;
import javax.persistence.*;
import java.util.*;

@Entity
public class Parent extends GenericModel {

       @Id
       public String code;

       public String name;

       @ManyToMany
       public Set<Child> children;

}