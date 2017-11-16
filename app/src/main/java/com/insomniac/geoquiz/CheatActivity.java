package com.insomniac.geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_ANSWER_IS_TRUE = "com.insomniac.geoquiz.cheatActivity";
    private static final String EXTRA_ANSWER_SHOWN = "com.insomniac.geoquiz.answer_shown";
    private static final String cheatingResult = "com.insomniac.geoquiz.cheatingResult";
    private static final String TAG = "CheatActivity";
    private boolean mAnswerTrue;
    private TextView mAnswerTextView;
    private Button mShowAnswer;
    private boolean answerShown = false;

    public static Intent newIntent(Context packageContext, boolean answerIsTrue){
        Log.d(TAG,"newwIntrnt");
        Intent i = new Intent(packageContext,CheatActivity.class);
        i.putExtra(EXTRA_ANSWER_IS_TRUE,answerIsTrue);
        return i;
    }

    public static boolean wasAnswerShown(Intent result){
        Log.d(TAG,"wasAnswerShown");
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN,false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
        Log.d(TAG,"onCreate");
        if (savedInstanceState != null) {
            Log.d(TAG,"savedInstanceState");
            answerShown = savedInstanceState.getBoolean(cheatingResult, false);
        }

        mAnswerTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        mShowAnswer = (Button) findViewById(R.id.cheat_button);
        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);
        mShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAnswerTrue)
                    mAnswerTextView.setText(R.string.true_answer);
                else
                    mAnswerTextView.setText(R.string.false_answer);
                answerShown = true;
                setAnswerShownResult(answerShown);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    int cx = mShowAnswer.getWidth() / 2;
                    int cy = mShowAnswer.getHeight() / 2;
                    float radius = mShowAnswer.getWidth();
                    Animator animator = ViewAnimationUtils.createCircularReveal(mShowAnswer, cx, cy, radius, 0);
                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animator1) {
                            super.onAnimationEnd(animator1);
                            mAnswerTextView.setVisibility(View.VISIBLE);
                            mAnswerTextView.setVisibility(View.INVISIBLE);
                        }
                    });
                    animator.start();
                }
                else {
                    mAnswerTextView.setVisibility(View.VISIBLE);
                    mAnswerTextView.setVisibility(View.INVISIBLE);
                }

            }
        });
    }

    private void setAnswerShownResult(boolean isAnswerShown){
        Log.d(TAG,"setAnswerShownResult");
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN,isAnswerShown);
        setResult(RESULT_OK,data);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstance){
        Log.d(TAG,"onSaveInstanceState");
        super.onSaveInstanceState(savedInstance);
        savedInstance.putBoolean(cheatingResult,answerShown);
    }
}
