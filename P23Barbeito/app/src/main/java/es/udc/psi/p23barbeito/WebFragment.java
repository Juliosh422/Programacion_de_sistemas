package es.udc.psi.p23barbeito;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.fragment.app.Fragment;

public class WebFragment extends Fragment {

    private static final String ARG_URL = "url";

    public static WebFragment newInstance(String url) {
        WebFragment f = new WebFragment();
        Bundle b = new Bundle();
        b.putString(ARG_URL, url);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_web, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        WebView myWebView = (WebView) view.findViewById(R.id.webview);
        myWebView.loadUrl(requireArguments().getString(ARG_URL));
    }
}