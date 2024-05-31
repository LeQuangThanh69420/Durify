package huce.duriu.durifyandroid.Service;

import android.os.AsyncTask;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FileDownloadService extends AsyncTask<String, Void, Boolean> {

    private String destinationPath;
    private DownloadListener listener;

    public FileDownloadService(String destinationPath, DownloadListener listener) {
        this.destinationPath = destinationPath;
        this.listener = listener;
    }

    @Override
    protected Boolean doInBackground(String... urls) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(urls[0])
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) return false;

            InputStream inputStream = response.body().byteStream();
            OutputStream outputStream = new FileOutputStream(destinationPath);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (listener != null) {
            listener.onDownloadComplete(success);
        }
    }

    public interface DownloadListener {
        void onDownloadComplete(boolean success);
    }
}
