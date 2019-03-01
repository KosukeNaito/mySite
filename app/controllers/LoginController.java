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


public class LoginController extends Controller{
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
