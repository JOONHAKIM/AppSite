package org.androidtown.appsite;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class appSite extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.main);
        setContentView(R.layout.intro);

//        ImageView image=(ImageView) findViewById(R.id.img_app_mark);
//        Animation alphanim= AnimationUtils.loadAnimation(this,R.anim.alpha);
//        image.startAnimation(alphanim);

        ImageButton icon1 = (ImageButton) findViewById(R.id.img_app_mark);
        icon1.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(appSite.this, mainSite.class);
                startActivity(intent);
            }
        });

        ImageButton icon2 = (ImageButton) findViewById(R.id.anim_bg);
        icon2.setOnClickListener(new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(appSite.this, mainSite.class);
                startActivity(intent);
            }
        });
  /*      ImageView image=(ImageView) findViewById(R.id.img_app_mark);
        Animation tween= AnimationUtils.loadAnimation(this,R.anim.tween);
        image.startAnimation(tween);*/


        //xml 리소스를 이용하지 않고 이미지를 출력하는 방법.
    /*    ImageView image = new ImageView(this);
        image.setImageResource(R.drawable.grh_mark);
        setContentView(image);*/


        //실행이 안될 것이다.
        //아래와 같은 코드는 이벤트 없이 애니메이션을 시작했다고 한다.
        //무슨 말인지 모르겠으나 하단의 코드를 보자.

//        ImageView img =(ImageView) findViewById(R.id.anim_bg);
//        img.setBackgroundResource(R.drawable.anim_list);
//
//        AnimationDrawable frameAnimation=(AnimationDrawable) img.getBackground();
//        frameAnimation.start();

        ImageView img =(ImageView) findViewById(R.id.anim_bg);
        img.setBackgroundResource(R.drawable.anim_list);

        slideStart slideS = new slideStart();
        slideEnd slideE = new slideEnd();

        Timer timerS= new Timer(false);
        timerS.schedule(slideS,1000);
        Timer timerE = new Timer(false);
        timerE.schedule(slideE,10000);

    }


    class slideStart extends TimerTask{
        slideStart(){}
        @Override
        public void run() {
            ImageView img =(ImageView) findViewById(R.id.anim_bg);
            AnimationDrawable frameAnimation=(AnimationDrawable) img.getBackground();
            frameAnimation.start();
        }
    }

    class slideEnd extends TimerTask{
        slideEnd(){}
        @Override
        public void run() {
            ImageView img =(ImageView) findViewById(R.id.anim_bg);
            AnimationDrawable frameAnimation=(AnimationDrawable) img.getBackground();
            frameAnimation.stop();
        }
    }
}
