package br.iesb.mobile.alunoonline;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import br.iesb.mobile.alunoonline.Model.Produto;

/**
 * Created by Filipe Reis on 29/04/2018.
 */

public class ProdutosRecyclerViewAdapter extends RecyclerView.Adapter<ProdutosRecyclerViewAdapter.ProdutoViewHolder> {


    private Context context;
    private List<Produto> listaProdutos;
    private List<Produto> todosProdutos;
    private ProdutoRecyclerClickListener produtoRecyclerClickListener;
    int countItem = 1;

    String nome_lista;
    FirebaseAuth firebaseAuth;
    String userId;
    List<Produto> auxListaCompras = new ArrayList<>();
    Produto produto;



    public ProdutosRecyclerViewAdapter(Context context, List<Produto> listaProdutos, String nome_lista, List<Produto> todosProdutos) {
        this.context = context;
        this.listaProdutos = listaProdutos;
        this.nome_lista = nome_lista;
        this.userId = this.firebaseAuth.getInstance().getUid();
        this.todosProdutos = todosProdutos;
    }


    @Override
    public ProdutoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lista_produtos, parent, false);


        return new ProdutoViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ProdutoViewHolder holder, int position) {
        produto = listaProdutos.get(position);

        holder.nome.setText(produto.getNome());
        holder.desc.setText(String.valueOf(produto.getDescricao()));

        holder.qtdProduto.setText(String.valueOf(produto.getQtd()));

        holder.produto = produto;
    }

    @Override
    public int getItemCount() { return listaProdutos.size(); }

    public void setRecyclerViewOnclickListener(ProdutoRecyclerClickListener r){
        produtoRecyclerClickListener = r;
    }


    /**
     * Inner class View Holder
     * Maps the elements of the list
     */

    public class ProdutoViewHolder extends RecyclerView.ViewHolder{

        public TextView nome, desc, qtdProduto;
        ImageView adicionaProduto, excluiProduto;
        public Produto produto;

        public ProdutoViewHolder(final View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.nome_produto);
            desc = itemView.findViewById(R.id.desc);
            qtdProduto = itemView.findViewById(R.id.qtdProduto);
            adicionaProduto = itemView.findViewById(R.id.adicionaElemento);
            excluiProduto   = itemView.findViewById(R.id.excluiProduto);

            adicionaProduto.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    v.startAnimation(AnimationUtils.loadAnimation(itemView.getContext(), R.anim.image_click));
                    adicionaProduto(v);
                    notifyDataSetChanged();
                }
            });

            excluiProduto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.startAnimation(AnimationUtils.loadAnimation(itemView.getContext(), R.anim.image_click));
                    excluiProduto(v);
                    notifyDataSetChanged();
                }
            });

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            params.setMargins(20, 0, 0, 0);
            itemView.setLayoutParams(params);

//            itemView.setOnClickListener(this);
        }

        public void adicionaProduto(View v){

            boolean temIgual = false;

            if(produtoRecyclerClickListener != null){

                if(!auxListaCompras.isEmpty()){

                     for (int i = 0; i < auxListaCompras.size(); i++) {
                         if(produto.getNome().equals(auxListaCompras.get(i).getNome())){
                             temIgual = true;
                             auxListaCompras.get(i).setQtd(auxListaCompras.get(i).getQtd() + 1);
                             produto.setQtd(auxListaCompras.get(i).getQtd());
                         }
                     }
                }

                if (!temIgual){
                    produto.setQtd(1);
                    auxListaCompras.add(produto);
                }

            }
        }

        public void excluiProduto(View v){

            for(int i = 0; i < auxListaCompras.size(); i++){
                if (auxListaCompras.get(i).equals(produto)){
                    auxListaCompras.get(i).setQtd(auxListaCompras.get(i).getQtd() - 1);
                    produto.setQtd(auxListaCompras.get(i).getQtd());

                    if(auxListaCompras.get(i).getQtd() == 0){
                        auxListaCompras.remove(i);
                    }
                }
            }
        }
    }
}
