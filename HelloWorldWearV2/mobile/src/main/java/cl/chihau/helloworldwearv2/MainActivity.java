package cl.chihau.helloworldwearv2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements DataClient.OnDataChangedListener {

    private static final String START_ACTIVITY_PATH = "/start-activity";
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.tv);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Wearable.getDataClient(this).addListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Wearable.getDataClient(this).removeListener(this);
    }


    public void abrirAppWear(View view) {
        Log.d("TEST", "Se ejecuta la App en el Reloj");
        new AbrirAppWearTask().execute();
    }

    private class AbrirAppWearTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... args) {
            Collection<String> nodes = getNodes();
            for (String node : nodes) {
                sendStartActivityMessage(node);
            }
            return null;
        }
    }

    private Collection<String> getNodes() {
        HashSet<String> results = new HashSet<>();

        List<Node> nodes =
                null;
        try {
            nodes = Tasks.await(Wearable.getNodeClient(this).getConnectedNodes());
        } catch (ExecutionException e) {
            Log.e("TEST", "Error: " + e);
        } catch (InterruptedException e) {
            Log.e("TEST", "Error: " + e);
        }

        for (Node node : nodes) {
            Log.d("TEST", "Nodo detectado: " + node.getDisplayName());
            results.add(node.getId());
        }

        return results;
    }

    private void sendStartActivityMessage(String node) {
        Task<Integer> sendTask =
                Wearable.getMessageClient(this).sendMessage(
                        node, START_ACTIVITY_PATH, new byte[0]);

        sendTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("TEST", "Error al enviar el mensaje: " + e.getMessage());
            }
        });

        sendTask.addOnCompleteListener(new OnCompleteListener<Integer>() {
            @Override
            public void onComplete(@NonNull Task<Integer> task) {
                Log.v("TEST", "Mensaje enviado");
            }
        });
    }

    @Override
    public void onDataChanged(@NonNull DataEventBuffer dataEvents) {
        for (DataEvent dataEvent : dataEvents) {
            if (dataEvent.getType() == DataEvent.TYPE_CHANGED) {
                DataMap dataMap = DataMapItem.fromDataItem(dataEvent.getDataItem()).getDataMap();

                String path = dataEvent.getDataItem().getUri().getPath();
                if (path.equals("/step-counter")) {
                    final int steps = dataMap.getInt("step-count");
                    final long time = dataMap.getLong("timestamp");

                    Log.d("TEST", "steps: " + steps + " - " + "timestamp: " + time);

                    // Para actualizar un textView desde otro Thread
                    tv.post(new Runnable() {
                        public void run() {
                            tv.setText("steps: " + steps + " - " + "timestamp: " + time);
                        }
                    });

                }
            }
        }

    }

}
