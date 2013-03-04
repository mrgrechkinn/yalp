package models;

import yalp.*;
import yalp.db.jpa.*;
import javax.persistence.*;
import java.util.*;

import yalp.data.validation.*;

@Entity
public class Address {
    
    @Id
    public String id;

    public String street;
    
    public City city;
    public int number;
    
    public int[] bikes;
    
    @Transient
    public List<Dog> dogs;
    
    public String toString() {
        return street;
    }
    
}

