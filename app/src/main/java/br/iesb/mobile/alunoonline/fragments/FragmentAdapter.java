package br.iesb.mobile.alunoonline.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class FragmentAdapter extends FragmentStatePagerAdapter {

    private String[] titulosTab;
    private Bundle bundle;

    public FragmentAdapter(FragmentManager fm, String[] titulosTab) {
        super(fm);
        this.titulosTab = titulosTab;
        this.bundle = bundle;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new FragmentListaCompras();
            case 1:
                return new FragmentListaMercados();
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
