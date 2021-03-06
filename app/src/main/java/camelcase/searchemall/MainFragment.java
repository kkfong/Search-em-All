package camelcase.searchemall;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

public class MainFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private final String TAG = MainFragment.class.getSimpleName();
    MainFragmentListener mainFragmentListener;
    private EditText mSearchBox;
    private Spinner mSearchScopeSpinner;
    private ImageButton mSearchButton;
    private String mStringSearchScope;
    private String mSearchQuery;
    private TextView.OnEditorActionListener myOnEditListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN) {
                mSearchQuery = mSearchBox.getText().toString();
                String searchScope = mStringSearchScope;
                mainFragmentListener.getSearchInfo(mSearchQuery, searchScope);
                return true;
            } else return false;
        }
    };

    public MainFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mainFragmentListener = (MainFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        initViews(view);
        initSpinner();
        listenButtonEvent();
        mSearchBox.setOnEditorActionListener(myOnEditListener);
        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mStringSearchScope = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private boolean initSpinner() {

        ArrayAdapter<CharSequence> mSpinnerAdapter = ArrayAdapter.createFromResource(
                getActivity(),
                R.array.spinner_items,
                android.R.layout.simple_spinner_item);

        mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        mSearchScopeSpinner.setAdapter(mSpinnerAdapter);
        mSearchScopeSpinner.setOnItemSelectedListener(this);
        return true;
    }

    private boolean listenButtonEvent() {
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchQuery = mSearchBox.getText().toString();
                String searchScope = mStringSearchScope;
                mainFragmentListener.getSearchInfo(mSearchQuery, searchScope);
            }
        });
        return true;
    }

    private void initViews(View view) {
        mSearchBox = getEditText(view, R.id.main_fragment_search_box);
        mSearchScopeSpinner = getSpinner(view, R.id.main_fragment_search_scope);
        mSearchButton = getImageButton(view, R.id.main_fragment_search_button);
    }

    private ImageButton getImageButton(View view, int id) {
        return (ImageButton) view.findViewById(id);
    }

    private Spinner getSpinner(View view, int id) {
        return (Spinner) view.findViewById(id);
    }

    private EditText getEditText(View view, int id) {
        return (EditText) view.findViewById(id);
    }

    public interface MainFragmentListener {
        void getSearchInfo(String query, String Scope);
    }
}
