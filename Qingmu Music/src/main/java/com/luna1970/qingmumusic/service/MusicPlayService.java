package com.luna1970.qingmumusic.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.luna1970.qingmumusic.Gson.SongInfo;
import com.luna1970.qingmumusic.application.MusicApplication;
import com.luna1970.qingmumusic.util.GlobalMusicPlayControllerConst;
import com.luna1970.qingmumusic.util.GsonUtil;
import com.luna1970.qingmumusic.util.HttpUtils;

import java.io.IOException;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.luna1970.qingmumusic.application.MusicApplication.playList;
import static com.luna1970.qingmumusic.application.MusicApplication.position;
import static com.luna1970.qingmumusic.application.MusicApplication.prevPosition;

/**
 * Created by Yue on 1/9/2017.
 */

public class MusicPlayService extends Service {
    private static final String TAG = "MusicPlayService2";
    private IntentFilter intentFilter;
    private Intent intent;
    private boolean hasPlayed;
    private BroadcastReceiver broadcastReceiver;
    private MediaPlayer mediaPlayer;
    private String[] playModeContainer;
    private String currentPlayMode;
    private boolean controlWorkThread;

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate: " + position + "  --> " + MusicApplication.position);
        mediaPlayer = new MediaPlayer();
        controlWorkThread = true;
        intentFilter = new IntentFilter();
        intentFilter.addAction(GlobalMusicPlayControllerConst.ACTION_ACTIVITY_PLAY_OR_PAUSE);
        intentFilter.addAction(GlobalMusicPlayControllerConst.ACTION_ACTIVITY_PLAY_NEXT);
        intentFilter.addAction(GlobalMusicPlayControllerConst.ACTION_ACTIVITY_PLAY_PREV);
        intentFilter.addAction(GlobalMusicPlayControllerConst.ACTION_ACTIVITY_PLAY_SPECIFIC);

        intentFilter.addAction(GlobalMusicPlayControllerConst.ACTION_ACTIVITY_PLAY_MODE_REPEAT_ALL);
        intentFilter.addAction(GlobalMusicPlayControllerConst.ACTION_ACTIVITY_PLAY_MODE_REPEAT_ONCE);
        intentFilter.addAction(GlobalMusicPlayControllerConst.ACTION_ACTIVITY_PLAY_MODE_SHUFFLE);
        intentFilter.addAction(GlobalMusicPlayControllerConst.ACTION_ACTIVITY_PLAY_MODE_ORDER);

        intentFilter.addAction(GlobalMusicPlayControllerConst.ACTION_FRAGMENT_REFRESH_PLAY_LIST);
        intentFilter.addAction(GlobalMusicPlayControllerConst.ACTION_FRAGMENT_PREPARE_PLAY);

        intentFilter.addAction(GlobalMusicPlayControllerConst.ACTION_ACTIVITY_SEEK_BAR_PROGRESS_CHANGED);

        playModeContainer = new String[]{GlobalMusicPlayControllerConst.ACTION_ACTIVITY_PLAY_MODE_REPEAT_ALL, GlobalMusicPlayControllerConst.ACTION_ACTIVITY_PLAY_MODE_REPEAT_ONCE, GlobalMusicPlayControllerConst.ACTION_ACTIVITY_PLAY_MODE_SHUFFLE, GlobalMusicPlayControllerConst.ACTION_ACTIVITY_PLAY_MODE_ORDER};
        currentPlayMode = GlobalMusicPlayControllerConst.ACTION_ACTIVITY_PLAY_MODE_REPEAT_ALL;
//        new Thread() {
//            @Override
//            public void run() {
//                while (controlWorkThread) {
//                    if (mediaPlayer.isPlaying()) {
//                        int currentPosition = mediaPlayer.getCurrentPosition();
//                        intent = new Intent();
//                        intent.putExtra(GlobalMusicPlayControllerConst.ACTION_SERVICE_UPDATE_SEEK_BAR_PROGRESS, currentPosition);
//                        intent.setAction(GlobalMusicPlayControllerConst.ACTION_SERVICE_UPDATE_SEEK_BAR_PROGRESS);
//                        sendBroadcast(intent);
//                    }
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//                super.run();
//            }
//        }.start();
        super.onCreate();
        setListeners();
    }

    private void setListeners() {
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                mp.reset();
                return false;
            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind: ");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, final int flags, int startId) {
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                prevPosition = position;
                if (currentPlayMode.equals(playModeContainer[0])) {
                    position = ++position >= playList.size() ? 0 : position;
                } else if (currentPlayMode.equals(playModeContainer[1])) {
                    mediaPlayer.start();
                    return;
                } else if (currentPlayMode.equals(playModeContainer[2])) {
                    position = getShufflePosition(position);
                } else if (currentPlayMode.equals(playModeContainer[3])) {
                    ++position;
                    if (position >= playList.size()) {
                        mediaPlayer.stop();
                        MusicPlayService.this.intent = new Intent();
                        MusicPlayService.this.intent.setAction(GlobalMusicPlayControllerConst.ACTION_SERVICE_PAUSE);
                        sendBroadcast(MusicPlayService.this.intent);
                        return;
                    }
                }
                playMusic(position, GlobalMusicPlayControllerConst.ACTION_SERVICE_PLAYING);
            }
        });
        Log.i(TAG, "onStartCommand: ");
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i(TAG, "onReceive: " + intent.getAction());
                prevPosition = position;
                switch (intent.getAction()) {
                    case GlobalMusicPlayControllerConst.ACTION_FRAGMENT_REFRESH_PLAY_LIST:
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.reset();
                        }
                        break;
                    case GlobalMusicPlayControllerConst.ACTION_FRAGMENT_PREPARE_PLAY:
                        playMusic(position, null);
                        break;
                    case GlobalMusicPlayControllerConst.ACTION_ACTIVITY_PLAY_OR_PAUSE:
                        if (hasPlayed) {
                            if (mediaPlayer.isPlaying()) {
                                mediaPlayer.pause();
                                intent = new Intent();
                                intent.setAction(GlobalMusicPlayControllerConst.ACTION_SERVICE_PAUSE);
                                sendBroadcast(intent);
                            } else {
                                mediaPlayer.start();
                                sendCustomBroadcast(GlobalMusicPlayControllerConst.ACTION_SERVICE_PLAY_CONTINUE);
                            }
                        } else {
                            hasPlayed = true;
                            playMusic(position, GlobalMusicPlayControllerConst.ACTION_SERVICE_PLAYING);
                            mediaPlayer.seekTo(intent.getIntExtra("seekBarProgress", 0));
                        }
                        break;
                    case GlobalMusicPlayControllerConst.ACTION_ACTIVITY_PLAY_NEXT:
                        if (currentPlayMode.equals(playModeContainer[2])) {
                            position = getShufflePosition(position);
                        } else {
                            position = ++position >= playList.size() ? 0 : position;
                        }
                        hasPlayed = true;
                        playMusic(position, GlobalMusicPlayControllerConst.ACTION_SERVICE_PLAYING);
                        break;
                    case GlobalMusicPlayControllerConst.ACTION_ACTIVITY_PLAY_PREV:
                        if (currentPlayMode.equals(playModeContainer[2])) {
                            position = getShufflePosition(position);
                        } else {
                            position = --position < 0 ? playList.size() - 1 : position;
                        }
                        hasPlayed = true;
                        playMusic(position, GlobalMusicPlayControllerConst.ACTION_SERVICE_PLAYING);
                        break;
                    case GlobalMusicPlayControllerConst.ACTION_ACTIVITY_PLAY_SPECIFIC:
                        int newPosition = intent.getIntExtra("position", 0);
                        if (newPosition != position) {
                            position = newPosition;
                            playMusic(newPosition, GlobalMusicPlayControllerConst.ACTION_SERVICE_PLAYING);
                        } else {
                            if (!mediaPlayer.isPlaying()) {
                                mediaPlayer.start();
                                sendCustomBroadcast(GlobalMusicPlayControllerConst.ACTION_SERVICE_PLAY_CONTINUE);
                            } else {
                                Toast.makeText(context, "当前正在播放该曲目", Toast.LENGTH_SHORT).show();
                            }
                        }
                        hasPlayed = true;
                        break;
                    case GlobalMusicPlayControllerConst.ACTION_ACTIVITY_PLAY_MODE_REPEAT_ALL:
                        currentPlayMode = GlobalMusicPlayControllerConst.ACTION_ACTIVITY_PLAY_MODE_REPEAT_ALL;
                        break;
                    case GlobalMusicPlayControllerConst.ACTION_ACTIVITY_PLAY_MODE_REPEAT_ONCE:
                        currentPlayMode = GlobalMusicPlayControllerConst.ACTION_ACTIVITY_PLAY_MODE_REPEAT_ONCE;
                        break;
                    case GlobalMusicPlayControllerConst.ACTION_ACTIVITY_PLAY_MODE_SHUFFLE:
                        currentPlayMode = GlobalMusicPlayControllerConst.ACTION_ACTIVITY_PLAY_MODE_SHUFFLE;
                        break;
                    case GlobalMusicPlayControllerConst.ACTION_ACTIVITY_PLAY_MODE_ORDER:
                        currentPlayMode = GlobalMusicPlayControllerConst.ACTION_ACTIVITY_PLAY_MODE_ORDER;
                        break;
                    case GlobalMusicPlayControllerConst.ACTION_ACTIVITY_SEEK_BAR_PROGRESS_CHANGED:
                        int currentPosition = intent.getIntExtra(GlobalMusicPlayControllerConst.ACTION_ACTIVITY_SEEK_BAR_PROGRESS_CHANGED, 0);
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.seekTo(currentPosition);
                        }
                }
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);
        return super.onStartCommand(intent, flags, startId);
    }

    private int getShufflePosition(int position) {
        int newPosition;
        do {
            newPosition = new Random().nextInt(playList.size());
        } while (position == newPosition);
        return newPosition;
    }

    private void playMusic(int position, String action) {
        if (MusicApplication.isPlaying) {
            mediaPlayer.stop();
        }
        if (playList.size() <= 0) {
            return;
        }
        int songId = playList.get(position).songId;
        getSongFilePath(songId);
    }

    private void getSongFilePath(int id) {
        String apiPath = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&format=json&from:webapp_music&method=baidu.ting.song.play&songid=" + id;
        HttpUtils.sendHttpRequest(apiPath, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final SongInfo song = GsonUtil.handlerSongInfoByRequestPlay(response.body().string());
                mediaPlayer.reset();
                mediaPlayer.setDataSource(song.FileLink);
                mediaPlayer.prepare();
                mediaPlayer.start();
            }
        });
    }

    private void sendCustomBroadcast(String action) {
        intent = new Intent();
        intent.setAction(action);
        sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        controlWorkThread = false;
        mediaPlayer.release();
        stopSelf();
        Log.i(TAG, "onDestroy: ");
        super.onDestroy();
    }
}