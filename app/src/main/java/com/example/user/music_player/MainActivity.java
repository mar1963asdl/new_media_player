package com.example.user.music_player;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button play, pause, stop;
    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private boolean isChanging = false;
    int currentposition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play = findViewById(R.id.btn_play);
        pause = findViewById(R.id.btn_pause);
        stop = findViewById(R.id.btn_stop);
        seekBar = findViewById(R.id.btn_seek);

        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        stop.setOnClickListener(this);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                isChanging = true;
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
                isChanging = false;
            }
        });

        //service
        startService(new Intent(this, MyService.class));
        Intent i = new Intent(this, MyService.class);
        bindService(i, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                System.out.println("Pass");
                mediaPlayer = ((MyService.MyBinder) iBinder).getMediaPlayer();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        }, Context.BIND_AUTO_CREATE);
    }

    boolean threadFlag = true;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_play:

                if (mediaPlayer != null) {
//                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.music);
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.reset();
                    }
                    String uriString = "android.resource://" + getPackageName() + "/" + R.raw.music;
                    try {
                        mediaPlayer.setDataSource(this, Uri.parse(uriString));
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(final MediaPlayer mediaPlayer) {
                            Thread thred = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    while (threadFlag) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                seekBar.setMax(mediaPlayer.getDuration());
                                                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                                            }
                                        });
                                        try {
                                            Thread.sleep(500);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });
                            thred.start();
                        }
                    });
                    mediaPlayer.start();
                } else if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.seekTo(currentposition);
                    mediaPlayer.start();
                }
                break;

            case R.id.btn_pause:
                if (mediaPlayer != null) {
                    mediaPlayer.pause();
                    currentposition = mediaPlayer.getCurrentPosition();
                }
                break;

            case R.id.btn_stop:
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer = null;
                }
                break;

        }
    }

    @Override
    protected void onDestroy() {
        threadFlag = false;
        super.onDestroy();
    }
}
