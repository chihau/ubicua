package cl.chihau.helloworldwearv2;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainActivity extends WearableActivity {

    private TextView mTextView;

    private ScheduledExecutorService mGeneratorExecutor;
    private ScheduledFuture<?> mDataItemGeneratorFuture;

    private DataClient mDataClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = findViewById(R.id.text);
        mDataClient = Wearable.getDataClient(this);

        // Enables Always-on
        setAmbientEnabled();

        mGeneratorExecutor = new ScheduledThreadPoolExecutor(1);
    }

    @Override
    public void onResume() {
        super.onResume();
        mDataItemGeneratorFuture = mGeneratorExecutor.scheduleWithFixedDelay(
                new enviarDatos(), 1, 5, TimeUnit.SECONDS);
    }

    @Override
    public void onPause() {
        super.onPause();
        mDataItemGeneratorFuture.cancel(true /* mayInterruptIfRunning */);
    }

    private class enviarDatos implements Runnable {
        private int count = 0;

        @Override
        public void run() {
            sendStepCount(count++, System.currentTimeMillis());
        }
    }

    public void sendStepCount(int steps, long timestamp) {
        Log.d("TEST", "steps: " + steps);
        PutDataMapRequest putDataMapRequest = PutDataMapRequest.create("/step-counter");

        putDataMapRequest.getDataMap().putInt("step-count", steps);
        putDataMapRequest.getDataMap().putLong("timestamp", timestamp);

        PutDataRequest request = putDataMapRequest.asPutDataRequest().setUrgent();

        Task<DataItem> putDataTask = mDataClient.putDataItem(request);

        putDataTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("TEST", "Error: " + e.getMessage());
            }
        });

        putDataTask.addOnCompleteListener(new OnCompleteListener<DataItem>() {
            @Override
            public void onComplete(@NonNull Task<DataItem> task) {
                Log.v("TEST", "request completada");
            }
        });
    }
}
