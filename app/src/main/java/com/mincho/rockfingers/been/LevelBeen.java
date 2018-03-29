package com.mincho.rockfingers.been;

public class LevelBeen {

    private Integer idLevel;
    private String nameLevel;


    public LevelBeen(Integer idLevel, String nameLevel) {
        this.idLevel = idLevel;
        this.nameLevel = nameLevel;
    }

    public Integer getIdLevel() {
        return idLevel;
    }

    public String getNameLevel() {
        return nameLevel;
    }
    /*

    public void setIdLevel(Integer idLevel) {
        this.idLevel = idLevel;
    }
    public void setNameLevel(String nameLevel) {
        this.nameLevel = nameLevel;
    }

    public int getPictureIDLevel() {
        return pictureIDLevel;
    }

    public void setPictureIDLevel(Context mCtx) {
        this.pictureIDLevel = mCtx.getResources().getIdentifier(this.pictureLevel,"drawable",mCtx.getPackageName());

    }

    public String getPictureLevel() {
        return pictureLevel;
    }

    public void setPictureLevel(String pictureLevel) {
        this.pictureLevel = pictureLevel;
    }*/
}
