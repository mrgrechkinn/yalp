package models;

import java.util.ArrayList;
import java.util.List;
import yalp.db.jpa.*;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class AnEntity extends Model {
	
	@OneToMany
	public List<AnotherEntity> children = new ArrayList<AnotherEntity>();
}
