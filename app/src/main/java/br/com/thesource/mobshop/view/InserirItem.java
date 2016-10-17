package br.com.thesource.mobshop.view;

import java.io.FileNotFoundException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import br.com.thesource.mobshop.R;
import br.com.thesource.mobshop.model.Produto;
import br.com.thesource.mobshop.util.AlterAllFontsInActivity;
import br.com.thesource.mobshop.util.ConnectionUtil;
import br.com.thesource.mobshop.util.LogUtil;
import br.com.thesource.mobshop.util.SessionStorageUtil;
import br.com.thesource.mobshop.wservices.ConexaoWSRest;
import br.com.thesource.mobshop.zxing.IntentIntegrator;
import br.com.thesource.mobshop.zxing.IntentResult;

public class InserirItem extends Activity {

	private EditText edTxtNomeProduto;
	private EditText edTxtCorProduto;
	private EditText edTxtMarcaProduto;
	private EditText edTxtQuantidadeProduto;
	private EditText edTxtTotalProduto;
	private EditText edTxtCodBarras;
	private TextView txtIdProduto;
    private ImageView barcode;
	
	private ProgressDialog dialog;
	private final Handler handler = new Handler();

    private static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_inserir_item);
        
        AlterAllFontsInActivity.overrideFonts(this, findViewById(R.id.linearInserirItem));
        
        edTxtNomeProduto = (EditText) findViewById(R.id.edTxtNomeProduto);
        edTxtCorProduto = (EditText) findViewById(R.id.edTxtCorProduto);
        edTxtMarcaProduto = (EditText) findViewById(R.id.edTxtMarcaProduto);
        edTxtQuantidadeProduto = (EditText) findViewById(R.id.edTxtQuantidadeProduto);
        edTxtTotalProduto = (EditText) findViewById(R.id.edTxtTotalProduto);
        edTxtCodBarras = (EditText) findViewById(R.id.edTxtCodBarras);
        txtIdProduto = (TextView) findViewById(R.id.txtIdProduto);
        ConnectionUtil.verificaConexao(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        ConnectionUtil.verificaConexao(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.editTextEndereco || super.onOptionsItemSelected(item);
    }

    public void inserirItemBtnAdiciona(View v){
    	
    	if(todosPreechidos()){
    		dialog = ProgressDialog.show(this, "", "Inserindo item a venda, por favor aguarde...", false, false);
        	new Thread() {
    			public void run() {
    				List<NameValuePair> params = new ArrayList<>();
                    params.add(new BasicNameValuePair("idProduto", txtIdProduto.getText().toString())); 
                    params.add(new BasicNameValuePair("idVenda", SessionStorageUtil.getIdVenda())); 	
                    params.add(new BasicNameValuePair("vlQuantidade", edTxtQuantidadeProduto.getText().toString())); 	
    		    	try {
    		    		ConexaoWSRest conexaoWSRest = new ConexaoWSRest();
    		    		conexaoWSRest.adicionarProdutoVenda(params,activity);
    		    		
    		    		handler.post(new Runnable() {
    						@Override
    						public void run() {
    							Toast.makeText(getBaseContext(),"Produto adicionado!", Toast.LENGTH_SHORT).show();
    						}
    					});
    		    		
    		    		Intent intent = new Intent();
                        finish();
    		            intent.setClass(InserirItem.this, TelaVenda.class);
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
    							Toast.makeText(getBaseContext(),"Erro ao adicionar o produto!", Toast.LENGTH_SHORT).show();
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

    public void inserirItemBtnCancelar(View v){
        Intent intent = new Intent();
        finish();
        intent.setClass(InserirItem.this, TelaVenda.class);
        startActivity(intent);
    }
    
    public void biparCodigoProduto(View v){
		IntentIntegrator integrator = new IntentIntegrator(InserirItem.this);
		integrator.initiateScan();
	}
    
    public void buscarCodigoProduto(View v){
    	consultarProduto(edTxtCodBarras.getText().toString());
    }

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, intent);

		if (scanResult != null) {
			if (scanResult.getContents() == null || scanResult.getContents().equals("")) {
				Toast.makeText(getBaseContext(), "Erro ao bipar produto!",
						Toast.LENGTH_LONG).show();
			} else {
				edTxtCodBarras.setText(scanResult.getContents());
				consultarProduto(scanResult.getContents());
				LogUtil.printInfo(scanResult.getContents());
			}

		}
	}
	
	private void consultarProduto(String codBarras){
		final String cod = codBarras;
		dialog = ProgressDialog.show(this, "", "Consultando o produto, por favor aguarde...", false, false);
    	new Thread() {
			public void run() {
				List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("codBarra", cod)); 	
		    	try {
		    		ConexaoWSRest conexaoWSRest = new ConexaoWSRest();
		    		final Produto produto = conexaoWSRest.buscarProduto(params,activity);

		    		handler.post(new Runnable() {
						@Override
						public void run() {
                            edTxtCodBarras.setText(cod);
							edTxtNomeProduto.setText(produto.getNmProduto());
				    		edTxtCorProduto.setText(produto.getProdutoCor().getDsCor());
				    		edTxtMarcaProduto.setText(produto.getMrProduto());
				    		edTxtTotalProduto.setText("R$0,00");
				    		txtIdProduto.setText(produto.getIdRefProduto().toString());
                            edTxtCodBarras.clearFocus();
                            edTxtQuantidadeProduto.requestFocus();
				    		edTxtQuantidadeProduto.addTextChangedListener(new TextWatcher() {
								
								@Override
								public void onTextChanged(CharSequence s, int start, int before, int count) {
									
									int qtdProduto;
									
									try{
										qtdProduto = Integer.parseInt(edTxtQuantidadeProduto.getText().toString().replace("R$", ""));
									}catch(Exception e){
										qtdProduto = 0;
									}
									
						    		Double vlProduto = produto.getPrProduto();
						    		Double nvValor = vlProduto*qtdProduto;
						    		String nvValorCurrency = String.format("R$%.2f", nvValor);
						    		edTxtTotalProduto.setText(String.valueOf(nvValorCurrency));

								}
								
								@Override
								public void beforeTextChanged(CharSequence s, int start, int count,
										int after) {									
								}
								
								@Override
								public void afterTextChanged(Editable s) {
								}
							});

						}
					});
		    		

				} catch (Exception e) {
					LogUtil.printError(e);
					handler.post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(getBaseContext(),"Produto nao existe!", Toast.LENGTH_SHORT).show();
						}
					});
				} finally {
					dialog.dismiss();
				}
			}
            	}.start();
	}
	
	@Override
    public void onBackPressed() {
    }
	
    private Boolean todosPreechidos(){
        return edTxtNomeProduto.getText().toString().length() > 0 &&
                edTxtCorProduto.getText().toString().length() > 0 &&
                edTxtMarcaProduto.getText().toString().length() > 0 &&
                edTxtQuantidadeProduto.getText().toString().trim().length() > 0 &&
                edTxtTotalProduto.getText().toString().length() > 0;
    }

}
