package com.mapas.franciscojavier.trekkingroute;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.mapas.franciscojavier.trekkingroute.Account.MainCalls;

import java.util.Calendar;

/**
 * Created by nicolas on 06-07-2015.
 */
public class DetallesFinRecorrido  extends SherlockFragment implements AdapterView.OnClickListener{
    private static String CRONO_TIEMPO;
    private static String DISTANCIA_FINAL;
    private MainCalls mListener;

    public static DetallesFinRecorrido newInstance(String text, String distanciaRecoFin) {
        DetallesFinRecorrido fragment = new DetallesFinRecorrido();
        CRONO_TIEMPO = text;
        DISTANCIA_FINAL = distanciaRecoFin;
        Bundle args = new Bundle();
        args.putString(CRONO_TIEMPO, text);
        args.putString(DISTANCIA_FINAL, distanciaRecoFin);

        fragment.setArguments(args);

        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_detalles_fin_recorrido, container, false);
        TextView tiempoInicio = (TextView) v.findViewById(R.id.edittextiempo_inicio);
        TextView tiempoFin = (TextView) v.findViewById(R.id.edittextiempo_fin);
        TextView tiempoTotalRecorrido = (TextView) v.findViewById(R.id.edittextiempo_recorrido);
        TextView distanciaRecorrida = (TextView) v.findViewById(R.id.editText_distancia_recorrida_fin);
        Button aceptar = (Button) v.findViewById(R.id.button_aceptar_detalles_fin);

        distanciaRecorrida.setText(DISTANCIA_FINAL);

        String tiempoFinal = calcularTiempoFinal();
        tiempoFin.setText(tiempoFinal);

        String tiempoInicial = calcularTiempoInicial(tiempoFinal);
        tiempoInicio.setText(tiempoInicial);
        tiempoTotalRecorrido.setText(CRONO_TIEMPO);

        aceptar.setOnClickListener(this);

        return v;
    }

    private String calcularTiempoInicial(String tiempoFinal) {

        String tiempo="", h, m, s;
        int seg=0, hor=0, min=0;
        String delimiter = ":";
        String[] tempF,tempC;
        tempF = tiempoFinal.split(delimiter);
        tempC = CRONO_TIEMPO.split(delimiter);

        if(tempC.length<3){
            CRONO_TIEMPO="00:"+CRONO_TIEMPO;
            int horaF = Integer.parseInt(tempF[0]);
            hor=horaF;
            int minutoF = Integer.parseInt(tempF[1]);
            int minutoC = Integer.parseInt(tempC[0]);
            min=minutoF-minutoC;
            int segundoF = Integer.parseInt(tempF[2]);
            int segundoC = Integer.parseInt(tempC[1]);
            seg=segundoF-segundoC;
        }
        else if (tempC.length==tempF.length){
            int horaF = Integer.parseInt(tempF[0]);
            int horaC = Integer.parseInt(tempC[0]);
            hor=horaF-horaC;
            int minutoF = Integer.parseInt(tempF[1]);
            int minutoC = Integer.parseInt(tempC[1]);
            min=minutoF-minutoC;
            int segundoF = Integer.parseInt(tempF[2]);
            int segundoC = Integer.parseInt(tempC[2]);
            seg=segundoF-segundoC;
        }
        if(hor<10) h="0"+hor+":";
        else  h=hor+":";
        if(min<10) m="0"+min+":";
        else  m=min+":";
        if(seg<10) s="0"+seg;
        else  s=seg+"";

        tiempo = h+m+s;
        return tiempo;
    }

    private String calcularTiempoFinal() {

        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);
        int second = now.get(Calendar.SECOND);
//        System.out.printf("FIN %02d:%02d:%02d", hour, minute, second);
        String h,m,s;
        if(hour<10){
            h="0"+hour+":";
        }
        else{
            h=hour+":";
        }
        if(minute<10){
            m="0"+minute+":";
        }
        else{
            m=minute+":";
        }
        if(second<10){
            s="0"+second;
        }
        else{
            s=second+"";
        }
        String tiempoTotalRecorrido= h+m+s;
        return tiempoTotalRecorrido;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_aceptar_detalles_fin:
                mListener.goToHome();
                break;
        }
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (MainCalls) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement MainCallbacks");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
