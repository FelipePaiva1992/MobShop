package br.com.code4u.mobshop.view;

import java.net.ConnectException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import br.com.code4u.mobshop.R;
import br.com.code4u.mobshop.model.VendaItem;
import br.com.code4u.mobshop.util.AlterAllFontsInActivity;
import br.com.code4u.mobshop.util.ConnectionUtil;
import br.com.code4u.mobshop.util.LogUtil;
import br.com.code4u.mobshop.util.SessionStorageUtil;
import br.com.code4u.mobshop.wservices.ConexaoWSRest;

import com.square.MagRead;
import com.square.MagReadListener;

public class PagamentoLeitorTarja extends Activity {
	
	private String cod_venda;
    private TextView txtTotalVenda;
    private TextView txtEstadoCartaoTarja;
    private EditText edNmNoCartao;
    private EditText edDigitoVerificador;
    
    private EditText edNumeroCartao;
    private EditText edVencimento;
    private MagRead read;
	private UpdateBytesHandler updateBytesHandler;
    
	private ProgressDialog dialog;
	private final Handler handler = new Handler();

    private static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pagamento_leitor_tarja);
        
        AlterAllFontsInActivity.overrideFonts(this, findViewById(R.id.linearPagamentoLeitorTarja));
        
        txtTotalVenda = (TextView)findViewById(R.id.txtTotalVenda);        
        txtEstadoCartaoTarja = (TextView)findViewById(R.id.txtEstadoCartaoTarja);       
        edNmNoCartao = (EditText)findViewById(R.id.edNmNoCartao);   
        edDigitoVerificador = (EditText)findViewById(R.id.edDigitoVerificador);   
        
        edNumeroCartao = (EditText)findViewById(R.id.edNumeroCartao);
        edNumeroCartao.setEnabled(true);
        edVencimento = (EditText)findViewById(R.id.edVencimento);
        edVencimento.setEnabled(true);
        
    	cod_venda = SessionStorageUtil.getIdVenda();

    	obterTotalVenda();
    	
    	read = new MagRead();
		read.addListener(new MagReadListener() {
			
			@Override
			public void updateBytes(String bytes) {
				Message msg = new Message();
				msg.obj = bytes;
				updateBytesHandler.sendMessage(msg);
			}
			
			@Override
			public void updateBits(String bits) {				
			}
		});
		read.start();
		updateBytesHandler = new UpdateBytesHandler();


        ConnectionUtil.verificaConexao(this);

    }

    private void obterTotalVenda() {
    	new Thread() {
			public void run() {
				List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("idVenda", SessionStorageUtil.getIdVenda())); 	
		    	try {
		    		ConexaoWSRest conexaoWSRest = new ConexaoWSRest();
		    		final List<VendaItem> items = conexaoWSRest.buscarProdutosVenda(params,activity);
		    		calcularTotal(items);

				} catch (ConnectException e) {
					LogUtil.printError(e);
					handler.post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(getBaseContext(),"Verifique a conexão de dados!", Toast.LENGTH_SHORT).show();
						}
					});
				} catch (Exception e) {
					LogUtil.printError(e);
					handler.post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(getBaseContext(),"Erro ao obter o valor total!", Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
    	}.start();		
	}
    

	private void calcularTotal(List<VendaItem> items) {
		
		DecimalFormat decimal = new DecimalFormat( "0.00" ); 
		Double totalCarrinho = 0.0;
		for(VendaItem item:items){
			totalCarrinho += item.getProduto().getPrProduto() * item.getVlQuantidade();
		}
		
		SessionStorageUtil.setVlVenda(Double.valueOf(decimal.format(totalCarrinho).replace(",", ".")));
		
		handler.post(new Runnable() {
			@Override
			public void run() {
		    	txtTotalVenda.setText("Total: R$ " + String.valueOf(SessionStorageUtil.getVlVenda()));
		    	txtEstadoCartaoTarja.setText("Insira o Cartão..");
			}
		});
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.editTextEndereco || super.onOptionsItemSelected(item);
    }


    public void pagamentoCartaoTarjaBtnFinalizar(View v){
    	
    	dialog = ProgressDialog.show(this, "",
				"Efetuando o pagamento da venda, por favor aguarde...", false,
				false);
    	new Thread() {
			public void run() {
		    	try {
		    		ConexaoWSRest conexaoWSRest = new ConexaoWSRest();
		    		//final StatusAdyen statusAdyen = conexaoWSRest.analisarAutorizacaoVenda();

		    		
		    		//if(statusAdyen.getResultCode() == "Authorised"){
		    			List<NameValuePair> params = new ArrayList<>();
	                    params.add(new BasicNameValuePair("idVenda", cod_venda)); 	
	                    params.add(new BasicNameValuePair("codConfirmacao", "123")); 
	                    params.add(new BasicNameValuePair("tipoPagamento", String.valueOf(1))); 
	    		    	try {
	    		    		conexaoWSRest.pagarVenda(params,activity);
	    		    		
	    		    		SessionStorageUtil.cleanVenda();
	    		    		
	    		    		handler.post(new Runnable() {
	    						@Override
	    						public void run() {
	    							Toast.makeText(getBaseContext(),"Pagamento da venda efetuado com sucesso!", Toast.LENGTH_SHORT).show();
	    						}
	    					});
	    		    		
	    		    	    Intent intent = new Intent(PagamentoLeitorTarja.this, InsercaoCPFCliente.class);
	    		    	    startActivity(intent);

	    				} catch (ConnectException e) {
	    					LogUtil.printError(e);
	    					handler.post(new Runnable() {
	    						@Override
	    						public void run() {
	    							Toast.makeText(getBaseContext(),"Verifique a conexão de dados!", Toast.LENGTH_SHORT).show();
	    						}
	    					});
	    				} catch (Exception e) {
	    					LogUtil.printError(e);
	    					handler.post(new Runnable() {
	    						@Override
	    						public void run() {
	    							Toast.makeText(getBaseContext(),"Erro ao efetuar o pagamento da venda!", Toast.LENGTH_SHORT).show();
	    						}
	    					});
	    				}
		    		//}else{
//		    			handler.post(new Runnable() {
//    						@Override
//    						public void run() {
//    							Toast.makeText(getBaseContext(),"Pagamento não autorizado " + statusAdyen.getResultCode() + "!", Toast.LENGTH_SHORT).show();
//    						}
//    					});
//		    		}
		    		

				} catch (Exception e) {
					LogUtil.printError(e);
					handler.post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(getBaseContext(),"Pagamento não autorizado!", Toast.LENGTH_SHORT).show();
						}
					});
				} finally {
					dialog.dismiss();
				}
			}
    	}.start();
    }

    public void pagamentoCartaoTarjaBtnCancelar(View v){
    	read.stop();
        Intent intent = new Intent();
        finish();
        intent.setClass(PagamentoLeitorTarja.this, TelaVenda.class);
        startActivity(intent);
    }
    
    @Override
    public void onBackPressed() {
    }
    	
	@SuppressLint("HandlerLeak") private class UpdateBytesHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			limparCartao();
			try{
				String bytes = (String)msg.obj;
				String []arrayBytes = bytes.split("=");
				String cartao = arrayBytes[0];
				cartao = cartao.substring(1, cartao.length());
				if(cartao.length() <16)
					throw new Exception();
				
				edNumeroCartao.setText(cartao);
				edNumeroCartao.setEnabled(false);
				String data = arrayBytes[1].substring(0, 4);
				data = data.substring(2,4) + "/" + data.substring(0,2);
								
				edVencimento.setText(data);
				edVencimento.setEnabled(false);
				edNmNoCartao.setText(SessionStorageUtil.getNmCliente());
				txtEstadoCartaoTarja.setText("Ok!");
			}catch(Exception e){
				txtEstadoCartaoTarja.setText("Tente Novamente...");
				limparCartao();
                //noinspection ImplicitArrayToString
                Log.d("cartao", e.getStackTrace().toString());
			}
			
		}
		
		public void limparCartao(){
			edVencimento.setText("");
			edNmNoCartao.setText("");
			edNumeroCartao.setText("");
			edDigitoVerificador.setText("");
		}
		
	}
	
	public void limparCartao(){
		edVencimento.setText("");
		edNmNoCartao.setText("");
		edNumeroCartao.setText("");
		edDigitoVerificador.setText("");
	}

	@Override
	protected void onResume() {
        ConnectionUtil.verificaConexao(this);
		if(SessionStorageUtil.getIdVenda() == null || SessionStorageUtil.getIdVenda().equals("")){
			SessionStorageUtil.cleanSessao();
			Intent intent = new Intent();
            finish();
	        intent.setClass(PagamentoLeitorTarja.this, InsercaoCodVendedor.class);
	        startActivity(intent);
		}
		super.onResume();
	}

	
}
