package sk.tuke.gamestudio.game.sudoku.lytvyn.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Field {

    private final int rowCount;
    private int numberOfHints;

    private Tile[][] preparedField;
    private Tile[][] solvedField;

    private GameState state;

    private long startMillis;

    public Field() {

        this.rowCount = 9;
        numberOfHints = 5;
        state=GameState.PLAYING;

        generateField();

    }


    private void generateField() {
        solvedField=generateSolvedField();
        preparedField= PrepareField(copy(solvedField),79
        );
        startMillis = System.currentTimeMillis();
    }
//this method will print a value what has to be set in place on row and column
    public void askForHelp(int row,int column){
        if(numberOfHints >0) {
            if (!preparedField[row][column].isLocked()) {
                System.out.println("Suggested value on this place = " + solvedField[row][column].getValue());
                numberOfHints--;
                System.out.println(numberOfHints + " hints left");
            }
            else{
                System.out.println("Very funny, value was set by a program");
                System.out.println(numberOfHints + " hints left");
            }
        }
        else
            System.out.println("U used all hints");

    }


//updateTile() function for Tile update
    public void updateTile(int value,int row, int  column){
        //row=row-1;
        //column=column-1;
        if (state == GameState.PLAYING) {
            try {
                if (value >= 1 && value <= 9 && !preparedField[row][column].isLocked()) {
                    preparedField[row][column].setValue(value);
                } else {
                    System.out.println("this bound is locked or u entered wrong value(must be 1<=value<=9)");
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                //
            }
//            if(!isNumberSuitable(preparedField, row, column, value)){
//                System.out.println("Uups, you already have this value on column(or row, or box 3x3)");
//            }else
            if(fieldIsSolved()) {
                state = GameState.SOLVED;
            }
        }
    }


//fieldIsSolved() checks if Field is won
    private boolean fieldIsSolved(){
        int victory=0;
        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                if(isNumberSuitable(preparedField, i, j, preparedField[i][j].getValue() ) || preparedField[i][j].getValue()==0) {
                    victory++;
                    break;
                }
            }
        }
        return victory==0;


    }

// generate solution for sudoku puzzle
    private Tile[][] generateSolvedField() {
        Tile[][] field= new Tile[rowCount][rowCount];

        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= 9; i++) { numbers.add(i);}
        Collections.shuffle(numbers);

        for(int i=0; i<9; i++){
            field[0][i]= new Tile();
            field[0][i].setValue(numbers.get(i));

        }
        //this cycle move the first line by NPLACES cyclically
        // NPlaces depends on i(iterator)
        // each next line has different coefficient of moving
        for(int i=1; i<=8; i++){
            int nplaces= i%3==0 ? 1:3;
            field[i]=shiftLine(field[i-1],nplaces);
        }
        return field;
    }

//This method moves line by NPLACES cyclically
    private Tile[] shiftLine(Tile[] line, int nPlaces){
        if(nPlaces<0 || line==null) return null;

        Tile[] bufferTiles = new Tile[rowCount];

        for(int i=0; i<nPlaces; i++){
            bufferTiles[i]= new Tile();
            bufferTiles[i].setValue(line[rowCount-nPlaces+i].getValue());
        }


        for(int i=0; i<rowCount-nPlaces; i++){
            bufferTiles[nPlaces+i]=new Tile();
            bufferTiles[nPlaces+i].setValue(line[i].getValue());
        }




        return bufferTiles;
    }


//PrepareField()  prepares Field for playing
    private Tile[][] PrepareField(Tile[][] field, int numberOfLockedPositions){
        // this function sets 0 on randomly generated positions in fields
        //numberOfLockedPositions -represents how many tiles with numbers will left
        numberOfLockedPositions--;

        List<Integer> positions= new ArrayList<>();
        for(int i=0; i<81; i++) positions.add(i);
        Collections.shuffle(positions);
        //System.out.println(positions);
        //Random rand= new Random();

        int row, column;
        int numberOfFreePositions= 81-numberOfLockedPositions;

        for(int i =0; i<numberOfFreePositions-1; i++){
            Integer newFreeTile = positions.get(i);
            column= newFreeTile % 9;
            row = newFreeTile / 9;

            field[row][column].setValue(0);
        }

        for(int i=0; i<9; i++) {
            for (int j = 0; j < 9; j++) {
                if (field[i][j].getValue()!=0){
                    field[i][j].setLocked(true);
                }
            }
        }
        return field;
    }

//Check if there is the same number on X, Y axes and in the bos 3x3
    private boolean isNumberSuitable(Tile[][] field , int row, int column, int valueNeedToSet){
        if(isPossibleY(field, column, valueNeedToSet) && isPossibleX(field, row, valueNeedToSet) && isPossibleBox(field,column, row, valueNeedToSet)){
            return true;
        }else{
            return false;
        }
    }

//Check for the same Tile values in row
    private boolean isPossibleX(Tile[][] field, int row, int valueNeedToSet) {
        for ( int columnIndex=0; columnIndex < rowCount; columnIndex++){
            if( field[columnIndex][row].getValue() == valueNeedToSet ){
                return false;
            }
        }
        return true;
    }

//Check for the same possible values in column
    private boolean isPossibleY(Tile[][] field, int column, int valueNeedToSet){
        for(int rowIndex=0; rowIndex  < rowCount; rowIndex++){
            if(field[column][rowIndex].getValue() == valueNeedToSet){
                return false;
            }
        }
        return true;
    }

//Check for the same possible value in 3x3 part
    private boolean isPossibleBox(Tile[][] field, int columnIndex, int rowIndex, int valueNeedToSet){

        int startPointCol= columnIndex < 3 ? 0 : columnIndex < 6 ? 3 : 6;
        int startPointRow= rowIndex < 3 ? 0 : rowIndex < 6 ? 3 : 6;

        for(int startIndexCOL=startPointCol; startIndexCOL < startPointCol + 3; startIndexCOL++){
            for(int startIndexROW=startPointRow; startIndexROW < startPointRow + 3; startIndexROW++){
                if( field[startIndexCOL][startIndexROW].getValue() == valueNeedToSet) {
                    return false;
                }
            }
        }
        return true;
    }


    public int getPlayingTime() {
        return ((int) (System.currentTimeMillis() - startMillis)) / 1000;
    }

    public int getScore() {
        return getPlayingTime();
    }


/* additional method for debugging
    private int getNumberOfZeros(Tile[][] Field){
        int zeros=0;
        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                if(Field[i][j].getValue()==0) zeros++;
            }
        }
        System.out.println("number of zeros = "+zeros);
        return zeros;
    }*/

    private Tile[][] copy(Tile[][] game) {
        Tile[][] copy = new Tile[9][9];
        for (int y = 0; y < 9; y++) {
            for (int cols = 0; cols < 9; cols++) {
                copy[y][cols] = new Tile();
                copy[y][cols].setValue(game[y][cols].getValue());
            }
        }
        return copy;
    }



    //Geters and seters----------------------------------

    public Tile getTile(int row , int column){
        return preparedField[row][column];
    }

    public GameState getState() {
        return state;
    }

//    public void setState(GameState state) {
//        this.state = state;
//    }

    public int getRowCount() {
        return rowCount;
    }
}
