package dev.diskettefox.madridbus;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import dev.diskettefox.madridbus.adapters.BusTimeAdapter;
import dev.diskettefox.madridbus.api.ApiCall;
import dev.diskettefox.madridbus.api.ApiInterface;
import dev.diskettefox.madridbus.api.StopModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StopActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BusTimeAdapter adapter;
    private List<StopModel.Dataline> linesList = new ArrayList<>();
    private TextView stopIdTextView;
    private TextView stopNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_stop);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        String stopId = getIntent().getStringExtra("stopId");

        stopIdTextView = findViewById(R.id.stop_id);
        stopNameTextView = findViewById(R.id.stop_name);

        if (stopId != null) {
            stopIdTextView.setText(stopId);
        }

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_stop);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BusTimeAdapter(this, linesList);
        recyclerView.setAdapter(adapter);

        if (stopId != null && !stopId.isEmpty()) {
            try {
                fetchStopDetails(Integer.parseInt(stopId));
            } catch (NumberFormatException e) {
                Log.e("StopActivity", "Invalid stop ID format", e);
            }
        }
    }

    private void fetchStopDetails(int stopId) {
        ApiInterface apiInterface = ApiCall.getStop().create(ApiInterface.class);
        Call<StopModel> call = apiInterface.getStop(stopId, ApiCall.token);

        call.enqueue(new Callback<StopModel>() {
            @Override
            public void onResponse(Call<StopModel> call, Response<StopModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    StopModel stopModel = response.body();
                    if (stopModel.getStopsData() != null && !stopModel.getStopsData().isEmpty()) {
                        List<StopModel.Stop> stops = stopModel.getStopsData().get(0).getStops();
                        if (stops != null && !stops.isEmpty()) {
                            StopModel.Stop stop = stops.get(0);

                            // Update UI with stop details from API
                            if (stop.getName() != null) {
                                stopNameTextView.setText(stop.getName());
                            }
                            if (stop.getStopId() != null) {
                                stopIdTextView.setText(stop.getStopId());
                            }

                            linesList.clear();
                            List<StopModel.Dataline> dataLines = stop.getDataLine();
                            if (dataLines != null) {
                                linesList.addAll(dataLines);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<StopModel> call, Throwable t) {
                Log.e("StopActivity", "Error fetching stop details", t);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
