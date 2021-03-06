package com.ktm.thumb_first;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {


    public static boolean loginStatus = true;

    public static boolean getLoginStatus(){
        return loginStatus;
    }

    public static void setLoginStatus(boolean status){
        loginStatus = status;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        SharedPreferences welcome = getSharedPreferences("welcome", Context.MODE_PRIVATE);
        String userName = welcome.getString("USER_NAME", "");

        TextView welcomeNote = findViewById(R.id.welcome_note);
        welcomeNote.setText("Hello " + userName + ", Welcome Again");

    }


    public void login(View view) {

        SharedPreferences welcome = getSharedPreferences("welcome", Context.MODE_PRIVATE);
        String password = welcome.getString("PASSWORD", "");

        EditText etPassword = findViewById(R.id.login_password);
        String enteredPassword = etPassword.getText().toString();

        if (enteredPassword.equals(password)) {
            setLoginStatus(true);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Snackbar.make(view, "Authentication Failed. Try Again.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);

        builder.setMessage("Do you really want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LoginActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("No",null)
        // .setCancelable(false)
        ;

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
