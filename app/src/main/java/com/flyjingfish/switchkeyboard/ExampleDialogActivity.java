package com.flyjingfish.switchkeyboard;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.flyjingfish.switchkeyboard.databinding.ActivityExampleDialogBinding;

public class ExampleDialogActivity extends AppCompatActivity {
    private ActivityExampleDialogBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExampleDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        StatusBarHelper.transparencyBar(this);
        binding.llMsgContent.setOnClickListener(v -> {
            InputDialog inputDialog = InputDialog.getDialog(binding.etContent.getText().toString());
            inputDialog.setOnContentCallBack(new InputDialog.OnContentCallBack() {
                @Override
                public void onSendContent(String content) {

                }

                @Override
                public void onContent(String content) {
                    binding.etContent.setText(content);
                }
            });
            inputDialog.show(getSupportFragmentManager(), "inputDialog");
        });
    }




}