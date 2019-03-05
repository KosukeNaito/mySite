package numpla;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.lang.NumberFormatException;

/**
 * マス（Squareクラス）の集合でナンバープレース全体を表すクラス
 * 9個のマスがブロックを作り9個のブロックがこのクラスであるナンバープレース全体を表す
 * 以下の一文字がマスを表し同一文字の塊がブロック、全体がこのクラスとなる。
 * xxxyyyzzz
 * xxxyyyzzz
 * xxxyyyzzz
 * aaabbbccc
 * aaabbbccc
 * aaabbbccc
 * iiijjjkkk
 * iiijjjkkk
 * iiijjjkkk
 */
public class WholeSquare {

    private String filePath = "";//元々csvを読み込む形式で作っていたためにあるファイルパスの文字列
    private ArrayList<ArrayList<Square>> squares = new ArrayList<>();//9x9の二次元配列で、マスの候補集合の9x9の集合を表す
    private boolean isUpdated = false;//いずれかのマスの候補集合が更新されたかどうかを表す二値。更新されなかった場合falseとなり処理を中断する

    /**
    * コンストラクタ
    */
    public WholeSquare(){
        initSquares();
        //readQuestion();
    }

    /**
    * char型の文字列で送られてくるボード情報をメンバ変数のsquaresにあてはめるメソッド。
    *
    */
    public void setSquaresFromCharArray(char[] board){
      if(board.length != 81){
        System.out.println("charArrayのサイズを9x9の81でお願いします");
        return;
      }
      int count=0;
      for(int i=0; i<9; i++){
        for(int j=0; j<9; j++){
          try{
            int num = Character.getNumericValue(board[count]);
            if(num >= 1 && num <= 9) {
              squares.get(i).get(j).decideCandidate(num);
            }
            count++;
          }catch(NumberFormatException e){
            System.out.println("数の形式が正しくありません");
            return;
          }
        }
      }
    }

    /**
     * 9x9がすべて埋まった後に使用する予定
     * 9x9に入った数字が数独のルールを守っている場合trueを返す
     * 違反例：行、列、ブロックのいずれかに同一の数字が入る
     * @return
     */
    public boolean isCorrectAnswer(){
        return isAllFinished() && isBlockCorrect() && isColCorrect() && isRowCorrect();
    }

    /**
    * 問題を読み込んだ際に呼ばれる。
    * 問題が正しい場合trueを返す。
    * 問題が誤っている場合falseを返す。
    * 違反例：行、列、ブロックのいずれかに同一の数字が入る
    */
    public boolean isCorrectQuestion(){
        return isBlockCorrect() && isColCorrect() && isRowCorrect();
    }

    /**
     * ブロック内の数字が数独のルールを守っている場合trueを返す
     * @return
     */
    private boolean isBlockCorrect(){
        ArrayList<ArrayList<Integer>> blockElements = new ArrayList<>();
        for(int i=0; i<squares.size(); i++){
            blockElements.add(new ArrayList<>());
        }
        for(int i=0; i<squares.size(); i++){
            for(int j=0; j<squares.size(); j++){
                int blockPosition = getBlockPosition(i, j);
                if(squares.get(i).get(j).getNumCandidate().size() == 1){
                    if(blockElements.get(blockPosition-1).contains(squares.get(i).get(j).getNumCandidate().get(0))){
                        return false;
                    }else{
                        blockElements.get(blockPosition-1).add(squares.get(i).get(j).getNumCandidate().get(0));
                    }
                }
            }
        }
        return true;
    }

    /**
     * 行内の数字が数独のルールを守っている場合trueを返す
     * @return
     */
    private boolean isRowCorrect(){
        ArrayList<ArrayList<Integer>> blockElements = new ArrayList<>();
        for(int i=0; i<squares.size(); i++){
            blockElements.add(new ArrayList<>());
        }
        for(int i=0; i<squares.size(); i++){
            for(int j=0; j<squares.size(); j++){
                if(squares.get(i).get(j).getNumCandidate().size() == 1){
                    if(blockElements.get(i).contains(squares.get(i).get(j).getNumCandidate().get(0))){
                        return false;
                    }else{
                        blockElements.get(i).add(squares.get(i).get(j).getNumCandidate().get(0));
                    }
                }
            }
        }
        return true;
    }

    /**
     * 列内の数字が数独のルールを守っている場合trueを返す
     * @return
     */
    private boolean isColCorrect(){
        ArrayList<ArrayList<Integer>> blockElements = new ArrayList<>();
        for(int i=0; i<squares.size(); i++){
            blockElements.add(new ArrayList<>());
        }
        for(int i=0; i<squares.size(); i++){
            for(int j=0; j<squares.size(); j++){
                if(squares.get(i).get(j).getNumCandidate().size() == 1){
                    if(blockElements.get(j).contains(squares.get(i).get(j).getNumCandidate().get(0))){
                        return false;
                    }else{
                        blockElements.get(j).add(squares.get(i).get(j).getNumCandidate().get(0));
                    }
                }
            }
        }
        return true;
    }

    /**
    * 全マスの候補集合のサイズが1となった場合trueを返す。
    */
    public boolean isAllFinished(){
        for(int i=0; i<squares.size(); i++){
            for(int j=0; j<squares.get(i).size(); j++){
                if(!squares.get(i).get(j).isSet()){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 現状の候補を元により候補を絞っていく
     * 例えば同じブロック内で1を候補として持つマスが一つしかない場合、そのマスは1で決定となる
     */
    public void narrowCandidate(){
        narrowCandidateHorizontal();
        narrowCandidateVertical();
        narrowCandidateBlock();
    }


    /**
    * 横方向（行）の情報を元に決定できる数字を決定する。
    * 横方向の中である数字を候補に持つマスが一つである場合
    * そのマスはその数字で決定となる。
    */
    private void narrowCandidateHorizontal(){
        for(int num=1; num<=9; num++) {//ナンバープレースに使われる数字1から9を表す
            for (int i = 0; i < squares.size(); i++) {
                int numCount = 0;//numを候補に持つマスの数
                int index = 0;
                for (int j = 0; j < squares.get(i).size(); j++) {
                    if(squares.get(i).get(j).getNumCandidate().contains(num)){
                        numCount++;
                        index = j;
                    }
                }
                if(numCount == 1){
                    squares.get(i).get(index).decideCandidate(num);
                }
            }
        }
    }

    /**
    * 縦方向（列）の情報を元に決定できる数字を決定する。
    * 縦方向の中である数字を候補に持つマスが一つである場合
    * そのマスはその数字で決定となる。
    */
    private void narrowCandidateVertical(){
        for(int num=1; num<=9; num++) {//ナンバープレースに使われる数字1から9を表す
            for (int i = 0; i < squares.size(); i++) {
                int numCount = 0;//numを候補に持つマスの数
                int index = 0;
                for (int j = 0; j < squares.get(i).size(); j++) {
                    if(squares.get(j).get(i).getNumCandidate().contains(num)){
                        numCount++;
                        index = j;
                    }
                }
                if(numCount == 1){
                    squares.get(index).get(i).decideCandidate(num);
                }
            }
        }
    }

    /**
    * 同一ブロック内の情報を元に決定できる数字を決定する。
    * 同一ブロック内である数字を候補に持つマスが一つである場合
    * そのマスはその数字で決定となる。
    */
    private void narrowCandidateBlock(){
        for(int num=1; num<=9;num++) {
            int numCount=0;
            int row = 0;
            int col = 0;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if(squares.get(i).get(j).getNumCandidate().contains(num)){
                        numCount++;
                        row = i;
                        col = j;
                    }
                }
            }
            if(numCount == 1){
                squares.get(row).get(col).decideCandidate(num);
            }
        }

        for(int num=1; num<=9;num++) {
            int numCount=0;
            int row = 0;
            int col = 0;
            for (int i = 0; i < 3; i++) {
                for (int j = 3; j < 6; j++) {
                    if(squares.get(i).get(j).getNumCandidate().contains(num)){
                        numCount++;
                        row = i;
                        col = j;
                    }
                }
            }
            if(numCount == 1){
                squares.get(row).get(col).decideCandidate(num);
            }
        }

        for(int num=1; num<=9;num++) {
            int numCount=0;
            int row = 0;
            int col = 0;
            for (int i = 0; i < 3; i++) {
                for (int j = 6; j < 9; j++) {
                    if(squares.get(i).get(j).getNumCandidate().contains(num)){
                        numCount++;
                        row = i;
                        col = j;
                    }
                }
            }
            if(numCount == 1){
                squares.get(row).get(col).decideCandidate(num);
            }
        }

        for(int num=1; num<=9;num++) {
            int numCount=0;
            int row = 0;
            int col = 0;
            for (int i = 3; i < 6; i++) {
                for (int j = 0; j < 3; j++) {
                    if(squares.get(i).get(j).getNumCandidate().contains(num)){
                        numCount++;
                        row = i;
                        col = j;
                    }
                }
            }
            if(numCount == 1){
                squares.get(row).get(col).decideCandidate(num);
            }
        }

        for(int num=1; num<=9;num++) {
            int numCount=0;
            int row = 0;
            int col = 0;
            for (int i = 3; i < 6; i++) {
                for (int j = 3; j < 6; j++) {
                    if(squares.get(i).get(j).getNumCandidate().contains(num)){
                        numCount++;
                        row = i;
                        col = j;
                    }
                }
            }
            if(numCount == 1){
                squares.get(row).get(col).decideCandidate(num);
            }
        }

        for(int num=1; num<=9;num++) {
            int numCount=0;
            int row = 0;
            int col = 0;
            for (int i = 3; i < 6; i++) {
                for (int j = 6; j < 9; j++) {
                    if(squares.get(i).get(j).getNumCandidate().contains(num)){
                        numCount++;
                        row = i;
                        col = j;
                    }
                }
            }
            if(numCount == 1){
                squares.get(row).get(col).decideCandidate(num);
            }
        }

        for(int num=1; num<=9;num++) {
            int numCount=0;
            int row = 0;
            int col = 0;
            for (int i = 6; i < 9; i++) {
                for (int j = 0; j < 3; j++) {
                    if(squares.get(i).get(j).getNumCandidate().contains(num)){
                        numCount++;
                        row = i;
                        col = j;
                    }
                }
            }
            if(numCount == 1){
                squares.get(row).get(col).decideCandidate(num);
            }
        }

        for(int num=1; num<=9;num++) {
            int numCount=0;
            int row = 0;
            int col = 0;
            for (int i = 6; i < 9; i++) {
                for (int j = 3; j < 6; j++) {
                    if(squares.get(i).get(j).getNumCandidate().contains(num)){
                        numCount++;
                        row = i;
                        col = j;
                    }
                }
            }
            if(numCount == 1){
                squares.get(row).get(col).decideCandidate(num);
            }
        }

        for(int num=1; num<=9;num++) {
            int numCount=0;
            int row = 0;
            int col = 0;
            for (int i = 6; i < 9; i++) {
                for (int j = 6; j < 9; j++) {
                    if(squares.get(i).get(j).getNumCandidate().contains(num)){
                        numCount++;
                        row = i;
                        col = j;
                    }
                }
            }
            if(numCount == 1){
                squares.get(row).get(col).decideCandidate(num);
            }
        }

    }

    /**
     * 既に決定しているマスの情報を元に、各マスの候補を減らす
     */
    public void reduceCandidate(){
        for(int i=0; i<squares.size(); i++){
            for(int j=0; j<squares.get(i).size(); j++){
                if(squares.get(i).get(j).isSet()){
                    reduceCandidateHorizontal(squares.get(i).get(j).getNumCandidate().get(0), i);//水平
                    reduceCandidateVertical(squares.get(i).get(j).getNumCandidate().get(0), j);//垂直
                    reduceCandidateBlock(squares.get(i).get(j).getNumCandidate().get(0), i, j);//ブロック
                }
            }
        }
    }

    /**
    * 既に決定している横方向（行）の情報を元に各マスの候補を減らす
    * 同じ行で決定している数字がある場合、その行の他のマスにその数字が入ることはなくなり削除される。
    */
    private void reduceCandidateHorizontal(int num, int row){
        for(int i=0; i<squares.get(row).size(); i++){
            if(!squares.get(row).get(i).isSet() && squares.get(row).get(i).getNumCandidate().contains(num)){
                squares.get(row).get(i).removeOneCandidate(num);
                this.isUpdated = true;
            }
        }
    }

    /**
     * 既に決定している縦方向（列）の情報を元に各マスの候補を減らす
     * 同じ列で決定している数字がある場合、その列の他のマスにその数字が入ることはなくなり削除される。
     * @param num
     * @param col
     */
    private void reduceCandidateVertical(int num, int col){
        for(int i=0; i<squares.size(); i++){
            if(!squares.get(i).get(col).isSet() && squares.get(i).get(col).getNumCandidate().contains(num)){
                squares.get(i).get(col).removeOneCandidate(num);
                this.isUpdated = true;
            }
        }
    }

    /**
     *  既に決定しているブロック内の情報を元に各マスの候補を減らす
     * 同じブロック内で決定している数字がある場合、そのブロック内の他のマスにその数字が入ることはなくなり削除される。
     * @param num
     * @param row
     * @param col
     */
    private void reduceCandidateBlock(int num, int row, int col){
        int blockPosision = getBlockPosition(row,col);
        switch (blockPosision){
            case 1:
                for(int i=0; i<3; i++){
                    for(int j=0; j<3; j++){
                        if(!squares.get(i).get(j).isSet() && squares.get(i).get(j).getNumCandidate().contains(num)){
                            squares.get(i).get(j).removeOneCandidate(num);
                            this.isUpdated = true;
                        }
                    }
                }
                break;
            case 2:
                for(int i=0; i<3; i++){
                    for(int j=3; j<6; j++){
                        if(!squares.get(i).get(j).isSet() && squares.get(i).get(j).getNumCandidate().contains(num)){
                            squares.get(i).get(j).removeOneCandidate(num);
                            this.isUpdated = true;
                        }
                    }
                }
                break;
            case 3:
                for(int i=0; i<3; i++){
                    for(int j=6; j<9; j++){
                        if(!squares.get(i).get(j).isSet() && squares.get(i).get(j).getNumCandidate().contains(num)){
                            squares.get(i).get(j).removeOneCandidate(num);
                            this.isUpdated = true;
                        }
                    }
                }
                break;
            case 4:
                for(int i=3; i<6; i++){
                    for(int j=0; j<3; j++){
                        if(!squares.get(i).get(j).isSet() && squares.get(i).get(j).getNumCandidate().contains(num)){
                            squares.get(i).get(j).removeOneCandidate(num);
                            this.isUpdated = true;
                        }
                    }
                }
                break;
            case 5:
                for(int i=3; i<6; i++){
                    for(int j=3; j<6; j++){
                        if(!squares.get(i).get(j).isSet() && squares.get(i).get(j).getNumCandidate().contains(num)){
                            squares.get(i).get(j).removeOneCandidate(num);
                            this.isUpdated = true;
                        }
                    }
                }
                break;
            case 6:
                for(int i=3; i<6; i++){
                    for(int j=6; j<9; j++){
                        if(!squares.get(i).get(j).isSet() && squares.get(i).get(j).getNumCandidate().contains(num)){
                            squares.get(i).get(j).removeOneCandidate(num);
                            this.isUpdated = true;
                        }
                    }
                }
                break;
            case 7:
                for(int i=6; i<9; i++){
                    for(int j=0; j<3; j++){
                        if(!squares.get(i).get(j).isSet() && squares.get(i).get(j).getNumCandidate().contains(num)){
                            squares.get(i).get(j).removeOneCandidate(num);
                            this.isUpdated = true;
                        }
                    }
                }
                break;
            case 8:
                for(int i=6; i<9; i++){
                    for(int j=3; j<6; j++){
                        if(!squares.get(i).get(j).isSet() && squares.get(i).get(j).getNumCandidate().contains(num)){
                            squares.get(i).get(j).removeOneCandidate(num);
                            this.isUpdated = true;
                        }
                    }
                }
                break;
            case 9:
                for(int i=6; i<9; i++){
                    for(int j=6; j<9; j++){
                        if(!squares.get(i).get(j).isSet() && squares.get(i).get(j).getNumCandidate().contains(num)){
                            squares.get(i).get(j).removeOneCandidate(num);
                            this.isUpdated = true;
                        }
                    }
                }
                break;
            default:break;
        }
    }

    /**
     * squareの場所からどのブロックに所属するか返す
     * ブロックは下記の通りに番号付けを行う
     * 123
     * 456
     * 789
     * @param row
     * @param col
     * @return
     */
    private int getBlockPosition(int row, int col){
        if(row <= 2 && col <= 2){
            return 1;
        }else if(row >= 3 && row <= 5 && col <= 2){
            return 4;
        }else if(row >= 6 && row <= 8 && col <= 2){
            return 7;
        }else if(row <= 2 && col <= 5){
            return 2;
        }else if(row >= 3 && row <= 5 && col <= 5){
            return 5;
        }else if(row >= 6 && row <= 8 && col <=5){
            return 8;
        }else if(row <= 2 && col <= 8){
            return 3;
        }else if(row >= 3 && row <= 5 && col <= 8){
            return 6;
        }else if(row >= 6 && row <= 8 && col <= 8){
            return 9;
        }
        System.out.println("ブロック内にないですけどなぜ？");
        return 0;
    }

    /**
     * ナンバープレース全体をコンソールに表示する。決まっていない場所は"*"が表示される
     */
    public void printSquares(){
        for(int i=0; i<squares.size(); i++){
            for(int j=0; j<squares.get(i).size(); j++){
                if(squares.get(i).get(j).getNumCandidate().size() == 1) {
                    System.out.print(squares.get(i).get(j).getNumCandidate().get(0));
                }else{
                    System.out.print("*");
                }
                System.out.print(",");
            }
            System.out.println();
        }
    }

    /**
     * メンバ変数のsquaresを初期化する。
     */
    private void initSquares(){
        this.squares.clear();
        for(int i=0; i<9; i++){
            ArrayList<Square> horizontalSquare = new ArrayList<>();
            for(int j=0; j<9; j++){
                Square s = new Square();
                horizontalSquare.add(s);
            }
            squares.add(horizontalSquare);
        }
    }

    /**
     * CSVファイルを読み込む。9x9以外は受け付けない
     */
    private void readQuestion(){
        try{
            File f = new File(filePath);
            BufferedReader br = new BufferedReader(new FileReader(f));

            String line;
            ArrayList<ArrayList<Integer>> question = new ArrayList<>();
            while((line = br.readLine()) != null){
                String[] data = line.split(",",0);
                if(data.length != 9){
                    System.out.println("サイズは9x9でお願いします。");
                    System.exit(0);
                }
                ArrayList<Integer> horizontalQuestion = new ArrayList<>();
                for(int i=0; i<data.length; i++){
                    if(data[i].equals("")){
                        horizontalQuestion.add(0);
                    }else {
                        horizontalQuestion.add(Integer.parseInt(data[i]));
                    }
                }
                question.add(horizontalQuestion);


            }

            for(int i=0; i<question.size(); i++){
                for(int j=0; j<question.get(i).size(); j++){
                    if(question.get(i).get(j) >= 1 && question.get(i).get(j) <= 9) {
                        squares.get(i).get(j).decideCandidate(question.get(i).get(j));
                    }
                }
            }
            br.close();



        }catch (IOException e){
            System.out.println(e);
        }
    }

    //以下、ゲッターセッター

    public ArrayList<ArrayList<Square>> getSquares(){
        return squares;
    }

    public void setIsUpdated(boolean bool){
        this.isUpdated = bool;
    }

    public boolean isUpdated(){
        return isUpdated;
    }

    public void setSquares(ArrayList<ArrayList<Square>> squares){
      this.squares = squares;
    }

}
