package com.example.studentappmvvm.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studentappmvvm.DataRepository;
import com.example.studentappmvvm.R;
import com.google.android.material.textfield.TextInputLayout;

import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private final DataRepository mRepository = DataRepository.getInstance();

    SharedPreferences sharedPreferences;

    EditText _emailText;
    EditText _passwordText;
    Button _loginButton;
    TextView _signupLink;
    TextInputLayout _passwordLayout;
    TextInputLayout _emailLayout;
    ProgressBar _progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //setTheme(R.style.Theme_MyApp);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("StudentApp", MODE_PRIVATE);

        _emailText = findViewById(R.id.input_email);
        _passwordText = findViewById(R.id.input_password);
        _loginButton = findViewById(R.id.btn_login);
        _signupLink = findViewById(R.id.link_signup);
        _passwordLayout = findViewById(R.id.passLayout);
        _emailLayout = findViewById(R.id.emailLayout);
        _progressBar = findViewById(R.id.progressBar);

        _loginButton.setOnClickListener(v -> {
            try {
                login();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        });

        _signupLink.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
            startActivityForResult(intent, REQUEST_SIGNUP);
            finish();
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        });
    }

    public void login() throws NoSuchAlgorithmException {
        Log.d(TAG, "Login");

//        if (!validate()) {
//            onLoginFailed();
//            return;
//        }

        _loginButton.setEnabled(false);

        _progressBar.setVisibility(View.VISIBLE);

        String login = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        mRepository.authUser(login, AppActivity.sha1(password), userEntity -> {
            if (userEntity != null) {
                _progressBar.setVisibility(View.INVISIBLE);

                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putInt("id", userEntity.getID());
                editor.putString("login", userEntity.getLogin());
                editor.putString("firstname", userEntity.getFirstName());
                editor.putString("lastname", userEntity.getLastName());
                editor.putString("nickname", userEntity.getNickName());
                editor.apply();

                onLoginSuccess();
                return 1;
            } else {
                _progressBar.setVisibility(View.INVISIBLE);
                onLoginFailed();
                return 0;
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        Intent intent = new Intent(this, AppActivity.class);
        startActivity(intent);
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailLayout.setError("Enter a valid email address");
            valid = false;
        } else {
            _emailLayout.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            String errorStr = "Between 4 and 10 alphanumeric characters";
            _passwordLayout.setError(errorStr);
            valid = false;
        } else {
            _passwordLayout.setError(null);
        }

        return valid;
    }
}
