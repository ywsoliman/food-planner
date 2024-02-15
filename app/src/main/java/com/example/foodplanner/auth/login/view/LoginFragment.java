package com.example.foodplanner.auth.login.view;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.foodplanner.R;
import com.example.foodplanner.auth.AuthActivity;
import com.example.foodplanner.auth.IAuthenticate;
import com.example.foodplanner.auth.login.presenter.LoginPresenter;
import com.example.foodplanner.db.MealsLocalDataSource;
import com.example.foodplanner.home.view.HomeActivity;
import com.example.foodplanner.models.Repository;
import com.example.foodplanner.network.MealsRemoteDataSource;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment implements IAuthenticate {


    private static final String PREF_NAME = "RememberMePrefs";
    private TextInputLayout emailInputLayout;
    private TextInputEditText emailInputEditText;
    private TextInputEditText passInputEditText;
    private TextView navigateToSignup;
    private Button loginButton;
    private Button guestButton;
    private LoginPresenter loginPresenter;
    private Button googleButton;
    private CheckBox rememberMe;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void rememberMeCheck() {
        SharedPreferences sharedPreferences =
                requireActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        boolean rememberMe = sharedPreferences.getBoolean("rememberMe", false);
        if (rememberMe) {
            navigateToHome();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUI(view);

        rememberMeCheck();

        loginPresenter = new LoginPresenter(this,
                Repository.getInstance(
                        FirebaseAuth.getInstance(), MealsRemoteDataSource.getInstance(requireContext()), MealsLocalDataSource.getInstance(getContext())
                ));

        loginButton.setOnClickListener(v -> handleLoginButton());
        navigateToSignup.setOnClickListener(v -> handleNavigateToSignup());
        guestButton.setOnClickListener(v -> handleGuestButton());
        googleButton.setOnClickListener(v -> handleGoogleButton());
        addEmailTextInputWatcher();
    }

    private void initUI(View view) {
        emailInputLayout = view.findViewById(R.id.emailTextInputLayout);
        emailInputEditText = view.findViewById(R.id.emailTextInputEdit);
        passInputEditText = view.findViewById(R.id.passwordTextInputEdit);
        navigateToSignup = view.findViewById(R.id.navigateToSignup);
        loginButton = view.findViewById(R.id.navigateToLogin);
        guestButton = view.findViewById(R.id.guestButton);
        googleButton = view.findViewById(R.id.googleButton);
        rememberMe = view.findViewById(R.id.rememberMeCheck);
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
                loginPresenter.validateEmail(email.toString());
            }
        });
    }

    private void handleGoogleButton() {
        ((AuthActivity) requireActivity()).signIn();
    }

    private void handleGuestButton() {
        loginPresenter.loginAsGuest();
    }

    private void handleLoginButton() {
        String email = emailInputEditText.getText().toString().trim();
        String password = passInputEditText.getText().toString().trim();
        loginPresenter.login(email, password);
    }

    private void handleNavigateToSignup() {
        Navigation.findNavController(requireView())
                .navigate(R.id.action_loginFragment_to_registerFragment);
    }

    private void navigateToHome() {
        if (rememberMe.isChecked()) {
            SharedPreferences sharedPreferences =
                    requireActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("rememberMe", true);
            editor.apply();
        }
        Intent intent = new Intent(requireActivity(), HomeActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }

    @Override
    public void onSuccess() {
        Toast.makeText(getContext(), "Signed-in successfully!", Toast.LENGTH_SHORT).show();
        navigateToHome();
    }

    @Override
    public void onFailure(String errorMsg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
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