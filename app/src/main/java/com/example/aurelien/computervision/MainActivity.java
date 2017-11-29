package com.example.aurelien.computervision;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.util.Base64;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final int CODE_LIBRARY = 1;
    private static final int CODE_CAPTURE = 2;
    Button captureButton;
    Button libButton;
    Button analyseButton;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        libButton = (Button)findViewById(R.id.buttonLib);
        captureButton = (Button)findViewById(R.id.buttonCapture);
        analyseButton = (Button)findViewById(R.id.buttonAnalyse);
        imageView = (ImageView)findViewById(R.id.imageView);
        libButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLibActivity();
            }
        });

        analyseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                analyseImage();
            }
        });

        captureButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startCaptureActivity();
            }
        });
    }

    /**
     * Analyse the image by upload
     */
    private void uploadImage() {

        // get picture
        String compress = convertBitMapToString();

        CallAPI callAPI = new CallAPI();
        callAPI.execute("http://svevi.fr:8080/projet/webapi/myresource",compress);

        Toast.makeText(getApplicationContext(),"http://svevi.fr:8080/projet/webapi/myresource with..." + compress.length(),Toast.LENGTH_SHORT).show();

    }

    /**
     * Convert image to str.
     *
     * @return
     */
    private String convertBitMapToString() {
        //ImageView imgView = (ImageView) findViewById(R.id.imgTaken);

        Bitmap bitmap=((BitmapDrawable)imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream); //compress to which format you want.
        byte[] byte_arr = stream.toByteArray();
        String image_str = Base64.encodeToString(byte_arr, Base64.DEFAULT);

        Log.d("uploadImage", "Image_str=" + image_str);
        return image_str;
    }


    private void startLibActivity() {
        Intent protectedIntent = new Intent();
        protectedIntent.setType("image/*");
        protectedIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(protectedIntent,CODE_LIBRARY);
    }

    private void analyseImage() {
        uploadImage();
    }

    private void startCaptureActivity() {
        Intent protectedIntent = new Intent();
        protectedIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(protectedIntent,CODE_CAPTURE);
    }

    private static String readStream(InputStream is) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = is.read();
            while(i != -1) {
                bo.write(i);
                i = is.read();
            }
            return bo.toString();
        } catch (IOException e) {
            return "";
        }
    }

    private static void postImage(String uri, String data) {

        try {
            URL url = new URL(uri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "text/plain");

            String input = data;

            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CODE_LIBRARY && resultCode == Activity.RESULT_OK) {
                android.net.Uri result=data.getData();
                imageView.setImageURI(result);
        }

        else if(requestCode == CODE_CAPTURE && resultCode == Activity.RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }

    }
}
