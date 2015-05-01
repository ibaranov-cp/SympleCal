package ca.mrrobot.symplecal;

import android.content.Context;
import android.widget.TextView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;


public class MainActivity extends ActionBarActivity {

    public static Context baseContext;
    public static String Filename = "Symple_Cal.txt";
    public static Float Date = (float)1;
    public static Float Cals = (float)1;
    public static Float Deficit = (float)1;
    public static Float Maint = (float)1;
    public static Float Cut = (float)1;

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

    // Writing to calorie tracker file
    public static void write (String filename,Context c){
        try {
            FileOutputStream fos =  c.openFileOutput(filename, Context.MODE_PRIVATE);
            String st = String.valueOf(Date) +"\n" + String.valueOf(Cals) +"\n" + String.valueOf(Deficit) +"\n";
            try {
                fos.write(st.getBytes());
                fos.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //Reading from calorie tracker file
        /*
    File Format:
    date (resets at midnight)
    calories for today
    lifetime deficit
     */
    public static String read (String filename,Context c){

        StringBuffer buffer = new StringBuffer();
        String Read;

        try {
            FileInputStream fis = c.openFileInput(filename);

            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

            try {
                if (fis != null) {
                    Date = Float.valueOf(reader.readLine());
                    Cals = Float.valueOf(reader.readLine());
                    Deficit = Float.valueOf(reader.readLine());
                    //while ((Read = reader.readLine()) != null) {
                     //   buffer.append(Read + "\n");
                    //}
                }
                fis.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return buffer.toString();
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
            Maint = Float.valueOf(Pref.getString("MAINT_CAL", "1"));
            Cut = Float.valueOf(Pref.getString("CUT_CAL", "1"));
            float tot = Maint - Cut;

            Time today = new Time(Time.getCurrentTimezone());
            today.setToNow();

            read(Filename,baseContext);

            // if it is a new day, update file
            if (today.monthDay != Date){
                Date = (float)today.monthDay;
                Deficit = Deficit + (Maint - Cals); //store deficit from day before
                Cals = (float)0; //reset
                write(Filename,baseContext);
            }


            float rem = tot - Cals;
            ProgressBar mProgress = (ProgressBar) rootView.findViewById(R.id.progressBar);
            mProgress.setProgress((int)(rem/tot*100.0));
            TextView t = (TextView) rootView.findViewById(R.id.rem_cal);
            t.setText(String.valueOf(rem));

            //Lifetime calorie deficit
            t = (TextView) rootView.findViewById(R.id.cals_def);
            t.setText(String.valueOf(Deficit));

            //Lifetime estimated weightloss
            t = (TextView) rootView.findViewById(R.id.weight_lost);
            t.setText(String.format("%.2f", Deficit / 3500.0));


            return rootView;
        }
    }
}
