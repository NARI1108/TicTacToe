package com.example.tictactoe;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
//بیس اکتیویتی در اینجا ایجاد کردیم در واقعا کارش این است که تمام متد ها و شئ ها و.... که تکراری هستند را جمع آوری نماییم و در اینجا تعریف کنیم که کلاس های دیگر به این دستور ها نیاز داشتند بتواند استفاده نمایید.
//  در واقعا کار بیس اکتیویتی این است تعداد کد ها را کم می کند.
public class BaseActivity extends AppCompatActivity {
    final static int PLAYER_2 = 2, No_winner = 3, PLAYER_1 = 1, NULL = 0;
    TextView txt_player1, txt_player2, txt_score_2, txt_score_1, txt_result;
    ImageView img_0, img_1 , img_2, img_3, img_4, img_5 , img_6, img_7, img_8;
    String player_name_1, player_name_2;
    Button btn_play_again;
    RelativeLayout result_layout;
    boolean game_over=false;
    int score_1=0, score_2=0, winner = -1, turn = PLAYER_1;//1=PLAYER_1 and 2=PLAYER_2
    int[] state ={NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL};// 0 = NULL and 1=PLAYER_1 and 2=PLAYER_2
    int[][] winner_position = {{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}};
    int[][] robot_action_2_position = {{0,1},{1,0},{1,2},{2,1},{3,4},{4,3},{4,5},{5,4},{6,7},{7,6},{7,8},{8,7}
            ,{0,3},{3,0},{3,6},{6,3},{1,4},{4,1},{4,7},{7,4},{2,5},{5,2},{5,8},{8,5},{0,4},{4,0},{4,8},{8,4},{2,4},{4,2},{4,6},{6,4}};
    //حالت های ممکن برای یکی از حرکت های ربات
    int[][] robot_action_3_position = {{0,1,2},{1,2,0},{0,2,1},{3,4,5},{4,5,3},{3,5,4},{6,7,8},{7,8,6},{6,8,7},{0,3,6},{3,6,0},{0,6,3}
            ,{1,4,7},{4,7,1},{1,7,4},{2,5,8},{5,8,2},{2,8,5},{0,4,8},{4,8,0},{0,8,4},{2,4,6},{4,6,2},{2,6,4}};
    int[] final_winner_position;
    //آرایه پویا
    ArrayList<ImageView> image_Views_List = new ArrayList<>();
    MediaPlayer click_snd ,  win_snd , loos_snd;
    //این متد در واقع اعداد تصادفی بین 0 تا 8 را تولید می کند که ربات برای پر کردن خانه از آن استفاده می کند.
    public int getRandomNumber(int max) {
        Random random = new Random();
        return random.nextInt(max);
    }
    //متد کلیک ربات (متناسب با عدد ورودی یکی از خانه ها توسط ربات پر میشه)
    public void robotClick(int tag){
        if (state[tag] != NULL || game_over) return;
        image_Views_List.get(tag).setImageResource(R.drawable.multiply);
        turn = PLAYER_2;
        setColorTextViews();
        state[tag] = PLAYER_1;
        getResult(true);//بازی تمام شده است .
        click_snd.start();
    }
    //حرکت ربات در صورتی که حداقل 1 خانه پر شده باشه
    public void robotAction_1(){
        int random = getRandomNumber(state.length);//اعداد بین 0 تا 8 است.
        if (!game_over)//!game_over =بازی تمام نشده است.
             {
            if (state[random]==NULL) robotClick(random);else
                robotAction_1();}
    }
    //حرکت ربات در صورتی که حداقل یک خانه توسط ربات پر شده و اطراف آن خانه خالی میباشد و میتونه توسط ربات پر بشه
    public void robotAction_2(){
        if (!game_over){
             /*
             اگر صفر پر بود و یک خالی بود بیا یک را پر کن.
            if (status[0]==PLAYER_1 && status[1]==NULL){
                 robotClick(1);
                 اگر یک پر بود و دوخالی بود بیا دو را پر کن.
             }else if (status[1]==PLAYER_1 && status[2]==NULL){
                 robotClick(2);
                 اگر یک پر بود و صفر خالی بود بیا صفر را پر کن.
             }else if (status[1]==PLAYER_1 && status[0]==NULL){
                 robotClick(0);
             }
            */
            //دستور بالا همانند دستور پایین است.
            for (int [] action_2 : robot_action_2_position)
            {
                if (state[action_2[0]]==PLAYER_1 && state[action_2[1]]==NULL){
                    robotClick(action_2[1]);return;}
            }
            robotAction_1();
        }
    }
    //حرکت ربات در صورتی که دوتا خانه توسط حریف به طریقی پر شده که امکان برنده شدن حریف وجود دارد در این صورت شرط گذاشتیم که ربات بتونه تشخیص بده و جلوی برنده شدن حریف را بگیرد (حالت دفاع)
    public void robotAction_3(){
        //game_over = بازی تمام شده است and !game_over = بازی تمام نشده است
        if (!game_over){
            //اگر خانه 0 و 1 پر شده بود بیا خانه 2 رو پر کن .
       /* if (status[0]==PLAYER_2 && status[1]==PLAYER_2 && status[2]==NULL){
            robotClick(2);
           اگر خانه ای 1 و 2 پر شده بیا خانه ای 0 را پر کن .
        }else if (status[1]==PLAYER_2 && status[2]==PLAYER_2 && status[0]==NULL){
            robotClick(0);
           اگر خانه ای 0 و 2 پر شده بود بیا خانه ای 1 را پر کن .
        }else if (status[0]==PLAYER_2 && status[2]==PLAYER_2 && status[1]==NULL){
            robotClick(1);
        }*/
            //دستورات بالا در واقع بخشی از دستور است که نوشته شده است که برای کمتر کردن تعداد خط کد از دستور زیر استفاده می کنیم.
            for (int [] action_3 : robot_action_3_position)
            {
                if (state[action_3[0]]==PLAYER_2 && state[action_3[1]]==PLAYER_2 && state[action_3[2]]==NULL){
                    robotClick(action_3[2]);return;}
            }
            robotAction_2();
        }
    }
    //حرکت ربات در صورتی که حداقل دوخانه کنار هم توسط رباط پر شده و خانه سوم خالی باشه (امکان برنده شدن ربات وجود داشته باشه)(متد حمله ربات)
    public void robotAction_4(){
        if (!game_over){
            //اگر 0 و 1 پر بود بیا 2 را پر کن
       /* if (status[0]==PLAYER_1 && status[1]==PLAYER_1 && status[2]==NULL){
            robotClick(2);
           اگر 1 و 2 پر بود بیا 0 را پر کن.
        }else if (status[1]==PLAYER_1 && status[2]==PLAYER_1 && status[0]==NULL){
            robotClick(0);
           اگر 0 و 2 پر بود بیا 1 را پر کن.
        }else if (status[0]==PLAYER_1 && status[2]==PLAYER_1 && status[1]==NULL){
            robotClick(1);
        }*/
            //دستورات بالا در واقع بخشی از دستور است که نوشته شده است که برای کمتر کردن تعداد خط کد از دستور زیر استفاده می کنیم.
            for (int [] action_4 : robot_action_3_position)
            {
                if (state[action_4[0]]==PLAYER_1 && state[action_4[1]]==PLAYER_1 && state[action_4[2]]==NULL){
                    robotClick(action_4[2]);return;}
            }
            robotAction_3();
        }
    }
    //متد چک کردن تمام خانه جهت پیدا کردن برنده بازی
    public int checkWinner(){
        for (int [] win_pos : winner_position){
            if (state[win_pos[0]] == state[win_pos[1]] && state[win_pos[1]] == state[win_pos[2]] && state[win_pos[0]]!=NULL){
                final_winner_position = win_pos;//ترکیب نهایی برنده را داخل یک ارایه جدا ذخیره میکنیم
                return state[win_pos[0]];
            }
        }
        return No_winner;
    }
    //متد نمایش نتیجه بازی طبق عدد بدست امده برای برنده
    public void getResult(boolean robot){
        winner = checkWinner();
        if (winner!=No_winner || isFullAllCells()){
            game_over = true;
            for (int i =0; i< image_Views_List.size(); i++){
                image_Views_List.get(i).setEnabled(false);}
            String res_str = "";
            switch (winner){
                case 1: res_str = player_name_1 + " برنده شد "; score_1++; animationCells();
                    if (robot)loos_snd.start();else win_snd.start();break;
                case 2: res_str = player_name_2+ " برنده شد "; score_2++; animationCells(); win_snd.start();break;
                case 3:res_str = "مساوی شدید";break;
            }
            txt_score_1.setText(String.valueOf(score_1));
            txt_score_2.setText(String.valueOf(score_2));
            //نمایان کردن لایه مربوط به نمایش نتیجه نهایی
            txt_result.setText(res_str);
            result_layout.setVisibility(View.VISIBLE);
            setColorCells();
        }
    }
    //متد بررسی کردن اینکه تمام خانه ها پرشده یا نه
    public boolean isFullAllCells(){
        for (int j : state) {
            if (j == NULL) return false;
        }
        return true;
    }
    //متد ریست کردن بازی جهت فراهم شدن امکان بازی مجدد
    public void resetGame(boolean robot){
        if (winner == No_winner)turn = PLAYER_1;else turn = winner;
        if (!robot)setColorTextViews();//شرط گذاشتیم فقط در حالت بازی 2 نفره رنگ تکست ویو ها عوض بشه
        game_over = false;
        winner = -1;
        Arrays.fill(state, NULL);//خالی قرار دادن وضعیت تمام خانه جهت انجام بازی جدید
        /*img_0.setImageResource(0);
        img_1.setImageResource(0);
        img_2.setImageResource(0);
        img_3.setImageResource(0);
        img_4.setImageResource(0);
        img_5.setImageResource(0);
        img_6.setImageResource(0);
        img_7.setImageResource(0);
        img_8.setImageResource(0);*/
        for (int i =0; i< image_Views_List.size(); i++){
            image_Views_List.get(i).setImageResource(0);
            image_Views_List.get(i).setEnabled(true);
        }
        result_layout.setVisibility(View.GONE);
        //شرط گذاشتیم در بازی با ربات اگر نوبت ربات بود حرکت اول انجام بشه
        if (turn==PLAYER_1 && robot)robotClick(getRandomNumber(state.length));
        //چون این متد در حالت بازی دونفره هم انجام میشه پس مجبور شدیم برای دکمه بازی مجدد رویداد کلیک تعریف کنیم و برای این متد یه متغییر بولین تعریف کنیم تا مشخص بشه تو حالت بازی دونفره این متد صدا زده شده یا بازی با ربات
    }
    //با این متد بعد پایان بازی تمام خانه ها سیاه سفید میشن و در صورت برنده داشتن 3 تا خونه برنده رنگی میشه
    private void setColorCells(){
        for (int i =0; i< image_Views_List.size(); i++){
            if (state[i]==1){
                image_Views_List.get(i).setImageResource(R.drawable.multiply_grey);
            }else if (state[i]==2){
                image_Views_List.get(i).setImageResource(R.drawable.circle_grey);
            }
            if (winner== PLAYER_1){
                image_Views_List.get(final_winner_position[0]).setImageResource(R.drawable.multiply);
                image_Views_List.get(final_winner_position[1]).setImageResource(R.drawable.multiply);
                image_Views_List.get(final_winner_position[2]).setImageResource(R.drawable.multiply);
            }else if (winner == PLAYER_2){
                image_Views_List.get(final_winner_position[0]).setImageResource(R.drawable.circle);
                image_Views_List.get(final_winner_position[1]).setImageResource(R.drawable.circle);
                image_Views_List.get(final_winner_position[2]).setImageResource(R.drawable.circle);
            }
        }
    }
    //متد نمایش انیمیشن برای 3 خانه دوز شده
    public void animationCells(){
        ArrayList<Animator> animator_list = new ArrayList<>();
        for (int pos : final_winner_position) {
            ObjectAnimator myAnimator = ObjectAnimator.ofFloat(image_Views_List.get(pos),"scaleX",0.5f);
            myAnimator.setRepeatCount(7);
            myAnimator.setRepeatMode(ValueAnimator.REVERSE);
            animator_list.add(myAnimator);
            myAnimator = ObjectAnimator.ofFloat(image_Views_List.get(pos),"scaleY",0.5f);
            myAnimator.setRepeatCount(7);
            myAnimator.setRepeatMode(ValueAnimator.REVERSE);
            animator_list.add(myAnimator);
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator_list);
        animatorSet.setDuration(200);
        animatorSet.start();
    }
    //متد تغییر رنگ تکست ویو ها جهت مشخص شدن اینکه نوبت کدوم بازیکنه
    public void setColorTextViews(){
        if (turn==PLAYER_1){
            txt_player1.setTextColor(getResources().getColor(R.color.blue));
            txt_player1.setBackgroundResource(R.drawable.style2);
            txt_score_1.setTextColor(getResources().getColor(R.color.black));

            txt_player2.setTextColor(getResources().getColor(R.color.grey));
            txt_player2.setBackgroundResource(R.drawable.style3);
            txt_score_2.setTextColor(getResources().getColor(R.color.grey));
        }else {
            txt_player2.setTextColor(getResources().getColor(R.color.red));
            txt_player2.setBackgroundResource(R.drawable.style2);
            txt_score_2.setTextColor(getResources().getColor(R.color.black));

            txt_player1.setTextColor(getResources().getColor(R.color.grey));
            txt_player1.setBackgroundResource(R.drawable.style3);
            txt_score_1.setTextColor(getResources().getColor(R.color.grey));
        }
    }
}
