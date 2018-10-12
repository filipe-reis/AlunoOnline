package br.iesb.mobile.alunoonline.Model;

import java.math.BigDecimal;

public class Sku {

    private Long sku_id;
    private String sku;
    private String descricao;
    private BigDecimal tamanho; //Capacidade do produto
    private String unidade; //Unidade de medida. Ex.: L, mL, Kg
    private String marca;
    private int qtd_stock;
    private String status;
    private double latitude; //Localizacao do produto no mercado.
    private double longitude;

    public Long getSku_id() {
        return sku_id;
    }

    public void setSku_id(Long sku_id) {
        this.sku_id = sku_id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getTamanho() {
        return tamanho;
    }

    public void setTamanho(BigDecimal tamanho) {
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

    public int getQtd_stock() {
        return qtd_stock;
    }

    public void setQtd_stock(int qtd_stock) {
        this.qtd_stock = qtd_stock;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
