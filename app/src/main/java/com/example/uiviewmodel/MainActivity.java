package com.example.uiviewmodel;

import static androidx.core.app.NotificationCompat.PRIORITY_HIGH;
import static com.example.uiviewmodel.R.drawable.ic_baseline_notifications_24;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

    public class MainActivity extends AppCompatActivity {

        private static final String CHANNEL_ID = "CHANNEL_ID";
        private static final int NOTIFY_ID = 1;
        private final Handler mHandler = new Handler();
        private int chooseCamera = 0;
    private TextView textView;

     @SuppressLint("SetTextI18n")
     @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        turnOnCamera();
        pushNotification();
        search();
        getImage();
        findViewById(R.id.toggleButton);
        ToggleButton toggleButton;
        toggleButton = findViewById(R.id.toggleButton);
        textView = findViewById(R.id.textView);

        toggleButton.setOnCheckedChangeListener((compoundButton, checked) -> {
            if (checked) {
                textView.setText("Bluetooth is ON");
                BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
                adapter.enable();
            } else {
                textView.setText("Bluetooth is OFF");
                BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
                adapter.disable();
            }
        });
        // For initial setting
        if (toggleButton.isChecked()) {
            textView.setText("Bluetooth is ON");
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            adapter.enable();
        } else {
            textView.setText("Bluetooth is OFF");
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            adapter.disable();
        }
    }
    public void turnOnCamera() {

        final Button button = findViewById(R.id.check);
        final RadioButton front = findViewById(R.id.backCamera);
        final RadioButton back = findViewById(R.id.frontCamera);
        button.setOnClickListener(view -> {

            if(front.isChecked()) {
                //back camera
                chooseCamera = 0;
                openCamera();
            }

            if(back.isChecked()) {
                //front camera
                chooseCamera = 1;
                openCamera();
            }
        });
    }

    public void openCamera() {
        Intent intent = new Intent(MainActivity.this, CameraBack.class);
        intent.putExtra("CHOOSE_CAMERA", chooseCamera);
        startActivity(intent);
    }

    public static void createChannelIfNeeded(NotificationManager manager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(notificationChannel);
        }
    }

    public void displayNotification(View view) {
        mHandler.postDelayed(mNoteRunnable, 10000);
    }

    private final Runnable mNoteRunnable = () -> {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                        .setAutoCancel(false)
                        .setSmallIcon(ic_baseline_notifications_24)
                        .setWhen(System.currentTimeMillis())
                        .setContentIntent(pendingIntent)
                        .setContentTitle("Notification")
                        .setContentText("10 seconds have passed!")
                        .setPriority(PRIORITY_HIGH);
        createChannelIfNeeded(notificationManager);
        notificationManager.notify(NOTIFY_ID, notificationBuilder.build());

    };

    public void pushNotification() {
        final Button button = findViewById(R.id.pushNotification);
        button.setOnClickListener(this::displayNotification);
    }

    public void search() {

        final Button button = findViewById(R.id.searchButton);

        button.setOnClickListener(view -> {
            String searchFor =  ((EditText)findViewById(R.id.searchWord)).getText().toString();
            Intent viewSearch = new Intent(Intent.ACTION_WEB_SEARCH);
            viewSearch.putExtra(SearchManager.QUERY, searchFor);
            startActivity(viewSearch);
        });
    }

    public void getImage() {
        final Button button = findViewById(R.id.image);
        final String url = "https://picsum.photos/200/300?random=1";
        button.setOnClickListener(view -> image(url));
    }

    public void image(String url) {
        ImageView img= findViewById(R.id.imageView);
        Picasso.with(MainActivity.this)
                .load(url)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(img);
    }
}