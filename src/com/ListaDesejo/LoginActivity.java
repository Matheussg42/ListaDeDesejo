package com.ListaDesejo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity {
	EditText inputLogin, inputSenha;
	Button btnEntrar, btnLimpar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.ListaDesejo.R.layout.login);
		
		btnLimpar = (Button)findViewById (com.ListaDesejo.R.id.btnLimpar);
		btnEntrar = (Button)findViewById (com.ListaDesejo.R.id.btnEntrar);
		inputLogin = (EditText)findViewById(com.ListaDesejo.R.id.inputLogin);
		inputSenha = (EditText)findViewById(com.ListaDesejo.R.id.inputSenha);

		btnEntrar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent trocatela = new
				Intent(LoginActivity.this,TabhostActivity.class);
				LoginActivity.this.startActivity(trocatela);
				LoginActivity.this.finish();

			}
		});
		
		final TextView txtView = (TextView) this.findViewById(com.ListaDesejo.R.id.linkCad);
		txtView.setOnClickListener(new OnClickListener() {

		    @Override
			public void onClick(View v) {
		        LoginActivity.this.startActivity(new Intent(LoginActivity.this, CadUserActivity.class));
		    }
		});
	} 
	
	@Override
	protected void onResume() {
        super.onResume();
        btnLimpar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            	 if (v.getId() == com.ListaDesejo.R.id.btnLimpar);
                 inputLogin.setText("");
                 if (v.getId() == com.ListaDesejo.R.id.btnLimpar);
                 inputSenha.setText("");
            }
        });
	}
	
	
}
