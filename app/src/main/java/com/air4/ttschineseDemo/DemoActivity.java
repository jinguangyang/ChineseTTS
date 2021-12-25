package com.air4.ttschineseDemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.air4.chinesetts.dispatcher.OnTtsStateListener;
import com.air4.chinesetts.dispatcher.TtsStateDispatcher;
import com.air4.chinesetts.tts.TtsManager;
import com.air4.chinesetts.utils.ThreadPoolManager;

public class DemoActivity extends AppCompatActivity {
    private static final String DEFAULT_INPUT_TEXT = "君不见,黄河之水天上来,奔流到海不复回,君不见,高堂明镜悲白发,朝如青丝暮成雪,人生得意须尽欢,莫使金樽空对月";
    private View speakBtn;
    private RadioGroup speedGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ttsdemo);

        TtsManager.getInstance().init(this);

        TtsStateDispatcher.getInstance().addListener(new OnTtsStateListener() {
            @Override
            public void onTtsReady() {
                speakBtn.setEnabled(true);
            }

            @Override
            public void onTtsStart(String text) {
            }

            @Override
            public void onTtsStop() {
            }
        });

        EditText input = findViewById(R.id.input);
        input.setHint(DEFAULT_INPUT_TEXT);

        speedGroup = findViewById(R.id.speed_chooser);
        speedGroup.check(R.id.normal);

        speakBtn = findViewById(R.id.start);
        speakBtn.setEnabled(false);
        speakBtn.setOnClickListener(v ->
                ThreadPoolManager.getInstance().execute(() -> {
                    float speed;
                    switch (speedGroup.getCheckedRadioButtonId()) {
                        case R.id.fast:
                            speed = 0.8F;
                            break;
                        case R.id.slow:
                            speed = 1.2F;
                            break;
                        case R.id.normal:
                        default:
                            speed = 1.0F;
                            break;
                    }

                    String inputText = input.getText().toString();
                    if (TextUtils.isEmpty(inputText)) {
                        inputText = DEFAULT_INPUT_TEXT;
                    }
                    TtsManager.getInstance().speak(inputText, speed, true);
                }));

        findViewById(R.id.stop).setOnClickListener(v ->
                TtsManager.getInstance().stopTts());
    }
}