package models;

import javax.persistence.Entity;
import yalp.*;
import yalp.db.jpa.*;
import yalp.data.validation.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
public class RegionalArticle extends GenericModel implements Serializable {
    @Id
    public RegionalArticlePk pk;

    public String name;

    @Override
    public String toString() {
        return "RegionalArticle{" +
                "pk=" + pk +
                ", name='" + name + '\'' +
                '}';
    }
}