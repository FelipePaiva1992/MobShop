package br.com.thesource.mobshop.model;

public class Sessao {

	private int id;
	private String cod_vendedor;
	private String cpf_cliente;
	private String cod_venda;
	
	public Sessao(int id, String cod_vendedor, String cod_venda, String cpf_cliente){
		this.id = id;
		this.cod_venda = cod_venda;
		this.cod_vendedor = cod_vendedor;
		this.cpf_cliente = cpf_cliente;
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCod_vendedor() {
		return cod_vendedor;
	}
	public void setCod_vendedor(String cod_vendedor) {
		this.cod_vendedor = cod_vendedor;
	}
	public String getCpf_cliente() {
		return cpf_cliente;
	}
	public void setCpf_cliente(String cpf_cliente) {
		this.cpf_cliente = cpf_cliente;
	}
	public String getCod_venda() {
		return cod_venda;
	}
	public void setCod_venda(String cod_venda) {
		this.cod_venda = cod_venda;
	}
	
	
	
	
}
