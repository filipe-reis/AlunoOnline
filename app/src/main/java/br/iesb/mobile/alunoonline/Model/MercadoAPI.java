package br.iesb.mobile.alunoonline.Model;

import java.io.Serializable;
import java.util.Comparator;

public class MercadoAPI implements Serializable{

    private Long mercado_id;
    private String nome;
    private String descricao;
    private String cnpj;
    private double latitude;
    private double longitude;

    //Atributo interno para definir o preco total da lista para cada mercado
    private double preco;
    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public Long getMercado_id() {
        return mercado_id;
    }

    public void setMercado_id(Long mercado_id) {
        this.mercado_id = mercado_id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
