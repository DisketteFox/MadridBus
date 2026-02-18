package dev.diskettefox.madridbus;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
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

        BottomNavigationView navigationBarView = findViewById(R.id.bottom_navigation);
        loadFragment(new FragmentStop(),true);

        // Navigation bar logic
        navigationBarView.setOnItemSelectedListener((BottomNavigationView.OnNavigationItemSelectedListener) menuItem -> {
            int itemId=menuItem.getItemId();

            // Menu selection
            if (itemId==R.id.menu_stops){
                loadFragment(new FragmentStop(),false);
            } else if (itemId==R.id.menu_lines) {
                loadFragment(new FragmentLines(),false);
            } else if (itemId==R.id.menu_maps) {
                loadFragment(new FragmentMap(),false);
            }

            return true;
        });
    }

    // Fragment manager
    public void loadFragment(Fragment fragment, boolean booted){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (booted){
            fragmentTransaction.add(R.id.framelayout_main,fragment);
        } else {
            fragmentTransaction.replace(R.id.framelayout_main,fragment);
        }

        fragmentTransaction.commit();
    }
}