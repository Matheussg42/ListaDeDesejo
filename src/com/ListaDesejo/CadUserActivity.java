package com.ListaDesejo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class CadUserActivity extends Activity {
	EditText nomeCad, emailCad, senhaCad;
	Button limparCampos,btnCancelar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.cadastrar_user);
		limparCampos = (Button)findViewById(R.id.limparCampos);
		btnCancelar = (Button)findViewById(R.id.btnCancelar);
		nomeCad = (EditText)findViewById(R.id.nomeCad);
		emailCad = (EditText)findViewById(R.id.emailCad);
		senhaCad = (EditText)findViewById(R.id.senhaCad);
	
	
		btnCancelar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent trocartela = new
				Intent(CadUserActivity.this,LoginActivity.class);
				CadUserActivity.this.startActivity(trocartela);
				CadUserActivity.this.finish();

			}
		});
	
	}

	 @Override
	protected void onResume() {
	        // TODO Auto-generated method stub
	        super.onResume();
	        limparCampos.setOnClickListener(new View.OnClickListener() {

	            @Override
	            public void onClick(View v) {
	                // TODO Auto-generated method stub
	            	 if (v.getId() == R.id.limparCampos);
	                 nomeCad.setText("");
	                 if (v.getId() == R.id.limparCampos);
	                 emailCad.setText("");
	                 if (v.getId() == R.id.limparCampos);
	                 senhaCad.setText("");
	            }
	        });
	    }
	
}