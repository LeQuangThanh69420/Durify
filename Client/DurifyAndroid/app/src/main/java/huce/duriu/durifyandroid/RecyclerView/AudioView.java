package huce.duriu.durifyandroid.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import huce.duriu.durifyandroid.R;

public class AudioView extends RecyclerView.ViewHolder {
    private ImageView playMusic, downloadMusic;
    private TextView musicName;

    public ImageView getPlayMusic() { return playMusic; }

    public ImageView getDownloadMusic() { return downloadMusic; }

    public TextView getMusicName() { return musicName; }

    public void setPlayMusic(ImageView playMusic) { this.playMusic = playMusic; }

    public void setDownloadMusic(ImageView downloadMusic) { this.downloadMusic = downloadMusic; }

    public void setMusicName(TextView musicName) { this.musicName = musicName; }

    public AudioView(@NonNull View itemView) {
        super(itemView);
        //musicName = itemView.findViewById(R.id.audioName);
        //playMusic = itemView.findViewById(R.id.playMusic);
        //downloadMusic = itemView.findViewById(R.id.downloadMusic);
    }
}
