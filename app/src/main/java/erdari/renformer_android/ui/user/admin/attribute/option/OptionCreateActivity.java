package erdari.renformer_android.ui.user.admin.attribute.option;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import erdari.renformer_android.R;
import erdari.renformer_android.data.model.relational.RelationalOption;
import erdari.renformer_android.config.Key;
import erdari.renformer_android.data.model.Option;
import erdari.renformer_android.data.repository.OptionRepository;
import erdari.renformer_android.ui.user.admin.attribute.AttributeEditActivity;

/**
 * Holds the option creation GUI.
 *
 * @author Ricard Pinilla Barnes
 */
public class OptionCreateActivity extends AppCompatActivity {

    private ProgressBar mProgressBar;

    private EditText mNameBox;
    private EditText mPriceBox;

    private long mReformTypeId;
    private long mAttributeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_create);

        mProgressBar = findViewById(R.id.categoriesProgressBar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mNameBox = findViewById(R.id.nameBox);
        mPriceBox = findViewById(R.id.priceBox);

        mReformTypeId = getIntent().getLongExtra(Key.REFORM_TYPE_ID, 0);
        mAttributeId = getIntent().getLongExtra(Key.ATTRIBUTE_ID, 0);

        mProgressBar.setVisibility(View.GONE);
    }

    /**
     * Stands for the home button action. Returns to the Reform type (by id) list.
     *
     * @param item A menu item.
     * @return always true.
     * @author Ricard Pinilla Barnes
     */
    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {

            returnToAttributeEdit();

            finish();
        }
        return true;
    }

    /**
     * Triggers the user creation event chain.
     *
     * @param view The triggered view.
     * @author Ricard Pinilla Barnes
     */
    public void createOption(View view) {
        mProgressBar.setVisibility(View.VISIBLE);

        String name = mNameBox.getText().toString().trim();
        String priceString = mPriceBox.getText().toString().trim();

        if (!name.isEmpty() && !priceString.isEmpty()) {
            float price = Float.parseFloat(priceString);

            Option option = new Option();
            option.setName(name);
            option.setPrice(price);

            RelationalOption relationalOption = new RelationalOption(option);
            relationalOption.setAttributeId(mAttributeId);

            OptionRepository optionRepo = new OptionRepository(this);
            optionRepo.createOption(relationalOption).observe(this, isCreated -> {
                if (isCreated) {
                    Toast.makeText(
                            this,
                            "Option successfully created.",
                            Toast.LENGTH_SHORT
                    ).show();

                    returnToAttributeEdit();
                }
            });

        } else {
            Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
        }

        mProgressBar.setVisibility(View.GONE);
    }

    /**
     * Starts an intent to return to the attribute edit GUI with the proper extras.
     *
     * @author Ricard Pinilla Barnes
     */
    private void returnToAttributeEdit() {
        Intent intent = new Intent(this, AttributeEditActivity.class);
        intent.putExtra(Key.REFORM_TYPE_ID, mReformTypeId);
        intent.putExtra(Key.ATTRIBUTE_ID, mAttributeId);
        startActivity(intent);
        finish();
    }
}
