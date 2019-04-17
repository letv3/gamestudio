package sk.tuke.gamestudio.game.color_sudoku.lytvyn.core;

public class Tile {

    private int value;
    private boolean locked;



    public Tile() {

        this.value = 0;
        this.locked = false;//false if it is possible to change Tile value, true- unpossible

    }

    public boolean checkValue(int value){
        if(value < 0 || value > 9) {
            System.out.println("incorrect value");
            return false;
        }
        else return true;
    }


//    public int checkIndex(int index){
//        if( index > 81 || index < 0 ){
//            System.out.println("Wrong INDEX of Tile");
//            return -1;
//        }
//        else{
//            return index;
//        }
//    }
//

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
