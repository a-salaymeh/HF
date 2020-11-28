package com.example.hf;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.PlayerApi;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.client.CallResult;
import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerContext;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;

public class SpotifyPlayer extends AppCompatActivity {
    private static final String CLIENT_ID = "c328d377977c4831849e711ec0674b21";
    //"cd05f71d8e6d4eae8146c7d75e2eebcf";
    //"f6d38aa71e614dc4b11612b8ebb15ff5";
    private static final String REDIRECT_URI = "com.example.hf://callback";
    private SpotifyAppRemote mSpotifyAppRemote;
    private PlayerApi player;
    private boolean playBool;
    ImageButton playPauseBtn;
    ImageButton nextBtn;
    ImageButton prevBtn;
    ImageButton repeatBtn;
    ImageButton shuffleBtn;
    TextView songName;
    TextView artistName;
    RoundedImageView coverArt;
    Subscription<PlayerContext> playerContextSubscription;
    private Bitmap picture;
    boolean isConnected = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify_player);
        playPauseBtn = findViewById(R.id.playPauseBtn);
        nextBtn = findViewById(R.id.nextBtn);
        prevBtn = findViewById(R.id.prevBtn);
        repeatBtn= findViewById(R.id.repeatBtn);;
        shuffleBtn = findViewById(R.id.shuffleBtn);;
        songName = findViewById(R.id.songName);;
        artistName = findViewById(R.id.artistName);;
        coverArt = findViewById(R.id.artImg);;


        spotifyConnect();



        playPauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!playBool) {
                    player.resume();
                }else{
                    player.pause();
                }
                //new updateInfo().execute("this will go to background");
            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.skipNext();
                //new updateInfo().execute("this will go to background");
            }
        });
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.skipPrevious();
                //new updateInfo().execute("this will go to background");
            }
        });
        repeatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.toggleRepeat();
                //new updateInfo().execute("this will go to background");
            }
        });
        shuffleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.toggleShuffle();
                //new updateInfo().execute("this will go to background");
            }
        });



    }
    protected void onResume() {
        super.onResume();
        if (isConnected) {
            new updateInfo().execute("this will go to background");
        }else{
            spotifyConnect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);

    }

    public void spotifyConnect(){

        // Set the connection parameters
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();

        //Connect to App Remote
        SpotifyAppRemote.connect(this, connectionParams,
                new Connector.ConnectionListener() {
                    @Override
                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        Log.d("MainActivity", "Connected! Yay!");
                        // start interacting with App Remote
                        // just a spotify test
                        player = mSpotifyAppRemote.getPlayerApi();
                        isConnected= true;
                        new updateInfo().execute("this will go to background");




                    }
                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.e("MainActivity", throwable.getMessage(), throwable);
                        // Something went wrong when attempting to connect! Handle errors here
                        isConnected = false;
                        AlertDialog.Builder builder = new AlertDialog.Builder(SpotifyPlayer.this);
                        builder.setCancelable(true);
                        builder.setTitle("Connection error");
                        builder.setMessage("Couldn't connect to Spotify");
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                                dialogInterface.cancel();
                            }
                        });
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                                dialogInterface.cancel();

                            }
                        });
                        builder.show();



                    }
                });
        return ;
    }


    private class updateInfo extends AsyncTask<String, Integer, String> {
        updateInfo(){
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.i("incoming param", strings[0] + "---------------------------") ;

            player.subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    if(!playerState.isPaused){
                        playBool = true;
                        songName.setText(playerState.track.name);
                        artistName.setText(playerState.track.artist.name);
                        mSpotifyAppRemote.getImagesApi().getImage(playerState.track.imageUri).setResultCallback(new CallResult.ResultCallback<Bitmap>() {
                            public void onResult(Bitmap bitmap)
                            { coverArt.setImageBitmap(bitmap); } });
                        playPauseBtn.setImageResource(R.drawable.ic_pause);
                    }else{
                        playBool = false;
                        playPauseBtn.setImageResource(R.drawable.ic_play);
                        coverArt.setImageResource(R.drawable.logo_white);
                        songName.setText("Song Name");
                        artistName.setText("Artist Name");



                    }
                })
                .setErrorCallback(throwable -> {
                    // =( =( =(
                });

            return null;
        }
    }




    private void connectedSP() {
        // Play a playlist
        mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:37i9dQZF1DWYnx77Gg1Rgu");

        // Subscribe to PlayerState
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    final Track track = playerState.track;
                    if (track != null) {
                        Log.d("MainActivity", track.name + " by " + track.artist.name);
                    }
                });
    }
}