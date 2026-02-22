package dev.diskettefox.madridbus;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import dev.diskettefox.madridbus.adapters.LineActivityAdapter;
import dev.diskettefox.madridbus.adapters.LinePagerAdapter;
import dev.diskettefox.madridbus.api.ApiCall;
import dev.diskettefox.madridbus.api.ApiInterface;
import dev.diskettefox.madridbus.models.LineModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LineActivity extends AppCompatActivity {
    private LineActivityAdapter adapter;
    private final ArrayList<LineModel.Data> data = new ArrayList<>();

    private TextView linealabel, destinoA, destinoB;
    private ViewPager viewPager;
    private TabLayout tabLayout;

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
        Log.d("LineActivity", "Line ID: " + lineId);

        String lineaLabel = getIntent().getStringExtra("lineLabel");
        String nameA = getIntent().getStringExtra("dsA");
        String nameB = getIntent().getStringExtra("dsB");

        linealabel = findViewById(R.id.line_label);
        destinoA = findViewById(R.id.destino_A_Line);
        destinoB = findViewById(R.id.destino_B_Line);
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);

        // Data assigned
        applyColoring(linealabel);
        linealabel.setText(lineaLabel);
        destinoA.setText(nameA);
        destinoB.setText(nameB);

        setupViewPager(lineaLabel);

        // Initialize RecyclerView
        adapter = new LineActivityAdapter(this, data);

        if (lineId != -1) {
            try {
                fetchLineDetails(apiInterface, lineId);
            } catch (NumberFormatException e) {
                Log.e("StopActivity", "Invalid line ID format", e);
                Toast.makeText(getBaseContext(), R.string.invalid_line, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setupViewPager(String lineLabel) {
        List<String> imageUrls = new ArrayList<>();
        // Urls
        imageUrls.add("https://www.madridbuses.com/img/autobuses-emt/horario-ida-linea-" + lineLabel + "-madrid-completo.png");
        imageUrls.add("https://www.madridbuses.com/img/autobuses-emt/horario-vuelta-linea-" + lineLabel + "-madrid-completo.png");

        LinePagerAdapter pagerAdapter = new LinePagerAdapter(this, imageUrls);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void applyColoring(TextView line) {
        String text = getIntent().getStringExtra("lineColor");
        GradientDrawable bf = (GradientDrawable) line.getBackground();

        if (text == null) {
            bf.setColor(ContextCompat.getColor(getBaseContext(), R.color.blue));
            return;
        }

        switch (text) {
            case "black":
                bf.setColor(ContextCompat.getColor(getBaseContext(), R.color.black));
                line.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.gold));
                break;
            case "gold":
                bf.setColor(ContextCompat.getColor(getBaseContext(), R.color.gold));
                line.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.black));
                break;
            case "turquoise":
                bf.setColor(ContextCompat.getColor(getBaseContext(), R.color.turquoise));
                break;
            case "lightblue":
                bf.setColor(ContextCompat.getColor(getBaseContext(), R.color.lightblue));
                break;
            case "brown":
                bf.setColor(ContextCompat.getColor(getBaseContext(), R.color.brown));
                break;
            case "yellow":
                bf.setColor(ContextCompat.getColor(getBaseContext(), R.color.yellow));
                line.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.black));
                break;
            default:
                bf.setColor(ContextCompat.getColor(getBaseContext(), R.color.blue));
                break;
        }
    }

    private void fetchLineDetails(ApiInterface apiInterface, int lineId) {
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
                        Log.e("API Response", "No stops data for line ID: " + lineId);
                        Toast.makeText(getBaseContext(), R.string.error_lines, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("API Response", "Failed response for line ID: " + lineId + ", Response: " + response);
                    Toast.makeText(getBaseContext(), R.string.error_lines, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LineModel> call, Throwable t) {
                Log.e("StopActivity", "Error fetching line details", t);
                Toast.makeText(getBaseContext(), R.string.error_lines, Toast.LENGTH_SHORT).show();
            }
        });
    }

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
