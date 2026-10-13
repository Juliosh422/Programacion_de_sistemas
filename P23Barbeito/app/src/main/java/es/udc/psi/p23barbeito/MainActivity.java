package es.udc.psi.p23barbeito;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity
        implements TopFragment.TopListener, TextFragment.TextListener {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Salir")
                        .setMessage("¿Quieres salir de la app?")
                        .setPositiveButton("OK", (d, w) -> {
                            Log.d(TAG, "Dialog: OK");
                            Toast.makeText(MainActivity.this, "OK", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .setNegativeButton("Cancel", (d, w) -> {
                            Log.d(TAG, "Dialog: Cancel");
                            Toast.makeText(MainActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
                        })
                        .show();
            }
        });
    }

    private TopFragment top() {
        return (TopFragment) getSupportFragmentManager().findFragmentById(R.id.TopFragm);
    }

    private void show(Fragment f) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, f)
                .commit();
        top().setClearEnabled(true);
    }

    @Override
    public void onShowText(String text) {
        show(TextFragment.newInstance(text));
    }

    @Override
    public void onShowUrl(String url) {
        show(WebFragment.newInstance(url));
    }

    @Override
    public void onClear() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentById(R.id.fragment_container);
        if (f != null) fm.beginTransaction().remove(f).commit();
        top().setClearEnabled(true);
    }

    @Override
    public void onEnableToggled(boolean enabled) {
        top().setClearEnabled(enabled);
    }
}