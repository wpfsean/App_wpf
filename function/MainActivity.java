package com.zhketech.client.app.sip;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.zhketech.client.app.sip.phone.tils.Linphone;
import com.zhketech.client.app.sip.phone.tils.MessageCallback;
import com.zhketech.client.app.sip.phone.tils.PhoneCallback;
import com.zhketech.client.app.sip.phone.tils.SipService;

import org.linphone.core.LinphoneCall;
import org.linphone.core.LinphoneChatMessage;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @BindView(R.id.dial_num)
    EditText mDialNum;
    @BindView(R.id.hang_up)
    Button mHangUp;
    @BindView(R.id.accept_call)
    Button mCallIn;
    @BindView(R.id.toggle_speaker)
    Button mToggleSpeaker;
    @BindView(R.id.toggle_mute)
    Button mToggleMute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Linphone.addCallback(null, new PhoneCallback() {
            @Override
            public void incomingCall(LinphoneCall linphoneCall) {
                super.incomingCall(linphoneCall);
                // 开启铃声免提
                Linphone.toggleSpeaker(true);
                mCallIn.setVisibility(View.VISIBLE);
                mHangUp.setVisibility(View.VISIBLE);
            }

            @Override
            public void outgoingInit() {
                super.outgoingInit();
                mHangUp.setVisibility(View.VISIBLE);
            }

            @Override
            public void callConnected() {
                super.callConnected();
                // 视频通话默认免提，语音通话默认非免提
                Linphone.toggleSpeaker(Linphone.getVideoEnabled());
                // 所有通话默认非静音
                Linphone.toggleMicro(false);
                mCallIn.setVisibility(View.GONE);
                mToggleSpeaker.setVisibility(View.VISIBLE);
                mToggleMute.setVisibility(View.VISIBLE);
            }

            @Override
            public void callEnd() {
                super.callEnd();
                sendBroadcast(new Intent(VideoActivity.RECEIVE_FINISH_VIDEO_ACTIVITY));
                mCallIn.setVisibility(View.GONE);
                mHangUp.setVisibility(View.GONE);
                mToggleMute.setVisibility(View.GONE);
                mToggleSpeaker.setVisibility(View.GONE);
            }
        });
    }

    @OnClick(R.id.audio_call)
    public void audioCall() {
        String dialNum = mDialNum.getText().toString();
        Linphone.callTo(dialNum, false);
    }

    @OnClick(R.id.video_call)
    public void videoCall() {
        String dialNum = mDialNum.getText().toString();
        Linphone.callTo(dialNum, true);
        startActivity(new Intent(MainActivity.this, VideoActivity.class));
    }

    @OnClick(R.id.hang_up)
    public void hangUp() {
        Linphone.hangUp();
    }

    @OnClick(R.id.accept_call)
    public void acceptCall() {
        Linphone.acceptCall();
        if (Linphone.getVideoEnabled()) {
            startActivity(new Intent(MainActivity.this, VideoActivity.class));
        }
    }

    @OnClick(R.id.toggle_mute)
    public void toggleMute() {
        Linphone.toggleMicro(!Linphone.getLC().isMicMuted());
    }

    @OnClick(R.id.toggle_speaker)
    public void toggleSpeaker() {
        Linphone.toggleSpeaker(!Linphone.getLC().isSpeakerEnabled());
    }


    @OnClick(R.id.send_message)
    public void send_Mess(View view) {


    }

    @Override
    protected void onResume() {
        super.onResume();

        SipService.addMessageCallback(new MessageCallback() {
            @Override
            public void receiverMessage(LinphoneChatMessage linphoneChatMessage) {
                final String from = linphoneChatMessage.getFrom().getUserName();
                final String to = linphoneChatMessage.getTo().getUserName();
                final String content = linphoneChatMessage.getText();
                System.out.print(from);
                System.out.print(from);
                System.out.print(from);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, from + "\t from \t" + content + "\t to \t" + to, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
