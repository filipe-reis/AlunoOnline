package br.iesb.mobile.alunoonline.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.iesb.mobile.alunoonline.DetalheMercado;
import br.iesb.mobile.alunoonline.MapActivity;
import br.iesb.mobile.alunoonline.Model.MercadoAPI;
import br.iesb.mobile.alunoonline.Model.Produto;
import br.iesb.mobile.alunoonline.ProdutoRecyclerClickListener;
import br.iesb.mobile.alunoonline.R;
import br.iesb.mobile.alunoonline.util.UtlListeCompre;

public class FragmentListaMercados extends Fragment implements ProdutoRecyclerClickListener{
    private static final String EXTRA_LISTA_PRODUTOS = "EXTRA_LISTA_PRODUTOS";
    private static final String EXTRA_NOME_LISTA = "EXTRA_NOME_LISTA";
    private static final String EXTRA_PRECO_LISTA = "EXTRA_PRECO_LISTA";
    private static final String EXTRA_TODOS_PRODUTOS = "EXTRA_TODOS_PRODUTOS";

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference comprasRef;
    private FirebaseAuth firebaseAuth;
    String userId;


    private static int POSICAO_MERCADO_A;
    private static int POSICAO_MERCADO_B;
    private static int POSICAO_MERCADO_C;

    private MercadoAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;

    public List<MercadoAPI> listaMercados = new ArrayList<>();   // Lista contendo os mercados

    public List<Produto> todosProdutos = new ArrayList<>();     // Todos os produtos retornados pela API

    public List<Produto> produtosMercadoA = new ArrayList<>();  // Lista para o mercado A com todos os produtos desse mercado
    public List<Produto> produtosMercadoB = new ArrayList<>();  // Lista para o mercado B com todos os produtos desse mercado
    public List<Produto> produtosMercadoC = new ArrayList<>();  // Lista para o mercado C com todos os produtos desse mercado

    public List<Produto> listaCompras = new ArrayList<>();      // lista montada pelo usuario

    public List<Produto> listaComprasMercadoA = new ArrayList<>();  // Lista de compra do usuário no mercado A
    public List<Produto> listaComprasMercadoB = new ArrayList<>();  // Lista de compra do usuário no mercado B
    public List<Produto> listaComprasMercadoC = new ArrayList<>();  // Lista de compra do usuário no mercado C

    private double precoMercadoA = 0.0;
    private double precoMercadoB = 0.0;
    private double precoMercadoC = 0.0;


    private String nomeLista, preco;
    private Button localizacaoBtn;
    private View view;

    public static FragmentListaMercados newInstance(ArrayList<Produto> produtos, String nome_lista, double preco, List<Produto> todosProdutos) {

        Bundle args = new Bundle();
        args.putSerializable(EXTRA_LISTA_PRODUTOS, produtos);
        args.putString(EXTRA_NOME_LISTA, nome_lista);
        args.putString(EXTRA_PRECO_LISTA, String.valueOf(preco));
        args.putSerializable(EXTRA_TODOS_PRODUTOS, (Serializable) todosProdutos);
        FragmentListaMercados fragment = new FragmentListaMercados();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listaCompras = (ArrayList<Produto>) getArguments().getSerializable(EXTRA_LISTA_PRODUTOS);
        nomeLista = getArguments().getString(EXTRA_NOME_LISTA);
        preco = getArguments().getString(EXTRA_PRECO_LISTA);
        todosProdutos = (ArrayList<Produto>) getArguments().getSerializable(EXTRA_TODOS_PRODUTOS);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_mercados, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        comprasRef = database.getReference().child("/" + userId);

        localizacaoBtn = view.findViewById(R.id.localizacaoBtn);

        localizacaoBtn.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getContext(), MapActivity.class);
                it.putExtra("listaMercados", (Serializable) listaMercados);
                startActivity(it);
            }
        });

        // Recupera lista de mercados e monta uma lista com todos os produtos em cada mercado
        getListaMercados();

        // Monta lista de compras do usuario em cada mercado
        montaListaEmCadaMercado();

        // Ordena a lista de mercados por ordem de menor preco
        ordenaMercadosPorPreco();

        // Configura RecyclerView para exibir a lista de mercados ordenada
        configuraRecyclerView();

        comprasRef.child(nomeLista).child("Preço")
                .setValue(listaMercados.get(0).getPreco());

        return view;
    }


    /**
     * Define a lista de todos os produto de cada mercado com base no
     * campo Mercado_Id da lista que contém todos os produtos vindos da API
     * Mercado_Id == 1 -> Mercado A
     * Mercado_Id == 2 -> Mercado B
     * Mercado_Id == 3 -> Mercado C
     */
    public void getListaMercados() {

        String anterior = "0";
        MercadoAPI mercado;

        for (int i = 0; i < todosProdutos.size(); i++) {
            //Crio novo mercado e seto na lista apenas quando mercado_id muda
            if (!anterior.equals(todosProdutos.get(i).getMercado_id())) {

                mercado = new MercadoAPI();
                mercado.setCnpj(todosProdutos.get(i).getCnpj());
                mercado.setDescricao(todosProdutos.get(i).getMercado_descricao());
                mercado.setNome(todosProdutos.get(i).getMercado_nome());
                mercado.setMercado_id(Long.valueOf(todosProdutos.get(i).getMercado_id()));
                mercado.setLatitude(todosProdutos.get(i).getLatitude());
                mercado.setLongitude(todosProdutos.get(i).getLongitude());

                listaMercados.add(mercado);
            }


            switch (todosProdutos.get(i).getMercado_id()) {
                case "1":
                    produtosMercadoA.add(todosProdutos.get(i));
                    POSICAO_MERCADO_A = listaMercados.size() - 1;
                    break;
                case "2":
                    produtosMercadoB.add(todosProdutos.get(i));
                    POSICAO_MERCADO_B = listaMercados.size() - 1;
                    break;
                case "3":
                    produtosMercadoC.add(todosProdutos.get(i));
                    POSICAO_MERCADO_C = listaMercados.size() - 1;
                    break;
            }

            anterior = todosProdutos.get(i).getMercado_id();
        }
    }

    /**
     * Para cada produto da lista de compras percorre a lista dos mercados
     * afim de identificar o produto em cada mercado e montar a lista de
     * compras em cada mercado.
     *
     * Produto é identificado nos mercados pelo SKU
     */
    private void montaListaEmCadaMercado(){

        for (Produto produtoEscolhidoLista  : listaCompras){

            for(Produto produtoMercadoA : produtosMercadoA){

                if(produtoEscolhidoLista.getSku().equals(produtoMercadoA.getSku())){
                    listaComprasMercadoA.add(produtoMercadoA);
                    precoMercadoA = precoMercadoA +  produtoMercadoA.getPreco() * produtoEscolhidoLista.getQtd();

                    listaMercados.get(POSICAO_MERCADO_A).setPreco(arredondaPreco(precoMercadoA));
                }
            }

            for(Produto produtoMercadoB : produtosMercadoB){

                if(produtoEscolhidoLista.getSku().equals(produtoMercadoB.getSku())){
                    listaComprasMercadoB.add(produtoMercadoB);
                    precoMercadoB = precoMercadoB + produtoMercadoB.getPreco() * produtoEscolhidoLista.getQtd();

                    listaMercados.get(POSICAO_MERCADO_B).setPreco(arredondaPreco(precoMercadoB));
                }
            }

            for(Produto produtoMercadoC : produtosMercadoC){

                if(produtoEscolhidoLista.getSku().equals(produtoMercadoC.getSku())){
                    listaComprasMercadoC.add(produtoMercadoC);
                    precoMercadoC = precoMercadoC + produtoMercadoC.getPreco() * produtoEscolhidoLista.getQtd();

                    listaMercados.get(POSICAO_MERCADO_C).setPreco(arredondaPreco(precoMercadoC));
                }
            }
        }
    }

    public double arredondaPreco(double precoTotalMercado) {

        precoTotalMercado *= (Math.pow(10, 2)); //Multiplica por 100

        precoTotalMercado = Math.ceil(precoTotalMercado); //Arredonda sempre pra cima

        precoTotalMercado /= (Math.pow(10, 2)); // Divide por 100 revertendo a primeira operação

        return precoTotalMercado;
    }


    /**
     *  Com as listas de cada mercado preenchidas, utiliza o
     *  quicksort para ordenar a listagem de mercados por preço.
     *
     *  Para implementação da ordenação @see UtlListeCompre
     */
    private void ordenaMercadosPorPreco(){
        UtlListeCompre utlListeCompre = new UtlListeCompre();
        MercadoAPI[] vetorMercados = new MercadoAPI[listaMercados.size()];
        vetorMercados = listaMercados.toArray(vetorMercados);
        listaMercados =(List<MercadoAPI>) utlListeCompre.ordenarListaDeMercadoPreco(vetorMercados,0, listaMercados.size() - 1);
    }

    private void configuraRecyclerView(){

        recyclerViewAdapter = new MercadoAdapter(getListMercadoView(), listaComprasMercadoA, listaComprasMercadoB, listaComprasMercadoC);
        recyclerView = view.findViewById(R.id.ListaMercados);
        recyclerViewAdapter.setRecyclerViewOnclickListener(this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerViewAdapter);
    }


    private List<MercadoAPI> getListMercadoView() {
        return listaMercados;
    }

    @Override
    public void onClickListener(View v, int position) {

    }


    /**
     * Configuração RecyclerView
     */
    public class MercadoAdapter extends RecyclerView.Adapter<MercadoAdapter.MercadoViewHolder> {

        private List<MercadoAPI> listaMercadoView;
        private ProdutoRecyclerClickListener listaRecyclerClickListener;
        List<Produto> listaMercadoA;
        List<Produto> listaMercadoB;
        List<Produto> listaMercadoC;



        public MercadoAdapter(List<MercadoAPI> listaMercadoView, List<Produto> listaMercadoA, List<Produto> listaMercadoB, List<Produto> listaMercadoC) {
            this.listaMercadoView = listaMercadoView;
            this.listaMercadoA = listaMercadoA;
            this.listaMercadoB = listaMercadoB;
            this.listaMercadoC = listaMercadoC;
        }

        @NonNull
        @Override
        public MercadoAdapter.MercadoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mercado, parent, false);
            return new MercadoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MercadoAdapter.MercadoViewHolder holder, int position) {
            MercadoAPI mercado = listaMercadoView.get(position);

            holder.nomeMercado.setText(mercado.getNome());
            holder.precoMercado.setText(String.valueOf(mercado.getPreco()));

            holder.mercado = mercado;
        }

        @Override
        public int getItemCount() {
            return listaMercados.isEmpty() ? 0 : listaMercados.size();
        }

        public void setRecyclerViewOnclickListener(ProdutoRecyclerClickListener r){
            listaRecyclerClickListener = r;
        }


        /**
         * Mercado View Holder
         */
        public class MercadoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            public TextView nomeMercado, precoMercado;
            public MercadoAPI mercado;

            public MercadoViewHolder(View itemView) {
                super(itemView);
                nomeMercado = itemView.findViewById(R.id.txtMercado);
                precoMercado = itemView.findViewById(R.id.txtPreco);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) itemView.getLayoutParams();
                params.setMargins(20, 0, 0, 0);
                itemView.setLayoutParams(params);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                if(listaRecyclerClickListener != null){
                    Intent it = new Intent(v.getContext(), DetalheMercado.class);

                    Log.i("TESTE", "NOME MERCADO >>> " + nomeMercado.getText());

                    if(nomeMercado.getText().equals("Mercado A")){

                        it.putExtra("listaCompras", (Serializable) listaMercadoA);
                    }else if(nomeMercado.getText().equals("Mercado B")){

                        it.putExtra("listaCompras", (Serializable) listaMercadoB);
                    }else if(nomeMercado.getText().equals("Mercado C")){

                        it.putExtra("listaCompras", (Serializable) listaMercadoC);
                    }

                    startActivity(it);
                }
            }
        }
    }
}


