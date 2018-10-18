package cl.chihau.hellonotifiwear;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    String canal = "my_channel_01";
    public static final String EXTRA_VOICE_REPLY = "extra_voice_reply";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void notificacionSimple(View view) {

        int notificationId = 1;
        // Build intent for notification content
        Intent viewIntent = new Intent(this, MainActivity.class);
        PendingIntent viewPendingIntent = PendingIntent.getActivity(this, 0, viewIntent, 0);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, canal)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Notificación Simple")
                .setContentText("Esta es una notificación simple de prueba")
                .setSubText("Toque para abrir una actividad de prueba")
                // En el teléfono este intent se gatilla cuando se presiona la notificación
                // En el reloj este intent se gatilla cuando se presiona el botón "Abrir en teléfono"
                .setContentIntent(viewPendingIntent);

        mostrarNotificacion(notificationId, notificationBuilder.build());

    }

    public void notificacionMapa(View view) {
        int notificationId = 2;

        Intent viewIntent = new Intent(this, MainActivity.class);
        PendingIntent viewPendingIntent = PendingIntent.getActivity(this, 0, viewIntent, 0);

        Intent mapIntent = new Intent(Intent.ACTION_VIEW);
        Uri geoUri = Uri.parse("geo:0,0?q=" + Uri.encode("Valparaíso"));
        mapIntent.setData(geoUri);
        PendingIntent mapPendingIntent =
                PendingIntent.getActivity(this, 0, mapIntent, 0);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, canal)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Notificación con Mapa")
                        .setContentText("Esta es una notificación con Mapa de prueba")
                        .setSubText("Toque la notificación para abrir una actividad de prueba o " +
                                "toque el botón VER MAPA para abrir Google Maps")
                        .setContentIntent(viewPendingIntent)
                        // En el teléfono aparecerá un botón en la notificación y en el reloj
                        // aparecerá un botón grande al presionar la notificación
                        .addAction(R.drawable.ic_map_white, "Ver mapa", mapPendingIntent);

        mostrarNotificacion(notificationId, notificationBuilder.build());
    }

    public void notificacionAccionSoloReloj(View view) {
        int notificationId = 3;

        Intent actionIntent = new Intent(this, MainActivity.class);
        PendingIntent actionPendingIntent =
                PendingIntent.getActivity(this, 0, actionIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        Intent mapIntent = new Intent(Intent.ACTION_VIEW);
        Uri geoUri = Uri.parse("geo:0,0?q=" + Uri.encode("Valparaíso"));
        mapIntent.setData(geoUri);
        PendingIntent mapPendingIntent =
                PendingIntent.getActivity(this, 0, mapIntent, 0);

        NotificationCompat.Action action =
                new NotificationCompat.Action.Builder(R.mipmap.ic_launcher,
                        "Abrir Actividad", actionPendingIntent)
                        .build();

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, canal)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Notificación sólo en el reloj")
                .setContentText("Esta notificación contiene una acción exclusiva sólo para relojes")
                .setSubText("Toque la notificación para abrir una actividad de prueba o " +
                        "toque el botón VER MAPA para abrir Google Maps o toque el botón Abrir Actividad" +
                        "en el reloj ejecutar la acción exclusiva para los relojes")
                .setContentIntent(actionPendingIntent)
                .addAction(R.mipmap.ic_launcher, "Ver mapa", mapPendingIntent)
                // El botón "Abrir Actividad" sólo aparecerá en el reloj
                .extend(new NotificationCompat.WearableExtender().addAction(action));
        // Cuando se usa .extend, las acciones especificadas con el método
        // NotificationCompat.Builder.addAction() no se muestran en el reloj, en este ejemplo
        // el botón Ver Mapa no aparecerá en el reloj

        mostrarNotificacion(notificationId, notificationBuilder.build());
    }

    public void notificacionTextoLargo(View view) {
        int notificationId = 4;

        Intent actionIntent = new Intent(this, MainActivity.class);
        PendingIntent actionPendingIntent = PendingIntent.getActivity(this, 0, actionIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.BigTextStyle bigStyle = new NotificationCompat.BigTextStyle();
        bigStyle.bigText("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod " +
                "tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, " +
                "quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat." +
                " Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu " +
                "fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in " +
                "culpa qui officia deserunt mollit anim id est laborum.");
        bigStyle.setBigContentTitle("Text extendido");

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, canal)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        // Agrega una imagen como fondo de pantalla para el reloj
                        // En el caso del teléfono lo utiliza como ícono de la notificación
                        .setLargeIcon(BitmapFactory.decodeResource(
                                getResources(), R.mipmap.ic_launcher))
                        .setContentTitle("Notificación con texto extendido")
                        .setContentText("Deslice para ver el texto completo")
                        .setContentIntent(actionPendingIntent)
                        .setStyle(bigStyle);

        mostrarNotificacion(notificationId, notificationBuilder.build());

    }

    public void notificacionAccionDirecta(View view) {
        int notificationId = 5;

        Intent actionIntent = new Intent(this, MainActivity.class);
        PendingIntent actionPendingIntent =
                PendingIntent.getActivity(this, 0, actionIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action.WearableExtender actionExtender =
                new NotificationCompat.Action.WearableExtender()
                        .setHintLaunchesActivity(true)
                        .setHintDisplayActionInline(true);

        NotificationCompat.Action action =
                new NotificationCompat.Action.Builder(R.drawable.ic_map_white, "Abrir Actividad",
                        actionPendingIntent).extend(actionExtender).build();;

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, canal)
                .setContentTitle("Notificación con imagen de fondo")
                .setContentText("Deslice hacia la izquierda para ver el efecto paralax")
                .setSmallIcon(R.mipmap.ic_launcher)
                .extend(new NotificationCompat.WearableExtender().addAction(action));

        mostrarNotificacion(notificationId, notificationBuilder.build());
    }

    public void notificacionSoloTelefono(View view) {
        int notificationId = 6;
        // Build intent for notification content
        Intent viewIntent = new Intent(this, MainActivity.class);
        PendingIntent viewPendingIntent = PendingIntent.getActivity(this, 0, viewIntent, 0);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, canal)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Notificación solo en el teléfono")
                .setContentText("Esta notificación sólo se muestra en teléfono/tablet")
                .setSubText("Toque para abrir una actividad de prueba")
                // En el teléfono este intent se gatilla cuando se presiona la notificación
                // En el reloj este intent se gatilla cuando se presiona el botón "Abrir en teléfono"
                .setContentIntent(viewPendingIntent)
                .setLocalOnly(true); // Este método hace que la notificación no se muestre en el reloj

        mostrarNotificacion(notificationId, notificationBuilder.build());
    }

    public void notificacionRespuestaVoz(View view) {
        int notificationId = 6;

        String replyLabel = "¿A usted le gusta el futbol?";
        String[] replyChoices = getResources().getStringArray(R.array.reply_choices);

        RemoteInput remoteInput = new RemoteInput.Builder(EXTRA_VOICE_REPLY)
                .setLabel(replyLabel)
                .setChoices(replyChoices)
                .build();

        // Create an intent for the reply action
        Intent replyIntent = new Intent(this, SecondActivity.class);
        PendingIntent replyPendingIntent =
                PendingIntent.getActivity(this, 0, replyIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        // Create the reply action and add the remote input
        NotificationCompat.Action action =
                new NotificationCompat.Action.Builder(R.mipmap.ic_launcher,
                        "Responder con la voz", replyPendingIntent)
                        .addRemoteInput(remoteInput)
                        .build();

        // Build the notification and add the action via WearableExtender
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, canal)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Notificación con respuesta por voz")
                        .setContentText("Para responder esta notificación utiliza la voz")
                        .extend(new NotificationCompat.WearableExtender().addAction(action));

        mostrarNotificacion(notificationId, notificationBuilder.build());
    }

    public void notificacionImagen(View view) {
        int notificationId = 10;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background);

        NotificationCompat.BigPictureStyle bigStyle = new NotificationCompat.BigPictureStyle();
        bigStyle.bigPicture(bitmap);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, canal)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Notificación con imágen")
                        .setContentText("Notificación con imágen de prueba")
                        .setStyle(bigStyle);

        mostrarNotificacion(notificationId, notificationBuilder.build());
    }



    public void mostrarNotificacion(int id, Notification notificacion) {
        NotificationManagerCompat mNotificationManager = NotificationManagerCompat.from(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = "my channel";
            String description = "channel description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(canal, name, importance);
            channel.setDescription(description);

            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            manager.createNotificationChannel(channel);
        }

        mNotificationManager.notify(id, notificacion);

    }
}
