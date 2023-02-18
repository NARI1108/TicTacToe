package com.example.tictactoe;
import android.app.Dialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class easySinglePlayer extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //لایه ای ایکس ام ال بازی دو نفره را برای بازی یک نفره آسان پیوند میدهیم چون زمین بازی در هر دو بازی یک نفره و دو نفره یکسان است.
        setContentView(R.layout.activity_level2);
        //آدرس دهی نام بازیکنان و امتیازات به لایه ای ایکس ام ال هاشون.
        txt_player1 = findViewById(R.id.txt_player1);  txt_player2 = findViewById(R.id.txt_player2);
        txt_score_1 = findViewById(R.id.txt_score_1);  txt_score_2 = findViewById(R.id.txt_score_2);
        //آدرس دهی ایمیج ویو ها به لایه ای ایکس ام ال ها شون
        img_0 = findViewById(R.id.img_0); img_1 = findViewById(R.id.img_1); img_2 = findViewById(R.id.img_2);
        img_3 = findViewById(R.id.img_3); img_4 = findViewById(R.id.img_4); img_5 = findViewById(R.id.img_5);
        img_6 = findViewById(R.id.img_6); img_7 = findViewById(R.id.img_7); img_8 = findViewById(R.id.img_8);
        // اآدرس دهی کادر و متنی که چه کسی برنده شده است و دکمه شروع بازی ، به لایه ای ایکس ام ال هاشون
        txt_result = findViewById(R.id.txt_result);
        btn_play_again = findViewById(R.id.btn_play_again);
        result_layout = findViewById(R.id.result_layout);
        //افزودن ایمیج ویو به آرایه ای پویا در بیس اکتیویتی در خط 36
        image_Views_List.add(img_0); image_Views_List.add(img_1); image_Views_List.add(img_2);
        image_Views_List.add(img_3); image_Views_List.add(img_4); image_Views_List.add(img_5);
        image_Views_List.add(img_6); image_Views_List.add(img_7); image_Views_List.add(img_8);
        //فراخوانی متد (دیالوگ انتخاب نام بازیکن)
        playerNameDialog();
        //متد کلیک دکمه بازی مجدد
        btn_play_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetGame(true);
            }
        });
    }
    //دیالوگ انتخاب نام بازیکن
    private void playerNameDialog(){
        Dialog playerNamesDialog = new Dialog(this);
        playerNamesDialog.setContentView(R.layout.player_names_dialog);
        playerNamesDialog.setCancelable(false);
        //آدرس دهی نام بازیکنان و دکمه ای شروع بازی به لایه ای ایکس ام ال هاشون .
        EditText edt_player_1 = playerNamesDialog.findViewById(R.id.edt_player_1);
        EditText edt_player_2 = playerNamesDialog.findViewById(R.id.edt_player_2);
        Button btn_start = playerNamesDialog.findViewById(R.id.btn_start);
        //در بازی یک نفره با ربات ، ربات را به عنوان بازیکن 1 قرار می دهیم و بنابراین نام بازیکن 1 را حذف می کنیم و بازیکن 2 در واقع خدمان هستیم . چرا بازیکن 1 را محو کردیم چون ما از زمین بازی دو نفره استفاده می کنیم.
        edt_player_1.setVisibility(View.GONE);
        //مربوط به قسمتی است که در انجا نام خود را وارد می کنید .در انجا نوشته (نام شما) را وارد کنید.
        edt_player_2.setHint(R.string.your_name);

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // بازیکن 1 در واقع ربات ما هست که با هوش مصنوعی کار می کند .
                player_name_1 = getString(R.string.Robot_Easy);
                player_name_2 = edt_player_2.getText().toString().trim();
                //بازیکن 2 در واقع ما هستیم.
                if (player_name_2.equals(""))player_name_2 = getString(R.string.You);
                // قرار دادن نام بازیکنان در قسمت نام در بازی
                txt_player1.setText(player_name_1);
                txt_player2.setText(player_name_2);

                playerNamesDialog.dismiss();
                //متد را فراخوانی می کنیم
                robotClick(getRandomNumber(9));// اعداد تصادفی بین 0 تا 8است.
            }
        });
        playerNamesDialog.show();
    }
    //متد کلیک روی خانه ها توسط کاربر
    public void imagesClick(View view){

        int tag = Integer.parseInt((String) view.getTag());
        if (state[tag] != NULL || game_over) return;
        ImageView imageView = (ImageView) view;
        //ادرس دهی
        imageView.setImageResource(R.drawable.circle);
        //نوبت بازی به بازیکن 1 تعلق می گیرد.
        turn = PLAYER_1;
        setColorTextViews();
        state[tag] = PLAYER_2;
        click_snd.start();
        getResult(true);
        //فراخوانی متد(حالت دفاع)
        robotAction_3();
    }
    @Override
    protected void onResume() {
        //مقدار دهی فایل های صوتی
        if (click_snd==null)click_snd = MediaPlayer.create(this,R.raw.click);//این صدا زمانی اجرا می شود که ما بر روی خانه ها کلیک می کنیم بسته به بازیکنان ، ضربه در و دایره در خانه ایجاد می شود.
        if (win_snd==null)win_snd = MediaPlayer.create(this,R.raw.win_sound);//این صدا زمانی اجرا می شود که ما  برنده شویم
        if (loos_snd==null)loos_snd = MediaPlayer.create(this,R.raw.fail_sound);// این صدا زمانی اجرا می شود که ما شکست خورده باشیم.
        super.onResume();
    }
    @Override
    protected void onPause() {
        //شرط گذاتیم در صورت بیرون رفتن از اکتیویتی مربوطه فایل های صوتی ریلیز بشن جهت جلوگیری از اکسپشن های احتمالی
        if (click_snd!=null)click_snd.release();
        if (win_snd!=null)win_snd.release();
        if (loos_snd!=null)loos_snd.release();
        super.onPause();
    }
}