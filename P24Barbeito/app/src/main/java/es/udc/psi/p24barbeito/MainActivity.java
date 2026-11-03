package es.udc.psi.p24barbeito;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import es.udc.psi.p24barbeito.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ARTICLE = "KEY_ARTICLE";
    public static final String KEY_POSITION = "KEY_POSITION";
    public static final String KEY_ACTION = "KEY_ACTION";
    public static final String ACTION_DELETE = "DELETE";
    public static final String ACTION_UPDATE = "UPDATE";

    private ActivityMainBinding binding;
    private ArticlesAdapter mAdapter;
    private ArrayList<Article> mDataset;

    private final ActivityResultLauncher<Intent> detailLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    String action = data.getStringExtra(KEY_ACTION);
                    int position = data.getIntExtra(KEY_POSITION, -1);

                    if (position != -1) {
                        if (ACTION_DELETE.equals(action)) {
                            mDataset.remove(position);
                            mAdapter.notifyItemRemoved(position);
                            Toast.makeText(this, "Eliminado el elemento número: " + position, Toast.LENGTH_SHORT).show();
                        } else if (ACTION_UPDATE.equals(action)) {
                            Article updatedArticle = data.getParcelableExtra(KEY_ARTICLE);
                            if (updatedArticle != null) {
                                mDataset.set(position, updatedArticle);
                                mAdapter.notifyItemChanged(position);
                                Toast.makeText(this, "Editado el elemento número: " + position, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 10 elementos iniciales
        mDataset = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mDataset.add(new Article(i));
        }

        mAdapter = new ArticlesAdapter(mDataset);
        binding.articlesRv.setLayoutManager(new LinearLayoutManager(this));
        binding.articlesRv.setAdapter(mAdapter);

        mAdapter.setClickListener((view, position) -> {
            Article article = mAdapter.getItem(position);
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra(KEY_ARTICLE, article);
            intent.putExtra(KEY_POSITION, position);
            detailLauncher.launch(intent);
        });

        binding.btnClear.setOnClickListener(v -> {
            int size = mDataset.size();
            mDataset.clear();
            mAdapter.notifyItemRangeRemoved(0, size);
        });

        // Botón Añadir
        binding.btnAdd.setOnClickListener(v -> showAddDialog());
    }

    private void showAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Añadir elementos");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setText("10");
        builder.setView(input);

        builder.setPositiveButton("Añadir", (dialog, which) -> {
            String value = input.getText().toString();
            int count = value.isEmpty() ? 10 : Integer.parseInt(value);
            int startSize = mDataset.size();
            for (int i = 0; i < count; i++) {
                mDataset.add(new Article(startSize + i));
            }
            mAdapter.notifyItemRangeInserted(startSize, count);
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
        builder.show();
    }
}