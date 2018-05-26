package br.iesb.mobile.alunoonline;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListaListas extends AppCompatActivity {

    RecyclerView recyclerView;
    ListaAdapter recyclerViewAdapter;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference comprasRef;
    FloatingActionButton fab;
    private EditText nome_lista_dialog;

    public List<String> listas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_listas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        comprasRef = database.getReference().child("/Compras");
        nome_lista_dialog = new EditText(this);
        /**
         * Layout para o edit Text da caixa de dialogo
         */
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        nome_lista_dialog.setLayoutParams(lp);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Adicionar lista", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        recyclerViewAdapter = new ListaAdapter(ListaListas.this, listas);
        recyclerView = findViewById(R.id.ListasRecyclerView);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Divide os itens da lista
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        getListas();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(ListaListas.this)
                        .setTitle("Criar nova lista")
                        .setView(nome_lista_dialog)
                        .setPositiveButton("Criar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (nome_lista_dialog.toString().isEmpty()) {
                                    //tratar erro de nome vazio
                                    Toast.makeText(ListaListas.this, "Insira um nome para a lista", Toast.LENGTH_SHORT).show();
                                } else {
                                    comprasRef.child(nome_lista_dialog.getText().toString()).child("Nome_Lista")
                                            .setValue(nome_lista_dialog.getText().toString());

                                    Toast.makeText(ListaListas.this, "Lista " + nome_lista_dialog.getText().toString() + " Criada.", Toast.LENGTH_SHORT).show();
                                    Intent it = new Intent(ListaListas.this, ListaProdutos.class);
                                    it.putExtra("nome", nome_lista_dialog.getText().toString());
                                    startActivity(it);
                                }
                            }
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
            }
        });
    }

    public void getListas(){

        comprasRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listas.clear();
                for(DataSnapshot c : dataSnapshot.getChildren()){
                    listas.add(c.getKey().toString());
                }
                recyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ListaListas.this, "Não foi possível recuperar os dados do banco de dados", Toast.LENGTH_LONG).show();
                Log.e("ERRO - Lista de Compras", databaseError.getMessage());

            }
        });


    }
}
