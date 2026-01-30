package com.example.barulho;

public class Mensalidade {
    private double vlrPago;
    private int dtDia;
    private int dtMes;
    private int dtAno;
    private String mesRef;
    private String keyJoga;

    public Mensalidade(){}

    public double getVlrPago() {
        return vlrPago;
    }
    public void setVlrPago(double vlrPago) {
        this.vlrPago = vlrPago;
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

    public String getMesRef() {
        return mesRef;
    }
    public void setMesRef(String mesRef) {
        this.mesRef = mesRef;
    }

    public String getKeyJoga() {
        return keyJoga;
    }
    public void setKeyJoga(String keyJoga) {
        this.keyJoga = keyJoga;
    }
}
