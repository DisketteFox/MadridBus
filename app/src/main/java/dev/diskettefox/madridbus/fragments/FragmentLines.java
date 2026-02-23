package dev.diskettefox.madridbus.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.loadingindicator.LoadingIndicator;
import com.google.android.material.search.SearchBar;

import java.util.ArrayList;

import dev.diskettefox.madridbus.R;
import dev.diskettefox.madridbus.adapters.LineAdapter;
import dev.diskettefox.madridbus.api.ApiCall;
import dev.diskettefox.madridbus.api.ApiInterface;
import dev.diskettefox.madridbus.models.HelloModel;
import dev.diskettefox.madridbus.models.LineModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentLines extends Fragment {
    private static final ArrayList<LineModel.Data> lineData = new ArrayList<>();
    private ArrayList<LineModel.Data> filteredList = new ArrayList<>();
    private LineAdapter adapter;
    private LoadingIndicator loadingIndicator;
    private LinearLayout noConnection;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_lines, container, false);
        RecyclerView recyclerLines = view.findViewById(R.id.recycler_lines);
        loadingIndicator = view.findViewById(R.id.progress_bar);
        noConnection = view.findViewById(R.id.no_connection);

        ApiInterface apiInterface = ApiCall.callApi().create(ApiInterface.class);
        String accessToken = ApiCall.token;

        // Initialize RecyclerView and Adapter
        recyclerLines.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new LineAdapter(getContext(), lineData);
        recyclerLines.setAdapter(adapter);

        // Show loading screen only if data is not yet loaded
        if (lineData.isEmpty()) {
            loadingIndicator.setVisibility(View.VISIBLE);
            // Fetch data for all lines
            fetchLinesData(apiInterface, accessToken);
        } else {
            loadingIndicator.setVisibility(View.GONE);
        }

        // Check for connection to API
        checkPing(apiInterface);

        // Homemade searchbar
        SearchBar searchBar = view.findViewById(R.id.search_bar_Lines);
        EditText editText = view.findViewById(R.id.lineET);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {filteredList.clear();}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchBar.setText(s);

                for (LineModel.Data data: lineData){
                    if (data.getLabel().toLowerCase().contains(s)){
                        filteredList.add(data);
                    }
                }
                recyclerLines.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter = new LineAdapter(getContext(), filteredList);
                recyclerLines.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
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
                lineData.clear();
                if (getContext() != null) {
                    Toast.makeText(getContext(), R.string.no_connection, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fetchLinesData(ApiInterface apiInterface, String accessToken) {
        Call<LineModel> call = apiInterface.getLines(accessToken);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<LineModel> call, @NonNull Response<LineModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LineModel line = response.body();
                    lineData.clear();
                    lineData.addAll(line.getData());
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }

                } else {
                    // Not intended to be  visible for user
                    Log.e("API Error", "Unable to connect to database");
                    showNoConnection();
                }
                hideLoadingIndicator(); // Hide loading indicator after all calls
            }

            @Override
            public void onFailure(@NonNull Call<LineModel> call, @NonNull Throwable t) {
                // Not intended to be  visible for user
                Log.e("Call Error", "Error retrieving lines",t);
                hideLoadingIndicator(); // Hide loading indicator if it fails
            }
        });
    }

    private void hideLoadingIndicator() {
        if (loadingIndicator != null) {
            loadingIndicator.setVisibility(View.GONE);
        }
    }
    private void showLoadingIndicator() {
        if (loadingIndicator != null) {
            loadingIndicator.setVisibility(View.VISIBLE);
        }
    }
    private void showNoConnection() {
        if (noConnection != null) {
            noConnection.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}