package com.example.androidtrackerexm.screens.login;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidtrackerexm.App;
import com.example.androidtrackerexm.MainActivityViewModel;
import com.example.androidtrackerexm.helpers.UserRepository;
import com.example.androidtrackerexm.models.AuthUser;
import com.example.androidtrackerexm.models.User;


public class LoginFragmentViewModel extends AndroidViewModel {

    UserRepository userRepository;
    public User loggingUser;
    MutableLiveData<Boolean> checkLogin = new MutableLiveData<>();


    public LoginFragmentViewModel(@NonNull Application application) {
        super(application);
        userRepository = App.getInstance().getUserRepository();
        loggingUser=new User();
    }

    public void getAuthUser() {
        User tmp=userRepository.getAuthUser();
        if (tmp != null) {
            loggingUser=tmp;
            checkLogin.setValue(true);
        }

    }

    public boolean checkAuthorization() {
        String tmpPass = userRepository.getExistingPass(loggingUser.getEmail());
        if (tmpPass != null) {
            if (loggingUser.password.equals(tmpPass)) {
                AuthUser authUser = new AuthUser();
                authUser.idUser = userRepository.getIdUserForMail(loggingUser.email);

                loggingUser = userRepository.getAuthUser();
                userRepository.addAuthUser(authUser);

                checkLogin.setValue(true);
                return true;
            }
        }

        return false;
    }
}

