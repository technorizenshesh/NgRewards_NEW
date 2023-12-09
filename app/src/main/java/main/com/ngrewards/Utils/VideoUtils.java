package main.com.ngrewards.Utils;

import android.content.Context;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Date;

public class VideoUtils {

    public static File uriToFile(Context context, Uri uri) throws IOException {
        File videoFile = new File(context.getCacheDir(), new Date() + ".mp4");
        videoFile.createNewFile();
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        OutputStream outputStream;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            outputStream = Files.newOutputStream(videoFile.toPath());
        } else {
            outputStream = new FileOutputStream(videoFile);
        }
        byte[] buffer = new byte[1024];
        int length;
        while (true) {
            assert inputStream != null;
            if (!((length = inputStream.read(buffer)) > 0)) break;
            outputStream.write(buffer, 0, length);
        }

        inputStream.close();
        outputStream.close();

        return videoFile;
    }
}
