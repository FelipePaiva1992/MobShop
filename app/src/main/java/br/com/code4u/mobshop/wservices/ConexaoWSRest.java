/**
 * @author FELIPE
 * 
 * A CLASSE TEM METODOS QUE OBTEM OS OBJETOS JSONREADER DA CLASSE "RESTConnection" E EFETUA O PARSE NOS MESMOS!
 * POPULANDO OS OBJETOS NECESSARIOS E OS RETORNANDO! 
 */
package br.com.code4u.mobshop.wservices;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;

import android.app.Activity;
import android.util.JsonReader;
import br.com.code4u.mobshop.model.Cliente;
import br.com.code4u.mobshop.model.Produto;
import br.com.code4u.mobshop.model.ProdutoCapacidade;
import br.com.code4u.mobshop.model.ProdutoCor;
import br.com.code4u.mobshop.model.StatusAdyen;
import br.com.code4u.mobshop.model.TipoPagamento;
import br.com.code4u.mobshop.model.Venda;
import br.com.code4u.mobshop.model.VendaItem;
import br.com.code4u.mobshop.model.Vendedor;
import br.com.code4u.mobshop.util.LogUtil;

public class ConexaoWSRest {

	public Boolean cadastrarCliente(List<NameValuePair> params, Activity activity) throws IOException, FileNotFoundException, ConnectException {

		RESTConnection connection = new RESTConnection();

		JsonReader reader = connection.request(RESTActions.CADASTRAR_CLIENTE, params, RESTConnection.METHOD_PUT, activity);

		try {
			parseCliente(reader);
			return true;
		} catch (Exception e) {
			LogUtil.printError(e);
			return false;
		}
	}
	
	public Cliente consultarCliente(List<NameValuePair> params, Activity activity) throws IOException, FileNotFoundException, ConnectException {

		Cliente cliente;

		RESTConnection connection = new RESTConnection();

		JsonReader reader = connection.request(RESTActions.BUSCAR_CLIENTE, params, RESTConnection.METHOD_GET, activity);

		cliente = parseCliente(reader);
		
		return cliente;
	}
	
	public Vendedor acessarVendedor(List<NameValuePair> params, Activity activity) throws IOException, FileNotFoundException, ConnectException {

		Vendedor vendedor;

		RESTConnection connection = new RESTConnection();

		JsonReader reader = connection.request(RESTActions.LOGAR_VENDEDOR, params, RESTConnection.METHOD_POST, activity);

		vendedor = parseVendedor(reader);
		
		return vendedor;
	}
	
	public Venda iniciarVenda(List<NameValuePair> params, Activity activity) throws IOException, FileNotFoundException, ConnectException {

		Venda venda;

		RESTConnection connection = new RESTConnection();

		JsonReader reader = connection.request(RESTActions.INICIAR_VENDA, params, RESTConnection.METHOD_GET, activity);

		venda = parseVenda(reader);

		
		return venda;
	}
	
	public Venda finalizarVenda(List<NameValuePair> params, Activity activity) throws IOException, FileNotFoundException, ConnectException {

		Venda venda;

		RESTConnection connection = new RESTConnection();

		JsonReader reader = connection.request(RESTActions.FINALIZAR_VENDA, params, RESTConnection.METHOD_GET, activity);

		venda = parseVenda(reader);
		
		return venda;
	}
	
	public Venda pagarVenda(List<NameValuePair> params, Activity activity) throws IOException, FileNotFoundException, ConnectException {

		Venda venda;

		RESTConnection connection = new RESTConnection();

		JsonReader reader = connection.request(RESTActions.PAGAR_VENDA, params, RESTConnection.METHOD_POST, activity);

		venda = parseVenda(reader);
		
		return venda;
	}
	
	public Produto buscarProduto(List<NameValuePair> params, Activity activity) throws IOException, FileNotFoundException, ConnectException {

		RESTConnection connection = new RESTConnection();

		JsonReader reader = connection.request(RESTActions.BUSCAR_PRODUTO, params, RESTConnection.METHOD_GET, activity);

		Produto produto;
		
		produto = parseProduto(reader);

		return produto;
	}
	
	public void adicionarProdutoVenda(List<NameValuePair> params, Activity activity) throws IOException, FileNotFoundException, ConnectException, Exception {

		RESTConnection connection = new RESTConnection();

		connection.request(RESTActions.INSERIR_PRODUTO_VENDA, params, RESTConnection.METHOD_PUT, activity);
	}

	public List<VendaItem> buscarProdutosVenda(List<NameValuePair> params, Activity activity) throws Exception {

		
		List<VendaItem> vendaItems = new ArrayList<>();

		RESTConnection connection = new RESTConnection();

		JsonReader reader = connection.request(RESTActions.BUSCAR_PRODUTOS_VENDA, params, RESTConnection.METHOD_GET, activity);

		reader.beginArray();

		while (reader.hasNext()) {
			vendaItems.add(parseVendaItem(reader));
		}
		
		reader.endArray();


		return vendaItems;
	}
	
	public void removerProdutoVenda(List<NameValuePair> params, Activity activity) throws IOException, FileNotFoundException, ConnectException, Exception {

		RESTConnection connection = new RESTConnection();

		connection.request(RESTActions.REMOVER_PRODUTO_VENDA, params, RESTConnection.METHOD_DELETE, activity);
	}
	
	public StatusAdyen analisarAutorizacaoVenda(Activity activity) throws IOException, FileNotFoundException, ConnectException, Exception {

		
		StatusAdyen statusAdyen;

		RESTConnection connection = new RESTConnection();

		JsonReader reader = connection.request(RESTActions.AUTORIZAR_VENDA, RESTConnection.METHOD_GET, activity);

		reader.beginArray();

		statusAdyen = parseStatusAdyen(reader);
		
		reader.endArray();


		return statusAdyen;
	}
	
	//PARSERS//
	private ProdutoCapacidade parseProdutoCapacidade(JsonReader reader) throws IOException{

		ProdutoCapacidade produtoCapacidade = new ProdutoCapacidade();
		String name;

		reader.beginObject();
		while (reader.hasNext()) {
			name = reader.nextName();

			if (name.equals("idTamanho")) {
				produtoCapacidade.setIdTamanho(reader.nextInt());
			} else if (name.equals("dsTamanho")) {
				produtoCapacidade.setDsTamanho(reader.nextString());
			}
		}
		reader.endObject();
		
		return produtoCapacidade;
	}
	
	private ProdutoCor parseProdutoCor(JsonReader reader) throws IOException{
		
		ProdutoCor produtoCor = new ProdutoCor();
		String name;
		
		reader.beginObject();
		while (reader.hasNext()) {
			name = reader.nextName();

			if (name.equals("idCor")) {
				produtoCor.setIdCor(reader.nextInt());
			} else if (name.equals("dsCor")) {
				produtoCor.setDsCor(reader.nextString());
			} 
		}
		reader.endObject();
		
		return produtoCor;
	}
	
	private Produto parseProduto(JsonReader reader) throws IOException{
		
		Produto produto = new Produto();
		String name;
		
		reader.beginObject();
		while (reader.hasNext()) {
			name = reader.nextName();

            switch (name) {
                case "idRefProduto":
                    produto.setIdRefProduto(reader.nextLong());
                    break;
                case "codBarras":
                    produto.setCodBarras(reader.nextString());
                    break;
                case "mrProduto":
                    produto.setMrProduto(reader.nextString());
                    break;
                case "nmProduto":
                    produto.setNmProduto(reader.nextString());
                    break;
                case "prProduto":
                    produto.setPrProduto(reader.nextDouble());
                    break;
                case "vlQuantidade":
                    produto.setVlQuantidade(reader.nextInt());
                    break;
                case "produtoCor":
                    produto.setProdutoCor(parseProdutoCor(reader));
                    break;
                case "produtoCapacidade":
                    produto.setProdutoCapacidade(parseProdutoCapacidade(reader));
                    break;
            }
		}
		reader.endObject();
		
		return produto;
	}
	
	private VendaItem parseVendaItem(JsonReader reader) throws IOException{
		
		VendaItem vendaItem = new VendaItem();
		String name;
		
		reader.beginObject();
		while (reader.hasNext()) {
			name = reader.nextName();
            switch (name) {
                case "idVendaItem":
                    vendaItem.setIdVendaItem(reader.nextLong());
                    break;
                case "vlQuantidade":
                    vendaItem.setVlQuantidade(reader.nextInt());
                    break;
                case "produto":
                    vendaItem.setProduto(parseProduto(reader));
                    break;
            }
		}
		reader.endObject();
					
		return vendaItem;
	}
	
	private Cliente parseCliente(JsonReader reader) throws IOException{
		
		Cliente cliente = new Cliente();
		String name;
		
		reader.beginObject();
		while (reader.hasNext()) {
			name = reader.nextName();

            switch (name) {
                case "idCliente":
                    cliente.setIdCliente(reader.nextLong());
                    break;
                case "cpfCliente":
                    cliente.setCpfCliente(reader.nextString());
                    break;
                case "nmCliente":
                    cliente.setNmCliente(reader.nextString());
                    break;
                case "logradouro":
                    cliente.setLogradouro(reader.nextString());
                    break;
                case "endereco":
                    cliente.setEndereco(reader.nextString());
                    break;
                case "numero":
                    cliente.setNumero(reader.nextString());
                    break;
                case "bairro":
                    cliente.setBairro(reader.nextString());
                    break;
                case "cidade":
                    cliente.setCidade(reader.nextString());
                    break;
                case "estado":
                    cliente.setEstado(reader.nextString());
                    break;
                case "email":
                    cliente.setEmail(reader.nextString());
                    break;
                case "vlTelefone":
                    cliente.setVlTelefone(reader.nextString());
                    break;
                case "dtNascimento":
                    cliente.setDtNascimento(reader.nextString());
                    break;
            }
		}
		reader.endObject();
		
		return cliente;
	}
	
	private Vendedor parseVendedor(JsonReader reader)throws IOException{
		
		Vendedor vendedor = new Vendedor();
		String name;
		
		reader.beginObject();
		while (reader.hasNext()) {
			name = reader.nextName();

            switch (name) {
                case "idVendedor":
                    vendedor.setIdVendedor(reader.nextInt());
                    break;
                case "nmVendedor":
                    vendedor.setNmVendedor(reader.nextString());
                    break;
                case "vlSenha":
                    vendedor.setVlSenha(reader.nextString());
                    break;
            }
		}
		reader.endObject();
		
		return vendedor;
	}
	
	private Venda parseVenda(JsonReader reader) throws IOException{
		
		Venda venda = new Venda();
		String name;
		
		reader.beginObject();
		while (reader.hasNext()) {
			name = reader.nextName();

            switch (name) {
                case "idVenda":
                    venda.setIdVenda(reader.nextLong());
                    break;
                case "dtVenda":
                    venda.setDtVenda(new Date(reader.nextLong()));
                    break;
                case "vfEntrege":
                    venda.setVfEntrege(reader.nextBoolean());
                    break;
                case "vfCancelado":
                    venda.setVfCancelado(reader.nextBoolean());
                    break;
                case "vfPago":
                    venda.setVfPago(reader.nextBoolean());
                    break;
                case "cliente":
                    venda.setCliente(parseCliente(reader));
                    break;
                case "vendedor":
                    venda.setVendedor(parseVendedor(reader));
                    break;
                case "tipoPagamento":
                    venda.setTipoPagamento(parseTipoPagamento(reader));
                    break;
                case "vlCodigoConfirmacao":
                    venda.setVlCodigoConfirmacao(reader.nextString());
                    break;
            }
		}
		reader.endObject();
		
		return venda;
	}
	
	private TipoPagamento parseTipoPagamento (JsonReader reader) throws IOException{
		
		TipoPagamento tipoPagamento = new TipoPagamento();
		String name;
		
		reader.beginObject();
		while (reader.hasNext()) {
			name = reader.nextName();

			if (name.equals("idTipoPagamento")) {
				tipoPagamento.setIdTipoPagamento(reader.nextInt());
			} else if (name.equals("nmTipoPagamento")) {
				tipoPagamento.setNmTipoPagamento(reader.nextString());
			} 
		}		
		reader.endObject();
		return tipoPagamento;
	}
	
	private StatusAdyen parseStatusAdyen (JsonReader reader) throws IOException{
		
		StatusAdyen statusAdyen = new StatusAdyen();
		String name;
		
		reader.beginObject();
		while (reader.hasNext()) {
			name = reader.nextName();

            switch (name) {
                case "pspReference":
                    statusAdyen.setPspReference(reader.nextString());
                    break;
                case "resultCode":
                    statusAdyen.setResultCode(reader.nextString());
                    break;
                case "authCode":
                    statusAdyen.setAuthCode(reader.nextString());
                    break;
                case "refusalReason":
                    statusAdyen.setRefusalReason(reader.nextString());
                    break;
            }
		}		
		reader.endObject();
		return statusAdyen;
	}
	
}