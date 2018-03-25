package br.iesb.mobile.alunoonline;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Cadastro extends AppCompatActivity {

    private TextView login;
    private EditText editNome, editSobrenome, editSenhaCad, editEmailCad, editTelefone;
    private Button btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        login           = findViewById( R.id.textFazerLogin );
        editNome        = findViewById( R.id.editNome       );
        editSobrenome   = findViewById( R.id.editSobrenome  );
        editSenhaCad    = findViewById( R.id.editSenhaCad   );
        editEmailCad    = findViewById( R.id.editEmailCad   );
        editTelefone    = findViewById( R.id.editTelefone   );
        btnRegistrar    = findViewById( R.id.btnRegistrar   );

        //Volta para tela de login
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Cadastro.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //Cadastra novo usuario
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editNome.getText().toString().isEmpty()){
                    editNome.setError("Nome é obrigatorio");
                    editNome.requestFocus();
                    return;
                }
                if(editSobrenome.getText().toString().isEmpty()){
                    editSobrenome.setError("Sobrenome é obrigatório");
                    editSobrenome.requestFocus();
                    return;
                }

                if(editEmailCad.getText().toString().isEmpty()){
                    editEmailCad.setError("Email é obrigatório");
                    editEmailCad.requestFocus();
                    return;
                }


                if(editSenhaCad.getText().toString().isEmpty()){
                    editSenhaCad.setError("Senha é obrigatória");
                    editSenhaCad.requestFocus();
                    return;
                }

                cadastrar();
            }
        });
    }

    private void cadastrar() {
        FirebaseAuth cadastro = FirebaseAuth.getInstance();

        final Task<AuthResult> processo = cadastro.createUserWithEmailAndPassword(editEmailCad.getText().toString(), editSenhaCad.getText().toString());
        processo.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(processo.isSuccessful()){
                    Intent intent = new Intent(Cadastro.this, Principal.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(Cadastro.this, "Cadastro inválido!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
