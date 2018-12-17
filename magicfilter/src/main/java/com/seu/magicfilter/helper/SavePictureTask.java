package com.seu.magicfilter.helper;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;

import com.seu.magicfilter.utils.MagicParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SavePictureTask extends AsyncTask<Bitmap, Integer, String> {

    private OnPictureSaveListener onPictureSaveListener;
    private File file;

    public SavePictureTask(File file, OnPictureSaveListener listener) {
        this.onPictureSaveListener = listener;
        this.file = file;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(final String result) {
        if (result != null)
            MediaScannerConnection.scanFile(MagicParams.context,
                    new String[]{result}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(final String path, final Uri uri) {
                            if (onPictureSaveListener != null)
                                onPictureSaveListener.onSaved(result);
                        }
                    });
    }

    @Override
    protected String doInBackground(Bitmap... params) {
        if (file == null)
            return null;
        return saveBitmap(params[0]);
    }

    private String saveBitmap(Bitmap bitmap) {
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            Bitmap bitmap1 = rotateBitmapByDegree(bitmap, -90);
            bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            bitmap.recycle();
            return file.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public interface OnPictureSaveListener {
        void onSaved(String result);
    }

    public Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap bitmap = null;
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
     /*   if (bitmap != null) {
            bitmap = bm;
        }
        if (bm != bitmap) {
            bm.recycle();
        }*/
        return bitmap;
    }

}
