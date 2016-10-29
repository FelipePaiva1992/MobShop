package br.com.code4u.mobshop.model;

import java.io.Serializable;
import java.util.List;


/**
 * The persistent class for the vendedor database table.
 * 
 */
public class Vendedor implements Serializable {
	private static final long serialVersionUID = 1L;

	private int idVendedor;

	private String nmVendedor;
	
	private String vlSenha;

	private List<Venda> vendas;

	public Vendedor() {
	}

	public int getIdVendedor() {
		return this.idVendedor;
	}

	public void setIdVendedor(int idVendedor) {
		this.idVendedor = idVendedor;
	}

	public String getNmVendedor() {
		return this.nmVendedor;
	}

	public void setNmVendedor(String nmVendedor) {
		this.nmVendedor = nmVendedor;
	}

	List<Venda> getVendas() {
		return this.vendas;
	}

	public void setVendas(List<Venda> vendas) {
		this.vendas = vendas;
	}

	public Venda addVenda(Venda venda) {
		getVendas().add(venda);
		venda.setVendedor(this);

		return venda;
	}

	public Venda removeVenda(Venda venda) {
		getVendas().remove(venda);
		venda.setVendedor(null);

		return venda;
	}

	public String getVlSenha() {
		return vlSenha;
	}

	public void setVlSenha(String vlSenha) {
		this.vlSenha = vlSenha;
	}

}