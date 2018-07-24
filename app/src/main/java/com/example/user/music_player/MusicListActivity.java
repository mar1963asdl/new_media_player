package com.example.user.music_player;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class MusicListActivity extends AppCompatActivity {
    private RecyclerView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);
        list = findViewById(R.id.recyclerList);
        list.setLayoutManager(new LinearLayoutManager(this));

//        ArrayList<String> data = new ArrayList();
        ArrayList<Song> songs = new ArrayList();
        String uriString = "android.resource://" + getPackageName() + "/" + R.raw.music;

        Song song=new Song();
        song.name="越長大越孤單";
        song.uri =uriString;
        //        data.add(uriString);
        songs.add(song);



        String uriString1 ="android.resource://" + getPackageName() + "/" + R.raw.music1;
        Song song1= new Song();
        song1.name="三角題";
        song1.uri = uriString1;
        songs.add(song1);
        list.setAdapter(new MyAdapter(songs,this));

    }
}
