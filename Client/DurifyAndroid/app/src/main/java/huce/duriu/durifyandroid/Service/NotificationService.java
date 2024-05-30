package huce.duriu.durifyandroid.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaMetadata;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.io.IOException;

import huce.duriu.durifyandroid.Activity.MainActivity;
import huce.duriu.durifyandroid.R;

public class NotificationService extends Service {
    public static final String CHANNEL_ID = "MusicServiceChannel";
    public static MediaSessionCompat mediaSession;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaSession = new MediaSessionCompat(this, "MusicService");
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if (MainActivity.currentPlay != null) {
            showNotification();
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

    private void showNotification() {
        PendingIntent emptyPendingIntent = PendingIntent.getActivity(this, 0, new Intent(), PendingIntent.FLAG_IMMUTABLE);

        mediaSession.setMetadata(
                    new MediaMetadataCompat.Builder()
                            .putLong(MediaMetadata.METADATA_KEY_DURATION, MainActivity.mediaPlayer.getDuration())
                            .build()
        );

        mediaSession.setPlaybackState(
                    new PlaybackStateCompat.Builder()
                            .setState(PlaybackStateCompat.STATE_PLAYING, MainActivity.mediaPlayer.getCurrentPosition(), 0f)
                            .setActions(PlaybackStateCompat.ACTION_PAUSE |
                                    PlaybackStateCompat.ACTION_PLAY |
                                    PlaybackStateCompat.ACTION_SKIP_TO_NEXT |
                                    PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)
                            .build()
        );

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(MainActivity.currentPlay.getMusicName())
                .setContentText(MainActivity.currentPlay.getMusicArtist())
                .setSmallIcon(R.drawable.logo)
                .setContentIntent(emptyPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mediaSession.getSessionToken())
                        .setShowActionsInCompactView(1));

        Notification notification1 = notification.build();
        startForeground(1, notification1);

        mediaSession.setCallback(new MediaSessionCompat.Callback() {
            // Implement callbacks
            @Override
            public boolean onMediaButtonEvent(Intent mediaButtonEvent) {
                return true;
            }

            @Override
            public void onPlay() {
                super.onPlay();
                if (MainActivity.mediaPlayer.isPlaying()) {
                    MainActivity.mediaPlayer.pause();
                } else {
                    MainActivity.mediaPlayer.start();
                }
            }

            @Override
            public void onPause() {
                super.onPause();
                if (MainActivity.mediaPlayer.isPlaying()) {
                    MainActivity.mediaPlayer.pause();
                } else {
                    MainActivity.mediaPlayer.start();
                }
            }

            @Override
            public void onSkipToNext() {
                super.onSkipToNext();
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
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } else if (MainActivity.audios.contains(MainActivity.currentPlay)) {
                    int i = MainActivity.audios.indexOf(MainActivity.currentPlay);
                    if (i < MainActivity.audios.size() - 1) {
                        MainActivity.currentPlay = MainActivity.audios.get(i + 1);
                        try {
                            MainActivity.mediaPlayer.reset();
                            MainActivity.mediaPlayer.setDataSource(MainActivity.currentPlay.getMusicURL());
                            MainActivity.mediaPlayer.prepare();
                            MainActivity.mediaPlayer.start();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                notification.setContentTitle(MainActivity.currentPlay.getMusicName());
                notification.setContentText(MainActivity.currentPlay.getMusicArtist());
                Notification notification1 = notification.build();
                startForeground(1, notification1);
            }

            @Override
            public void onSkipToPrevious() {
                super.onSkipToPrevious();
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
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } else if (MainActivity.audios.contains(MainActivity.currentPlay)) {
                    int i = MainActivity.audios.indexOf(MainActivity.currentPlay);
                    if (i > 0) {
                        MainActivity.currentPlay = MainActivity.audios.get(i - 1);
                        try {
                            MainActivity.mediaPlayer.reset();
                            MainActivity.mediaPlayer.setDataSource(MainActivity.currentPlay.getMusicURL());
                            MainActivity.mediaPlayer.prepare();
                            MainActivity.mediaPlayer.start();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                notification.setContentTitle(MainActivity.currentPlay.getMusicName());
                notification.setContentText(MainActivity.currentPlay.getMusicArtist());
                Notification notification1 = notification.build();
                startForeground(1, notification1);
            }

            @Override
            public void onSeekTo(long pos) {
                // your seek() implements
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (MainActivity.mediaPlayer != null) {
            MainActivity.mediaPlayer.release();
        }
        stopForeground(true);
    }
}
