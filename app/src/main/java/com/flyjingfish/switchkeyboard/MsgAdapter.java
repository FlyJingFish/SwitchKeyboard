package com.flyjingfish.switchkeyboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flyjingfish.switchkeyboard.databinding.ItemMsgBinding;

import java.util.ArrayList;
import java.util.List;


public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.MyViewHolder> {
    private List<String> list = new ArrayList<>();

    public MsgAdapter(List<String> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(ItemMsgBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false).getRoot());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ItemMsgBinding binding = ItemMsgBinding.bind(holder.itemView);
        binding.tvMsg.setText(list.get(position));
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
