package com.example.barulho;

import java.io.Serializable;

public class Jogador implements Serializable {
    private String nome;
    private String whats;
    private String senha;
    private int contJogos;
    private int contGols;
    private int contAssist;
    private int nivel;                  //  0 NORMAL      1 ADMINISTRADOR
    private int ativo;                  //  0 INATIVO     1 ATIVO

    public Jogador(){}

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getWhats() {
        return whats;
    }
    public void setWhats(String whats) {
        this.whats = whats;
    }

    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getContJogos() {
        return contJogos;
    }
    public void setContJogos(int contJogos) {
        this.contJogos = contJogos;
    }

    public int getContGols() {
        return contGols;
    }
    public void setContGols(int contGols) {
        this.contGols = contGols;
    }

    public int getContAssist() {
        return contAssist;
    }
    public void setContAssist(int contAssist) {
        this.contAssist = contAssist;
    }

    public int getNivel() {
        return nivel;
    }
    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public int getAtivo() {
        return ativo;
    }
    public void setAtivo(int ativo) {
        this.ativo = ativo;
    }
}
