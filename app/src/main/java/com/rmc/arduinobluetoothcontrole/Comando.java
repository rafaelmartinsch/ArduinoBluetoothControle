package com.rmc.arduinobluetoothcontrole;

import android.content.SharedPreferences;


public class Comando {
    private String up;
    private String down;
    private String left;
    private String right;
    private String rotLeft;
    private String rotRight;
    private String stop;
    SharedPreferences preferencias;

    public Comando(SharedPreferences preferencias){
        this.preferencias=preferencias;
        up = preferencias.getString("up", "F");
        down = preferencias.getString("down", "R");
        left = preferencias.getString("left", "E");
        right = preferencias.getString("right", "D");
        rotLeft = preferencias.getString("rotLeft", "Q");
        rotRight = preferencias.getString("rotRight", "E");
        stop = preferencias.getString("stop", "p");
    }

    public boolean Grava(){
        SharedPreferences.Editor editarPreferencias = preferencias.edit();
        editarPreferencias.putString("up", up);
        editarPreferencias.putString("down", down);
        editarPreferencias.putString("left", left);
        editarPreferencias.putString("right", right);
        editarPreferencias.putString("rotLeft", rotLeft);
        editarPreferencias.putString("rotRight", rotRight);
        editarPreferencias.putString("stop", stop);
        editarPreferencias.commit();

        return true;
    }


    public String getUp() {
        return up;
    }
    public void setUp(String up) {
        this.up = up;
    }
    public String getDown() {
        return down;
    }
    public void setDown(String down) {
        this.down = down;
    }
    public String getLeft() {
        return left;
    }
    public void setLeft(String left) {
        this.left = left;
    }
    public String getRight() {
        return right;
    }
    public void setRight(String right) {
        this.right = right;
    }
    public String getRotRight() {
        return rotRight;
    }
    public void setRotRight(String rotRight) {
        this.rotRight = rotRight;
    }
    public String getRotLeft() {
        return rotLeft;
    }
    public void setRotLeft(String rotLeft) {
        this.rotLeft = rotLeft;
    }
    public String getStop() {
        return stop;
    }
    public void setStop(String stop) {
        this.stop = stop;
    }
}
