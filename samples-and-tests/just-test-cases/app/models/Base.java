package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import yalp.db.jpa.Model;

@Entity
public class Base extends Model{
	public String name;
	@ManyToOne
	public Referenced ref;
}
