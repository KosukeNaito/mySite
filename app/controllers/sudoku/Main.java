package sudoku;

public class Main {

    public static void main(String arg[]){
        WholeSquare wholeSquare = new WholeSquare();
        wholeSquare.printSquares();
        System.out.println();
        while(!wholeSquare.isAllFinished()){
            wholeSquare.reduceCandidate();
            wholeSquare.narrowCandidate();
            wholeSquare.printSquares();
            System.out.println();
        }
        if(wholeSquare.isCorrectAnswer()){
            System.out.println("達成!");
        }else{
            System.out.println("無理でした");
        }
    }

}
