package huce.duriu.durifyandroid.Service;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

import huce.duriu.durifyandroid.Model.Music;

public class AudioService {
    public static String path = "/storage/emulated/0/Download/";
    public static List<Music> fetchAudioFilesFromMusicFolder(Context context) {
        List<Music> audios = new ArrayList<>();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION
        };

        String selection = MediaStore.Audio.Media.DATA + " LIKE ?";
        String[] selectionArgs = new String[]{path + "%"};

        Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, MediaStore.Audio.Media.DATE_ADDED + " DESC");

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int dataIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                String filePath = cursor.getString(dataIndex);
                audios.add(new Music(filePath.replace(path, ""), filePath));
            }
            cursor.close();
        }

        return audios;
    }
}