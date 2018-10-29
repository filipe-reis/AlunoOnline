package br.iesb.mobile.alunoonline;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.iesb.mobile.alunoonline.Model.Produto;

public class RecuperarSenha extends AppCompatActivity {

    private Button btnRecuperaSenha;
    private EditText editEmailRecupera;
    private FirebaseAuth auth;

    private List<Produto> todosProdutos= new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_senha);

        Intent it = getIntent();
        todosProdutos = (ArrayList) it.getSerializableExtra("todosProdutos");

        btnRecuperaSenha = findViewById(R.id.btnRecuperaSenha);
        editEmailRecupera = findViewById(R.id.editEmailRecuperar);

        auth = FirebaseAuth.getInstance();

        btnRecuperaSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.sendPasswordResetEmail(editEmailRecupera.getText().toString());
                Toast.makeText(RecuperarSenha.this, "E-mail enviado.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RecuperarSenha.this, MainActivity.class);
                intent.putExtra("todosProdutos", (Serializable) todosProdutos);
                startActivity(intent);
                finish();
            }
        });
    }
}
