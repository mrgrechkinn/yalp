package models;

import yalp.*;
import yalp.db.jpa.*;
import javax.persistence.*;
import java.util.*;

@Entity
public class SomeText extends Model {
    
    public String text;
    public String lang;
    
    public String toString() {
        return text;
    }
    
}

