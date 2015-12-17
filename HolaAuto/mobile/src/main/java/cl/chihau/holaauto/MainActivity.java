package cl.chihau.holaauto;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final int DELAY_MILLIS = 15000;
    public static final int COUNT_DOWN_INTERVAL = 1000;
    private TextView mTextView;

    private CountDownTimer mCountDownTimer;
    private TextView mAutoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAutoText = (TextView) findViewById(R.id.help_text);
        mTextView = (TextView) findViewById(R.id.context_text);
        Button mStartButton = (Button) findViewById(R.id.start_conversation);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCountDownTimer();
            }
        });
    }

    private void startCountDownTimer() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }

        mCountDownTimer = new CountDownTimer(DELAY_MILLIS, COUNT_DOWN_INTERVAL) {

            public void onTick(long millisUntilFinished) {
                mAutoText.setVisibility(View.VISIBLE);
                mTextView.setText("Posting notification in " + (millisUntilFinished / 1000));
            }

            public void onFinish() {
                mAutoText.setVisibility(View.GONE);
                mTextView.setText("Notification Posted");
                sendMessage();
            }
        };
        mCountDownTimer.start();
    }

    private void sendMessage() {
        Intent serviceIntent = new Intent(MainActivity.this, MessagingService.class);
        serviceIntent.setAction(MessagingService.SEND_MESSAGE_ACTION);
        startService(serviceIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy called");
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }
}