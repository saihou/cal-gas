package com.saihou.calgas;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements NumberPicker.OnValueChangeListener, Spinner.OnItemSelectedListener{

    NumberPicker aPriceDollars;
    NumberPicker aPriceCents1;
    NumberPicker aPriceCents2;

    NumberPicker aFeesDollars;
    NumberPicker aFeesCents1;
    NumberPicker aFeesCents2;

    Spinner aCashbackSpinner;

    TextView aTotalCost;

    NumberPicker bPriceDollars;
    NumberPicker bPriceCents1;
    NumberPicker bPriceCents2;

    NumberPicker bFeesDollars;
    NumberPicker bFeesCents1;
    NumberPicker bFeesCents2;

    Spinner bCashbackSpinner;

    TextView bTotalCost;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        aPriceDollars = (NumberPicker) view.findViewById(R.id.board_price_a_dollars);
        aPriceCents1 = (NumberPicker) view.findViewById(R.id.board_price_a_cents_1);
        aPriceCents2 = (NumberPicker) view.findViewById(R.id.board_price_a_cents_2);

        //set range
        aPriceDollars.setMaxValue(5);
        aPriceDollars.setMinValue(1);
        aPriceCents1.setMaxValue(9);
        aPriceCents1.setMinValue(0);
        aPriceCents2.setMaxValue(9);
        aPriceCents2.setMinValue(0);

        //default
        aPriceDollars.setValue(2);
        aPriceCents1.setValue(1);
        aPriceCents2.setValue(9);

        //listeners
        aPriceDollars.setOnValueChangedListener(this);
        aPriceCents1.setOnValueChangedListener(this);
        aPriceCents2.setOnValueChangedListener(this);

        aFeesDollars = (NumberPicker) view.findViewById(R.id.extra_fees_a_dollars);
        aFeesCents1 = (NumberPicker) view.findViewById(R.id.extra_fees_a_cents_1);
        aFeesCents2 = (NumberPicker) view.findViewById(R.id.extra_fees_a_cents_2);

        //set range
        aFeesDollars.setMaxValue(2);
        aFeesDollars.setMinValue(0);
        aFeesCents1.setMaxValue(9);
        aFeesCents1.setMinValue(0);
        aFeesCents2.setMaxValue(9);
        aFeesCents2.setMinValue(0);

        //default
        aFeesDollars.setValue(0);
        aFeesCents1.setValue(3);
        aFeesCents2.setValue(5);

        //listeners
        aFeesDollars.setOnValueChangedListener(this);
        aFeesCents1.setOnValueChangedListener(this);
        aFeesCents2.setOnValueChangedListener(this);

        aCashbackSpinner = (Spinner) view.findViewById(R.id.cashback_spinner);
        aCashbackSpinner.setOnItemSelectedListener(this);

        aTotalCost = (TextView) view.findViewById(R.id.total_cost);

        return view;
    }

    public void calculate() {
        String rawPrice = String.valueOf(aPriceDollars.getValue()) + "." + String.valueOf(aPriceCents1.getValue()) + String.valueOf(aPriceCents2.getValue());
        String rawFees = String.valueOf(aFeesDollars.getValue()) + "." + String.valueOf(aFeesCents1.getValue()) + String.valueOf(aFeesCents2.getValue());

        float price = Float.parseFloat(rawPrice);
        float fees = Float.parseFloat(rawFees);
        float cashback_percent = Float.parseFloat(aCashbackSpinner.getSelectedItem().toString().substring(0,1));
        float gallons = 10;

        float cost = price * gallons + fees;
        float cashback = cashback_percent * cost / 100.0f;
        float totalCost = (cost - cashback);
        aTotalCost.setText(String.format("%.2f", totalCost));
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        calculate();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        calculate();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        calculate();
    }
}
