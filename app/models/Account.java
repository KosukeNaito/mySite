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
  public static Long id=0;

  @NotNull
  @Column(unique=true)
  public String name;

  @NotNull
  public String password;

  public static Finder<Long, Account> find = new Finder<Long, Account>(Account.class);

  public static Find<Long, Account> getFind(){
    return find;
  }

  public String getName(){
    return this.name;
  }

  public String getPassword(){
    return this.password;
  }

  public void setName(String name){
    this.name = name;
  }

  public void setPassword(String password){
    this.password = password;
  }

  public void setId(){
    id++;
  }

}
