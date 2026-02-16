package dev.diskettefox.madridbus;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import dev.diskettefox.madridbus.Api_requests.ApiCall;
import dev.diskettefox.madridbus.Api_requests.ApiInterface;
import dev.diskettefox.madridbus.Api_requests.Modelo_parada;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentMain extends Fragment {
    ArrayList<Modelo_parada.Parada> listaDparadas=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.view_stops, container, false);
        RecyclerView recyclerStops=view.findViewById(R.id.recycler_stops);
        ApiInterface apiInterface= ApiCall.getStop().create(ApiInterface.class);

        Call<Modelo_parada> call=apiInterface.getAllParadas("da0a4f54-aaa7-4f6f-b2e7-155d1ce0957d");
        call.enqueue(new Callback<Modelo_parada>() {
            @Override
            public void onResponse(Call<Modelo_parada> call, Response<Modelo_parada> response) {
                Modelo_parada parada=response.body();

                listaDparadas.addAll(parada.getParadas());
                BusAdapter adapter=new BusAdapter(container.getContext(),listaDparadas);
                recyclerStops.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<Modelo_parada> call, Throwable t) {
                if (listaDparadas.isEmpty()){
                    Log.d("mensaje de error","Lo sentimos pero hubo un error inesperado.... Paguina caida :(");
                }
                Log.d("Error! llamada fallida.",t.toString());
            }
        });

        return view;
    }
}