package es.udc.psi.p23barbeito;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import es.udc.psi.p23barbeito.databinding.FragmentTextBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TextFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TextFragment extends Fragment {

    public interface TextListener {
        void onEnableToggled(boolean clearEnabled);
    }

    private static final String ARG_TEXT = "text";
    private FragmentTextBinding binding;
    private TextListener listener;
    private boolean clearEnabled = true;

    public static TextFragment newInstance(String text) {
        TextFragment f = new TextFragment();
        Bundle b = new Bundle();
        b.putString(ARG_TEXT, text);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (TextListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTextBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.tvText.setText(requireArguments().getString(ARG_TEXT));
        binding.butEnable.setOnClickListener(v -> {
            clearEnabled = !clearEnabled;
            listener.onEnableToggled(clearEnabled);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}