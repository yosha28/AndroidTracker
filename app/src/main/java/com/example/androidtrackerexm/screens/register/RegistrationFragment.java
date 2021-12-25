package com.example.androidtrackerexm.screens.register;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidtrackerexm.App;
import com.example.androidtrackerexm.models.AppDataBase;
import com.example.androidtrackerexm.models.AuthUser;
import com.example.androidtrackerexm.models.User;
import com.example.androidtrackerexm.R;
import com.example.androidtrackerexm.databinding.FragmentRegistrationBinding;


public class RegistrationFragment extends Fragment {

    FragmentRegistrationBinding binding;
    String emailText;
    User user;
    AppDataBase db;

    public RegistrationFragment() {
        super(R.layout.fragment_registration);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().findViewById(R.id.toolbar).setVisibility(View.GONE);
        getActivity().findViewById(R.id.blockCalendar).setVisibility(View.GONE);
        db = App.getInstance().getDatabase();
        return inflater.inflate(R.layout.fragment_registration, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        binding = FragmentRegistrationBinding.bind(view);

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailText = binding.etMail.getText().toString();
                if (!emailValidator(emailText)) {
                    binding.tvHelpEmail.setVisibility(View.VISIBLE);
                } else {
                    binding.tvHelpEmail.setVisibility(View.GONE);

                    user = new User();
                    user.email = binding.etMail.getText().toString();
                    user.password = binding.etPassword.getText().toString();

                    AuthUser authUser = new AuthUser();

                    authUser.idUser = db.getUserDao().insert(user);

                    db.getAuthUserDao().insert(authUser);

                    goToMapScreen();
                }

            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    public void goToMapScreen() {
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_registrationFragment_to_mapFragment);
    }

    public boolean emailValidator(String emailToText) {
        return Patterns.EMAIL_ADDRESS.matcher(emailToText).matches();
    }
}