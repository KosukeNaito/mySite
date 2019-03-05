package models;

import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import play.data.validation.*;
import play.data.format.*;

import com.avaje.ebean.Model;


@Entity
public class Account extends Model{


  //@GeneratedValue(strategy = GenerationType.AUTO)
  //public Long id;

  @Id
  @NotNull
  @Column(unique=true)
  public String name;//ユーザーネームを表す

  @NotNull
  public String password;//ユーザーのパスワードを表す

  public static Finder<Long, Account> find = new Finder<Long, Account>(Account.class);

  public static Find<Long, Account> getFind(){
    return find;
  }

  //以下ゲッターセッター

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

}
