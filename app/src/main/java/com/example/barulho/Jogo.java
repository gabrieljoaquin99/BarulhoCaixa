package com.example.barulho;

import java.io.Serializable;

public class Jogo implements Serializable {
    private int seq;
    private int local;                  //  0 CASA      1 FORA
    private int dtDia;
    private int dtMes;
    private int dtAno;
    private String adversario;
    private int golsMand;
    private int golsVis;
    private int resultado;                  //  0 DERROTA      1 EMPATE      2 VITORIA

    public Jogo(){}


    public int getSeq() {
        return seq;
    }
    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getLocal() {
        return local;
    }
    public void setLocal(int local) {
        this.local = local;
    }

    public int getDtDia() {
        return dtDia;
    }
    public void setDtDia(int dtDia) {
        this.dtDia = dtDia;
    }

    public int getDtMes() {
        return dtMes;
    }
    public void setDtMes(int dtMes) {
        this.dtMes = dtMes;
    }

    public int getDtAno() {
        return dtAno;
    }
    public void setDtAno(int dtAno) {
        this.dtAno = dtAno;
    }

    public String getAdversario() {
        return adversario;
    }
    public void setAdversario(String adversario) {
        this.adversario = adversario;
    }

    public int getGolsMand() {
        return golsMand;
    }
    public void setGolsMand(int golsMand) {
        this.golsMand = golsMand;
    }

    public int getGolsVis() {
        return golsVis;
    }
    public void setGolsVis(int golsVis) {
        this.golsVis = golsVis;
    }

    public int getResultado() {
        return resultado;
    }
    public void setResultado(int resultado) {
        this.resultado = resultado;
    }
}
