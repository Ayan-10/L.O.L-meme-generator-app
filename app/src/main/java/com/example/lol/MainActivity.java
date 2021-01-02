package com.example.lol;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    String stringurl;
    ImageView memeimage;
    OutputStream outputStream;
    TextView t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(getApplicationContext(),"Swipe Left for next meme",Toast.LENGTH_LONG).show();

        Button share = (Button) findViewById(R.id.share);
        Button save = (Button) findViewById(R.id.save);

        loadMeme();

        memeimage.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            @Override
            public void onSwipeLeft() {
                loadMeme();
            }

            @Override
            public void onSwipeRight() {
                Toast.makeText(getApplicationContext(),"Swipe Left for next meme",Toast.LENGTH_LONG).show();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Hey,checkout this meme");
                intent.putExtra(Intent.EXTRA_TEXT,stringurl);
                startActivity(Intent.createChooser(intent, "Share it using"));
            }
        });
        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveImage();
            }
        });
    }

    private void saveImage() {
        BitmapDrawable drawable = (BitmapDrawable) memeimage.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        File filepath = Environment.getExternalStorageDirectory();
        File dir = new File(filepath.getAbsolutePath()+"/Memes");
        dir.mkdir();

        String filename = String.format("%d.jpg",System.currentTimeMillis());
        File file = new File(dir,filename);
        try {
            outputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        Toast.makeText(getApplicationContext(),"Save to Memes folder",Toast.LENGTH_SHORT).show();
        try {
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadMeme(){
        ProgressBar progressBar= (ProgressBar) findViewById(R.id.bar);
        progressBar.setVisibility(View.VISIBLE);

         memeimage = (ImageView) findViewById(R.id.meme);
        // Instantiate the RequestQueue.
      //  RequestQueue queue = Volley.newRequestQueue(this);
         String url ="https://meme-api.herokuapp.com/gimme";
         
// Request a string response from the provided URL.
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            stringurl = response.getString("url");
                            Glide.with(MainActivity.this).load(stringurl).listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    progressBar.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    progressBar.setVisibility(View.GONE);
                                    return false;
                                }
                            }).into(memeimage);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                textView.setText("That didn't work!");
            }
        });


// Add the request to the RequestQueue.
       MySingleton.getInstance(this).addToRequestQueue(jsonRequest);
    }
}
