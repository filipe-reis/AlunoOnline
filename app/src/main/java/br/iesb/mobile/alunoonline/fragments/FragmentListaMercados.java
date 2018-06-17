package br.iesb.mobile.alunoonline.fragments;

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
    private MercadoAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;

    private List<Mercado> listaMercados = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_mercados, container, false);

        recyclerViewAdapter = new MercadoAdapter(listaMercados);
        recyclerView = view.findViewById(R.id.ListaMercados);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerViewAdapter);

        getListaMercados();
        return view;
    }

    public void getListaMercados(){

        DatabaseReference listaMercadoRef = null;
        try{
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            listaMercadoRef = database.getReference().child("/Mercados");
        }catch(Exception e){
            System.out.print("ERRO - " + e.getMessage());
        }

        listaMercadoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot c : dataSnapshot.getChildren()){
                    Mercado mercado = new Mercado();
                    try{
                        mercado.setNome( c.child("Sobre").child("Nome")  .getValue().toString());
                        mercado.setDistancia(new BigDecimal(c.child("Sobre").child("Distancia").getValue().toString().replace(',', '.')) );
                        mercado.setPreco(new BigDecimal(c.child("Sobre").child("Preco").getValue().toString().replace(',', '.')));

                    }catch(Exception e){
                        System.out.println("ERRO Recuperando produto: " + e.getMessage());
                        e.printStackTrace();
                    }

                    listaMercados.add(mercado);
                }
                recyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Não foi possível recuperar os dados do banco de dados", Toast.LENGTH_LONG).show();
                Log.e("Recuperar Lista Mercado", databaseError.getMessage());

            }
        });
    }

    /**
     * Configuração RecyclerView
     */
    public class MercadoAdapter extends RecyclerView.Adapter<MercadoAdapter.MercadoViewHolder>{

        private List<Mercado> listaMercados;

        public MercadoAdapter(List<Mercado> listaMercados){
            this.listaMercados = listaMercados;
        }

        @NonNull
        @Override
        public MercadoAdapter.MercadoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mercado, parent, false);
            return new MercadoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MercadoAdapter.MercadoViewHolder holder, int position) {
            Mercado mercado = listaMercados.get(position);

            holder.nomeMercado.setText(mercado.getNome());
            holder.precoMercado.setText(String.valueOf(mercado.getPreco()));
            holder.distanciaMercado.setText(String.valueOf(mercado.getDistancia()));

            holder.mercado = mercado;
        }

        @Override
        public int getItemCount() {
            return listaMercados.isEmpty() ? 0 : listaMercados.size();
        }


        /**
         * Mercado View Holder
         */
        public class MercadoViewHolder extends RecyclerView.ViewHolder{

            public TextView nomeMercado, precoMercado, distanciaMercado;
            public Mercado mercado;

            public MercadoViewHolder(View itemView) {
                super(itemView);
                nomeMercado = itemView.findViewById(R.id.txtMercado);
                precoMercado = itemView.findViewById(R.id.txtPreco);
                distanciaMercado =  itemView.findViewById(R.id.txtDistancia);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) itemView.getLayoutParams();
                params.setMargins(20, 0, 0, 0);
                itemView.setLayoutParams(params);

            }
        }
    }


}


