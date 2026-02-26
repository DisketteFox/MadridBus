package dev.diskettefox.madridbus;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import dev.diskettefox.madridbus.api.ApiCall;
import dev.diskettefox.madridbus.api.ApiInterface;
import dev.diskettefox.madridbus.fragments.FragmentLines;
import dev.diskettefox.madridbus.fragments.FragmentMap;
import dev.diskettefox.madridbus.fragments.FragmentStop;
import dev.diskettefox.madridbus.models.LineModel;
import dev.diskettefox.madridbus.models.TokenModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigationBarView = findViewById(R.id.bottom_navigation);

        getToken();

        if (savedInstanceState == null) {
            loadFragment(new FragmentStop(), true);
        }

        // Navigation bar logic
        navigationBarView.setOnItemSelectedListener(menuItem -> {
            int itemId = menuItem.getItemId();

            // Menu selection
            if (itemId == R.id.menu_stops) {
                loadFragment(new FragmentStop(), false);
            } else if (itemId == R.id.menu_lines) {
                loadFragment(new FragmentLines(), false);
            } else if (itemId == R.id.menu_maps) {
                loadFragment(new FragmentMap(), false);
            }
            return true;
        });
    }

    public void getToken() {
        ApiInterface apiInterface = ApiCall.callApi().create(ApiInterface.class);
        String clientId = "b0c015e3-7e51-401a-b69b-0c8bb6e6e55f";
        String passKey = "A5C462BF6FD9BE59C286125F9A4101BA38071FCB1D0CDA03F205C0F07DA8CE2E7E5D1AB6A5A2829D60A7C2E3E7116E134B7BBCE782ACB70DC7FA880A294F4D75";

        Call<TokenModel> call = apiInterface.getToken(clientId, passKey);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<TokenModel> call, @NonNull Response<TokenModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TokenModel token = response.body();
                    if (token.getData() != null) {
                        TokenModel.Data data = token.getData().get(0);
                        Log.d("Token", data.getAccessToken());
                        ApiCall.setToken(data.getAccessToken());
                    }

                } else {
                    Log.e("API Error", "Unable to connect to database");
                }
            }

            @Override
            public void onFailure(@NonNull Call<TokenModel> call, @NonNull Throwable t) {
                Log.e("Call Error", "Error retrieving lines",t);
            }
        });
    }

    // Fragment manager
    public void loadFragment(Fragment fragment, boolean booted) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (booted) {
            fragmentTransaction.add(R.id.framelayout_main, fragment);
        } else {
            fragmentTransaction.replace(R.id.framelayout_main, fragment);
        }

        fragmentTransaction.commit();
    }
}