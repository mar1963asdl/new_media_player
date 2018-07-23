package com.example.user.music_player;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.view.View;
import android.widget.SeekBar;

import java.io.IOException;

public class MyService extends Service implements View.OnClickListener{
    MediaPlayer mediaPlayer;
    int currentposition;
    private SeekBar seekBar;
    public MyService() {
    }

    @Override
    public void onCreate() {
        mediaPlayer = new MediaPlayer();
        System.out.println("hello onCreate");
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("hello");
        return super.onStartCommand(intent, flags, startId);
    }

    class MyBinder extends Binder {
        MyBinder() {
        }

        public MyService getService() {
            return MyService.this;
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_play:
                if (mediaPlayer == null) {
//                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.music);
                    mediaPlayer=new MediaPlayer();
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
}

