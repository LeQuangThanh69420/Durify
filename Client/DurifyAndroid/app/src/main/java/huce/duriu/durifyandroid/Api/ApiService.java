package huce.duriu.durifyandroid.Api;

import java.util.List;

import huce.duriu.durifyandroid.Model.Music;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("LeQuangThanh69420/Durify/main/Data/MusicList.json")
    Call<List<Music>> getMusicList();
}