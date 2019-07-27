package com.ListaDesejo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ShareActionProvider;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

public class TabhostActivity extends Activity{ 
	
	EditText nomeListaField, assuntoListaField;
	Button btnCriarLista, btnCancelar;
	SQLiteDatabase bd;
	
	private TabHost myTabHost; 
	@Override 
	protected void onCreate(Bundle savedInstanceState) { 
		
		super.onCreate(savedInstanceState); 
		
		setContentView(R.layout.tabhost01); 
		
		// Recuperation du TabHost 
		
		myTabHost =(TabHost) findViewById(R.id.TabHost01); 
		
		// Before adding tabs, it is imperative to call the method setup()
		
		myTabHost.setup(); 
		
		// Adding tabs
			// tab1 settings 
		
		TabSpec spec = myTabHost.newTabSpec("tab_home"); 
		
		// text and image of tab 
		
		spec.setIndicator("Lista Feitas",getResources().getDrawable(android.R.drawable.ic_dialog_info)); 
		
		// specify layout of tab 
		
		spec.setContent(R.id.onglet1); 
		
		// adding tab in TabHost
		
		myTabHost.addTab(spec);
		
		// otherwise :
			
		myTabHost.addTab(myTabHost.newTabSpec("tab_home2")
		.setIndicator("Criar Lista",getResources().getDrawable(android.R.drawable.ic_input_add))
		.setContent(R.id.Onglet2));
	
		/*ExpandableListView listView = (ExpandableListView) findViewById(R.id.expandableListView2);
		listView.setAdapter(new MeuAdapter(this));*/
		
		btnCriarLista = (Button)findViewById(com.ListaDesejo.R.id.btnCriarLista);
		btnCancelar = (Button)findViewById(com.ListaDesejo.R.id.btnCancelar);
		nomeListaField = (EditText)findViewById(com.ListaDesejo.R.id.nomeListaField);
		assuntoListaField = (EditText)findViewById(com.ListaDesejo.R.id.assuntoListaField);

		/*btnCriarLista.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

					Intent trocatela = new
					Intent(tabhostActivity.this,tabhostActivity.class);
					tabhostActivity.this.startActivity(trocatela);
					tabhostActivity.this.finish();
			}
		});*/

		
	}
	
	 @Override
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH) public boolean onCreateOptionsMenu(Menu menu) {
		 
	        /** Inflating the current activity's menu with res/menu/items.xml */
	        getMenuInflater().inflate(R.menu.barra_menu, menu);
	 
	        /** Getting the actionprovider associated with the menu item whose id is share */
	        ShareActionProvider mShareActionProvider = (ShareActionProvider) menu.findItem(R.id.share).getActionProvider();
	 
	        /** Setting a share intent */
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
        // TODO Auto-generated method stub
        super.onResume();
        btnCancelar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
            	 if (v.getId() == com.ListaDesejo.R.id.btnCancelar);
            	 nomeListaField.setText("");
                 if (v.getId() == com.ListaDesejo.R.id.btnCancelar);
                 assuntoListaField.setText("");
            }
        });
    
        this.createDb();
		this.createlist();
	
	}
	 public void CadastrarClick(View v){
	    	EditText nomeListaField = (EditText)findViewById(R.id.nomeListaField);
	    	EditText assuntoListaField = (EditText)findViewById(R.id.assuntoListaField);
	    	
	    	if(nomeListaField.getText().toString().length() <= 0){
	    		nomeListaField.setError("Preencha o campo nome.");
	    		nomeListaField.requestFocus();
	    	} else if(assuntoListaField.getText().toString().length() <= 0){
	    		assuntoListaField.setError("Preencha o campo e-mail.");
	    		assuntoListaField.requestFocus();
	    	} else {
	    		try{
		    		ContentValues ctv = new ContentValues();
		    		ctv.put("nome", nomeListaField.getText().toString());
		    		ctv.put("assunto", assuntoListaField.getText().toString());
		    		if(getBd().insert("listasPresente", "_id", ctv) > 0){
		    			Toast.makeText(getBaseContext(), "Sucesso ao cadastrar a Lista! :)", Toast.LENGTH_SHORT).show();
		    			
		    			this.createlist();
		    			Intent trocatela = new
		    			Intent(TabhostActivity.this,TabhostActivity.class);
		    			TabhostActivity.this.startActivity(trocatela);
		    			TabhostActivity.this.finish();
		    			
		    		} else {
		    			Toast.makeText(getBaseContext(), "Erro ao cadastrar a Lista. :(", Toast.LENGTH_SHORT).show();
		    		}
	    		} catch(Exception ex){
	    			Toast.makeText(getBaseContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
	    		}
	    		
	    		getBd().close();
	    	}
	    }
	 
	 private void createDb() {

		// Tabela de clientes
		StringBuilder sqlListaDesejos = new StringBuilder();
		sqlListaDesejos.append("CREATE TABLE IF NOT EXISTS listasPresente (");
		sqlListaDesejos.append("_id INTEGER PRIMARY KEY, ");
		sqlListaDesejos.append("nome VARCHAR(30), ");
		sqlListaDesejos.append("assunto VARCHAR(30)); ");
		//sqlListaDesejos.append("FOREIGN KEY(_idItem) REFERENCES _idItem); ");
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
	
	
	private void createlist(){
	try{
		
	
		Cursor cursor = getBd().rawQuery("SELECT _id, nome, assunto FROM listasPresente", null);
		
		String[] from = {"nome", "assunto"};
		int[] to = {R.id.txvID, R.id.txvNome};
		
		@SuppressWarnings("deprecation")
		android.widget.SimpleCursorAdapter ad = new android.widget.SimpleCursorAdapter(getBaseContext(),
				R.layout.listar_model, cursor, from, to);
	
		ListView ltwLista = (ListView)findViewById(R.id.ltwLista);
		
		ltwLista.setAdapter(ad);
	
	
		
		ltwLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			
			@Override
			public void onItemClick(@SuppressWarnings("rawtypes") AdapterView adapter, View view,
					int position, long id) {
				
				SQLiteCursor c = (SQLiteCursor) adapter.getAdapter().getItem(position);
	
				Intent it = new Intent(getBaseContext(), EditarListaActivity.class);
				it.putExtra("id", c.getInt(0));
				it.putExtra("titulo", c.getString(1));
				startActivity(it);
			c.close();
			}	
		});
		//cursor.close();
		getBd().close();
	}catch(Exception e){
		e.printStackTrace();
		System.out.println("POG!");
	}
	
	}
}