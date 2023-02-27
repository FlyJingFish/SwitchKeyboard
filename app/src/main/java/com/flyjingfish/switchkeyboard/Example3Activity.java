package com.flyjingfish.switchkeyboard;

import android.os.Bundle;
import android.view.KeyEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.flyjingfish.switchkeyboard.databinding.ActivityExample3Binding;
import com.flyjingfish.switchkeyboardlib.SwitchKeyboardUtil;

public class Example3Activity extends AppCompatActivity {
    private ActivityExample3Binding binding;
    private Example3Fragment example3Fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExample3Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        example3Fragment = new Example3Fragment();
        transaction.replace(R.id.container, example3Fragment).commitAllowingStateLoss();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (example3Fragment.onKeyDown(keyCode, event)) {//传递給fragment
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}