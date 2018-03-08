package br.iesb.mobile.alunoonline;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private TextView esqueceuSenha, cadastro;
    private Button loginBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Localizando Componentes
        esqueceuSenha = findViewById(R.id.textEsqueceuSenha );
        cadastro      = findViewById(R.id.textCadastro      );
        loginBtn      = findViewById(R.id.btnLogin          );


        esqueceuSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RecuperarSenha.class);
                startActivity(intent);
            }
        });

        cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Cadastro.class);
                startActivity(intent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }

        });
    }

    private void login() {
        //Autencicacao Firebase
        FirebaseAuth auth = FirebaseAuth.getInstance();

        //Recuperando campos de login
        EditText editEmail = findViewById(R.id.editEmail);
        EditText editSenha = findViewById(R.id.editSenha);

        //Metodo assincrono
        Task<AuthResult> processo = auth.signInWithEmailAndPassword(editEmail.getText().toString(), editSenha.getText().toString());
        processo.addOnCompleteListener(new OnCompleteListener<AuthResult>() { //Metodo que espera o resultado da autenticacao
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(MainActivity.this, Principal.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "E-mail ou senha inválido!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
