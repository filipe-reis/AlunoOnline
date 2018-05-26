package br.iesb.mobile.alunoonline;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CompraAdapter extends RecyclerView.Adapter<CompraAdapter.CompraViewHolder> {

    private Context context;
    private List<Produtos> listaCompras;

    public CompraAdapter(Context context, List<Produtos> listaCompras) {
        this.context = context;
        this.listaCompras = listaCompras;
    }

    @NonNull
    @Override
    public CompraAdapter.CompraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lista_produtos, parent, false);
        return new CompraAdapter.CompraViewHolder(view);
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
