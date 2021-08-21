package com.example.musicplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.viewHolder> {

    Context context;
    ArrayList<Audio> audioList;
    public OnItemClickListener onItemClickListener;

    public Adapter(Context context, ArrayList<Audio> audioList) {
        this.context = context;
        this.audioList = audioList;
    }

    @NonNull
    @Override
    public Adapter.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.songs_list, viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(final Adapter.viewHolder holder, final int i) {
        holder.title.setText(audioList.get(i).getTitle());
        holder.artist.setText(audioList.get(i).getArtist());
        holder.album.setText(audioList.get(i).getAlbum());
    }

    @Override
    public int getItemCount() {
        return audioList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView title, artist,album;
        public viewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            artist = (TextView) itemView.findViewById(R.id.artist);
            album =(TextView) itemView.findViewById(R.id.album);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(getAdapterPosition(), v);
                }
            });
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int pos, View v);
    }
}
