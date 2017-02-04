package com.luna1970.qingmumusic.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestFutureTarget;
import com.luna1970.qingmumusic.Gson.AlbumInfo;
import com.luna1970.qingmumusic.Gson.Billboard;
import com.luna1970.qingmumusic.Gson.TopBillboard;
import com.luna1970.qingmumusic.R;
import com.luna1970.qingmumusic.adapter.SongListAdapter;
import com.luna1970.qingmumusic.application.MusicApplication;
import com.luna1970.qingmumusic.listener.CustomRecyclerItemOnClickListener;
import com.luna1970.qingmumusic.util.GsonUtil;
import com.luna1970.qingmumusic.util.HttpUtils;
import com.luna1970.qingmumusic.util.PlayController;
import com.luna1970.qingmumusic.util.UriUtils;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.luna1970.qingmumusic.application.MusicApplication.playState;

/**
 * Created by Yue on 2/3/2017.
 *
 */

public class TopBillboardActivity extends BaseActivity {
    private static final String TAG = "TopBillboardActivity";
    private TopBillboard topBillboard;
    private int type;
    private SongListAdapter songListAdapter;
    private LocalBroadcastManager localBroadcastManager;
    private ImageView toolbarBackgroundIv;
    private ActionBar actionBar;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_billboard);
        Logger.init(TAG);
        Glide.get(this).clearMemory();
        topBillboard = new TopBillboard();
        topBillboard.songList = new ArrayList<>();
        topBillboard.billboard = new Billboard();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        setToolbar();
        getIntentData();
        initViews();
    }

    public void getIntentData() {
        Bundle bundle = getIntent().getExtras();
        type = bundle.getInt("type");
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("加载中...");
        }

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitleEnabled(false);
        collapsingToolbarLayout.setTitle("");
    }

    private void initViews() {
        toolbarBackgroundIv = (ImageView) findViewById(R.id.toolbar_background_iv);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        songListAdapter = new SongListAdapter(topBillboard.songList, new CustomRecyclerItemOnClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent();
                intent.setAction(PlayController.ACTION_REFRESH_PLAY_LIST);
                localBroadcastManager.sendBroadcast(intent);
                playState.updatePlayList(topBillboard.songList);
                intent = new Intent();
                intent.setAction(PlayController.ACTION_PLAY_SPECIFIC);
                intent.putExtra(PlayController.ACTION_PLAY_SPECIFIC, position);
                localBroadcastManager.sendBroadcast(intent);

            }
        });
        recyclerView.setAdapter(songListAdapter);
        recyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (topBillboard.songList != null && topBillboard.songList.size() == 0) {
            sendNetRequest();
        }
    }


    private void sendNetRequest() {
        String songListUri = UriUtils.getRecommendUri(type, 0, 100);
        HttpUtils.sendHttpRequest(songListUri, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                final TopBillboard topBillboard = GsonUtil.handlerTopBillboard(res);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TopBillboardActivity.this.topBillboard.songList.clear();
                        TopBillboardActivity.this.topBillboard.songList.addAll(topBillboard.songList);
                        songListAdapter.notifyDataSetChanged();
                        TopBillboardActivity.this.topBillboard.billboard = topBillboard.billboard;
                        Glide.with(TopBillboardActivity.this).load(TopBillboardActivity.this.topBillboard.billboard.picBigSquare).into(toolbarBackgroundIv);
                        actionBar.setTitle(TopBillboardActivity.this.topBillboard.billboard.name);
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
