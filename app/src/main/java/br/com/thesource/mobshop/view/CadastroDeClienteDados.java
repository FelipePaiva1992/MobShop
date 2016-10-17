package br.com.thesource.mobshop.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import br.com.thesource.mobshop.R;
import br.com.thesource.mobshop.util.AlterAllFontsInActivity;
import br.com.thesource.mobshop.util.ConnectionUtil;

public class CadastroDeClienteDados extends Activity {

	public static EditText editTextNomeCliente;
	public static EditText editTextCPFCliente;
	public static EditText editTextNascimentoCliente;
	public static Boolean dadosOk = false;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_cadastro_de_cliente_dados);
        AlterAllFontsInActivity.overrideFonts(this, findViewById(R.id.linearCadastroClienteDados));
        
        if(!dadosOk){
        	dadosOk = false;
            editTextNomeCliente = (EditText)findViewById(R.id.editTextNomeCliente);
            editTextCPFCliente = (EditText)findViewById(R.id.editTextCPFCliente);
            editTextCPFCliente = (EditText)findViewById(R.id.editTextEnderecoCliente);
            editTextNascimentoCliente = (EditText)findViewById(R.id.editTextNascimentoCliente);
        }

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

    public void cadastroClienteBtnCadastrar(View v){
    	dadosOk= true;
        finish();
		Intent intent = new Intent(CadastroDeClienteDados.this, CadastroDeCliente.class);
		
		startActivity(intent);
    		    		
    }

    @Override
    public void onBackPressed() {
        dadosOk= false;
        finish();
        Intent intent = new Intent(CadastroDeClienteDados.this, CadastroDeCliente.class);

        startActivity(intent);
    }
}
