package br.com.code4u.mobshop.adapter.model;

/**
 * Created by felipepaiva on 14/01/15.
 */
public class ListaProduto {

    private long id;
    private String nomeProduto;
    private int quantidadeProduto;
    private Double totalProduto;
    private String removerProduto;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public int getQuantidadeProduto() {
        return quantidadeProduto;
    }

    public void setQuantidadeProduto(int quantidadeProduto) {
        this.quantidadeProduto = quantidadeProduto;
    }

    public Double getTotalProduto() {
        return totalProduto;
    }

    public void setTotalProduto(Double totalProduto) {
        this.totalProduto = totalProduto;
    }

    public String getRemoverProduto() {
        return removerProduto;
    }

    public void setRemoverProduto(String removerProduto) {
        this.removerProduto = removerProduto;
    }
}
