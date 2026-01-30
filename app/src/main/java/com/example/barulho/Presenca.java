package com.example.barulho;

public class Presenca {
    private String keyJogo;
    private String keyJogador;
    private int gols;
    private int assist;
    private int dtAno;

    public Presenca(){}


    public String getKeyJogo() {
        return keyJogo;
    }
    public void setKeyJogo(String keyJogo) {
        this.keyJogo = keyJogo;
    }

    public String getKeyJogador() {
        return keyJogador;
    }
    public void setKeyJogador(String keyJogador) {
        this.keyJogador = keyJogador;
    }

    public int getGols() {
        return gols;
    }
    public void setGols(int gols) {
        this.gols = gols;
    }

    public int getAssist() {
        return assist;
    }
    public void setAssist(int assist) {
        this.assist = assist;
    }

    public int getDtAno() {
        return dtAno;
    }
    public void setDtAno(int dtAno) {
        this.dtAno = dtAno;
    }
}
