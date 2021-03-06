package com.example.androidtrackerexm;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androidtrackerexm.locationHelpers.LocationService;
import com.example.androidtrackerexm.models.User;
import com.example.androidtrackerexm.databinding.ActivityMainBinding;
import com.example.androidtrackerexm.databinding.NavHeaderMainBinding;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    Bitmap userAvatar;

    NavController navController;
    AlertDialog alertDialog;

    MainActivityViewModel viewModel;

    public static final String TAG = MainActivity.class.getCanonicalName();


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Log.d(TAG, "Create Activity!!!!!!!!!!!!!!!!!!!!!!!!!!");
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.mapFragment,
                R.id.historyFragment,
                R.id.loginFragment)
                .setOpenableLayout(drawer)
                .build();


        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        binding.setModelMain(viewModel);
        binding.setLifecycleOwner(this);
        binding.appBarMain.setBarModel(viewModel);

        NavHeaderMainBinding headerMenuBinding = DataBindingUtil.inflate(getLayoutInflater(),
                R.layout.nav_header_main,
                binding.navView,
                false);
        binding.navView.addHeaderView(headerMenuBinding.getRoot());
        headerMenuBinding.setMainModel(viewModel);


    }

    //?????? ?? ?? authFragment ???? 3 ?? 4 ?????????????????? ?????????? ???????? ???????????????? ???????????? ???? sign out
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_BACK:
                    if (binding.appBarMain.toolbar.getVisibility() == View.VISIBLE) {
                        binding.drawerLayout.open();
                        return true;
                    }
                    break;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    //  void pickAvatarFromGalary() {
    public void selectAvatar(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && data != null) {
            Log.d("pick-photo", data.getData().toString());


            if (Build.VERSION.SDK_INT >= 28) {
                ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), data.getData());

                try {
                    userAvatar = ImageDecoder.decodeBitmap(source);
                    viewModel.setGlobalAvatar(userAvatar);
                    //  imageView.setImageBitmap(userAvatar);
                    //   Log.d("pick-photo", userAvatar.getByteCount() + "");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    userAvatar = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    //  imageView.setImageBitmap(userAvatar);
                    viewModel.setGlobalAvatar(userAvatar);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void onSignOutClick(MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Logout");

        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                viewModel.loggin.setValue(false);
                viewModel.userRepository.clearAuthUser();
                Intent intent = new Intent(getApplicationContext(), LocationService.class);
                stopService(intent);

                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent1);
                finish();

            }
        });

        builder.setNegativeButton("Decline", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog = builder.create();
        alertDialog.show();

    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "Destroy Activity!!!!!!!!!!!!!!!!!!!!!!!!!!");

        super.onDestroy();

    }


}