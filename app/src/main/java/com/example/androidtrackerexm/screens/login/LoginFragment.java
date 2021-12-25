package com.example.androidtrackerexm.screens.login;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidtrackerexm.App;
import com.example.androidtrackerexm.MainActivityViewModel;
import com.example.androidtrackerexm.databinding.FragmentLoginBinding;
import com.example.androidtrackerexm.models.AppDataBase;
import com.example.androidtrackerexm.models.AuthUser;
import com.example.androidtrackerexm.models.User;
import com.example.androidtrackerexm.R;


public class LoginFragment extends Fragment {

    FragmentLoginBinding binding;
    MainActivityViewModel mainViewModel;
    LoginFragmentViewModel viewModel;

    public LoginFragment() {
        super(R.layout.fragment_login);
    }

    @SuppressLint("Range")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        getActivity().findViewById(R.id.toolbar).setVisibility(View.GONE);
        getActivity().findViewById(R.id.blockCalendar).setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentLoginBinding.bind(view);

        viewModel = new ViewModelProvider(this).get(LoginFragmentViewModel.class);
        mainViewModel = new ViewModelProvider(getActivity()).get(MainActivityViewModel.class);

        binding.setLifecycleOwner(this);
        binding.setModelLogin(viewModel);

        viewModel.getAuthUser();

        viewModel.checkLogin.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    mainViewModel.setCurrentUser(viewModel.loggingUser);
                    viewModel.checkLogin.setValue(false);
                    goToMapScreen();
                }

            }
        });


        binding.btnRegister.setOnClickListener(v -> goToRegisterScreen());
        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewModel.checkAuthorization()) {
                    binding.tvHelpEmail.setVisibility(View.GONE);
                    binding.tvHelpPassword.setVisibility(View.GONE);

                } else {
                    binding.tvHelpEmail.setVisibility(View.VISIBLE);
                    binding.tvHelpPassword.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    public void goToRegisterScreen() {
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_loginFragment_to_registrationFragment);
    }

    public void goToMapScreen() {
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_loginFragment_to_mapFragment);

    }

}