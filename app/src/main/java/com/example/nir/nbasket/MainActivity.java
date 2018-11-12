package com.example.nir.nbasket;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends Activity implements RecognitionListener {
    public final int MY_PERMISSIONS_REQUEST=1;
    private static  SpeechRecognizer speech;
    private double TotalShots=0;
    private double InShots=0;
    private double OutShots=0;
    private double PresShots=0;
    private int StrairtShots=0;
    ArrayList<String> listItems=new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private ListView list;
    private TextToSpeech textToSpeechSystem;
    private int sPoints=3;
    private  SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        list = (ListView) findViewById(R.id.ListStrait);
        listItems = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, listItems);
        list.setAdapter(adapter);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECORD_AUDIO,Manifest.permission.INTERNET},
                MY_PERMISSIONS_REQUEST);
        textToSpeechSystem=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    textToSpeechSystem.setLanguage(Locale.US);
                }
            }
        });

        EditText et= (EditText)findViewById(R.id.maxStr);
        et.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ((EditText)view).setText("");

            }
    });

        Button button= (Button)findViewById(R.id.bStart);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                clearText();
                try
                {
                    EditText et= findViewById(R.id.maxStr);
                    sPoints = Integer.valueOf(et.getText().toString());
                }
                catch (Exception e)
                {
                    sPoints=3;
                }
                T2s(("Set sequence to " + String.valueOf(sPoints).toString()));

                T2s("Starting ");

                startVoiceR();
            }
        });






    }


    private void startVoiceR()
    {
        try {
            Log.i("startVoiceR", "startVoiceR");

            if (speech!=null) {
                //speech.stopListening();
                speech.destroy();
                Intent recognizerIntent =null;
                speech = SpeechRecognizer.createSpeechRecognizer(this);
                speech.setRecognitionListener(this);

                recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en");

                recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());

                //recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.EXTRA_LANGUAGE);//.LANGUAGE_MODEL_WEB_SEARCH);
                recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
                speech.startListening(recognizerIntent);
                Log.i("startVoiceR", "speech!=null");
            }
            else
            {
                speech = SpeechRecognizer.createSpeechRecognizer(this);
                speech.setRecognitionListener(this);

                Intent recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en");

                recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());

                //recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.EXTRA_LANGUAGE);//.LANGUAGE_MODEL_WEB_SEARCH);
                recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
                speech.startListening(recognizerIntent);
                Log.i("startVoiceR", "speech==null");
            }



        }
        catch(Exception e)
        {
            Log.i("startVoiceR",e.getMessage());        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    @Override

    public void onBeginningOfSpeech() {
        Log.i("onBeginningOfSpeech","onBeginningOfSpeech");
       // startVoiceR();
    }

    @Override

    public void onBufferReceived(byte[] buffer) {
        Log.i("onBufferReceived","onBufferReceived");
    }

    @Override

    public void onEndOfSpeech() {
        Log.i("onEndOfSpeech","onEndOfSpeech");
        //speech.destroy();
        //startVoiceR();
    }

    @Override

    public void onError(int errorCode) {

        switch (errorCode) {

            case SpeechRecognizer.ERROR_AUDIO:

                Log.i("message = R.string.error_audio_error","ERROR_AUDIO");
                //speech.destroy();
                startVoiceR();
                break;

            case SpeechRecognizer.ERROR_CLIENT:

                Log.i("message = R.string.error_client","ERROR_CLIENT");

                break;

            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:

                Log.i("message = R.string.error_permission","ERROR_INSUFFICIENT_PERMISSIONS");

                break;

            case SpeechRecognizer.ERROR_NETWORK:

                Log.i("message = R.string.error_network","ERROR_NETWORK");

                break;

            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:

                Log.i("message = R.string.error_timeout", "ERROR_NETWORK_TIMEOUT");
                //speech.destroy();
                startVoiceR();
                break;

            case SpeechRecognizer.ERROR_NO_MATCH:

                Log.i("message = R.string.error_no_match","ERROR_NO_MATCH");
               // speech.destroy();
                startVoiceR();
                break;

            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:

                Log.i("message = R.string.error_busy","ERROR_RECOGNIZER_BUSY");
                //speech.destroy();
               // startVoiceR();
                break;

            case SpeechRecognizer.ERROR_SERVER:

                Log.i("message = R.string.error_server","ERROR_SERVER");
                //speech.destroy();
                startVoiceR();
                break;

            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:

                Log.i("message = R.string.error_timeout","ERROR_SPEECH_TIMEOUT");
                //speech.destroy();
                startVoiceR();
                break;

            default:

                Log.i(" message = R.string.error_understand","");

                break;
        }
       // Log.i("OnError",String.valueOf(errorCode));
        //speech.startListening(recognizerIntent);
    }

  /*  @Override
    protected void onPause() {

        Log.i("onPause", "on pause called");
      /*  if (speech != null) {
            speech.stopListening();
            speech.cancel();
            speech.destroy();

        }
        //speech = null;*/
        //startVoiceR();
     /*   super.onPause();
    }*/



    @Override

    public void onEvent(int arg0, Bundle arg1) {
        Log.i("onEvent ", "onEvent");
    }

    @Override

    public void onPartialResults(Bundle arg0) {

        Log.i("onPartialResults ", "onPartialResults");
        //speech.destroy();
        startVoiceR();
    }

    @Override

    public void onReadyForSpeech(Bundle arg0) {
        Log.i("onReadyForSpeech ", "onReadyForSpeech");
        //speech.destroy();
        //startVoiceR();

    }

    @Override
    public void onResults(Bundle results) {

        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

        Log.i("Result ", matches.get(0));
        if((matches.get(0).contains("NO") || (matches.get(0).contains("no")))) addtoOut();
        if((matches.get(0).contains("YES") || (matches.get(0).contains("yes")|| (matches.get(0).contains("ye")))))  addtoIn();
        //speech.destroy();
        startVoiceR();
    }

    private void addtoIn() {
        InShots++;
        StrairtShots++;
        TotalShots++;
        if (StrairtShots==sPoints) T2s("You Have reach your sequence ") ;
        addtoPres();
        updateallText();
        Log.i("addtoIn ",String.valueOf(InShots));
        T2s("OK");

    }
    private void addtoPres() {
        PresShots = (InShots/TotalShots)*100;
        Log.i("addtoPres ",String.valueOf(PresShots));
        Log.i("TotalShots ",String.valueOf(TotalShots));

        //updateallText();
    }
    private void addtoOut()
    {
        OutShots++;
        if (StrairtShots>0)
        {
            addTolist(StrairtShots);
            T2s("You Have reach sequence of " + String.valueOf(StrairtShots).toString());
            StrairtShots=0;
        }
        TotalShots++;
        addtoPres();
        updateallText();
        T2s("OK");
    }

    private void clearText()
    {
        OutShots=0;
        TotalShots=0;
        InShots=0;
        PresShots=0;
        adapter.clear();
    }

    private void updateallText()
    {
        TextView otv = findViewById(R.id.outTV);
        TextView ttv = findViewById(R.id.TotalTV);
        TextView itv = findViewById(R.id.inTV);
        TextView ptv = findViewById(R.id.perTV);
        otv.setText(String.valueOf(OutShots));
        ttv.setText(String.valueOf(TotalShots));
        itv.setText(String.valueOf(InShots));
        ptv.setText(new DecimalFormat("##.##").format(PresShots));

    }

    private void T2s(String t)
    {

        textToSpeechSystem.speak(t, TextToSpeech.QUEUE_ADD, null);
    }
    private void addTolist(int ps)
    {
        ListView lv = findViewById(R.id.ListStrait);
        listItems.add(format.format(Calendar.getInstance().getTime()) + " SIAR - " + String.valueOf(ps));
        // next thing you have to do is check if your adapter has changed
        adapter.notifyDataSetChanged();
    }
    @Override

    public void onRmsChanged(float rmsdB) {
     //  Log.i("onRmsChanged ", "onRmsChanged");
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        speech.destroy();
        textToSpeechSystem.stop();
    }

}
