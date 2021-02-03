package com.example.androidsns.adapter;


import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.androidsns.R;

import java.util.ArrayList;

public class GallaryAdapter extends RecyclerView.Adapter<GallaryAdapter.GallayViewHolder> {

    private ArrayList<String> mDataSet;
    private Activity activity;

    public static class GallayViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public GallayViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    public GallaryAdapter(Activity activity, ArrayList<String> myDataSet) {
        mDataSet = myDataSet;
        this.activity = activity;
    }

    @NonNull
    @Override
    public GallaryAdapter.GallayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView)LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallary, parent, false);
        final GallayViewHolder gallayViewHolder = new GallayViewHolder(cardView);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("profilePath", mDataSet.get(gallayViewHolder.getAdapterPosition()));
                activity.setResult(Activity.RESULT_OK, resultIntent);
                activity.finish();
            }
        });
        return gallayViewHolder;
    }

    @Override
    public void onBindViewHolder(GallayViewHolder holder, int position) { // 이미지를 불러오는 함수
        CardView cardView = holder.cardView;

        ImageView imageView = cardView.findViewById(R.id.imageView);
        Glide.with(activity).load(mDataSet.get(position)).centerCrop().override(500).into(imageView); // 큰 이미지 파일들을 알아서 압축시켜줌
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
