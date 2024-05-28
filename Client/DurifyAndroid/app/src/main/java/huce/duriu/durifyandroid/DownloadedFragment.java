package huce.duriu.durifyandroid;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DownloadedFragment extends Fragment {

    private static final int REQUEST_CODE_READ_MEDIA_AUDIO = 1;
    private MediaPlayer mediaPlayer;
    private RecyclerView recyclerView;

    public DownloadedFragment() {
        // Required empty public constructor
    }

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
            // Get your parameters here if you have any
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_downloaded, container, false);

        if (checkAndRequestPermissions()) {
            initializeRecyclerView(view);
        }
        EditText searchViewDownloaded = view.findViewById(R.id.searchViewDownloaded);
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

        recyclerView = view.findViewById(R.id.downloadedList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    private boolean checkAndRequestPermissions() {
        List<String> permissions = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_AUDIO)
                    != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_MEDIA_AUDIO);
            }
        } else {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.POST_NOTIFICATIONS);
            }
        }

        if (!permissions.isEmpty()) {
            requestPermissions(permissions.toArray(new String[0]), REQUEST_CODE_READ_MEDIA_AUDIO);
            return false;
        }
        return true;
    }

    private void initializeRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.downloadedList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<String> audioPaths = AudioFetcher.fetchAudioFilesFromMusicFolder(getContext());
        AudioAdapter adapter = new AudioAdapter(getContext(), audioPaths, new AudioAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String audioPath) {
                playAudio(audioPath);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void playAudio(String audioPath) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audioPath);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Cannot play audio: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_READ_MEDIA_AUDIO) {
            boolean allPermissionsGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }
            if (allPermissionsGranted) {
                View view = getView();
                if (view != null) {
                    initializeRecyclerView(view);
                }
            } else {
                // Permission denied, handle appropriately
                Toast.makeText(getContext(), "Permissions are required to proceed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}