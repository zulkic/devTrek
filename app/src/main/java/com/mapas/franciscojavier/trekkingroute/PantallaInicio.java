package com.mapas.franciscojavier.trekkingroute;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by nicolas on 10-06-2015.
 */
public class PantallaInicio extends android.app.Fragment {

    public static PantallaInicio newInstance() {
        PantallaInicio fragment = new PantallaInicio();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
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
