package yonsei_church.yonsei.tv.view.playback.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v17.leanback.app.VideoSupportFragment;
import android.support.v17.leanback.app.VideoSupportFragmentGlueHost;
import android.support.v17.leanback.media.MediaPlayerAdapter;
import android.support.v17.leanback.media.PlaybackGlue;
import android.support.v17.leanback.media.PlaybackTransportControlGlue;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import yonsei_church.yonsei.tv.api.ApiProvider;
import yonsei_church.yonsei.tv.api.ITvAPI;
import yonsei_church.yonsei.tv.api.vo.VideoItem;
import yonsei_church.yonsei.tv.utils.LogUtil;
import yonsei_church.yonsei.tv.utils.PreferenceManager;
import yonsei_church.yonsei.tv.utils.WaitingDialog;

public class PlaybackVideoFragment extends VideoSupportFragment {
    /* access modifiers changed from: private */
    public static PreferenceManager mPref;
    private ApiProvider apiProvider;
    private String mBannerUrl = "";
    /* access modifiers changed from: private */
    public String mKey = "";
    /* access modifiers changed from: private */
    public CountDownTimer mLiveCountDownTimer;
    /* access modifiers changed from: private */
    public String mLiveTime = "";
    /* access modifiers changed from: private */
    public String mLiveUrl = "";
    /* access modifiers changed from: private */
    public String mSeq = "";
    /* access modifiers changed from: private */
    public PlaybackTransportControlGlue<MediaPlayerAdapter> mTransportControlGlue;
    /* access modifiers changed from: private */
    public VideoItem mVideo;
    /* access modifiers changed from: private */
    public String mVideoUrl = "";

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getActivity().getWindow().addFlags(128);
        mPref = PreferenceManager.getInstance(getActivity());
        this.mVideo = (VideoItem) getActivity().getIntent().getSerializableExtra("Video");
        this.mKey = getActivity().getIntent().getStringExtra("play_key");
        LogUtil.e("PlaybackVideoFragment mKey : " + this.mKey);
        this.mLiveUrl = getActivity().getIntent().getStringExtra("liveUrl");
        this.mLiveTime = getActivity().getIntent().getStringExtra("liveTime");
        this.mBannerUrl = getActivity().getIntent().getStringExtra("bannerUrl");
        VideoSupportFragmentGlueHost videoSupportFragmentGlueHost = new VideoSupportFragmentGlueHost(this);
        MediaPlayerAdapter mediaPlayerAdapter = new MediaPlayerAdapter(getActivity());
        mediaPlayerAdapter.setRepeatAction(0);
        this.mTransportControlGlue = new PlaybackTransportControlGlue<>(getActivity(), mediaPlayerAdapter);
        this.mTransportControlGlue.setHost(videoSupportFragmentGlueHost);
        this.mTransportControlGlue.setControlsOverlayAutoHideEnabled(true);
        this.mTransportControlGlue.addPlayerCallback(new PlaybackGlue.PlayerCallback() {
            public void onPreparedStateChanged(PlaybackGlue playbackGlue) {
                super.onPreparedStateChanged(playbackGlue);
                LogUtil.e("onPreparedStateChanged glue : " + playbackGlue.isPrepared() + ", mSeq : " + PlaybackVideoFragment.this.mSeq);
                if (playbackGlue.isPrepared()) {
                    PlaybackVideoFragment.this.mTransportControlGlue.play();
                    if (PlaybackVideoFragment.this.mLiveTime != null && !PlaybackVideoFragment.this.mLiveTime.isEmpty()) {
                        PlaybackVideoFragment.this.setLiveCountDownTimer(PlaybackVideoFragment.this.mLiveTime);
                    }
                    if (PlaybackVideoFragment.mPref.getString("userId", "") == null || PlaybackVideoFragment.mPref.getString("userId", "").isEmpty()) {
                        if (PlaybackVideoFragment.this.mVideo != null) {
                            PlaybackVideoFragment.this.getContentClick("", PlaybackVideoFragment.this.mSeq);
                        }
                    } else if (PlaybackVideoFragment.this.mVideo != null) {
                        PlaybackVideoFragment.this.getContentClick(PlaybackVideoFragment.mPref.getString("userId", ""), PlaybackVideoFragment.this.mSeq);
                    }
                }
            }

            public void onPlayStateChanged(PlaybackGlue playbackGlue) {
                super.onPlayStateChanged(playbackGlue);
                LogUtil.e("onPlayStateChanged : " + playbackGlue.isPlaying());
            }

            public void onPlayCompleted(PlaybackGlue playbackGlue) {
                super.onPlayCompleted(playbackGlue);
                LogUtil.e("onPlayCompleted mKey : " + PlaybackVideoFragment.this.mKey + ", mSeq : " + PlaybackVideoFragment.this.mSeq);
                if (PlaybackVideoFragment.this.mKey != null && !PlaybackVideoFragment.this.mKey.isEmpty()) {
                    PlaybackVideoFragment.this.getNextContentInfo(PlaybackVideoFragment.this.mKey, PlaybackVideoFragment.this.mSeq);
                } else if (PlaybackVideoFragment.this.mLiveUrl == null || PlaybackVideoFragment.this.mLiveUrl.isEmpty()) {
                    PlaybackVideoFragment.this.getActivity().finish();
                } else {
                    PlaybackVideoFragment.this.getActivity().finish();
                }
            }
        });
        if (this.mLiveUrl != null && !this.mLiveUrl.isEmpty()) {
            this.mTransportControlGlue.setSubtitle("");
            this.mTransportControlGlue.getPlayerAdapter().setDataSource(Uri.parse(this.mLiveUrl));
            this.mTransportControlGlue.playWhenPrepared();
        } else if (this.mBannerUrl == null || this.mBannerUrl.isEmpty()) {
            if (this.mVideo.getSeq() != null) {
                this.mSeq = this.mVideo.getSeq();
            }
            this.mTransportControlGlue.setSubtitle(this.mVideo.getTitle());
            if (this.mVideo.getUrl().contains("mp4") || this.mVideo.getUrl().contains("mp3") || this.mVideo.getUrl().contains("m3u8")) {
                this.mTransportControlGlue.getPlayerAdapter().setDataSource(Uri.parse(this.mVideo.getUrl()));
                this.mTransportControlGlue.playWhenPrepared();
                return;
            }
            try {
                this.mTransportControlGlue.getPlayerAdapter().setDataSource(Uri.parse(getVimeoUrl(this.mVideo.getUrl().substring(this.mVideo.getUrl().lastIndexOf("/") + 1))));
                this.mTransportControlGlue.playWhenPrepared();
            } catch (Exception e) {
                LogUtil.e("재생 에러입니다. : " + e.toString());
                getActivity().finish();
            }
        } else {
            this.mTransportControlGlue.setSubtitle("");
            try {
                this.mTransportControlGlue.getPlayerAdapter().setDataSource(Uri.parse(getVimeoUrl(this.mBannerUrl.substring(this.mBannerUrl.lastIndexOf("/") + 1))));
                this.mTransportControlGlue.playWhenPrepared();
            } catch (Exception e2) {
                LogUtil.e("재생 에러입니다. : " + e2.toString());
                getActivity().finish();
            }
        }
    }

    public void onPause() {
        super.onPause();
        if (this.mTransportControlGlue != null) {
            this.mTransportControlGlue.pause();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.mLiveCountDownTimer != null) {
            this.mLiveCountDownTimer.cancel();
        }
    }

    /* access modifiers changed from: private */
    public void setLiveCountDownTimer(String str) {
        Date date;
        try {
            date = new SimpleDateFormat("yyyyMMddHHmmss").parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
            date = null;
        }
        this.mLiveCountDownTimer = new CountDownTimer(date.getTime() - System.currentTimeMillis(), 1000) {
            public void onTick(long j) {
            }

            public void onFinish() {
                if (PlaybackVideoFragment.this.mLiveCountDownTimer != null) {
                    PlaybackVideoFragment.this.mLiveCountDownTimer.cancel();
                }
                if (PlaybackVideoFragment.this.mLiveUrl != null && !PlaybackVideoFragment.this.mLiveUrl.isEmpty()) {
                    PlaybackVideoFragment.this.getActivity().finish();
                }
            }
        };
        this.mLiveCountDownTimer.start();
    }

    private String getVimeoUrl(final String str) {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                String str = "http://player.vimeo.com/video/" + str + "/config";
                DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
                try {
                    HttpPost httpPost = new HttpPost(str);
                    httpPost.setHeader("Content-Type", "application/json");
                    try {
                        JSONArray jSONArray = (JSONArray) ((JSONObject) ((JSONObject) new JSONObject(EntityUtils.toString(defaultHttpClient.execute(httpPost).getEntity())).get("request")).get("files")).get("progressive");
                        int i = 0;
                        while (true) {
                            if (i >= jSONArray.length()) {
                                break;
                            }
                            JSONObject jSONObject = (JSONObject) jSONArray.get(i);
                            if ("720".equals(jSONObject.get("height"))) {
                                String unused = PlaybackVideoFragment.this.mVideoUrl = jSONObject.get("url").toString();
                                break;
                            } else {
                                String unused2 = PlaybackVideoFragment.this.mVideoUrl = jSONObject.get("url").toString();
                                i++;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                } catch (Throwable th) {
                    defaultHttpClient.getConnectionManager().closeExpiredConnections();
                    throw th;
                }
                defaultHttpClient.getConnectionManager().closeExpiredConnections();
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this.mVideoUrl;
    }

    /* access modifiers changed from: private */
    public void getContentClick(String str, String str2) {
        WaitingDialog.showWaitingDialog(getActivity());
        ApiProvider apiProvider2 = this.apiProvider;
        ((ITvAPI) ApiProvider.createService(ITvAPI.class, getActivity())).getContentClick(str, Integer.parseInt(str2)).enqueue(new Callback<Void>() {
            public void onResponse(Call<Void> call, Response<Void> response) {
                WaitingDialog.cancelWaitingDialog();
            }

            public void onFailure(Call<Void> call, Throwable th) {
                WaitingDialog.cancelWaitingDialog();
                LogUtil.e("onFailure : " + th.toString());
            }
        });
    }

    /* access modifiers changed from: private */
    public void getNextContentInfo(String str, String str2) {
        WaitingDialog.showWaitingDialog(getActivity());
        ApiProvider apiProvider2 = this.apiProvider;
        ((ITvAPI) ApiProvider.createService(ITvAPI.class, getActivity())).getNextVideoInfo(str, Integer.parseInt(str2)).enqueue(new Callback<VideoItem>() {
            public void onResponse(Call<VideoItem> call, Response<VideoItem> response) {
                WaitingDialog.cancelWaitingDialog();
                VideoItem body = response.body();
                String unused = PlaybackVideoFragment.this.mSeq = body.getNextseq();
                PlaybackVideoFragment.this.setDataSource(body);
            }

            public void onFailure(Call<VideoItem> call, Throwable th) {
                WaitingDialog.cancelWaitingDialog();
                LogUtil.e("onFailure : " + th.toString());
            }
        });
    }

    /* access modifiers changed from: private */
    public void setDataSource(VideoItem videoItem) {
        if (videoItem.getNexturl() == null || videoItem.getNexturl().isEmpty()) {
            getActivity().finish();
            return;
        }
        this.mTransportControlGlue.setSubtitle(videoItem.getNexttitle());
        if (videoItem.getNexturl().contains("mp4") || videoItem.getNexturl().contains("mp3") || videoItem.getNexturl().contains("m3u8")) {
            this.mTransportControlGlue.getPlayerAdapter().setDataSource(Uri.parse(videoItem.getNexturl()));
            this.mTransportControlGlue.playWhenPrepared();
            return;
        }
        try {
            this.mTransportControlGlue.getPlayerAdapter().setDataSource(Uri.parse(getVimeoUrl(videoItem.getNexturl().substring(videoItem.getNexturl().lastIndexOf("/") + 1))));
            this.mTransportControlGlue.playWhenPrepared();
        } catch (Exception e) {
            LogUtil.e("재생 에러입니다. : " + e.toString());
            getActivity().finish();
        }
    }
}
