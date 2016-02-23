package com.saihou.calgas;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

    MainActivityFragment fragmentA;
    MainActivityFragment fragmentB;
    EditText gallonsText;
    SharedPreferences sharedPrefs;
    String savedSettingsFileName = "CalGas";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentA = (MainActivityFragment) getFragmentManager().findFragmentById(R.id.fragmentA);
        fragmentB = (MainActivityFragment) getFragmentManager().findFragmentById(R.id.fragmentB);
        gallonsText = (EditText) findViewById(R.id.gallons_text);
        gallonsText.setText("15");
        gallonsText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 2) {
                    // hide virtual keyboard
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(gallonsText.getWindowToken(), 0);
                    gallonsText.clearFocus();
                }
                invokeFragmentsCalculate();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        loadPreviousValues();
    }

    private void loadPreviousValues() {
        sharedPrefs = getSharedPreferences(savedSettingsFileName, MODE_PRIVATE);
        gallonsText.setText(sharedPrefs.getString("gallons", "15"));
        String aBoardPrice = sharedPrefs.getString("aBoardPrice", "2.19");
        String aExtraFees = sharedPrefs.getString("aExtraFees", "0.35");
        String aCashback = sharedPrefs.getString("aCashback", "0");
        String bBoardPrice = sharedPrefs.getString("bBoardPrice", "2.32");
        String bExtraFees = sharedPrefs.getString("bExtraFees", "0.00");
        String bCashback = sharedPrefs.getString("bCashback", "5");
        fragmentA.setDefaultValues(aBoardPrice, aExtraFees, aCashback);
        fragmentB.setDefaultValues(bBoardPrice, bExtraFees, bCashback);
    }

    private void invokeFragmentsCalculate() {
        fragmentA.calculate();
        fragmentB.calculate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    public float getEstimatedGallons() {
        return ((gallonsText.getText().length() > 0) ? (Float.parseFloat(gallonsText.getText().toString())) : 0.0f);
    }

    public void calculate() {
        fragmentA.resetBackgroundColor();
        fragmentB.resetBackgroundColor();

        MainActivityFragment fragment = (fragmentA.getTotalCost() > fragmentB.getTotalCost()) ? fragmentB : fragmentA;
        fragment.setBackgroundColor();
    }

    @Override
    public void onStop() {
        super.onStop();
        saveCurrentValues();
    }

    private void saveCurrentValues() {
        sharedPrefs = getSharedPreferences(savedSettingsFileName, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("gallons", gallonsText.getText().toString().trim());

        String [] aValues = fragmentA.getCurrentValues();
        editor.putString("aBoardPrice", aValues[0]);
        editor.putString("aExtraFees", aValues[1]);
        editor.putString("aCashback", aValues[2]);

        String [] bValues = fragmentB.getCurrentValues();
        editor.putString("bBoardPrice", bValues[0]);
        editor.putString("bExtraFees", bValues[1]);
        editor.putString("bCashback", bValues[2]);
        editor.commit();
    }
}
