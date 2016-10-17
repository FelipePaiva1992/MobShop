package br.com.thesource.mobshop.view;

import java.io.FileNotFoundException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import br.com.thesource.mobshop.R;
import br.com.thesource.mobshop.util.AlterAllFontsInActivity;
import br.com.thesource.mobshop.util.ConnectionUtil;
import br.com.thesource.mobshop.util.LogUtil;
import br.com.thesource.mobshop.util.SessionStorageUtil;
import br.com.thesource.mobshop.wservices.ConexaoWSRest;

public class TelaVenda extends Activity {

	private final Context context = this;
	
	private String cod_venda;

    private final Handler handler = new Handler();

    private static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_tela_venda);
   
        AlterAllFontsInActivity.overrideFonts(this, findViewById(R.id.linearTelaVenda));
        
    	cod_venda = SessionStorageUtil.getIdVenda();
        String nm_cliente = SessionStorageUtil.getNmCliente();


        TextView textViewCodVenda = (TextView) findViewById(R.id.textViewCodVenda);
        textViewCodVenda.setText(cod_venda);

        TextView textViewNmCliente = (TextView) findViewById(R.id.textViewNmCliente);
        textViewNmCliente.setText(nm_cliente);


        ConnectionUtil.verificaConexao(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        ConnectionUtil.verificaConexao(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.editTextEndereco || super.onOptionsItemSelected(item);
    }

    public void telaVendaBtnNovoItem(View v){
        Intent intent = new Intent();
        finish();
        intent.setClass(TelaVenda.this, InserirItem.class);
        startActivity(intent);
    }


    public void telaVendaBtnItens(View v){
        Intent intent = new Intent();
        finish();
        intent.setClass(TelaVenda.this, ListaItens.class);
        startActivity(intent);
    }

    public void telaVendaBtnPagar(View v){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
 
			// set title
			alertDialogBuilder.setTitle("Pagamento");
 
			// set dialog message
			alertDialogBuilder
				.setMessage("Selecione o tipo de pagamento!")
				.setCancelable(false)
				.setPositiveButton("Dinheiro",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
                        finish();
						Intent intent = new Intent(TelaVenda.this, PagamentoDinheiro.class);
						startActivity(intent);
						dialog.cancel();
					}
				  })
				.setNegativeButton("Cartão",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						SessionStorageUtil.setIdVenda(cod_venda);
                        finish();
						Intent intent = new Intent(TelaVenda.this, PagamentoMaquina.class);
						startActivity(intent);
						dialog.cancel();
					}
				});
 
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
				alertDialog.show();
    	
    }

    public void telaVendaBtnCancelar(View v){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		 
		// set title
		alertDialogBuilder.setTitle("Cancelar");

		// set dialog message
		alertDialogBuilder
			.setMessage("Deseja cancelar a venda?")
			.setCancelable(false)
			.setPositiveButton("Sim",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
			    	new Thread() {
						public void run() {
							List<NameValuePair> params = new ArrayList<>();
			                params.add(new BasicNameValuePair("idVenda", cod_venda)); 	
					    	try {
					    		ConexaoWSRest conexaoWSRest = new ConexaoWSRest();
					    		conexaoWSRest.finalizarVenda(params,activity);
					    		
					    	    Intent intent = new Intent();
                                finish();
					    	    intent.setClass(TelaVenda.this, InsercaoCPFCliente.class);
					    	    startActivity(intent);

							}catch (ConnectException e) {
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
										Toast.makeText(getBaseContext(),"Erro ao cancelar a venda!", Toast.LENGTH_SHORT).show();
									}
								});
							}
						}
                    			    	}.start();
				}
			  })
			.setNegativeButton("Não",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {

				}
			});

			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();

			// show it
			alertDialog.show();
    	
    }
    
    @Override
    public void onBackPressed() {
    }

}
