package com.saihou.calgas;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

    MainActivityFragment fragmentA;
    MainActivityFragment fragmentB;
    EditText gallonsText;

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
                invokeFragmentsCalculate();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public float getEstimatedGallons() {
        return Float.parseFloat(gallonsText.getText().toString());
    }

    public void calculate() {
        fragmentA.resetBackgroundColor();
        fragmentB.resetBackgroundColor();

        MainActivityFragment fragment = (fragmentA.getTotalCost() > fragmentB.getTotalCost()) ? fragmentB : fragmentA;
        fragment.setBackgroundColor();
    }
}
