package br.iesb.mobile.alunoonline;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.iesb.mobile.alunoonline.Model.Produto;
import br.iesb.mobile.alunoonline.util.UtlListeCompre;

public class ListaProdutos extends AppCompatActivity implements ProdutoRecyclerClickListener {


    private ProdutosRecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    private String nome_lista;
    private ProgressDialog load;
    private UtlListeCompre utl;

    // Lista para cada mercado
    private List<Produto> listaProdutosMercadoA = new ArrayList<>();
    private List<Produto> listaProdutosMercadoB = new ArrayList<>();
    private List<Produto> listaProdutosMercadoC = new ArrayList<>();
    private List<Produto> todosProdutos = new ArrayList<>();
    List<Produto> listaRV = new ArrayList<>();
    Produto[] vetorCompras;

    UtlListeCompre utlListeCompre = new UtlListeCompre();

    private Button btnFinalizar;
    Toolbar toolbar;
    FirebaseDatabase database;
    String userId;
    FirebaseAuth firebaseAuth;
    int countItem = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_produtos);
        toolbar = findViewById(R.id.toolbarListaProdutos);
        setSupportActionBar(toolbar);

//        GetJson consomeAPI = new GetJson();

        btnFinalizar = findViewById(R.id.btnFinalizar);
        userId = this.firebaseAuth.getInstance().getUid();

        Intent it = getIntent();
        nome_lista = it.getStringExtra("nome");
        todosProdutos = (ArrayList) it.getSerializableExtra("todosProdutos");

        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                database = FirebaseDatabase.getInstance();
                DatabaseReference compra = database.getReference("/" + userId + "/" + nome_lista + "/Produtos");

                for (int i = 0; i < recyclerViewAdapter.auxListaCompras.size(); i++){

                    compra.child(String.valueOf(countItem)).child("Produto"     ).setValue(recyclerViewAdapter.auxListaCompras.get(i).getNome().toString());
                    compra.child(String.valueOf(countItem)).child("Preco"       ).setValue(String.valueOf(recyclerViewAdapter.auxListaCompras.get(i).getPreco()));
                    compra.child(String.valueOf(countItem)).child("Descricao"   ).setValue(recyclerViewAdapter.auxListaCompras.get(i).getDescricao ()    .toString());

                    compra.child(String.valueOf(countItem)).child("Sku"         ).setValue(recyclerViewAdapter.auxListaCompras.get(i).getSku()           .toString());
                    compra.child(String.valueOf(countItem)).child("Sku_Id"      ).setValue(recyclerViewAdapter.auxListaCompras.get(i).getSku_id()        .toString());
                    compra.child(String.valueOf(countItem)).child("Sku_Desc"    ).setValue(recyclerViewAdapter.auxListaCompras.get(i).getSku_descricao() .toString());

                    compra.child(String.valueOf(countItem)).child("Categoria"   ).setValue(recyclerViewAdapter.auxListaCompras.get(i).getCategoria_nome().toString());
                    compra.child(String.valueOf(countItem)).child("Categoria_Id").setValue(recyclerViewAdapter.auxListaCompras.get(i).getCategoria_id()  .toString());

                    compra.child(String.valueOf(countItem)).child("Mercado"     ).setValue(recyclerViewAdapter.auxListaCompras.get(i).getMercado_nome()  .toString());
                    compra.child(String.valueOf(countItem)).child("Mercado_Id"  ).setValue(recyclerViewAdapter.auxListaCompras.get(i).getMercado_id()    .toString());

                    compra.child(String.valueOf(countItem)).child("Quantidade").setValue(recyclerViewAdapter.auxListaCompras.get(i).getQtd());

                    countItem++;
                }
                //Iniciando Activity da lista de Compras
                Intent it = new Intent(v.getContext(), ListaCompras.class);
                it.putExtra("nome", nome_lista);
                it.putExtra("todosProdutos", (Serializable) todosProdutos);
                startActivity(it);
                finish();
            }
        });

        recyclerViewAdapter = new ProdutosRecyclerViewAdapter(this,listaProdutosMercadoA, nome_lista, todosProdutos);
        recyclerView = findViewById(R.id.ListaProdutosRecyclerView);
        recyclerViewAdapter.setRecyclerViewOnclickListener(this);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        for (int i = 0; i < todosProdutos.size(); i++){

            if(todosProdutos.get(i).getMercado_id().equals("1")){          // MERCADO A

                //Carrega lista rcyclerview
                listaProdutosMercadoA.add(todosProdutos.get(i));

            }else if(todosProdutos.get(i).getMercado_id().equals("2")){    // MERCADO B

                //Carrega lista rcyclerview
                listaProdutosMercadoB.add(todosProdutos.get(i));

            }else if (todosProdutos.get(i).getMercado_id().equals("3")){   // MERCADO C

                //Carrega lista rcyclerview
                listaProdutosMercadoC.add(todosProdutos.get(i));
            }

            recyclerViewAdapter.notifyDataSetChanged();
        }
        //Divide os itens da lista
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        //Executa chamada para buscar registros da API
//        consomeAPI.execute();
    }


    /***************** Icones Tool Bar ********************/
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_lista_listas, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int res_id = item.getItemId();
        if (res_id == R.id.icon_sort_compra){
            ordenarPorNome();
            Toast.makeText(this, "Ordena por Nome", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void ordenarPorNome(){
        vetorCompras = new Produto[listaProdutosMercadoA.size()];
        vetorCompras = listaProdutosMercadoA.toArray(vetorCompras);
        listaRV = (List<Produto>) utlListeCompre.ordenarListaNome(vetorCompras, 0, listaProdutosMercadoA.size() - 1);
        configuraRecyclerView(listaRV);
    }

    private void configuraRecyclerView(List<Produto> listaRV){
        recyclerViewAdapter = new ProdutosRecyclerViewAdapter(this,listaRV, nome_lista, todosProdutos);
        recyclerView = findViewById(R.id.ListaProdutosRecyclerView);
        recyclerViewAdapter.setRecyclerViewOnclickListener(this);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }


    public void chamaActivityCompras(){
        Intent it = new Intent(this, ListaCompras.class);
        startActivity(it);
    }

    @Override
    public void onClickListener(View v, int position) {

    }
}
