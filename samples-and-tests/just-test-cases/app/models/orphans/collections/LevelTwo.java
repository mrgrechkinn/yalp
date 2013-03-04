package models.orphans.collections;

import yalp.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name = "collections.LevelTwo")
@Table(name = "collections_level_two")
public class LevelTwo extends Model {

    @ManyToOne
    public LevelOne levelOne;

}
