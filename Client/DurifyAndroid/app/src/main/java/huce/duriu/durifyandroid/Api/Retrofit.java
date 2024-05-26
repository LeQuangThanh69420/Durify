package huce.duriu.durifyandroid.Api;

import retrofit2.converter.gson.GsonConverterFactory;

public class Retrofit {
    public static retrofit2.Retrofit getInstance() {
        return new retrofit2.Retrofit.Builder().baseUrl("https://raw.githubusercontent.com/").addConverterFactory(GsonConverterFactory.create()).build();
    }
}
