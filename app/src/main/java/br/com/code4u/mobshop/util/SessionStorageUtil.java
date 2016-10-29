package br.com.code4u.mobshop.util;

public class SessionStorageUtil {

	/* VENDEDOR */
	private static String idVendedor;
	private static String nmVendedor;
	
	/* CLIENTE */
	private static String cpfCliente;
	private static String nmCliente;
	private static String dsEndereco;
	private static String vlTelefone;	
	
	/* VENDA */
	private static String idVenda;
	private static Double vlVenda;
	
	public static String getIdVendedor() {
		return idVendedor;
	}
	public static void setIdVendedor(String idVendedor) {
		SessionStorageUtil.idVendedor = idVendedor;
	}
	public static String getNmVendedor() {
		return nmVendedor;
	}
	public static void setNmVendedor(String nmVendedor) {
		SessionStorageUtil.nmVendedor = nmVendedor;
	}
	public static String getCpfCliente() {
		return cpfCliente;
	}
	public static void setCpfCliente(String cpfCliente) {
		SessionStorageUtil.cpfCliente = cpfCliente;
	}
	public static String getNmCliente() {
		return nmCliente;
	}
	public static void setNmCliente(String nmCliente) {
		SessionStorageUtil.nmCliente = nmCliente;
	}	
	public static String getDsEndereco() {
		return dsEndereco;
	}
	public static void setDsEndereco(String dsEndereco) {
		SessionStorageUtil.dsEndereco = dsEndereco;
	}
	public static String getVlTelefone() {
		return vlTelefone;
	}
	public static void setVlTelefone(String vlTelefone) {
		SessionStorageUtil.vlTelefone = vlTelefone;
	}
	public static String getIdVenda() {
		return idVenda;
	}
	public static void setIdVenda(String idVenda) {
		SessionStorageUtil.idVenda = idVenda;
	}
	public static Double getVlVenda() {
		return vlVenda;
	}
	public static void setVlVenda(Double vlVenda) {
		SessionStorageUtil.vlVenda = vlVenda;
	}
	
	public static void cleanVenda(){
		cpfCliente = null;
		nmCliente = null;
		idVenda = null;
		vlVenda = null;
	}
	
	public static void cleanSessao(){
		cpfCliente = null;
		nmCliente = null;
		idVenda = null;
		idVendedor = null;
		nmVendedor = null;
		vlVenda = null;
	}
}
