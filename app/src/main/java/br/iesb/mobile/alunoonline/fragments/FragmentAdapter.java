package br.iesb.mobile.alunoonline.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import br.iesb.mobile.alunoonline.Model.Produto;

public class FragmentAdapter extends FragmentStatePagerAdapter {

    private final String nomeLista;
    private final ArrayList<Produto> produtos;
    private List<Produto> todosProdutos;
    private final double preco;
    private String[] titulosTab;
    private Bundle bundle;

    public FragmentAdapter(FragmentManager fm, String[] titulosTab, String nomeLista, ArrayList<Produto> produtos, double preco, List<Produto> todosProdutos) {
        super(fm);
        this.titulosTab = titulosTab;
        this.bundle = bundle;
        this.nomeLista = nomeLista;
        this.produtos = produtos;
        this.preco = preco;
        this.todosProdutos = todosProdutos;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return FragmentListaCompras.newInstance(produtos, nomeLista, preco);
            case 1:
                return new FragmentListaMercados().newInstance(produtos, nomeLista, preco, todosProdutos);
            default:
                return null;
       }
    }
    @Override
    public int getCount() {
        return titulosTab.length;
    }

    public CharSequence getPageTitle(int position){
        return this.titulosTab[position];
    }

    public void parametrosFragment(Bundle bundle){
        this.bundle = bundle;
    }

}
