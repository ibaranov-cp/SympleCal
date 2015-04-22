package ca.mrrobot.symplecal;

import android.preference.PreferenceActivity;

import java.util.List;

/**
 * Created by Ilia Baranov on 2015-04-21.
 */
public class MyPreferenceActivity extends PreferenceActivity
{
    @Override
    public void onBuildHeaders(List<Header> target)
    {
        loadHeadersFromResource(R.xml.headers_preference, target);
    }

    @Override
    protected boolean isValidFragment(String fragmentName)
    {
        return MyPreferenceFragment.class.getName().equals(fragmentName);
    }
}
