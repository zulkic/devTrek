package com.mapas.franciscojavier.trekkingroute;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;

/**
 * Created by nicolas on 10-06-2015.
 */
public class PantallaInicio extends SherlockFragment {

    public static PantallaInicio newInstance() {
        PantallaInicio fragment = new PantallaInicio();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_login);
        View v = inflater.inflate(R.layout.frament_pantalla_inicio, container, false);
        return v;
    }
}
