package br.iesb.mobile.alunoonline.dijkstra;

public class Aresta {

   private double peso;
   private Vertice origem;
   private Vertice destino;

   public Aresta(Vertice v1, Vertice v2, double peso) {

       this.peso = peso;
       this.origem = v1;
       this.destino = v2;

   }

   public void setPeso(double novoPeso) {

       this.peso = novoPeso;
   }

   public double getPeso() {

       return peso;
   }

   public void setDestino(Vertice destino) {
       this.destino = destino;
   }

   public Vertice getDestino() {
       return destino;
   }

   public void setOrigem(Vertice origem) {
       this.origem = origem;
   }

   public Vertice getOrigem() {
       return origem;
   }

} 