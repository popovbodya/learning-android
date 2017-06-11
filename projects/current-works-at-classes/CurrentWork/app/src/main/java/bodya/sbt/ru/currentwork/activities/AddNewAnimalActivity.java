package bodya.sbt.ru.currentwork.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import bodya.sbt.ru.currentwork.Animal;
import bodya.sbt.ru.currentwork.R;
import bodya.sbt.ru.currentwork.async.DataBaseLoaderFunctions;
import bodya.sbt.ru.currentwork.async.DataBaseWorker;
import bodya.sbt.ru.currentwork.interfaces.DataBaseLoaderProvider;

public class AddNewAnimalActivity extends AppCompatActivity {

    private static final String ANIMAL_KEY = "animal_key";
    private static final String TAG = "AddNewAnimalActivity";
    private static final byte MAX_EDIT_TEXT_LENGTH = 9;

    private TextInputEditText ageEditText;
    private TextInputEditText heightEditText;
    private TextInputEditText weightEditText;
    private TextInputEditText nameEditText;
    private TextInputEditText[] editTexts;
    private Button addButton;

    private Animal cachedAnimal;
    private DataBaseWorker dataBaseWorker;
    private boolean updateMode = false;

    public static Intent newIntent(Context context) {
        return new Intent(context, AddNewAnimalActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_animal_layout);

        DataBaseLoaderProvider dataBaseLoaderProvider = (DataBaseLoaderProvider) getApplication();
        dataBaseWorker = dataBaseLoaderProvider.getDataBaseWorker();

        nameEditText = (TextInputEditText) findViewById(R.id.name_edit_text);
        ageEditText = (TextInputEditText) findViewById(R.id.age_edit_text);
        weightEditText = (TextInputEditText) findViewById(R.id.weight_edit_text);
        heightEditText = (TextInputEditText) findViewById(R.id.height_edit_text);
        addButton = (Button) findViewById(R.id.add_animal_button);

        editTexts = new TextInputEditText[]{ageEditText, nameEditText, weightEditText, heightEditText};
        for (TextInputEditText editText : editTexts) {
            editText.addTextChangedListener(new EditTextWatcherImpl());
        }

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updateMode) {
                    updateAnimal();
                } else {
                    createAnimal();
                }
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            addButton.setText(getResources().getString(R.string.update_animal_upper_case));
            getDataFromExtras(extras);
            updateMode = true;
        }
    }


    private void updateCachedAnimal() {
        Log.e(TAG, "updateCachedAnimal");
        String name = nameEditText.getText().toString();
        int age = Integer.valueOf(ageEditText.getText().toString());
        int weight = Integer.valueOf(weightEditText.getText().toString());
        int height = Integer.valueOf(heightEditText.getText().toString());

        cachedAnimal.setName(name);
        cachedAnimal.setAge(age);
        cachedAnimal.setWeight(weight);
        cachedAnimal.setHeight(height);

    }

    private void updateAnimal() {
        Log.e(TAG, "updateAnimal");
        updateCachedAnimal();
        dataBaseWorker.queueTask(DataBaseLoaderFunctions.UPDATE_USER, cachedAnimal);
        updateMode = false;
        finish();
    }

    private void createAnimal() {
        Log.e(TAG, "createAnimal");
        String name = nameEditText.getText().toString();
        int age = Integer.valueOf(ageEditText.getText().toString());
        int weight = Integer.valueOf(weightEditText.getText().toString());
        int height = Integer.valueOf(heightEditText.getText().toString());

        cachedAnimal = new Animal(name, age, weight, height);
        dataBaseWorker.queueTask(DataBaseLoaderFunctions.CREATE_USER, cachedAnimal);
        finish();
    }


    private void getDataFromExtras(Bundle bundle) {
        cachedAnimal = (Animal) bundle.getSerializable(ANIMAL_KEY);
        if (cachedAnimal == null) {
            return;
        }
        nameEditText.setText(cachedAnimal.getName());
        ageEditText.setText(String.valueOf(cachedAnimal.getAge()));
        weightEditText.setText(String.valueOf(cachedAnimal.getWeight()));
        heightEditText.setText(String.valueOf(cachedAnimal.getHeight()));
    }

    private class EditTextWatcherImpl implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            boolean setEnabled = true;
            for (EditText editText : editTexts) {
                Editable text = editText.getText();
                if (TextUtils.isEmpty(text) || text.length() > MAX_EDIT_TEXT_LENGTH) {
                    setEnabled = false;
                    break;
                }
            }
            addButton.setEnabled(setEnabled);
        }
    }
}
