package br.com.thesource.mobshop.view;

import java.io.FileNotFoundException;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import br.com.thesource.mobshop.R;
import br.com.thesource.mobshop.model.VendaItem;
import br.com.thesource.mobshop.util.AlterAllFontsInActivity;
import br.com.thesource.mobshop.util.ConnectionUtil;
import br.com.thesource.mobshop.util.LogUtil;
import br.com.thesource.mobshop.util.SessionStorageUtil;
import br.com.thesource.mobshop.wservices.ConexaoWSRest;

public class PagamentoMaquina extends Activity {
	
	private String cod_venda;
    private TextView txtTotalVenda;
    private EditText editTxtCodConfirmacao;

	private ProgressDialog dialog;
	private final Handler handler = new Handler();

    private static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pagamento_maquina);
        
        AlterAllFontsInActivity.overrideFonts(this, findViewById(R.id.linearPagamentoMaquina));
        
        txtTotalVenda = (TextView)findViewById(R.id.txtTotalVenda);
        editTxtCodConfirmacao = (EditText)findViewById(R.id.editTxtCodConfirmacao);
        
        addItemsOnSpinner();
        
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
			}
		});
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.editTextEndereco || super.onOptionsItemSelected(item);
    }


    // add items into spinner dynamically
    void addItemsOnSpinner() {

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        List<String> list = new ArrayList<>();
        list.add("Debito");
        list.add("Credito");
        list.add("Credito Parcelado");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    public void pagamentoMaquinaBtnFinalizar(View v){
    	
    	if(todosPreechidos()){
    		dialog = ProgressDialog.show(this, "",
    				"Efetuando o pagamento da venda, por favor aguarde...", false,
    				false);
        	new Thread() {
    			public void run() {
    				List<NameValuePair> params = new ArrayList<>();
                    params.add(new BasicNameValuePair("idVenda", cod_venda)); 	
                    params.add(new BasicNameValuePair("codConfirmacao", editTxtCodConfirmacao.getText().toString())); 
                    params.add(new BasicNameValuePair("tipoPagamento", String.valueOf(1))); 
    		    	try {
    		    		ConexaoWSRest conexaoWSRest = new ConexaoWSRest();
    		    		conexaoWSRest.pagarVenda(params,activity);
    		    		
    		    		SessionStorageUtil.cleanVenda();
    		    		
    		    		handler.post(new Runnable() {
    						@Override
    						public void run() {
    							Toast.makeText(getBaseContext(),"Pagamento da venda efetuado com sucesso!", Toast.LENGTH_SHORT).show();
    						}
    					});
    		    		
    		    	    Intent intent = new Intent(PagamentoMaquina.this, InsercaoCPFCliente.class);
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
    				} finally {
    					dialog.dismiss();
    				}
    			}
        	}.start();
    	}else{
    		Toast.makeText(getBaseContext(),"Campos em banco!", Toast.LENGTH_SHORT).show();
    	}
    }

    public void pagamentoMaquinaBtnCancelar(View v){
        Intent intent = new Intent();
        finish();
        intent.setClass(PagamentoMaquina.this, TelaVenda.class);
        startActivity(intent);
    }
    
    @Override
    public void onBackPressed() {
    }
    
    private Boolean todosPreechidos(){
        return editTxtCodConfirmacao.getText().toString().trim().length() > 0;
    }

	@Override
	protected void onResume() {
        ConnectionUtil.verificaConexao(this);
		if(SessionStorageUtil.getIdVenda() == null || SessionStorageUtil.getIdVenda().equals("")){
			SessionStorageUtil.cleanSessao();
			Intent intent = new Intent();
            finish();
	        intent.setClass(PagamentoMaquina.this, InsercaoCodVendedor.class);
	        startActivity(intent);
		}
		super.onResume();
	}
}
