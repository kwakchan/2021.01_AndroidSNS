package com.example.androidsns.adapter;


import android.app.Activity;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.androidsns.PostInfo;
import com.example.androidsns.R;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.GallayViewHolder> {

    private ArrayList<PostInfo> mDataSet;
    private Activity activity;

    static class GallayViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        GallayViewHolder(CardView v) {
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
        
        // 제목
        TextView titleTextView = cardView.findViewById(R.id.titleTextView);
        titleTextView.setText(mDataSet.get(position).getTitle());

        // 날짜
        TextView createdTextView = cardView.findViewById(R.id.createdAtTextView);
        createdTextView.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(mDataSet.get(position).getCreatedAt()));

        // 이미지 동영상
        LinearLayout contentsLayout = cardView.findViewById(R.id.contentsLayout);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ArrayList<String> contentsList = mDataSet.get(position).getContents();

        if(contentsLayout.getChildCount() == 0){
            for(int i=0; i<contentsList.size(); i++){
                String contents = contentsList.get(i);
                if(Patterns.WEB_URL.matcher(contents).matches()){
                    ImageView imageView = new ImageView(activity);
                    imageView.setLayoutParams(layoutParams);
                    contentsLayout.addView(imageView);
                    Glide.with(activity).load(contents).override(1000).into(imageView);
                } else {
                    TextView textView = new TextView(activity);
                    textView.setLayoutParams(layoutParams);
                    textView.setText(contents);
                    contentsLayout.addView(textView);
                }
            }
        }

    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
