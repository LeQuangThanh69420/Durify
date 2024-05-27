package huce.duriu.durifyandroid.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import huce.duriu.durifyandroid.R;

public class MusicView extends RecyclerView.ViewHolder {


    private ImageView img, playMusic, downloadMusic;
    private TextView musicName, musicArtist, musicNation;

    public ImageView getImg() { return img; }

    public ImageView getPlayMusic() {
        return playMusic;
    }

    public ImageView getDownloadMusic() {
        return downloadMusic;
    }

    public TextView getMusicName() {
        return musicName;
    }

    public TextView getMusicArtist() {
        return musicArtist;
    }

    public TextView getMusicNation() {
        return musicNation;
    }

    public void setImg(ImageView img) {
        this.img = img;
    }

    public void setMusicName(TextView musicName) {
        this.musicName = musicName;
    }

    public void setMusicArtist(TextView musicArtist) {
        this.musicArtist = musicArtist;
    }

    public void setMusicNation(TextView musicNation) {
        this.musicNation = musicNation;
    }

    public MusicView(@NonNull View itemView) {
        super(itemView);
        img = itemView.findViewById(R.id.img);
        musicName = itemView.findViewById(R.id.musicName);
        musicArtist = itemView.findViewById(R.id.musicArtist);
        musicNation = itemView.findViewById(R.id.musicNation);
        playMusic = itemView.findViewById(R.id.playMusic);
        downloadMusic = itemView.findViewById(R.id.downloadMusic);
    }
}
