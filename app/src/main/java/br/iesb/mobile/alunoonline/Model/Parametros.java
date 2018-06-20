package br.iesb.mobile.alunoonline.Model;

public class Parametros {

    private String nome_lista;
    private double valorTotalLista;

    public Parametros(){}

    public Parametros(String nome_lista) {
        this.nome_lista = nome_lista;
    }

    public Parametros(String nome_lista, double valorTotalLista){this.nome_lista = nome_lista; this.valorTotalLista = valorTotalLista;}

    public String getNome_lista() {
        return nome_lista;
    }

    public void setNome_lista(String nome_lista) {
        this.nome_lista = nome_lista;
    }

    public double getValorTotalLista() {
        return valorTotalLista;
    }

    public void setValorTotalLista(double valorTotalLista) {
        this.valorTotalLista = valorTotalLista;
    }
}
