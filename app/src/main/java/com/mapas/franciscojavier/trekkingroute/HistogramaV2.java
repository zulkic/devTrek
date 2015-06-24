package com.mapas.franciscojavier.trekkingroute;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;

import JSON.Coordenadas_Ruta;
import greendao.Coordenada;

/**
 * Created by juancarlosgonzalezca on 21-06-2015.
 */
public class HistogramaV2 extends Fragment
{
    private ArrayList<Coordenada> coordenadas;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        // fun little snippet that prevents users from taking screenshots
        // on ICS+ devices :-)
        //getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
        //WindowManager.LayoutParams.FLAG_SECURE);
        View view = inflater.inflate(R.layout.histograma2, container, false);

        Coordenadas_Ruta coordenadas_ruta = new Coordenadas_Ruta(39 , getActivity());
        coordenadas = new ArrayList<>();
        try {
            coordenadas = coordenadas_ruta.execute().get();
        }
        catch (Exception e){}

        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        ArrayList<Entry> lineEntries = new ArrayList<Entry>();
        ArrayList<String> labels = new ArrayList<String>();
        Integer distancia = 0;
        Integer i = 0;
        Coordenada anterior = null;
        int[] colores = new int[coordenadas.size()];
        for(Coordenada coordenada : coordenadas)
        {
            if(distancia == 0 && anterior == null) {
                BarEntry entry = new BarEntry(coordenada.getAltitud(), i);
                entries.add(entry);
                labels.add(distancia.toString());
                anterior = coordenada;
            }
            else
            {
                double Lat1=(anterior.getLatitud()*3.141592654)/180;
                double Lat2=(coordenada.getLatitud()*3.141592654)/180;

                double Long1=(anterior.getLongitud()*3.141592654)/180;
                double Long2=(coordenada.getLongitud()*3.141592654)/180;
                Double calc = 6371 * Math.acos((Math.cos(Lat1) * Math.cos(Lat2) * Math.cos(Long2 - Long1) + Math.sin(Lat1) * Math.sin(Lat2)));
                calc = calc *1000;
                distancia += calc.intValue();
                BarEntry entry = new BarEntry(coordenada.getAltitud().floatValue(),i);
                Entry lineEntry = new Entry(coordenada.getAltitud().floatValue(),i);
                entries.add(entry);
                lineEntries.add(lineEntry);
                labels.add(distancia.toString());
            }
            colores[i] = calcularColor(coordenada.getAltitud());
            i+=1;
        }
        //int[] colores = {Color.BLUE, Color.MAGENTA, Color.GREEN, Color.CYAN, Color.DKGRAY};
        BarDataSet dataset = new BarDataSet(entries, "Distancia (mts) vs Altitud (mts)");
        dataset.setColors(colores);

        BarChart chart = new BarChart(getActivity());
        getActivity().setContentView(chart);

        BarData data = new BarData(labels, dataset);
        chart.setData(data);
        chart.setDescription("Histograma");

        return view;
    }

    public int calcularColor(int altura)
    {
        if(altura >=0 && altura < 250)
            return Color.rgb(3, 128, 0);;
        if(altura >=250 && altura < 500)
            return Color.rgb(141,212,40);
        if(altura >=500 && altura < 1000)
            return Color.rgb(154,226,143);
        if(altura >=1000 && altura < 1500)
            return Color.rgb(255,180,91);
        if(altura >=1500 && altura <3000)
            return Color.rgb(134,73,0);
        else
            return Color.rgb(255,217,172);
    }


}

/*      LineChart
        ArrayList<Entry> entries = new ArrayList<Entry>();
        ArrayList<String> labels = new ArrayList<String>();
        Integer distancia = 0;
        Integer i = 0;
        Coordenada anterior = null;
        for(Coordenada coordenada : coordenadas)
        {
            if(distancia == 0 && anterior == null) {
                Entry entry = new Entry(coordenada.getAltitud(), i);
                entries.add(entry);
                labels.add(distancia.toString());
                anterior = coordenada;
            }
            else
            {
                double Lat1=(anterior.getLatitud()*3.141592654)/180;
                double Lat2=(coordenada.getLatitud()*3.141592654)/180;

                double Long1=(anterior.getLongitud()*3.141592654)/180;
                double Long2=(coordenada.getLongitud()*3.141592654)/180;
                Double calc = 6371 * Math.acos((Math.cos(Lat1) * Math.cos(Lat2) * Math.cos(Long2 - Long1) + Math.sin(Lat1) * Math.sin(Lat2)));
                calc = calc *1000;
                distancia += calc.intValue();
                Log.i("altura: ", coordenada.getAltitud().toString());
                Entry entry = new Entry(coordenada.getAltitud().floatValue(),i);
                entries.add(entry);
                labels.add(distancia.toString());
            }
            i+=1;
        }

        LineDataSet dataset = new LineDataSet(entries, "Distancia vs Altitud");
        LineChart chart = new LineChart(getActivity());
        getActivity().setContentView(chart);

        LineData data = new LineData(labels, dataset);
        chart.setData(data);
        chart.setDescription("Histograma");

        return view;
* */
