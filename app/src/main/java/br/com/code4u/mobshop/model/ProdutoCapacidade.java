package br.com.code4u.mobshop.model;

import java.io.Serializable;
import java.util.List;


/**
 * The persistent class for the produto_tamanho database table.
 * 
 */
public class ProdutoCapacidade implements Serializable {
	private static final long serialVersionUID = 1L;

	private int idTamanho;

	private String dsTamanho;

	private List<Produto> produtos;

	public ProdutoCapacidade() {
	}

	public int getIdTamanho() {
		return this.idTamanho;
	}

	public void setIdTamanho(int idTamanho) {
		this.idTamanho = idTamanho;
	}

	public String getDsTamanho() {
		return this.dsTamanho;
	}

	public void setDsTamanho(String dsTamanho) {
		this.dsTamanho = dsTamanho;
	}

	List<Produto> getProdutos() {
		return this.produtos;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}

	public Produto addProduto(Produto produto) {
		getProdutos().add(produto);
		produto.setProdutoCapacidade(this);

		return produto;
	}

	public Produto removeProduto(Produto produto) {
		getProdutos().remove(produto);
		produto.setProdutoCapacidade(null);

		return produto;
	}

}