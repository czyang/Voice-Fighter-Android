package me.codeand.ahahpah_en;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder.AudioSource;
import android.util.Log;

/**
 * @author Chengzhi Yang
 * @email coding.game[at]gmail.com
 */
public class Recorder {
	
	// ===========================================================
	// Constants
	// ===========================================================
	public static int SEN_BASE = 200;
	
	// ===========================================================
	// Fields
	// ===========================================================
	public boolean recording = false;
	private AudioRecord recorder;
	private short audioData[];
	private int bufferSize;
	
	
    // ===========================================================
	// Constructors
	// ===========================================================
    public Recorder() {
		bufferSize = AudioRecord.getMinBufferSize(44100,
				AudioFormat.CHANNEL_CONFIGURATION_MONO,
				AudioFormat.ENCODING_PCM_16BIT);
		// get the buffer size to use with this audio record
		recorder = new AudioRecord(AudioSource.MIC, 44100,
				AudioFormat.CHANNEL_CONFIGURATION_MONO,
				AudioFormat.ENCODING_PCM_16BIT, bufferSize);
		
		audioData = new short[bufferSize];
		
		recording = true;
    }
	
    // ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
    
    // ===========================================================
	// Methods
	// ===========================================================
    public void update () {
		/*
		 * if (recorder != null) { recorder.stop(); recorder.release(); }
		 */
		assert recorder != null;

		// loop while recording is needed
		if (recorder.getState() == android.media.AudioRecord.STATE_INITIALIZED) {
			if (recorder.getRecordingState() == android.media.AudioRecord.RECORDSTATE_STOPPED)
				recorder.startRecording();
			else {
				int j = recorder.read(audioData, 0, bufferSize);
				// read the PCM audio data into the audioData array
				long upSum = 0;
				int upCount = 0;
				int shootCount = 0;
				int index = 0;
				int maxUp = 0;
				while (true) {
					if (index >= j)
						break;
					int k = Math.abs(audioData[index]);
					if (k > MainGame.mAh && k < MainGame.mPah) {
						// Log.d("Voice","up"+k);
						upCount++;
						upSum += k;
						if ( k > maxUp ) {
							maxUp = k;
						}
					}
					if (k > MainGame.mPah) {
						// Log.d ("Voice", "shoot" + k);
						shootCount++;
					}
					index++;
				}
				MainGame.mMaxUp = maxUp;
				//Log.d("Voice", "" + maxUp );
				//Log.d("Voice", "" + upCount + " " + shootCount);

				if ( shootCount > 50 ) {
					MainGame.pahh = true;
				} else if ( upCount != 0 ) {
					MainGame.ahh = true;
					MainGame.mVoicePoint = upSum / upCount;
				} else {
					MainGame.mVoicePoint = 0;
				}
			}
		}
	}
    
    public void release () {
		if (recorder.getState() == android.media.AudioRecord.RECORDSTATE_RECORDING)
			recorder.stop(); // stop the recorder before ending the thread
		recorder.release();
		// release the recorders resources
		recorder = null;
    }
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================}
}