package controllers;

import play.mvc.*;

import views.html.*;
import models.*;

public class RegistrationController extends Controller{

  public Result registration(){
    return ok(registration.render());
  }


  public Result registUser(){
    return ok(getRegistUserResult(stringToUser(request().body().asText())));
  }

  private User stringToUser(String str){
    String[] userInfo = str.split(",",0);
    User user = new User();
    user.setUserName(userInfo[0]);
    user.setPassword(userInfo[1]);
    return user;
  }

  private String getRegistUserResult(User user){
    if(User.find.where().eq("userName",user.userName).findRowCount() > 0){
      System.out.println("ユーザ名が重複しています。");
      return "duplication";
    }

    try{
      user.save();
      return "success";
    }catch(Exception e){
      System.out.println("error");
      return "unknown";
    }
  }

}
