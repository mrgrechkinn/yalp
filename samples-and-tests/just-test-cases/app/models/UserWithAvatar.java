package models;

import yalp.*;
import yalp.db.jpa.*;

import javax.persistence.*;
import java.util.*;

@Entity
public class UserWithAvatar extends Model {

    public String username;
    public Blob avatar;

}

