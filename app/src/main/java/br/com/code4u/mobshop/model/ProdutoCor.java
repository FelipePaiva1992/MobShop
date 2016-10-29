package br.com.code4u.mobshop.model;

import java.io.Serializable;
import java.util.List;


/**
 * The persistent class for the produto_cor database table.
 * 
 */
public class ProdutoCor implements Serializable {
	private static final long serialVersionUID = 1L;

	private int idCor;

	private String dsCor;

	private List<Produto> produtos;

	public ProdutoCor() {
	}

	public int getIdCor() {
		return this.idCor;
	}

	public void setIdCor(int idCor) {
		this.idCor = idCor;
	}

	public String getDsCor() {
		return this.dsCor;
	}

	public void setDsCor(String dsCor) {
		this.dsCor = dsCor;
	}

	List<Produto> getProdutos() {
		return this.produtos;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}

	public Produto addProduto(Produto produto) {
		getProdutos().add(produto);
		produto.setProdutoCor(this);

		return produto;
	}

	public Produto removeProduto(Produto produto) {
		getProdutos().remove(produto);
		produto.setProdutoCor(null);

		return produto;
	}

}