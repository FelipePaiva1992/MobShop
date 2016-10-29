package br.com.code4u.mobshop.view;

import java.io.FileNotFoundException;
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
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import br.com.code4u.mobshop.R;
import br.com.code4u.mobshop.dao.SessaoDao;
import br.com.code4u.mobshop.model.Sessao;
import br.com.code4u.mobshop.model.Venda;
import br.com.code4u.mobshop.util.AlterAllFontsInActivity;
import br.com.code4u.mobshop.util.ConnectionUtil;
import br.com.code4u.mobshop.util.LogUtil;
import br.com.code4u.mobshop.util.SessionStorageUtil;
import br.com.code4u.mobshop.wservices.ConexaoWSRest;

public class InicioVenda extends Activity {

	private final Context context = this;

	
	private String cod_vendedor;
	private String cpfCliente;

    private ProgressDialog dialog;
	private final Handler handler = new Handler();
    private static Activity activity;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_inicio_venda);
        AlterAllFontsInActivity.overrideFonts(this, findViewById(R.id.linearInicioVenda));
        
        cod_vendedor = SessionStorageUtil.getIdVendedor();
    	cpfCliente = SessionStorageUtil.getCpfCliente();

        TextView txtNmVendedor = (TextView) findViewById(R.id.txtNmVendedor);
    	txtNmVendedor.setText(SessionStorageUtil.getNmVendedor());
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

    public void inicioVendaBtnIniciar(View v){
    	dialog = ProgressDialog.show(this, "",
				"Iniciando uma nova venda, por favor aguarde...", false,
				false);
    	new Thread() {
			public void run() {
				List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("idVendedor", cod_vendedor)); 	
                params.add(new BasicNameValuePair("cpfCliente", cpfCliente));
		    	try {
		    		ConexaoWSRest conexaoWSRest = new ConexaoWSRest();
		    		Venda venda = conexaoWSRest.iniciarVenda(params, activity);
		    		
		    		SessaoDao sessaoDao = SessaoDao.getInstance(getApplicationContext());
		    		
		    		try {
		    			List<Sessao> sessoes = sessaoDao.recuperarTodos();
			    		sessaoDao.deletar(sessoes.get(0));	
					} catch (Exception e) {
						LogUtil.printError(e);
					}
		    		
		    		Sessao sessao = new Sessao(1, String.valueOf(venda.getVendedor().getIdVendedor()), String.valueOf(venda.getIdVenda()), venda.getCliente().getCpfCliente());
		    		
		    		sessaoDao.salvar(sessao);
		    		
		    		SessionStorageUtil.setIdVenda(String.valueOf(venda.getIdVenda()));
		    	    SessionStorageUtil.setNmCliente(venda.getCliente().getNmCliente());
                    finish();
		    	    Intent intent = new Intent(InicioVenda.this, TelaVenda.class);
		    	    
		    	    startActivity(intent);

				} catch (FileNotFoundException e) {
					LogUtil.printError(e);
					handler.post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(getBaseContext(),"Tente novamente!", Toast.LENGTH_SHORT).show();
						}
					});
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
							Toast.makeText(getBaseContext(),"Erro ao iniciar a venda!", Toast.LENGTH_SHORT).show();
						}
					});
				} finally {
					dialog.dismiss();
				}
			}
    	}.start();
    }


    public void inicioVendaBtnCancelar(View v){
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		 
		// set title
		alertDialogBuilder.setTitle("Cancelar");

		// set dialog message
		alertDialogBuilder
			.setMessage("Deseja cancelar a venda?")
			.setCancelable(false)
			.setPositiveButton("Sim",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
			        Intent intent = new Intent();
                    finish();
			        intent.setClass(InicioVenda.this, InsercaoCPFCliente.class);
			        startActivity(intent);
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
