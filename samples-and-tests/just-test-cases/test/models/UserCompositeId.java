package models;

import yalp.db.jpa.GenericModel;
import yalp.db.jpa.Model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class UserCompositeId extends GenericModel {

  @EmbeddedId
  public UserId id;
  public Integer age;

}
