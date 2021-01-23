package org.agentspace.android;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.widget.Toast;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.HashMap;
import java.util.Locale;

public class Speecher {
    
    private TextToSpeech textToSpeech;
    private boolean initialized;
    private AudioManager am;
    private int streamMaxVolume;
    private int streamVolume;
    private boolean headset;
    private Activity activity;
    private String language;
    private boolean speaking;
    
    public Speecher (Activity _activity) {
        this(_activity, "");
    }
    
    public Speecher (Activity _activity, String _language) {
        activity = _activity;
        language = _language;
        initialized = false;
        speaking = false;
        textToSpeech = new TextToSpeech(activity.getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int ttsLang;
                    if (language.equals("")) {
                        ttsLang = textToSpeech.setLanguage(Locale.US);
                    }
                    else {
                        Locale locale = new Locale(language.toLowerCase(),language.toUpperCase());
                        ttsLang = textToSpeech.setLanguage(locale);
                    }

                    if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                            || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("Speecher", "The Language "+language+" is not supported!");
                    } else {
                        Log.i("Speecher", "Language "+language+" Supported.");
                        textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                            @Override
                            public void onStart(String utteranceId) {
                                Log.i("Speecher","On Start");
                                setSpeaking(true);
                            }

                            @Override
                            public void onDone(String utteranceId) {
                                Log.i("Speecher","On Done");
                                setSpeaking(false);
                            }

                            @Override
                            public void onError(String utteranceId) {
                                Log.i("Speecher","On Error");
                                setSpeaking(false);
                            }
                        });
                        Log.i("Speecher", "Initialization success.");
                        initialized = true;
                    }
                }
                if (!initialized) {
                    Toast.makeText(activity.getApplicationContext(), "TTS Initialization failed!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        am = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        streamMaxVolume = am.getStreamMaxVolume(am.STREAM_VOICE_CALL);
        streamVolume = am.getStreamVolume(am.STREAM_VOICE_CALL);

        setHeadset(false);
        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        activity.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
                    int state = intent.getIntExtra("state", -1);
                    switch (state) {
                        case 0:
                            Log.i("Speecher", "Headset is unplugged");
                            setHeadset(false);
                            break;
                        case 1:
                            Log.i("Speecher", "Headset is plugged");
                            setHeadset(true);
                            break;
                    }
                }
            }
        }, filter);

    }

    private void setHeadset(boolean _headset) {
        headset = _headset;
        am.setSpeakerphoneOn(!headset);
    }

    private void setSpeaking(boolean _speaking) {
        speaking = _speaking;
    }

    public boolean isInitialized() { return initialized; }
    
    public boolean isSpeaking() {
        return speaking;
        //if (textToSpeech == null) return false;
        //return textToSpeech.isSpeaking();
    }
    
    public boolean isHeadset() {
        return headset;
    }
    
    public void speak (String text) {
        if (textToSpeech == null) return;

        // set volume
        int actualVolume = headset ? streamVolume : streamMaxVolume;
        am.setStreamVolume(am.STREAM_VOICE_CALL, actualVolume,0 );

        // speak
        Log.i("Speecher", "speaking: " + text);
        speaking = true;
        HashMap<String, String> params = new HashMap <String, String>();
        params.put(TextToSpeech.Engine.KEY_PARAM_VOLUME, "1.0");
        params.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(AudioManager.STREAM_VOICE_CALL));
        params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "SPEECHER");
        int speechStatus = textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, params);
        if (speechStatus == TextToSpeech.ERROR) {
            Log.e("Speecher", "Error in converting Text to Speech!");
            speaking = false;
        }

    }

    public void release() {
        if (textToSpeech == null) return;
        textToSpeech.stop();
        textToSpeech.shutdown();
    }
    
}
