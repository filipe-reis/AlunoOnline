package br.iesb.mobile.alunoonline;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.iesb.mobile.alunoonline.Model.Produto;

public class DetalheMercadoAdapter extends RecyclerView.Adapter<DetalheMercadoAdapter.DetalheMercadoViewHolder>{

    private List<Produto> lista;

    public DetalheMercadoAdapter(List<Produto> listaCompras) {
        this.lista = listaCompras;
    }

    @NonNull
    @Override
    public DetalheMercadoAdapter.DetalheMercadoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detalhe_mercado, parent, false);
        return new DetalheMercadoAdapter.DetalheMercadoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetalheMercadoViewHolder holder, int position) {
        Produto produto = lista.get(position);

        holder.nome.setText(produto.getNome());
        holder.desc.setText(produto.getDescricao());
//        holder.qtdProduto.setText(produto.getQtd());
        holder.preco.setText(String.valueOf(produto.getPreco()).replace(".", ","));

        holder.produtos = produto;
    }

    @Override
    public int getItemCount() {
        return lista.isEmpty() ? 0 : lista.size();
    }


    public class DetalheMercadoViewHolder extends RecyclerView.ViewHolder{

        public TextView nome, desc, qtdProduto, preco;
        Produto produtos;

        public DetalheMercadoViewHolder(View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.nome);
            desc = itemView.findViewById(R.id.descricao);
//            qtdProduto = itemView.findViewById(R.id.qtd);
            preco = itemView.findViewById(R.id.precoProduto);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            params.setMargins(0, 0, 0, 0);
            itemView.setLayoutParams(params);


        }
    }
}
