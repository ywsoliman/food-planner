package com.example.foodplanner.login.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodplanner.R;
import com.example.foodplanner.home.view.HomeActivity;
import com.example.foodplanner.register.view.RegisterActivity;
import com.example.foodplanner.login.presenter.LoginPresenter;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity implements ILoginView {

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
        emailInputEditText = findViewById(R.id.emailTextInputEdit);
        passInputEditText = findViewById(R.id.passwordTextInputEdit);
        navigateToSignup = findViewById(R.id.signupButton);
        loginButton = findViewById(R.id.navigateToLogin);
        guestButton = findViewById(R.id.guestButton);
        googleButton = findViewById(R.id.googleButton);
    }

    @Override
    public void onLoginSuccess() {
        Toast.makeText(this, "Signed-in successfully!", Toast.LENGTH_SHORT).show();
        navigateToHome();
    }

    private void navigateToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLoginFailed(String errorMsg) {
        Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
    }

}