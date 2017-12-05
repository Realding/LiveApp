package com.example.lenovo.liveapp;


import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.utils.Log;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LiveActivity extends AppCompatActivity {

    private static final String TAG = LiveActivity.class.getSimpleName();
    private String mUrl;
    private String mTitle;
    private ImageView mBackButton;
    private TextView mTextView;
    private TextView mSysTime;
    private io.vov.vitamio.widget.VideoView mVideoView;
    private static final int RETRY_TIMES = 5;
    private static final int AUTO_HIDE_TIME = 5000;
    private int mCount;
    private RelativeLayout mLoadingLayout;
    private Uri mUri;
    private RelativeLayout mRootView;
    private LinearLayout mTopLayout;
    private LinearLayout mBottomLayout;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private ImageView mPlayButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);
        mUrl = getIntent().getStringExtra("url");
        mTitle = getIntent().getStringExtra("title");
        Log.d(TAG,">> onCreate mTitle=" + mTitle + ", mUrl=" +mUrl);
        initView();
        initPlayer();
    }


    private void initPlayer() {
        Vitamio.isInitialized(getApplicationContext());
        Vitamio.initialize(this);
        //设置视频解码监听
        if (!LibsChecker.checkVitamioLibs(this)) {
            return;
        }
        mUri = Uri.parse(mUrl);
        mPlayButton = (ImageView) findViewById(R.id.iv_play);//播放按钮
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mVideoView.isPlaying()){
                    mVideoView.stopPlayback();
                    mPlayButton.setImageResource(R.drawable.pause_button_normal);
                }else {
                    mVideoView.setVideoURI(mUri);
                    mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mVideoView.start();
                        }
                    });
                    mPlayButton.setImageResource(R.drawable.play_button_normal);
                }
            }
        });

        mVideoView = (VideoView) findViewById(R.id.surface_view);//播放界面

        mVideoView.setVideoURI(mUri);//解析地址
        /*new AlertDialog.Builder(LiveActivity.this)
                .setMessage("hello")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ;
                    }
                }).setCancelable(false).show();
        */
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {

                mVideoView.start();
            }
        });

        /*MediaController controller = new MediaController(this);
        mVideoView.setMediaController(controller);*/
        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                if(mCount > RETRY_TIMES){
                    new AlertDialog.Builder(LiveActivity.this)
                            .setMessage(R.string.err_message)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    LiveActivity.this.finish();
                                }
                            }).setCancelable(false).show();
                }else{
                    mVideoView.stopPlayback();
                    mVideoView.setVideoURI(Uri.parse(mUrl));
                }
                mCount++;
                return false;
            }
        });
        mVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {
                switch (i){
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                        mLoadingLayout.setVisibility(View.VISIBLE);
                    break;
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                    case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
                    case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
                        mLoadingLayout.setVisibility(View.GONE);
                    break;
                }
                return false;
            }
        });

    }

    private void initView() {
        mBackButton = (ImageView) findViewById(R.id.iv_back);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mTextView = (TextView) findViewById(R.id.tv_title);
        mTextView.setText(mTitle);
        mSysTime = (TextView)findViewById(R.id.tv_systime);
        mSysTime.setText(getCurrentTime());
        mLoadingLayout = (RelativeLayout) findViewById(R.id.rl_loading_layout);
        mRootView = (RelativeLayout) findViewById(R.id.activity_live);
        mTopLayout = (LinearLayout) findViewById(R.id.ll_top_layout);
        mBottomLayout = (LinearLayout) findViewById(R.id.ll_play_layout);

        mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mBottomLayout.getVisibility() == View.VISIBLE
                        || mTopLayout.getVisibility() == View.VISIBLE){
                    mTopLayout.setVisibility(View.GONE);
                    mBottomLayout.setVisibility(View.GONE);
                    return;
                }

                if(mVideoView.isPlaying()){
                    //正在播放
                    mPlayButton.setImageResource(R.drawable.play_button_normal);
                }else{
                    mPlayButton.setImageResource(R.drawable.pause_button_normal);
                }
                mTopLayout.setVisibility(View.VISIBLE);
                mBottomLayout.setVisibility(View.VISIBLE);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mTopLayout.setVisibility(View.GONE);
                        mBottomLayout.setVisibility(View.GONE);
                    }
                },AUTO_HIDE_TIME);
            }
        });
    }
    private String getCurrentTime(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(c.getTime());
        return time;
    }

    @Override
    protected void onStop(){
        super.onStop();
        mCount = 0;
        if(mVideoView != null){
            mVideoView.stopPlayback();
        }
    }
}
