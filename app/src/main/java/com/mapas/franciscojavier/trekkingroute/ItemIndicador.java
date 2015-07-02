package com.mapas.franciscojavier.trekkingroute;

/**
 * Created by FranciscoJavier on 14-05-2015.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import greendao.Tipo_obstaculo;
import greendao.Tipo_punto_interes;

public class ItemIndicador extends BaseAdapter {

    private Context context;
    private List<Tipo_punto_interes> items;
    private List<Tipo_obstaculo> items_obs;

    public ItemIndicador(Context context, List<Tipo_punto_interes> items) {
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
            rowView = inflater.inflate(R.layout.indicador_row, parent, false);
        }

        // Set data into the view.
        ImageView icono = (ImageView) rowView.findViewById(R.id.imageButton_Ind);
        TextView tv = (TextView) rowView.findViewById(R.id.textView_nombre_indicador);
        Tipo_punto_interes punto = this.items.get(position);
        int resID = context.getResources().getIdentifier(punto.getNombre_icono().trim(),"drawable",context.getPackageName());
        icono.setImageResource(resID);
        tv.setText(punto.getNombre());
        return rowView;
    }


}