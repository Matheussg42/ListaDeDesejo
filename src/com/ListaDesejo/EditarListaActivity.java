package com.ListaDesejo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ListView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

public class EditarListaActivity extends Activity {

	Button btnCriarItem, btnSalvarLista, btnDelete;
	TextView countLista;
	SQLiteDatabase bd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.ListaDesejo.R.layout.editar_lista);
	
		btnCriarItem = (Button) findViewById(com.ListaDesejo.R.id.btnCriarItem);
		btnSalvarLista = (Button) findViewById(com.ListaDesejo.R.id.btnSalvarLista);
		btnDelete = (Button) findViewById(com.ListaDesejo.R.id.btnDelete);
		
		Intent it = getIntent();
		countLista =  (TextView) findViewById(com.ListaDesejo.R.id.countLista);
		countLista.setText(it.getStringExtra("titulo"));
		
		btnCriarItem.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {

				Intent trocar = new Intent(EditarListaActivity.this, CriarItemActivity.class);
				//mantendo os parametros da bagaça
				Intent it = getIntent();
				trocar.putExtra("titulo", it.getStringExtra("titulo"));
				trocar.putExtra("idPresente", it.getIntExtra("id", 0));
				EditarListaActivity.this.startActivity(trocar);
				EditarListaActivity.this.finish();

			}
		});
		
		
		btnSalvarLista.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {

				Intent trocartela2 = new Intent(EditarListaActivity.this,
						TabhostActivity.class);
				EditarListaActivity.this.startActivity(trocartela2);
				EditarListaActivity.this.finish();

			}
		});
		
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
	
	@SuppressWarnings("unused")
	private void createDb() {
		 
		 StringBuilder sqlListaDesejos = new StringBuilder();
			sqlListaDesejos.append("CREATE TABLE IF NOT EXISTS listasItem (");
			sqlListaDesejos.append("_id INTEGER PRIMARY KEY, ");
			sqlListaDesejos.append("idPresente INTEGER, ");
			sqlListaDesejos.append("nomeItem VARCHAR(30), ");
			sqlListaDesejos.append("categoriaItem VARCHAR(30), ");
			sqlListaDesejos.append("precoMin NUMERIC(7,2), ");
			sqlListaDesejos.append("precoMax NUMERIC(7,2));");
			getBd().execSQL(sqlListaDesejos.toString());
			
			getBd().close();
	 }
	
	
	@Override
	protected void onResume() {
        super.onResume();
		this.createlist();
	}
	
	public void ApagarClick(View v) {
		try {
			final SQLiteDatabase bd = openOrCreateDatabase("Presente.db",
					Context.MODE_PRIVATE, null);

			Intent it = getIntent();

			final int id = it.getIntExtra("id", 0);

			
			Builder msg = new Builder(EditarListaActivity.this);
			msg.setMessage("Deseja apagar esta Lista??");
			msg.setNegativeButton("Não", null);
			msg.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
			
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (bd.delete("listasPresente", "_id=?",
							new String[] { String.valueOf(id) }) > 0) {
						Toast.makeText(getBaseContext(),
								"Sucesso ao apagar a Lista. :)", Toast.LENGTH_SHORT)
								.show();
						finish();
					} else {
						Toast.makeText(getBaseContext(), "Erro ao apagar a Lista.",
								Toast.LENGTH_SHORT).show();
					}
				}
			});
			
			msg.show();
				
		} catch (Exception ex) {
			Toast.makeText(getBaseContext(), ex.getMessage(),
					Toast.LENGTH_SHORT).show();
		}
		bd.close();		
	}
		
		private void createlist(){
			try{
				
			Intent it = getIntent();
			
			Cursor cursor = getBd().rawQuery("SELECT * FROM listasItem WHERE idPresente = ?", new String [] {String.valueOf(it.getIntExtra("id", 0))});
			
			String[] from = {"nomeItem", "categoriaItem"};
			int[] to = {R.id.txvID, R.id.txvNome};
			
			@SuppressWarnings("deprecation")
			android.widget.SimpleCursorAdapter ad = new android.widget.SimpleCursorAdapter(getBaseContext(),
					R.layout.listar_model, cursor, from, to);
	
			ListView ltwItem = (ListView)findViewById(R.id.ltwItem);
			
			ltwItem.setAdapter(ad);
			
			ltwItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			
				@Override
				public void onItemClick(@SuppressWarnings("rawtypes") AdapterView adapter, View view,
						int position, long id) {
					
					SQLiteCursor c = (SQLiteCursor) adapter.getAdapter().getItem(position);
					Intent it2 = getIntent();
					Intent it = new Intent(getBaseContext(), EditarItemActivity.class);
					it.putExtra("id", c.getInt(0));
					c.close();
					it.putExtra("titulo", it2.getStringExtra("titulo"));
					it.putExtra("idPresente", it2.getIntExtra("id", 0));
					startActivity(it);
					
				}
			});
			//cursor.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		getBd().close();
		}
		
}