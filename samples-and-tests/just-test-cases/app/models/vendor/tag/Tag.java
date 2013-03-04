package models.vendor.tag;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import yalp.data.validation.MaxSize;
import yalp.data.validation.Required;
import yalp.db.jpa.Model;

@Entity(name="VendorTag")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="tag_type", discriminatorType=DiscriminatorType.STRING)
public abstract class Tag extends Model {

    @Required
    @MaxSize(10)
    @Column(nullable=false, unique=true, length=10)
    public String label;

}
