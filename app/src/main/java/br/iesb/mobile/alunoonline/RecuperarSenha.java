package br.iesb.mobile.alunoonline;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RecuperarSenha extends AppCompatActivity {

    private Button btnRecuperaSenha;
    private EditText editEmailRecupera;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_senha);

        btnRecuperaSenha = findViewById(R.id.btnRecuperaSenha);
        editEmailRecupera = findViewById(R.id.editEmailRecuperar);

        auth = FirebaseAuth.getInstance();

        btnRecuperaSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.sendPasswordResetEmail(editEmailRecupera.getText().toString());
                Intent intent = new Intent(RecuperarSenha.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
