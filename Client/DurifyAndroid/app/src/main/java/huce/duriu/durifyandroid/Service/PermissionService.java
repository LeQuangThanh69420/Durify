package huce.duriu.durifyandroid.Service;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

public class PermissionService {
    public static final int REQUEST_EXTERNAL_STORAGE = 1;
    public static boolean checkReadExternalStoragePermission(Context context) {
        int permissionResult = ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionResult == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);
            // Sau khi yêu cầu quyền, kết quả sẽ được xử lý trong onRequestPermissionsResult() của activity
            return false;
        }
    }
}
