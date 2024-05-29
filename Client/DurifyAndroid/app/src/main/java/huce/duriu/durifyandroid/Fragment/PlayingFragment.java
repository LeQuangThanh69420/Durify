package huce.duriu.durifyandroid.Fragment;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import huce.duriu.durifyandroid.Activity.MainActivity;
import huce.duriu.durifyandroid.R;
import huce.duriu.durifyandroid.Service.MusicService;

public class PlayingFragment extends Fragment {
    private TextView titlePlaying;
    private ShapeableImageView musicImage;
    private int x = 0;
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

        buttonVolume.setOnClickListener(v -> {
            AudioManager audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

            if (audioManager != null) {
                if (isMuted) {
                    // Unmute
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, previousVolume, 0);
                    isMuted = false;
                } else {
                    // Mute
                    previousVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                    isMuted = true;
                }
            }
        });

        buttonPre.setOnClickListener(v -> {
            Intent serviceIntent = new Intent(getActivity(), MusicService.class);
            getActivity().startService(serviceIntent);
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
        });

        buttonPlay.setOnClickListener(v -> {
            Intent serviceIntent = new Intent(getActivity(), MusicService.class);
            getActivity().startService(serviceIntent);
            if(MainActivity.mediaPlayer.isPlaying()) {
                MainActivity.mediaPlayer.pause();
            }
            else {
                MainActivity.mediaPlayer.start();
            }
        });

        buttonNext.setOnClickListener(v -> {
            Intent serviceIntent = new Intent(getActivity(), MusicService.class);
            getActivity().startService(serviceIntent);
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
                        musicImage.setRotation(x++);
                        buttonPlay.setImageResource(R.drawable.baseline_pause_circle_outline_24);
                    }
                    else {
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
        });
        return view;
    }
}