package camelcase.searchemall;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.Serializable;

public class WebViewFragment extends Fragment implements Serializable {

    private final String TAG = WebViewFragment.class.getSimpleName();
    WebviewFragmentListener webviewFragmentListener;
    private String mSearchQuery = "";
    private String mUrl = "";
    private boolean mJsEnable = false;
    private FloatingActionButton fab;
    private WebView mWebView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public WebViewFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        webviewFragmentListener = (WebviewFragmentListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.webview_fragment, container, false);
        mWebView = (WebView) v.findViewById(R.id.webview_fragment_webview);
        final ProgressBar progressBar = (ProgressBar) v.findViewById(R.id.webview_fragment_progressbar);

        fab = (FloatingActionButton) v.findViewById(R.id.webview_fragment_goBack);
        fab.setOnClickListener(myListener);

        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mWebView.reload();
            }
        });

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(mJsEnable);

        mWebView.loadUrl(mUrl + mSearchQuery);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                mSwipeRefreshLayout.setRefreshing(false);
                if (mWebView.canGoBack()) webviewFragmentListener.getCurrentUrl(url);
                else webviewFragmentListener.getCurrentUrl("");
            }
        });
        WebView.setWebContentsDebuggingEnabled(false);
        return v;
    }

    // method to get search parameters from other fragment/activity
    public void getSearchParams(String query, String url, boolean isJsEnable) {
        mSearchQuery = query;
        mUrl = url;
        mJsEnable = isJsEnable;
    }

    public interface WebviewFragmentListener {
        void getCurrentUrl(String url);
    }

    private View.OnClickListener myListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
            } else {
                Toast.makeText(getContext(), "Can not go back", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
