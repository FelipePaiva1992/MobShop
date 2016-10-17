package br.com.thesource.mobshop.view;

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
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;
import br.com.thesource.mobshop.R;
import br.com.thesource.mobshop.dao.SessaoDao;
import br.com.thesource.mobshop.model.Cliente;
import br.com.thesource.mobshop.model.Sessao;
import br.com.thesource.mobshop.model.Venda;
import br.com.thesource.mobshop.util.AlterAllFontsInActivity;
import br.com.thesource.mobshop.util.ConnectionUtil;
import br.com.thesource.mobshop.util.LogUtil;
import br.com.thesource.mobshop.util.Mask;
import br.com.thesource.mobshop.util.SessionStorageUtil;
import br.com.thesource.mobshop.wservices.ConexaoWSRest;

public class InsercaoCPFCliente extends Activity {

	private final Context context = this;
	
    private EditText editTextCPFCliente;
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
        setContentView(R.layout.activity_insercao_cpfcliente);
        
        AlterAllFontsInActivity.overrideFonts(this, findViewById(R.id.linearInsercaoCPFCliente));
        editTextCPFCliente = (EditText)findViewById(R.id.editTextEnderecoCliente);
        TextWatcher maskCpf = Mask.insert("###.###.###-##", editTextCPFCliente);
        editTextCPFCliente.addTextChangedListener(maskCpf);
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

    public void buttonCPFClienteClick(View v){

        final String cpfSemMascara = Mask.unmask(editTextCPFCliente.getText().toString());
        if(todosPreechidos()){
        	dialog = ProgressDialog.show(this, "", "Buscando cliente, por favor aguarde...", false, false);
        	new Thread() {
    			public void run() {
    				List<NameValuePair> params = new ArrayList<>();
                    params.add(new BasicNameValuePair("cpfCliente", cpfSemMascara));
    		    	try {
    		    		ConexaoWSRest conexaoWSRest = new ConexaoWSRest();
    		    		Cliente cliente = conexaoWSRest.consultarCliente(params,activity);

    		    		SessionStorageUtil.setNmCliente(cliente.getNmCliente());
    		    		SessionStorageUtil.setCpfCliente(cpfSemMascara);

                        cod_vendedor = SessionStorageUtil.getIdVendedor();
                        cpfCliente = SessionStorageUtil.getCpfCliente();

                        params = new ArrayList<>();
                        params.add(new BasicNameValuePair("idVendedor", cod_vendedor));
                        params.add(new BasicNameValuePair("cpfCliente", cpfCliente));

                        Venda venda = conexaoWSRest.iniciarVenda(params,activity);

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
                        Intent intent = new Intent(InsercaoCPFCliente.this, TelaVenda.class);

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
    							Toast.makeText(getBaseContext(),"Erro ao encontrar o cliente!", Toast.LENGTH_SHORT).show();
    						}
    					});

    					SessionStorageUtil.setCpfCliente(editTextCPFCliente.getText().toString());
                        finish();
    					Intent intent = new Intent(InsercaoCPFCliente.this, CadastroDeCliente.class);

    	    	        startActivity(intent);
    				} finally {
    					dialog.dismiss();
    				}
    			}
        	}.start();
        }else{
    		Toast.makeText(getBaseContext(),"Campos em banco!", Toast.LENGTH_SHORT).show();
        }
    	
    }
    
    public void buttonLogoutClick(View v){
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
    	 
		// set title
		alertDialogBuilder.setTitle("Logout");

		// set dialog message
		alertDialogBuilder
			.setMessage("Deseja efetuar logout?")
			.setCancelable(false)
			.setPositiveButton("Sim",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
			    	SessionStorageUtil.cleanSessao();
                    finish();
			    	Intent intent = new Intent(InsercaoCPFCliente.this, InsercaoCodVendedor.class);
					
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
    
    private Boolean todosPreechidos(){
        return Mask.unmask(editTextCPFCliente.getText().toString()).trim().length() > 0;
    }

    @Override
    public void onBackPressed() { }
}
