package br.iesb.mobile.alunoonline.Model;

import java.math.BigDecimal;

public class Mercado {

    private String nome;
    private BigDecimal distancia;
    private BigDecimal preco;

    public Mercado() {}

    public Mercado(String nome, BigDecimal distancia, BigDecimal preco) {
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

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }
}
