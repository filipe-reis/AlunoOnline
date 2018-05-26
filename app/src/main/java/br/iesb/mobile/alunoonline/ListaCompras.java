package br.iesb.mobile.alunoonline;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListaCompras extends AppCompatActivity {
    private CompraAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    private FirebaseDatabase database;
    private DatabaseReference listaCompraReference;
    String nome_lista;
    private Button   btnCriaLista ;
    private TextView txtPrecoTotalLista;

/*    public double getPrecoTotalLista() {
        return precoTotalLista;
    }

    public void setPrecoTotalLista(Double precoTotalLista) {
        this.precoTotalLista = precoTotalLista;
    }*/

    private double precoTotalLista = 0.0;

    public List<Produtos> listaCompras = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_compras);

        btnCriaLista = findViewById(R.id.btnCriarLista);
        txtPrecoTotalLista = findViewById(R.id.txtPrecoTotaLista);

        Intent it = getIntent();
        nome_lista = it.getStringExtra("nome");

        recyclerViewAdapter = new CompraAdapter(ListaCompras.this, listaCompras);
        recyclerView = findViewById(R.id.ListaComprasRecyclerView);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Divide os itens da lista
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        getListaCompras();

        btnCriaLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //persistirListaUID();
            }
        });
    }

    private void getListaCompras() {
    //Recuperar do banco de dados
        try{
            database = FirebaseDatabase.getInstance();
            listaCompraReference = database.getReference().child("/Compras/" + nome_lista);
        }catch(Exception e){
            System.out.print("ERRO - " + e.getMessage());
        }

        listaCompraReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot c : dataSnapshot.getChildren()){
                    Produtos produto = new Produtos();
                    try{
                        produto.setDesc( c.child("Descricao").getValue().toString());
                        produto.setNome( c.child("Produto")  .getValue().toString());
                        produto.setMarca(c.child("Marca")    .getValue().toString());
                        produto.setPreco(Double.parseDouble(c.child("Preco")
                                .getValue().toString().replace(',', '.')));

                    }catch(Exception e){
                        System.out.println("ERRO Recuperando produto: " + e.getMessage());
                        e.printStackTrace();
                    }

                    if(c.hasChild("Produto")){
                        listaCompras.add(produto);
                        calculaPrecoTotalLista(produto.getPreco());
                    }
                }
                recyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ListaCompras.this, "Não foi possível recuperar os dados do banco de dados", Toast.LENGTH_LONG).show();
                Log.e("ERRO - Lista de Compras", databaseError.getMessage());

            }
        });

    }

    /**
     * Incremento do preco do novo produto inserido
     * E seta na tela
     * @param precoProdAtual
     */
    public void calculaPrecoTotalLista(double precoProdAtual){

        precoTotalLista += precoProdAtual;

        txtPrecoTotalLista.setText(String.valueOf(precoTotalLista));
    }



}
