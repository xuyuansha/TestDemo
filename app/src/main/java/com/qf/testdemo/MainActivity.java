package com.qf.testdemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends ActionBarActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.imageView);
        


    }

    public void click(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra("return-data", true);

        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1) {
            Log.e("onActivityResult", "数据获取成功");

            Uri uri = data.getData();
            Log.e("uri:", uri.toString());

            Bitmap bitmap = getBitmap(uri);

            imageView.setImageBitmap(bitmap);

        }
    }

    private Bitmap getBitmap(Uri uri) {
        InputStream ins = null;
        Bitmap bitmap = null;
        try {
            ins = getContentResolver().openInputStream(uri);

            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(ins, null, opts);

            int outHeight = opts.outHeight;
            int outWidth = opts.outWidth;

            Log.e("图片原大小：", "" + outHeight + ":" + outWidth);

            opts.inJustDecodeBounds = false;
            //获得采样率
            opts.inSampleSize = getSampleSize(outWidth, outHeight);
            Log.e("采样率",opts.inSampleSize+"====");
            ins = getContentResolver().openInputStream(uri);
            bitmap = BitmapFactory.decodeStream(ins, null, opts);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    private int getSampleSize(int outWidth, int outHeight) {
        int inSampleSize = 1;

        //获得屏幕的宽高

        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        int x = outWidth / point.x;
        int y = outHeight / point.y;
        inSampleSize = x > y ? x : y;
        if (inSampleSize > 0)
            return inSampleSize;
        else
            return 1;
    }


}
