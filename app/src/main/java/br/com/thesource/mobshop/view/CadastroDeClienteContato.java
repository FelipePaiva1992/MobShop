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

public class CadastroDeClienteContato extends Activity {

	public static EditText editTextEmail;
	public static EditText editTextTelefone;
	public static Boolean contatoOk = false;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_cadastro_de_cliente_contato);
        AlterAllFontsInActivity.overrideFonts(this, findViewById(R.id.linearCadastroClienteContato));

        if(!contatoOk){
        	contatoOk = false;
            editTextEmail = (EditText)findViewById(R.id.editTextEmail);
            editTextTelefone = (EditText)findViewById(R.id.editTextTelefone);
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
    	contatoOk = true;
        finish();
        Intent intent = new Intent(CadastroDeClienteContato.this, CadastroDeCliente.class);
        
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        contatoOk = false;
        finish();
        Intent intent = new Intent(CadastroDeClienteContato.this, CadastroDeCliente.class);

        startActivity(intent);
    }
}
