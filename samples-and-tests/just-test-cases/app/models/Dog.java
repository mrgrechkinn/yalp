package models;

import yalp.*;
import yalp.db.jpa.*;
import javax.persistence.*;
import java.util.*;

@Entity
public class Dog extends Model {
    
    private String name;
    public Integer age;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String toString() {
        return name + "(" + age + ")";
    }
    
}

