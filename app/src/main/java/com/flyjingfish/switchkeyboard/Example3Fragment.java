package com.flyjingfish.switchkeyboard;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flyjingfish.switchkeyboard.databinding.ActivityExample1Binding;
import com.flyjingfish.switchkeyboardlib.MenuModeView;
import com.flyjingfish.switchkeyboardlib.SwitchKeyboardUtil;

import java.util.ArrayList;
import java.util.List;

public class Example3Fragment extends Fragment {

    private com.flyjingfish.switchkeyboard.databinding.ActivityExample1Binding binding;
    private SwitchKeyboardUtil switchKeyboardUtil;
    private boolean dark;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ActivityExample1Binding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        switchKeyboardUtil = new SwitchKeyboardUtil(requireActivity());
        switchKeyboardUtil.setMenuViewHeightEqualKeyboard(false);
        switchKeyboardUtil.setUseSwitchAnim(true);
        switchKeyboardUtil.setInputEditText(binding.etContent);
        switchKeyboardUtil.setAudioBtn(binding.tvAudio);
        switchKeyboardUtil.setAudioTouchView(binding.tvAudioTouch);
        switchKeyboardUtil.setMenuViewContainer(binding.llMenu);
        switchKeyboardUtil.setToggleMenuViews(new MenuModeView(binding.tvMore,binding.llMenuBtn),
                new MenuModeView(binding.ivFace,binding.llEmoji),
                new MenuModeView(binding.tvGift,binding.llGift),
                new MenuModeView(binding.tvWord,binding.llWord));
        switchKeyboardUtil.attachLifecycle(this);
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
                binding.ivFace.setImageResource(R.drawable.ic_face);
            }



            @Override
            public void onShowMenuLayout(View layoutView) {
                binding.tvAudio.setImageResource(layoutView == binding.tvAudioTouch?R.drawable.ic_keyboard:R.drawable.ic_audio);
                binding.ivFace.setImageResource(layoutView == binding.llEmoji?R.drawable.ic_keyboard:R.drawable.ic_face);
            }

            @Override
            public void onHideMenuViewContainer() {
                binding.tvAudio.setImageResource(R.drawable.ic_audio);
                binding.ivFace.setImageResource(R.drawable.ic_face);
            }
        });
        binding.rv.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                switchKeyboardUtil.hideMenuAndKeyboard();
            }
            return false;
        });
        binding.tvVideo.setOnClickListener(v -> {
            dark = !dark;
            StatusBarHelper.setLightStatusBar(requireActivity(),dark,false);
//            Toast.makeText(requireActivity(),"去视频通话",Toast.LENGTH_SHORT).show()
        });
        List<String> msgList = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            msgList.add("item="+i);
        }
        MsgAdapter msgAdapter = new MsgAdapter(msgList);
        binding.rv.setAdapter(msgAdapter);
        binding.rv.setLayoutManager(new LinearLayoutManager(requireActivity()));

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

    private void scrollToBottom() {
        if (getLifecycle().getCurrentState() == Lifecycle.State.RESUMED && binding.rv.getAdapter() != null){
            binding.rv.scrollToPosition(binding.rv.getAdapter().getItemCount() - 1);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return switchKeyboardUtil.onKeyDown(keyCode, event);
    }
}
