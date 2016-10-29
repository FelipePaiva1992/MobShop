package br.com.code4u.mobshop.model;

import java.io.Serializable;


/**
 * The persistent class for the venda_item database table.
 * 
 */
public class VendaItem implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long idVendaItem;

	private int vlQuantidade;

	private Produto produto;

	private Venda venda;

	public VendaItem() {
	}

	public Long getIdVendaItem() {
		return this.idVendaItem;
	}

	public void setIdVendaItem(Long idVendaItem) {
		this.idVendaItem = idVendaItem;
	}

	public int getVlQuantidade() {
		return this.vlQuantidade;
	}

	public void setVlQuantidade(int vlQuantidade) {
		this.vlQuantidade = vlQuantidade;
	}

	public Produto getProduto() {
		return this.produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Venda getVenda() {
		return this.venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}

}