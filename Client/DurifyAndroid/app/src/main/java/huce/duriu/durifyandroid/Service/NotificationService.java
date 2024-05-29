package huce.duriu.durifyandroid.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.io.IOException;

import huce.duriu.durifyandroid.Activity.MainActivity;
import huce.duriu.durifyandroid.R;
import huce.duriu.durifyandroid.Model.Music;

public class NotificationService extends Service {
    public static final String CHANNEL_ID = "MusicServiceChannel";
    private MediaPlayer mediaPlayer;
    private MediaSessionCompat mediaSession;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MainActivity.mediaPlayer; // Assuming mediaPlayer is initialized in MainActivity
        mediaSession = new MediaSessionCompat(this, "MusicService");
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if (action != null) {
            switch (action) {
                case "ACTION_PREVIOUS":
                    if (MainActivity.musics.contains(MainActivity.currentPlay)) {
                        int i = MainActivity.musics.indexOf(MainActivity.currentPlay);
                        if (i > 0) {
                            MainActivity.currentPlay = MainActivity.musics.get(i - 1);
                            MainActivity.mediaPlayer.reset();
                            try {
                                MainActivity.mediaPlayer.reset();
                                MainActivity.mediaPlayer.setDataSource(MainActivity.currentPlay.getMusicURL());
                                MainActivity.mediaPlayer.prepare();
                                MainActivity.mediaPlayer.start();
                            }
                            catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                    else if (MainActivity.audios.contains(MainActivity.currentPlay)) {
                        int i = MainActivity.audios.indexOf(MainActivity.currentPlay);
                        if (i > 0) {
                            MainActivity.currentPlay = MainActivity.audios.get(i - 1);
                            try {
                                MainActivity.mediaPlayer.reset();
                                MainActivity.mediaPlayer.setDataSource(MainActivity.currentPlay.getMusicURL());
                                MainActivity.mediaPlayer.prepare();
                                MainActivity.mediaPlayer.start();
                            }
                            catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                        }
                    }
                    break;
                case "ACTION_PAUSE":
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    } else {
                        mediaPlayer.start();
                    }
                    break;
                case "ACTION_NEXT":
                    if (MainActivity.musics.contains(MainActivity.currentPlay)) {
                        int i = MainActivity.musics.indexOf(MainActivity.currentPlay);
                        if (i < MainActivity.musics.size() - 1) {
                            MainActivity.currentPlay = MainActivity.musics.get(i + 1);
                            MainActivity.mediaPlayer.reset();
                            try {
                                MainActivity.mediaPlayer.reset();
                                MainActivity.mediaPlayer.setDataSource(MainActivity.currentPlay.getMusicURL());
                                MainActivity.mediaPlayer.prepare();
                                MainActivity.mediaPlayer.start();
                            }
                            catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                    else if (MainActivity.audios.contains(MainActivity.currentPlay)) {
                        int i = MainActivity.audios.indexOf(MainActivity.currentPlay);
                        if (i < MainActivity.audios.size() - 1) {
                            MainActivity.currentPlay = MainActivity.audios.get(i + 1);
                            try {
                                MainActivity.mediaPlayer.reset();
                                MainActivity.mediaPlayer.setDataSource(MainActivity.currentPlay.getMusicURL());
                                MainActivity.mediaPlayer.prepare();
                                MainActivity.mediaPlayer.start();
                            }
                            catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                        }
                    }
                    break;
            }
        }
        Music currentPlay = MainActivity.currentPlay;
        if (currentPlay != null) {
            showNotification(currentPlay);
        }
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Music Service Channel",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }

    private void showNotification(Music currentPlay) {
        PendingIntent emptyPendingIntent = PendingIntent.getActivity(this, 0, new Intent(), PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Now Playing")
                .setContentText(currentPlay.getMusicName())
                .setSmallIcon(R.drawable.logo)
                .setContentIntent(emptyPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mediaSession.getSessionToken())
                        .setShowActionsInCompactView(1, 2, 3));
        if(MainActivity.mediaPlayer.isPlaying()){
            notification
                    .addAction(R.drawable.baseline_skip_previous_24, "Previous", getPendingIntent("ACTION_PREVIOUS"))
                    .addAction(R.drawable.baseline_pause_circle_outline_24, "Pause", getPendingIntent("ACTION_PAUSE"))
                    .addAction(R.drawable.baseline_skip_next_24, "Next", getPendingIntent("ACTION_NEXT"));
        } else {
            notification
                    .addAction(R.drawable.baseline_skip_previous_24, "Previous", getPendingIntent("ACTION_PREVIOUS"))
                    .addAction(R.drawable.baseline_play_circle_outline_24, "Pause", getPendingIntent("ACTION_PAUSE"))
                    .addAction(R.drawable.baseline_skip_next_24, "Next", getPendingIntent("ACTION_NEXT"));
        }

        Notification notification1 = notification.build();
        startForeground(1, notification1);
    }

    private PendingIntent getPendingIntent(String action) {
        Intent intent = new Intent(this, NotificationService.class);
        intent.setAction(action);
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        stopForeground(true);
    }
}
