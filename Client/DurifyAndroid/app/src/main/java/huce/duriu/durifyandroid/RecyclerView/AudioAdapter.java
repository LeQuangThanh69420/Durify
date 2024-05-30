package huce.duriu.durifyandroid.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.util.List;

import huce.duriu.durifyandroid.Activity.MainActivity;
import huce.duriu.durifyandroid.Model.Music;
import huce.duriu.durifyandroid.R;
import huce.duriu.durifyandroid.Service.AudioService;

public class AudioAdapter extends RecyclerView.Adapter<AudioView> {
    private List<Music> audios;
    public List<Music> getAudios() { return audios; }
    public void setAudios(List<Music> audios) { this.audios = audios; }
    public AudioAdapter(List<Music> audios) { this.audios = audios; }
    public void updateUI() { notifyDataSetChanged(); }
    private int selectedPosition = -1;

    @NonNull
    @Override
    public AudioView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_audio, parent, false);
        return new AudioView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioView holder, int position) {
        Music audio = audios.get(position);
        holder.getAudioName().setText(audios.get(position).getMusicName());

        if (position == selectedPosition) {
            holder.getPlayAudio().setVisibility(View.VISIBLE);
            holder.getDeleteAudio().setVisibility(View.VISIBLE);
        } else {
            holder.getPlayAudio().setVisibility(View.GONE);
            holder.getDeleteAudio().setVisibility(View.GONE);
        }

        holder.getPlayAudio().setOnClickListener(v -> {

        });

        holder.itemView.setOnClickListener(v -> {
            int previousPosition = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            if( selectedPosition == previousPosition) selectedPosition = -1;
            notifyItemChanged(previousPosition);
            notifyItemChanged(selectedPosition);
        });

        holder.getPlayAudio().setOnClickListener(v -> {
            File file = new File(audio.getMusicURL());
            if (file.exists()) {
                MainActivity.currentPlay = audio;
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
                audios.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, audios.size());
                MainActivity.audios.remove(position);
                Toast.makeText(v.getContext(), "Failed to open file, remove it from list", Toast.LENGTH_SHORT).show();
            }
        });

        holder.getDeleteAudio().setOnClickListener(v -> {
            String delAudioName = audio.getMusicName();
            String delAudioPath = AudioService.path + delAudioName;
            try {
                File fileToDelete = new File(delAudioPath);
                if (fileToDelete.exists()) {
                    if (fileToDelete.delete()) {
                        audios.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, audios.size());
                        MainActivity.audios.remove(position);
                        Toast.makeText(v.getContext(), "Deleted " + delAudioName + " successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(v.getContext(), "Application DON'T HAVE PERMISSION on this file", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(v.getContext(), "File not found", Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e) {
                Toast.makeText(holder.itemView.getContext(), "An error occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() { return audios.size(); }

    public void updateList(List<Music> audios) {
        this.audios = audios;
        notifyDataSetChanged();
    }
}