package sk.tuke.gamestudio.server.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.game.color_sudoku.lytvyn.webui.WebUI;
import sk.tuke.gamestudio.service.ScoreService;

import java.util.List;

//http://localhost:8080/sudoku-lytvyn
@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class SudokuLytvynController {

    private final WebUI webUI;

    private final ScoreService scoreService;

    public SudokuLytvynController(ScoreService scoreService) {
        this.scoreService = scoreService;
        this.webUI = new WebUI(scoreService);

    }

    @RequestMapping("/sudoku-myname")
    public String sudoku(@RequestParam(value = "command", required = false) String command,
                         @RequestParam(value = "row", required = false) String row,
                         @RequestParam(value = "column", required = false) String column,
                         Model model) {

        // if required, add additional code, e.g. to check provided parameters for null

        webUI.processCommand(command, row, column);
        model.addAttribute("webUI", webUI);
        List<Score> bestScores = scoreService.getBestScores("sudoku-sudoku.lytvyn");
        model.addAttribute("scores", bestScores);

        return "sudoku-sudoku.lytvyn"; //same name as the template

    }
}
