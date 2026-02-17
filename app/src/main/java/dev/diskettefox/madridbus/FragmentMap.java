package dev.diskettefox.madridbus;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;


public class FragmentMap extends Fragment {
        public FragmentMap(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view=inflater.inflate(R.layout.view_map,container,false);
            PhotoView map1=view.findViewById(R.id.imgMap1);
         



        return view;

        }
}