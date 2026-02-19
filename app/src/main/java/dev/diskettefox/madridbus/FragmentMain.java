package dev.diskettefox.madridbus;

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
import com.google.android.material.search.SearchBar;

import java.util.ArrayList;
import java.util.List;

import dev.diskettefox.madridbus.adapters.BusAdapter;
import dev.diskettefox.madridbus.api.ApiCall;
import dev.diskettefox.madridbus.api.ApiInterface;
import dev.diskettefox.madridbus.api.StopModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentMain extends Fragment {
    private final ArrayList<StopModel.Stop> stopsList = new ArrayList<>();
    private BusAdapter adapter;
    private LoadingIndicator loadingIndicator;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_stops, container, false);
        RecyclerView recyclerStops = view.findViewById(R.id.recycler_stops);
        loadingIndicator = view.findViewById(R.id.progress_bar);
        SearchBar searchBar=view.findViewById(R.id.search_bar_Stops);

        ApiInterface apiInterface = ApiCall.getStop().create(ApiInterface.class);
        String accessToken = ApiCall.token;

        // Initialize RecyclerView and Adapter
        recyclerStops.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BusAdapter(getContext(), stopsList);
        recyclerStops.setAdapter(adapter);

        // Show loading screen
        loadingIndicator.setVisibility(View.VISIBLE);

        // Array of stop IDs to fetch
        int[] stopIds = {5710, 3862, 3542, 4812};

        // Fetch data for all stop IDs
        for (int stopId : stopIds) {
            fetchStopData(apiInterface, stopId, accessToken);
        }

        busqueda(searchBar);

        return view;
    }

    private void busqueda(SearchBar searchBar){


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
                                stopsList.addAll(stops);
                            }
                        }
                        adapter.notifyDataSetChanged();
                        Log.d("JustWorking", "Stops loaded: " + stopsList);
                    } else {
                        Log.d("API Response", "No stops data for stop ID: " + stopId);
                    }
                } else {
                    Log.d("API Response", "Failed response for stop ID: " + stopId + ", Response: " + response);
                }

                hideLoadingIndicator(); // Hide loading indicator after all calls
            }

            @Override
            public void onFailure(@NonNull Call<StopModel> call, @NonNull Throwable t) {
                Log.e("Call Error", "Error retrieving data for stop ID: " + stopId, t);
                hideLoadingIndicator(); // Hide loading indicator if it fails
            }
        });
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
