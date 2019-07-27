package com.ListaDesejo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ShareActionProvider;
import android.widget.Toast;
	
	
public class EditarItemActivity extends Activity {
	Button btnCancelar, btnSalvarItem, btnBusca;
	SQLiteDatabase bd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.ListaDesejo.R.layout.editar_item);
		
		btnCancelar = (Button)
		findViewById (com.ListaDesejo.R.id.btnCancelar);
		
		btnSalvarItem = (Button)
		findViewById (com.ListaDesejo.R.id.btnSalvarItem);		

		btnCancelar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent trocatela = new
				Intent(EditarItemActivity.this,EditarListaActivity.class);
				
				Intent it = getIntent();
				trocatela.putExtra("titulo", it.getStringExtra("titulo"));
				
				EditarItemActivity.this.startActivity(trocatela);
				EditarItemActivity.this.finish();

			}
		});
		
		
		Intent it = getIntent();

		int id = it.getIntExtra("id", 0);

	     bd = openOrCreateDatabase("Presente.db",
				Context.MODE_PRIVATE, null);

		Cursor cursor = bd.rawQuery("SELECT * FROM listasItem WHERE _id = ?",
				new String[] { String.valueOf(id) });
			
		if (cursor.moveToFirst()) {
			EditText nomeItemField = (EditText) findViewById(R.id.nomeItemField);
			EditText categoriaItemField = (EditText) findViewById(R.id.categoriaItemField);
			EditText precoMin = (EditText) findViewById(R.id.precoMinItem);
			EditText precoMax = (EditText) findViewById(R.id.precoMaxItem);

			nomeItemField.setText(cursor.getString(1));
			categoriaItemField.setText(cursor.getString(2));
			precoMin.setText(cursor.getString(3));
			precoMax.setText(cursor.getString(4));
		}
		
		
		btnBusca = (Button) findViewById(R.id.btnBusca);
		btnBusca.setOnClickListener(new OnClickListener()
		{
		@Override
		public void onClick(View arg0){
		String nomeProd =((EditText)findViewById(R.id.nomeItemField)).getText().toString();
		String precoMin =((EditText)findViewById(R.id.precoMinItem)).getText().toString();
		String precoMax =((EditText)findViewById(R.id.precoMaxItem)).getText().toString();
		Intent i = new
		Intent(android.content.Intent.ACTION_VIEW,
		Uri.parse("http://www.zoom.com.br/" + nomeProd + "/preco-" + precoMin + "-a-"+precoMax));
		startActivity(i);
		}
		});
		
		cursor.close();
		bd.close();
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
        super.onResume();
  
        this.createDb();	
	}
	
	public void AtualizarClick(View v) {
		EditText nomeItemField = (EditText) findViewById(R.id.nomeItemField);
		EditText categoriaItemField = (EditText) findViewById(R.id.categoriaItemField);
		EditText precoMin = (EditText) findViewById(R.id.precoMinItem);
		EditText precoMax = (EditText) findViewById(R.id.precoMaxItem);

		if (nomeItemField.getText().toString().length() <= 0) {
			nomeItemField.setError("Preencha o campo nome.");
			nomeItemField.requestFocus();
		} else if (categoriaItemField.getText().toString().length() <= 0) {
			categoriaItemField.setError("Preencha o campo categoria.");
			categoriaItemField.requestFocus();
		} else if (precoMin.getText().toString().length() <= 0) {
			precoMin.setError("Preencha o campo preço maximo.");
			precoMin.requestFocus();
		} else if (precoMax.getText().toString().length() <= 0) {
			precoMax.setError("Preencha o campo preço minimo.");
			precoMax.requestFocus();
		} else {
			try {
				bd = openOrCreateDatabase("Presente.db",
						Context.MODE_PRIVATE, null);

				Intent it = getIntent();
				ContentValues ctv = new ContentValues();
				ctv.put("nomeItem", nomeItemField.getText().toString());
				ctv.put("categoriaItem", categoriaItemField.getText().toString());
				ctv.put("precoMin", precoMin.getText().toString());
				ctv.put("precoMax", precoMax.getText().toString());
				ctv.put("idPresente", it.getIntExtra("idPresente", 0));

				int id = it.getIntExtra("id", 0);

				if (bd.update("listasItem", ctv, "_id=?",
						new String[] { String.valueOf(id) }) > 0) {
					Toast.makeText(getBaseContext(),
							"Sucesso ao atualizar o item.",
							Toast.LENGTH_SHORT).show();
					Intent trocatela = new
	    			Intent(this,EditarListaActivity.class);
					trocatela.putExtra("titulo", it.getStringExtra("titulo"));
					trocatela.putExtra("id", it.getIntExtra("idPresente", 0));
	    			this.startActivity(trocatela);
	    			this.finish();
				} else {
					Toast.makeText(getBaseContext(),
							"Erro ao atualizar o item.", Toast.LENGTH_SHORT)
							.show();
				}
				bd.close();
			} catch (Exception ex) {
				Toast.makeText(getBaseContext(), ex.getMessage(),
						Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	public void ApagarClick(View v) {
		try {
			final SQLiteDatabase bd = openOrCreateDatabase("Presente.db",
					Context.MODE_PRIVATE, null);

			Intent it = getIntent();

			final int id = it.getIntExtra("id", 0);

			
			Builder msg = new Builder(EditarItemActivity.this);
			msg.setMessage("Deseja apagar este item?");
			msg.setNegativeButton("Não", null);
			msg.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (bd.delete("listasItem", "_id=?",
							new String[] { String.valueOf(id) }) > 0) {
						Toast.makeText(getBaseContext(),
								"Sucesso ao apagar o item.", Toast.LENGTH_SHORT)
								.show();
						finish();
					} else {
						Toast.makeText(getBaseContext(), "Erro ao apagar o item.",
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
	
	private void createDb() {
		 
		 StringBuilder sqlListaDesejos = new StringBuilder();
			sqlListaDesejos.append("CREATE TABLE IF NOT EXISTS listasItem (");
			sqlListaDesejos.append("_id INTEGER PRIMARY KEY, ");
			sqlListaDesejos.append("nomeItem VARCHAR(30), ");
			sqlListaDesejos.append("categoriaItem VARCHAR(30), ");
			sqlListaDesejos.append("precoMin NUMERIC(7,2), ");
			sqlListaDesejos.append("precoMax NUMERIC(7,2));");
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
}