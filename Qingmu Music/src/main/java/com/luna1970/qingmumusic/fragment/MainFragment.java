package com.luna1970.qingmumusic.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.luna1970.qingmumusic.R;
import com.luna1970.qingmumusic.util.TopSongType;

import java.util.HashMap;

/**
 * Created by Yue on 1/26/2017.
 *
 */

public class MainFragment extends Fragment {
    private static final String TAG = "MainFragment";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        startFragment();
        return view;
    }

    private void startFragment() {
        Log.d(TAG, "startFragment: ");
        HashMap<Integer, String> allType = TopSongType.ALL_TYPE;
        MainNewAlbumFragment mainNewAlbumFragment = new MainNewAlbumFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.load_fragment_translate_in_right2left, 0);
        fragmentTransaction.replace(R.id.container_new_album_fragment, mainNewAlbumFragment);
        fragmentTransaction.commit();

        fragmentTransaction = fragmentManager.beginTransaction();
        Fragment TYPE_NEW_SONG = MainTopSongListFragment.newInstance(TopSongType.TYPE_NEW_SONG,       allType.get(TopSongType.TYPE_NEW_SONG));
        fragmentTransaction.replace(R.id.TYPE_NEW_SONG , TYPE_NEW_SONG);
        fragmentTransaction.setCustomAnimations(R.anim.load_fragment_translate_in_right2left, 0);
        fragmentTransaction.commit();

        fragmentTransaction = fragmentManager.beginTransaction();
        Fragment TYPE_HOT_SONG = MainTopSongListFragment.newInstance(TopSongType.TYPE_HOT_SONG,       allType.get(TopSongType.TYPE_HOT_SONG));
        fragmentTransaction.replace(R.id.TYPE_HOT_SONG, TYPE_HOT_SONG);
        fragmentTransaction.setCustomAnimations(R.anim.load_fragment_translate_in_right2left, 0);
        fragmentTransaction.commit();

        fragmentTransaction = fragmentManager.beginTransaction();
        Fragment newSongOnline = new BannerFocusFragment();
        fragmentTransaction.replace(R.id.new_song_online, newSongOnline);
        fragmentTransaction.commit();

        fragmentTransaction = fragmentManager.beginTransaction();
        Fragment TYPE_EUROPE_AMERICA = MainTopSongListFragment.newInstance(TopSongType.TYPE_EUROPE_AMERICA, allType.get(TopSongType.TYPE_EUROPE_AMERICA));
        fragmentTransaction.replace(R.id.TYPE_EUROPE_AMERICA, TYPE_EUROPE_AMERICA);
        fragmentTransaction.commit();

        fragmentTransaction = fragmentManager.beginTransaction();
        Fragment TYPE_ORIGINAL = MainTopSongListFragment.newInstance(TopSongType.TYPE_ORIGINAL,       allType.get(TopSongType.TYPE_ORIGINAL));
        fragmentTransaction.replace(R.id.TYPE_ORIGINAL, TYPE_ORIGINAL);
        fragmentTransaction.commit();

        fragmentTransaction = fragmentManager.beginTransaction();
        Fragment TYPE_POP_MUSIC = MainTopSongListFragment.newInstance(TopSongType.TYPE_POP_MUSIC,      allType.get(TopSongType.TYPE_POP_MUSIC));
        fragmentTransaction.replace(R.id.TYPE_POP_MUSIC, TYPE_POP_MUSIC);
        fragmentTransaction.commit();

        fragmentTransaction = fragmentManager.beginTransaction();
        Fragment TYPE_CHINESE_SONG = MainTopSongListFragment.newInstance(TopSongType.TYPE_CHINESE_SONG,   allType.get(TopSongType.TYPE_CHINESE_SONG));
        fragmentTransaction.replace(R.id.TYPE_CHINESE_SONG, TYPE_CHINESE_SONG);
        fragmentTransaction.commit();

        fragmentTransaction = fragmentManager.beginTransaction();
        Fragment TYPE_CLASSICAL_SONG = MainTopSongListFragment.newInstance(TopSongType.TYPE_CLASSICAL_SONG, allType.get(TopSongType.TYPE_CLASSICAL_SONG));
        fragmentTransaction.replace(R.id.TYPE_CLASSICAL_SONG, TYPE_CLASSICAL_SONG);
        fragmentTransaction.commit();

        fragmentTransaction = fragmentManager.beginTransaction();
        Fragment TYPE_NET_WORK_SONGS = MainTopSongListFragment.newInstance(TopSongType.TYPE_NET_WORK_SONGS, allType.get(TopSongType.TYPE_NET_WORK_SONGS));
        fragmentTransaction.replace(R.id.TYPE_NET_WORK_SONGS, TYPE_NET_WORK_SONGS);
        fragmentTransaction.commit();

        fragmentTransaction = fragmentManager.beginTransaction();
        Fragment TYPE_FILM_SONGS = MainTopSongListFragment.newInstance(TopSongType.TYPE_FILM_SONGS,     allType.get(TopSongType.TYPE_FILM_SONGS));
        fragmentTransaction.replace(R.id.TYPE_FILM_SONGS, TYPE_FILM_SONGS);
        fragmentTransaction.commit();

        fragmentTransaction = fragmentManager.beginTransaction();
        Fragment TYPE_LOVE_SONGS = MainTopSongListFragment.newInstance(TopSongType.TYPE_LOVE_SONGS,     allType.get(TopSongType.TYPE_LOVE_SONGS));
        fragmentTransaction.replace(R.id.TYPE_LOVE_SONGS, TYPE_LOVE_SONGS);
        fragmentTransaction.commit();

//        fragmentTransaction = fragmentManager.beginTransaction();
//        Fragment TYPE_BILLBOARD = MainTopSongListFragment.newInstance(TopSongType.TYPE_BILLBOARD,      allType.get(TopSongType.TYPE_BILLBOARD));
//        fragmentTransaction.replace(R.id.TYPE_BILLBOARD, TYPE_BILLBOARD);
//        fragmentTransaction.commit();

        fragmentTransaction = fragmentManager.beginTransaction();
        Fragment TYPE_ROCK = MainTopSongListFragment.newInstance(TopSongType.TYPE_ROCK,           allType.get(TopSongType.TYPE_ROCK));
        fragmentTransaction.replace(R.id.TYPE_ROCK, TYPE_ROCK);
        fragmentTransaction.commit();

        fragmentTransaction = fragmentManager.beginTransaction();
        Fragment TYPE_JAZZ = MainTopSongListFragment.newInstance(TopSongType.TYPE_JAZZ,           allType.get(TopSongType.TYPE_JAZZ));
        fragmentTransaction.replace(R.id.TYPE_JAZZ, TYPE_JAZZ);
        fragmentTransaction.commit();
    }

}
