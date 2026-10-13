package es.udc.psi_2025_26.p24vazreg;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import es.udc.psi_2025_26.p24vazreg.databinding.ItemBinding;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.MyViewHolder> {
    private final ArrayList<Article> mDataset;
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private ItemBinding binding;

        public MyViewHolder(ItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void bind(Article article) {
            binding.itemTitle.setText(article.getTitle());
        }
    }
    public ArticlesAdapter(ArrayList<Article> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public ArticlesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        ItemBinding binding = ItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(mDataset.get(position));
    }
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}