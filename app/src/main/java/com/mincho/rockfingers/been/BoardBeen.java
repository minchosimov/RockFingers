package com.mincho.rockfingers.been;

import android.content.Context;

public class BoardBeen {

    private int idBoard;
    private String nameBoard;
    private String manyfactureBoard;
    private String pictureBoard;
    private int idPictureBoard;


    public BoardBeen(int idBoard, String nameBoard, String manyfactureBoard, String pictureBoard) {
        this.idBoard = idBoard;
        this.nameBoard = nameBoard;
        this.manyfactureBoard = manyfactureBoard;
        this.pictureBoard = pictureBoard;
    }

    /*
        public int getIdBoard() {
            return idBoard;
        }

        public void setIdBoard(int idBoard) {
            this.idBoard = idBoard;
        }

        public String getNameBoard() {
            return nameBoard;
        }

        public void setNameBoard(String nameBoard) {
            this.nameBoard = nameBoard;
        }

        public String getManyfactureBoard() {
            return manyfactureBoard;
        }

        public void setManyfactureBoard(String manyfactureBoard) {
            this.manyfactureBoard = manyfactureBoard;
        }

        public void setPictureBoard(String pictureBoard) {
            this.pictureBoard = pictureBoard;
        }
    */
    public int getIdPictureBoard() {
        return idPictureBoard;
    }

    public void setIdPictureBoard(Context mCtx) {
        this.idPictureBoard = mCtx.getResources().getIdentifier(this.pictureBoard, "drawable", mCtx.getPackageName());
    }

    public String getPictureBoard() {
        return pictureBoard;
    }
}
