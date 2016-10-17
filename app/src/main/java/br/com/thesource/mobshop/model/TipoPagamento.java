package br.com.thesource.mobshop.model;

import java.io.Serializable;

public class TipoPagamento implements Serializable {

	private static final long serialVersionUID = 1L;

	private int idTipoPagamento;
	
	private String nmTipoPagamento;
	
	private Venda venda;

	public int getIdTipoPagamento() {
		return idTipoPagamento;
	}

	public void setIdTipoPagamento(int idTipoPagamento) {
		this.idTipoPagamento = idTipoPagamento;
	}

	public String getNmTipoPagamento() {
		return nmTipoPagamento;
	}

	public void setNmTipoPagamento(String nmTipoPagamento) {
		this.nmTipoPagamento = nmTipoPagamento;
	}

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}
	
	
	
}
