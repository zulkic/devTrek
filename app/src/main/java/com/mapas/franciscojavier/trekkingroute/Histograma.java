package com.mapas.franciscojavier.trekkingroute;

/**
 * Created by juancarlosgonzalezca on 15-06-2015.
 */

import android.app.Fragment;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

import java.util.ArrayList;

import JSON.Coordenadas_Ruta;
import greendao.Coordenada;

/**
 * A straightforward example of using AndroidPlot to plot some data.
 */
public class Histograma extends Fragment
{

    private XYPlot plot;
    private PointF minXY;
    private PointF maxXY;
    private ArrayList<Coordenada> coordenadas;

    // Definition of the touch states
    static final int NONE = 0;
    static final int ONE_FINGER_DRAG = 1;
    static final int TWO_FINGERS_DRAG = 2;
    int mode = NONE;

    PointF firstFinger;
    float lastScrolling;
    float distBetweenFingers;
    float lastZooming;


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
        View view = inflater.inflate(R.layout.fragment_histograma, container, false);

        // initialize our XYPlot reference:
        plot = (XYPlot) view.findViewById(R.id.mySimpleXYPlot);

        Coordenadas_Ruta coordenadas_ruta = new Coordenadas_Ruta(39 , getActivity());
        coordenadas = new ArrayList<>();
        try {
            coordenadas = coordenadas_ruta.execute().get();
        }
        catch (Exception e){}
        // Create a couple arrays of y-values to plot:
        ArrayList<Number> series1Numbers = new ArrayList<Number>();
        ArrayList<Number> distancia = new ArrayList<>();
        int i = 0;
        for(Coordenada coordenada : coordenadas)
        {
            series1Numbers.add(coordenada.getAltitud());
            distancia.add(i);
            i+=100;
        }

        /*XYSeries series1 = new SimpleXYSeries(distancia,
                series1Numbers,          // SimpleXYSeries takes a List so turn our array into a List
                "Ruta Cerro Condell");   */                          // Set the display title of the series


        // Turn the above arrays into XYSeries':
        XYSeries series1 = new SimpleXYSeries(
                series1Numbers,          // SimpleXYSeries takes a List so turn our array into a List
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, // Y_VALS_ONLY means use the element index as the x value
                "Ruta Cerro Condell");                             // Set the display title of the series



        // Create a formatter to use for drawing a series using LineAndPointRenderer
        // and configure it from xml:
        LineAndPointFormatter series1Format = new LineAndPointFormatter();
        series1Format.setPointLabelFormatter(new PointLabelFormatter());
        series1Format.configure(getActivity(),
                R.xml.line);

// get rid of the decimal place on the display:

        plot.getGraphWidget().setTicksPerRangeLabel(1);
        plot.getGraphWidget().setTicksPerDomainLabel(3);
        //plot.getGraphWidget().setDomainLabelWidth(50);

        plot.setDomainLabel("Je ne se pais");
        plot.setRangeLabel("Altura (mts)");

        // add a new series' to the xyplot:
        plot.addSeries(series1, series1Format);

        // reduce the number of range labels
        //plot.setTicksPerRangeLabel(2);
        //plot.getGraphWidget().setDomainLabelOrientation(-45);

        plot.redraw();
        return view;
    }
    /*
    private void zoom(float scale) {
        float domainSpan = maxXY.x    - minXY.x;
        float domainMidPoint = maxXY.x        - domainSpan / 2.0f;
        float offset = domainSpan * scale / 2.0f;
        minXY.x=domainMidPoint- offset;
        maxXY.x=domainMidPoint+offset;
    }

    private void scroll(float pan) {
        float domainSpan = maxXY.x    - minXY.x;
        float step = domainSpan / plot.getWidth();
        float offset = pan * step;
        minXY.x+= offset;
        maxXY.x+= offset;
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
    }
    */
}
