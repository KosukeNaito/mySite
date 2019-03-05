package controllers;

import play.mvc.*;

import views.html.*;
import shirokuro.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class ShirokuroController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result shirokuro() {
        return ok(shirokuro.render());
    }

    /**
    * x+y+","+boardInfo の形で送られてくる文字列を分割し、置けるかどうか判定。
    * 置けた場合反転操作後のボード情報を返す。置けなかった場合エラーを表す"cantPut"の文字列を返す
    * x：ボードのx座標を表す（横のインデックス）
    * y：ボードのy座標を表す(縦のインデックス)
    * boardInfo：左上から右下へむかってボード情報がはいる"00000110....221000"
    * 空0 黒1 白2
    **/
    public Result playerPutStone(){
      String result = request().body().asText();
      System.out.println(result);
      String[] xyAndBoard = result.split(",",0);
      Board board = new Board(xyAndBoard[1]);
      int x = Character.getNumericValue(xyAndBoard[0].charAt(0));
      int y = Character.getNumericValue(xyAndBoard[0].charAt(1));
      return ok(board.playerPutStone(x,y));
    }

    /**
    * ボードの情報が文字列で送られてくる。
    * その情報を元に以下の操作を行う。
    * ・石を置ける場合、石を置いて反転操作後のボード情報を返す
    * ・石を置けない場合、エラーを表す"cantPut"の文字列を返す
    **/
    public Result comPutStone(){
      String result = request().body().asText();
      System.out.println(result);
      Board board = new Board(result);
      return ok(board.comPutStone());
    }

}
