package com.mapas.franciscojavier.trekkingroute;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.mapas.franciscojavier.trekkingroute.Utility.Globals;
import com.mapas.franciscojavier.trekkingroute.Utility.RefreshListener;

import java.util.ArrayList;

import JSON.Coordenadas_Ruta;
import greendao.Coordenada;

public class FIRHistograma extends Fragment implements View.OnClickListener, RefreshListener {

    private ArrayList<Coordenada> coordenadas;
    private LineChart chart;
    private Integer max;
    private Integer min;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Coordenadas_Ruta coordenadas_ruta = new Coordenadas_Ruta(Globals.ini_rec.getId().intValue() , Globals.context);
        coordenadas = new ArrayList<>();
        try {
            coordenadas = coordenadas_ruta.execute().get();
        }
        catch (Exception e){}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
            Log.i("histograma: ", "me han llamado");
            view = inflater.inflate(R.layout.fir_layout_histograma, null);
            ImageButton botonDescarga = (ImageButton) view.findViewById(R.id.imageButton_download);
            botonDescarga.setOnClickListener(this);

            ArrayList<Entry> entries = new ArrayList<Entry>();
            ArrayList<String> labels = new ArrayList<String>();
            Integer distancia = 0;
            Integer i = 0;
            Coordenada anterior = null;
            int[] colores = new int[coordenadas.size()];
            max = 0;
            min = 0;
            for(Coordenada coordenada : coordenadas)
            {
                if(distancia == 0 && anterior == null) {
                    Entry entry = new Entry(coordenada.getAltitud(), i);
                    entries.add(entry);
                    labels.add(distancia.toString());
                    anterior = coordenada;
                    max = coordenada.getAltitud();
                    min = max;
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
                    Entry entry = new Entry(coordenada.getAltitud().floatValue(),i);
                    entries.add(entry);
                    labels.add(distancia.toString());
                    int actual = coordenada.getAltitud();
                    if(actual > max)
                        max = actual;
                    if(actual < min)
                        min = actual;
                }
                colores[i] = calcularColor(coordenada.getAltitud());
                i+=1;
            }

            LineDataSet dataset = new LineDataSet(entries, "Ruta");
            dataset.setColor(Color.BLACK);
            dataset.setDrawValues(false);

            chart = (LineChart) view.findViewById(R.id.chart);

            ArrayList<LineDataSet> datos = coloresNivel(entries);
            datos.add(dataset);

            LineData data = new LineData(labels, datos);

            chart.setData(data);
            chart.setDescription("Distancia (mts) vs Altitud (mts)");

            //Legend legend = chart.getLegend();
            //legend.setEnabled(false);

            if(min-30 > 0)
                min-=30;
            max+=30;
            YAxis yAxisL = chart.getAxisLeft();
            yAxisL.setStartAtZero(false);
            yAxisL.setAxisMinValue(min);
            yAxisL.setAxisMaxValue(max);

            YAxis yAxisR = chart.getAxisRight();
            yAxisR.setStartAtZero(false);
            yAxisR.setAxisMinValue(min);
            yAxisR.setAxisMaxValue(max);

        return view;
    }

    public int calcularColor(int altura)
    {
        if(altura >=0 && altura < 250)
            return Color.rgb(3, 128, 0);
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

    public static FIRHistograma newInstance(String text) {

        FIRHistograma f = new FIRHistograma();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }

    public ArrayList<LineDataSet> coloresNivel(ArrayList<Entry> entries)
    {
        ArrayList<LineDataSet> datos = new ArrayList<>();
        ArrayList<Entry> entries1 = new ArrayList<>();
        ArrayList<Entry> entries2 = new ArrayList<>();
        ArrayList<Entry> entries3 = new ArrayList<>();
        ArrayList<Entry> entries4 = new ArrayList<>();
        ArrayList<Entry> entries5 = new ArrayList<>();
        ArrayList<Entry> entries6 = new ArrayList<>();
        Entry entry = null;
        int i=0;
        Log.i("max: ", max.toString());
        Log.i("min: ", min.toString());
        for(Entry punto : entries) {
            if(max >= 0 && min <= 250) {
                entry = new Entry(250, i);
                entries1.add(entry);
            }
            if (max >= 250 && min <= 500) {
                entry = new Entry(500, i);
                entries2.add(entry);
            }
            if (max >= 500 && min <= 1000) {
                entry = new Entry(1000, i);
                entries3.add(entry);
            }
            if (max >= 1000 && min <= 1500) {
                entry = new Entry(1500, i);
                entries4.add(entry);
            }
            if(max >= 1500 && min <= 3000) {
                entry = new Entry(3000, i);
                entries5.add(entry);
            }
            if (max >= 3000){
                entry = new Entry(max+20, i);
                entries6.add(entry);
            }
            i+=1;
        }
        if(entries1 != null && entries1.size() > 0) {
            LineDataSet nivel1 = new LineDataSet(entries1, "0-250mts");
            nivel1.setColor(Color.rgb(3, 128, 0));
            nivel1.setDrawValues(false);
            nivel1.setDrawFilled(true);
            nivel1.setFillColor(Color.rgb(3, 128, 0));
            nivel1.setDrawCircles(false);
            datos.add(nivel1);
        }
        if(entries2 != null && entries2.size() > 0) {
            LineDataSet nivel2 = new LineDataSet(entries2, "250-500mts");
            nivel2.setColor(Color.rgb(141, 212, 40));
            nivel2.setDrawValues(false);
            nivel2.setDrawFilled(true);
            nivel2.setFillColor(Color.rgb(141, 212, 40));
            nivel2.setDrawCircles(false);
            datos.add(nivel2);
        }

        if(entries3 != null && entries3.size() > 0) {
            LineDataSet nivel3 = new LineDataSet(entries3, "500-1000mts");
            nivel3.setColor(Color.rgb(154, 226, 143));
            nivel3.setDrawValues(false);
            nivel3.setDrawFilled(true);
            nivel3.setFillColor(Color.rgb(154, 226, 143));
            nivel3.setDrawCircles(false);
            datos.add(nivel3);
        }

        if(entries4 != null && entries4.size() > 0) {
            LineDataSet nivel4 = new LineDataSet(entries4, "1000-1500mts");
            nivel4.setColor(Color.rgb(255, 180, 91));
            nivel4.setDrawValues(false);
            nivel4.setDrawFilled(true);
            nivel4.setFillColor(Color.rgb(255, 180, 91));
            nivel4.setDrawCircles(false);
            datos.add(nivel4);
        }

        if(entries5 != null && entries5.size() > 0) {
            LineDataSet nivel5 = new LineDataSet(entries5, "1500-3000mts");
            nivel5.setColor(Color.rgb(134, 73, 0));
            nivel5.setDrawValues(false);
            nivel5.setDrawFilled(true);
            nivel5.setFillColor(Color.rgb(134, 73, 0));
            nivel5.setDrawCircles(false);
            datos.add(nivel5);
        }
        if(entries6 != null && entries6.size() > 0) {
            LineDataSet nivel6 = new LineDataSet(entries6, "+3000mts");
            nivel6.setColor(Color.rgb(255, 217, 172));
            nivel6.setDrawValues(false);
            nivel6.setDrawFilled(true);
            nivel6.setFillColor(Color.rgb(255, 217, 172));
            nivel6.setDrawCircles(false);
            datos.add(nivel6);
        }
        return datos;
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageButton_download:
                chart.saveToGallery("cerro condell.jpg",85);
                break;
        }
    }

    @Override
    public void fragmentBecameVisible() {

    }
}
