package es.udc.psi.p22barbeito;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import es.udc.psi.p22barbeito.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    private final ActivityResultLauncher<Intent> reactionLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    String pressed = result.getData().getStringExtra(ReactionActivity.EXTRA_REACTION);
                    binding.tvReaction.setText(pressed);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.butShare.setOnClickListener(v -> {
            String text = getTextOrWarn();
            if (text == null) return;
            Intent send = new Intent(Intent.ACTION_SEND);
            send.setType("text/plain");
            send.putExtra(Intent.EXTRA_TEXT, text);
            startActivity(Intent.createChooser(send, getString(R.string.but_share_string)));
        });

        binding.butContinue.setOnClickListener(v -> {
            String text = getTextOrWarn();
            if (text == null) return;
            Intent intent = new Intent(this, ShareActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT, text);
            startActivity(intent);
        });

        binding.butReaction.setOnClickListener(v -> {
            String text = getTextOrWarn();
            if (text == null) return;
            Intent intent = new Intent(this, ReactionActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT, text);
            reactionLauncher.launch(intent);
        });
    }

    private String getTextOrWarn() {
        String text = binding.editTextInput.getText().toString().trim();
        if (text.isEmpty()) {
            Toast.makeText(this, R.string.empty_string, Toast.LENGTH_SHORT).show();
            // por qué hay que usar getApplicationContext() en lugar de this? Porque this hace referencia al OnClickListener, no a la actividad.

            return null;
        }
        return text;
    }
}