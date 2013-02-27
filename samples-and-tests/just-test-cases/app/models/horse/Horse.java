package models.horse;

import yalp.data.validation.*;
import yalp.db.jpa.Model;
import javax.persistence.*;

@Entity
public class Horse extends Model {
    @OneToOne(cascade = CascadeType.ALL)
    @Valid
    private BLUP blup;

    public BLUP getBlup() {
        return blup;
    }

    public void setBlup(BLUP blup) {
        this.blup = blup;
    }
}
