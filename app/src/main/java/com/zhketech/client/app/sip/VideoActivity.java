package com.zhketech.client.app.sip;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;

import com.zhketech.client.app.sip.sip.tils.Linphone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoActivity extends AppCompatActivity {
    @BindView(R.id.video_rendering)
    SurfaceView mRenderingView;
    @BindView(R.id.video_preview)
    SurfaceView mPreviewView;

    private FinishVideoActivityReceiver mReceiver;
    public static final String RECEIVE_FINISH_VIDEO_ACTIVITY = "receive_finish_video_activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        ButterKnife.bind(this);
        IntentFilter intentFilter = new IntentFilter(RECEIVE_FINISH_VIDEO_ACTIVITY);
        mReceiver = new FinishVideoActivityReceiver();
        registerReceiver(mReceiver, intentFilter);
        Linphone.setAndroidVideoWindow(new SurfaceView[]{mRenderingView}, new SurfaceView[]{mPreviewView});
    }

    @Override
    protected void onResume() {
        super.onResume();
        Linphone.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Linphone.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
        Linphone.onDestroy();
    }

    @OnClick(R.id.video_hang)
    public void hang() {
        Linphone.hangUp();
        finish();
    }

    @OnClick(R.id.video_mute)
    public void mute() {
        Linphone.toggleMicro(!Linphone.getLC().isMicMuted());
    }

    @OnClick(R.id.video_speaker)
    public void speaker() {
        Linphone.toggleSpeaker(!Linphone.getLC().isSpeakerEnabled());
    }

    public class FinishVideoActivityReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            VideoActivity.this.finish();
        }
    }
}