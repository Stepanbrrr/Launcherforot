package com.example.kiosklauncher;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class KioskLauncherActivity extends AppCompatActivity {

    // ЗАМЕНИТЕ НА ПАКЕТ ВАШЕГО ПРИЛОЖЕНИЯ
    private static final String TARGET_APP_PACKAGE = "com.example.targetapp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        
        // Блокировка системных элементов
        disableSystemUI();
        
        // Запуск целевого приложения
        launchTargetApp();
    }

    private void disableSystemUI() {
        // Скрыть статус-бар
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        // Запрет на выключение экрана
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        // Блокировка лаунчера как системного
        PackageManager pm = getPackageManager();
        ComponentName componentName = new ComponentName(this, KioskLauncherActivity.class);
        pm.setComponentEnabledSetting(
            componentName,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        );
    }

    private void launchTargetApp() {
        try {
            // Попытка запуска целевого приложения
            Intent launchIntent = getPackageManager().getLaunchIntentForPackage(TARGET_APP_PACKAGE);
            if (launchIntent != null) {
                startActivity(launchIntent);
            } else {
                showToast("Приложение не найдено!");
            }
        } catch (SecurityException e) {
            showToast("Ошибка доступа: " + e.getMessage());
        } catch (Exception e) {
            showToast("Ошибка: " + e.getMessage());
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Всегда возвращаемся к целевому приложению
        launchTargetApp();
    }

    @Override
    public void onBackPressed() {
        // Запрет кнопки "Назад"
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            // Повторная блокировка при возврате фокуса
            disableSystemUI();
            launchTargetApp();
        }
    }
}
