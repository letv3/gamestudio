package sk.tuke.gamestudio.game.sudoku.lytvyn.consoleui;


import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio.game.sudoku.lytvyn.core.Field;
import sk.tuke.gamestudio.game.sudoku.lytvyn.core.GameState;
import sk.tuke.gamestudio.game.sudoku.lytvyn.core.Tile;
import sk.tuke.gamestudio.service.*;
import sk.tuke.gamestudio.entity.*;




import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ConsoleUI {

    private static final String GAME_NAME = "sudoku";

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private RatingService ratingService;


    private Field field;
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    final Pattern INPUT_PATTERN
            = Pattern.compile("^([1-9])([a-iA-I])([1-9])$");
    final Pattern HELP_INPUT_PATTERN
            = Pattern.compile("^([hH])([a-iA-I])([1-9])$");


    public void run() {
        field = new Field();
        printNaming();
        do {

            printField();
            processInput();

        } while (field.getState() != GameState.SOLVED);

        System.out.println("CONGRATS, U SOLVED THIS PUZZLE");
        scoreService.addScore(new Score(System.getProperty("user.name"), field.getScore(), GAME_NAME, new Date()));

        System.out.println("Want to leave you comment here?(y/n)");
        String answer = readLine().toUpperCase();
        if (answer.equals("Y")) {
            String commentText  = readLine();
            commentService.addComment(new Comment(System.getProperty("user.name"),commentText, GAME_NAME, new Date()));
        }

        System.out.println("Want to rate the game(from 1 to 5)?(y/n)");
        answer=readLine().toUpperCase();
        if(answer.equals("Y")){
            int rate= Integer.parseInt(readLine());
            ratingService.setRating(new Rating(System.getProperty("user.name"), GAME_NAME, rate, new Date()));
        }


        for (Score s:scoreService.getBestScores(GAME_NAME)) {
            System.out.println(s.toString());
        }
        System.out.println();

        for (Comment c : commentService.getComments(GAME_NAME)){
            System.out.println(c.toString());
        }
        System.out.println(ratingService.getAverageRating(GAME_NAME));

        System.out.println(ratingService.getRating(GAME_NAME,"letv"));


        System.out.println("Want to play one more time?(y/n)");
        String line = readLine().toUpperCase();
        if (line.equals("Y")) {
            run();
        }
         else  System.exit(0);


    }

    private String readLine() {
        try {
            return br.readLine();
        } catch (IOException e) {
            System.err.println("Cant read the input, try again");
            return "";
        }
    }

    private void printNaming(){
        System.out.println();
        System.out.println("   SUDOKU GAME by LETV");
        System.out.println("You can use 5 hints to solve the sudoku");
        System.out.println();
    }

    private void printField() {
        printFieldHeader();
        printFieldBody();
    }

    private void printFieldHeader() {
        System.out.print("  ");
        for (int column = 0; column < field.getRowCount(); column++) {
            System.out.printf("%3s", column + 1);
        }
        System.out.println();
        System.out.print("  ");
        for (int column = 0; column < field.getRowCount(); column++) {
            System.out.print("---");
        }
        System.out.println();

    }

    private void printFieldBody() {
        for (int row = 0; row < field.getRowCount(); row++) {
            System.out.print(((char) (row + 'A'))+"|");
            for (int column = 0; column < field.getRowCount(); column++) {
                final Tile tile = field.getTile(row, column);
                //System.out.print(' ');
                System.out.printf("%3s", tile.getValue());

            }
            System.out.println();
        }

    }

    private void processInput() {
        System.out.println("Type input (ex. 9A1, 4A1,HA1- hint on A1, X-exit):");
        String line = readLine().toUpperCase();


        if (line.equals("X")) { System.exit(0); }

        Matcher m = INPUT_PATTERN.matcher(line);
        Matcher helpm = HELP_INPUT_PATTERN.matcher(line);



        if(helpm.matches()){
            int row = helpm.group(2).charAt(0) - 65;
            int col = Integer.parseInt(helpm.group(3)) - 1;
            field.askForHelp(row,col);
        }else if (m.matches()) {
            //System.out.println(m.group(2));

            int row = m.group(2).charAt(0) - 65;
            int col = Integer.parseInt(m.group(3)) - 1;
            int value=Integer.parseInt(m.group(1));
            field.updateTile(value, row, col);
        } else {
            System.out.println("Incorrect input, try again.");
        }
    }
}

