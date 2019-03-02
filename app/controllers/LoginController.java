package controllers;

import play.mvc.*;
import java.util.*;
import views.html.*;
import models.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import play.data.validation.*;
import play.data.format.*;

import com.avaje.ebean.Model;

/**
* ユーザー認証の際に使用される
*/
public class LoginController extends Controller{
  /**
  * "id,password"の形で送られてきた文字列を受け取りデータベースとの照合を行う
  * idとpasswordの組み合わせが正しいものであった場合"true"の文字列を返す
  * idとpasswordの組み合わせが間違っていた場合"false"の文字列を返す
  */
  public Result isAccountAuthSucceed(){
    String[] accountInfo = request().body().asText().split(",", 0);
    List<Account> accountList = Account.getFind().all();
    for(Account account: accountList){
      if(account.getName().equals(accountInfo[0])){
        if(account.getPassword().equals(accountInfo[1])){
          return ok("true");
        }
      }
    }
    return ok("false");
  }
}
