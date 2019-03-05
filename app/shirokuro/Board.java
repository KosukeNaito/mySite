package shirokuro;

import java.util.Random;

public class Board {

    private BoardState[][] board = new BoardState[8][8];//ボードに置かれた石の情報を持つ
    private boolean[][] canPut = new boolean[8][8];//ボード内でおける場所の情報を持つ。
    private BoardState playerStone = BoardState.BLACK;//プレイヤーがどちらの色であるかを表す

    /**
    * ボードに置かれた石の情報。空、黒、白がある
    */
    protected enum BoardState{
      EMPTY(0),
      BLACK(1),
      WHITE(2);

      private int id;

      private BoardState(int id){
          this.id = id;
      }
      public int getId(){
          return this.id;
      }
    }

    /**
    * プレイヤーが石を置いた座標を引数とする。
    * 置ける場合、置いた後のボードの情報を文字列として返す。ボードの左上から右下へ流れる形で文字列が入る。
    * 例："000001100200...000101"空：0 黒：1 白：2
    * 置けなかった場合、エラーを表す"cantPut"の文字列を返す
    */
    public String playerPutStone(int x, int y){
      if(this.calculateCanPut(x, y, this.playerStone)){
        this.board[x][y] = this.playerStone;
        this.turnBetween(x, y, this.playerStone);
        return this.sendBoardStr();
      }
      return "cantPut";
    }

    /**
    * comが石を置くために、置ける場所を計算し置いた結果を返す。
    * 置けた場合ボードの情報を文字列として返す。
    * 置けなかった場合、エラーを表す"cantPut"の文字列を返す
    */
    public String comPutStone(){
      this.calculateCanPutAll(this.getComStone());

      return this.comPutStoneRandom();
    }

    /**
     * 置ける場所からランダムに石を置く
     * 置けた場合ボードの情報を文字列として返す。
     * 置けなかった場合、エラーを表す"cantPut"の文字列を返す
     */
    private String comPutStoneRandom(){
      int count=0;
      for(int i=0; i<8; i++){
        for(int j=0; j<8; j++){
          if(this.canPut[i][j]){
            count++;
          }
        }
      }
      System.out.println(count);
      if(count==0){
        return "cantPut";
      }
      Random r = new Random();
      int putPosition = r.nextInt(count);
      count = 0;
      for(int i=0; i<8; i++){
        for(int j=0; j<8; j++){
          if(this.canPut[i][j]){
            if(count==putPosition){
              this.board[i][j] = this.getComStone();
              this.turnBetween(i, j, this.getComStone());
              return this.sendBoardStr();
            }
            count++;
          }
        }
      }
      return "cantPut";
    }

    /**
     * コンストラクタ
     * 引数にボードの情報が入った長さ64の文字列を使用する.
     * ボードの左上から右下の順番で文字列に数が入る.空：0 黒：1 白：2
     * @param boardStatesStr
     */
    public Board(String boardStatesStr){
        if(boardStatesStr.length() != 64){
            System.out.println("長さが64の文字列を送ってください");
            return;
        }
        int count = 0;
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                char oneSquareState = boardStatesStr.charAt(count);
                if(oneSquareState == '0') {
                    this.board[i][j] = BoardState.EMPTY;
                }else if(oneSquareState == '1'){
                    this.board[i][j] = BoardState.BLACK;
                }else if(oneSquareState == '2'){
                    this.board[i][j] = BoardState.WHITE;
                }
                count++;
            }
        }
    }

    /**
     * 現在のボード情報とこれからおかれる石の色を元に置くことのできる場所を全て調べる
     */
    public void calculateCanPutAll(BoardState putColor){
        if(putColor == BoardState.EMPTY){
            System.out.println("引数は黒か白でお願いします");
        }
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                canPut[i][j] = false;
                if(board[i][j] == BoardState.EMPTY){
                    if(canSandwichUp(i, j, putColor) || canSandwichDown(i, j, putColor) ||
                    canSandwichRight(i, j, putColor) || canSandwichLeft(i, j, putColor) ||
                    canSandwichUpRight(i, j, putColor) || canSandwichDownRight(i, j, putColor) ||
                    canSandwichUpLeft(i, j, putColor) || canSandwichDownLeft(i, j, putColor)){
                        canPut[i][j] = true;
                    }
                }
            }
        }

    }

    /**
    * 引数i,jがインデックスputColorが置かれる石の色となる。
    * ボードi,jのマスに石を置ける場合、trueを返す。置けない場合false
    */
    public boolean calculateCanPut(int i, int j, BoardState putColor){
        if(putColor == BoardState.EMPTY){
            return false;
        }

        if(canSandwichUp(i, j, putColor) || canSandwichDown(i, j, putColor) ||
                canSandwichRight(i, j, putColor) || canSandwichLeft(i, j, putColor) ||
                canSandwichUpRight(i, j, putColor) || canSandwichDownRight(i, j, putColor) ||
                canSandwichUpLeft(i, j, putColor) || canSandwichDownLeft(i, j, putColor)){
            return true;
        }
        return false;
    }

    /**
    * 引数x,yがインデックスputColorが石の色となる。
    * xyに石を置いた場合に反転する石を反転しボード情報を表すメンバ変数に反映する
    */
    public void turnBetween(int x, int y, BoardState putColor){
        boolean upFlag = canSandwichUp(x, y, putColor);
        boolean downFlag = canSandwichDown(x, y, putColor);
        boolean rightFlag = canSandwichRight(x, y, putColor);
        boolean leftFlag = canSandwichLeft(x, y, putColor);
        boolean upRightFlag = canSandwichUpRight(x, y, putColor);
        boolean downRightFlag = canSandwichDownRight(x, y, putColor);
        boolean upLeftFlag = canSandwichUpLeft(x, y, putColor);
        boolean downLeftFlag = canSandwichDownLeft(x, y, putColor);
        if(leftFlag){
            for(int i = 1; 0<=y-i; i++){
                if(board[x][y-i] == putColor){
                    break;
                }
                if(board[x][y-i] != putColor || board[x][y-i] != BoardState.EMPTY){
                    board[x][y-i] = putColor;
                }
            }
        }
        if(rightFlag){
            for(int i = 1; 8>y+i; i++){
                if(board[x][y+i] == putColor){
                    break;
                }
                if(board[x][y+i] != putColor || board[x][y+i] != BoardState.EMPTY){
                    board[x][y+i] = putColor;
                }
            }
        }
        if(downFlag){
            for(int i = 1; x+i<8; i++){
                if(board[x+i][y] == putColor){
                    break;
                }
                if(board[x+i][y] != putColor || board[x+i][y] != BoardState.EMPTY){
                    board[x+i][y] = putColor;
                }
            }
        }
        if(upFlag){
            for(int i = 1; 0<=x-i; i++){
                if(board[x-i][y] == putColor){
                    break;
                }
                if(board[x-i][y] != putColor || board[x-i][y] != BoardState.EMPTY){
                    board[x-i][y] = putColor;
                }
            }
        }
        if(downLeftFlag){
            for(int k=1; x+k<8 || y-k>=0; k++){
                if(board[x+k][y-k] == putColor){
                    break;
                }
                if(board[x+k][y-k] != putColor || board[x+k][y-k] != BoardState.EMPTY){
                    board[x+k][y-k] = putColor;
                }
            }
        }
        if(downRightFlag){
            for(int k=1; x+k<8 || y+k<8; k++){
                if(board[x+k][y+k] == putColor){
                    break;
                }
                if(board[x+k][y+k] != putColor || board[x+k][y+k] != BoardState.EMPTY){
                    board[x+k][y+k] = putColor;
                }
            }
        }
        if(upRightFlag){
            for(int k=1; x-k>=0 || y+k<8; k++){
                if(board[x-k][y+k] == putColor){
                    break;
                }
                if(board[x-k][y+k] != putColor || board[x-k][y+k] != BoardState.EMPTY){
                    board[x-k][y+k] = putColor;
                }
            }
        }
        if(upLeftFlag){
            for(int k=1; x-k>=0 || y-k>=0; k++){
                if(board[x-k][y-k] == putColor){
                    break;
                }
                if(board[x-k][y-k] != putColor || board[x-k][y-k] != BoardState.EMPTY){
                    board[x-k][y-k] = putColor;
                }
            }
        }
    }

    /**
     *　メンバ変数のボード情報を元にボード情報文字列を作成し返す。
     *  例："0000000000012....210000"
     *  空：0 黒：1 白：2
     * @return
     */
    public String sendBoardStr(){
        StringBuilder boardStateStr = new StringBuilder();
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                boardStateStr.append(String.valueOf(board[i][j].getId()));
            }
        }
        return boardStateStr.toString();
    }

    /**
    * 引数i,jがインデックスputColorが置かれる石の色となる。
    * i,jにputColorの石を置いた際に上に挟める石があるかどうかを返す。返せる場合true
    */
    private boolean canSandwichUp(int i, int j, BoardState putColor){
        if(putColor == BoardState.BLACK) {
            for(int k=1; 0<=i-k; k++){
                if(i-k<0){
                    return false;
                }
                if(k == 1){
                    if(board[i-k][j] == BoardState.BLACK){
                        return false;
                    }
                }
                if(board[i-k][j] == BoardState.EMPTY){
                    return false;
                }
                if(board[i-k][j] == BoardState.BLACK){
                    return true;
                }
            }
        }else if(putColor == BoardState.WHITE){
            for(int k=1; 0<=i-k; k++){
                if(i-k<0){
                    return false;
                }
                if(k == 1){
                    if(board[i-k][j] == BoardState.WHITE){
                        return false;
                    }
                }
                if(board[i-k][j] == BoardState.EMPTY){
                    return false;
                }
                if(board[i-k][j] == BoardState.WHITE){
                    return true;
                }
            }
        }
        return false;
    }

    /**
    * 引数i,jがインデックスputColorが置かれる石の色となる。
    * i,jにputColorの石を置いた際に下に挟める石があるかかどうかを返す。返せる場合true
    */
    private boolean canSandwichDown(int i, int j, BoardState putColor){
        if(putColor == BoardState.BLACK) {
            for(int k=1; i+k<8; k++){
                if(i+k<0){
                    return false;
                }
                if(k == 1){
                    if(board[i+k][j] == BoardState.BLACK){
                        return false;
                    }
                }
                if(board[i+k][j] == BoardState.EMPTY){
                    return false;
                }
                if(board[i+k][j] == BoardState.BLACK){
                    return true;
                }
            }
        }else if(putColor == BoardState.WHITE){
            for(int k=1; i+k<8; k++){
                if(i+k>=8){
                    return false;
                }
                if(k == 1){
                    if(board[i+k][j] == BoardState.WHITE){
                        return false;
                    }
                }
                if(board[i+k][j] == BoardState.EMPTY){
                    return false;
                }
                if(board[i+k][j] == BoardState.WHITE){
                    return true;
                }
            }
        }
        return false;
    }

    /**
    * 引数i,jがインデックスputColorが置かれる石の色となる。
    * i,jにputColorの石を置いた際に右に挟める石があるかどうかを返す。返せる場合true
    */
    private boolean canSandwichRight(int i, int j, BoardState putColor){
        if(putColor == BoardState.BLACK) {
            for(int k=1; j+k<8; k++){
                if(j+k>=8){
                    return false;
                }
                if(k == 1){
                    if(board[i][j+k] == BoardState.BLACK){
                        return false;
                    }
                }
                if(board[i][j+k] == BoardState.EMPTY){
                    return false;
                }
                if(board[i][j+k] == BoardState.BLACK){
                    return true;
                }
            }
        }else if(putColor == BoardState.WHITE){
            for(int k=1; j+k<8; k++){
                if(j+k>=8){
                    return false;
                }
                if(k == 1){
                    if(board[i][j+k] == BoardState.WHITE){
                        return false;
                    }
                }
                if(board[i][j+k] == BoardState.EMPTY){
                    return false;
                }
                if(board[i][j+k] == BoardState.WHITE){
                    return true;
                }
            }
        }
        return false;
    }

    /**
    * 引数i,jがインデックスputColorが置かれる石の色となる。
    * i,jにputColorの石を置いた際に左に挟める石があるかどうかを返す。返せる場合true
    */
    private boolean canSandwichLeft(int i, int j, BoardState putColor){
        if(putColor == BoardState.BLACK) {
            for(int k=1; 0<=j-k; k++){
                if(j-k<0){
                    return false;
                }
                if(k == 1){
                    if(board[i][j-k] == BoardState.BLACK){
                        return false;
                    }
                }
                if(board[i][j-k] == BoardState.EMPTY){
                    return false;
                }
                if(board[i][j-k] == BoardState.BLACK){
                    return true;
                }
            }
        }else if(putColor == BoardState.WHITE){
            for(int k=1; 0<=j-k; k++){
                if(j-k<0){
                    return false;
                }
                if(k == 1){
                    if(board[i][j-k] == BoardState.WHITE){
                        return false;
                    }
                }
                if(board[i][j-k] == BoardState.EMPTY){
                    return false;
                }
                if(board[i][j-k] == BoardState.WHITE){
                    return true;
                }
            }
        }
        return false;
    }

    /**
    * 引数i,jがインデックスputColorが置かれる石の色となる。
    * i,jにputColorの石を置いた際に右上に挟める石があるかどうかを返す。返せる場合true
    */
    private boolean canSandwichUpRight(int i, int j, BoardState putColor){
        if(putColor == BoardState.BLACK){
            for(int k=1; i-k>=0 || j+k<8; k++){
                if(i-k<0 || j+k>=8){
                    return false;
                }
                if(k==1){
                    if(board[i-k][j+k] == BoardState.BLACK){
                        return false;
                    }
                }
                if(board[i-k][j+k] == BoardState.EMPTY){
                    return false;
                }
                if(board[i-k][j+k] == BoardState.BLACK){
                    return true;
                }
            }
        }else if(putColor == BoardState.WHITE){
            for(int k=1; i-k>=0 || j+k<8; k++){
                if(i-k<0 || j+k>=8){
                    return false;
                }
                if(k==1){
                    if(board[i-k][j+k] == BoardState.WHITE){
                        return false;
                    }
                }
                if(board[i-k][j+k] == BoardState.EMPTY){
                    return false;
                }
                if(board[i-k][j+k] == BoardState.WHITE){
                    return true;
                }
            }
        }
        return false;
    }

    /**
    * 引数i,jがインデックスputColorが置かれる石の色となる。
    * i,jにputColorの石を置いた際に左上に挟める石があるかどうかを返す。返せる場合true
    */
    private boolean canSandwichUpLeft(int i, int j, BoardState putColor){
        if(putColor == BoardState.BLACK){
            for(int k=1; i-k>=0 || j-k>=0; k++){
                if(i-k<0 || j-k<0){
                    return false;
                }
                if(k==1){
                    if(board[i-k][j-k] == BoardState.BLACK){
                        return false;
                    }
                }
                if(board[i-k][j-k] == BoardState.EMPTY){
                    return false;
                }
                if(board[i-k][j-k] == BoardState.BLACK){
                    return true;
                }
            }
        }else if(putColor == BoardState.WHITE){
            for(int k=1; i-k>=0 || j-k>=0; k++){
                if(i-k<0 || j-k<0){
                    return false;
                }
                if(k==1){
                    if(board[i-k][j-k] == BoardState.WHITE){
                        return false;
                    }
                }
                if(board[i-k][j-k] == BoardState.EMPTY){
                    return false;
                }
                if(board[i-k][j-k] == BoardState.WHITE){
                    return true;
                }
            }
        }
        return false;
    }

    /**
    * 引数i,jがインデックスputColorが置かれる石の色となる。
    * i,jにputColorの石を置いた際に右下に挟める石があるかどうかを返す。返せる場合true
    */
    private boolean canSandwichDownRight(int i, int j, BoardState putColor){
        if(putColor == BoardState.BLACK){
            for(int k=1; i+k<8 || j+k<8; k++){
                if(i+k>=8 || j+k>=8){
                    return false;
                }
                if(k==1){
                    if(board[i+k][j+k] == BoardState.BLACK){
                        return false;
                    }
                }
                if(board[i+k][j+k] == BoardState.EMPTY){
                    return false;
                }
                if(board[i+k][j+k] == BoardState.BLACK){
                    return true;
                }
            }
        }else if(putColor == BoardState.WHITE){
            for(int k=1; i+k<8 || j+k<8; k++){
                if(i+k>=8 || j+k>=8){
                    return false;
                }
                if(k==1){
                    if(board[i+k][j+k] == BoardState.WHITE){
                        return false;
                    }
                }
                if(board[i+k][j+k] == BoardState.EMPTY){
                    return false;
                }
                if(board[i+k][j+k] == BoardState.WHITE){
                    return true;
                }
            }
        }
        return false;
    }

    /**
    * 引数i,jがインデックスputColorが置かれる石の色となる。
    * i,jにputColorの石を置いた際に左下に挟める石があるかどうかを返す。返せる場合true
    */
    private boolean canSandwichDownLeft(int i, int j, BoardState putColor){
        if(putColor == BoardState.BLACK){
            for(int k=1; i+k<8 || j-k>=0; k++){
                if(i+k>=8 || j-k<0){
                    return false;
                }
                if(k==1){
                    if(board[i+k][j-k] == BoardState.BLACK){
                        return false;
                    }
                }
                if(board[i+k][j-k] == BoardState.EMPTY){
                    return false;
                }
                if(board[i+k][j-k] == BoardState.BLACK){
                    return true;
                }
            }
        }else if(putColor == BoardState.WHITE){
            for(int k=1; i+k<8 || j-k>=0; k++){
                if(i+k>=8 || j-k<0){
                    return false;
                }
                if(k==1){
                    if(board[i+k][j-k] == BoardState.WHITE){
                        return false;
                    }
                }
                if(board[i+k][j-k] == BoardState.EMPTY){
                    return false;
                }
                if(board[i+k][j-k] == BoardState.WHITE){
                    return true;
                }
            }
        }
        return false;
    }

    /**
    * メンバ変数のプレイヤーの石の情報を元にコンピュータの石の情報を返す。
    */
    public BoardState getComStone(){
      if(this.playerStone == BoardState.BLACK){
        return BoardState.WHITE;
      }else if(this.playerStone == BoardState.WHITE){
        return BoardState.BLACK;
      }
      return BoardState.EMPTY;
    }

}
