package controllers;

import play.mvc.*;

import views.html.*;
import numpla.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class NumplaController extends Controller {

    /**
     * ナンバープレースのページを表示する
     */
     public Result numpla(){
       return ok(numpla.render());
     }

     /**
     * 送られてきた問題をWholeSquareクラスに適用し、
     * 結果に応じてString型の文字列を結果として返す
     * 返す文字列のパターンを以下に記す。
     * １．問題が間違っている場合　"miss"を返す
     * ２．問題を解けなかった場合　"muri"を返す
     * ３．問題が解けた場合　長さ81の文字列に答えを連ねていく。マスの左上から右下に向かう順。
     *      例："921456...312486"
     */
     public Result numplaReciever(){
       System.out.println("run!");
       char[] board = requestEditor(request().body().asText());
       WholeSquare wholeSquare = new WholeSquare();
       wholeSquare.setSquaresFromCharArray(board);
       String answer = "";
       if(!wholeSquare.isCorrectQuestion()){
         System.out.println("問題が間違ってます");
         answer = "miss";
         return ok(answer);
       }
       while(!wholeSquare.isAllFinished()){
           wholeSquare.reduceCandidate();
           wholeSquare.narrowCandidate();
           wholeSquare.printSquares();
           System.out.println();
           if(!wholeSquare.isUpdated()){
               break;
           }
           wholeSquare.setIsUpdated(false);
       }

       if(wholeSquare.isCorrectAnswer()){
           System.out.println("達成!");
           for(int i=0; i<wholeSquare.getSquares().size(); i++){
             for(int j=0; j<wholeSquare.getSquares().size(); j++){
               if(wholeSquare.getSquares().get(i).get(j).getNumCandidate().size()==1){
                 answer = answer + String.valueOf(wholeSquare.getSquares().get(i).get(j).getNumCandidate().get(0));
               }
             }
           }
           System.out.println(answer);
           return ok(answer);
       }else{
           System.out.println("無理でした");
           answer = "muri";
           return ok(answer);
       }
     }


     /**
     *  json形式で送られてくるデータからナンバープレースの要素となる
     *  数字のみ抽出しchar型の配列として返す。
     **/
     private char[] requestEditor(String requestStr){
       requestStr = requestStr.replace("[","");
       requestStr = requestStr.replace("]","");
       requestStr = requestStr.replace("\n","");
       requestStr = requestStr.replace(" ","");
       requestStr = requestStr.replace(",","");
       System.out.println(requestStr);
       return requestStr.toCharArray();
     }

}
