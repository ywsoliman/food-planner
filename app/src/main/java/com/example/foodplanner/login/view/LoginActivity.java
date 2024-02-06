package com.example.foodplanner.login.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodplanner.IAuthenticate;
import com.example.foodplanner.R;
import com.example.foodplanner.home.view.HomeActivity;
import com.example.foodplanner.login.presenter.LoginPresenter;
import com.example.foodplanner.register.view.RegisterActivity;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity implements IAuthenticate {

    private static final String TAG = "LoginActivity";
    private TextInputLayout emailInputLayout;
    private TextInputEditText emailInputEditText;
    private TextInputEditText passInputEditText;
    private TextView navigateToSignup;
    private Button loginButton;
    private Button guestButton;
    private LoginPresenter loginPresenter;
    private MaterialCardView googleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUI();
        loginPresenter = new LoginPresenter(this);

        loginButton.setOnClickListener(v -> handleLoginButton());
        navigateToSignup.setOnClickListener(v -> handleNavigateToSignup());
        guestButton.setOnClickListener(v -> handleGuestButton());
        googleButton.setOnClickListener(v -> handleGoogleButton());
        addEmailTextInputWatcher();
    }

    private void addEmailTextInputWatcher() {
        emailInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable email) {
                Log.i(TAG, "afterTextChanged: " + email.toString());
                loginPresenter.validateEmail(email.toString());
            }
        });
    }

    private void handleGoogleButton() {
        Toast.makeText(this, "Google clicked!", Toast.LENGTH_SHORT).show();
    }

    private void handleGuestButton() {
        // TODO
    }

    private void handleLoginButton() {
        String email = emailInputEditText.getText().toString().trim();
        String password = passInputEditText.getText().toString().trim();
        loginPresenter.tryLogin(email, password);
    }

    private void handleNavigateToSignup() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void initUI() {
        emailInputLayout = findViewById(R.id.emailTextInputLayout);
        emailInputEditText = findViewById(R.id.emailTextInputEdit);
        passInputEditText = findViewById(R.id.passwordTextInputEdit);
        navigateToSignup = findViewById(R.id.signupButton);
        loginButton = findViewById(R.id.navigateToLogin);
        guestButton = findViewById(R.id.guestButton);
        googleButton = findViewById(R.id.googleButton);
    }

    @Override
    public void onSuccess() {
        Toast.makeText(this, "Signed-in successfully!", Toast.LENGTH_SHORT).show();
        navigateToHome();
    }

    private void navigateToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onFailure(String errorMsg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(errorMsg);
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    @Override
    public void showEmailValid() {
        emailInputLayout.setError(null);
    }

    @Override
    public void showEmailNotValid(String errorMsg) {
        emailInputLayout.setError(errorMsg);
    }

}