package br.iesb.mobile.alunoonline;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
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
import android.support.v7.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.iesb.mobile.alunoonline.fragments.FragmentAdapter;
import br.iesb.mobile.alunoonline.fragments.FragmentListaCompras;
import br.iesb.mobile.alunoonline.fragments.FragmentListaMercados;
import br.iesb.mobile.alunoonline.fragments.PassadorDeInformacao;

public class ListaCompras extends FragmentActivity{

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String    nome_lista;
    private FragmentAdapter fragmentAdapter;
    private Fragment fragmentListaCompras;
    private Fragment fragmentListaMercados;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_lista_compras);

            tabLayout = findViewById(R.id.tabLayout);
            viewPager = findViewById(R.id.view_pager);
            Toolbar toolbar = findViewById(R.id.my_awesome_toolbar);


            fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), getResources().getStringArray(R.array.titulo_tabs_lista_compras));
            viewPager.setAdapter(fragmentAdapter);
            tabLayout.setupWithViewPager(viewPager);

            //fragmentListaCompras = new FragmentListaCompras();
            fragmentListaMercados = new FragmentListaMercados();

            Intent it = getIntent();
            nome_lista = it.getStringExtra("nome");
            this.enviaMensagemParaOFragment(nome_lista, new FragmentListaCompras());


    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void enviaMensagemParaOFragment(String nome, PassadorDeInformacao fragment) {
        fragment.passaInformacao(nome);
    }
}
