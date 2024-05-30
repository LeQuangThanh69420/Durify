package huce.duriu.durifyandroid.Fragment;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import huce.duriu.durifyandroid.Activity.MainActivity;
import huce.duriu.durifyandroid.Model.Music;
import huce.duriu.durifyandroid.R;
import huce.duriu.durifyandroid.RecyclerView.AudioAdapter;

public class DownloadedFragment extends Fragment {
    private RecyclerView recyclerView;

    public DownloadedFragment() {
        // Required empty public constructor
    }

    @NonNull
    public static DownloadedFragment newInstance(String param1, String param2) {
        DownloadedFragment fragment = new DownloadedFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_downloaded, container, false);

        recyclerView = view.findViewById(R.id.downloadedList);
        AudioAdapter adapter = new AudioAdapter(MainActivity.audios);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        EditText searchViewDownloaded = view.findViewById(R.id.searchViewDownloaded);
        searchViewDownloaded.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                String query = editable.toString();
                List<Music> audios = new ArrayList<>();
                try {
                    for (Music audio : MainActivity.audios) {
                        if (audio.getMusicName().toLowerCase().contains(query.toLowerCase())) {
                            audios.add(audio);
                        }
                    }
                    adapter.updateList(audios);
                }
                catch (Exception e) {
                    Toast.makeText(view.getContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                }

            }
        });
        searchViewDownloaded.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (searchViewDownloaded.getRight() - searchViewDownloaded.getCompoundDrawables()[2].getBounds().width()*2)) {
                    searchViewDownloaded.setText("");
                    searchViewDownloaded.clearFocus();
                    InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isAcceptingText()) {
                        imm.hideSoftInputFromWindow(searchViewDownloaded.getWindowToken(), 0);
                    }
                    return true;
                }
            }
            return false;
        });

        return view;
    }
}