package sn.mona.monafinalpro;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.ArrayList;
//بناء شاشة لعرض الفيديو
public class VideoPlayer
        extends AppCompatActivity
        implements MediaPlayer.OnCompletionListener {

    VideoView vw;
    ArrayList<Integer> videolist = new ArrayList<>();
    int currvideo = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        vw = (VideoView)findViewById(R.id.vidvw);
        vw.setMediaController(new MediaController(this));
        vw.setOnCompletionListener(this);

        // video name should be in lower case alphabet.

       // setVideo();
    }

    public void setVideo(Uri vUri)
    {
//        String uriPath
//                = "android.resource://"
//                + getPackageName() + "/" + id;
//        Uri uri = Uri.parse(uriPath);
        vw.setVideoURI(vUri);
        vw.start();
    }

    public void onCompletion(MediaPlayer mediapalyer)
    {
        Toast.makeText(this, "Video Completed", Toast.LENGTH_SHORT).show();
    }


}