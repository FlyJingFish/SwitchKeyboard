package com.flyjingfish.switchkeyboard;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flyjingfish.switchkeyboard.databinding.ActivityExample2Binding;
import com.flyjingfish.switchkeyboardlib.AutoShowKeyboardType;
import com.flyjingfish.switchkeyboardlib.MenuModeView;
import com.flyjingfish.switchkeyboardlib.SwitchKeyboardUtil;

import java.util.ArrayList;
import java.util.List;

public class Example2Activity extends BaseActivity {
    private SwitchKeyboardUtil switchKeyboardUtil;
    private ActivityExample2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExample2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        switchKeyboardUtil = new SwitchKeyboardUtil(this);
        switchKeyboardUtil.setMenuViewHeightEqualKeyboard(true);
        switchKeyboardUtil.setUseSwitchAnim(true);
        switchKeyboardUtil.setAutoShowKeyboard(true, AutoShowKeyboardType.FIRST_SHOW);
        switchKeyboardUtil.attachLifecycle(this);
        switchKeyboardUtil.setInputEditText(binding.etContent);
        switchKeyboardUtil.setAudioBtn(binding.tvAudio);
        switchKeyboardUtil.setAudioTouchView(binding.tvAudioTouch);
        switchKeyboardUtil.setMenuViewContainer(binding.llMenu);
        switchKeyboardUtil.setToggleMenuViews(new MenuModeView(binding.tvMore, binding.llMenuBtn),
                new MenuModeView(binding.tvFace, binding.llEmoji, binding.tvFaceBack,true),
                new MenuModeView(binding.tvWord, binding.llWord, binding.tvWordBack,true),
                new MenuModeView(binding.tvGift, binding.llGift, binding.tvGiftBack,true)
        );
        switchKeyboardUtil.setOnKeyboardMenuListener(new SwitchKeyboardUtil.OnKeyboardMenuListener() {
            @Override
            public void onScrollToBottom() {
                scrollToBottom();
            }

            @Override
            public void onCallShowKeyboard() {

            }

            @Override
            public void onCallHideKeyboard() {
            }

            @Override
            public void onKeyboardHide(int keyboardHeight) {

            }

            @Override
            public void onKeyboardShow(int keyboardHeight) {
                binding.tvAudio.setImageResource(R.drawable.ic_audio);
            }

            @Override
            public void onShowMenuLayout(View layoutView) {
                binding.tvAudio.setImageResource(layoutView == binding.tvAudioTouch?R.drawable.ic_keyboard:R.drawable.ic_audio);
            }

            @Override
            public void onHideMenuViewContainer() {
                binding.tvAudio.setImageResource(R.drawable.ic_audio);
            }
        });

        binding.rv.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                switchKeyboardUtil.hideMenuAndKeyboard();
            }
            return false;
        });

        List<String> msgList = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            msgList.add("item=" + i);
        }
        MsgAdapter msgAdapter = new MsgAdapter(msgList);
        binding.rv.setAdapter(msgAdapter);
        binding.rv.setLayoutManager(new LinearLayoutManager(this));

        View.OnLayoutChangeListener onLayoutChangeListener = (v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> scrollToBottom();
        binding.rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState != RecyclerView.SCROLL_STATE_IDLE){
                    binding.rv.removeOnLayoutChangeListener(onLayoutChangeListener);
                }else {
                    binding.rv.addOnLayoutChangeListener(onLayoutChangeListener);
                }
            }
        });
        binding.rv.postDelayed(() -> scrollToBottom(),200);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (switchKeyboardUtil.onKeyDown(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void scrollToBottom() {
//        if (!binding.rv.canScrollVertically(1)){
        if (getLifecycle().getCurrentState() == Lifecycle.State.RESUMED && binding.rv.getAdapter() != null){
            binding.rv.scrollToPosition(binding.rv.getAdapter().getItemCount() - 1);
        }
//        }
    }
}