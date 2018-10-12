package br.iesb.mobile.alunoonline.Model;

import java.math.BigDecimal;

public class Mercado {

    private String nome;
    private BigDecimal distancia;
    private double preco;

    public Mercado(){}
    public Mercado(double preco) {this.preco = preco;}

    public Mercado(String nome, BigDecimal distancia, double preco) {
        this.nome = nome;
        this.distancia = distancia;
        this.preco = preco;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getDistancia() {
        return distancia;
    }

    public void setDistancia(BigDecimal distancia) {
        this.distancia = distancia;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }
}
