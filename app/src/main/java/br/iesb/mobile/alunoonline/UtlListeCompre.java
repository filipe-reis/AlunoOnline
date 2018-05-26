package br.iesb.mobile.alunoonline;

import java.util.Arrays;
import java.util.List;

public class UtlListeCompre {

    public UtlListeCompre(){};

    public Object ordenarLista(Produtos[] prod, int inicio, int fim) {

            if (inicio < fim) {
                int posicaoPivo = separar(prod, inicio, fim);
                ordenarLista(prod, inicio, posicaoPivo - 1);
                ordenarLista(prod, posicaoPivo + 1, fim);
            }

            return Arrays.asList(prod);

    }

    private static int separar(Produtos[] vetor, int inicio, int fim) {

        Produtos aux = vetor[inicio]; //Variavel utilizada na ultima parte do algoritmo para
        // setar valor final do vetor
        double pivo = vetor[inicio].getPreco();
        int i = inicio + 1, f = fim;
        while (i <= f) {
            if (vetor[i].getPreco() <= pivo)
                i++;
            else if (pivo < vetor[f].getPreco())
                f--;
            else {
                Produtos troca = vetor[i];
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
}
