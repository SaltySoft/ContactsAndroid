package com.mti.saltycontacts.business;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;

/**
 * Created by Antoine on 11/23/13.
 */
public class ImageManager {

    public static Bitmap bitmapFromUrl(Context context, String url) {
        File f = new File(url);
        Uri image_uri = Uri.parse(url);
        try {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            //The new size we want to scale to
            final int REQUIRED_SIZE = 70;

            //Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            Bitmap result = rotateBitmap(BitmapFactory.decodeStream(new FileInputStream(f), null, o2), getOrientation(context, url));
            Bitmap cropped_result;
            if (result.getWidth() >= result.getHeight()) {
                int displace = (result.getWidth() - result.getHeight()) / 2;
                cropped_result = Bitmap.createBitmap(result, displace, 0, result.getHeight(), result.getHeight());
            } else {
                int displace = (result.getHeight() - result.getWidth()) / 2;
                cropped_result = Bitmap.createBitmap(result, 0, displace, result.getWidth(), result.getWidth());
            }
            return cropped_result;
        } catch (FileNotFoundException e) {
        }
        return null;
    }

    public static Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);


        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public static int getOrientation(Context context, String imagePath) {
        try {
            ExifInterface exif = new ExifInterface(imagePath);
            String orientation = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
            if (orientation.equals(ExifInterface.ORIENTATION_NORMAL)) {
                return 0;
            } else if (orientation.equals(ExifInterface.ORIENTATION_ROTATE_90 + "")) {
                return 90;
            } else if (orientation.equals(ExifInterface.ORIENTATION_ROTATE_180 + "")) {
                return 180;
            } else if (orientation.equals(ExifInterface.ORIENTATION_ROTATE_270 + "")) {
                return 270;
            }
        } catch (IOException e) {
        }
        return 0;
    }
}
