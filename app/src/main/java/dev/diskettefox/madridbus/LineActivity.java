package dev.diskettefox.madridbus;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

import dev.diskettefox.madridbus.adapters.LineActivityAdapter;
import dev.diskettefox.madridbus.api.ApiCall;
import dev.diskettefox.madridbus.api.ApiInterface;
import dev.diskettefox.madridbus.models.LineModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LineActivity extends AppCompatActivity {
    private LineActivityAdapter adapter;
    private final ArrayList<LineModel.Data> data = new ArrayList<>();

    private TextView linealabel,destinoA,destinoB;

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
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }

        int lineId = getIntent().getIntExtra("lineId", -1);
        Log.e("LineActivity", "Line ID: " + lineId);

        // recuperación de datos del bundle e inicialización de algunos elementos
        String lineaLabel = getIntent().getStringExtra("lineLabel");
        String nameA = getIntent().getStringExtra("dsA");
        String nameB = getIntent().getStringExtra("dsB");
        linealabel =findViewById(R.id.line_label);
        destinoA =findViewById(R.id.destino_A_Line);
        destinoB =findViewById(R.id.destino_B_Line);

        // asignación de datos
        coloreaLinea(linealabel);
        linealabel.setText(lineaLabel);
        destinoA.setText(nameA);
        destinoB.setText(nameB);


        // Initialize RecyclerView
        adapter = new LineActivityAdapter(this, data);

        if (lineId != -1) {
            try {
                fetchStopDetails(apiInterface, lineId);
            } catch (NumberFormatException e) {
                Log.d("StopActivity", "Invalid stop ID format", e);
            }
        }
    }

    private void coloreaLinea(TextView linea){
        String texto=getIntent().getStringExtra("lineColor");
        GradientDrawable fondo= (GradientDrawable) linea.getBackground();

        if (texto.equals("black")) {
            fondo.setColor(ContextCompat.getColor(getBaseContext(), R.color.black));
            linea.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.gold));
        } else if (texto.equals("gold")){
            fondo.setColor(ContextCompat.getColor(getBaseContext(), R.color.gold));
            linea.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.black));
        } else if (texto.equals("turquoise")) {
            fondo.setColor(ContextCompat.getColor(getBaseContext(), R.color.turquoise));
        } else if (texto.equals("lightblue")) {
            fondo.setColor(ContextCompat.getColor(getBaseContext(), R.color.lightblue));
        } else if (texto.equals("brown")) {
            fondo.setColor(ContextCompat.getColor(getBaseContext(), R.color.brown));
        } else if (texto.equals("yellow")) {
            fondo.setColor(ContextCompat.getColor(getBaseContext(), R.color.yellow));
            linea.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.black));
        } else {
            fondo.setColor(ContextCompat.getColor(getBaseContext(), R.color.blue));
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

    // Esto hace que funcione el botón de regresar.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}