package sk.tuke.gamestudio.game.sudoku.lytvyn.webui;

import sk.tuke.gamestudio.service.ScoreService;

public class WebUI {
//    private Field field;
    private ScoreService scoreService;
    
    public WebUI(ScoreService scoreService) {
        this.scoreService = scoreService;
        createField();
    }

    public void processCommand(String command, String rowString, String columnString) {
	//TODO: implement what should happen when a command comes (open, mark, newgame, ...)
    }

    // TODO: create game field representation for HTML
    public String renderAsHtml() {
	// The most basic (and outdated) method of creating game field representation 
	// is by composing a string containing all HTML code for field layout as a table.
	// But be warned: dark is that path... Figure out better approach.
    	return "";
    }
    
    private void createField() {
//        field = new Field(9, 9, 10);
    }
    
    // TODO: add another methods as needed
}
