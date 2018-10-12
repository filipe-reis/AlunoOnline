package br.iesb.mobile.alunoonline;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import br.iesb.mobile.alunoonline.Model.Lista;
import br.iesb.mobile.alunoonline.Model.ListadasListas;

public class ListaAdapter extends RecyclerView.Adapter<ListaAdapter.ListaViewHolder>{
    private Context context;
    private List<Lista> lista;
    private ProdutoRecyclerClickListener listaRecyclerClickListener;
    private ListaCompras listaCompras = new ListaCompras();


    public ListaAdapter(Context context, List<Lista> lista) {
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
        Lista listas = lista.get(position);

        holder.textNomeLista.setText(listas.getNome());
        holder.textPrecoLista.setText(""); //Pegar preço final da lista

        holder.listas = listas;
    }

    @Override
    public int getItemCount() {
        return lista.isEmpty() ? 0 : lista.size();
    }

    public void setRecyclerViewOnclickListener(ProdutoRecyclerClickListener r){
        listaRecyclerClickListener = r;
    }

    public class ListaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView textNomeLista, textPrecoLista;
        Lista listas;

        public ListaViewHolder(View itemView) {
            super(itemView);
            textNomeLista = itemView.findViewById(R.id.textNomeLista);
            textPrecoLista = itemView.findViewById(R.id.textPrecoLista);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            params.setMargins(0, 0, 0, 0);
            itemView.setLayoutParams(params);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(listaRecyclerClickListener != null){

                Intent it = new Intent(view.getContext(), ListaCompras.class);
                it.putExtra("nome", textNomeLista.getText());
                context.startActivity(it);

            }else{
                Toast.makeText(view.getContext(), "Erro ao buscar Lista", Toast.LENGTH_SHORT).show();

            }
        }
    }
}
