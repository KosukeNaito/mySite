package controllers;

import play.mvc.*;

import views.html.*;
import models.*;

public class RegistrationController extends Controller{

  /**
  * ユーザー登録画面を表示する
  **/
  public Result registration(){
    return ok(registration.render());
  }

  /**
  * "name,password"の形で送られてくるユーザ情報の文字列をデータベースに登録するよう試みる
  **/
  public Result registAccount(){
    return ok(getRegistAccountResult(stringToAccount(request().body().asText())));
  }

  /**
  *  "name,password"の形で送られてきた文字列を受け取りAccount型に変換して返す
  **/
  private Account stringToAccount(String str){
    String[] accountInfo = str.split(",",0);
    Account account = new Account();
    account.setName(accountInfo[0]);
    account.setPassword(accountInfo[1]);
    return account;
  }

  /**
  *  送られてきたユーザ型の情報を元にデータベースとの照合を行いユーザーネームが重複しない場合、データベースへ登録を行う
  *  登録したもしくは登録に失敗したという結果をString型の文字列で以下のように返す
  *  登録成功　　："success"
  *  重複　　　　："duplication"
  *  謎の登録失敗："unknown"
  **/
  private String getRegistAccountResult(Account account){
    if(Account.find.where().eq("name",account.name).findRowCount() > 0){
      System.out.println("ユーザ名が重複しています。");
      return "duplication";
    }
    account.save();
    try{
      //account.save();
      return "success";
    }catch(Exception e){
      System.out.println("error");
      return "unknown";
    }
  }

}
