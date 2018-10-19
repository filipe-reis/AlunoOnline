package br.iesb.mobile.alunoonline.dijkstra;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Dijkstra {

    List<Vertice> menorCaminho   = new ArrayList<Vertice>();   // Lista que guarda os vertices pertencentes ao menor caminho encontrado
    List<Vertice> naoVisitados   = new ArrayList<Vertice>();         // Lista dos vertices que ainda nao foram visitados
    Vertice       verticeCaminho = new Vertice();              // Variavel que recebe os vertices pertencentes ao menor caminho para ser adicionado a lista
    Vertice       vizinho        = new Vertice();              // Variavel que marca o vizinho do vertice atualmente visitado
    Vertice       atual          = new Vertice();              // Variavel que guarda o vertice que esta sendo visitado

    // Algoritmo de Dijkstra
    public List<Vertice> encontrarMenorCaminhoDijkstra(Grafo grafo, Vertice v1, Vertice v2) {

        menorCaminho.add(v1);   // Adiciona a origem na lista do menor caminho

        for (int i = 0; i < grafo.getVertices().size(); i++) {      // Colocando a distancias iniciais

            if (grafo.getVertices().get(i).getDescricao().equals(v1.getDescricao())) {  // Vertice atual tem distancia zero, e todos os outros, 9999999("infinita")
                grafo.getVertices().get(i).setDistancia(0);

            } else {
                grafo.getVertices().get(i).setDistancia(9999999);
            }

            this.naoVisitados.add(grafo.getVertices().get(i));      // Insere o vertice na lista de vertices nao visitados
        }

        Collections.sort(naoVisitados);     //Ordena por distancia


        while (!this.naoVisitados.isEmpty()) {   // O algoritmo continua ate que todos os vertices sejam visitados

            atual = this.naoVisitados.get(0);   // Pega o vertice com menor distancia, que e o primeito da lista ordenada

            /*
             * Verifica a distancia de cada vizinho e compara com a distancia do atual somado
             * ao peso da aresta correspondente. Se a distancia do vizinho for menor, esse vertice
             * e incluido no grafo de menor caminho para ser incluso no caminho.
             */
            for (int i = 0; i < atual.getArestas().size(); i++) {

                vizinho = atual.getArestas().get(i).getDestino();
                System.out.println("Olhando o vizinho de " + atual + ": "+ vizinho);
                if (!vizinho.verificarVisita()) {


                    if (vizinho.getDistancia() > (atual.getDistancia() + atual.getArestas().get(i).getPeso())) {       // Comparando a distância do vizinho com a possível distância

                        vizinho.setDistancia(atual.getDistancia()+ atual.getArestas().get(i).getPeso());
                        vizinho.setPai(atual);

                        /*
                         * Se o vizinho for o vertice final, foi encontrada uma distancia menor.
                         * Atualiza, portanto, a lista de menor caminho
                         */
                        if (vizinho == v2) {
                            menorCaminho.clear();
                            verticeCaminho = vizinho;
                            menorCaminho.add(vizinho);
                            while (verticeCaminho.getPai() != null) {

                                menorCaminho.add(verticeCaminho.getPai());
                                verticeCaminho = verticeCaminho.getPai();
                            }

                            Collections.sort(menorCaminho);     // Ordena a lista do menor caminho, para seja exibido na sequencia da origem ao destino
                        }
                    }
                }
            }

            // Marca o vertice atual como visitado e remove da lista
            atual.visitar();
            this.naoVisitados.remove(atual);

            // Ordena para que o vertice com menor distancia esteja na primeira posicao
            Collections.sort(naoVisitados);
        }
        return menorCaminho;
    }
}


