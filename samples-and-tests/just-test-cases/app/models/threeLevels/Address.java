package models.threeLevels;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import yalp.db.jpa.GenericModel;
import yalp.db.jpa.Model;

@Entity(name="tlAddress")
public class Address extends GenericModel {
    
  @Id
  public Long id;
  
  @Column
  public String streetName;
  
  @Override
  public String toString() {
    return streetName;
  }
  
}
