package com.example.androidsns.adapter;


import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.androidsns.PostInfo;
import com.example.androidsns.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.GallayViewHolder> {

    private ArrayList<PostInfo> mDataSet;
    private Activity activity;

    public static class GallayViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public GallayViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    public MainAdapter(Activity activity, ArrayList<PostInfo> myDataSet) {
        mDataSet = myDataSet;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MainAdapter.GallayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView)LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        final GallayViewHolder gallayViewHolder = new GallayViewHolder(cardView);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return gallayViewHolder;
    }

    @Override
    public void onBindViewHolder(GallayViewHolder holder, int position) { // 이미지를 불러오는 함수
        CardView cardView = holder.cardView;
        TextView textView = cardView.findViewById(R.id.textView);
        textView.setText(mDataSet.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
