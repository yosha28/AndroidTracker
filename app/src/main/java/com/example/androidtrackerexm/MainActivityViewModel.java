package com.example.androidtrackerexm;

import android.app.Application;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.androidtrackerexm.models.AppDataBase;
import com.example.androidtrackerexm.models.User;
import com.example.androidtrackerexm.helpers.UserRepository;

import java.io.ByteArrayOutputStream;

public class MainActivityViewModel extends AndroidViewModel {

    MutableLiveData<Bitmap> globalAvatar = new MutableLiveData<>();
    MutableLiveData<User> globalUser = new MutableLiveData<>();
    ObservableField<User> userObserve = new ObservableField<>();

    MutableLiveData<Boolean> loggin=new MutableLiveData<>();
    User currentUser;
    UserRepository userRepository;
    AppDataBase db;
    AlertDialog alertDialog;
    Boolean logout = false;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);

        db = App.getInstance().getDatabase();
        userRepository = App.getInstance().getUserRepository();
        loggin.setValue(false);

    }

    /* public ObservableField<User> getUserObserve() {
         return userObserve;
     }

     public MutableLiveData<Bitmap> getGlobalAvatar() {
         return globalAvatar;
     }
 */
    public void setCurrentUser(User user) {
        this.currentUser = user;
      //  globalUser.setValue(user);
        userObserve.set(user);
        loggin.setValue(true);
    }

    public void setGlobalAvatar(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayAvatar = new ByteArrayOutputStream();
        globalAvatar.postValue(bitmap);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayAvatar);

        if (currentUser != null) {
            currentUser.avatar = byteArrayAvatar.toByteArray();
            userObserve.set(currentUser);
            userObserve.notifyChange();

            userRepository.updateUser(currentUser);
        }

    }
    public ObservableField<User> getUserLogging() {
        return userObserve;
    }

    @Override
    protected void onCleared() {

        super.onCleared();
    }
}
