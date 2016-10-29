package br.com.code4u.mobshop.view;

import java.net.ConnectException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import br.com.code4u.mobshop.R;
import br.com.code4u.mobshop.model.StatusAdyen;
import br.com.code4u.mobshop.model.VendaItem;
import br.com.code4u.mobshop.util.AlterAllFontsInActivity;
import br.com.code4u.mobshop.util.ConnectionUtil;
import br.com.code4u.mobshop.util.LogUtil;
import br.com.code4u.mobshop.util.SessionStorageUtil;
import br.com.code4u.mobshop.wservices.ConexaoWSRest;

public class PagamentoLeitorChip extends Activity {
	
	private String cod_venda;
    private TextView txtTotalVenda;
    private TextView txtStatusCartao;

	private ProgressDialog dialog;
	private final Handler handler = new Handler();

    private static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pagamento_leitor_chip);
        
        AlterAllFontsInActivity.overrideFonts(this, findViewById(R.id.linearPagamentoLeitorChip));
        
        txtTotalVenda = (TextView)findViewById(R.id.txtTotalVenda);      
        txtStatusCartao = (TextView)findViewById(R.id.txtStatusCartao); 
    	cod_venda = SessionStorageUtil.getIdVenda();
    	
    	obterTotalVenda();
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
		    	txtStatusCartao.setText("Insira o Cartão..");
			}
		});
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.editTextEndereco || super.onOptionsItemSelected(item);
    }


    public void pagamentoCartaoChipBtnFinalizar(View v){
    		
    		dialog = ProgressDialog.show(this, "",
    				"Efetuando o pagamento da venda, por favor aguarde...", false,
    				false);
        	new Thread() {
    			public void run() {
    		    	try {
    		    		ConexaoWSRest conexaoWSRest = new ConexaoWSRest();
    		    		final StatusAdyen statusAdyen = conexaoWSRest.analisarAutorizacaoVenda(activity);

    		    		
    		    		if(statusAdyen.getResultCode().equals("Authorised")){
    		    			List<NameValuePair> params = new ArrayList<>();
    	                    params.add(new BasicNameValuePair("idVenda", cod_venda)); 	
    	                    params.add(new BasicNameValuePair("codConfirmacao", statusAdyen.getAuthCode()));
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
    	    		    		
    	    		    	    Intent intent = new Intent(PagamentoLeitorChip.this, InsercaoCPFCliente.class);
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
    		    		}else{
    		    			handler.post(new Runnable() {
	    						@Override
	    						public void run() {
	    							Toast.makeText(getBaseContext(),"Pagamento não autorizado " + statusAdyen.getResultCode() + "!", Toast.LENGTH_SHORT).show();
	    						}
	    					});
    		    		}
    		    		

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

    public void pagamentoCartaoChipBtnCancelar(View v){
        Intent intent = new Intent();
        finish();
        intent.setClass(PagamentoLeitorChip.this, TelaVenda.class);
        startActivity(intent);
    }
    
    @Override
    public void onBackPressed() {
    }
    
	@Override
	protected void onResume() {
        ConnectionUtil.verificaConexao(this);
		if(SessionStorageUtil.getIdVenda() == null || SessionStorageUtil.getIdVenda().equals("")){
			SessionStorageUtil.cleanSessao();
			Intent intent = new Intent();
            finish();
	        intent.setClass(PagamentoLeitorChip.this, InsercaoCodVendedor.class);
	        startActivity(intent);
		}
		super.onResume();
	}
}
