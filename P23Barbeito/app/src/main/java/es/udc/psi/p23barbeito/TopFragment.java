package es.udc.psi.p23barbeito;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import es.udc.psi.p23barbeito.databinding.FragmentTopBinding;

public class TopFragment extends Fragment {

    public interface TopListener {
        void onShowText(String text);
        void onShowUrl(String url);
        void onClear();
    }

    private FragmentTopBinding binding;
    private TopListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (TopListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTopBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.butA.setOnClickListener(v -> listener.onShowText("A"));
        binding.butB.setOnClickListener(v -> listener.onShowText("B"));
        binding.butUrl.setOnClickListener(v -> listener.onShowUrl(binding.etUrl.getText().toString()));
        binding.butClear.setOnClickListener(v -> listener.onClear());
    }

    public void setClearEnabled(boolean enabled) {
        binding.butClear.setEnabled(enabled);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}