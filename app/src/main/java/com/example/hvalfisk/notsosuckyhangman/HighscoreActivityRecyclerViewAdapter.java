package com.example.hvalfisk.notsosuckyhangman;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

class HighscoreActivityRecyclerViewAdapter extends RecyclerView.Adapter<HighscoreActivityRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<String> names;
    private final Context context;
    private final ArrayList<Integer> highestStreaks;

    public HighscoreActivityRecyclerViewAdapter (ArrayList<String> names, ArrayList<Integer> highestStreaks, Context context) {
        this.names = names;
        this.highestStreaks = highestStreaks;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.highscore_entry, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.playerNames.setText(names.get(position));
        viewHolder.streakCount.setText(""+highestStreaks.get(position));
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView playerNames;
        TextView streakCount;
        ImageView medalImage;
        ConstraintLayout layout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.highscore_layout);
            playerNames = itemView.findViewById(R.id.playerName);
            streakCount = itemView.findViewById(R.id.streakCount);
            medalImage = itemView.findViewById(R.id.imageView2);


        }
    }
}
