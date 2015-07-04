package com.mapas.franciscojavier.trekkingroute.Utility;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by juancarlosgonzalezca on 04-07-2015.
 */
public class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

    public MyGestureListener() {
        super();
    }

    @Override
    public void onLongPress(MotionEvent e) {
        //onLongPress code
        Log.i("Evento: ", e.toString());
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        //onDoubleTap code
        Log.i("Evento: ", e.toString());
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        //onFling cod
        Log.i("Evento: ", e1.toString());
        return true;
    }
}