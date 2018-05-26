package br.iesb.mobile.alunoonline;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListaProdutos extends AppCompatActivity implements ProdutoRecyclerClickListener {

    private ProdutosRecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    private FirebaseDatabase database;
    private DatabaseReference listaProdutosReference;
    private String nome_lista;


    private List<Produtos> listaProdutos = new ArrayList<>();
    private ListaCompras listaCompra = new ListaCompras();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_produtos);

        Intent it = getIntent();
        nome_lista = it.getStringExtra("nome");

        recyclerViewAdapter = new ProdutosRecyclerViewAdapter(this,listaProdutos, nome_lista);
        recyclerView = findViewById(R.id.ListaProdutosRecyclerView);
        recyclerViewAdapter.setRecyclerViewOnclickListener(this);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Divide os itens da lista
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        getListaProdutos();
    }

    private void getListaProdutos(){

        //Recuperar do banco de dados
        database = FirebaseDatabase.getInstance();
        listaProdutosReference = database.getReference().child("/Produtos");

        listaProdutosReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaProdutos.clear();
                for(DataSnapshot c : dataSnapshot.getChildren()){
                    Produtos produto = new Produtos();
                    produto.setDesc( c.child("descricao").getValue().toString());
                    produto.setNome( c.child("produto")  .getValue().toString());
                    produto.setMarca(c.child("marca")    .getValue().toString());
                    produto.setPreco(Double.parseDouble(c.child("preco")    .getValue().toString().replace(',', '.')));
                    listaProdutos.add(produto);
                }
                recyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ListaProdutos.this, "Não foi possível recuperar os dados do banco de dados", Toast.LENGTH_LONG).show();
                Log.e("Erro aqui", databaseError.getMessage());
                Log.e("Detalhe do erro", databaseError.getDetails());

            }
        });
    }

    public void chamaActivityCompras(){
        Intent it = new Intent(this, ListaCompras.class);
        startActivity(it);
    }

    @Override
    public void onClickListener(View v, int position) {

    }
}
