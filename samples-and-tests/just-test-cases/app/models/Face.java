package models;

import yalp.*;
import yalp.db.jpa.*;

import javax.persistence.*;
import java.util.*;

@Entity
public class Face extends Model {
    
    public String name;
    
    @OneToOne(cascade = CascadeType.ALL)
    public Nose nose;
    
}

