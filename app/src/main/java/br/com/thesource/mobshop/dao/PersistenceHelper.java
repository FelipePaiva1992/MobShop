package br.com.thesource.mobshop.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class PersistenceHelper extends SQLiteOpenHelper {

	private static final String NOME_BANCO =  "teste";
	private static final int VERSAO =  1;
         
      private static PersistenceHelper instance;  
         
      private PersistenceHelper(Context context) {  
          super(context, NOME_BANCO, null, VERSAO);  
      }  
         
      public static PersistenceHelper getInstance(Context context) {  
          if(instance == null)  
              instance = new PersistenceHelper(context);  
             
          return instance;  
      }  
        
      @Override  
      public void onCreate(SQLiteDatabase db) {  
    	  db.execSQL(SessaoDao.SCRIPT_DELECAO_TABELA);  
          db.execSQL(SessaoDao.SCRIPT_CRIACAO_TABELA_SESSAO);  
      }  
     
      @Override  
      public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {  
          db.execSQL(SessaoDao.SCRIPT_DELECAO_TABELA);  
          onCreate(db);  
      }  

}
