package huce.duriu.durifyandroid;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.net.ConnectivityManagerCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

import huce.duriu.durifyandroid.Api.ApiService;
import huce.duriu.durifyandroid.Api.Retrofit;
import huce.duriu.durifyandroid.Model.Music;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_loading);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.getMusicList();
        new Handler().postDelayed(() -> {
            startActivity(new Intent(LoadingActivity.this, MainActivity.class));
            finish();
        }, 3000);
    }

    private void getMusicList() {
        if (isNetworkConnected()) {
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

    private boolean isNetworkConnected() {
        //ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        //return cm != null && ConnectivityManagerCompat.isActiveNetworkMetered(cm);
        return true;
    }
}