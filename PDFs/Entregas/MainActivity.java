package es.udc.psi_2025_26.p24vazreg;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import es.udc.psi_2025_26.p24vazreg.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private ArticlesAdapter mAdapter, mAdapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        //setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // inicialización de datos
        ArrayList<Article> initialData = new ArrayList<>();
        for (int i=0; i< 10; i++)
            initialData.add(new Article(i));
        mAdapter = new ArticlesAdapter (initialData);
        initRecycler(mAdapter, binding.articlesRv, new ArticlesAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Log.d("_TAG", " Item " + position + " Data: " +mAdapter.getItem(position).getTitle() );
                Toast.makeText(getApplicationContext(), "item " + position,
                        Toast.LENGTH_SHORT).show();
            }
        });

        // inicializacion datos 2o RecyclerView
        mAdapter2 = new ArticlesAdapter (initialData);
        initRecycler(mAdapter2, binding.articlesRv2, new ArticlesAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Log.d("_TAG", " 2o Item " + position + " Otros Data: " +mAdapter.getItem(position).getTitle() );
                Toast.makeText(getApplicationContext(), "item " + position,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initRecycler(ArticlesAdapter mAdapter, RecyclerView recyclerView,
                              ArticlesAdapter.OnItemClickListener listener) {
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setClickListener(listener);
    }
}
