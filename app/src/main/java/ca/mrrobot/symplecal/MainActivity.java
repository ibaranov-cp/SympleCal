package ca.mrrobot.symplecal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ProgressBar;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    public static Context baseContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        baseContext = getBaseContext();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.preferences:
                Intent intent = new Intent();
                intent.setClassName(this, "ca.mrrobot.symplecal.MyPreferenceActivity");
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            SharedPreferences Pref = PreferenceManager.getDefaultSharedPreferences(baseContext);
            //Remaining calories for the day = maint - cut - recorded foods
            float tot =  Float.valueOf(Pref.getString("MAINT_CAL","1")) - Float.valueOf(Pref.getString("CUT_CAL","1"));
            // TODO: feed in daily recorded value here instead of 700
            float rem = tot - 700;
            ProgressBar mProgress = (ProgressBar) rootView.findViewById(R.id.progressBar);
            mProgress.setProgress((int)(rem/tot*100.0));
            TextView t = (TextView) rootView.findViewById(R.id.rem_cal);
            t.setText(String.valueOf(rem));

            //Lifetime calorie deficit
            // TODO: feed in lifetime deficit
            int def = 100;
            t = (TextView) rootView.findViewById(R.id.cals_def);
            t.setText(String.valueOf(def));

            //Lifetime estimated weightloss
            t = (TextView) rootView.findViewById(R.id.weight_lost);
            t.setText(String.format("%.2f", def/3500.0));


            return rootView;
        }
    }
}
