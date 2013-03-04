package models.horse;

import yalp.data.validation.Max;
import yalp.data.validation.Min;
import yalp.db.jpa.Model;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class BLUP extends Model {
    @Min(50) @Max(150) private Integer total;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}