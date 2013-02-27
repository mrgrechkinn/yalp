package models;

import yalp.data.validation.Required;
import yalp.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "error_members")
public class ErrorMember extends Model {

	public ErrorMember() {
	}
	
	@Required
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
