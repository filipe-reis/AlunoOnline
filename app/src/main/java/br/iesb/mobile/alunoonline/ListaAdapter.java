package br.iesb.mobile.alunoonline;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ListaAdapter extends RecyclerView.Adapter<ListaAdapter.ListaViewHolder>{
    private Context context;
    private List<String> lista;

    public ListaAdapter(Context context, List<String> lista) {
        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public ListaAdapter.ListaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lista_listas, parent, false);
        return new ListaAdapter.ListaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaViewHolder holder, int position) {
        ListadasListas listas = new ListadasListas();
        listas.setNome(lista.get(position));
        listas.setPreco(99.90);

        holder.textNomeLista.setText(listas.getNome());
        holder.textPrecoLista.setText("Menor Preço da Lista: R$" + listas.getPreco()); //Pegar preço final da lista

        holder.listas = listas;
    }


    @Override
    public int getItemCount() {
        return lista.isEmpty() ? 0 : lista.size();
    }


    public class ListaViewHolder extends RecyclerView.ViewHolder{

        public TextView textNomeLista, textPrecoLista;
        ListadasListas listas;


        public ListaViewHolder(View itemView) {
            super(itemView);
            textNomeLista = itemView.findViewById(R.id.textNomeLista);
            textPrecoLista = itemView.findViewById(R.id.textPrecoLista);
            //itemView.setOnClickListener(this);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            params.setMargins(0, 0, 0, 0);
            itemView.setLayoutParams(params);

        }
    }
}
