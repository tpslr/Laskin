package com.topias.LaskinApp;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.FrameLayout;
import android.content.res.Configuration;
import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.expression.Symbol;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.parser.client.SyntaxError;
import java.util.Arrays;
import io.github.kexanie.library.MathView;
import com.chaquo.python.*;
import com.chaquo.python.android.AndroidPlatform;


public class FullscreenActivity extends AppCompatActivity {
    public Boolean outputNumeric = true;
    public Boolean showBrackets = false;
    public Boolean calculated = false;
    public ExprEvaluator mExprEvaluator = new ExprEvaluator();
    public String inputExpr = "";
    public String variableANS = "";
    public String variableA = "";
    public String variableB = "";
    public String variableC = "";
    public String variablex = "";
    public String variabley = "";
    public String variablez = "";

    public IExpr evaluateNumeric(IExpr ast, Boolean numeric) {
        EvalEngine evalEngine = mExprEvaluator.getEvalEngine();
        evalEngine.setNumericMode(numeric);
        ast = evalEngine.evalWithoutNumericReset(ast);
        evalEngine.setNumericMode(false);
        return ast;
    }
    public String parseInput(String input) {
        String expr = input;
        while (expr.contains("--")) expr = expr.replace("--", "+");
        while (expr.contains("++")) expr = expr.replace("++", "+");
        expr = expr.replace("÷", "/");
        expr = expr.replace("×", "*");
        expr = expr.replace("²", "^2");
        expr = expr.replace("∞", "Infinity");
        expr = expr.replace("π", "Pi");
        expr = expr.replace("frac", "/");
        expr = expr.replace("sin^{-1}", "asin");
        expr = expr.replace("cos^{-1}", "acos");
        expr = expr.replace("tan^{-1}", "atan");
        expr = expr.replace(" sin (", "sin(Pi/180*(");
        expr = expr.replace(" cos (", "cos(Pi/180*(");
        expr = expr.replace(" tan (", "tan(Pi/180*(");
        expr = expr.replace("{", "(");
        expr = expr.replace("}", ")");
        expr = expr.replace("√", "sqrt(");
        expr = expr.replace(" ", "");

        System.out.println(expr);
        return expr;
    }
    public String parseInputView(String input) {
        String expr = input;
        while (expr.contains(" -  - ")) expr = expr.replace(" -  - ", " + ");
        while (expr.contains(" +  + ")) expr = expr.replace(" +  + ", " + ");
        expr = expr.replace("÷", "\\div");
        expr = expr.replace("×", "\\times");
        expr = expr.replace("²", " ^ 2");
        expr = expr.replace("√", "sqrt");
        expr = expr.replace("∞", "\\infty");
        expr = expr.replace("π", "\\pi");
        expr = expr.replace(" sin(", "sin({");
        expr = expr.replace(" cos(", "cos({");
        expr = expr.replace(" tan(", "tan({");
        System.out.println(expr);
        return expr;
    }
    public String parseOutput(String output) {
        System.out.println(output);
        output = output.replace("/", "\\over");
        output = output.replace("(", "{(");
        output = output.replace(")", ")}");
        output = output.replace("Infinity", "\\infty");
        return output;
    }
    public void updateInput() {
        /*
        String insideBrackets = "";
        Boolean inBracket = false;
        Boolean addLastNumber = false;
        */
        Boolean normalBracket = false;
        Boolean addCurlyBracket = false;
        Boolean inBrackets = false;
        Boolean completeBrackets = false;
        Boolean completeFrac = false;
        Boolean completePower = false;
        Integer curlyBrackets = 0;
        Integer brackets = 0;
        String insideBrackets = "";
        String lastNumber = "";
        String output = "";
        calculated = false;

        MathView inputText = findViewById(R.id.inputText);
        inputExpr = inputExpr.replace("  ", " ");
        String[] a = parseInputView(inputExpr).split("\\s");
        if (true) {
            for (String i : a) {
                if (completePower) {
                    completePower = false;
                    output = output.substring(0, output.length() - 2);
                    output += "{" + i + "}";
                } else if (i.equals("frac")) {
                    output += "\\over";
                } else if (Arrays.asList("ans", "x", "y", "z", "A", "B", "C", "{", "}", "\\pi", "\\infty", "+", "-", "\\times", "\\div", "sin", "cos", "tan", "sin^{-1}", "cos^{-1}", "tan^{-1}").contains(i) || i.matches("-?\\d+(\\.\\d+)?")) {
                    output += i;
                } else if (i.equals("sqrt")) {
                    output += "\\sqrt{";
                } else if (i.equals("^")) {
                    output += i + " -";
                    completePower = true;
                } else if (i.equals("(")) {
                    output += "{\\left(";
                    brackets += 1;
                } else if (i.equals(")")) {
                    output += "\\right)}";
                    brackets -= 1;
                }
            }
            for (Integer i = brackets; brackets > 0; brackets--) {
                if (showBrackets) {
                    showBrackets = false;
                    output += "\\right)}";
                    inputExpr += ")";
                } else {
                    output += "\\right.}";
                }
            }
            for (char i : output.toCharArray()) {
                String b = String.valueOf(i);
                if (b.equals("{")) {
                    curlyBrackets += 1;
                } else if (b.equals("}")) {
                    curlyBrackets -= 1;
                }
            }
            for (Integer i = curlyBrackets; curlyBrackets > 0; curlyBrackets--) {
                if (showBrackets) {
                    output += "}";
                    inputExpr += "}";
                } else {
                    output += "}";
                }
            }
            if (showBrackets) {
                showBrackets = false;
            }
            System.out.println(output);

        }

        else {
            for (String i : a) {
                System.out.println(i);
                if (i.isEmpty()) {
                }
                if (completeBrackets) {
                    completeBrackets = false;
                    if (addCurlyBracket) {
                        output += insideBrackets + "}";
                        addCurlyBracket = false;
                    } else {
                        output += "{(" + insideBrackets + ")}";
                    }
                    insideBrackets = "";
                }
                if (completeFrac) {
                    completeFrac = false;
                    if (i.equals("(")) {
                        inBrackets = true;
                        brackets += 1;
                        addCurlyBracket = true;
                    } else if (i.matches("-?\\d+(\\.\\d+)?")) {
                        output += i + "}";
                    }
                } else if (completePower) {
                    completePower = false;
                    if (i.equals("(")) {
                        inBrackets = true;
                        brackets += 1;
                        addCurlyBracket = true;
                    } else if (i.matches("-?\\d+(\\.\\d+)?")) {
                        output = output.substring(0, output.length() - 1);
                        output += i;
                    }
                } else if (i.equals("^")) {
                    output += "^{";
                    completePower = true;
                } else if (i.equals("frac")) {
                    if (completeBrackets) {
                        completeBrackets = false;
                        output += "{" + insideBrackets + "\\over";
                    } else if (inBrackets) {
                        inBrackets = false;
                        normalBracket = true;
                        output += "(" + insideBrackets;
                        output = output.substring(0, output.length() - lastNumber.length());
                        output += "{" + lastNumber + "\\over";
                    } else {
                        output = output.substring(0, output.length() - lastNumber.length());
                        output += "{" + lastNumber + "\\over";
                    }
                    completeFrac = true;
                } else if (Arrays.asList("\\infty", "+", "-", "\\times", "\\div", "sin", "cos", "tan", "sin⁻¹", "cos⁻¹", "tan⁻¹").contains(i) || i.matches("-?\\d+(\\.\\d+)?")) {
                    if (i.matches("-?\\d+(\\.\\d+)?")) {
                        lastNumber = i;
                    }
                    if (completeBrackets) {
                        completeBrackets = false;
                        if (addCurlyBracket) {
                            output += insideBrackets + "}";
                            addCurlyBracket = false;
                        } else {
                            output += "{(" + insideBrackets + ")}";
                        }
                        insideBrackets = "";
                    }
                    if (inBrackets) {
                        insideBrackets += i;
                    } else {
                        output += i;
                    }
                } else if (i.equals("(")) {
                    inBrackets = true;
                    brackets += 1;
                    if (brackets > 1) {
                        insideBrackets += i;
                    }
                } else if (i.equals(")")) {
                    brackets -= 1;
                    if (normalBracket) {
                        normalBracket = false;
                        output += i;
                    } else if (brackets == 0) {
                        completeBrackets = true;
                        inBrackets = false;
                    } else if (brackets >= 1) {
                        insideBrackets += i;
                    }
                }
            }
            if (completeBrackets) {
                if (addCurlyBracket) {
                    output += insideBrackets + "}";
                } else {
                    output += "{(" + insideBrackets + ")}";
                }
                insideBrackets = "";
            }
            if (inBrackets) {
                if (addCurlyBracket) {
                    output += "(" + insideBrackets + "}";
                } else {
                    output += "(" + insideBrackets;
                }
            }
            if (completeFrac) {
                output += "}";
            }
            if (completePower) {
                output += "\\small ()}";
            }
            System.out.println(output);
        }
        inputText.setText("\\(\\color{white}{" + output + "}\\)");
        /*
        String[] a = parseInput(inputExpr).split("\\s");
        for (String i : a) {
            System.out.println(i);
            if (i.matches("-?\\d+(\\.\\d+)?")) {
                if (completeFrac) {
                    if (brackets == 0 && bracket) {
                        output = output.substring(0, output.length() - 1);
                        output += insideBrackets + "}";
                        completeFrac = false;
                        bracket = false;
                    }
                    else if (!bracket) {
                        output = output.substring(0, output.length() - 1);
                        output += i + "}";
                        completeFrac = false;
                    }
                    else {
                        insideBrackets += i;
                    }
                }
                else if (output.endsWith("^")){
                    output += i;
                }
                else {
                    lastNumber = i;
                    addLastNumber = true;
                }
            }
            else {
                System.out.println("a");
                if (i.equals("(")) {
                    brackets += 1;
                    bracket = true;
                }
                else if (i.equals(")")) {
                    brackets -= 1;
                }
                if (brackets == 0 && bracket) {
                    output = output.substring(0, output.length() - 1);
                    output += insideBrackets + "}";
                    completeFrac = false;
                    bracket = false;
                }
                else if (bracket) {
                    insideBrackets += i;
                }
                else if (i.equals("frac")) {
                    output += "\\frac{" + lastNumber + "}{}";
                    completeFrac = true;
                    addLastNumber = false;
                }
                else if (Arrays.asList("+","-","*","/","^","sin(","cos(","tan(","asin(","acos(","atan(","(",")").contains(i)) {
                    if (addLastNumber) {
                        output += lastNumber + i;
                        addLastNumber = false;
                    }
                    else {
                        output += i;
                    }
                }
            }
        }
        if (addLastNumber) {
            output += lastNumber;
        }
        if (output.isEmpty()) {
            output = lastNumber;
        }
        inputText.setText("\\(" + output + "\\)");
        System.out.println(output);
    */
    }
    public String calculate(String input) {
        input = input.replace("A", variableA);
        input = input.replace("B", variableB);
        input = input.replace("C", variableC);
        input = input.replace("x", variablex);
        input = input.replace("y", variabley);
        input = input.replace("z", variablez);
        input = input.replace("ans", variableANS);
        Python py = Python.getInstance();
        PyObject solver = py.getModule("solver");
        return solver.callAttr("evaluate",input).toString();
        //return evaluateNumeric(mExprEvaluator.parse(input), outputNumeric).toString();
    }
    public void pressFunc(View v) {
        ToggleButton Tbutton = findViewById(R.id.func);
        Button bBracket = findViewById(R.id.buttonBracket);
        Button bBracket2 = findViewById(R.id.buttonBracket2);
        Button bSin = findViewById(R.id.buttonSin);
        Button bCos = findViewById(R.id.buttonCos);
        Button bTan = findViewById(R.id.buttonTan);
        Button bVA = findViewById(R.id.buttonVA);
        Button bVB = findViewById(R.id.buttonVB);
        Button bVC = findViewById(R.id.buttonVC);

        if (Tbutton.isChecked()) {
            bBracket.setText("{");
            bBracket2.setText("}");
            bBracket.setTag(" { ");
            bBracket2.setTag(" } ");
            bSin.setText("sin⁻¹");
            bSin.setTag(" sin^{-1} ( ");
            bCos.setText("cos⁻¹");
            bCos.setTag(" cos^{-1} ( ");
            bTan.setText("tan⁻¹");
            bTan.setTag(" tan^{-1} ( ");
            bVA.setText("x");
            bVA.setTag(" x ");
            bVB.setText("y");
            bVB.setTag(" y ");
            bVC.setText("z");
            bVC.setTag(" z ");
        }
        else {
            bBracket.setText("(");
            bBracket2.setText(")");
            bBracket.setTag(" ( ");
            bBracket2.setTag(" ) ");
            bSin.setText("sin");
            bSin.setTag(" sin ( ");
            bCos.setText("cos");
            bCos.setTag(" cos ( ");
            bTan.setText("tan");
            bTan.setTag(" tan ( ");
            bVA.setText("A");
            bVA.setTag(" A ");
            bVB.setText("B");
            bVB.setTag(" B ");
            bVC.setText("C");
            bVC.setTag(" C ");
        }
    }
    public void pressEquals(View v) {
        showBrackets = true;
        updateInput();
        MathView outputText = findViewById(R.id.outputText);
        String output;
        try {
            output = parseOutput(calculate(parseInput(inputExpr)));
            calculated = true;
            variableANS = "(" + parseInput(inputExpr) + ")";
        }
        catch (SyntaxError e){
            output = "SyntaxError";
            calculated = false;
        }
        outputText.setText("\\(\n \\color{white}{" + output + "}\\)");
    }

    public void pressCE(View v) {
        MathView outputText = findViewById(R.id.outputText);
        inputExpr = "";
        outputText.setText("");
        updateInput();
    }

    public void pressC(View v) {
        inputExpr = "";
        updateInput();
    }

    public void pressChange(View v) {
        outputNumeric = !outputNumeric;
        if (calculated) {
            MathView outputText = findViewById(R.id.outputText);
            String output = calculate(parseInput(inputExpr));
            outputText.setText("\\(\\color{white}{" + output + "}\\)");
        }
    }

    public void pressBackspace(View v) {
        if (inputExpr.isEmpty()) {}
        else if       (inputExpr.endsWith(" sin ( ")
                ||inputExpr.endsWith(" cos ( ")
                ||inputExpr.endsWith(" tan ( ")) {
            inputExpr = inputExpr.substring(0, inputExpr.length() - 7);
        }
        else if  (inputExpr.endsWith(" + ")
                ||inputExpr.endsWith(" - ")
                ||inputExpr.endsWith(" × ")
                ||inputExpr.endsWith(" ÷ ")
                ||inputExpr.endsWith(" A ")
                ||inputExpr.endsWith(" B ")
                ||inputExpr.endsWith(" C ")
                ||inputExpr.endsWith(" x ")
                ||inputExpr.endsWith(" y ")
                ||inputExpr.endsWith(" z ")
                ||inputExpr.endsWith(" ^ ")
                ||inputExpr.endsWith(" ( ")
                ||inputExpr.endsWith(" ) ")
                ||inputExpr.endsWith(" { ")
                ||inputExpr.endsWith(" } ")) {
            inputExpr = inputExpr.substring(0, inputExpr.length() - 3);
        }
        else if  (inputExpr.endsWith(" sin^{-1} ( ")
                ||inputExpr.endsWith(" cos^{-1} ( ")
                ||inputExpr.endsWith(" tan^{-1} ( ")) {
            inputExpr = inputExpr.substring(0, inputExpr.length() - 10);
        }
        else if  (inputExpr.endsWith(" frac ")) {
            inputExpr = inputExpr.substring(0, inputExpr.length() - 6);
        }
        else {
            inputExpr = inputExpr.substring(0, inputExpr.length() - 1);
        }
        updateInput();
    }

    public void addSymbol(View v) {
        String value = v.getTag().toString();
        ToggleButton Tbutton = findViewById(R.id.buttonSave);
        if (Tbutton.isChecked()) {
            try {
                if      (value.equals(" A ")) {
                    calculate(inputExpr);
                    variableA = "(" + inputExpr + ")";
                }
                else if (value.equals(" B ")) {
                    calculate(inputExpr);
                    variableB = "(" + inputExpr + ")";
                }
                else if (value.equals(" C ")) {
                    calculate(inputExpr);
                    variableC = "(" + inputExpr + ")";
                }
                else if (value.equals(" x ")) {
                    calculate(inputExpr);
                    variablex = "(" + inputExpr + ")";
                }
                else if (value.equals(" y ")) {
                    calculate(inputExpr);
                    variabley = "(" + inputExpr + ")";
                }
                else if (value.equals(" z ")) {
                    calculate(inputExpr);
                    variablez = "(" + inputExpr + ")";
                }
            }
            catch (Exception o) {

            }
        }
        else {
            if (inputExpr.isEmpty() && Arrays.asList(" + "," - "," × "," ÷ ", " ^ ").contains(value)) {
                inputExpr = inputExpr + " ans " + value;
            }
            else {
                inputExpr = inputExpr + value;
            }
            updateInput();
        }
    }

    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private boolean mVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        mVisible = true;
        mContentView = findViewById(R.id.button3);
        hide();
        MathView formula = findViewById(R.id.inputText);
        formula.config("MathJax.Hub.Config({ TeX: { extensions: [\"color.js\"] }});");

        setContentView(R.layout.activity_fullscreen);

        mVisible = true;
        mContentView = findViewById(R.id.mainLayout);


        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
                View decorView = getWindow().getDecorView();
                int uiOptions = View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN;
                decorView.setSystemUiVisibility(uiOptions);
            }
        });
        findViewById(R.id.button0).setOnTouchListener(mDelayHideTouchListener);
    }
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_fullscreen);
        updateInput();
    }
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
        }
    };
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}



