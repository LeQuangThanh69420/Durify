package huce.duriu.durifyandroid.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

import huce.duriu.durifyandroid.Service.ApiService;
import huce.duriu.durifyandroid.Service.Retrofit;
import huce.duriu.durifyandroid.Service.AudioService;
import huce.duriu.durifyandroid.Model.Music;
import huce.duriu.durifyandroid.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoadingActivity extends AppCompatActivity {
    private int PERMISSION_REQUEST_CODE = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_loading);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.loading), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        try {
            this.getMusicList();
            this.getAudioList();
        }
        catch (Exception e) {

        }


        new Handler().postDelayed(() -> {
            startActivity(new Intent(LoadingActivity.this, MainActivity.class));
            finish();
        }, 3000);
    }

    private void getMusicList() {
        if (isNetworkConnected(this)) {
            ApiService apiService = Retrofit.getInstance().create(ApiService.class);
            Call<List<Music>> call = apiService.getMusicList();
            call.enqueue(new Callback<List<Music>>() {
                @Override
                public void onResponse(Call<List<Music>> call, Response<List<Music>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        MainActivity.musics = response.body();
                    }
                }
                @Override
                public void onFailure(Call<List<Music>> call, Throwable throwable) {
                    MainActivity.musics = new ArrayList<>();
                }
            });
        } else {
            MainActivity.musics = new ArrayList<>();
        }
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        else return false;
    }

    private void getAudioList() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if(!checkPermission()) {
                requestPermission();
            }
            MainActivity.audios = AudioService.fetchAudioFilesFromMusicFolder(getApplicationContext());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(LoadingActivity.this, Manifest.permission.READ_MEDIA_AUDIO);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(LoadingActivity.this, Manifest.permission.READ_MEDIA_AUDIO)) {
            Toast.makeText(LoadingActivity.this, "READ PERMISSION is REQUIRE, ALLOW from SETTTINGS", Toast.LENGTH_LONG).show();
        }
        else {
            ActivityCompat.requestPermissions(LoadingActivity.this, new String[]{Manifest.permission.READ_MEDIA_AUDIO}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getAudioList();
            }
        }
    }
}