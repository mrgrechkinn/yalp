package models;

import yalp.data.binding.As;
import yalp.data.binding.NoBinding;
import yalp.data.validation.*;
import yalp.db.jpa.*;

import javax.persistence.*;
import java.util.*;

@Entity
public class User extends Model {
	
	public User(String name) {
		this.name = name;
	}
	
	public User() {
	}
    
    public String name;
    public Boolean b;
    public boolean c;
    public Integer i;
    public int j;
    public long l;
    public Long k;

	@Required
    @As("dd/MM/yyyy")
    public Date birth;

    @Email
    public String email;
    
    @Valid
    @Transient
    public List<MyAddress> addresses;

	public String toString() {
		return name;
	}
	
	public static String yip() {
		return "YIP";
	}
    
}

