package br.iesb.mobile.alunoonline;

import com.google.firebase.database.DatabaseReference;

/**
 * Created by Filipe Reis on 29/04/2018.
 */

public class Produtos {

    private String produto, desc, marca;
    private String preco;


    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getNome() {
        return produto;
    }

    public void setNome(String nome) { this.produto = nome; }

    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}