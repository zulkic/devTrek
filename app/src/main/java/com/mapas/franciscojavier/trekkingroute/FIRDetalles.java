package com.mapas.franciscojavier.trekkingroute;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mapas.franciscojavier.trekkingroute.Utility.Globals;
import com.mapas.franciscojavier.trekkingroute.dummy.DummyContent;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import JSON.Puntos_Interes_Ruta;
import greendao.Punto_interes;
import greendao.Ruta;
import repositorios.RutaRepo;

public class FIRDetalles extends Fragment implements AbsListView.OnItemClickListener{

    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private ListView listView;
    private Ruta ruta;
    private Long id_ruta;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;
    private ArrayList<Punto_interes> lista_puntos;

    // TODO: Rename and change types of parameters
    public static DetalleIndicadorFragment newInstance(String param1, String param2) {
        DetalleIndicadorFragment fragment = new DetalleIndicadorFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FIRDetalles() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.id_ruta = this.getArguments().getLong("id_ruta");
        ruta = RutaRepo.getRutaForId(Globals.context, id_ruta);
        Puntos_Interes_Ruta tarea_get_puntos = new Puntos_Interes_Ruta(ruta.getId().intValue(),getActivity());
        try {
            this.lista_puntos = tarea_get_puntos.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalleindicador, container, false);

        // Set the adapter
        listView = (ListView) view.findViewById(android.R.id.list);
        if(this.lista_puntos.isEmpty()){

        }
        else{
            this.listView.setAdapter(new DetalleIndicadorItem(Globals.context,this.lista_puntos));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView adapter, View view, int position, long arg) {
                    // Loads the given URL
                    Toast.makeText(getActivity(), "Accediendo a: "
                            , Toast.LENGTH_SHORT).show();
                }
            });

        }

        // Set OnItemClickListener so we can be notified on item clicks
        //mListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
        }
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = listView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }



    public static FIRDetalles newInstance(String text) {

        FIRDetalles f = new FIRDetalles();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }
}