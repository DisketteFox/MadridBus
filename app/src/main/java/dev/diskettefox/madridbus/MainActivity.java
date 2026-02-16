package dev.diskettefox.madridbus;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import dev.diskettefox.madridbus.Api_requests.ApiCall;
import dev.diskettefox.madridbus.Api_requests.ApiInterface;
import dev.diskettefox.madridbus.Api_requests.Modelo_parada;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    ArrayList<Modelo_parada.Parada> listaDparadas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView recyclerStops = findViewById(R.id.recycler_stops);
        ApiInterface apiInterface = ApiCall.getStop().create(ApiInterface.class);

        BottomNavigationView navigationBarView = findViewById(R.id.bottom_navigation);
        FrameLayout frameLayout = findViewById(R.id.framelayout_main);
        cargaFragment(new FragmentMain(), true);

        // navegation bar login esto es solo pa que cambie de fragment
        navigationBarView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.menu_stops) {
                    cargaFragment(new FragmentMain(), false);
                } else if (itemId == R.id.menu_lines) {
                    cargaFragment(new FragmentLines(), false);
                } else if (itemId == R.id.menu_maps) {
                    cargaFragment(new FragmentMap(), false);
                }
                return true;
            }
        });


        Call<Modelo_parada> call = apiInterface.getAllParadas("da0a4f54-aaa7-4f6f-b2e7-155d1ce0957d");
        call.enqueue(new Callback<Modelo_parada>() {
            @Override
            public void onResponse(Call<Modelo_parada> call, Response<Modelo_parada> response) {
                Modelo_parada parada = response.body();
                listaDparadas.addAll(parada.getParadas());

                Log.d("cosa rara", listaDparadas.toString());
                BusAdapter adapter = new BusAdapter(MainActivity.this, listaDparadas);

                recyclerStops.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<Modelo_parada> call, Throwable t) {
                if (listaDparadas.isEmpty()) {
                    Log.d("mensaje de error", "Lo sentimos pero hubo un error inesperado.... Paguina caida :(");
                }
                Log.d("Error! llamada fallida.", t.toString());
            }
        });


    }

    public void cargaFragment(Fragment fragment, boolean seInicioLaApp) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (seInicioLaApp) {
            fragmentTransaction.add(R.id.framelayout_main, fragment);
        } else {
            fragmentTransaction.replace(R.id.framelayout_main, fragment);
        }
        fragmentTransaction.commit();
    }


}