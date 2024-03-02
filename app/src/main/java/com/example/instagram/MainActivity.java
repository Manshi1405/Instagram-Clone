package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnKeyListener {

    TextView textView;
    Boolean signUpModeActive = true;
    EditText usernameEditText;
    EditText passwordEditText;

    public void showUserList() {
        Intent intent = new Intent(getApplicationContext(),user_list.class);
        startActivity(intent);
    }



    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {

        if(i==KeyEvent.KEYCODE_ENTER && keyEvent.getAction()== KeyEvent.ACTION_DOWN) {
            clickToSignup(view);
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.textView2) {
            Button button = findViewById(R.id.button);
            if (signUpModeActive) {
                signUpModeActive = false;
                button.setText("Log In");
                textView.setText("or, Sign Up");
            } else {
                signUpModeActive = true;
                button.setText("Sign Up");
                textView.setText("or, Log In");
            }
        }
        else if(view.getId() == R.id.logoImageView || view.getId() == R.id.backgroundLayout) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
    }

    public void clickToSignup(View view) {

        if (usernameEditText.getText().toString().equals("") && passwordEditText.getText().toString().equals(""))
            Toast.makeText(this, "A Username And Password Is Required", Toast.LENGTH_SHORT).show();
        else {
            if (signUpModeActive) {
                ParseUser signupUser = new ParseUser();
                signupUser.setUsername(usernameEditText.getText().toString());
                signupUser.setPassword(passwordEditText.getText().toString());
                signupUser.signUpInBackground(e -> {
                    if (e == null) {
                        Toast.makeText(MainActivity.this, "Log In Successful", Toast.LENGTH_SHORT).show();
                        showUserList();
                    }
                    else
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            } else {
                //LOGIN
                ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (user != null) {
                            Toast.makeText(MainActivity.this, "Log In Successful", Toast.LENGTH_SHORT).show();
                            showUserList();
                        }
                        else
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView2);
        textView.setOnClickListener(this);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        ImageView logoImageView = findViewById(R.id.logoImageView);
        ConstraintLayout backgroundLayout = findViewById(R.id.backgroundLayout);
        passwordEditText.setOnClickListener(this);
        backgroundLayout.setOnClickListener(this);
        passwordEditText.setOnClickListener(this);

        if(ParseUser.getCurrentUser()!=null) {
            showUserList();
        }
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

}