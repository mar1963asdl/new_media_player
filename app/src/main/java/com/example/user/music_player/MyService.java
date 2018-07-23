package com.example.user.music_player;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

public class MyService extends Service {
    MediaPlayer mediaPlayer;

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
}
