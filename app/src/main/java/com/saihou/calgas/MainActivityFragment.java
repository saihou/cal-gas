package com.saihou.calgas;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements Spinner.OnItemSelectedListener {

    TextView titleText;
    RelativeLayout parentView;
    String fragmentName;

    EditText boardPriceText;
    EditText extraFeesText;

    Spinner cashbackSpinner;

    TextView totalCostText;
    float totalCost;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        fragmentName = (getId() == R.id.fragmentA) ? "A" : "B";
        parentView = (RelativeLayout) view.findViewById(R.id.relativeLayout);

        titleText = (TextView) view.findViewById(R.id.gas_station_label);
        titleText.setText((fragmentName.equals("A")) ? R.string.station_a : R.string.station_b);

        boardPriceText = (EditText) view.findViewById(R.id.board_price_text);
        boardPriceText.setText(fragmentName.equals("A") ? "2.19" : "2.32");
        boardPriceText.addTextChangedListener(new CustomTextWatcher(boardPriceText));

        extraFeesText = (EditText) view.findViewById(R.id.extra_fees_text);
        extraFeesText.setText(fragmentName.equals("A") ? "0.35" : "0.00");
        extraFeesText.addTextChangedListener(new CustomTextWatcher(extraFeesText));

        cashbackSpinner = (Spinner) view.findViewById(R.id.cashback_spinner);
        cashbackSpinner.setSelection(fragmentName.equals("A") ? 0 : 5);
        cashbackSpinner.setOnItemSelectedListener(this);

        totalCostText = (TextView) view.findViewById(R.id.total_cost);

        return view;
    }

    public void calculate() {
        MainActivity activity = (MainActivity) getActivity();

        String rawPrice = boardPriceText.getText().toString().trim();
        String rawFees = extraFeesText.getText().toString().trim();

        float price = convertToFloat(rawPrice);
        float fees = convertToFloat(rawFees);
        float cashback_percent = Float.parseFloat(cashbackSpinner.getSelectedItem().toString().substring(0, 1));
        float gallons = activity.getEstimatedGallons();

        float cost = price * gallons + fees;
        float cashback = cashback_percent * cost / 100.0f;
        totalCost = (cost - cashback);
        totalCostText.setText(String.format("$%.2f", totalCost));

        activity.calculate();
    }

    private float convertToFloat(String rawPrice) {
        try {
            float price = Float.parseFloat(rawPrice);

            return price;
        } catch (NumberFormatException e) {
            return 0.00f;
        }
    }

    public float getTotalCost() {
        return totalCost;
    }

    public void setBackgroundColor() {
        parentView.setBackgroundResource(R.color.pastelGreen);
    }

    public void resetBackgroundColor() {
        parentView.setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        calculate();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        calculate();
    }

    public class CustomTextWatcher implements TextWatcher {

        EditText editText;
        String before;

        public CustomTextWatcher(EditText editText) {
            this.editText = editText;
            before = "";
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            before = s.toString();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
//            if (s.length() == 4) {
//                // hide virtual keyboard
//                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
//                editText.clearFocus();
//            }
            int index = s.toString().indexOf(".");
            if (index != -1) {
                String afterDecimal = s.toString().substring(index+1);
                if (afterDecimal.length() > 2) {
                    editText.setText(before);
                }
            }

            calculate();
        }
    }
}
