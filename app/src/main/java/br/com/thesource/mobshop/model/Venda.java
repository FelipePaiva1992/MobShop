package br.com.thesource.mobshop.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the venda database table.
 * 
 */
public class Venda implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long idVenda;

	private Date dtVenda;

	private Boolean vfEntrege;

	private Boolean vfPago;
	
	private Boolean vfCancelado;

	private String vlCodigoConfirmacao;
	
	private Cliente cliente;

	private Vendedor vendedor;

	private List<VendaItem> vendaItems;
		
	private TipoPagamento tipoPagamento;

	public Venda() {
	}

	public Long getIdVenda() {
		return this.idVenda;
	}

	public void setIdVenda(Long idVenda) {
		this.idVenda = idVenda;
	}

	public Date getDtVenda() {
		return this.dtVenda;
	}

	public void setDtVenda(Date dtVenda) {
		this.dtVenda = dtVenda;
	}

	public Boolean getVfEntrege() {
		return this.vfEntrege;
	}

	public void setVfEntrege(Boolean vfEntrege) {
		this.vfEntrege = vfEntrege;
	}

	public Boolean getVfPago() {
		return this.vfPago;
	}

	public void setVfPago(Boolean vfPago) {
		this.vfPago = vfPago;
	}

	public Boolean getVfCancelado() {
		return vfCancelado;
	}

	public void setVfCancelado(Boolean vfCancelado) {
		this.vfCancelado = vfCancelado;
	}
	
	public String getVlCodigoConfirmacao() {
		return vlCodigoConfirmacao;
	}

	public void setVlCodigoConfirmacao(String vlCodigoConfirmacao) {
		this.vlCodigoConfirmacao = vlCodigoConfirmacao;
	}

	public Cliente getCliente() {
		return this.cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Vendedor getVendedor() {
		return this.vendedor;
	}

	public void setVendedor(Vendedor vendedor) {
		this.vendedor = vendedor;
	}

	List<VendaItem> getVendaItems() {
		return this.vendaItems;
	}

	public void setVendaItems(List<VendaItem> vendaItems) {
		this.vendaItems = vendaItems;
	}

	public TipoPagamento getTipoPagamento() {
		return tipoPagamento;
	}

	public void setTipoPagamento(TipoPagamento tipoPagamento) {
		this.tipoPagamento = tipoPagamento;
	}
	
	public VendaItem addVendaItem(VendaItem vendaItem) {
		getVendaItems().add(vendaItem);
		vendaItem.setVenda(this);

		return vendaItem;
	}

	public VendaItem removeVendaItem(VendaItem vendaItem) {
		getVendaItems().remove(vendaItem);
		vendaItem.setVenda(null);

		return vendaItem;
	}

}