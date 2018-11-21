package org.afunc.image_test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.github.piasy.biv.view.BigImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final
    SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BigImageView bigImageView = (BigImageView) findViewById(R.id.mBigImage);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tupian);


        bigImageView.onCacheHit(compressImage(bitmap));
//        bigImageView
    }

    private static boolean createOrExistsDir(final File file) {
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

    private static boolean createOrExistsFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return file.isFile();
        } else if (!createOrExistsDir(file.getParentFile())) {
            return false;
        } else {
            try {
                boolean isCreate = file.createNewFile();
                if (isCreate) {
//                    printDeviceInfo(filePath);
                }

                return isCreate;
            } catch (IOException var3) {
                var3.printStackTrace();
                return false;
            }
        }
    }

    private static final String FILE_SEP = System.getProperty("file.separator");

    public File compressImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 500) {  //循环判断如果压缩后图片是否大于500kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            long length = baos.toByteArray().length;
        }


        Date date = new Date(System.currentTimeMillis());
        //图片名
        String filename = format.format(date);

        String fullPath = null;

        if ("mounted".equals(Environment.getExternalStorageState()) && Utils.getApp().getExternalCacheDir() != null) {
            fullPath = Utils.getApp().getExternalCacheDir() + FILE_SEP + filename + ".png";
        } else {
            fullPath = Utils.getApp().getCacheDir() + FILE_SEP + filename + ".png";
        }
        if (!createOrExistsFile(fullPath)) {
            Log.e(TAG, "log to " + fullPath + " failed!");
        } else {

            File file = new File(fullPath);
            try {
                FileOutputStream fos = new FileOutputStream(file);
                try {
                    fos.write(baos.toByteArray());
                    fos.flush();
                    fos.close();
                } catch (IOException e) {

                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {

                e.printStackTrace();
            }

            Log.d(TAG, "compressImage: " + file);
            // recycleBitmap(bitmap);
            return file;
        }
        return null;
    }
}
