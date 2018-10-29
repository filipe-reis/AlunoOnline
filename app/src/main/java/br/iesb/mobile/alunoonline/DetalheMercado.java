package br.iesb.mobile.alunoonline;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.iesb.mobile.alunoonline.Model.Produto;
import br.iesb.mobile.alunoonline.util.UtlListeCompre;

public class DetalheMercado extends AppCompatActivity {

    private DetalheMercadoAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    Toolbar toolbar;
    UtlListeCompre utlListeCompre;

    List<Produto> listaCompras = new ArrayList<>();
    Produto[] vetorCompras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_mercado);
        toolbar = findViewById(R.id.toolbarDetalheMercados);
        setSupportActionBar(toolbar);

        utlListeCompre = new UtlListeCompre();

        Intent it = getIntent();
        listaCompras = (ArrayList) it.getSerializableExtra("listaCompras");
        Log.i("TESTE", "TAMANHO DA LISTA >>> " + listaCompras.size());

        recyclerViewAdapter = new DetalheMercadoAdapter(listaCompras);
        recyclerView = findViewById(R.id.DetalheMercado);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

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
            ordenarPreco();
            Toast.makeText(this, "Ordena por Preco", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }



    public void ordenarPreco(){
        vetorCompras = new Produto[listaCompras.size()];
        vetorCompras = listaCompras.toArray(vetorCompras);
        listaCompras = (List<Produto>) utlListeCompre.ordenarListaPreco(vetorCompras, 0, listaCompras.size() - 1);
        configuraRecyclerView(listaCompras);
    }

    private void configuraRecyclerView(List<Produto> listaRV){
        recyclerViewAdapter = new DetalheMercadoAdapter(listaRV);
        recyclerView = findViewById(R.id.DetalheMercado);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

    }
}
