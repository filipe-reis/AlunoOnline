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

    private List<Produtos> listaProdutos = new ArrayList<>();
    private ListaCompras listaCompra = new ListaCompras();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_produtos);

        recyclerViewAdapter = new ProdutosRecyclerViewAdapter(this,listaProdutos);
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
                    produto.setPreco(c.child("preco")    .getValue().toString());
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




  /*     Produtos produto0 = new Produtos();
        produto0.setId(1);
        produto0.setNome("Arroz");
        produto0.setPreco(13.50);
        produto0.setDesc("Arroz Tio Jorge");
        listaProdutos.add(produto0);

        Produtos produto1 = new Produtos();
        produto1.setId(2);
        produto1.setNome("Feijão");
        produto1.setPreco(8.50);
        produto1.setDesc("Feijão Tio Jorge");
        listaProdutos.add(produto1);

        Produtos produto2 = new Produtos();
        produto2.setId(3);
        produto2.setNome("Azeite");
        produto2.setPreco(7);
        produto2.setDesc("Azeite extra extra extra virgem");
        listaProdutos.add(produto2);

        Produtos produto3 = new Produtos();
        produto3.setId(4);
        produto3.setNome("Oleo");
        produto3.setPreco(34.20);
        produto3.setDesc("Azeite extra extra extra virgem");
        listaProdutos.add(produto3);

        Produtos produto4 = new Produtos();
        produto4.setId(5);
        produto4.setNome("Farofa");
        produto4.setPreco(100.25);
        produto4.setDesc("Azeite extra extra extra virgem");
        listaProdutos.add(produto4);

        Produtos produto5 = new Produtos();
        produto5.setId(6);
        produto5.setNome("Biscoito");
        produto5.setPreco(49.99);
        produto5.setDesc("Azeite extra extra extra virgem");
        listaProdutos.add(produto5);
        listaProdutos.add(produto0);
        listaProdutos.add(produto1);
        listaProdutos.add(produto2);
        listaProdutos.add(produto3);
        listaProdutos.add(produto4);*/
    }

    public void chamaActivityCompras(){
        Intent it = new Intent(this, ListaCompras.class);
        startActivity(it);
    }

    @Override
    public void onClickListener(View v, int position) {

    }
}
