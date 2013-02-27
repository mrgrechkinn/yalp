package models;

import yalp.data.binding.As;
import yalp.data.binding.NoBinding;

import java.util.Date;

public class Person {

    public String userName;
    public String password;
    @As("dd/MM/yyyy")
    public Date creationDate;
    @NoBinding("secure")
    public String role;

}
