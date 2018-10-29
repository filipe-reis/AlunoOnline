package br.iesb.mobile.alunoonline;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.iesb.mobile.alunoonline.Model.Produto;
import br.iesb.mobile.alunoonline.util.UtlListeCompre;

public class SplashScreen extends AppCompatActivity {


    private ProgressDialog load;
    private UtlListeCompre utl;
    private List<Produto> todosProdutos = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //Executa chamada para buscar registros da API
        GetJson consomeAPI = new GetJson();
        consomeAPI.execute();

    }


    private class GetJson extends AsyncTask<Void, Void, List<Produto>> {

        @Override
        protected void onPreExecute() {super.onPreExecute();}

        @Override
        protected List<Produto> doInBackground(Void... params) {
            utl = new UtlListeCompre();

            return utl.getInformacao("http://10.0.2.2:8080/produtos");
        }

        @Override
        protected void onPostExecute(List<Produto> produtosApi) {

            for (int i = 0; i < produtosApi.size(); i++) {
                Produto produto = new Produto();

                //Preenche objeto Produto
                produto.setProduto_id(produtosApi.get(i).getProduto_id());
                produto.setNome(produtosApi.get(i).getNome());
                produto.setDescricao(produtosApi.get(i).getDescricao());
                produto.setPreco(produtosApi.get(i).getPreco());

                //SKU
                produto.setSku(produtosApi.get(i).getSku());
                produto.setSku_id(produtosApi.get(i).getSku_id());
                produto.setSku_descricao(produtosApi.get(i).getSku_descricao());
                produto.setTamanho(produtosApi.get(i).getTamanho());
                produto.setUnidade(produtosApi.get(i).getUnidade());
                produto.setMarca(produtosApi.get(i).getMarca());
                produto.setQtd_stock(produtosApi.get(i).getQtd_stock());
                produto.setStatus(produtosApi.get(i).getStatus());

                //Categoria
                produto.setCategoria_id(produtosApi.get(i).getCategoria_id());
                produto.setCategoria_nome(produtosApi.get(i).getCategoria_nome());
                produto.setCategoria_descricao(produtosApi.get(i).getCategoria_descricao());

                //Mercado
                produto.setMercado_id(produtosApi.get(i).getMercado_id());
                produto.setMercado_nome(produtosApi.get(i).getMercado_nome());
                produto.setMercado_descricao(produtosApi.get(i).getMercado_descricao());
                produto.setLatitude(produtosApi.get(i).getLatitude());
                produto.setLongitude(produtosApi.get(i).getLongitude());


                todosProdutos.add(produto);
            }

            Log.i("Size", "Tamanho todosProdutos: " + todosProdutos.size());
            Intent it = new Intent(SplashScreen.this, MainActivity.class);
            it.putExtra("todosProdutos", (Serializable) todosProdutos);
            startActivity(it);
            todosProdutos.clear();
            finish();
        }
    }

}
