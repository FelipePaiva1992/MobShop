package br.com.code4u.mobshop.wservices;

class RESTActions {

	//private static final String HOST = "http://wsmobshop.feehpaiva1992.cloudbees.net/";
	//private static final String HOST = "http://192.168.43.179:8200/WSMobShop/"; //JBOSS
	//private static final String HOST = "http://192.168.43.179:8080/WebServiceMS/"; //TOMCAT
	//private static final String HOST = "http://192.168.1.36:8200/WSMobShop/";
	//private static final String HOST = "http://thesource.com.br/WebServiceMS/"; // TOMCAT THE SOURCE
    //private static final String HOST = "http://192.168.1.36:8080/WebServiceMS/"; // TOMCAT LOCAL
    //private static final String HOST = "http://54.191.197.37:9080/WebServiceMS/"; // TOMCAT thesource
    private static final String HOST = "http://35.161.75.109/WebServiceMS/"; // TOMCAT thesource
	
	public static final String LOGAR_VENDEDOR = HOST+"rest/endpointVendedor/logarVendedor";

    public static final String BUSCAR_PRODUTO = HOST+"rest/endpointProduto/encontrarProduto";

    public static final String BUSCAR_CLIENTE = HOST+"rest/endpointCliente/encontrarCliente";

    public static final String CADASTRAR_CLIENTE = HOST+"rest/endpointCliente/inserirCliente";

    public static final String INSERIR_PRODUTO_VENDA = HOST+"rest/endpointVendaItem/inserirProdutoNaVenda";

    public static final String REMOVER_PRODUTO_VENDA = HOST+"rest/endpointVendaItem/removerProdutoDaVenda";

    public static final String INICIAR_VENDA = HOST+"rest/endpointVenda/iniciarVenda";

    public static final String FINALIZAR_VENDA = HOST+"rest/endpointVenda/encerrarVenda";
    
    public static final String PAGAR_VENDA = HOST+"rest/endpointVenda/pagarVenda";

    public static final String BUSCAR_PRODUTOS_VENDA = HOST + "rest/endpointVendaItem/encontrarProdutosNaVenda";
    
    public static final String ACORDAR_SERVIDOR = HOST + "rest/endpointUtil/acordarServidor";
    
    public static final String AUTORIZAR_VENDA = HOST + "rest/endpointCartao/autorizacaoVenda";
    
//	private static final String HOST = "http://ws-mobshop.rhcloud.com/";
//
//	public static final String LOGAR_VENDEDOR = HOST+"WSPOSMobile/rest/endpointVendedor/logarVendedor";
//
//    public static final String BUSCAR_PRODUTO = HOST+"WSPOSMobile/rest/endpointProduto/findProduto";
//
//    public static final String BUSCAR_CLIENTE = HOST+"WSPOSMobile/rest/endpointCliente/findCliente";
//
//    public static final String CADASTRAR_CLIENTE = HOST+"WSPOSMobile/rest/endpointCliente/cadastrarCliente";
//
//    public static final String INSERIR_PRODUTO_VENDA = HOST+"WSPOSMobile/rest/endpointVendaItem/insertProdutoVenda";
//
//    public static final String REMOVER_PRODUTO_VENDA = HOST+"WSPOSMobile/rest/endpointVendaItem/removeProdutoVenda";
//
//    public static final String INICIAR_VENDA = HOST+"WSPOSMobile/rest/endpointVenda/initVenda";
//
//    public static final String FINALIZAR_VENDA = HOST+"WSPOSMobile/rest/endpointVenda/endVenda";
//    
//    public static final String PAGAR_VENDA = HOST+"WSPOSMobile/rest/endpointVenda/pagarVenda";
//
//    public static final String BUSCAR_PRODUTOS_VENDA = HOST + "WSPOSMobile/rest/endpointVendaItem/findProdutosVenda";

}

