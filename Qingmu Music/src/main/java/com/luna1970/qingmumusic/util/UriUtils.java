package com.luna1970.qingmumusic.util;

import java.text.MessageFormat;

/**
 * Created by Yue on 1/29/2017.
 *
 */

public class UriUtils {
    /**
     * new songList recommend
     */
    private static final String NEW_SONG = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.plaza.getNewSongs&format=json&limit=";
    /**
     * hot songList in order
     */
    private static final String TOP_HOT = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&method=baidu.ting.billboard.billList&format=json&type={0}&offset={1}&size={2}";
    /**
     * new songList background in first page
     */
    private static final String BANNER_FOCUS = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.9.8.1&channel=1426d&operator=0&method=baidu.ting.plaza.index&cuid=62DCEAF9427E66A9276CA44&focu_num={0}";

    private static final String SONG_FILE = "http://tingapi.ting.baidu.com/v1/restserver/ting?from=qianqian&version=2.1.0&type=json&from=webapp_music&method=baidu.ting.song.play&songid=";

    public static String getRecommendUri(int type, int offset, int size) {
        MessageFormat messageFormat = new MessageFormat(TOP_HOT);
        return messageFormat.format(new String[]{type + "", offset + "", size + ""});
    }

    /**
     * custom album or songList cover/avatar pixel size
     * @param oldUri untreated cover/avatar uri
     * @param size cover/avatar width pixel
     * @return specified width pixel image uri
     */
    public static String getCustomImageSize(String oldUri, int size) {
        StringBuilder stringBuilder = new StringBuilder(oldUri);
        return stringBuilder.delete(stringBuilder.length() - 3, stringBuilder.length()).append(size).toString();
    }

    public static String getBannerFocusImagePath(int index) {
        MessageFormat messageFormat = new MessageFormat(BANNER_FOCUS);
        return messageFormat.format(new Integer[]{index});
    }

    public static String getNewSong(int count) {
        return NEW_SONG + count;
    }


    public static String getSongFile(int id) {
        return SONG_FILE + id;
    }
}
