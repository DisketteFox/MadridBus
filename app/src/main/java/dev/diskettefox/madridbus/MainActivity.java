package dev.diskettefox.madridbus;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigationBarView = findViewById(R.id.bottom_navigation);
        navigationBarView.setSelectedItemId(R.id.stops_Main);

        navigationBarView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.stops_Main) {
                return true;
            } else if (itemId == R.id.lines) {
                Intent intent = new Intent(MainActivity.this, LinesActivity.class);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.maps) {
                return true;
            }
            return false;
        });


    }

}