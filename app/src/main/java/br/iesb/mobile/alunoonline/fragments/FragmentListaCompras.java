package br.iesb.mobile.alunoonline.fragments;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.iesb.mobile.alunoonline.ListaCompras;
import br.iesb.mobile.alunoonline.Model.Produtos;
import br.iesb.mobile.alunoonline.R;
import br.iesb.mobile.alunoonline.UtlListeCompre;

public class FragmentListaCompras extends Fragment implements View.OnClickListener{
    private CompraAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    private FirebaseDatabase database;
    private DatabaseReference listaCompraReference;
    String nome_lista;
    private Button btnCriaLista;
    private TextView txtPrecoTotalLista;

    public double precoTotalLista = 0.0;
    UtlListeCompre utlListeCompre;

    public List<Produtos> listaCompras = new ArrayList<>();
    Produtos[] vetorCompras;

    public FragmentListaCompras(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.tab_compras, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        recyclerViewAdapter = new CompraAdapter(listaCompras);
        recyclerView = view.findViewById(R.id.ListaCompras);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerViewAdapter);

        //Divide os itens da lista
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));

        getListaCompras();

        utlListeCompre     = new UtlListeCompre();
        btnCriaLista       = view.findViewById(R.id.btnCriarLista);
        txtPrecoTotalLista = view.findViewById(R.id.txtPrecoTotaLista);

        btnCriaLista.setOnClickListener(this);
    }

    private void configuraRecyclerView(List<Produtos> listaRV, View view){
        recyclerViewAdapter = new CompraAdapter(listaRV);
        recyclerView = view.findViewById(R.id.ListaCompras);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //Divide os itens da lista
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
    }

    private void getListaCompras() {
        ListaCompras nomeListaCompra = new ListaCompras();
        //Recuperar do banco de dados
        try{
            database = FirebaseDatabase.getInstance();
            listaCompraReference = database.getReference().child("/Compras/filipe");
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
                Toast.makeText(getContext(), "Não foi possível recuperar os dados do banco de dados", Toast.LENGTH_LONG).show();
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

        precoTotalLista = Math.ceil(precoTotalLista);
        txtPrecoTotalLista.setText(String.valueOf(precoTotalLista).replace('.', ','));
    }

    @Override
    public void onClick(View view) {
        //Cria lista
    }

    /**
     * Configuração RecyclerView
     */
    public class CompraAdapter extends RecyclerView.Adapter<CompraAdapter.CompraViewHolder> {

        private List<Produtos> listaCompras;

        public CompraAdapter(List<Produtos> listaCompras) {
            this.listaCompras = listaCompras;
        }

        @NonNull
        @Override
        public CompraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_produtos, parent, false);
            return new CompraViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CompraViewHolder holder, int position) {
            Produtos produtos = listaCompras.get(position);

            holder.nome.setText(produtos.getNome());
            holder.preco.setText(String.valueOf(produtos.getPreco()));

            holder.produto = produtos;
        }

        @Override
        public int getItemCount() {
            return listaCompras.isEmpty() ? 0 : listaCompras.size();
        }

        public void addItemLista(int position){
            notifyItemInserted(position);
        }

        public class CompraViewHolder extends RecyclerView.ViewHolder{

            public TextView nome, preco;
            public Produtos produto;

            public CompraViewHolder(View itemView) {
                super(itemView);
                nome = itemView.findViewById(R.id.nome_produto);
                preco = itemView.findViewById(R.id.preco);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) itemView.getLayoutParams();
                params.setMargins(20, 0, 0, 0);
                itemView.setLayoutParams(params);

            }
        }
    }


    /***************** Icones Tool Bar ********************/

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int res_id = item.getItemId();
        if(res_id == R.id.icon_more_info_compra){

            Toast.makeText(getContext(), "Icon de busca", Toast.LENGTH_SHORT).show();

        }else if (res_id == R.id.icon_sort_compra){
            ordenarPreco(getView());

            Toast.makeText(getContext(), "Icon de Ordenação Fragment", Toast.LENGTH_SHORT).show();


        }else{
            Toast.makeText(getContext(), "Icone nao mapeado aqui", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void ordenarPreco(View view){
        vetorCompras = new Produtos[listaCompras.size()];
        vetorCompras = listaCompras.toArray(vetorCompras);
        listaCompras = (List<Produtos>) utlListeCompre.ordenarListaPreco(vetorCompras, 0, listaCompras.size() - 1);
        configuraRecyclerView(listaCompras, view);
    }

    public void ordenarNome(View view){
        vetorCompras = new Produtos[listaCompras.size()];
        vetorCompras = listaCompras.toArray(vetorCompras);
        listaCompras = (List<Produtos>) utlListeCompre.ordenarListaPreco(vetorCompras, 0, listaCompras.size() - 1);
        configuraRecyclerView(listaCompras, view);
    }
}


