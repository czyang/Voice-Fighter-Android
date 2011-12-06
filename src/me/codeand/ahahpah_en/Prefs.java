package me.codeand.ahahpah_en;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.Log;

/**
 * @author Chengzhi Yang
 * @email coding.game[at]gmail.com
 */
 
public class Prefs extends PreferenceActivity implements YesNoDialogPreference.YesNoDialogListener  {
	public static String SENSITIVITY = "Sensitivity";
	
	@Override
	protected void onCreate(Bundle  savedInstanceState)  {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
		
        Preference resetButton = getPreferenceManager().findPreference("reset");
        if (resetButton != null) {
        	YesNoDialogPreference yesNo = (YesNoDialogPreference)resetButton;
        	yesNo.setListener(this);
        }
	}
	
	public void onDialogClosed(boolean positiveResult) {
		if (positiveResult) {
			//SharedPreferences prefs = getSharedPreferences(PreferenceConstants.PREFERENCE_NAME, MODE_PRIVATE);
			//SharedPreferences.Editor editor = prefs.edit();
			//editor.commit();
			//Log.d("yes","yes");
			Preference tempP = getPreferenceManager().findPreference(SENSITIVITY);
			if ( tempP != null ) {
				MainGame.mPrefs.edit().putInt(SENSITIVITY, 0).commit();
				MainGame.mAh = 500;
				if ( MainGame.mAh < 2000 ) {
					MainGame.mPah = 7000;
				} else if ( MainGame.mAh < 4000 ) {
					MainGame.mPah = 9000;
				} else if ( MainGame.mAh < 6000 ) {
					MainGame.mPah = 11000;
				} else if ( MainGame.mAh < 8000 ) {
					MainGame.mPah = 13000;
				} else if ( MainGame.mAh < 10000 ) {
					MainGame.mPah = 15000;
				} else if ( MainGame.mAh < 12000 ) {
					MainGame.mPah = 17000;
				} else if ( MainGame.mAh < 14000 ) {
					MainGame.mPah = 19000;
				} else if ( MainGame.mAh < 16000 ) {
					MainGame.mPah = 21000;
				} else if ( MainGame.mAh < 18000 ) {
					MainGame.mPah = 25000;
				} else if ( MainGame.mAh < 20000 ) {
					MainGame.mPah = 30000;
				} else {
					MainGame.mPah = 32000;
				}
				finish();
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		int sen = MainGame.mPrefs.getInt(SENSITIVITY, 0);
		MainGame.mAh = 500 + sen * Recorder.SEN_BASE;
		if ( MainGame.mAh < 2000 ) {
			MainGame.mPah = 7000;
		} else if ( MainGame.mAh < 4000 ) {
			MainGame.mPah = 9000;
		} else if ( MainGame.mAh < 6000 ) {
			MainGame.mPah = 11000;
		} else if ( MainGame.mAh < 8000 ) {
			MainGame.mPah = 13000;
		} else if ( MainGame.mAh < 10000 ) {
			MainGame.mPah = 15000;
		} else if ( MainGame.mAh < 12000 ) {
			MainGame.mPah = 17000;
		} else if ( MainGame.mAh < 14000 ) {
			MainGame.mPah = 19000;
		} else if ( MainGame.mAh < 16000 ) {
			MainGame.mPah = 21000;
		} else if ( MainGame.mAh < 18000 ) {
			MainGame.mPah = 25000;
		} else if ( MainGame.mAh < 20000 ) {
			MainGame.mPah = 30000;
		} else {
			MainGame.mPah = 32000;
		}
		Log.d("dddddd", "dddd");
	}
	
	
}