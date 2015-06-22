package com.mapas.franciscojavier.trekkingroute;

/**
 * Created by FranciscoJavier on 12-05-2015.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

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
            rowView.setBackgroundColor(context.getResources().getColor(R.color.background_pair_list));
            nombre.setTextColor(context.getResources().getColor(R.color.text_pair_list));
            distancia.setTextColor(context.getResources().getColor(R.color.text_pair_list));
            tiempo.setTextColor(context.getResources().getColor(R.color.text_pair_list));
            oficial.setTextColor(context.getResources().getColor(R.color.text_pair_list));

        }
        else{
            rowView.setBackgroundColor(context.getResources().getColor(R.color.background_odd_list));
            nombre.setTextColor(context.getResources().getColor(R.color.white));
            distancia.setTextColor(context.getResources().getColor(R.color.white));
            tiempo.setTextColor(context.getResources().getColor(R.color.white));
            oficial.setTextColor(context.getResources().getColor(R.color.white));
        }
        return rowView;
    }


}