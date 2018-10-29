package br.iesb.mobile.alunoonline;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.iesb.mobile.alunoonline.Model.Lista;
import br.iesb.mobile.alunoonline.Model.Produto;
import br.iesb.mobile.alunoonline.util.UtlListeCompre;

public class ListaListas extends AppCompatActivity implements ProdutoRecyclerClickListener{

    RecyclerView recyclerView;
    ListaAdapter recyclerViewAdapter;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference comprasRef;
    FloatingActionButton fab;
    private EditText nome_lista_dialog;
    private UtlListeCompre utlListeCompre;
    private FirebaseAuth firebaseAuth;
    private String userId;
    private ActionBar actionBar;

    Lista[] vetorListas;

    private List<Produto> todosProdutos= new ArrayList<>();


    public List<Lista> listas = new ArrayList<>();
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_listas);
        toolbar = findViewById(R.id.toolbarListas);
        setSupportActionBar(toolbar);

        utlListeCompre = new UtlListeCompre();

        Intent it = getIntent();
        todosProdutos = (ArrayList) it.getSerializableExtra("todosProdutos");

        //Recuperando id do usuario logado
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();

        comprasRef = database.getReference().child("/" + userId);
        nome_lista_dialog = new EditText(this);
        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(25);
        nome_lista_dialog.setFilters(filterArray);

        nome_lista_dialog.setMaxWidth(25);


        /**
         * Layout para o edit Text da caixa de dialogo
         */
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        nome_lista_dialog.setLayoutParams(lp);

        fab = findViewById(R.id.fab);


        recyclerViewAdapter = new ListaAdapter(ListaListas.this, listas, todosProdutos);
        recyclerView = findViewById(R.id.ListasRecyclerView);
        recyclerViewAdapter.setRecyclerViewOnclickListener(this);
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
                        .setTitle("Dê um nome para sua lista.")
                        .setView(nome_lista_dialog)
                        .setPositiveButton("Criar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (nome_lista_dialog.toString().isEmpty()) {
                                    //tratar erro de nome vazio
                                    Toast.makeText(ListaListas.this, "Insira um nome para a lista", Toast.LENGTH_SHORT).show();
                                } else {

                                    // Cria lista no path do usuario
                                    comprasRef.child(nome_lista_dialog.getText().toString()).child("Nome_Lista")
                                            .setValue(nome_lista_dialog.getText().toString());


                                    Toast.makeText(ListaListas.this, "Lista: " + nome_lista_dialog.getText().toString() + " criada.", Toast.LENGTH_SHORT).show();
                                    Intent it = new Intent(ListaListas.this, ListaProdutos.class);
                                    it.putExtra("nome", nome_lista_dialog.getText().toString());
                                    it.putExtra("todosProdutos", (Serializable) todosProdutos);
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
                    Lista lista = new Lista();
                    lista.setNome(c.getKey().toString());

                    if (c.hasChild("Preço")){
                        lista.setPreco(Double.parseDouble(c.child("Preço").getValue().toString()));
                    }
                    listas.add(lista);
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

    @Override
    protected void onRestart() {
        super.onRestart();
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(ListaListas.this)
                        .setTitle("Dê um nome para sua lista.")
                        .setView(nome_lista_dialog)
                        .setPositiveButton("Criar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (nome_lista_dialog.toString().isEmpty()) {
                                    //tratar erro de nome vazio
                                    Toast.makeText(ListaListas.this, "Insira um nome para a lista", Toast.LENGTH_SHORT).show();
                                } else {

                                    // Cria lista no path do usuario
                                    comprasRef.child(nome_lista_dialog.getText().toString()).child("Nome_Lista")
                                            .setValue(nome_lista_dialog.getText().toString());


                                    Toast.makeText(ListaListas.this, "Lista: " + nome_lista_dialog.getText().toString() + " criada.", Toast.LENGTH_SHORT).show();
                                    Intent it = new Intent(ListaListas.this, ListaProdutos.class);
                                    it.putExtra("nome", nome_lista_dialog.getText().toString());
                                    it.putExtra("todosProdutos", (Serializable) todosProdutos);
                                    startActivity(it);
                                }
                            }
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
            }
        });
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

        // Define the listener
        MenuItem.OnActionExpandListener expandListener = new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when action item collapses
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                return true;  // Return true to expand action view
            }
        };

        if (res_id == R.id.icon_sort_compra){
            ordenarPreco();
            Toast.makeText(this, "Ordena por Preco", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void ordenarPreco(){
        vetorListas = new Lista[listas.size()];
        vetorListas = listas.toArray(vetorListas);
        listas = (List<Lista>) utlListeCompre.ordenarListaListas(vetorListas, 0, listas.size() - 1);
        configuraRecyclerView(listas);
    }


    private void configuraRecyclerView(List<Lista> listaRV){

        recyclerViewAdapter = new ListaAdapter(ListaListas.this, listaRV, todosProdutos);
        recyclerView = findViewById(R.id.ListasRecyclerView);
        recyclerViewAdapter.setRecyclerViewOnclickListener(this);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Divide os itens da lista
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @Override
    public void onClickListener(View v, int position) {}
}
