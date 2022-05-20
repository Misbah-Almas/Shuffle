package com.example.shuffle;



import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyViewHolder> {

    private final Context mContext;
    private final String[] mData;

    MusicAdapter(Context mContext, String[] mData)

    {
        this.mContext = mContext;
        this.mData = mData;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textView.setText(mData[position]);
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(mContext,PlayerActivity.class);
            intent.putExtra("pos",position);
            mContext.startActivity(intent);

        });

    }

    @Override
    public int getItemCount() {
        return mData.length;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView textView;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.txtSong);
            imageView = itemView.findViewById(R.id.imgSong);
        }
    }
}
