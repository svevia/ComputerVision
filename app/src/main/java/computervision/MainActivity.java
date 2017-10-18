package computervision;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.aurelien.computervision.R;

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

        captureButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startCaptureActivity();
            }
        });
    }

    private void startLibActivity() {
        Intent protectedIntent = new Intent();
        protectedIntent.setType("image/*");
        protectedIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(protectedIntent,CODE_LIBRARY);
    }

    private void startCaptureActivity() {
        Intent protectedIntent = new Intent();
        protectedIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(protectedIntent,CODE_CAPTURE);
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
