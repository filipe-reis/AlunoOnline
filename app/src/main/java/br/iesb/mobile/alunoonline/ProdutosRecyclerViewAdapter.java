package br.iesb.mobile.alunoonline;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by Filipe Reis on 29/04/2018.
 */

public class ProdutosRecyclerViewAdapter extends RecyclerView.Adapter<ProdutosRecyclerViewAdapter.ProdutoViewHolder> {

    private Context context;
    private List<Produtos> listaProdutos;
    private ProdutoRecyclerClickListener produtoRecyclerClickListener;
    FirebaseDatabase database;
    int countItem = 1;

    /**
     * Inner class View Holder
     * Maps the elements of the list
     */

    public ProdutosRecyclerViewAdapter(Context context, List<Produtos> listaProdutos) {
        this.context = context;
        this.listaProdutos = listaProdutos;
    }


    @Override
    public ProdutoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lista_produtos, parent, false);
        return new ProdutoViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ProdutoViewHolder holder, int position) {
        Produtos produtos = listaProdutos.get(position);

        holder.nome.setText(produtos.getNome());
        holder.preco.setText("R$"+ String.valueOf(produtos.getPreco()));

        holder.produto = produtos;
    }

    @Override
    public int getItemCount() { return listaProdutos.size(); }

    public void setRecyclerViewOnclickListener(ProdutoRecyclerClickListener r){
        produtoRecyclerClickListener = r;
    }

    public void addListItem(Produtos prod, int position){
        notifyItemInserted(position);
    }


    public class ProdutoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView nome, preco;
        public Produtos produto;

        public ProdutoViewHolder(View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.nome_produto);
            preco = itemView.findViewById(R.id.preco_produto);
            itemView.setOnClickListener(this);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            params.setMargins(0, 0, 0, 0);
            itemView.setLayoutParams(params);



            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(produtoRecyclerClickListener != null){

                database = FirebaseDatabase.getInstance();
                DatabaseReference compra = database.getReference("/Compra");

                //Pegar e criar um no pra ele
                //Setar o nome da lista na child
                compra.child(String.valueOf(countItem)).child("Produto")  .setValue(produto.getNome ().toString());
                compra.child(String.valueOf(countItem)).child("Preco")    .setValue(produto.getPreco().toString());
                compra.child(String.valueOf(countItem)).child("Marca")    .setValue(produto.getMarca().toString());
                compra.child(String.valueOf(countItem)).child("Descricao").setValue(produto.getDesc ().toString());

                countItem++;
                //Iniciando Activity da lista de Compras
                Intent it = new Intent(v.getContext(), ListaCompras.class);
                context.startActivity(it);

            }
        }
    }
}
