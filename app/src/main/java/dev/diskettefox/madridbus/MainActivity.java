package dev.diskettefox.madridbus;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
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
        FrameLayout frameLayout=findViewById(R.id.framelayout);
        cargaFragment(new Fragment_main(),true);

        // navegation bar login esto es solo pa que cambie de fragment
        navigationBarView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId=menuItem.getItemId();
                if (itemId==R.id.menu_stops){
                    cargaFragment(new Fragment_main(),false);
                } else if (itemId==R.id.menu_lines) {
                    cargaFragment(new Fragment_lines(),false);
                } else if (itemId==R.id.menu_maps) {
                    cargaFragment(new Fragment_map(),false);
                }
                return true;
            }
        });

    }

    public void cargaFragment(Fragment fragment,boolean seInicioLaApp){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        if (seInicioLaApp){
            fragmentTransaction.add(R.id.framelayout,fragment);
        }
        else {
            fragmentTransaction.replace(R.id.framelayout,fragment);
        }
        fragmentTransaction.commit();
    }

}