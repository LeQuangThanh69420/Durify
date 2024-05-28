package huce.duriu.durifyandroid.Activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import huce.duriu.durifyandroid.Fragment.DownloadedFragment;
import huce.duriu.durifyandroid.Fragment.HomeFragment;
import huce.duriu.durifyandroid.Fragment.PlayingFragment;
import huce.duriu.durifyandroid.Model.Music;
import huce.duriu.durifyandroid.R;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    public static List<Music> musics;
    public static List<Music> audios;
    public static MediaPlayer mediaPlayer;
    public static BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment = new HomeFragment();
    PlayingFragment playingFragment = new PlayingFragment();
    DownloadedFragment downloadedFragment = new DownloadedFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        mediaPlayer = new MediaPlayer();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, 0, systemBars.right, 0);
            return insets;
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        if (item.getItemId() == R.id.home) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, homeFragment)
                    .commit();
            return true;
        }
        else if (item.getItemId() == R.id.playing) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, playingFragment)
                    .commit();
            return true;
        }
        else if (item.getItemId() == R.id.downloaded) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, downloadedFragment)
                    .commit();
            return true;
        }
        return false;
    }
}