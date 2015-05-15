package com.mapas.franciscojavier.trekkingroute;

/**
 * Created by FranciscoJavier on 14-05-2015.
 */

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import greendao.Punto_interes;
public class ItemIndicador extends BaseAdapter {

    private Context context;
    private List<Punto_interes> items;

    public ItemIndicador(Context context, List<Punto_interes> items) {
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
        Punto_interes punto = this.items.get(position);

        switch (punto.getId_tipo_punto_interes()){
            case 1:
                icono.setImageResource(R.drawable.ic_parque);
                break;
            case 2:
                icono.setImageResource(R.drawable.ic_agua);
                break;
            case 3:
                icono.setImageResource(R.drawable.ic_cabaa);
                break;
            case 4:
                icono.setImageResource(R.drawable.ic_info);
                break;
            case 5:
                icono.setImageResource(R.drawable.ic_montaas);
                break;
            case 6:
                icono.setImageResource(R.drawable.ic_nieve);
                break;
            case 7:
                icono.setImageResource(R.drawable.ic_parque);
                break;
            case 8:
                icono.setImageResource(R.drawable.ic_rio);
                break;
            case 9:
                icono.setImageResource(R.drawable.ic_volcan);
                break;
            case 10:
                icono.setImageResource(R.drawable.ic_zoo);
                break;
            default:
                break;
        }
        return rowView;
    }


}