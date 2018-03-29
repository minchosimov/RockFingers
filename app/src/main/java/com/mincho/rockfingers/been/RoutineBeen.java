package com.mincho.rockfingers.been;

import java.util.ArrayList;

public class RoutineBeen {
    private int idRoutine;
    private String nameRoutine;
    private String authorRoutine;
    private int idBoard;
    private ArrayList<LevelBeen> levelsRoutine;

    /*public RoutineBeen() {
        levelsRoutine = new ArrayList<>();
    }*/

    public RoutineBeen(int idRoutine, String nameRoutine, String authorRoutine, int idBoard, ArrayList<LevelBeen> levels) {

        this.idRoutine = idRoutine;
        this.nameRoutine = nameRoutine;
        this.authorRoutine = authorRoutine;
        this.idBoard = idBoard;
        this.levelsRoutine = levels;
    }

    public int getIdRoutine() {
        return idRoutine;
    }

    /*public void setIdRoutine(int idRoutine) {
        this.idRoutine = idRoutine;
    }*/

    public String getNameRoutine() {
        return nameRoutine;
    }

    /* public void setNameRoutine(String nameRoutine) {
         this.nameRoutine = nameRoutine;
     }

     public String getAuthorRoutine() {
         return authorRoutine;
     }

     public void setAuthorRoutine(String authorRoutine) {
         this.authorRoutine = authorRoutine;
     }
 */
    public ArrayList<LevelBeen> getLevelsRoutine() {
        return levelsRoutine;
    }

    /*
    public void setLevelsRoutine(ArrayList<LevelBeen> levelsRoutine) {
        this.levelsRoutine = levelsRoutine;
    }
*/
    public int getBoard() {
        return idBoard;
    }
/*
    public void setBoard(int board) {
        this.idBoard = idRoutine;
    }*/
}
