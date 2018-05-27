package br.iesb.mobile.alunoonline;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

public class TelaInicial extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ProdutoRecyclerClickListener {

    RecyclerView recyclerView;
    ListaAdapter recyclerViewAdapter;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference comprasRef;
    private EditText nome_lista_dialog;

    public List<String> listas = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial);
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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(TelaInicial.this)
                        .setTitle("Criar nova lista")
                        .setView(nome_lista_dialog)
                        .setPositiveButton("Criar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (nome_lista_dialog.toString().isEmpty()) {
                                    //tratar erro de nome vazio
                                    Toast.makeText(TelaInicial.this, "Insira um nome para a lista", Toast.LENGTH_SHORT).show();
                                } else {
                                    comprasRef.child(nome_lista_dialog.getText().toString()).child("Nome_Lista")
                                            .setValue(nome_lista_dialog.getText().toString());

                                    Toast.makeText(TelaInicial.this, "Lista " + nome_lista_dialog.getText().toString() + " Criada.", Toast.LENGTH_SHORT).show();
                                    Intent it = new Intent(TelaInicial.this, ListaProdutos.class);
                                    it.putExtra("nome", nome_lista_dialog.getText().toString());
                                    startActivity(it);
                                }
                            }
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        recyclerViewAdapter = new ListaAdapter(TelaInicial.this, listas);
        recyclerView = findViewById(R.id.ListasRecyclerView);
        recyclerViewAdapter.setRecyclerViewOnclickListener(this);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Divide os itens da lista
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        getListas();
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
                Toast.makeText(TelaInicial.this, "Não foi possível recuperar os dados do banco de dados", Toast.LENGTH_LONG).show();
                Log.e("ERRO - Lista de Compras", databaseError.getMessage());

            }
        });
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tela_inicial, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClickListener(View v, int position) {}
}
