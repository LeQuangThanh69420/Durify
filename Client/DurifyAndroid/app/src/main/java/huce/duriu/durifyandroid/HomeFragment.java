package huce.duriu.durifyandroid;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import huce.duriu.durifyandroid.Model.Music;
import huce.duriu.durifyandroid.RecyclerView.MusicAdapter;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.musicList);
        MusicAdapter adapter = new MusicAdapter(MainActivity.musics);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        EditText searchViewHome = view.findViewById(R.id.searchViewHome);
        searchViewHome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                String query = editable.toString();
                List<Music> musics = new ArrayList<>();
                for (Music music : MainActivity.musics) {
                    if (music.getMusicName().toLowerCase().contains(query.toLowerCase())) {
                        musics.add(music);
                    }
                }
                adapter.updateList(musics);
            }
        });
        searchViewHome.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (searchViewHome.getRight() - searchViewHome.getCompoundDrawables()[2].getBounds().width()*2)) {
                    searchViewHome.setText("");
                    searchViewHome.clearFocus();
                    InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isAcceptingText()) {
                        imm.hideSoftInputFromWindow(searchViewHome.getWindowToken(), 0);
                    }
                    return true;
                }
            }
            return false;
        });

        return view;
    }
}