package com.example.mamk.binarydatatopng;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

public class MainActivity extends AppCompatActivity {

    public static void savePNG_After(Bitmap bitmap, String name) {
        File file = new File(name);
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            byte[] b = new byte[0];
            RandomAccessFile f = new RandomAccessFile("/sdcard/datammk", "r");
            b = new byte[(int)f.length()];
            f.readFully(b);
            IntBuffer intBuf =
                    ByteBuffer.wrap(b)
                            .order(ByteOrder.BIG_ENDIAN)
                            .asIntBuffer();
            int[] array = new int[intBuf.remaining()];
            intBuf.get(array);
            for (int i = 0; i < array.length; i++) {
                int argb = (array[i] & 0xFF) << 24 | (array[i] >>> 8);
                array[i] = argb;
            }
            Bitmap bitmap = Bitmap.createBitmap(array, 512, 512, Bitmap.Config.ARGB_8888);
            ImageView v = new ImageView(this);
            v.setImageBitmap(bitmap);
            setContentView(v);
            savePNG_After(bitmap, "/sdcard/abcdeg.png");
            setContentView(v);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
