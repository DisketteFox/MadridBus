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
import dev.diskettefox.madridbus.adapters.LineAdapter;
import dev.diskettefox.madridbus.api.ApiCall;
import dev.diskettefox.madridbus.api.ApiInterface;
import dev.diskettefox.madridbus.api.LineModel;
import dev.diskettefox.madridbus.api.StopModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LineActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LineAdapter adapter;
    private List<LineModel.Data> data = new ArrayList<>();
    private TextView tvLineLabel, tvLineNameA, tvLineNameB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_line);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        int lineId = getIntent().getIntExtra("lineId", -1);
        Log.e("LineActivity", "Line ID: " + lineId);

        tvLineLabel = findViewById(R.id.line_label);
        tvLineNameA = findViewById(R.id.line_name_a);
        tvLineNameB = findViewById(R.id.line_name_b);

        if (lineId != -1) {
            tvLineLabel.setText(String.valueOf(lineId));
        }


    }

    private void fetchStopDetails(int lineId) {
        ApiInterface apiInterface = ApiCall.callApi().create(ApiInterface.class);
        Call<LineModel> call = apiInterface.getLineDetail(lineId, ApiCall.token);

        call.enqueue(new Callback<LineModel>() {
            @Override
            public void onResponse(Call<LineModel> call, Response<LineModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LineModel lineModel = response.body();
                    if (lineModel.getData() != null && !lineModel.getData().isEmpty()) {
                        ArrayList<LineModel.Data> linesList = lineModel.getData();

                        // Update UI with stop details from API
                        if (data.get(0).getLabel() != null) {
                            tvLineLabel.setText(data.get(0).getLabel());
                        }
                        if (data.get(0).getNameA() != null) {
                            tvLineNameA.setText(data.get(0).getNameA());
                        }
                        if (data.get(0).getNameB() != null) {
                            tvLineNameA.setText(data.get(0).getNameB());
                        }

                        linesList.clear();
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<LineModel> call, Throwable t) {
                Log.e("StopActivity", "Error fetching stop details", t);
            }
        });
        }
}
