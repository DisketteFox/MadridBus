package dev.diskettefox.madridbus;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import dev.diskettefox.madridbus.Api_requests.ApiCall;
import dev.diskettefox.madridbus.Api_requests.ApiInterface;
import dev.diskettefox.madridbus.Api_requests.ModeloStop;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentMain extends Fragment {
    ArrayList<ModeloStop.Stops> listaDparadas=new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.view_stops, container, false);
        RecyclerView recyclerStops=(RecyclerView)view.findViewById(R.id.recycler_stops);
        ApiInterface apiInterface= ApiCall.getStop().create(ApiInterface.class);

        Call<ModeloStop> call=apiInterface.getStop(373,"da0a4f54-aaa7-4f6f-b2e7-155d1ce0957d");
        call.enqueue(new Callback<ModeloStop>() {
            @Override
            public void onResponse(Call<ModeloStop> call, Response<ModeloStop> response) {
                ModeloStop stop=response.body();
                listaDparadas.addAll(stop.getStop());

                recyclerStops.setLayoutManager(new LinearLayoutManager(getContext()));
                BusAdapter adapter=new BusAdapter(getContext(),listaDparadas);
                recyclerStops.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                Log.d("cosa stat",listaDparadas.toString());

            }

            @Override
            public void onFailure(Call<ModeloStop> call, Throwable t) {
                if (listaDparadas.isEmpty()){
                    Log.d("mensaje de error","Lo sentimos pero hubo un error inesperado.... Paguina caida :(");
                }
                Log.d("Error! llamada fallida.",t.toString());
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}