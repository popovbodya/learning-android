package ru.popov.bodya.gitwatcher;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddNewUserFragment extends Fragment {

    private Button addButton;
    private EditText editText;
    private UserStorage storage;
    private OnButtonClickListener onButtonClickListener;

    public static AddNewUserFragment newInstance(UserStorage storage) {
        AddNewUserFragment userListFragment = new AddNewUserFragment();
        userListFragment.storage = storage;
        return userListFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onButtonClickListener = (OnButtonClickListener) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onButtonClickListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_text, container, false);
        addButton = (Button) view.findViewById(R.id.add_new_user_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storage.addNewUser(editText.getText().toString());
                notifyListener();
            }
        });

        editText = (EditText) view.findViewById(R.id.edit_text_username);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    addButton.setEnabled(false);
                } else {
                    addButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        addButton.setEnabled(false);

        return view;
    }

    public interface OnButtonClickListener {
        void onClick();
    }

    private void notifyListener() {
        onButtonClickListener.onClick();
    }

}
