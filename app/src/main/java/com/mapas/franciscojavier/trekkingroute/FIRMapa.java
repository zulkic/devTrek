package com.mapas.franciscojavier.trekkingroute;

import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mapas.franciscojavier.trekkingroute.R;

public class FIRMapa extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return inflater.inflate(R.layout.fir_layout_mapa, container,false);
    }
    public static FIRMapa newInstance(String text) {

        FIRMapa f = new FIRMapa();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }


}