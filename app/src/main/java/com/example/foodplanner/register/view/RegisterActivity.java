package com.example.foodplanner.register.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodplanner.R;
import com.example.foodplanner.login.view.LoginActivity;
import com.example.foodplanner.register.presenter.RegisterPresenter;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity implements IRegisterAuth {

    private TextInputLayout emailTextInputLayout;
    private TextInputLayout passwordTextInputLayout;
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
        addEmailTextInputWatcher();
        addPasswordTextInputWatcher();
    }

    private void addPasswordTextInputWatcher() {
        passInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable password) {
                registerPresenter.validatePassword(password.toString());
            }
        });
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
                registerPresenter.validateEmail(email.toString());
            }
        });
    }

    private void handleRegisterButton() {
        String email = emailInputEditText.getText().toString().trim();
        String password = passInputEditText.getText().toString().trim();
        registerPresenter.tryRegister(email, password);
        navigateToLogin();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void handleNavigateToSignup() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void initUI() {
        emailTextInputLayout = findViewById(R.id.emailTextInputLayout);
        passwordTextInputLayout = findViewById(R.id.passwordTextInputLayout);
        emailInputEditText = findViewById(R.id.emailTextInputEdit);
        passInputEditText = findViewById(R.id.passwordTextInputEdit);
        navigateToLogin = findViewById(R.id.navigateToLogin);
        signupButton = findViewById(R.id.signupButton);
    }

    @Override
    public void onSuccess() {
        Toast.makeText(this, "Registered successfully!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure(String errorMsg) {
        Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showEmailValid() {
        emailTextInputLayout.setError(null);
    }

    @Override
    public void showEmailNotValid(String errorMsg) {
        emailTextInputLayout.setError(errorMsg);
    }

    @Override
    public void showPasswordTooShort(String errorMsg) {
        passwordTextInputLayout.setError(errorMsg);
    }

    @Override
    public void showPasswordValid() {
        passwordTextInputLayout.setError(null);
    }
}