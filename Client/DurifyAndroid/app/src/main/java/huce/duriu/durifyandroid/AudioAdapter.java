package huce.duriu.durifyandroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.ViewHolder> {
    private Context context;
    private List<String> audioList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(String audioPath);
    }

    public AudioAdapter(Context context, List<String> audioList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.audioList = audioList;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.audio_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String audioPath = audioList.get(position);
        holder.textView.setText(audioPath);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(audioPath);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return audioList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.musicName);
        }
    }
}