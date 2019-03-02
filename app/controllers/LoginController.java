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
  public Result isUserAuthSucceed(){
    String[] userInfo = request().body().asText().split(",", 0);
    List<User> userList = User.getFind().all();
    for(User user: userList){
      if(user.getUserName().equals(userInfo[0])){
        if(user.getPassword().equals(userInfo[1])){
          return ok("true");
        }
      }
    }
    return ok("false");
  }
}
