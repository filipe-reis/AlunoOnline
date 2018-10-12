package br.iesb.mobile.alunoonline.fragments;

import android.app.Activity;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.iesb.mobile.alunoonline.Model.Mercado;
import br.iesb.mobile.alunoonline.Model.Produtos;
import br.iesb.mobile.alunoonline.R;

public class FragmentListaMercados extends Fragment {
    private static final String EXTRA_LISTA_PRODUTOS = "EXTRA_LISTA_PRODUTOS";
    private static final String EXTRA_NOME_LISTA = "EXTRA_NOME_LISTA";
    private static final String  EXTRA_PRECO_LISTA = "EXTRA_PRECO_LISTA";

    private MercadoAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;

    public List<Mercado> listaMercados = new ArrayList<>();
    public List<Produtos> listaProdutosMercado = new ArrayList<>();
    public List<Produtos> listaTodosProdutos = new ArrayList<>();

    private String nomeLista, preco;
    private View view;

    public static FragmentListaMercados newInstance(ArrayList<Produtos> produtos, String nome_lista, double preco) {

        Bundle args = new Bundle();
        args.putSerializable(EXTRA_LISTA_PRODUTOS, produtos);
        args.putString(EXTRA_NOME_LISTA, nome_lista);
        args.putString(EXTRA_PRECO_LISTA, String.valueOf(preco));
        FragmentListaMercados fragment = new FragmentListaMercados();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listaTodosProdutos = (ArrayList<Produtos>) getArguments().getSerializable(EXTRA_LISTA_PRODUTOS);
        nomeLista = getArguments().getString(EXTRA_NOME_LISTA);
        preco = getArguments().getString(EXTRA_PRECO_LISTA);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_mercados, container, false);

        getListaMercados();
        return view;
    }

    public void getListaMercados() {

        DatabaseReference listaMercadoRef = null;
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            listaMercadoRef = database.getReference().child("/Mercados/");
        } catch (Exception e) {
            System.out.print("ERRO - " + e.getMessage());
        }

        listaMercadoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaMercados.clear();
                for (DataSnapshot c : dataSnapshot.getChildren()) {
                    Mercado mercado = new Mercado();
                    try {
                        mercado.setNome(c.child("Sobre").child("nome").getValue().toString());
                    } catch (Exception e) {
                        System.out.println("ERRO Recuperando produto: " + e.getMessage());
                        e.printStackTrace();
                    }

                    listaMercados.add(mercado);
                }

                for(int i = 0; i < listaMercados.size(); i++){
                    double precoMercadoTotal = 0, precoMercado = 0;
                    int count = 1;

                    while(count <= 15){
                        String path = i+1 + "/" + count + "/preco";
                        precoMercado = (Double.parseDouble(dataSnapshot.child(path).getValue().toString()));
                        precoMercadoTotal = precoMercadoTotal + precoMercado;
                        count++;
                    }

                    double precoMercadoTotalArredondado = precoMercadoTotal;
                    precoMercadoTotalArredondado *= (Math.pow(10, 2)); //Multiplica por 100

                    precoMercadoTotalArredondado = Math.ceil(precoMercadoTotalArredondado); //Arredonda sempre pra cima

                    precoMercadoTotalArredondado/= (Math.pow(10, 2)); // Divide por 100 revertendo a primeira operação

                    listaMercados.get(i).setPreco(precoMercadoTotalArredondado);
                }

                recyclerViewAdapter = new MercadoAdapter(getListMercadoView());
                recyclerView = view.findViewById(R.id.ListaMercados);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(recyclerViewAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Não foi possível recuperar os dados do banco de dados", Toast.LENGTH_LONG).show();
                Log.e("Recuperar Lista Mercado", databaseError.getMessage());

            }
        });
    }

    private ListaMercadoView getListMercadoView() {
        ListaMercadoView listaMercadoView = new ListaMercadoView();
        listaMercadoView.mercados = listaMercados;
       // listaMercadoView.total = "90,00";
        return listaMercadoView;
    }

    /**
     * Configuração RecyclerView
     */
    public class MercadoAdapter extends RecyclerView.Adapter<MercadoAdapter.MercadoViewHolder> {

        private ListaMercadoView listaMercadoView;

        public MercadoAdapter(ListaMercadoView listaMercadoView) {
            this.listaMercadoView = listaMercadoView;
        }

        @NonNull
        @Override
        public MercadoAdapter.MercadoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mercado, parent, false);
            return new MercadoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MercadoAdapter.MercadoViewHolder holder, int position) {
            Mercado mercado = listaMercadoView.mercados.get(position);

            holder.nomeMercado.setText(mercado.getNome());
            holder.precoMercado.setText(String.valueOf(mercado.getPreco()));

            holder.mercado = mercado;
        }

        @Override
        public int getItemCount() {
            return listaMercados.isEmpty() ? 0 : listaMercados.size();
        }


        /**
         * Mercado View Holder
         */
        public class MercadoViewHolder extends RecyclerView.ViewHolder {

            public TextView nomeMercado, precoMercado, distanciaMercado;
            public Mercado mercado;

            public MercadoViewHolder(View itemView) {
                super(itemView);
                nomeMercado = itemView.findViewById(R.id.txtMercado);
                precoMercado = itemView.findViewById(R.id.txtPreco);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) itemView.getLayoutParams();
                params.setMargins(20, 0, 0, 0);
                itemView.setLayoutParams(params);

            }
        }
    }

    //O que a recycler view vai exibir
    public class ListaMercadoView{
        List<Mercado> mercados;
        String total;
    }


}


