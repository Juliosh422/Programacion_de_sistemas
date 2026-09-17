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
                // Acción a realizar cuando se hace clic en el botón
                Log.d("_TAG",  "boton pulsado"); //es .d de debug, hay otros como w para warning etc... no hay q poner el tag en recursos pq el usuario no lo ve
                ((Button) view).setText(R.string.but_main_string);
            }
        });
    }
}