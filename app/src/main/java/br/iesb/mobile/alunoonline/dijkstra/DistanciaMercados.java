package br.iesb.mobile.alunoonline.dijkstra;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.iesb.mobile.alunoonline.Model.MercadoAPI;

public class DistanciaMercados {

    public DistanciaMercados() {}

    public List<String> calculaMercadoMaisProximoDaLocalizacaoAtual(List<MercadoAPI> listaMercados, LatLng localizacaoAtual) {

        double latitude = -15.649996;
        double longitude = -47.7982585;

        List<Vertice> vizinhos = new ArrayList<Vertice>();
        List<Vertice> listaVertices = new ArrayList<Vertice>();
        List<Vertice> listaVerticesMenorCaminho = new ArrayList<Vertice>();
        Map<String, Double> mercadoMaisPertoDoAtual = new HashMap<String, Double>();
        List<String> menorCaminho = new ArrayList<>();

        List<Aresta> arestasAtual = new ArrayList<Aresta>();
        List<Aresta> arestasM1 = new ArrayList<Aresta>();
        List<Aresta> arestasM2 = new ArrayList<Aresta>();
        List<Aresta> arestasM3 = new ArrayList<Aresta>();

        double dist;

        Vertice verticeAtual = new Vertice();
        Vertice v1 = new Vertice();
        Vertice v2 = new Vertice();
        Vertice v3 = new Vertice();

        MercadoAPI mercadoMaisBarato = new MercadoAPI();
        mercadoMaisBarato = listaMercados.get(0);

        MercadoAPI mercado2 = new MercadoAPI();
        mercado2 = listaMercados.get(1);

        MercadoAPI mercado3 = new MercadoAPI();
        mercado3 = listaMercados.get(2);


        // Vertice da localização atual
        verticeAtual.setDescricao("Localizacao Atual");
        verticeAtual.setDistancia(0.0);
        listaVertices.add(verticeAtual);

        // Vertice do mercado mais barato
        v1.setDescricao(mercadoMaisBarato.getNome());
        dist = distancia(latitude, longitude, mercadoMaisBarato.getLatitude(), mercadoMaisBarato.getLongitude(), "K");
        listaVertices.add(v1);
        mercadoMaisPertoDoAtual.put("Atual1", dist);

        // Vertice do mercado 2
        v2.setDescricao(mercado2.getNome());
        listaVertices.add(v2);
        dist = distancia(latitude, longitude, mercado2.getLatitude(), mercado2.getLongitude(), "K");
        v2.setDistancia(dist);
        mercadoMaisPertoDoAtual.put("Atual2", dist);

        //Vertice do mercado 3
        v3.setDescricao(mercado3.getNome());
        listaVertices.add(v3);
        dist = distancia(latitude, longitude, mercado3.getLatitude(), mercado3.getLongitude(), "K");
        v3.setDistancia(dist);
        mercadoMaisPertoDoAtual.put("Atual3", dist);

        /**
         * Identificando o mercado mais proximo da localizacao atual.
         * 1 = mercado mais barato
         * 2 = mercado 2
         * 3 = mercado 3
         */
        if (mercadoMaisPertoDoAtual.get("Atual2") < mercadoMaisPertoDoAtual.get("Atual1")) {
            if (mercadoMaisPertoDoAtual.get("Atual2") < mercadoMaisPertoDoAtual.get("Atual3")) {
                arestasAtual.add(criaAresta(verticeAtual, v2, mercadoMaisPertoDoAtual.get("Atual2")));
                verticeAtual.setArestas(arestasAtual);
            } else {
                arestasAtual.add(criaAresta(verticeAtual, v3, mercadoMaisPertoDoAtual.get("Atual3")));
                verticeAtual.setArestas(arestasAtual);
            }
        } else if (mercadoMaisPertoDoAtual.get("Atual1") < mercadoMaisPertoDoAtual.get("Atual3")) {
            arestasAtual.add(criaAresta(verticeAtual, v1, mercadoMaisPertoDoAtual.get("Atual1")));
            verticeAtual.setArestas(arestasAtual);
        } else {
            arestasAtual.add(criaAresta(verticeAtual, v3, mercadoMaisPertoDoAtual.get("Atual3")));
            verticeAtual.setArestas(arestasAtual);
        }

        //Mercado 1 vai para mercado 2 e mercado 3
        arestasM1.add(criaAresta(v1, v2, distancia(mercadoMaisBarato.getLatitude(), mercadoMaisBarato.getLongitude(),
                mercado2.getLatitude(), mercado2.getLongitude(), "K")));

        arestasM1.add(criaAresta(v1, v3, distancia(mercadoMaisBarato.getLatitude(), mercadoMaisBarato.getLongitude(),
                mercado3.getLatitude(), mercado3.getLongitude(), "K")));

        v1.setArestas(arestasM1);

        //Mercado 2 vai para o mercado 1 e mercado 3
        arestasM2.add(criaAresta(v2, v1, distancia(mercado2.getLatitude(), mercado2.getLongitude(),
                mercadoMaisBarato.getLatitude(), mercadoMaisBarato.getLongitude(), "K")));

        arestasM2.add(criaAresta(v2, v3, distancia(mercado2.getLatitude(), mercado2.getLongitude(),
                mercado3.getLatitude(), mercado3.getLongitude(), "K")));

        v2.setArestas(arestasM2);

        // Mercado 3 vai para mercado 1 e mercado 2
        arestasM3.add(criaAresta(v3, v1, distancia(mercado3.getLatitude(), mercado3.getLongitude(),
                mercadoMaisBarato.getLatitude(), mercadoMaisBarato.getLongitude(), "K")));

        arestasM3.add(criaAresta(v3, v2, distancia(mercado3.getLatitude(), mercado3.getLongitude(),
                mercado2.getLatitude(), mercado2.getLongitude(),"K")));

        v3.setArestas(arestasM3);

        Grafo grafo = new Grafo();
        grafo.setVertices(listaVertices);

        Dijkstra dj = new Dijkstra();

        listaVerticesMenorCaminho = dj.encontrarMenorCaminhoDijkstra(grafo, verticeAtual, v1); // v1 é o mercado mais barato

        Log.i("GRAFO","Lista de menor caminho: " + listaVerticesMenorCaminho);

        for (int i = 0; i < listaVerticesMenorCaminho.size(); i++){
            menorCaminho.add(listaVerticesMenorCaminho.get(i).getDescricao());
        }

        return  menorCaminho;
    }

    public static Aresta criaAresta(Vertice verticeAtual, Vertice vizinho, double peso) {

        return new Aresta(verticeAtual, vizinho, peso);
    }

    private static double distancia(double lat1, double lon1, double lat2, double lon2, String unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == "K") {
            dist = dist * 1.609344;
        } else if (unit == "N") {
            dist = dist * 0.8684;
        }

        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

}
