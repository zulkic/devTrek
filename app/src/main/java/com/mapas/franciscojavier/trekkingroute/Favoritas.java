package com.mapas.franciscojavier.trekkingroute;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.mapas.franciscojavier.trekkingroute.Utility.Globals;

import java.util.ArrayList;

import greendao.Ruta;
import repositorios.RutaRepo;

/**
 * Created by juancarlosgonzalezca on 18-06-2015.
 */
public class Favoritas extends SherlockFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private ListView listView;
    ArrayList<Ruta> rutas = new ArrayList<>();

    // TODO: Rename and change types of parameters
    public static Favoritas newInstance(String param1, String param2) {
        Favoritas fragment = new Favoritas();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public Favoritas() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        try {
            rutas =  (ArrayList<Ruta>) RutaRepo.favoritas(getActivity());
            for(Ruta ruta : rutas )
            {
                Log.i("ruta: ", ruta.getNombre());
                Log.i("oficial: ", ruta.getOficial().toString());
            }
        }
        catch (Exception e)
        {
            Log.i("Error: ", "no hay rutas favoritas");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_routes, container, false);

        // Set the adapter
        listView = (ListView) view.findViewById(android.R.id.list);
        if(rutas.isEmpty())
        {
            setEmptyText("Texto Vacio");
        }
        else{
            // Sets the data behind this ListView
            this.listView.setAdapter(new ItemRuta(getActivity(),rutas));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView adapter, View view, int position, long arg) {
                    // Loads the given URL
                    Ruta item = (Ruta) listView.getAdapter().getItem(position);
                    Toast.makeText(getActivity(), "Accediendo a: " + item.getNombre()
                            , Toast.LENGTH_SHORT).show();

                    Globals.ini_rec = item;
                    Fragment frag = new Frag_Iniciar_Rec();
                    FragmentTransaction ft = Globals.ft.beginTransaction();
                    ft.replace(R.id.content_frame, frag);
                    ft.addToBackStack(frag.getTag());
                    ft.commit();
                }
            });

        }

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

}
