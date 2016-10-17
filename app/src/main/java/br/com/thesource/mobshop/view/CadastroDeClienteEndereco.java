package br.com.thesource.mobshop.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import br.com.thesource.mobshop.R;
import br.com.thesource.mobshop.util.AlterAllFontsInActivity;
import br.com.thesource.mobshop.util.ConnectionUtil;

public class CadastroDeClienteEndereco extends Activity {

	public static EditText editTextEstado;
	public static EditText editTextCidade;
	public static EditText editTextEndereco;
	public static EditText editTextNumero;
	public static EditText editTextBairro;
    public static Boolean enderecoOk = false;
	
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_cadastro_de_cliente_endereco);
        AlterAllFontsInActivity.overrideFonts(this, findViewById(R.id.linearCadastroClienteEndereco));
        
        if(!enderecoOk){
        	enderecoOk = false;
        	editTextEstado  	= (EditText)findViewById(R.id.editTextEstado);
            editTextCidade 	= (EditText)findViewById(R.id.editTextCidade);
            editTextEndereco	= (EditText)findViewById(R.id.editTextEndereco);
            editTextNumero 	= (EditText)findViewById(R.id.editTextNumero);
            editTextBairro = (EditText)findViewById(R.id.editTextBairro);
        }

        addItemsOnSpinner();
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

    // add items into spinner dynamically
    void addItemsOnSpinner() {

        Spinner spinner = (Spinner) findViewById(R.id.spinnerLogradouro);
        List<String> list = new ArrayList<>();
        list.add("Rua");
        list.add("Avenida");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }
    
    public void cadastroClienteBtnCadastrar(View v){
    	enderecoOk = true;
        finish();
        Intent intent = new Intent(CadastroDeClienteEndereco.this, CadastroDeCliente.class);
        
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        enderecoOk = false;
        finish();
        Intent intent = new Intent(CadastroDeClienteEndereco.this, CadastroDeCliente.class);

        startActivity(intent);
    }
}
