package huce.duriu.durifyandroid.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import huce.duriu.durifyandroid.Api.ApiService;
import huce.duriu.durifyandroid.Api.Retrofit;
import huce.duriu.durifyandroid.File.FileDownloadTask;
import huce.duriu.durifyandroid.MainActivity;
import huce.duriu.durifyandroid.Model.Music;
import huce.duriu.durifyandroid.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MusicAdapter extends RecyclerView.Adapter<MusicView> implements FileDownloadTask.DownloadListener {
    private List<Music> musics;
    public List<Music> getMusics() {
        return musics;
    }
    public void setListProduct(List<Music> musics) {
        this.musics = musics;
    }
    private int selectedPosition = -1;
    public MusicAdapter(List<Music> musics) { this.musics = musics; }
    public void updateUI() {
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MusicView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music, parent, false);
        return new MusicView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicView holder, int position) {
        Picasso.get().load(musics.get(position).getMusicImageURL()).into(holder.getImg());
        holder.getMusicName().setText(musics.get(position).getMusicName());
        holder.getMusicArtist().setText(musics.get(position).getMusicArtist());
        holder.getMusicNation().setText(musics.get(position).getMusicNation());

        if (position == selectedPosition) {
            holder.getPlayMusic().setVisibility(View.VISIBLE);
            holder.getDownloadMusic().setVisibility(View.VISIBLE);
        } else {
            holder.getPlayMusic().setVisibility(View.GONE);
            holder.getDownloadMusic().setVisibility(View.GONE);
        }
        Music music = musics.get(position);
        holder.itemView.setOnClickListener(v -> {
            int previousPosition = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(previousPosition);
            notifyItemChanged(selectedPosition);

            System.out.println("Music ID: " + music.getMusicId());
            System.out.println("Music Name: " + music.getMusicName());
            System.out.println("Music URL: " + music.getMusicURL());
            System.out.println("Music Image URL: " + music.getMusicImageURL());
            System.out.println("Music Artist: " + music.getMusicArtist());
            System.out.println("Music Nation: " + music.getMusicNation());
        });
        holder.getDownloadMusic().setOnClickListener(v -> {
            FileDownloadTask downloadTask = new FileDownloadTask("/storage/emulated/0/Download/" + music.getMusicName() + ".mp3", this);
            downloadTask.execute(music.getMusicURL());
        });
    }

    @Override
    public int getItemCount() { return musics.size(); }

    public void updateList(List<Music> musics) {
        this.musics = musics;
        notifyDataSetChanged();
    }

    @Override
    public void onDownloadComplete(boolean success) {
    }
}
