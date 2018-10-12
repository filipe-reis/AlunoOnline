package br.iesb.mobile.alunoonline;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import br.iesb.mobile.alunoonline.Model.Produto;
import br.iesb.mobile.alunoonline.Model.Produtos;

/**
 * Created by Filipe Reis on 29/04/2018.
 */

public class ProdutosRecyclerViewAdapter extends RecyclerView.Adapter<ProdutosRecyclerViewAdapter.ProdutoViewHolder> {

    private Context context;
    private List<Produto> listaProdutos;
    private ProdutoRecyclerClickListener produtoRecyclerClickListener;
    FirebaseDatabase database;
    String nome_lista;
    int contItem = 1;


    public ProdutosRecyclerViewAdapter(Context context, List<Produto> listaProdutos, String nome_lista) {
        this.context = context;
        this.listaProdutos = listaProdutos;
        this.nome_lista = nome_lista;
    }


    @Override
    public ProdutoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lista_produtos, parent, false);
        return new ProdutoViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ProdutoViewHolder holder, int position) {
        Produto produto = listaProdutos.get(position);

        holder.nome.setText(produto.getNome());
        holder.preco.setText(String.valueOf(produto.getPreco()));

        holder.produto = produto;
    }

    @Override
    public int getItemCount() { return listaProdutos.size(); }

    public void setRecyclerViewOnclickListener(ProdutoRecyclerClickListener r){
        produtoRecyclerClickListener = r;
    }

    public void addListItem(Produtos prod, int position){
        notifyItemInserted(position);
    }


    /**
     * Inner class View Holder
     * Maps the elements of the list
     */

    public class ProdutoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView nome, preco;
        public Produto produto;

        public ProdutoViewHolder(View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.nome_produto);
            //preco = itemView.findViewById(R.id.preco);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            params.setMargins(20, 0, 0, 0);
            itemView.setLayoutParams(params);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(produtoRecyclerClickListener != null){

                database = FirebaseDatabase.getInstance();
                DatabaseReference compra = database.getReference("/Compras");

                //Pegar e criar um no pra ele
                //Setar o nome da lista na child
                compra.child(nome_lista).child(String.valueOf(contItem)).child("Produto")  .setValue(produto.getNome ().toString());
                compra.child(nome_lista).child(String.valueOf(contItem)).child("Preco")    .setValue(String.valueOf(produto.getPreco()));
                compra.child(nome_lista).child(String.valueOf(contItem)).child("Descricao").setValue(produto.getDescricao ().toString());

                contItem++;

                //Iniciando Activity da lista de Compras
                Intent it = new Intent(v.getContext(), ListaCompras.class);
                it.putExtra("nome", nome_lista);
                context.startActivity(it);

            }
        }
    }
}
