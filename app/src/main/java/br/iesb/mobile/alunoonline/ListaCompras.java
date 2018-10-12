package br.iesb.mobile.alunoonline;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.iesb.mobile.alunoonline.Model.Lista;
import br.iesb.mobile.alunoonline.Model.Mercado;
import br.iesb.mobile.alunoonline.Model.Parametros;
import br.iesb.mobile.alunoonline.Model.Produtos;
import br.iesb.mobile.alunoonline.fragments.FragmentAdapter;
import br.iesb.mobile.alunoonline.fragments.FragmentListaCompras;
import br.iesb.mobile.alunoonline.fragments.FragmentListaMercados;
import br.iesb.mobile.alunoonline.fragments.PassadorDeInformacao;
import br.iesb.mobile.alunoonline.util.UtlListeCompre;

public class ListaCompras extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String nome_lista;
    private FragmentAdapter fragmentAdapter;
    private Toolbar toolbar;
    private DatabaseReference comprasRef;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private ArrayList<Produtos> listaCompras = new ArrayList<>();
    private List<Mercado> listaMercados = new ArrayList<>();
    private DatabaseReference listaCompraReference, ref;
    private double precoTotal;
    private UtlListeCompre utlListeCompre = new UtlListeCompre();
    public ArrayList<Produtos> produtosMercado = new ArrayList<>();
    public double precoTotalLista = 0.0, arredondado = 0.0;
    private FragmentListaMercados fragmentListaMercados = new FragmentListaMercados();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_compras);

        Intent it = getIntent();
        nome_lista = it.getStringExtra("nome");

        comprasRef = database.getReference().child("/Parametros");

        setLista();
        getListaCompras();

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.view_pager);
        toolbar = findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);

        conciliarProdutosComListadeCompras();


    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    /***************** Icones Tool Bar ********************/
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.tab_lista_compra, menu);
        return true;
    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int res_id = item.getItemId();
//        if(res_id == R.id.icon_more_info_compra){
//            Toast.makeText(getApplicationContext(), "Icon de busca", Toast.LENGTH_SHORT).show();
//        }else if (res_id == R.id.icon_sort_compra){
//            Toast.makeText(getApplicationContext(), "Icon de Ordenação Activity", Toast.LENGTH_SHORT).show();
//            fragmentListaCompras.
//        }else{
//            Toast.makeText(getApplicationContext(), "Icone nao mapeado aqui", Toast.LENGTH_SHORT).show();
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    public double conciliarProdutosComListadeCompras() {
        int i = 0;
        Log.d("TESTE", "Conciliar Produtos Lista de Compras");
        DatabaseReference merRef = FirebaseDatabase.getInstance().getReference().child("/Mercados/1/");
        // for(i = 0; i < listaMercados.size(); i++){
        //merRef.child(String.valueOf(i+1));
        merRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                produtosMercado.clear();
                for (DataSnapshot c : dataSnapshot.getChildren()) {
                    Produtos p = new Produtos();
                    if (c.hasChild("produto")) {
                        p.setPreco(Double.parseDouble(c.child("preco").getValue().toString()));

                        p.setNome(c.child("produto").getValue().toString());

                        produtosMercado.add(p);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//        }
//
//        for(i = 1; i <= listaMercados.size(); i++){
//            precoTotal = utlListeCompre.precoTotaldoMercado(produtosMercado, 1);
//            atualizarPrecoTotalMercado(precoTotal);
//        }
        return precoTotal;
    }

    public void setLista() {

        Map<String, Parametros> param = new HashMap<>();
        param.put("Lista", new Parametros(nome_lista));

        comprasRef.setValue(param);
    }

    private void getListaCompras() {
        try {
            database = FirebaseDatabase.getInstance();
            listaCompraReference = database.getReference().child("/Compras/"+nome_lista);
        } catch (Exception e) {
            System.out.print("ERRO - " + e.getMessage());
        }

        listaCompraReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot c : dataSnapshot.getChildren()) {
                    Produtos produto = new Produtos();
                    try {
                        produto.setDesc(c.child("Descricao").getValue().toString());
                        produto.setNome(c.child("Produto").getValue().toString());
                        produto.setMarca(c.child("Marca").getValue().toString());
                        produto.setPreco(Double.parseDouble(c.child("Preco")
                                .getValue().toString().replace(',', '.')));

                    } catch (Exception e) {
                        System.out.println("ERRO Recuperando produto: " + e.getMessage());
                        e.printStackTrace();
                    }

                    if (c.hasChild("Produto")) {
                        listaCompras.add(produto);
                        calculaPrecoTotalLista(produto.getPreco());
                    }
                }
                arredondarPreco();
                fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), getResources().getStringArray(R.array.titulo_tabs_lista_compras), nome_lista, listaCompras, arredondado);
                viewPager.setAdapter(fragmentAdapter);
                tabLayout.setupWithViewPager(viewPager);

            }

            public void calculaPrecoTotalLista(double precoProdAtual) {
                precoTotalLista += precoProdAtual;
            }

            public void arredondarPreco() {
                arredondado = precoTotalLista;
                arredondado *= (Math.pow(10, 2)); //Multiplica por 100

                arredondado = Math.ceil(arredondado); //Arredonda sempre pra cima

                arredondado /= (Math.pow(10, 2)); // Divide por 100 revertendo a primeira operação

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ListaCompras.this, "Não foi possível recuperar os dados do banco de dados", Toast.LENGTH_LONG).show();
                Log.e("ERRO - Lista de Compras", databaseError.getMessage());

            }
        });

    }
}

