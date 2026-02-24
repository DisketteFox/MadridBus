package dev.diskettefox.madridbus.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.loadingindicator.LoadingIndicator;
import com.google.android.material.search.SearchView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.diskettefox.madridbus.LoginActivity;
import dev.diskettefox.madridbus.R;
import dev.diskettefox.madridbus.StopActivity;
import dev.diskettefox.madridbus.adapters.StopAdapter;
import dev.diskettefox.madridbus.api.ApiCall;
import dev.diskettefox.madridbus.api.ApiInterface;
import dev.diskettefox.madridbus.models.HelloModel;
import dev.diskettefox.madridbus.models.StopModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentStop extends Fragment {
    private static final ArrayList<StopModel.Stop> stopsList = new ArrayList<>();
    private StopAdapter adapter;
    private LoadingIndicator loadingIndicator;
    private LinearLayout noFavorites, noConnection;

    // private final int[] stopIds = {};
    private final int[] stopIds = {5710, 3862, 3542, 4812, 666};
    private static int responsesReceived = 0;
    private final Map<Integer, Integer> stopIdToIndex = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_stops, container, false);
        RecyclerView recyclerStops = view.findViewById(R.id.recycler_stops);
        loadingIndicator = view.findViewById(R.id.progress_bar);
        noFavorites = view.findViewById(R.id.no_favorites);
        noConnection = view.findViewById(R.id.no_connection);

        MaterialToolbar toolbar = view.findViewById(R.id.my_toolbar);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.token) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });

        ApiInterface apiInterface = ApiCall.callApi().create(ApiInterface.class);
        String accessToken = ApiCall.token;

        // Initialize RecyclerView and Adapter
        recyclerStops.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new StopAdapter(getContext(), stopsList);
        recyclerStops.setAdapter(adapter);

        // Show loading screen
        if (stopIds.length != 0) {
            if (stopsList.isEmpty()) {
                showLoadingIndicator();
            } else {
                hideLoadingIndicator();
            }
        } else {
            showNoFavorites();
        }

        // Populate the map for sorting
        for (int i = 0; i < stopIds.length; i++) {
            stopIdToIndex.put(stopIds[i], i);
        }

        // Check for connection to API
        checkPing(apiInterface);

        // Fetch data for all stop IDs
        if (stopsList.isEmpty()) {
            responsesReceived = 0;
            for (int stopId : stopIds) {
                fetchStopData(apiInterface, stopId, accessToken);
            }
        } else {
            adapter.notifyDataSetChanged();
        }

        // Search bar
        SearchView searchView = view.findViewById(R.id.search_view_Stops);

        // Handle Search action on keyboard
        searchView.getEditText().setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                String text = v.getText().toString();
                if (!text.isEmpty()) {
                    try {
                        int searchStopId = Integer.parseInt(text);
                        Log.d("Search", "Search button clicked for stop ID: " + searchStopId);

                        Context context = getContext();
                        if (context != null) {
                            Intent intent = new Intent(context, StopActivity.class);
                            intent.putExtra("stopId", String.valueOf(searchStopId));
                            context.startActivity(intent);
                        }

                        searchView.hide();
                    } catch (NumberFormatException e) {
                        if (getContext() != null) {
                            Toast.makeText(getContext(), "Invalid Stop ID", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                return true;
            }
            return false;
        });
        return view;
    }

    private void checkPing(ApiInterface apiInterface){
        Call<HelloModel> call = apiInterface.getHello();
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<HelloModel> call, @NonNull Response<HelloModel> response) {}
            @Override
            public void onFailure(@NonNull Call<HelloModel> call, @NonNull Throwable t) {
                hideLoadingIndicator();
                showNoConnection();
                if (getContext() != null) {
                    Toast.makeText(getContext(), R.string.no_connection, Toast.LENGTH_SHORT).show();
                }
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
                        // Not intended to be  visible for user
                        Log.e("API Response", "No stops data for stop ID: " + stopId);
                        showNoConnection();
                    }
                } else {
                    // Not intended to be  visible for user
                    Log.e("API Response", "Failed response for stop ID: " + stopId + ", Response: " + response);
                    showNoConnection();
                }
                onResponseReceived();
            }

            @Override
            public void onFailure(@NonNull Call<StopModel> call, @NonNull Throwable t) {
                //No connection
                Log.e("Call Error", "Unable to connect to EMT API", t);
                showNoConnection();
            }
        });
    }

    private void onResponseReceived() {
        responsesReceived++;
        if (responsesReceived == stopIds.length) {
            // Sorting list
            synchronized (stopsList) {
                stopsList.sort(Comparator.comparing(stop -> stopIdToIndex.get(Integer.parseInt(stop.getStopId()))));
            }
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
            hideLoadingIndicator();
        }
    }

    // Visibility changes
    private void hideLoadingIndicator() {
        loadingIndicator.setVisibility(View.GONE);
    }
    private void showLoadingIndicator() {
        loadingIndicator.setVisibility(View.VISIBLE);
    }
    private void showNoFavorites() {
        noFavorites.setVisibility(View.VISIBLE);
    }
    private void showNoConnection() {
        noConnection.setVisibility(View.VISIBLE);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}