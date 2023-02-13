package com.flyjingfish.switchkeyboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.flyjingfish.switchkeyboard.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.example1.setOnClickListener(v -> startActivity(new Intent(this,SecondActivity.class)));
        binding.example2.setOnClickListener(v -> startActivity(new Intent(this,SecondActivity2.class)));
        binding.example3.setOnClickListener(v -> startActivity(new Intent(this,SecondActivity3.class)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}