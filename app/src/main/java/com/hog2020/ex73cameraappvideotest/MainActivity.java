package com.hog2020.ex73cameraappvideotest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    VideoView vv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vv= findViewById(R.id.vv);

        //동적퍼미션
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (checkSelfPermission(permissions[0])== PackageManager.PERMISSION_DENIED){
                requestPermissions(permissions,0);
            }
        }
    }

    public void clickBtn(View view) {

        Intent intent=new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent,100);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==100 && resultCode==RESULT_OK){
            //비디오 정보는 용량이 커서 무조건 외부저장소에 저장되고 그경로를 Uri 로 줌
            Uri uri =data.getData();
            vv.setVideoURI(uri);
            //비디오뷰에 컨트롤바 나오도록
            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(vv);
            vv.setMediaController(mediaController);

            //비디오 데이터를 로딩하는 시간이 걸리기에 곧바로 start 하면 안되고
            //준비가 되면 start 해야됨
            vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    vv.start();
                }
            });
        }
    }
}