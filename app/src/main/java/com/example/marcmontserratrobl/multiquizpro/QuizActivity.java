package com.example.marcmontserratrobl.multiquizpro;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {


    public static final String CORRECT_ANSWER = "correct_answer";
    public static final String CORRECT_QUESTION = "correct_question";
    public static final String ANSWER_IS_CORRECT = "answer_is_correct";
    public static final String ANSWER = "answer";
    private int ids_answers[]={
            R.id.answer1, R.id.answer2, R.id.answer3, R.id.answer4
    };
    private String[] all_questions;

    private TextView text_question;
    private RadioGroup group;
    private Button btn_next,btn_prev;

    private int correct_answer;
    private int current_question;
    private boolean [] answer_is_correct;
    private int[] answer;

    @Override
    protected void onSaveInstanceState(Bundle outState) {// escriure onSaveInst... i agafar protected void, ha de tenir només un paràmetre
        Log.i("lifecycle","onSaveInstanceState");
        super.onSaveInstanceState(outState);
        outState.putInt(CORRECT_ANSWER,correct_answer);//Guardem les dades
        outState.putInt(CORRECT_QUESTION,current_question); //Guardem les dades
        outState.putBooleanArray(ANSWER_IS_CORRECT,answer_is_correct);//Guardem les dades
        outState.putIntArray(ANSWER,answer);//Guardem les dades

    }

    @Override
    protected void onStop() {
        Log.i("lifecycle","onStop");
        super.onStop();
    }

    @Override
    protected void onStart() {
        Log.i("lifecycle","onStart");
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        Log.i("lifecycle","onDestroy");
        super.onDestroy();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("lifecycle","onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        text_question = (TextView) findViewById(R.id.text_question);
        group = (RadioGroup) findViewById(R.id.answer_group);
        btn_next = (Button) findViewById(R.id.btn_check);
        btn_prev=(Button) findViewById(R.id.btn_prev);

        all_questions = getResources().getStringArray(R.array.all_questions);

        if (savedInstanceState==null){
            startOver();
        }else {
            Bundle state= savedInstanceState; //creem un bundle per recuperar les dades
            correct_answer = state.getInt(CORRECT_ANSWER);//anem recuperant els int, arrays... que haviem guardat a dalt
            current_question= state.getInt(CORRECT_QUESTION);
            answer_is_correct= state.getBooleanArray(ANSWER_IS_CORRECT);
            answer= state.getIntArray(ANSWER);
            showQuestion();//mostrs ara sí la pregunta
        }

        //final int correct_answer = getResources().getInteger(R.integer.correct_answer);


        btn_next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                checkAnswer();
                if(current_question<all_questions.length-1) {
                    current_question++;
                    showQuestion();
                }else {
                    checkResults();
                }
            }
        });

        btn_prev.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            checkAnswer();
                                            if (current_question>0){
                                                current_question--;
                                                showQuestion();
                                            }                                        }
                                    });


    }

    private void startOver() {
        answer_is_correct = new boolean[all_questions.length];
        answer= new int [all_questions.length];
        for(int i=0; i<answer.length;i++){
            answer[i]=-1;
        }
        current_question=0;//torna a la primera pregunta per començar de nou el test
        showQuestion();
    }

    private void checkResults() {
        int correctas=0, incorrectas=0, nocontestadas=0;
        for (int i = 0; i < all_questions.length; i++){
            if (answer_is_correct[i]) correctas++;
            else if (answer[i] == -1) nocontestadas++; //incrementa les no contestades ja que es queden amb -1
            else incorrectas++;
        }

        //TODO: Permitir la traducción de este texto:
        String message =
                String.format("Correctas: %d\nIncorrectas: %d\nNo contestadas: %d\n",
                        correctas, incorrectas, nocontestadas);
        AlertDialog.Builder builder= new AlertDialog.Builder(this);// CREAR un creador de quadres de diagles, importar classe de android suport v7, this per a l'acivitat mateix,
        builder.setTitle(R.string.results);
        builder.setMessage(message);
        builder.setCancelable(false);// no es pot tornar enrere un cop ha sortit el quadre de dialeg
        builder.setPositiveButton(R.string.finish, new DialogInterface.OnClickListener() { //CTRL+barra
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton(R.string.start_over, new DialogInterface.OnClickListener() {//CTRL+barra on clicklistener
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //borrar les respostes i tornar a la pregunta 0
                startOver();//metode per resetejar la app

            }
        });
        builder.create().show();//mostra el quadre de dialeg
    }

    private void checkAnswer() {
        int id = group.getCheckedRadioButtonId();
        int ans= -1;
        for (int i=0;i<ids_answers.length;i++){
            if(ids_answers[i]==id){
                ans=i;
            }
        }

        answer_is_correct [current_question]= (ans == correct_answer);
        answer[current_question]=ans;
    }

    private void showQuestion() {

        String q = all_questions[current_question];
        String [] parts = q.split(";");

        group.clearCheck();
        text_question.setText(parts[0]);

        //String[] answers = getResources().getStringArray(R.array.);

        for (int i=0; i<ids_answers.length;i++){
            RadioButton rb = (RadioButton) findViewById(ids_answers[i]);
            String ans =parts [i+1];
            if(ans.charAt(0)=='*'){
                correct_answer=i;
                ans=ans.substring(1);
            }
            rb.setText(ans);
            if (answer[current_question]==i){
                rb.setChecked(true);
            }
        }

        if (current_question==0){
            btn_prev.setVisibility(View.GONE);
        }else{
            btn_prev.setVisibility(View.VISIBLE);
        }

        if (current_question==all_questions.length-1){
            btn_next.setText(R.string.finish);
        }else {
            btn_next.setText(R.string.next);
        }

    }
}
