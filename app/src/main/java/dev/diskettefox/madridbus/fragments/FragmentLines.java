package dev.diskettefox.madridbus.fragments;

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

import com.google.android.material.loadingindicator.LoadingIndicator;

import java.util.ArrayList;

import dev.diskettefox.madridbus.R;
import dev.diskettefox.madridbus.adapters.LineAdapter;
import dev.diskettefox.madridbus.api.ApiCall;
import dev.diskettefox.madridbus.api.ApiInterface;
import dev.diskettefox.madridbus.models.LineModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentLines extends Fragment {
    private final ArrayList<LineModel.Data> lineData = new ArrayList<>();
    private LineAdapter adapter;
    private LoadingIndicator loadingIndicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_lines, container, false);
        RecyclerView recyclerLines = view.findViewById(R.id.recycler_lines);
        loadingIndicator = view.findViewById(R.id.progress_bar);

        ApiInterface apiInterface = ApiCall.callApi().create(ApiInterface.class);
        String accessToken = ApiCall.token;

        // Initialize RecyclerView and Adapter
        recyclerLines.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new LineAdapter(getContext(), lineData);
        recyclerLines.setAdapter(adapter);

        // Show loading screen
        loadingIndicator.setVisibility(View.VISIBLE);

        // se llama a la API y rellena con todas las lineas.
        llamaAlaslineas(apiInterface, accessToken);

        return view;
    }

    private void fetchStopData(ApiInterface apiInterface, int lineId, String accessToken) {
        Call<LineModel> call = apiInterface.getLineDetail(lineId, accessToken);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<LineModel> call, @NonNull Response<LineModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LineModel line = response.body();
                    // Process the response
                    if (line.getData() != null) {
                        lineData.addAll(line.getData());
                        adapter.notifyDataSetChanged();
                        Log.d("JustWorking", "Stops loaded");
                    } else {
                        Log.d("API Response", "No stops data for stop ID: " + lineId);
                    }
                } else {
                    Log.d("API Response", "Failed response for stop ID: " + lineId + ", Response: " + response);
                }
                hideLoadingIndicator(); // Hide loading indicator after all calls
            }

            @Override
            public void onFailure(@NonNull Call<LineModel> call, @NonNull Throwable t) {
                Log.e("Call Error", "Error retrieving data for stop ID: " + lineId, t);
                hideLoadingIndicator(); // Hide loading indicator if it fails
            }
        });
    }

    private void llamaAlaslineas(ApiInterface apiInterface, String accessToken) {
        Call<LineModel> call = apiInterface.getLines( accessToken);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<LineModel> call, @NonNull Response<LineModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LineModel line = response.body();
                    lineData.addAll(line.getData());
                    adapter.notifyDataSetChanged();

                } else {
                    Log.d("API Response", "fallo en la comunicación.");
                }
                hideLoadingIndicator(); // Hide loading indicator after all calls
            }

            @Override
            public void onFailure(@NonNull Call<LineModel> call, @NonNull Throwable t) {
                Log.e("Call Error", "Error recibiendo los datos ",t);
                hideLoadingIndicator(); // Hide loading indicator if it fails
            }
        });
    }

    private void hideLoadingIndicator() {
        loadingIndicator.setVisibility(View.GONE);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}