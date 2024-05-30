package huce.duriu.durifyandroid.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.List;

import huce.duriu.durifyandroid.Activity.LoadingActivity;
import huce.duriu.durifyandroid.Activity.MainActivity;
import huce.duriu.durifyandroid.Service.AudioService;
import huce.duriu.durifyandroid.File.FileDownloadTask;
import huce.duriu.durifyandroid.Model.Music;
import huce.duriu.durifyandroid.R;

public class MusicAdapter extends RecyclerView.Adapter<MusicView> implements FileDownloadTask.DownloadListener {
    private List<Music> musics;
    public List<Music> getMusics() { return musics; }
    public void setMusics(List<Music> musics) { this.musics = musics; }
    public MusicAdapter(List<Music> musics) { this.musics = musics; }
    public void updateUI() { notifyDataSetChanged(); }
    private int selectedPosition = -1;

    @NonNull
    @Override
    public MusicView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music, parent, false);
        return new MusicView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicView holder, int position) {
        Music music = musics.get(position);
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

        holder.itemView.setOnClickListener(v -> {
            int previousPosition = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            if( selectedPosition == previousPosition) selectedPosition = -1;
            notifyItemChanged(previousPosition);
            notifyItemChanged(selectedPosition);
        });

        holder.getPlayMusic().setOnClickListener(v -> {
            if (LoadingActivity.isNetworkConnected(holder.itemView.getContext())) {
                MainActivity.currentPlay = music;
                MainActivity.bottomNavigationView.setSelectedItemId(R.id.playing);
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
                Toast.makeText(holder.itemView.getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
            }
        });

        holder.getDownloadMusic().setOnClickListener(v -> {
            File directory = new File(AudioService.path);
            if (!directory.exists()) {
                if (!directory.mkdirs()) {
                    Toast.makeText(holder.itemView.getContext(), "An error occurred while create folder Durify", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            String newAudioName = music.getMusicName() + ".mp3";
            String newAudioPath = AudioService.path + newAudioName;
            try {
                Music newAudio = new Music(newAudioName, newAudioPath);
                if (!MainActivity.audios.contains(newAudio)) {
                    FileDownloadTask downloadTask = new FileDownloadTask(newAudioPath, this);
                    downloadTask.execute(music.getMusicURL());
                    MainActivity.audios.add(0, newAudio);
                    Toast.makeText(holder.itemView.getContext(), "Download " + newAudioName + " successfully", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(holder.itemView.getContext(), "This music is already downloaded", Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e) {
                Toast.makeText(holder.itemView.getContext(), "An error occurred", Toast.LENGTH_SHORT).show();
            }

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
