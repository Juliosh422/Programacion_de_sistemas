package es.udc.psi_2025_26.p24vazreg;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.IntentCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import es.udc.psi_2025_26.p24vazreg.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ARTICLE = "KEY_ARTICLE";
    public static final String KEY_POSITION = "KEY_POSITION";
    public static final String KEY_ACTION = "KEY_ACTION";
    public static final String ACTION_DELETE = "DELETE";
    public static final String ACTION_UPDATE = "UPDATE";

    private static final int DEFAULT_NEW_ITEMS = 10;

    private ActivityMainBinding binding;
    private ArticlesAdapter mAdapter;
    private ArrayList<Article> mDataset;

    // Intent con resultado (API nueva, tema 2.2 Intents)
    private final ActivityResultLauncher<Intent> detailLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() != RESULT_OK) return;
                Intent data = result.getData();
                if (data == null) return;

                int position = data.getIntExtra(KEY_POSITION, -1);
                if (position < 0 || position >= mDataset.size()) return;

                String action = data.getStringExtra(KEY_ACTION);
                if (ACTION_DELETE.equals(action)) {
                    mDataset.remove(position);
                    mAdapter.notifyItemRemoved(position);
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.msg_deleted, position),
                            Toast.LENGTH_SHORT).show();
                } else if (ACTION_UPDATE.equals(action)) {
                    Article updated = IntentCompat.getParcelableExtra(data, KEY_ARTICLE, Article.class);
                    if (updated != null) {
                        mDataset.set(position, updated);
                        mAdapter.notifyItemChanged(position);
                        Toast.makeText(getApplicationContext(),
                                getString(R.string.msg_edited, position),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

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

        // inicialización de datos
        mDataset = new ArrayList<>();
        for (int i = 0; i < 10; i++)
            mDataset.add(new Article(i));

        initRecycler();

        // Botón 1: borrar todos los elementos
        binding.btnClear.setOnClickListener(v -> {
            int size = mDataset.size();
            mDataset.clear();
            mAdapter.notifyItemRangeRemoved(0, size);
        });

        // Botón 2: añadir elementos (diálogo con EditText)
        binding.btnAdd.setOnClickListener(v -> showAddDialog());
    }

    private void initRecycler() {
        mAdapter = new ArticlesAdapter(mDataset);
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.articlesRv.setLayoutManager(linearLayoutManager);
        binding.articlesRv.setAdapter(mAdapter);

        // Click en un elemento: abre la segunda actividad con el Article parcelable
        mAdapter.setClickListener((view, position) -> {
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra(KEY_ARTICLE, mAdapter.getItem(position));
            intent.putExtra(KEY_POSITION, position);
            detailLauncher.launch(intent);
        });
    }

    private void showAddDialog() {
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
        input.setText(String.valueOf(DEFAULT_NEW_ITEMS));

        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_add_title)
                .setMessage(R.string.dialog_add_message)
                .setView(input)
                .setPositiveButton(R.string.btn_add, (dialog, which) -> {
                    String text = input.getText().toString();
                    if (text.isEmpty()) return;
                    int count = Integer.parseInt(text);
                    // La numeración empieza en el tamaño actual de la lista
                    int start = mDataset.size();
                    for (int i = 0; i < count; i++)
                        mDataset.add(new Article(start + i));
                    mAdapter.notifyItemRangeInserted(start, count);
                })
                .setNegativeButton(R.string.btn_cancel, null)
                .show();
    }
}
