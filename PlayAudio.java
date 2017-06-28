package com.il.tikkun.utils;

import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created by David vardi on 6/13/2016.
 */
public class PlayAudio {

    private boolean isPlaying;

    private MediaPlayer mMediaPlayer;

    private void play(String url,boolean isPlay) {

        this.isPlaying = isPlay;

        if (isPlaying) {

            try {

                mMediaPlayer = new MediaPlayer();

                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                mMediaPlayer.setDataSource(url);

                mMediaPlayer.prepareAsync();

                mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                    @Override
                    public void onPrepared(MediaPlayer mp) {

                        mp.start();

                        isPlaying = false;


                    }
                });

                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {

                        mMediaPlayer.release();

                        mMediaPlayer = null;

                        isPlaying = true;



                    }
                });

            } catch (IOException e) {

                e.printStackTrace();
            }

        } else {

            if (mMediaPlayer != null)

                mMediaPlayer.release();

            mMediaPlayer = null;

            isPlaying = true;


        }

    }
}
