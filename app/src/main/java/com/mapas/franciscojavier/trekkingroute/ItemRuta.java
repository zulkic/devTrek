package com.mapas.franciscojavier.trekkingroute;

/**
 * Created by FranciscoJavier on 12-05-2015.
 */
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import greendao.Ruta;

public class ItemRuta extends BaseAdapter {

    private Context context;
    private List<Ruta> items;

    public ItemRuta(Context context, List<Ruta> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public Object getItem(int position) {
        return this.items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;

        if (convertView == null) {
            // Create a new view into the list.
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.route_row, parent, false);
        }

        // Set data into the view.
        TextView nombre = (TextView) rowView.findViewById(R.id.nombre_ruta);
        TextView distancia = (TextView) rowView.findViewById(R.id.kms_ruta);
        TextView tiempo = (TextView) rowView.findViewById(R.id.tiempo_ruta);
        TextView oficial = (TextView) rowView.findViewById(R.id.oficial_ruta);

        Ruta item = this.items.get(position);
        nombre.setText(item.getNombre());
        nombre.setTextColor(Color.BLACK);
        distancia.setText(Float.toString(item.getKms())+" "+ context.getText(R.string.text_distancia));
        tiempo.setText(item.getTiempo_estimado()+" " +context.getText(R.string.text_tiempo));
        if(item.getOficial())
        {
            oficial.setText(R.string.text_oficial);
        }
        else {
            oficial.setText(R.string.text_no_oficial);
        }
        if(position%2==0)
        {
            rowView.setBackgroundColor(Color.LTGRAY);
        }
        return rowView;
    }


}