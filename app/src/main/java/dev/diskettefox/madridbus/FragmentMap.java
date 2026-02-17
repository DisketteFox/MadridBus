package dev.diskettefox.madridbus;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;


public class FragmentMap extends Fragment {
        public FragmentMap(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view=inflater.inflate(R.layout.view_map,container,false);
            ImageView imagenMap=view.findViewById(R.id.imgMap);
            String url="https://www.crtm.es/media/t3wadujx/serie_2a1_madrid_ciudad_es.pdf";
            Glide.with(requireContext())
                    .load(url)
                    .into(imagenMap);
            return view;
        }
}