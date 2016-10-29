package br.com.code4u.mobshop.model;

import java.io.Serializable;
import java.util.List;


/**
 * The persistent class for the produto database table.
 * 
 */
public class Produto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long idRefProduto;

	private String codBarras;

	private String mrProduto;

	private String nmProduto;

	private Double prProduto;
	
	private int vlQuantidade;

	private ProdutoCor produtoCor;

	private ProdutoCapacidade produtoCapacidade;

	private List<VendaItem> vendaItems;

	public Produto() {
	}

	public Long getIdRefProduto() {
		return this.idRefProduto;
	}

	public void setIdRefProduto(Long idRefProduto) {
		this.idRefProduto = idRefProduto;
	}

	public String getCodBarras() {
		return this.codBarras;
	}

	public void setCodBarras(String codBarras) {
		this.codBarras = codBarras;
	}

	public String getMrProduto() {
		return this.mrProduto;
	}

	public void setMrProduto(String mrProduto) {
		this.mrProduto = mrProduto;
	}

	public String getNmProduto() {
		return this.nmProduto;
	}

	public void setNmProduto(String nmProduto) {
		this.nmProduto = nmProduto;
	}

	public Double getPrProduto() {
		return this.prProduto;
	}

	public int getVlQuantidade() {
		return vlQuantidade;
	}
	
	public void setVlQuantidade(int vlQuantidade) {
		this.vlQuantidade = vlQuantidade;
	}


	public void setPrProduto(Double prProduto) {
		this.prProduto = prProduto;
	}

	public ProdutoCor getProdutoCor() {
		return this.produtoCor;
	}

	public void setProdutoCor(ProdutoCor produtoCor) {
		this.produtoCor = produtoCor;
	}

	public ProdutoCapacidade getProdutoTamanho() {
		return this.produtoCapacidade;
	}

	public void setProdutoCapacidade(ProdutoCapacidade produtoCapacidade) {
		this.produtoCapacidade = produtoCapacidade;
	}

	List<VendaItem> getVendaItems() {
		return this.vendaItems;
	}

	public void setVendaItems(List<VendaItem> vendaItems) {
		this.vendaItems = vendaItems;
	}

	public VendaItem addVendaItem(VendaItem vendaItem) {
		getVendaItems().add(vendaItem);
		vendaItem.setProduto(this);

		return vendaItem;
	}

	public VendaItem removeVendaItem(VendaItem vendaItem) {
		getVendaItems().remove(vendaItem);
		vendaItem.setProduto(null);

		return vendaItem;
	}

}