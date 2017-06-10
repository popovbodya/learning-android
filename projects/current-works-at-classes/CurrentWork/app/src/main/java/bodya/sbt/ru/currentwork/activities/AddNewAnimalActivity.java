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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import bodya.sbt.ru.currentwork.Animal;
import bodya.sbt.ru.currentwork.AnimalStorage;
import bodya.sbt.ru.currentwork.R;
import bodya.sbt.ru.currentwork.interfaces.AnimalsStorageProvider;

public class AddNewAnimalActivity extends AppCompatActivity {

    private static final String ANIMAL_KEY = "animal_key";

    private TextInputEditText ageEditText;
    private TextInputEditText heightEditText;
    private TextInputEditText weightEditText;
    private TextInputEditText nameEditText;
    private TextInputEditText[] editTexts;
    private Button addButton;

    private Animal cachedAnimal;
    private AnimalStorage animalStorage;
    private boolean updateMode = false;

    public static Intent newIntent(Context context) {
        return new Intent(context, AddNewAnimalActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_animal_layout);

        AnimalsStorageProvider animalsStorageProvider = (AnimalsStorageProvider) getApplication();
        animalStorage = animalsStorageProvider.getAnimalsStorage();

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

        if (getIntent().getExtras() != null) {
            addButton.setText(getResources().getString(R.string.update_animal_upper_case));
            getDataFromExtras();
            updateMode = true;
        }
    }

    private void updateCachedAnimal() {
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
        if (cachedAnimal == null) {
            createAnimal();
        }
        updateCachedAnimal();
        animalStorage.updateAnimal(cachedAnimal);
        updateMode = false;
        finish();
    }

    private void createAnimal() {
        String name = nameEditText.getText().toString();
        int age = Integer.valueOf(ageEditText.getText().toString());
        int weight = Integer.valueOf(weightEditText.getText().toString());
        int height = Integer.valueOf(heightEditText.getText().toString());

        cachedAnimal = new Animal(name, age, weight, height);
        animalStorage.addAnimal(cachedAnimal);
        finish();
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
                if (TextUtils.isEmpty(editText.getText())) {
                    setEnabled = false;
                    break;
                }
            }
            addButton.setEnabled(setEnabled);
        }
    }

    private void getDataFromExtras() {
        Bundle bundle = getIntent().getExtras();
        cachedAnimal = bundle.getParcelable(ANIMAL_KEY);
        if (cachedAnimal == null) {
            return;
        }
        nameEditText.setText(cachedAnimal.getName());
        ageEditText.setText(String.valueOf(cachedAnimal.getAge()));
        weightEditText.setText(String.valueOf(cachedAnimal.getWeight()));
        heightEditText.setText(String.valueOf(cachedAnimal.getHeight()));
    }
}
