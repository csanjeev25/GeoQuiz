package com.insomniac.geoquiz;



public class Question {

    private int mQuestionId;
    private boolean mAnswerTrue;

    Question(int QuestionId,boolean mAnswerTrue){
        this.mQuestionId = QuestionId;
        this.mAnswerTrue = mAnswerTrue;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }

    public int getQuestionId() {
        return mQuestionId;
    }

    public void setQuestionId(int questionId) {
        mQuestionId = questionId;
    }
}
