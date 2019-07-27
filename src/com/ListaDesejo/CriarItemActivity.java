package com.ListaDesejo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ShareActionProvider;
import android.widget.Toast;

public class CriarItemActivity extends Activity {

	Button btnCancelar;
	SQLiteDatabase bd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.ListaDesejo.R.layout.criar_item);
	

		btnCancelar = (Button)
		findViewById (com.ListaDesejo.R.id.btnCancelar);
		
		btnCancelar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent trocatela = new
				Intent(CriarItemActivity.this,EditarListaActivity.class);
				Intent it = getIntent();
				trocatela.putExtra("titulo", it.getStringExtra("titulo"));
				CriarItemActivity.this.startActivity(trocatela);
				CriarItemActivity.this.finish();

			}
		});
	
	}
	
	
	 @Override
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH) public boolean onCreateOptionsMenu(Menu menu) {
		 
	        getMenuInflater().inflate(R.menu.barra_menu, menu);
	        ShareActionProvider mShareActionProvider = (ShareActionProvider) menu.findItem(R.id.share).getActionProvider();
	        mShareActionProvider.setShareIntent(getDefaultShareIntent());
	 
	        return super.onCreateOptionsMenu(menu);
	    }
	
	 private Intent getDefaultShareIntent(){
	        Intent intent = new Intent(Intent.ACTION_SEND);
	        intent.setType("text/plain");
	        intent.putExtra(Intent.EXTRA_SUBJECT, "SUBJECT");
	        intent.putExtra(Intent.EXTRA_TEXT,"Extra Text");
	        return intent;
	    }
	
	@Override
	protected void onResume() {
        super.onResume();
        
        this.createDb();	
	}
	
	private void createDb() {
		 
		 StringBuilder sqlListaDesejos = new StringBuilder();
			sqlListaDesejos.append("CREATE TABLE IF NOT EXISTS listasItem (");
			sqlListaDesejos.append("_id INTEGER PRIMARY KEY, ");
			sqlListaDesejos.append("nomeItem VARCHAR(30), ");
			sqlListaDesejos.append("categoriaItem VARCHAR(30), ");
			sqlListaDesejos.append("precoMin NUMERIC(7,2), ");
			sqlListaDesejos.append("precoMax NUMERIC(7,2), ");
			sqlListaDesejos.append("idPresente INTEGER);");
			getBd().execSQL(sqlListaDesejos.toString());
			
			getBd().close();
	 }
	
	public SQLiteDatabase getBd() {
		if(bd == null){
			bd = openOrCreateDatabase("Presente.db", Context.MODE_PRIVATE, null);
		}
		if(!bd.isOpen()){
			bd = openOrCreateDatabase("Presente.db", Context.MODE_PRIVATE, null);
		}
		return bd;
	}

	public void setBd(SQLiteDatabase bd) {
		this.bd = bd;
	}
	
	public void CriarItemClick(View v){
    	EditText nomeItemField = (EditText)findViewById(R.id.nomeItemField);
    	EditText categoriaItemField = (EditText)findViewById(R.id.categoriaItemField);
    	EditText precoMinItem = (EditText)findViewById(R.id.precoMinItem);
    	EditText precoMaxItem = (EditText)findViewById(R.id.precoMaxItem);
    	
    	if(nomeItemField.getText().toString().length() <= 0){
    		nomeItemField.setError("Preencha o campo nome.");
    		nomeItemField.requestFocus();
    	} else if(categoriaItemField.getText().toString().length() <= 0){
    		categoriaItemField.setError("Preencha o campo categoria.");
    		categoriaItemField.requestFocus();
    	}  else if(precoMinItem.getText().toString().length() <= 0){
    		precoMinItem.setError("Preencha o campo preço minimo.");
    		precoMinItem.requestFocus();
    	} else if(precoMaxItem.getText().toString().length() <= 0){
    		precoMaxItem.setError("Preencha o campo praço maximo");
    		precoMaxItem.requestFocus();
    	}else {
    		try{
	    		ContentValues ctv = new ContentValues();
	    		Intent it = getIntent();
	    		ctv.put("nomeItem", nomeItemField.getText().toString());
	    		ctv.put("categoriaItem", categoriaItemField.getText().toString());
	    		ctv.put("precoMin", precoMinItem.getText().toString());
	    		ctv.put("precoMax", precoMaxItem.getText().toString());
	    		ctv.put("idPresente", it.getIntExtra("idPresente", 0));
	    		if(getBd().insert("listasItem", "_id", ctv) > 0){
	    			Toast.makeText(getBaseContext(), "Sucesso ao cadastrar o Item! :)", Toast.LENGTH_SHORT).show();
	    			
	    			Intent trocatela = new
	    			Intent(this,EditarListaActivity.class);
					trocatela.putExtra("titulo", it.getStringExtra("titulo"));
					trocatela.putExtra("id", it.getIntExtra("idPresente", 0));
	    			this.startActivity(trocatela);
	    			this.finish();
	    			
	    			
	    		} else {
	    			Toast.makeText(getBaseContext(), "Erro ao cadastrar o Item. :(", Toast.LENGTH_SHORT).show();
	    		}
    		} catch(Exception ex){
    			Toast.makeText(getBaseContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
    		}
    		
    		getBd().close();
    	}
    }
}