package dev.diskettefox.madridbus.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.loadingindicator.LoadingIndicator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.diskettefox.madridbus.R;
import dev.diskettefox.madridbus.adapters.StopAdapter;
import dev.diskettefox.madridbus.api.ApiCall;
import dev.diskettefox.madridbus.api.ApiInterface;
import dev.diskettefox.madridbus.api.StopModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentStop extends Fragment {
    private final ArrayList<StopModel.Stop> stopsList = new ArrayList<>();
    private StopAdapter adapter;
    private LoadingIndicator loadingIndicator;

    private final int[] stopIds = {5710, 3862, 3542, 4812};
    private int responsesReceived = 0;
    private final Map<Integer, Integer> stopIdToIndex = new HashMap<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_stops, container, false);
        RecyclerView recyclerStops = view.findViewById(R.id.recycler_stops);
        loadingIndicator = view.findViewById(R.id.progress_bar);

        ApiInterface apiInterface = ApiCall.callApi().create(ApiInterface.class);
        String accessToken = ApiCall.token;

        // Initialize RecyclerView and Adapter
        recyclerStops.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new StopAdapter(getContext(), stopsList);
        recyclerStops.setAdapter(adapter);

        // Show loading screen
        loadingIndicator.setVisibility(View.VISIBLE);

        // Populate the map for sorting
        for (int i = 0; i < stopIds.length; i++) {
            stopIdToIndex.put(stopIds[i], i);
        }

        // Fetch data for all stop IDs
        if (stopsList.isEmpty()) {
            for (int stopId : stopIds) {
                fetchStopData(apiInterface, stopId, accessToken);
            }
        }
        return view;
    }

    private void fetchStopData(ApiInterface apiInterface, int stopId, String accessToken) {
        Call<StopModel> call = apiInterface.getStop(stopId, accessToken);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<StopModel> call, @NonNull Response<StopModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    StopModel stop = response.body();
                    // Process the response
                    if (stop.getStopsData() != null) {
                        for (StopModel.Data data : stop.getStopsData()) {
                            List<StopModel.Stop> stops = data.getStops();
                            if (stops != null) {
                                synchronized (stopsList) {
                                    stopsList.addAll(stops);
                                }
                            }
                        }
                        Log.d("JustWorking", "Stops loaded for stop ID: " + stopId);
                    } else {
                        Log.d("API Response", "No stops data for stop ID: " + stopId);
                    }
                } else {
                    Log.d("API Response", "Failed response for stop ID: " + stopId + ", Response: " + response);
                }
                onResponseReceived();
            }

            @Override
            public void onFailure(@NonNull Call<StopModel> call, @NonNull Throwable t) {
                Log.e("Call Error", "Error retrieving data for stop ID: " + stopId, t);
                onResponseReceived();
            }
        });
    }

    private void onResponseReceived() {
        responsesReceived++;
        if (responsesReceived == stopIds.length) {
            // All responses received, now sort the list and update the UI
            synchronized (stopsList) {
                Collections.sort(stopsList, Comparator.comparing(stop -> stopIdToIndex.get(Integer.parseInt(stop.getStopId()))));
            }
            adapter.notifyDataSetChanged();
            hideLoadingIndicator();
        }
    }

    // Pretty self-explanatory
    private void hideLoadingIndicator() {
        loadingIndicator.setVisibility(View.GONE);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
