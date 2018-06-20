package br.iesb.mobile.alunoonline.Model;

public class Lista {

    private String nome;
    private double preco;

    public Lista (){}

    public Lista(String nome, double preco){
        this.nome = nome;
        this.preco = preco;
    }

    public Lista(String nome){
        this.nome = nome;
        this.preco = preco;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
