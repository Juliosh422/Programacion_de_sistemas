package es.udc.psi.p21barbeito;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Button but_main;

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

        but_main = findViewById(R.id.but_main); // R es la clase principal de los recursos, en este caso accedemos a los recursos de tipo id y dentro de ids but_main

        but_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("_TAG", "Nombre actual: " + ((Button) view).getText());
                ((Button) view).setText(R.string.boton_string); // nuevo recurso "Botón"
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("_TAG", "MainActivity - onStart");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("_TAG", "MainActivity - onResume");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.d("_TAG", "MainActivity - onPause");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d("_TAG", "MainActivity - onStop");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("_TAG", "MainActivity - onDestroy");
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("_TAG", "MainActivity - onRestart");
    }
}