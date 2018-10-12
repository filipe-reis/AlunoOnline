package br.iesb.mobile.alunoonline.Model;

public class MercadoAPI {

    private Long mercado_id;
    private String nome;
    private String descricao;
    private String cnpj;

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
}
