package br.iesb.mobile.alunoonline.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.iesb.mobile.alunoonline.Model.Lista;
import br.iesb.mobile.alunoonline.Model.MercadoAPI;
import br.iesb.mobile.alunoonline.Model.Produto;
import br.iesb.mobile.alunoonline.Model.Produtos;

public class UtlListeCompre {

    public UtlListeCompre(){};

    /**
     * Faz ordenação por preco do produto utilizando o algoritmo quicksort
     * @param prod vetor a ser ordenado
     * @param inicio posicao inicial
     * @param fim posicao final
     * @return vetor ordenado pelo preco
     */
    public Object ordenarListaPreco(Produto[] prod, int inicio, int fim) {

            if (inicio < fim) {
                int posicaoPivo = separarPorPreco(prod, inicio, fim);
                ordenarListaPreco(prod, inicio, posicaoPivo - 1);
                ordenarListaPreco(prod, posicaoPivo + 1, fim);
            }
            return Arrays.asList(prod);
    }

    private static int separarPorPreco(Produto[] vetor, int inicio, int fim) {

        Produto aux = vetor[inicio]; //Variavel utilizada na ultima parte do algoritmo para
        // setar valor final do vetor
        double pivo = vetor[inicio].getPreco();
        int i = inicio + 1, f = fim;
        while (i <= f) {
            if (vetor[i].getPreco() <= pivo)
                i++;
            else if (pivo < vetor[f].getPreco())
                f--;
            else {
                Produto troca = vetor[i];
                vetor[i] = vetor[f];
                vetor[f] = troca;
                i++;
                f--;
            }
        }
        vetor[inicio] = vetor[f];
        vetor[f] = aux;
        return f;
    }


    /**
     * Faz ordenação da lista de mercados pelo preço.
     * Algoritmo utilizado: QuickSort
     * @param mercado Array de mercados a ser ordenados
     * @param inicio posicao inical do array
     * @param fim posicao final do array
     * @return lista de mercados ordenados por preco
     */
    public Object ordenarListaDeMercadoPreco(MercadoAPI[] mercado, int inicio, int fim) {

        if (inicio < fim) {
            int posicaoPivo = separarMercadosPorPreco(mercado, inicio, fim);
            ordenarListaDeMercadoPreco(mercado, inicio, posicaoPivo - 1);
            ordenarListaDeMercadoPreco(mercado, posicaoPivo + 1, fim);
        }
        return Arrays.asList(mercado);
    }

    private static int separarMercadosPorPreco(MercadoAPI[] vetor, int inicio, int fim) {

        MercadoAPI aux = vetor[inicio]; //Variavel utilizada na ultima parte do algoritmo para
        // setar valor final do vetor
        double pivo = vetor[inicio].getPreco();
        int i = inicio + 1, f = fim;
        while (i <= f) {
            if (vetor[i].getPreco() <= pivo)
                i++;
            else if (pivo < vetor[f].getPreco())
                f--;
            else {
                MercadoAPI troca = vetor[i];
                vetor[i] = vetor[f];
                vetor[f] = troca;
                i++;
                f--;
            }
        }
        vetor[inicio] = vetor[f];
        vetor[f] = aux;
        return f;
    }



    /**
     * Faz ordenação por preco da LISTA utilizando o algoritmo quicksort
     * @param prod vetor a ser ordenado
     * @param inicio posicao inicial
     * @param fim posicao final
     * @return vetor ordenado pelo preco
     */
    public Object ordenarListaListas(Lista[] prod, int inicio, int fim) {

        if (inicio < fim) {
            int posicaoPivo = separarPorPreco(prod, inicio, fim);
            ordenarListaListas(prod, inicio, posicaoPivo - 1);
            ordenarListaListas(prod, posicaoPivo + 1, fim);
        }
        return Arrays.asList(prod);
    }

    private static int separarPorPreco(Lista[] vetor, int inicio, int fim) {

        Lista aux = vetor[inicio]; //Variavel utilizada na ultima parte do algoritmo para
        // setar valor final do vetor
        double pivo = vetor[inicio].getPreco();
        int i = inicio + 1, f = fim;
        while (i <= f) {
            if (vetor[i].getPreco() <= pivo)
                i++;
            else if (pivo < vetor[f].getPreco())
                f--;
            else {
                Lista troca = vetor[i];
                vetor[i] = vetor[f];
                vetor[f] = troca;
                i++;
                f--;
            }
        }
        vetor[inicio] = vetor[f];
        vetor[f] = aux;
        return f;
    }


    /**
     * Faz ordenação por preco do produto utilizando o algoritmo quicksort
     * @param prod vetor a ser ordenado
     * @param inicio posicao inicial
     * @param fim posicao final
     * @return vetor ordenado pelo preco
     */
    public Object ordenarListaNome(Produto[] prod, int inicio, int fim) {

        if (inicio < fim) {
            int posicaoPivo = separarPorNome(prod, inicio, fim);
            ordenarListaNome(prod, inicio, posicaoPivo - 1);
            ordenarListaNome(prod, posicaoPivo + 1, fim);
        }
        return Arrays.asList(prod);
    }

    private static int separarPorNome(Produto[] vetor, int inicio, int fim) {

        Produto aux = vetor[inicio]; //Variavel utilizada na ultima parte do algoritmo para
        // setar valor final do vetor
        String pivo = vetor[inicio].getNome();
        int i = inicio + 1, f = fim;
        while (i <= f) {
            if (vetor[i].getNome().compareTo(pivo) <= 0 )
                i++;
            else if (vetor[f].getNome().compareTo(pivo) > 0 )
                f--;
            else {
                Produto troca = vetor[i];
                vetor[i] = vetor[f];
                vetor[f] = troca;
                i++;
                f--;
            }
        }
        vetor[inicio] = vetor[f];
        vetor[f] = aux;
        return f;
    }

    /**
     * Verifica os produtos da lista de compras estão disponiveis no mercado, retorna a lista com os produtos do mercado
     * @param listaCompras lista das compras criada pelo usuario
     * @param produtosMercado lista de produtos do mercado
     * @return lista conciliada com os produtos disponiveis
     */
    public List<Produtos> conciliarListadeComprascomMercado(List<Produtos> listaCompras, List<Produtos> produtosMercado){
        int i, j;
        List<Produtos> listaConciliada = new ArrayList<>();

        for (i = 0; i < listaCompras.size(); i++){
            for(j = 0; j < produtosMercado.size(); j++){
                if (listaCompras.get(i).getNome() == produtosMercado.get(j).getNome()){
                    //Enontrou produto no mercado
                    listaConciliada.add(produtosMercado.get(j));
                }else{
                    //Produto não encontrado no mercado
                }
            }
        }
        return listaConciliada;
    }



    /////////////////////////////////////////////////////
    // Métodos para recuperação das informações da API //
    /////////////////////////////////////////////////////

    public List<Produto> getInformacao(String end){
        String json;
        List<Produto> retorno;
        json = UtlListeCompre.getJSONFromAPI(end);
        Log.i("Resultado", json);
        retorno = parseJson(json);

        return retorno;
    }

    private List<Produto> parseJson(String json){
        try {
            List<Produto> produtos = new ArrayList<>();
            JSONObject jsonObject;

            //Atribui a string que contem o conteudo do json para um JSONArray
            JSONArray jsonArray = new JSONArray(json);

            for(int i = 0; i < jsonArray.length(); i++) {
                Produto produto = new Produto();

                //Para cada elemento do array, cria-se um JSONObject
                jsonObject = jsonArray.getJSONObject(i);

                //Atribui os objetos que estão nas camadas mais altas
                produto.setProduto_id    (jsonObject.getString("produto_id"     ));
                produto.setCodigo_barras (jsonObject.getString("codigo_barras"  ));
                produto.setNome          (jsonObject.getString("nome"           ));
                produto.setDescricao     (jsonObject.getString("descricao"      ));
                produto.setPreco         (jsonObject.getDouble("preco"          ));

                //Sku é um objeto, instancia um novo JSONObject
                JSONObject sku = jsonObject.getJSONObject("sku");
                produto.setSku           (sku.getString("sku"       ));
                produto.setSku_descricao (sku.getString("descricao" ));
                produto.setSku_id        (sku.getString("sku_id"    ));

                //Categoria tambem é um Objeto
                JSONObject categoria = jsonObject.getJSONObject("categoria");
                produto.setCategoria_id        (categoria.getString("categoria_id" ));
                produto.setCategoria_nome      (categoria.getString("nome"         ));
                produto.setCategoria_descricao (categoria.getString("descricao"    ));

                //Mercado tambem é um Objeto
                JSONObject mercado = jsonObject.getJSONObject("mercado");
                produto.setMercado_id        (mercado.getString("mercado_id" ));
                produto.setMercado_nome      (mercado.getString("nome"       ));
                produto.setMercado_descricao (mercado.getString("descricao"  ));
                produto.setCnpj              (mercado.getString("cnpj"       ));
                produto.setLatitude          (mercado.getDouble("latitude"   ));
                produto.setLongitude         (mercado.getDouble("longitude"  ));

                //Adicionando na lista
                produtos.add(produto);
            }

            return produtos;
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Faz conexão com a API
     * Executa metodo GET no recurso passado na url e carrega o JSON
     * @param url endereco que será feita a requisição
     * @return
     */
    public static String getJSONFromAPI(String url){
        String retorno = "";
        try {
            URL apiEnd = new URL(url);
            int codigoResposta;
            HttpURLConnection conexao;
            InputStream is;

            //Abre conexao utilizando protocolo HTTP e metodo GET
            conexao = (HttpURLConnection) apiEnd.openConnection();
            conexao.setRequestMethod("GET");

            //Timeouts para conexao
            conexao.setReadTimeout(15000);
            conexao.setConnectTimeout(15000);
            conexao.connect();

            //Toda resquisição que não da erro retorna abaixo de 400 == HTTP_BAD_REQUEST
            codigoResposta = conexao.getResponseCode();
            if(codigoResposta < HttpURLConnection.HTTP_BAD_REQUEST){
                is = conexao.getInputStream();
            }else{
                is = conexao.getErrorStream();
            }

            retorno = converterInputStreamToString(is);
            is.close();
            conexao.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        return retorno;
    }

    /**
     * Converte o InputStream recebido para string que conterá o JSON completo
     * @param is
     * @return
     */
    private static String converterInputStreamToString(InputStream is){
        StringBuffer buffer = new StringBuffer();
        try{
            BufferedReader br;
            String linha;

            br = new BufferedReader(new InputStreamReader(is));
            while((linha = br.readLine())!=null){
                buffer.append(linha);
            }

            br.close();
        }catch(IOException e){
            e.printStackTrace();
        }

        Log.w("JSON", buffer.toString());

        return buffer.toString();
    }
}
