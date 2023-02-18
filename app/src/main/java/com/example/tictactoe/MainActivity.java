package com.example.tictactoe;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    //متد کلیک  روی دکمه ها
    public void buttonsClick(View view){
           int id = view.getId();
           //این دستورات در داخل سویئچ مربوط به چهار دکمه ای است در خانه قرار دارد.
           switch(id){
               case R.id.btn_single_player:levelDialog();
                    break;
               case R.id.btn_double_player:
                   startActivity(new Intent(MainActivity.this, doublePlayer.class));
                   break;
               case R.id.btn_Guide:
                   startActivity(new Intent(MainActivity.this,guideGame.class));
                   break;
               case R.id.btn_contact_us:
                   startActivity(new Intent(MainActivity.this,contactGame.class));
           }
        }
    //دیالوگ انتخاب سطح بازی
    private void levelDialog(){
        Dialog LevelDialog = new Dialog(this);
        LevelDialog.setContentView(R.layout.level_dialog);
        LevelDialog.setCancelable(false);
        //این سه خط کد مربوط به دکمه ها در بازی با ربات می باشد.
        Button btn_start = LevelDialog.findViewById(R.id.btn_start);
        RadioButton rdo_easy = LevelDialog.findViewById(R.id.rdo_easy);
        RadioButton rdo_hard = LevelDialog.findViewById(R.id.rdo_hard);
        //این قطعه کد ها مربوط به سطح بازی انتخاب شده توسط شما متناسب با آن سطح اکتیوتی را اتصال می دهد.
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rdo_easy.isChecked()){
                    startActivity(new Intent(MainActivity.this,easySinglePlayer.class));
                }else if(rdo_hard.isChecked())
                    startActivity(new Intent(MainActivity.this,hardSinglePlayer.class));
                LevelDialog.dismiss();
            }
        });
        LevelDialog.show();
    }
}