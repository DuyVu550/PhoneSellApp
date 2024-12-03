package com.example.phonesellapp;

import android.os.Bundle;
import android.widget.ListView;
//import android.widget.Toolbar;
import android.widget.ViewFlipper;
import androidx.appcompat.widget.Toolbar;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
      //  Toolbar toolbar = findViewById(R.id.toolBarMain);
        ViewFlipper viewFlipper = findViewById(R.id.viewFlipper);
        RecyclerView recyclerView = findViewById(R.id.recycleView);
        ListView listViewMain = findViewById(R.id.ListViewMain);
        NavigationView navigationView = findViewById(R.id.navigationView);

    }
}