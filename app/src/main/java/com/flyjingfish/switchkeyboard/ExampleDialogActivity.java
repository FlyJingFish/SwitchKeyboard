package com.flyjingfish.switchkeyboard;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.flyjingfish.switchkeyboard.databinding.ActivityExampleDialogBinding;
import com.flyjingfish.switchkeyboardlib.SwitchKeyboardUtil;

public class ExampleDialogActivity extends BaseActivity {
    private ActivityExampleDialogBinding binding;

    @Override
    public String getTitleString() {
        return DialogFragment.class.getSimpleName();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExampleDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //请注意这块也是必须做的，不然首次打开DialogFragment时你会看到状态栏被改变了
        //当然如果你已经给你的 Activity 设置了透明状态栏，这句是可以忽略掉的
        new SwitchKeyboardUtil(this).setSystemUi();

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