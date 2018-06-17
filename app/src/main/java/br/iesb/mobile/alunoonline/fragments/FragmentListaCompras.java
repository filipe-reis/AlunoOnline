package br.iesb.mobile.alunoonline.fragments;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
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

public class FragmentListaCompras extends Fragment implements View.OnClickListener, PassadorDeInformacao{
    private CompraAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    private FirebaseDatabase database;
    private DatabaseReference listaCompraReference;
    String nome_lista;
    private Button btnCriaLista, btnOrdenar, btnOrdenarPreco;
    private TextView txtPrecoTotalLista;

    public double precoTotalLista = 0.0;
    UtlListeCompre utlListeCompre;

    public List<Produtos> listaCompras = new ArrayList<>();
    Produtos[] vetorCompras;

    public FragmentListaCompras(){}
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
        btnOrdenar         = view.findViewById(R.id.btnOrdenar);
        btnOrdenarPreco    = view.findViewById(R.id.btnOrdenarPreco);
        txtPrecoTotalLista = view.findViewById(R.id.txtPrecoTotaLista);



        //configuraRecyclerView(listaCompras, view);


        btnCriaLista.setOnClickListener(this);
        btnOrdenar.setOnClickListener(this);
        btnOrdenarPreco.setOnClickListener(this);
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

        txtPrecoTotalLista.setText(String.valueOf(precoTotalLista));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnCriarLista:
                //persistirListaUID();
                break;
            case R.id.btnOrdenar:
                vetorCompras = new Produtos[listaCompras.size()];
                vetorCompras = listaCompras.toArray(vetorCompras);
                listaCompras = (List<Produtos>) utlListeCompre.ordenarListaPreco(vetorCompras, 0, listaCompras.size() - 1);
                configuraRecyclerView(listaCompras, view);
                break;
            case R.id.btnOrdenarPreco:
                vetorCompras = new Produtos[listaCompras.size()];
                vetorCompras = listaCompras.toArray(vetorCompras);
                listaCompras = (List<Produtos>) utlListeCompre.ordenarListaPreco(vetorCompras, 0, listaCompras.size() - 1);
                configuraRecyclerView(listaCompras, view);
                break;
        }

    }

     @Override
    public void passaInformacao(String nome) {
        this.nome_lista = nome;
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
}


