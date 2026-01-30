package com.example.barulho;

public class LancamentoFin {
    private double vlrPago;
    private int dtDia;
    private int dtMes;
    private int dtAno;
    private int tipo;                  //  0 DESPESA      1 RECEITA
    private String descricao;

    public LancamentoFin(){}

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

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
