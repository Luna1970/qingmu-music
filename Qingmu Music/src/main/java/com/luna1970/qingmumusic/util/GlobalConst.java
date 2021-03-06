package com.luna1970.qingmumusic.util;

/**
 * Created by Yue on 1/9/2017.
 *
 */

public final class GlobalConst {
    // play button selected
    public static final String ACTION_PLAY_OR_PAUSE = "PLAY_OR_PAUSE";
    public static final String ACTION_PLAY_NEXT = "PLAY_NEXT";
    public static final String ACTION_PLAY_PREV = "PLAY_PREV";
    public static final String ACTION_PLAY_SPECIFIC = "PLAY_SPECIFIC";
    public static final String ACTION_PLAY_SPECIFIC_SONG_ID = "ACTION_PLAY_SPECIFIC_SONG_ID";
    public static final String ACTION_PLAY_STOP = "ACTION_PLAY_STOP";

    // playing mode
    public static final String ACTION_PLAY_MODE_CHANGED = "PLAY_MODE_CHANGED";

    // play list
    public static final String ACTION_PLAY_LIST_CLEAR = "ACTION_PLAY_LIST_CLEAR";

    // seek bar progress
    public static final String ACTION_SEEK_BAR_PROGRESS_CHANGED = "SEEK_BAR_PROGRESS_CHANGED";

    // service state
    public static final String STATE_SERVICE_PLAYING = "SERVICE_PLAYING";
    public static final String STATE_SERVICE_PLAY_CONTINUE = "SERVICE_PLAY_CONTINUE";
    public static final String STATE_SERVICE_PAUSE = "SERVICE_PAUSE";
    public static final String STATE_SERVICE_STOP = "SERVICE_STOP";
    public static final String STATE_SERVICE_UPDATE_SEEK_BAR_PROGRESS = "SERVICE_UPDATE_SEEK_BAR_PROGRESS";
    public static final String STATE_SERVICE_UPDATE_BUFFER_PROGRESS = "STATE_SERVICE_UPDATE_BUFFER_PROGRESS";

    // fragment
    public static final String ACTION_REFRESH_PLAY_LIST = "REFRESH_PLAY_LIST";

    public static final String PLAY_CONTROL_BAR_FRAGMENT_TAG = "PLAY_CONTROL_BAR_FRAGMENT_TAG";

    // user info
    public static final String CURRENT_POSITION = "CURRENT_POSITION";

    // update info
    public static final String UPDATE_WEBSITE = "http://www.qingmu.tech";
    public static final String UPDATE_WEBSITE_PACKAGE_INFO = "http://www.qingmu.tech/update.xml";
}
