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

import java.util.ArrayList;
import java.util.List;

import dev.diskettefox.madridbus.adapters.BusTimeAdapter;
import dev.diskettefox.madridbus.adapters.LineActivityAdapter;
import dev.diskettefox.madridbus.adapters.LineAdapter;
import dev.diskettefox.madridbus.api.ApiCall;
import dev.diskettefox.madridbus.api.ApiInterface;
import dev.diskettefox.madridbus.api.LineModel;
import dev.diskettefox.madridbus.api.StopModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LineActivity extends AppCompatActivity {
    private LineActivityAdapter adapter;
    private final ArrayList<LineModel.Data> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_line);
        ApiInterface apiInterface = ApiCall.callApi().create(ApiInterface.class);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        int lineId = getIntent().getIntExtra("lineId", -1);
        Log.e("LineActivity", "Line ID: " + lineId);

        // Initialize RecyclerView
        adapter = new LineActivityAdapter(this, data);

        if (lineId != -1) {
            try {
                fetchStopDetails(apiInterface, lineId);
            } catch (NumberFormatException e) {
                Log.e("StopActivity", "Invalid stop ID format", e);
            }
        }
    }

    private void fetchStopDetails(ApiInterface apiInterface, int lineId) {
        Call<LineModel> call = apiInterface.getLineDetail(lineId, ApiCall.token);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<LineModel> call, @NonNull Response<LineModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LineModel line = response.body();

                    // Get data
                    if (line.getData() != null) {
                        data.addAll(line.getData());
                        adapter.notifyDataSetChanged();
                        Log.d("JustWorking", "Line loaded");
                    } else {
                        Log.d("API Response", "No stops data for line ID: " + lineId);
                    }
                } else {
                    Log.d("API Response", "Failed response for line ID: " + lineId + ", Response: " + response);
                }
            }

            @Override
            public void onFailure(Call<LineModel> call, Throwable t) {
                Log.e("StopActivity", "Error fetching line details", t);
            }
        });
        }
}
