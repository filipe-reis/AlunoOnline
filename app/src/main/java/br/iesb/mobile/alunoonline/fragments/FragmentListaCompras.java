package br.iesb.mobile.alunoonline.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import br.iesb.mobile.alunoonline.Model.Produto;
import br.iesb.mobile.alunoonline.R;
import br.iesb.mobile.alunoonline.util.UtlListeCompre;

public class FragmentListaCompras extends Fragment implements View.OnClickListener{
    private static final String EXTRA_LISTA_PRODUTOS ="EXTRA_LISTA_PRODUTOS";
    private static final String EXTRA_NOME_LISTA = "EXTRA_NOME_LISTA";
    private static final String EXTRA_PRECO_LISTA = "EXTRA_PRECO_LISTA";

    private CompraAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    private FirebaseDatabase database, database1;
    String nome_lista;
    private Button btnCriaLista;
    private TextView txtPrecoTotalLista;

    public double precoTotalLista = 0.0;
    public String arredondado;
    UtlListeCompre utlListeCompre;

    public List<Produto> listaCompras = new ArrayList<>();
    Produto[] vetorCompras;

    public static FragmentListaCompras newInstance(ArrayList<Produto> produtos, String nome_lista, double preco) {

        Bundle args = new Bundle();
        args.putSerializable(EXTRA_LISTA_PRODUTOS, produtos);
        args.putString(EXTRA_NOME_LISTA, nome_lista);
        args.putString(EXTRA_PRECO_LISTA, String.valueOf(preco));
        FragmentListaCompras fragment = new FragmentListaCompras();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        listaCompras = (ArrayList<Produto>) getArguments().getSerializable(EXTRA_LISTA_PRODUTOS);
        nome_lista = getArguments().getString(EXTRA_NOME_LISTA);
        arredondado = getArguments().getString(EXTRA_PRECO_LISTA);
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

        utlListeCompre     = new UtlListeCompre();

    }

    private void configuraRecyclerView(List<Produto> listaRV, View view){
        recyclerViewAdapter = new CompraAdapter(listaRV);
        recyclerView = view.findViewById(R.id.ListaCompras);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //Divide os itens da lista
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
    }

    @Override
    public void onClick(View view) {
        //Cria lista
    }

    /**
     * Configuração RecyclerView
     */
    public class CompraAdapter extends RecyclerView.Adapter<CompraAdapter.CompraViewHolder> {

        private List<Produto> listaCompras;

        public CompraAdapter(List<Produto> listaCompras) {
            this.listaCompras = listaCompras;
        }

        @NonNull
        @Override
        public CompraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_compras, parent, false);
            return new CompraViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CompraViewHolder holder, int position) {
            Produto produtos = listaCompras.get(position);

            holder.nome.setText(produtos.getNome());
            holder.desc.setText(String.valueOf(produtos.getDescricao()));
            holder.qtd.setText(String.valueOf(produtos.getQtd()));

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

            public TextView nome, desc, qtd;
            public Produto produto;

            public CompraViewHolder(View itemView) {
                super(itemView);
                nome = itemView.findViewById(R.id.nome_produto);
                desc  = itemView.findViewById(R.id.desc);
                qtd = itemView.findViewById(R.id.qtdProdutoListaCompras);

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
       /* if(res_id == R.id.icon_more_info_compra){
            ordenarNome(getView());
            Toast.makeText(getContext(), "Ordena por Nome", Toast.LENGTH_SHORT).show();
        }else */if (res_id == R.id.icon_sort_compra){
            ordenarNome(getView());
            Toast.makeText(getContext(), "Ordena por Nome", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void ordenarPreco(View view){
        vetorCompras = new Produto[listaCompras.size()];
        vetorCompras = listaCompras.toArray(vetorCompras);
        listaCompras = (List<Produto>) utlListeCompre.ordenarListaPreco(vetorCompras, 0, listaCompras.size() - 1);
        configuraRecyclerView(listaCompras, view);
    }

    public void ordenarNome(View view){
        vetorCompras = new Produto[listaCompras.size()];
        vetorCompras = listaCompras.toArray(vetorCompras);
        listaCompras = (List<Produto>) utlListeCompre.ordenarListaNome(vetorCompras, 0, listaCompras.size() - 1);
        configuraRecyclerView(listaCompras, view);
    }

    public void setarNome(String nome){
        this.nome_lista = nome;
    }
}




