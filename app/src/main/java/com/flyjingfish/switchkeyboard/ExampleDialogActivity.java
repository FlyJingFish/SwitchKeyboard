package com.flyjingfish.switchkeyboard;

import android.os.Bundle;
import android.view.KeyEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.flyjingfish.switchkeyboard.databinding.ActivityExample2Binding;
import com.flyjingfish.switchkeyboard.databinding.ActivityExampleDialogBinding;
import com.flyjingfish.switchkeyboard.dialog.BaseInputDialog;
import com.flyjingfish.switchkeyboard.dialog.InputDialog;
import com.flyjingfish.switchkeyboardlib.SwitchKeyboardUtil;

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
            inputDialog.setOnContentCallBack(new BaseInputDialog.OnContentCallBack() {
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