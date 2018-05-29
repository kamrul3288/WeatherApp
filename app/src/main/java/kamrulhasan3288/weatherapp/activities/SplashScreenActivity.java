package kamrulhasan3288.weatherapp.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import kamrulhasan3288.weatherapp.R;

public class SplashScreenActivity extends AppCompatActivity {

    @BindView(R.id.splash_activity_message_text)
    TextView messageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);

        //----------show splash screen message with Roboto font------------------
        setMessageTextWIthCustomFont();

        //------------launch main activity after delay time-------------------
        LaunchMainActivityAfterDelayTime();

    }

    private void setMessageTextWIthCustomFont() {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/roboto_regular.ttf");
        messageText.setTypeface(typeface);
        messageText.setText(getResources().getString(R.string.app_name));
    }

    private void LaunchMainActivityAfterDelayTime() {
        int DELAY_TIME = 2000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreenActivity.this,MainActivity.class));
                finish();
            }
        }, DELAY_TIME);
    }


}
