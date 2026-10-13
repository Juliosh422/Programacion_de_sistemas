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

    Button but1, but2, but3;

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
        but2 = findViewById(R.id.but2);
        but3 = findViewById(R.id.but3);

        but1.setOnClickListener(this);
        but2.setOnClickListener(this);
        but3.setOnClickListener(new View.OnClickListener() { // listener anónimo, solo este
            @Override
            public void onClick(View view) {
                incrementar((Button) view);
            }
        });

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.but1 || view.getId() == R.id.but2) {
            incrementar((Button) view);
        }
    }

    private void incrementar(Button b) {
        int val = Integer.parseInt(b.getText().toString());
        val++;
        b.setText(String.valueOf(val));
        Log.d("_TAG", "segunda_activ - botón " + b.getId() + " = " + val);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("count1", Integer.parseInt(but1.getText().toString()));
        outState.putInt("count2", Integer.parseInt(but2.getText().toString()));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        but1.setText(String.valueOf(savedInstanceState.getInt("count1", 0)));
        but2.setText(String.valueOf(savedInstanceState.getInt("count2", 0)));
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("_TAG", "segunda_activ - onStart");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("_TAG", "segunda_activ - onResume");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.d("_TAG", "segunda_activ - onPause");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d("_TAG", "segunda_activ - onStop");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("_TAG", "segunda_activ - onDestroy");
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("_TAG", "segunda_activ - onRestart");
    }
}