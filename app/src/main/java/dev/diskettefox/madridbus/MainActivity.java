package dev.diskettefox.madridbus;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

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

        BottomNavigationView navigationBarView = findViewById(R.id.bottom_navigation);
        cargaFragment(new FragmentMain(),true);

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