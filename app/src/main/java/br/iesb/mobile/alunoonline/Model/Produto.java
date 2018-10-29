package br.iesb.mobile.alunoonline.Model;

import java.io.Serializable;

public class Produto implements Serializable{

    // Produto
    private String produto_id;
    private String codigo_barras;
    private String nome;
    private String descricao;
    private Double preco;

    //Sku
    private String sku;
    private String sku_id;
    private String sku_descricao;
    private Double tamanho;
    private String unidade;
    private String marca;
    private String qtd_stock;
    private String status;

    //Categoria
    private String categoria_id;
    private String categoria_nome;
    private String categoria_descricao;

    //Mercado
    private String mercado_id;
    private String mercado_nome;
    private String mercado_descricao;
    private String cnpj;
    private double latitude;
    private double longitude;

    private int qtd;

    public int getQtd() {
        return qtd;
    }

    public void setQtd(int qtd) {
        this.qtd = qtd;
    }

    public String getProduto_id() {
        return produto_id;
    }

    public void setProduto_id(String produto_id) {
        this.produto_id = produto_id;
    }

    public String getCodigo_barras() {
        return codigo_barras;
    }

    public void setCodigo_barras(String codigo_barras) {
        this.codigo_barras = codigo_barras;
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

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getSku_id() {
        return sku_id;
    }

    public void setSku_id(String sku_id) {
        this.sku_id = sku_id;
    }

    public String getSku_descricao() {
        return sku_descricao;
    }

    public void setSku_descricao(String sku_descricao) {
        this.sku_descricao = sku_descricao;
    }

    public Double getTamanho() {
        return tamanho;
    }

    public void setTamanho(Double tamanho) {
        this.tamanho = tamanho;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getQtd_stock() {
        return qtd_stock;
    }

    public void setQtd_stock(String qtd_stock) {
        this.qtd_stock = qtd_stock;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategoria_id() {
        return categoria_id;
    }

    public void setCategoria_id(String categoria_id) {
        this.categoria_id = categoria_id;
    }

    public String getCategoria_nome() {
        return categoria_nome;
    }

    public void setCategoria_nome(String categoria_nome) {
        this.categoria_nome = categoria_nome;
    }

    public String getCategoria_descricao() {
        return categoria_descricao;
    }

    public void setCategoria_descricao(String categoria_descricao) {
        this.categoria_descricao = categoria_descricao;
    }

    public String getMercado_id() {
        return mercado_id;
    }

    public void setMercado_id(String mercado_id) {
        this.mercado_id = mercado_id;
    }

    public String getMercado_nome() {
        return mercado_nome;
    }

    public void setMercado_nome(String mercado_nome) {
        this.mercado_nome = mercado_nome;
    }

    public String getMercado_descricao() {
        return mercado_descricao;
    }

    public void setMercado_descricao(String mercado_descricao) {
        this.mercado_descricao = mercado_descricao;
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
