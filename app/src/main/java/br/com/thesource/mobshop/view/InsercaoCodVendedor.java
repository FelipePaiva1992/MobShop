package br.com.thesource.mobshop.view;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;
import br.com.thesource.mobshop.R;
import br.com.thesource.mobshop.model.Vendedor;
import br.com.thesource.mobshop.util.AlterAllFontsInActivity;
import br.com.thesource.mobshop.util.ConnectionUtil;
import br.com.thesource.mobshop.util.LogUtil;
import br.com.thesource.mobshop.util.SessionStorageUtil;
import br.com.thesource.mobshop.wservices.ConexaoWSRest;

public class InsercaoCodVendedor extends Activity {

	private final Context context = this;
    private EditText editTextCodVendedor;
    private EditText editTextSenhaVendedor;

	private ProgressDialog dialog;
	private final Handler handler = new Handler();

    private static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_insercao_cod_vendedor);
        AlterAllFontsInActivity.overrideFonts(this, findViewById(R.id.linearInsercaoCodVendedor));

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

    public void buttonCodVendedorClick(View v) {

        editTextCodVendedor = (EditText) findViewById(R.id.editTextCodVendedor);
        editTextSenhaVendedor = (EditText) findViewById(R.id.editTextSenhaVendedor);
        
        if(todosPreechidos()){
        	dialog = ProgressDialog.show(this, "",
    				"Acessando, por favor aguarde...", false,
    				false);
        	new Thread() {
    			public void run() {
    				List<NameValuePair> params = new ArrayList<>();
                    params.add(new BasicNameValuePair("idVendedor", editTextCodVendedor.getText().toString())); 
                    params.add(new BasicNameValuePair("vlSenha", editTextSenhaVendedor.getText().toString())); 

                    try{
    		    		ConexaoWSRest conexaoWSRest = new ConexaoWSRest();
    		    		Vendedor vendedor = conexaoWSRest.acessarVendedor(params,activity);

    		    		SessionStorageUtil.setNmVendedor(vendedor.getNmVendedor());
    		    		SessionStorageUtil.setIdVendedor(editTextCodVendedor.getText().toString());
                        finish();
    		    	    Intent intent = new Intent(InsercaoCodVendedor.this, InsercaoCPFCliente.class);		    	    
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
    							Toast.makeText(getBaseContext(),"Vendedor ou Senha incorretos!", Toast.LENGTH_SHORT).show();
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
    
    
    
    private Boolean todosPreechidos(){
        return editTextCodVendedor.getText().toString().trim().length() > 0 && editTextSenhaVendedor.getText().toString().trim().length() > 0;
    }
    
    @Override
    public void onBackPressed() {
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		 
		// set title
		alertDialogBuilder.setTitle("Sair");

		// set dialog message
		alertDialogBuilder
			.setMessage("Deseja sair da aplicação?")
			.setCancelable(false)
			.setPositiveButton("Sim",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					finish();
                    System.exit(0);
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

}
