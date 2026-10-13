package es.udc.psi.p22barbeito;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import es.udc.psi.p22barbeito.databinding.ActivityReactionBinding;

public class ReactionActivity extends AppCompatActivity {
    public static final String EXTRA_REACTION = "es.udc.psi.p22barbeito.REACTION";
    ActivityReactionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityReactionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.tvText.setText(getIntent().getStringExtra(Intent.EXTRA_TEXT));

        binding.butReaction1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishWith(binding.butReaction1);
            }
        });
        binding.butReaction2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishWith(binding.butReaction2);
            }
        });
        binding.butReaction3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishWith(binding.butReaction3);
            }
        });

    }

    private void finishWith(Button b) {
        Intent result = new Intent();
        result.putExtra(EXTRA_REACTION, b.getText().toString());
        setResult(RESULT_OK, result);
        finish();
    }
}