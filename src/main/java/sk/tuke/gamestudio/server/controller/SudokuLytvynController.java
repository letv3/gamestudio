package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.game.sudoku.lytvyn.core.*;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.RatingService;
import sk.tuke.gamestudio.service.ScoreService;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//http://localhost:8080/sudoku-lytvyn
@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
@RequestMapping("/sudoku-lytvyn")
public class SudokuLytvynController {


    @Autowired
    private ScoreService scoreService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private RatingService ratingService;

    private static Map<Integer, Field> fields = new HashMap<>();
    private Field field;
    private int displayId;
    private String loggedUser, GAMENAME="sudoku";

    @RequestMapping
    public String sudoku_init(Model model) {
        newGame();
        updateModel(model);
        return "sudoku-lytvyn"; //same name as the template

    }

    @RequestMapping("/{value}/{row}/{column}")
    public String sudoku(@PathVariable int value, @PathVariable int row, @PathVariable int column, Model model) {
        if (field == null)
            newGame();
        try{
            field.updateTile(value,row,column);
        }catch(NumberFormatException e){
            //nothing was sent
            e.printStackTrace();
        }
        updateModel(model);
        return "sudoku-lytvyn"; //same name as the template

    }

    @RequestMapping("/new")
    public String newGame(Model model) {
        newGame();
        updateModel(model);
        return "sudoku-lytvyn";
    }

    @RequestMapping("/login")
    public String login(String login, Model model) {
        loggedUser = login;
        updateModel(model);
        return "sudoku-lytvyn";
    }

    @RequestMapping("/logout")
    public String logout(Model model) {
        loggedUser = null;
        updateModel(model);
        return "sudoku-lytvyn";
    }

    @RequestMapping("/comment")
    public String addComment(String comment, Model model){
        commentService.addComment(new Comment(loggedUser, comment, GAMENAME, new Date()));
        updateModel(model);
        return "sudoku-lytvyn";
    }


    @RequestMapping("/rating")
    public String addRating(String rating, Model model){
        ratingService.setRating(new Rating(loggedUser, GAMENAME, Integer.parseInt(rating), new Date()));
        updateModel(model);
        return "sudoku-lytvyn";
    }

    @RequestMapping("/display/{id}")
    public String display(@PathVariable int id, Model model) {
        displayId = id;
        Field field = fields.get(id);
        if (field != null)
            model.addAttribute("htmlField", getHtmlField(field));
        updateModel(model);
        return "sudoku_view";
    }

    @RequestMapping("/display/pair/{id}")
    public String pair(@PathVariable int id, Model model) {
        displayId = id;
        if (field == null)
            newGame();
        fields.put(id, field);
        updateModel(model);
        return "sudoku";
    }



//   ------ Functions imlementation

    public GameState getGameState() {
        return field.getState();
    }

    public boolean getGameWon(){
        if(field.getState()==GameState.SOLVED) return true;
        else return false;
    }
    public int getCurrentScore(){
        scoreService.addScore(new Score(loggedUser, field.getScore(), GAMENAME, new Date()));
        return field.getScore();
    }

    public String getHtmlField() {
        return getHtmlField(field);
    }


    public String getHtmlField(Field field) {
        StringBuilder sb = new StringBuilder();
        sb.append("<table class='field'>\n");
        for (int row = 0; row < field.getRowCount(); row++) {
            sb.append("<tr>\n");
            for (int column = 0; column < field.getRowCount(); column++) {
                Tile tile = field.getTile(row, column);
                sb.append("<td>\n");
                if(field.equals(this.field)) {
                    sb.append("<div " +
                            String.format("onclick=\"mark('%d/%d')\"", row, column)
                            + "'>\n");
                }
                sb.append("<img src='/images/sudoku.lytvyn/" + getImageName(tile) + ".png'>");
                //if(field.equals(this.field)) sb.append("</a>\n");
                sb.append("</div>\n");
                sb.append("</td>\n");
            }
            sb.append("</tr>\n");
        }
        sb.append("</table>\n");

        return sb.toString();
    }

    public String getButtonField(){

        StringBuilder sb = new StringBuilder();
        sb.append("<table>\n");
        sb.append("<tr>\n");
        for (int i = 0; i < field.getRowCount(); i++) {
            Tile tile= new Tile();
            tile.setValue(i+1);
            sb.append("<td>\n");
            sb.append("<div>\n");
            sb.append("<img src='/images/sudoku.lytvyn/" + getImageName(tile) + ".png' onclick='setCurrentValue("+tile.getValue()+")' >");
            sb.append("</div>\n");
            sb.append("</td>\n");
        }
        sb.append("</tr>\n");
        sb.append("</table>\n");
         return sb.toString();
    }

    private String getImageName(Tile tile) {
        switch (tile.getValue()) {
            case 0:
                return "0";
            case 1:
                if (tile.isLocked())
                    return "1b";
                else
                    return "1";
            case 2:
                if (tile.isLocked())
                    return "2b";
                else
                    return "2";
            case 3:
                if (tile.isLocked())
                    return "3b";
                else
                    return "3";
            case 4:
                if (tile.isLocked())
                    return "4b";
                else
                    return "4";
            case 5:
                if (tile.isLocked())
                    return "5b";
                else
                    return "5";
            case 6:
                if (tile.isLocked())
                    return "6b";
                else
                    return "6";
            case 7:
                if (tile.isLocked())
                    return "7b";
                else
                    return "7";
            case 8:
                if (tile.isLocked())
                    return "8b";
                else
                    return "8";
            case 9:
                if (tile.isLocked())
                    return "9b";
                else
                    return "9";

        }
        throw new IllegalArgumentException();
    }


    private void updateModel(Model model) {
        model.addAttribute("displayId", displayId);
        model.addAttribute("loggedUser", loggedUser);
        model.addAttribute("scores", scoreService.getBestScores(GAMENAME));
        model.addAttribute("comments",commentService.getComments(GAMENAME));
        model.addAttribute("rating", ratingService.getAverageRating(GAMENAME));
    }

    private void newGame() {
        field = new Field();
        if (displayId != 0)
            fields.put(displayId, field);
    }

}
