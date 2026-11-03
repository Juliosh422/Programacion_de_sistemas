package es.udc.psi_2025_26.p24vazreg;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.IntentCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import es.udc.psi_2025_26.p24vazreg.databinding.ActivityDetailBinding;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding binding;
    private Article article;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Recoger el Article parcelable y su posición en la lista
        article = IntentCompat.getParcelableExtra(getIntent(), MainActivity.KEY_ARTICLE, Article.class);
        position = getIntent().getIntExtra(MainActivity.KEY_POSITION, -1);

        if (article != null) {
            binding.etTitle.setText(article.getTitle());
            binding.etSubtitle.setText(article.getSubtitle());
            binding.etDescription.setText(article.getDescription());
        }

        // Editar: activa la edición de los tres atributos
        binding.btnEdit.setOnClickListener(v -> setEditable(true));

        // Eliminar: cierra la ventana e informa del borrado
        binding.btnDelete.setOnClickListener(v -> {
            Intent result = new Intent();
            result.putExtra(MainActivity.KEY_ACTION, MainActivity.ACTION_DELETE);
            result.putExtra(MainActivity.KEY_POSITION, position);
            setResult(RESULT_OK, result);
            finish();
        });

        // OK: cierra la ventana y devuelve los cambios
        binding.btnOk.setOnClickListener(v -> {
            Article updated = new Article(
                    binding.etTitle.getText().toString(),
                    binding.etSubtitle.getText().toString(),
                    binding.etDescription.getText().toString());
            Intent result = new Intent();
            result.putExtra(MainActivity.KEY_ACTION, MainActivity.ACTION_UPDATE);
            result.putExtra(MainActivity.KEY_ARTICLE, updated);
            result.putExtra(MainActivity.KEY_POSITION, position);
            setResult(RESULT_OK, result);
            finish();
        });
    }

    private void setEditable(boolean editable) {
        binding.etTitle.setEnabled(editable);
        binding.etSubtitle.setEnabled(editable);
        binding.etDescription.setEnabled(editable);
    }
}
