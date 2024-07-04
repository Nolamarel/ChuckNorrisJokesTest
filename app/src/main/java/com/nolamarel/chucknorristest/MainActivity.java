package com.nolamarel.chucknorristest;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.nolamarel.chucknorristest.databinding.ActivityMainBinding;
import com.nolamarel.chucknorristest.models.JokeApi;
import com.nolamarel.chucknorristest.models.JokeDb;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private String jValue, jIcon, jId, jUrl;
    private TextView jokeText;
    private ImageView jokeImage;
    private Context context;
    private DatabaseHelper dbHlper;
    private int currentId = -1;
    private HandlerThread networkThread;
    private Handler networkHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        networkThread = new HandlerThread("NetworkThread");
        networkThread.start();
        networkHandler = new Handler(networkThread.getLooper());

        jokeText = binding.jokeText;
        jokeImage = binding.jokeImage;
        context = this;
        dbHlper = new DatabaseHelper(context);


        binding.jokeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getJoke();
            }
        });
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPreviousJoke();
            }
        });

        binding.forwardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNextJoke();
            }
        });

        if (savedInstanceState != null) {
            String savedJokeText = savedInstanceState.getString("savedJokeText", "");
            String savedJokeIconUrl = savedInstanceState.getString("savedJokeIconUrl", "");
            jokeText.setText(savedJokeText);
            Glide.with(context).load(savedJokeIconUrl).into(jokeImage);
        }

    }

    private void getJoke(){
        networkHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    final JokeApi joke = JokeApiService.getJoke();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            jValue = joke.value;
                            jIcon = joke.iconUrl;
                            jId = joke.jokeId;
                            jUrl = joke.url;
                            jokeText.setText(joke.value);
                            Glide.with(context).load(joke.iconUrl).into(jokeImage);
                            JokeDb joke = new JokeDb(jIcon, 0, jUrl, jValue, jId);
                            addJokeToSQL(joke);
                        }
                    });
                } catch (IOException e) {
                    Log.e("api", "Error fetching joke: " + e.getMessage());
                }
            }
        });
    }

    private void addJokeToSQL(JokeDb joke){
        boolean success = dbHlper.addJoke(joke);
        currentId = joke.getId();
        if (success) {
            Toast.makeText(this, "Joke successfully added", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Joke adding error", Toast.LENGTH_SHORT).show();
        }
    }

    private void getPreviousJoke(){
        try {
            if (currentId > 0) {
                currentId -= 1;
                JokeDb joke = dbHlper.getJokeById(currentId);
                jokeText.setText(joke.getValue());
                Glide.with(context).load(joke.getIcon_url()).into(jokeImage);
            } else {
                Toast.makeText(context, "Add joke first", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e){
            Toast.makeText(context, "It's the first joke", Toast.LENGTH_SHORT).show();
        }
    }

    private void getNextJoke(){
        try {
            if (currentId != - 1){
                currentId += 1;
                JokeDb joke = dbHlper.getJokeById(currentId);
                jokeText.setText(joke.getValue());
                Glide.with(context).load(joke.getIcon_url()).into(jokeImage);
            } else {
                Toast.makeText(context, "Add joke first", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e){
            Toast.makeText(context, "It's the last joke", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        networkHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getJoke();
                networkHandler.postDelayed(this, 5 * 60 * 1000);
            }
        }, 5 * 1000);
    }
    @Override
    protected void onPause() {
        super.onPause();
        networkHandler.removeCallbacksAndMessages(null);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("savedJokeText", jokeText.getText().toString());
        outState.putString("savedJokeIconUrl", jIcon);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        networkThread.quitSafely();
    }

}