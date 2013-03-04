package models;

import yalp.*;
import yalp.db.jpa.*;

import javax.persistence.*;
import java.util.*;

@Entity
public class Post extends Model {
    
    public String name;
    
    @ManyToMany(cascade=CascadeType.ALL)
    public List<Tag> tags = new ArrayList();
    
}

