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
import br.iesb.mobile.alunoonline.Model.Parametros;
import br.iesb.mobile.alunoonline.fragments.FragmentAdapter;
import br.iesb.mobile.alunoonline.fragments.FragmentListaCompras;
import br.iesb.mobile.alunoonline.fragments.FragmentListaMercados;
import br.iesb.mobile.alunoonline.fragments.PassadorDeInformacao;

public class ListaCompras extends AppCompatActivity{

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String    nome_lista;
    private FragmentAdapter fragmentAdapter;
    private Fragment fragmentListaCompras;
    private Fragment fragmentListaMercados;
    private Toolbar toolbar;
    private DatabaseReference comprasRef;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_lista_compras);

        Intent it = getIntent();
        nome_lista = it.getStringExtra("nome");

        comprasRef = database.getReference().child("/Parametros");

        setLista();

        tabLayout = findViewById(R.id.tabLayout);
            viewPager = findViewById(R.id.view_pager);
            toolbar = findViewById(R.id.my_awesome_toolbar);
            setSupportActionBar(toolbar);


            fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), getResources().getStringArray(R.array.titulo_tabs_lista_compras));
            viewPager.setAdapter(fragmentAdapter);
            tabLayout.setupWithViewPager(viewPager);


            fragmentListaCompras = new FragmentListaCompras();
            fragmentListaMercados = new FragmentListaMercados();



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

    public void setLista(){


        Map<String, Parametros> param = new HashMap<>();
        param.put("Lista", new Parametros(nome_lista));

        comprasRef.setValue(param);
    }

}

