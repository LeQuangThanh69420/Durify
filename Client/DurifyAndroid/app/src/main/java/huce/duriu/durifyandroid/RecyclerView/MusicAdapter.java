package huce.duriu.durifyandroid.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import huce.duriu.durifyandroid.Model.Music;
import huce.duriu.durifyandroid.R;

public class MusicAdapter extends RecyclerView.Adapter<MusicView> {
    private List<Music> musics;
    public List<Music> getMusics() {
        return musics;
    }
    public void setListProduct(List<Music> musics) {
        this.musics = musics;
    }
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
    }

    @Override
    public int getItemCount() { return musics.size(); }

    public void updateList(List<Music> musics) {
        this.musics = musics;
        notifyDataSetChanged();
    }
}
