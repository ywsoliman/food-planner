package com.example.foodplanner.register.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodplanner.R;
import com.example.foodplanner.register.presenter.IRegisterView;
import com.example.foodplanner.register.presenter.RegisterPresenter;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity implements IRegisterView {

    private TextInputEditText emailInputEditText;
    private TextInputEditText passInputEditText;
    private TextView navigateToLogin;
    private Button signupButton;
    private RegisterPresenter registerPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initUI();
        registerPresenter = new RegisterPresenter(this);
        navigateToLogin.setOnClickListener(v -> handleNavigateToSignup());
        signupButton.setOnClickListener(v -> handleRegisterButton());
    }

    private void handleRegisterButton() {
        String email = emailInputEditText.getText().toString().trim();
        String password = passInputEditText.getText().toString().trim();
        registerPresenter.tryRegister(email, password);
    }

    private void handleNavigateToSignup() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void initUI() {
        emailInputEditText = findViewById(R.id.emailTextInputEdit);
        passInputEditText = findViewById(R.id.passwordTextInputEdit);
        navigateToLogin = findViewById(R.id.navigateToLogin);
        signupButton = findViewById(R.id.signupButton);
    }

    @Override
    public void onRegisterSuccess() {
        Toast.makeText(this, "Registered successfully!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRegisterFailed(String errorMsg) {
        Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
    }
}