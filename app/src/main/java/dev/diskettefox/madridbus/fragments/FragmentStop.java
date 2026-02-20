package dev.diskettefox.madridbus.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.loadingindicator.LoadingIndicator;
import com.google.android.material.search.SearchBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.diskettefox.madridbus.R;
import dev.diskettefox.madridbus.StopActivity;
import dev.diskettefox.madridbus.adapters.StopAdapter;
import dev.diskettefox.madridbus.api.ApiCall;
import dev.diskettefox.madridbus.api.ApiInterface;
import dev.diskettefox.madridbus.api.BaseDatosCall;
import dev.diskettefox.madridbus.api.BaseDatosInterface;
import dev.diskettefox.madridbus.api.BaseDatosModel;
import dev.diskettefox.madridbus.models.StopModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentStop extends Fragment {
    private final ArrayList<StopModel.Stop> stopsList = new ArrayList<>();
    private final ArrayList<StopModel.Stop> stopFilter = new ArrayList<>();
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

        // Search bar
        SearchBar searchBar = view.findViewById(R.id.search_bar_Stops);
        EditText editText = view.findViewById(R.id.stopsET);

        // Handle Search action on keyboard
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                    int searchStopId = Integer.parseInt(v.getText().toString());
                    Log.d("Search", "Search button clicked for stop ID: " + searchStopId);
                    if (searchStopId < 51182) {
                        Intent intent = new Intent(getContext(), StopActivity.class);
                        intent.putExtra("stopId", String.valueOf(searchStopId));
                        getContext().startActivity(intent);
                    } else {
                        Log.d("Search Error", "Invalid stop ID: " + searchStopId);
                    }
                    return true;
                }
                return false;
            }
        });

        // Filter logic
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {stopFilter.clear();}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchBar.setText(s);

                for (StopModel.Stop stop: stopsList){
                    if (stop.getStopId().contains(s)){
                        stopFilter.add(stop);
                    }
                }
                recyclerStops.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter = new StopAdapter(getContext(), stopFilter);
                recyclerStops.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

        testeoBBDD();

        return view;
    }


    private void testeoBBDD(){
        BaseDatosInterface baseDatosInterface= BaseDatosCall.getBBDD().create(BaseDatosInterface.class);
        BaseDatosModel modelo=new BaseDatosModel("5710",true);
        Call<BaseDatosModel> callB= baseDatosInterface.anadeFavorito(modelo);
        callB.enqueue(new Callback<BaseDatosModel>() {
            @Override
            public void onResponse(Call<BaseDatosModel> call, Response<BaseDatosModel> response) {
                if (response.isSuccessful() && response.body()!=null){
                    Log.d("llamado exitoso", "SE HA CREADO ALGO EN LA BASE DE DATOS.: ");
                }
            }

            @Override
            public void onFailure(Call<BaseDatosModel> call, Throwable t) {
                Log.e("Call Error", "Error retrieving data for stop ID: ", t);
                onResponseReceived();
            }
        });
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
