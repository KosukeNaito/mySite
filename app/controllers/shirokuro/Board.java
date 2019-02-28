package shirokuro;

import java.util.Random;

public class Board {

    private BoardState[][] board = new BoardState[8][8];
    private boolean[][] canPut = new boolean[8][8];
    private BoardState playerStone = BoardState.BLACK;

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

    public String playerPutStone(int x, int y){
      if(this.calculateCanPut(x, y, this.playerStone)){
        this.board[x][y] = this.playerStone;
        this.turnBetween(x, y, this.playerStone);
        return this.sendBoardStr();
      }
      return "cantPut";
    }

    public String comPutStone(){
      this.calculateCanPutAll(this.getComStone());

      return this.comPutStoneRandom();
    }

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
     *
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

    public BoardState getComStone(){
      if(this.playerStone == BoardState.BLACK){
        return BoardState.WHITE;
      }else if(this.playerStone == BoardState.WHITE){
        return BoardState.BLACK;
      }
      return BoardState.EMPTY;
    }

}
