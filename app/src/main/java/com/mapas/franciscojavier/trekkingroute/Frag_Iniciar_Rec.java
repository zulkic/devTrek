package com.mapas.franciscojavier.trekkingroute;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import com.actionbarsherlock.app.SherlockFragment;

import java.lang.reflect.Field;

public class Frag_Iniciar_Rec extends SherlockFragment implements View.OnClickListener {

    private CustomViewPager mViewPager;
    private ViewPagerAdapter adapter;
    private Boolean enabled = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpager_main, container, false);
        // Locate the ViewPager in viewpager_main.xml
        mViewPager = (CustomViewPager) view.findViewById(R.id.viewPager);
        ToggleButton lock = (ToggleButton) view.findViewById(R.id.button);
        lock.setOnClickListener(this);
        // Set the ViewPagerAdapter into ViewPager
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(1);
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class
                    .getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.button:
                if(enabled) {
                    enabled = false;
                    mViewPager.setSwipeable(enabled);
                }
                else
                {
                    enabled = true;
                    mViewPager.setSwipeable(enabled);
                }
                break;
        }
    }
}