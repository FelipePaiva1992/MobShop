package br.com.thesource.mobshop.view;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
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
import android.widget.ImageView;
import android.widget.Toast;
import br.com.thesource.mobshop.R;
import br.com.thesource.mobshop.util.AlterAllFontsInActivity;
import br.com.thesource.mobshop.util.ConnectionUtil;
import br.com.thesource.mobshop.util.LogUtil;
import br.com.thesource.mobshop.util.SessionStorageUtil;
import br.com.thesource.mobshop.wservices.ConexaoWSRest;

public class CadastroDeCliente extends Activity {

	private String editTextEstado;
	private String editTextCidade;
	private String editTextEndereco;
	private String editTextNumero;
	private String editTextBairro;
	private String spinner;
	private String editTextNomeCliente;
	private String editTextCPFCliente;
	private String editTextNascimentoCliente;
	private String editTextEmail;
	private String editTextTelefone;


    private ProgressDialog dialog;
	private final Handler handler = new Handler();
	
	private String cod_vendedor;
    private static String cpfClienteAcesso;
    private static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_cadastro_de_cliente);
        AlterAllFontsInActivity.overrideFonts(this, findViewById(R.id.linearCadastroCliente));

        ImageView imageViewEnderecoOk = (ImageView) findViewById(R.id.imageViewEnderecoOk);
        ImageView imageViewDadosOk = (ImageView) findViewById(R.id.ImageViewDadosOk);
        ImageView imageViewContatoOk = (ImageView) findViewById(R.id.ImageViewContatoOk);
        
        try{
        	if(CadastroDeClienteEndereco.enderecoOk){
            	editTextEstado = CadastroDeClienteEndereco.editTextEstado.getText().toString();
                editTextCidade = CadastroDeClienteEndereco.editTextCidade.getText().toString();
                editTextEndereco = CadastroDeClienteEndereco.editTextEndereco.getText().toString();
                editTextNumero = CadastroDeClienteEndereco.editTextNumero.getText().toString();
                editTextBairro = CadastroDeClienteEndereco.editTextBairro.getText().toString();
                spinner = "Rua";
                imageViewEnderecoOk.setVisibility(View.VISIBLE);
                (findViewById(R.id.enderecoBox)).setClickable(false);
            }
            
            if(CadastroDeClienteDados.dadosOk){
            	editTextNomeCliente = CadastroDeClienteDados.editTextNomeCliente.getText().toString();
                editTextCPFCliente = CadastroDeClienteDados.editTextCPFCliente.getText().toString();
                editTextNascimentoCliente = CadastroDeClienteDados.editTextNascimentoCliente.getText().toString();
                imageViewDadosOk.setVisibility(View.VISIBLE);
                (findViewById(R.id.dadosBox)).setClickable(false);
            }

    		if(CadastroDeClienteContato.contatoOk){
    			editTextEmail = CadastroDeClienteContato.editTextEmail.getText().toString();
    	        editTextTelefone = CadastroDeClienteContato.editTextTelefone.getText().toString();
    	        imageViewContatoOk.setVisibility(View.VISIBLE);
                (findViewById(R.id.contatoBox)).setClickable(false);
    		}
        }catch(Exception e){
        	LogUtil.printError(e);
        }
        
		
    	cod_vendedor = SessionStorageUtil.getIdVendedor();
        cpfClienteAcesso = SessionStorageUtil.getCpfCliente();
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
    
    public void cadastrarEndereco(View v){
        finish();
    	Intent intent = new Intent(CadastroDeCliente.this, CadastroDeClienteEndereco.class);
        
        startActivity(intent);
    }
    
    public void cadastrarDados(View v){
        finish();
    	Intent intent = new Intent(CadastroDeCliente.this, CadastroDeClienteDados.class);
        
        startActivity(intent);
    }

	public void cadastrarContato(View v){
        finish();
		Intent intent = new Intent(CadastroDeCliente.this, CadastroDeClienteContato.class);
        
        startActivity(intent);
	}

    public void cadastroClienteBtnCadastrar(View v){

            if(CadastroDeClienteContato.contatoOk && CadastroDeClienteDados.dadosOk && CadastroDeClienteEndereco.enderecoOk){
                dialog = ProgressDialog.show(this, "", "Cadastrando cliente, por favor aguarde...", false,
                        false);
                new Thread() {
                    public void run() {
                        List<NameValuePair> params = new ArrayList<>();
                        params.add(new BasicNameValuePair("nmCliente", editTextNomeCliente));
                        params.add(new BasicNameValuePair("cpfCliente", editTextCPFCliente.toString()));
                        params.add(new BasicNameValuePair("logradouro", spinner));
                        params.add(new BasicNameValuePair("endereco", editTextEndereco));
                        params.add(new BasicNameValuePair("numero", editTextNumero));
                        params.add(new BasicNameValuePair("bairro", editTextBairro));
                        params.add(new BasicNameValuePair("cidade", editTextCidade));
                        params.add(new BasicNameValuePair("estado", editTextEstado));
                        params.add(new BasicNameValuePair("vlTelefone", editTextTelefone));
                        params.add(new BasicNameValuePair("email", editTextEmail));
                        params.add(new BasicNameValuePair("dtNascimento", editTextNascimentoCliente));
                        try {
                            ConexaoWSRest conexaoWSRest = new ConexaoWSRest();
                            Boolean resut = conexaoWSRest.cadastrarCliente(params, activity);

                            if(resut){

                                SessionStorageUtil.setIdVendedor(cod_vendedor);
                                SessionStorageUtil.setCpfCliente(editTextCPFCliente.toString());
                                finish();
                                Intent intent = new Intent(CadastroDeCliente.this, InicioVenda.class);

                                startActivity(intent);
                            }else{
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getBaseContext(),"Erro ao cadastrar o cliente!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }catch (SocketTimeoutException | FileNotFoundException e) {
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
                        } catch (IOException e) {
                            LogUtil.printError(e);
                        } catch (Exception e) {
                            LogUtil.printError(e);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getBaseContext(),"Erro ao cadastrar Cliente!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } finally {
                            dialog.dismiss();
                        }
                    }
                }.start();
            }else{
                Toast.makeText(getBaseContext(),"Insira os dados do cliente!", Toast.LENGTH_SHORT).show();
            }

    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(CadastroDeCliente.this, InsercaoCPFCliente.class);

        startActivity(intent);
    }
}
