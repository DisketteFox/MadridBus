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
import java.util.List;

import dev.diskettefox.madridbus.Api_requests.ApiCall;
import dev.diskettefox.madridbus.Api_requests.ApiInterface;
import dev.diskettefox.madridbus.Api_requests.ModeloStop;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentMain extends Fragment {
    ArrayList<ModeloStop.Stop> stopsList = new ArrayList<>(); // Change Stops to Stop

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_stops, container, false);
        RecyclerView recyclerStops = (RecyclerView) view.findViewById(R.id.recycler_stops);
        ApiInterface apiInterface = ApiCall.getStop().create(ApiInterface.class);
        String accessToken = ApiCall.token;

        // Call to API
        Call<ModeloStop> call = apiInterface.getStop(373, accessToken);
        call.enqueue(new Callback<ModeloStop>() {
            @Override
            public void onResponse(Call<ModeloStop> call, Response<ModeloStop> response) {
                ModeloStop stop = response.body();
                if (stop != null) {
                    // Loop through each Data item and add stops to the list
                    for (ModeloStop.Data data : stop.getStopsData()) {
                        List<ModeloStop.Stop> stops = data.getStops();
                        if (stops != null) {
                            stopsList.addAll(stops);
                        }
                    }

                    // Set up RecyclerView
                    recyclerStops.setLayoutManager(new LinearLayoutManager(getContext()));
                    BusAdapter adapter = new BusAdapter(getContext(), stopsList);
                    recyclerStops.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    // Notify success
                    Log.d("JustWorking", stopsList.toString());
                } else {
                    Log.d("API Response", "No data returned");
                }
            }

            @Override
            public void onFailure(Call<ModeloStop> call, Throwable t) {
                if (stopsList.isEmpty()) {
                    Log.d("Message error", "Unexpected error (Unreachable API)");
                }
                Log.d("Call error", t.toString());
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
