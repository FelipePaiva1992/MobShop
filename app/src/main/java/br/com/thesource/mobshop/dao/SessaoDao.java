package br.com.thesource.mobshop.dao;

import java.util.ArrayList;
import java.util.List;

import br.com.thesource.mobshop.model.Sessao;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SessaoDao {

	private static final String NOME_TABELA = "Sessao";
	private static final String COLUNA_ID = "id";
	private static final String COLUNA_CODIGO_VENDEDOR = "codigo_vendedor";
	private static final String COLUNA_CPF_CLIENTE = "cpf_cliente";
	private static final String COLUNA_CODIGO_VENDA = "codigo_venda";
	
	public static final String SCRIPT_CRIACAO_TABELA_SESSAO = "CREATE TABLE " + NOME_TABELA + "("
			+ COLUNA_ID + " INTEGER PRIMARY KEY," 
			+ COLUNA_CODIGO_VENDEDOR + " TEXT," 
			+ COLUNA_CPF_CLIENTE + " TEXT," 
			+ COLUNA_CODIGO_VENDA + " TEXT" + ")";
	
	public static final String SCRIPT_DELECAO_TABELA =  "DROP TABLE IF EXISTS " + NOME_TABELA;


	private SQLiteDatabase dataBase = null;
	
	private static SessaoDao instance;
	
	public static SessaoDao getInstance(Context context) {
		if(instance == null)
			instance = new SessaoDao(context);
		return instance;
	}

	private SessaoDao(Context context) {
		PersistenceHelper persistenceHelper = PersistenceHelper.getInstance(context);
        dataBase = persistenceHelper.getWritableDatabase();
	}
	
	public void salvar(Sessao sessao) {
        ContentValues values = gerarContentValeues(sessao);
        dataBase.insert(NOME_TABELA, null, values);
    }
	
	public List<Sessao> recuperarTodos() {
        String queryReturnAll = "SELECT * FROM " + NOME_TABELA;
        Cursor cursor = dataBase.rawQuery(queryReturnAll, null);

        return construirSessaoPorCursor(cursor);
    }
 
    public void deletar(Sessao sessao) {
 
        String[] valoresParaSubstituir = {
                String.valueOf(sessao.getId())
        };
 
        dataBase.delete(NOME_TABELA, COLUNA_ID + " =  ?", valoresParaSubstituir);
    }
	
	public void fecharConexao() {
        if(dataBase != null && dataBase.isOpen())
            dataBase.close(); 
    }
	
	
	private ContentValues gerarContentValeues(Sessao sessao) {
        ContentValues values = new ContentValues();
        values.put(COLUNA_CODIGO_VENDEDOR, sessao.getCod_vendedor());
        values.put(COLUNA_CPF_CLIENTE, sessao.getCpf_cliente());
        values.put(COLUNA_CODIGO_VENDA, sessao.getCod_venda());
 
        return values;
    }
	
	private List<Sessao> construirSessaoPorCursor(Cursor cursor) {
        List<Sessao> sessoes = new ArrayList<>();
        if(cursor == null)
            return sessoes;
         
        try {
 
            if (cursor.moveToFirst()) {
                do {
 
                    int indexID = cursor.getColumnIndex(COLUNA_ID);
                    int indexVendedor = cursor.getColumnIndex(COLUNA_CODIGO_VENDEDOR);
                    int indexVenda = cursor.getColumnIndex(COLUNA_CODIGO_VENDA);
                    int indexCpf = cursor.getColumnIndex(COLUNA_CPF_CLIENTE);
 
                    int id = cursor.getInt(indexID);
                    String cod_vendedor = cursor.getString(indexVendedor);
                    String cod_venda = cursor.getString(indexVenda);
                    String cpf_cliente = cursor.getString(indexCpf);
 
                    Sessao sessao = new Sessao(id, cod_vendedor, cod_venda, cpf_cliente);
 
                    sessoes.add(sessao);
 
                } while (cursor.moveToNext());
            }
             
        } finally {
            cursor.close();
        }
        return sessoes;
    }

	
}
