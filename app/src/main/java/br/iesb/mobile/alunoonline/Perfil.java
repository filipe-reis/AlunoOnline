package br.iesb.mobile.alunoonline;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Perfil extends AppCompatActivity {

    public static final int REQUEST_IMAGE_CAPTURE = 9999;

    EditText edtNome, edtDataNasc, edtSexo;
    Button btnGravar, btnRemover, btnFoto, btnSair, btnMap;

    FirebaseDatabase database;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        final MainActivity mainActivity = new MainActivity();

        edtNome     = findViewById(R.id.edtNome     );
        edtDataNasc = findViewById(R.id.edtDataNasc );
        edtSexo     = findViewById(R.id.edtSexo     );

        btnGravar   = findViewById(R.id.btnGravar);
        btnFoto     = findViewById(R.id.btnFoto);
        btnSair     = findViewById(R.id.btnSair);
        btnMap      = findViewById(R.id.btnMap);

        database = FirebaseDatabase.getInstance();
        auth     = FirebaseAuth.getInstance(); //Saber qual o usuario logado

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Perfil.this, MapActivity.class);
                startActivity(intent);
            }
        });


        btnGravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gravar();
            }
        });

        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(it, REQUEST_IMAGE_CAPTURE);
            }
        });

        btnSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.getInstance().signOut();
                goMainAcvtivity();
            }
        });
    }

    public void gravar(){

        String nome = edtNome.getText().toString();
        String dataNasc = edtDataNasc.getText().toString();
        String sexo = edtSexo.getText().toString();

        FirebaseUser user = auth.getCurrentUser();

        DatabaseReference alunos = database.getReference("/Alunos");
        alunos.child(user.getUid()).child("Nome").setValue(nome);
        alunos.child(user.getUid()).child("Data de Nascimento").setValue(dataNasc);
        alunos.child(user.getUid()).child("Sexo").setValue(sexo);
    }

    public void signOut(View view) {
        if(LoginManager.getInstance() != null){
            LoginManager.getInstance().logOut();
        }else if(auth.getCurrentUser() != null){
            auth.signOut();
        }//else if google


        goMainAcvtivity();
    }

    private void goMainAcvtivity() {
        Intent intent = new Intent(Perfil.this, MainActivity.class);
        startActivity(intent);
    }
}
