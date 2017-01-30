package com.luna1970.qingmumusic.fragment;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.luna1970.qingmumusic.Gson.Song;
import com.luna1970.qingmumusic.Gson.SongInfo;
import com.luna1970.qingmumusic.R;
import com.luna1970.qingmumusic.adapter.RecommendListAdapter;
import com.luna1970.qingmumusic.application.MusicApplication;
import com.luna1970.qingmumusic.listener.CustomRecyclerItemOnClickListener;
import com.luna1970.qingmumusic.util.GsonUtil;
import com.luna1970.qingmumusic.util.HttpUtils;
import com.luna1970.qingmumusic.util.UriUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Yue on 1/29/2017.
 *
 */

public class MainRecommendListFragment extends Fragment {
    private static final String TAG = "MainRecommendListFrag";

    private List<Song> songList;
    private RecommendListAdapter recommendListAdapter;
    private MediaPlayer mediaPlayer;
    private int songId;
    private Bundle bundle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommend_list, container, false);
        mediaPlayer = ((MusicApplication)getActivity().getApplication()).mediaPlayer;
        setView(view);
        return view;
    }

    public void setView(View view) {
        TextView titleTv= (TextView) view.findViewById(R.id.title_tv);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        bundle = getArguments();
//        Log.i(TAG, "setView: " + getArguments());
        if (bundle != null) {
            titleTv.setText(bundle.getString("title"));
        }

        songList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recommendListAdapter = new RecommendListAdapter(songList, new CustomRecyclerItemOnClickListener() {
            @Override
            public void onClick(int id) {
                songId = id;
                getFile();
            }
        });
        recyclerView.setAdapter(recommendListAdapter);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
    }

    public void getFile() {
        String apiPath = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&format=json&from:webapp_music&method=baidu.ting.song.play&songid=" + songId;
        HttpUtils.sendHttpRequest(apiPath, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final SongInfo song = GsonUtil.handlerSongInfoByRequestPlay(response.body().string());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mediaPlayer.reset();
                        try {
                            mediaPlayer.setDataSource(song.FileLink);
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        int type = 1;
        bundle = getArguments();
        if (bundle != null) {
            type = bundle.getInt("type");
        }
        requestSongInfoList(type);
    }

    private void requestSongInfoList(int type) {
        String apiUri = UriUtils.getRecommendUri(type, 0, 50);
        HttpUtils.sendHttpRequest(apiUri, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                songList.addAll(GsonUtil.handlerSongListByRequestDailyRecommend(response.body().string()));
                Runnable runnable = new Runnable() {
                    public void run() {
                        recommendListAdapter.notifyDataSetChanged();
                    }
                };
                getActivity().runOnUiThread(runnable);
            }
        });
    }

    public static Fragment newInstance(int type, String title) {
        MainRecommendListFragment mainRecommendListFragment = new MainRecommendListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putInt("type", type);
//        Log.d(TAG, "newInstance() called with: type = [" + type + "], title = [" + title + "]");
        mainRecommendListFragment.setArguments(bundle);
        return mainRecommendListFragment;
    }
}
