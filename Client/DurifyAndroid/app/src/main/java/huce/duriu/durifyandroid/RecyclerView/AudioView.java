package huce.duriu.durifyandroid.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import huce.duriu.durifyandroid.R;

public class AudioView extends RecyclerView.ViewHolder {
    private ImageView playAudio, deleteAudio;
    private TextView audioName;

    public ImageView getPlayAudio() { return playAudio; }

    public ImageView getDeleteAudio() { return deleteAudio; }

    public TextView getAudioName() { return audioName; }

    public void setPlayAudio(ImageView playAudio) { this.playAudio = playAudio; }

    public void setDeleteAudio(ImageView deleteAudio) { this.deleteAudio = deleteAudio; }

    public void setAudioName(TextView audioName) { this.audioName = audioName; }

    public AudioView(@NonNull View itemView) {
        super(itemView);
        audioName = itemView.findViewById(R.id.audioName);
        playAudio = itemView.findViewById(R.id.playAudio);
        deleteAudio = itemView.findViewById(R.id.deleteAudio);
    }
}
