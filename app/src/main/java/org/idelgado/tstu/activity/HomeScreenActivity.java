package org.idelgado.tstu.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import org.idelgado.tstu.LauncherApplication;
import org.idelgado.tstu.R;
import org.idelgado.tstu.service.TSTUService;


public class HomeScreenActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        ((LauncherApplication)getApplication()).setHomeScreenActivity(this);

        Button uninstallButton = (Button)findViewById(R.id.uninstall_button);
        uninstallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri packageURI = Uri.parse("package:" + getPackageName());
                Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
                startActivity(uninstallIntent);
            }
        });

        // Show the Launcher selection intent if it is not already the default
        launchAppChooser();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_screen, menu);
        return true;
    }

    /**
     * Starts the application based on the specified package name
     * @param packageName
     */
    public void startApplication(String packageName) {
        Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);

        if (intent != null) {
            startActivity(intent);

            // Start the transient state heads up service
            Intent serviceIntent = new Intent(this, TSTUService.class);
            serviceIntent.putExtra(TSTUService.APP_PACKAGE_NAME, packageName);

            startService(serviceIntent);
        }
    }

    private void launchAppChooser() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
