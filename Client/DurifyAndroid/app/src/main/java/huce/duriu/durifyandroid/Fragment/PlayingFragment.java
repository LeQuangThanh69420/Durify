package huce.duriu.durifyandroid.Fragment;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaMetadata;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import huce.duriu.durifyandroid.Activity.MainActivity;
import huce.duriu.durifyandroid.R;
import huce.duriu.durifyandroid.Service.NotificationService;

public class PlayingFragment extends Fragment {
    private TextView titlePlaying;
    private ShapeableImageView musicImage;
    private float x = 0f;
    private boolean isMuted = false;
    private int previousVolume;
    private SeekBar seekBar;
    private TextView currentTime;
    private TextView durationTime;
    private ImageView buttonVolume;
    private ImageView buttonPre;
    private ImageView buttonPlay;
    private ImageView buttonNext;
    private ImageView buttonLoop;

    public PlayingFragment() {
        // Required empty public constructor
    }

    public static String formatMMSS(int time){
        time = time / 1000;
        int minutes = time / 60;
        int seconds = time % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public static PlayingFragment newInstance(String param1, String param2) {
        PlayingFragment fragment = new PlayingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_playing, container, false);
        if (MainActivity.mediaPlayer.isPlaying() || MainActivity.mediaPlayer != null) {
            Intent serviceIntent = new Intent(view.getContext(), NotificationService.class);
            view.getContext().startService(serviceIntent);
        }
        titlePlaying = view.findViewById(R.id.TitlePlaying);
        musicImage = view.findViewById(R.id.MusicImage);
        seekBar = view.findViewById(R.id.seekBar);
        currentTime = view.findViewById(R.id.currentTime);
        durationTime = view.findViewById(R.id.durationTime);
        buttonVolume = view.findViewById(R.id.buttonVolume);
        buttonPre = view.findViewById(R.id.buttonPre);
        buttonPlay = view.findViewById(R.id.buttonPlay);
        buttonNext = view.findViewById(R.id.buttonNext);
        buttonLoop = view.findViewById(R.id.buttonLoop);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (MainActivity.mediaPlayer != null && fromUser) {
                    MainActivity.mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        buttonVolume.setOnClickListener(v -> {
            AudioManager audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
            if (audioManager != null) {
                if (isMuted) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, previousVolume, 0);
                    isMuted = false;
                } else {
                    previousVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                    isMuted = true;
                }
            }
        });

        buttonPre.setOnClickListener(v -> {
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
                    File file = new File(MainActivity.audios.get(i - 1).getMusicURL());
                    if (file.exists()) {
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
                    else {
                        MainActivity.audios.remove(MainActivity.audios.get(i - 1));
                        Toast.makeText(v.getContext(), "Failed to open file, remove it from list", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            if (MainActivity.mediaPlayer.isPlaying() || MainActivity.mediaPlayer != null) {
                Intent serviceIntent = new Intent(view.getContext(), NotificationService.class);
                view.getContext().startService(serviceIntent);
            }
        });

        buttonPlay.setOnClickListener(v -> {
            if(MainActivity.mediaPlayer.isPlaying()) {
                MainActivity.mediaPlayer.pause();
            }
            else {
                MainActivity.mediaPlayer.start();
            }
        });

        buttonNext.setOnClickListener(v -> {
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
                    File file = new File(MainActivity.audios.get(i + 1).getMusicURL());
                    if (file.exists()) {
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
                    else {
                        MainActivity.audios.remove(MainActivity.audios.get(i + 1));
                        Toast.makeText(v.getContext(), "Failed to open file, remove it from list", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            if (MainActivity.mediaPlayer.isPlaying() || MainActivity.mediaPlayer != null) {
                Intent serviceIntent = new Intent(view.getContext(), NotificationService.class);
                view.getContext().startService(serviceIntent);
            }
        });

        buttonLoop.setOnClickListener(v -> {
            if(MainActivity.mediaPlayer.isLooping()) {
                MainActivity.mediaPlayer.setLooping(false);
            }
            else {
                MainActivity.mediaPlayer.setLooping(true);
            }
        });

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    new Handler().postDelayed(this, 100);
                    if (x == 360) {
                        x = 0;
                    }

                    if(MainActivity.mediaPlayer != null) {
                        if(!MainActivity.currentPlay.getMusicName().equals("")) {
                            titlePlaying.setText("Now playing: " + MainActivity.currentPlay.getMusicName());
                        }

                        if(!MainActivity.currentPlay.getMusicImageURL().equals("")) {
                            Picasso.get().load(MainActivity.currentPlay.getMusicImageURL()).into(musicImage);
                        }

                        if(MainActivity.mediaPlayer.isPlaying()) {
                            if (NotificationService.mediaSession != null) {
                                NotificationService.mediaSession.setMetadata(
                                        new MediaMetadataCompat.Builder()
                                                .putLong(MediaMetadata.METADATA_KEY_DURATION, MainActivity.mediaPlayer.getDuration())
                                                .build()
                                );
                                NotificationService.mediaSession.setPlaybackState(
                                        new PlaybackStateCompat.Builder()
                                                .setState(PlaybackStateCompat.STATE_PLAYING, MainActivity.mediaPlayer.getCurrentPosition(), 0f)
                                                .setActions(PlaybackStateCompat.ACTION_PAUSE |
                                                        PlaybackStateCompat.ACTION_PLAY |
                                                        PlaybackStateCompat.ACTION_SKIP_TO_NEXT |
                                                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)
                                                .build()
                                );
                            }
                            musicImage.setRotation(x++);
                            seekBar.setProgress(MainActivity.mediaPlayer.getCurrentPosition());
                            seekBar.setMax(MainActivity.mediaPlayer.getDuration());
                            currentTime.setText(formatMMSS(MainActivity.mediaPlayer.getCurrentPosition()));
                            durationTime.setText(formatMMSS(MainActivity.mediaPlayer.getDuration()));
                            buttonPlay.setImageResource(R.drawable.baseline_pause_circle_outline_24);
                        }
                        else {
                            if (NotificationService.mediaSession != null) {
                                NotificationService.mediaSession.setPlaybackState(
                                        new PlaybackStateCompat.Builder()
                                                .setState(PlaybackStateCompat.STATE_PAUSED, MainActivity.mediaPlayer.getCurrentPosition(), 0f)
                                                .setActions(PlaybackStateCompat.ACTION_PAUSE |
                                                        PlaybackStateCompat.ACTION_PLAY |
                                                        PlaybackStateCompat.ACTION_SKIP_TO_NEXT |
                                                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)
                                                .build()
                                );
                            }
                            musicImage.setRotation(x);
                            buttonPlay.setImageResource(R.drawable.baseline_play_circle_outline_24);
                        }

                        if(MainActivity.mediaPlayer.isLooping()) {
                            buttonLoop.setImageResource(R.drawable.base_unloop);
                        }
                        else {
                            buttonLoop.setImageResource(R.drawable.base_loop);
                        }

                        if(isMuted) {
                            buttonVolume.setImageResource(R.drawable.baseline_volume_up_24);
                        }
                        else {
                            buttonVolume.setImageResource(R.drawable.baseline_volume_off_24);
                        }
                    }
                }
                catch (Exception e) {
                    System.out.println(e);
                    Toast.makeText(view.getContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
}