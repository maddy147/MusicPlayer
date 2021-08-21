package com.example.musicplayer;

import static android.bluetooth.BluetoothGattDescriptor.PERMISSION_READ;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer = new MediaPlayer();
    List<Audio> audioList = new ArrayList<>();
    RecyclerView recyclerView;
    int current_index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button play = findViewById(R.id.play);
        Button pause = findViewById(R.id.pause);
        Button next = findViewById(R.id.next);
        Button prev = findViewById(R.id.previous);
        if(checkPermission()){
            display_Songs();
        }
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                }
                else{
                    mediaPlayer.start();
                }
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                }
                else{
                    mediaPlayer.start();
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current_index < audioList.size() -1){
                    current_index++;
                }
                else{
                    current_index = 0;
                }
                play_Songs(current_index);
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current_index > 0){
                    current_index--;
                }
                else {
                    current_index = audioList.size()-1;
                }
                play_Songs(current_index);
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(current_index < audioList.size() -1){
                    current_index++;
                }
                else{
                    current_index = 0;
                }
                play_Songs(current_index);
            }
        });
    }
    public void display_Songs(){
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        get_Songs();
    }
    public void play_Songs(int index){
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(this, Uri.parse(audioList.get(index).getData()));
            mediaPlayer.prepare();
            mediaPlayer.start();
            current_index = index;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void get_Songs(){
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                    String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                    String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));

                    Audio A = new Audio(data,title,album,artist);
                    audioList.add(A);
                } while (cursor.moveToNext());

            }
            cursor.close();
        }
        if(audioList.size() == 0 ){
            Toast.makeText(MainActivity.this,"NO SONG FOUND",Toast.LENGTH_SHORT).show();
        }
        Adapter adapter = new Adapter(this, (ArrayList<Audio>) audioList);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos, View v) {
                current_index = pos;
                play_Songs(current_index);
            }
        });
    }
    public boolean checkPermission() {
        int READ_EXTERNAL_PERMISSION = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if((READ_EXTERNAL_PERMISSION != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_READ);
            return false;
        }
        return true;
    }

}