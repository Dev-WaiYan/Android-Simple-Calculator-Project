package com.devwaiyan.simplecalculator;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Available theme list
    enum ThemeList {
        STANDARD_THEME(0),
        SIMPLE_THEME(1);

        private int value;
        ThemeList(int value){
            this.value = value;
        }
    }

    // Input/Output Field
    private EditText ipopField;
    // Number Buttons
    private Button btnNine, btnEight, btnSeven, btnSix, btnFive, btnFour, btnThree, btnTwo, btnOne, btnZero;
    // Operator and Control Buttons
    private Button btnBackspace, btnPoint, btnPlus, btnMinus, btnMultiply, btnDivide, btnEqual, btnAC, btnChooseTheme;

    private BasicCalculator basicCalculator; // Calculator Object Reference
    private List<Character> list; // To use List to get flexibility while changing text in TextField.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Start theme control
        Intent intent = getIntent();
        if(intent.hasExtra("theme")) {
            if(intent.getIntExtra("theme", 1) == ThemeList.SIMPLE_THEME.value) {
                setContentView(R.layout.activity_main); //Simple theme
            } else if(intent.getIntExtra("theme", 0) == ThemeList.STANDARD_THEME.value) {
                setContentView(R.layout.activity_standard_theme); //Standard theme
            }
        } else {
            setContentView(R.layout.activity_main); // Default theme
        }
        // End of theme control

        basicCalculator = new BasicCalculator(); // Calculator Object
        bindViewWithVariable(); // Binding View and Variable

        // Hide keyboard when at least android version is Android-5.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ipopField.setShowSoftInputOnFocus(false);
        }

        list = new LinkedList<>(); // Use List to get flexibility while changing text in TextField.
        ipopField.requestFocus(); // Initially set focus in textField.

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED); // Rotate off (Default)
    }

    @Override
    protected void onResume() {
        super.onResume();

        btnChooseTheme.setOnClickListener(v -> showAvailableTheme()); // Show available themes

        btnNine.setOnClickListener(v -> setTextAtIpOpField("9"));
        btnEight.setOnClickListener(v -> setTextAtIpOpField("8"));
        btnSeven.setOnClickListener(v -> setTextAtIpOpField("7"));
        btnSix.setOnClickListener(v -> setTextAtIpOpField("6"));
        btnFive.setOnClickListener(v -> setTextAtIpOpField("5"));
        btnFour.setOnClickListener(v -> setTextAtIpOpField("4"));
        btnThree.setOnClickListener(v -> setTextAtIpOpField("3"));
        btnTwo.setOnClickListener(v -> setTextAtIpOpField("2"));
        btnOne.setOnClickListener(v -> setTextAtIpOpField("1"));
        btnZero.setOnClickListener(v -> setTextAtIpOpField("0"));
        btnPoint.setOnClickListener(v -> setTextAtIpOpField("."));

        btnBackspace.setOnClickListener(v -> setTextAtIpOpField("_")); // Backspace

        btnEqual.setOnClickListener(v -> {
            Editable values = ipopField.getText();
            try {
                ipopField.setText(basicCalculator.calculate(values.toString())); // When press equal button, Calculation starts.
            } catch (Exception e) {
                ipopField.setText(values);
                Toast.makeText(getApplicationContext(),"Check Input Please !", Toast.LENGTH_SHORT).show();
            }
            ipopField.setSelection(ipopField.getText().length());
        });

        btnAC.setOnClickListener(v -> ipopField.setText("")); // All Clear
        btnPlus.setOnClickListener(v -> setTextAtIpOpField("+"));
        btnMinus.setOnClickListener(v -> setTextAtIpOpField("-"));
        btnMultiply.setOnClickListener(v -> setTextAtIpOpField("\u00D7"));
        btnDivide.setOnClickListener(v -> setTextAtIpOpField("\u00F7"));
    }


    private void bindViewWithVariable() {
        btnChooseTheme = findViewById(R.id.btn_choosetheme);

        ipopField = findViewById(R.id.ip_op_field);
        btnNine = findViewById(R.id.btn_nine);
        btnEight = findViewById(R.id.btn_eight);
        btnSeven = findViewById(R.id.btn_seven);
        btnSix = findViewById(R.id.btn_six);
        btnFive = findViewById(R.id.btn_five);
        btnFour = findViewById(R.id.btn_four);
        btnThree = findViewById(R.id.btn_three);
        btnTwo = findViewById(R.id.btn_two);
        btnOne = findViewById(R.id.btn_one);
        btnZero = findViewById(R.id.btn_zero);
        btnPoint = findViewById(R.id.btn_point);
        btnBackspace = findViewById(R.id.btn_backspace);

        btnPlus = findViewById(R.id.btn_plus);
        btnMinus = findViewById(R.id.btn_minus);
        btnMultiply = findViewById(R.id.btn_multiply);
        btnDivide = findViewById(R.id.btn_divide);
        btnEqual = findViewById(R.id.btn_equal);
        btnAC = findViewById(R.id.btn_AC);
    }


    // Method to show available themes
    private void showAvailableTheme() {
        setContentView(R.layout.available_themes);
        Button btnChangeStandardTheme = findViewById(R.id.btn_changeStandardTheme);
        Button btnChangeSimpleTheme = findViewById(R.id.btn_changeSimpleTheme);

        // Change theme
        btnChangeStandardTheme.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("theme", ThemeList.STANDARD_THEME.value);
            startActivity(intent);
        }); // Change to standard theme

        btnChangeSimpleTheme.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("theme", ThemeList.SIMPLE_THEME.value);
            startActivity(intent);
        } ); // Change to simple theme
    }


    // Method for set text in input_output_field
    private void setTextAtIpOpField(String msg) {
        list.clear(); // set empty list.
        int cursorEnd = Selection.getSelectionEnd(ipopField.getText()); // Trace cursor end
        char[] oldText = ipopField.getText().toString().toCharArray(); // firstly, get text as string and then change as character array.

        // Add a character by character from Character array to list
        for (char ch : oldText) {
            list.add(ch);
        }

        // Add new message to list
        // When user presses a operator, it will set a blank before and after this operator.
        if (msg.charAt(0) == '+' || msg.charAt(0) == '-' || msg.charAt(0) == '\u00D7' || msg.charAt(0) == '\u00F7') {
            list.add(cursorEnd, ' ');
            list.add(++cursorEnd, msg.charAt(0));
            list.add(++cursorEnd, ' ');
        }
        // When user presses backspace, it will remove a character from text field.
        else if (msg.charAt(0) == '_') {
            // To use backspace, requirements are: ['List must not be empty', 'A character must exist before the cursor.']
            if(!list.isEmpty() && (cursorEnd != 0)) {
                list.remove(--cursorEnd);
                --cursorEnd;
            }
        } else {
            list.add(cursorEnd, msg.charAt(0));
        }

        ipopField.setText(""); // Before setting new text, First clear all old text.

        if(!list.isEmpty()) {
            for (Character ch : list) {
                ipopField.setText(ipopField.getText().append(ch)); // Now, set new Result-Text as a character by character.
            }

            ipopField.setSelection(cursorEnd + 1); // Move cursor to next
        }
    }

}