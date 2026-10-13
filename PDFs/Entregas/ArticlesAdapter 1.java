package es.udc.psi_2025_26.p24vazreg;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import es.udc.psi_2025_26.p24vazreg.databinding.ItemBinding;


public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.MyViewHolder> {
    public interface OnItemClickListener {
        public void onClick(View view, int position);
    }

    private  OnItemClickListener clickListener;
    public  void setClickListener(OnItemClickListener itemClickListener) {
        clickListener = itemClickListener;
        //Log.d("_TAG", " fijado el listener");
    }

    private final ArrayList<Article> mDataset;
    public  class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        private ItemBinding binding;

        public MyViewHolder(ItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }
        public void bind(Article article) {
            binding.itemTitle.setText(article.getTitle());
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.onClick(view, getAdapterPosition());
            }
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

    public Article getItem(int i) {
        return mDataset.get(i);
    }
}