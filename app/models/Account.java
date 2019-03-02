package models;

import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import play.data.validation.*;
import play.data.format.*;

import com.avaje.ebean.Model;


@Entity
public class Account extends Model{

  @Id
  public Long id;

  @NotNull
  @Column(unique=true)
  public String userName;

  @NotNull
  public String password;

  public static Finder<Long, Account> find = new Finder<Long, Account>(Account.class);

  public static Find<Long, Account> getFind(){
    return find;
  }

  public String getUserName(){
    return this.userName;
  }

  public String getPassword(){
    return this.password;
  }

  public void setUserName(String userName){
    this.userName = userName;
  }

  public void setPassword(String password){
    this.password = password;
  }

}
