package br.iesb.mobile.alunoonline;

import java.util.Arrays;

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
    public Object ordenarListaPreco(Produtos[] prod, int inicio, int fim) {

            if (inicio < fim) {
                int posicaoPivo = separarPorPreco(prod, inicio, fim);
                ordenarListaPreco(prod, inicio, posicaoPivo - 1);
                ordenarListaPreco(prod, posicaoPivo + 1, fim);
            }
            return Arrays.asList(prod);
    }

    private static int separarPorPreco(Produtos[] vetor, int inicio, int fim) {

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


    /**
     * Faz ordenação por preco do produto utilizando o algoritmo quicksort
     * @param prod vetor a ser ordenado
     * @param inicio posicao inicial
     * @param fim posicao final
     * @return vetor ordenado pelo preco
     */
    public Object ordenarListaNome(Produtos[] prod, int inicio, int fim) {

        if (inicio < fim) {
            int posicaoPivo = separarPorNome(prod, inicio, fim);
            ordenarListaNome(prod, inicio, posicaoPivo - 1);
            ordenarListaNome(prod, posicaoPivo + 1, fim);
        }
        return Arrays.asList(prod);
    }

    private static int separarPorNome(Produtos[] vetor, int inicio, int fim) {

        Produtos aux = vetor[inicio]; //Variavel utilizada na ultima parte do algoritmo para
        // setar valor final do vetor
        String pivo = vetor[inicio].getNome();
        int i = inicio + 1, f = fim;
        while (i <= f) {
            if (vetor[i].getNome().compareTo(pivo) <= 0 )
                i++;
            else if (vetor[f].getNome().compareTo(pivo) > 0 )
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
