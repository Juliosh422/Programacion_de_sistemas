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

public class segunda_activ extends AppCompatActivity implements View.OnClickListener{

    Button but1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_segunda);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        but1 = findViewById(R.id.but1); // R es la clase principal de los recursos, en este caso accedemos a los recursos de tipo id y dentro de ids but_main

        but1.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Log.d("_TAG",  "boton pulsado");
        Button tmp_but = (Button) view; //cambio el view de vista a boton
        tmp_but.setText(tmp_but.getText().toString() + getString(R.string.zero_string));
    }
}