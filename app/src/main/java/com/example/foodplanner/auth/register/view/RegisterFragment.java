package com.example.foodplanner.auth.register.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.foodplanner.R;
import com.example.foodplanner.auth.AuthActivity;
import com.example.foodplanner.auth.register.presenter.RegisterPresenter;
import com.example.foodplanner.db.MealsLocalDataSource;
import com.example.foodplanner.models.Repository;
import com.example.foodplanner.network.MealsRemoteDataSource;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterFragment extends Fragment implements IRegisterAuth {

    private TextInputLayout emailTextInputLayout;
    private TextInputLayout passwordTextInputLayout;
    private TextInputLayout confirmPasswordTextInputLayout;
    private TextInputEditText emailInputEditText;
    private TextInputEditText passInputEditText;
    private TextInputEditText confirmPassInputEditText;
    private TextView navigateToLogin;
    private Button signupButton;
    private RegisterPresenter registerPresenter;
    private Button googleButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUI(view);
        registerPresenter = new RegisterPresenter(this,
                Repository.getInstance(
                        MealsRemoteDataSource.getInstance(getContext()),
                        MealsLocalDataSource.getInstance(getContext())
                ));
        navigateToLogin.setOnClickListener(v -> handleNavigateToLogin());
        signupButton.setOnClickListener(v -> handleRegisterButton());
        googleButton.setOnClickListener(v -> handleGoogleButton());
        addEmailTextInputWatcher();
        addPasswordTextInputWatcher();
        addConfirmPasswordTextInputWatcher();
    }

    private void initUI(View view) {
        confirmPasswordTextInputLayout = view.findViewById(R.id.confirmPasswordTextInputLayout);
        confirmPassInputEditText = view.findViewById(R.id.confirmPasswordTextInputEdit);
        emailTextInputLayout = view.findViewById(R.id.emailTextInputLayout);
        passwordTextInputLayout = view.findViewById(R.id.passwordTextInputLayout);
        emailInputEditText = view.findViewById(R.id.emailTextInputEdit);
        passInputEditText = view.findViewById(R.id.passwordTextInputEdit);
        navigateToLogin = view.findViewById(R.id.navigateToLogin);
        signupButton = view.findViewById(R.id.navigateToSignup);
        googleButton = view.findViewById(R.id.googleButton);
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

    private void addConfirmPasswordTextInputWatcher() {
        confirmPassInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable confirmPassword) {
                registerPresenter.checkPasswordsMatch(passInputEditText.getText().toString(), confirmPassword.toString());
            }
        });
    }

    private void handleGoogleButton() {
        ((AuthActivity) requireActivity()).signIn();
    }

    private void handleRegisterButton() {
        String email = emailInputEditText.getText().toString().trim();
        String password = passInputEditText.getText().toString().trim();
        registerPresenter.register(email, password);
    }

    private void handleNavigateToLogin() {
        Navigation.findNavController(requireView()).
                navigate(R.id.action_registerFragment_to_loginFragment);
    }

    @Override
    public void onSuccess() {
        Toast.makeText(getContext(), R.string.registered_successfully, Toast.LENGTH_SHORT).show();
        handleNavigateToLogin();
    }

    @Override
    public void onFailure(String errorMsg) {
        Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showEmailValid() {
        emailTextInputLayout.setErrorEnabled(false);
        emailTextInputLayout.setError(null);
    }

    @Override
    public void showEmailNotValid(String errorMsg) {
        emailTextInputLayout.setErrorEnabled(true);
        emailTextInputLayout.setError(errorMsg);
    }

    @Override
    public void showPasswordTooShort(String errorMsg) {
        passwordTextInputLayout.setErrorEnabled(true);
        passwordTextInputLayout.setError(errorMsg);
    }

    @Override
    public void showPasswordValid() {
        passwordTextInputLayout.setErrorEnabled(false);
        passwordTextInputLayout.setError(null);
    }

    @Override
    public void showPasswordNotMatching(String errorMsg) {
        confirmPasswordTextInputLayout.setError(errorMsg);
    }

    @Override
    public void showPasswordMatching() {
        confirmPasswordTextInputLayout.setError(null);
    }
}