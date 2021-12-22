package com.example.androidtrackerexm.screens.login;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidtrackerexm.App;
import com.example.androidtrackerexm.MainActivityViewModel;
import com.example.androidtrackerexm.Models.AppDataBase;
import com.example.androidtrackerexm.Models.AuthUser;
import com.example.androidtrackerexm.Models.User;
import com.example.androidtrackerexm.R;
import com.example.androidtrackerexm.databinding.FragmentAuthorizationBinding;


public class LoginFragment extends Fragment {

    FragmentAuthorizationBinding binding;
    AppDataBase db;
    MainActivityViewModel mainViewModel;

    public LoginFragment() {
        super(R.layout.fragment_authorization);
    }

    @SuppressLint("Range")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_authorization, container, false);

        getActivity().findViewById(R.id.toolbar).setVisibility(View.GONE);
        getActivity().findViewById(R.id.blockCalendar).setVisibility(View.GONE);

        binding = FragmentAuthorizationBinding.bind(view);

        mainViewModel = new ViewModelProvider(getActivity()).get(MainActivityViewModel.class);
        db = App.getInstance().getDatabase();


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getAuthUser();

        binding.btnRegister.setOnClickListener(v -> goToRegisterScreen());
        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAuthorization()) {
                    binding.tvHelpEmail.setVisibility(View.GONE);
                    binding.tvHelpPassword.setVisibility(View.GONE);
                    goToMapScreen();
                } else {
                    binding.tvHelpEmail.setVisibility(View.VISIBLE);
                    binding.tvHelpPassword.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    public void getAuthUser() {
        User tmpUser = db.getUserDao().getAuthUser(db.getAuthUserDao().getIdAuthUser());
        if (tmpUser != null) {
            mainViewModel.setCurrentUser(tmpUser);
            goToMapScreen();
        }
    }

    public boolean checkAuthorization() {
        String tmpPass = db.getUserDao().getPassword(binding.etMail.getText().toString());
        if (tmpPass != null) {
            if (binding.etPassword.getText().toString().equals(tmpPass)) {
                AuthUser authUser = new AuthUser();
                authUser.idUser = db.getUserDao().getId(binding.etMail.getText().toString());
                db.getAuthUserDao().insert(authUser);
                mainViewModel.setCurrentUser(db.getUserDao().getAuthUser( authUser.idUser));
                return true;
            }
        }

        return false;
    }

    public void goToRegisterScreen() {
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_authorizationFragment_to_registrationFragment);
    }

    public void goToMapScreen() {
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_authorizationFragment_to_mapFragment);
    }

}