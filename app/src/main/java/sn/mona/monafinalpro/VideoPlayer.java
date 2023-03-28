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

import sn.mona.monafinalpro.Data.Medecine;

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

        if (getIntent()!=null&&getIntent().hasExtra("med"))
        {
            Medecine m= (Medecine) getIntent().getExtras().get("med");
            setVideo(Uri.parse(m.getVideo()));
        }


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