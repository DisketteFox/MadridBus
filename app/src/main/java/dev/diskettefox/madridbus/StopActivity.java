package dev.diskettefox.madridbus;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.loadingindicator.LoadingIndicator;

import java.util.ArrayList;
import java.util.List;

import dev.diskettefox.madridbus.adapters.StopActivityAdapter;
import dev.diskettefox.madridbus.api.ApiCall;
import dev.diskettefox.madridbus.api.ApiInterface;
import dev.diskettefox.madridbus.api.BaseDatosCall;
import dev.diskettefox.madridbus.api.BaseDatosInterface;
import dev.diskettefox.madridbus.api.BaseDatosModel;
import dev.diskettefox.madridbus.api.TimeRequest;
import dev.diskettefox.madridbus.models.StopModel;
import dev.diskettefox.madridbus.models.TimeModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StopActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StopActivityAdapter adapter;
    private final List<StopModel.Dataline> linesList = new ArrayList<>();
    private TextView stopIdTextView;
    private TextView stopNameTextView;
    private LoadingIndicator loadingIndicator;
    private CardView stopCard;
    private MaterialToolbar materialToolbar;
    private int timesResponsesReceived = 0;
    private StopModel.Stop parada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_stop);

        loadingIndicator = findViewById(R.id.progress_bar);
        stopCard = findViewById(R.id.busCard_Stop);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }

        String stopId = getIntent().getStringExtra("stopId");
        stopCard.setVisibility(View.GONE);
        loadingIndicator.setVisibility(View.VISIBLE);

        stopIdTextView = findViewById(R.id.stop_id);
        stopNameTextView = findViewById(R.id.stop_name);

        if (stopId != null) {
            stopIdTextView.setText(stopId);
        }

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_stop);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StopActivityAdapter(this, linesList);
        recyclerView.setAdapter(adapter);

        if (stopId != null && !stopId.isEmpty()) {
            try {
                fetchStopDetails(Integer.parseInt(stopId));
            } catch (NumberFormatException e) {
                Log.e("StopActivity", "Invalid stop ID format", e);
                Toast.makeText(getBaseContext(), R.string.invalid_stop, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void fetchStopDetails(int stopId) {
        ApiInterface apiInterface = ApiCall.callApi().create(ApiInterface.class);
        Call<StopModel> call = apiInterface.getStop(stopId, ApiCall.token);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<StopModel> call, @NonNull Response<StopModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    StopModel stopModel = response.body();

                    // Check if the response returns an error
                    if (stopModel.getCode().equals("80")) {
                        Toast.makeText(getBaseContext(), R.string.error_token, Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (stopModel.getCode().equals("81")) {
                        Toast.makeText(getBaseContext(), R.string.stop_error, Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (stopModel.getCode().equals("90")) {
                        Toast.makeText(getBaseContext(), R.string.invalid_stop, Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    if (stopModel.getStopsData() != null && !stopModel.getStopsData().isEmpty()) {
                        List<StopModel.Stop> stops = stopModel.getStopsData().get(0).getStops();
                        if (stops != null && !stops.isEmpty()) {
                            StopModel.Stop stop = stops.get(0);

                            if (stop.getName() != null) {
                                stopNameTextView.setText(stop.getName());
                            }
                            if (stop.getStopId() != null) {
                                stopIdTextView.setText(stop.getStopId());
                            }
                            stop.setFavorite(false);
                            parada=stop;
                            linesList.clear();
                            List<StopModel.Dataline> dataLines = stop.getDataLine();
                            if (dataLines != null && !dataLines.isEmpty()) {
                                linesList.addAll(dataLines);
                                fetchArrivalTimes(stopId, apiInterface);
                                return;
                            }
                        }
                    }
                }
                loadingIndicator.setVisibility(View.GONE);
                stopCard.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(@NonNull Call<StopModel> call, @NonNull Throwable t) {
                Log.e("StopActivity", "Error fetching stop details", t);
                Toast.makeText(getBaseContext(), R.string.stop_error, Toast.LENGTH_SHORT).show();
                loadingIndicator.setVisibility(View.GONE);
            }
        });

        materialToolbar = findViewById(R.id.my_toolbar);
    }

    private void fetchArrivalTimes(int stopId, ApiInterface apiInterface) {
        timesResponsesReceived = 0;
        for (int i = 0; i < linesList.size(); i++) {
            StopModel.Dataline line = linesList.get(i);
            try {
                int lineId = Integer.parseInt(line.getLineId());
                Call<TimeModel> timeCall = apiInterface.getTime(stopId, lineId, ApiCall.token, TimeRequest.get());
                
                Log.d("StopActivity", "Fetching time for line: " + lineId + " at stop: " + stopId);

                timeCall.enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<TimeModel> call, @NonNull Response<TimeModel> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            TimeModel timeModel = response.body();
                            if (timeModel.getData() != null && !timeModel.getData().isEmpty()) {
                                List<TimeModel.Line> times = timeModel.getData().get(0).getLines();
                                if (times != null && !times.isEmpty()) {
                                    line.setTimeArriving(formatTime(times.get(0).getTime()));
                                    if (times.size() > 1) {
                                        line.setTimeNext(formatTime(times.get(1).getTime()));
                                    } else {
                                        line.setTimeNext("---");
                                    }
                                }
                            }
                        } else {
                            Log.e("StopActivity", "Time response not successful for line " + line.getLabel());
                        }
                        checkAllTimesReceived();
                    }

                    @Override
                    public void onFailure(@NonNull Call<TimeModel> call, @NonNull Throwable t) {
                        Log.e("StopActivity", "Error fetching times for line " + line.getLabel(), t);
                        Toast.makeText(getBaseContext(), R.string.error_time, Toast.LENGTH_SHORT).show();
                        checkAllTimesReceived();
                    }
                });
            } catch (NumberFormatException e) {
                Log.e("StopActivity", "Invalid line ID: " + line.getLineId());
                Toast.makeText(getBaseContext(), R.string.invalid_line, Toast.LENGTH_SHORT).show();
                checkAllTimesReceived();
            }
        }
    }

    private void checkAllTimesReceived() {
        timesResponsesReceived++;
        if (timesResponsesReceived == linesList.size()) {
            adapter.notifyDataSetChanged();
            loadingIndicator.setVisibility(View.GONE);
            stopCard.setVisibility(View.VISIBLE);
        }
    }

    private String formatTime(String timeInSeconds) {
        try {
            int seconds = Integer.parseInt(timeInSeconds);
            if (seconds >= 999999) return "Delayed";
            if (seconds < 60) return "Arriving";
            return (seconds / 60) + " min";
        } catch (NumberFormatException e) {
            return timeInSeconds;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_stop, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
            return true;
        } else if (itemId == R.id.update) {
            String stopId = getIntent().getStringExtra("stopId");
            if (stopId != null && !stopId.isEmpty()) {
                try {
                    stopCard.setVisibility(View.GONE);
                    loadingIndicator.setVisibility(View.VISIBLE);
                    linesList.clear();
                    adapter.notifyDataSetChanged();
                    fetchStopDetails(Integer.parseInt(stopId));
                } catch (NumberFormatException e) {
                    Log.e("StopActivity", "Error while refreshing", e);
                    Toast.makeText(getBaseContext(), R.string.error_refreshing, Toast.LENGTH_SHORT).show();
                }
            }
            return true;
        } else if (itemId == R.id.favorite) {
            // Falta que el icono haga algo cuando es pulsado
            if (!parada.isFavorite()){
                parada.setFavorite(true);
                item.setIcon(R.drawable.ic_favorite_filled_24dp);
                //addFavoriteBBDD(parada.getStopId(),parada.isFavorite());
                Log.d("StopActivity", "Favorite Stops is active clicked");
            }else{
                Log.d("StopActivity2", "Favorite Stops is inactive clicked Detele");
                parada.setFavorite(false);
                item.setIcon(R.drawable.ic_favorite_24dp);
                //removeFavoriteBBDD(parada.getStopId(),parada.isFavorite());
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void addFavoriteBBDD(String paradaid,Boolean estado){
        BaseDatosInterface baseDatosInterface= BaseDatosCall.getBBDD().create(BaseDatosInterface.class);
        BaseDatosModel modelo=new BaseDatosModel(paradaid,estado);
        Call<BaseDatosModel> callB= baseDatosInterface.anadeFavorito(modelo);
        callB.enqueue(new Callback<BaseDatosModel>() {
            @Override
            public void onResponse(Call<BaseDatosModel> call, Response<BaseDatosModel> response) {
                if (response.isSuccessful() && response.body()!=null){
                    Log.d("Exit call", "Succefully added to favorites");
                    Toast.makeText(getBaseContext(), R.string.message_favorite_add, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<BaseDatosModel> call, Throwable t) {
                Log.e("Call Error", "Error retrieving data for BBDD.", t);
                Toast.makeText(getBaseContext(), R.string.database_error, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void removeFavoriteBBDD(String paradaid,Boolean estado){
        BaseDatosInterface baseDatosInterface= BaseDatosCall.getBBDD().create(BaseDatosInterface.class);
        BaseDatosModel modelo=new BaseDatosModel(paradaid,estado);
        Call<BaseDatosModel> callB= baseDatosInterface.elimidaDFavoritos(modelo);
        callB.enqueue(new Callback<BaseDatosModel>() {
            @Override
            public void onResponse(Call<BaseDatosModel> call, Response<BaseDatosModel> response) {
                if (response.isSuccessful() && response.body()!=null){
                    Log.d("Exit call", "Succesfully removed from favorites");
                    Toast.makeText(getBaseContext(), R.string.message_favorite_remove, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<BaseDatosModel> call, Throwable t) {
                Log.e("Call Error", "Error retrieving data for BBDD.", t);
                Toast.makeText(getBaseContext(), R.string.database_error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
