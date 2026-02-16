package dev.diskettefox.madridbus;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
public class FragmentMap extends Fragment {
        public FragmentMap(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view=inflater.inflate(R.layout.view_map,container,false);
            return view;
    }
}