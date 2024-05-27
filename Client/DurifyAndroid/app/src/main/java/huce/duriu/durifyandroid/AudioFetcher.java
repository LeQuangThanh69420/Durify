package huce.duriu.durifyandroid;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

public class AudioFetcher {
    public static List<String> fetchAudioFilesFromMusicFolder(Context context) {
        List<String> audioPaths = new ArrayList<>();

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
        String[] selectionArgs = new String[]{"%/Download/%"};

        Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, MediaStore.Audio.Media.DATE_ADDED + " DESC");

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int dataIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                String filePath = cursor.getString(dataIndex);
                audioPaths.add(filePath);
            }
            cursor.close();
        }

        return audioPaths;
    }
}