package sudoku;

import java.util.ArrayList;

/**
 * マスを表すクラス。候補が一つの場合決定しているとみなす
 */
public class Square {

    private ArrayList<Integer> numCandidate = new ArrayList<>();//マスに入る候補


    /**
     * コンストラクタ。マスに入る候補として1～9のすべてを入れる
     */
    public Square(){
        for(int i=1; i<=9; i++){
            numCandidate.add(i);
        }
    }

    /**
     * numCandidateに含まれているnumを削除する
     * @param num
     */
    public void removeOneCandidate(int num){
        if(!this.isSet()) {
            for (int i = 0; i < numCandidate.size(); i++) {
                if (numCandidate.get(i) == num) {
                    numCandidate.remove(i);
                }
            }
        }
    }

    /**
     * マスの数字がすでに決定している場合trueを返す
     * @return
     */
    public boolean isSet(){
        if(this.numCandidate.size() == 1){
            return true;
        }else {
            return false;
        }
    }

    /**
     * マスの候補を引数の数字のみにする
     * @param num
     */
    public void decideCandidate(int num){
      if(num >= 1 && num <= 9) {
        if(this.numCandidate.size() != 1) {
            this.numCandidate.clear();
            numCandidate.add(num);
        }
      }else {
          System.out.println("1から9以外の数字が入っています");
      }
    }

    public ArrayList<Integer> getNumCandidate(){
        return numCandidate;
    }

}
