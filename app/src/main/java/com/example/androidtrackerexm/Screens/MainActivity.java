package com.example.androidtrackerexm.Screens;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItem;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androidtrackerexm.App;
import com.example.androidtrackerexm.LocationHelpers.LocationManager;
import com.example.androidtrackerexm.LocationHelpers.LocationService;
import com.example.androidtrackerexm.Models.AppDataBase;
import com.example.androidtrackerexm.Models.User;
import com.example.androidtrackerexm.R;
import com.example.androidtrackerexm.databinding.ActivityMainBinding;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    Bitmap userAvatar;
    View headerNav;
    TextView textViewNav;
    ImageView imageView;
    AppDataBase db;
    User tmpUser ;
    NavController navController;
    AlertDialog alertDialog;
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
        db = App.getInstance().getDatabase();

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.mapFragment,
                R.id.historyFragment,
                R.id.authorizationFragment)
                .setOpenableLayout(drawer)
                .build();


        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);

        NavigationUI.setupWithNavController(navigationView, navController);

        headerNav = binding.navView.getHeaderView(0);
        imageView = headerNav.findViewById(R.id.imageView);
        textViewNav = headerNav.findViewById(R.id.tvNHMail);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tmpUser = db.getUserDao().getAuthUser(db.getAuthUserDao().getIdAuthUser());
                pickAvatarFromGalary();

                ByteArrayOutputStream byteArrayAvatar = new ByteArrayOutputStream();
                if(userAvatar!=null)
                {
                    userAvatar.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayAvatar);
                    tmpUser.avatar=byteArrayAvatar.toByteArray();

               db.getUserDao().update(tmpUser);

                    imageView.setImageBitmap(BitmapFactory.decodeByteArray(tmpUser.avatar, 0, tmpUser.avatar.length));
                }

            }
        });

    }

    //что б в authFragment из 3 и 4 фрагмента можно было попадать только из sign out
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

    void pickAvatarFromGalary() {
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
                    imageView.setImageBitmap(userAvatar);
                    Log.d("pick-photo", userAvatar.getByteCount() + "");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    userAvatar = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    imageView.setImageBitmap(userAvatar);
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

                db.getAuthUserDao().deleteAll();

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