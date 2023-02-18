package com.example.tictactoe;


import android.app.Dialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
public class doublePlayer extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level2);

        txt_player1 = findViewById(R.id.txt_player1);
        txt_score_1 = findViewById(R.id.txt_score_1);
        txt_score_2 = findViewById(R.id.txt_score_2);
        txt_player2 = findViewById(R.id.txt_player2);
        img_0 = findViewById(R.id.img_0);
        img_1 = findViewById(R.id.img_1);
        img_2 = findViewById(R.id.img_2);
        img_3 = findViewById(R.id.img_3);
        img_4 = findViewById(R.id.img_4);
        img_5 = findViewById(R.id.img_5);
        img_6 = findViewById(R.id.img_6);
        img_7 = findViewById(R.id.img_7);
        img_8 = findViewById(R.id.img_8);
        txt_result = findViewById(R.id.txt_result);
        btn_play_again = findViewById(R.id.btn_play_again);
        result_layout = findViewById(R.id.result_layout);

        //مقدار دهی ارایه ایمیج ویو ها (خانه های بازی)
        image_Views_List.add(img_0); image_Views_List.add(img_1); image_Views_List.add(img_2); image_Views_List.add(img_3);
        image_Views_List.add(img_4); image_Views_List.add(img_5); image_Views_List.add(img_6); image_Views_List.add(img_7); image_Views_List.add(img_8);



        playerNameDialog();

        btn_play_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetGame(false);
            }
        });
    }

    //دیالوگ انتخاب نام بازیکن ها
    private void playerNameDialog(){
        Dialog playerNamesDialog = new Dialog(this);
        playerNamesDialog.setContentView(R.layout.player_names_dialog);
        playerNamesDialog.setCancelable(false);

        EditText edt_player_1 = playerNamesDialog.findViewById(R.id.edt_player_1);
        EditText edt_player_2 = playerNamesDialog.findViewById(R.id.edt_player_2);
        Button btn_start = playerNamesDialog.findViewById(R.id.btn_start);

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                player_name_1 = edt_player_1.getText().toString().trim();
                player_name_2 = edt_player_2.getText().toString().trim();
                //شرط گذاشتیم در صورتی که کاربر نامی وارد نکرد از اسم های پیشفرض استفاده کنه
                if (player_name_1.equals(""))player_name_1 = "بازیکن 1";
                if (player_name_2.equals(""))player_name_2 = "بازیکن 2";

                txt_player1.setText(player_name_1);
                txt_player2.setText(player_name_2);
                setColorTextViews();
                playerNamesDialog.dismiss();

            }
        });

        playerNamesDialog.show();
    }

    //رویدا کلیک روی خانه های بازی
    public void imagesClick(View view){

        int tag = Integer.parseInt((String) view.getTag());

        //شرط گذاشتیم در صورتی که یک خانه پر شده بود یا بازی تموم شده بود دیگه عملی انجام نشه
        if (state[tag] != NULL || game_over) return;

        ImageView imageView = (ImageView) view;
        //شرط گذاشتیم اگر نوبت بازیکن اول بود داخل خونه کلیک شده تصویر ضربدر بزاره نوبت عوض بشه و خانه مورد نظر در ارایه مربوط به حالت های خانه ها پر بشه
        if (turn == PLAYER_1){
            imageView.setImageResource(R.drawable.multiply);
            turn = PLAYER_2;
            state[tag] = PLAYER_1;
        }else {
            imageView.setImageResource(R.drawable.circle);
            turn = PLAYER_1;
            state[tag] = PLAYER_2;
        }
        setColorTextViews();
        getResult(false);
        click_snd.start();

    }

    @Override
    protected void onResume() {
        //مقدار دهی فایل های صوتی
        if (click_snd==null)click_snd = MediaPlayer.create(this,R.raw.click);
        if (win_snd==null)win_snd = MediaPlayer.create(this,R.raw.win_sound);
        super.onResume();
    }

    @Override
    protected void onPause() {
        //شرط گذاتیم در صورت بیرون رفتن از اکتیویتی مربوطه فایل های صوتی ریلیز بشن جهت جلوگیری از اکسپشن های احتمالی
        if (click_snd!=null)click_snd.release();
        if (win_snd!=null)win_snd.release();
        super.onPause();
    }


}