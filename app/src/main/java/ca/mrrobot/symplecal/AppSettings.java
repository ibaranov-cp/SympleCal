package ca.mrrobot.symplecal;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by Ilia Baranov on 2015-04-21.
 *
 */
public class AppSettings extends PreferenceFragment
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.app_preference);
    }
}