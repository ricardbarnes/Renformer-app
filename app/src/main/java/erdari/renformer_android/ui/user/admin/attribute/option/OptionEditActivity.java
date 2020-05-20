package erdari.renformer_android.ui.user.admin.attribute.option;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import erdari.renformer_android.R;
import erdari.renformer_android.config.Key;
import erdari.renformer_android.data.auxiliary.ReformIdObjectBundle;
import erdari.renformer_android.data.model.Attribute;
import erdari.renformer_android.data.model.Option;
import erdari.renformer_android.data.model.relational.RelationalOption;
import erdari.renformer_android.data.repository.AttributeRepository;
import erdari.renformer_android.data.repository.OptionRepository;
import erdari.renformer_android.ui.user.admin.attribute.AttributeEditActivity;

import static android.view.View.GONE;

/**
 * Holds the attribute edition GUI.
 *
 * @author Ricard Pinilla Barnes
 */
public class OptionEditActivity extends AppCompatActivity {

    private boolean mIsEditMode;

    private long mOptionId;
    private Option mOption;
    private OptionRepository mOptionRepo;
    private AttributeRepository mAttributeRepo;
    private Attribute mParentAttribute;

    private TextView mIdBox;
    private EditText mNameBox;
    private EditText mPriceBox;
    private TextView mDeleteText;

    private Button mEditButton;
    private ProgressBar mProgressBar;

    private InputMethodManager mImm;

    private ReformIdObjectBundle mBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_option);

        setTitle(getString(R.string.option_title));

        mProgressBar = findViewById(R.id.categoriesProgressBar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mIdBox = findViewById(R.id.userIdText);
        mNameBox = findViewById(R.id.nameBox);
        mPriceBox = findViewById(R.id.priceBox);
        mEditButton = findViewById(R.id.buttonEdit);
        mDeleteText = findViewById(R.id.deleteOptionLabel);

        mOptionRepo = new OptionRepository(this);
        mAttributeRepo = new AttributeRepository(this);

        long reformTypeId = getIntent().getLongExtra(Key.REFORM_TYPE_ID, 0);
        long attributeId = getIntent().getLongExtra(Key.ATTRIBUTE_ID, 0);
        mOptionId = getIntent().getLongExtra(Key.OPTION_ID, 0);

        mBundle = new ReformIdObjectBundle();
        mBundle.setReformTypeId(reformTypeId);
        mBundle.setAttributeId(attributeId);

        mOptionRepo.findLocalOptionById(mOptionId).observe(this, option -> {
            if (option != null) {
                mOption = option;
                mOptionRepo.deleteLocalOption(option); // Cleans the room and free some space
                mIdBox.setText(String.valueOf(mOption.getId()));
                mNameBox.setText(mOption.getName());
                mPriceBox.setText(String.valueOf(mOption.getPrice()));
            }

            mProgressBar.setVisibility(GONE);
        });

        mAttributeRepo.findAttributeById(attributeId).observe(this, attribute -> {
            if (attribute != null) {
                mParentAttribute = attribute;
            } else {
                Toast.makeText(this, "Some error happened", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    /**
     * Stands for the home button action.
     *
     * @param item A menu item.
     * @return always true.
     */
    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            goBack();
        }
        return true;
    }

    /**
     * Toggles the edit/save button.
     *
     * @param view The triggered view.
     * @author Ricard Pinilla Barnes
     */
    public void toggleSave(View view) {
        mNameBox.setEnabled(!mIsEditMode);
        mPriceBox.setEnabled(!mIsEditMode);
        if (mIsEditMode) {
            applyChanges();
            mEditButton.setText(R.string.edit);
            mImm.hideSoftInputFromWindow(mNameBox.getWindowToken(), 0);
            mDeleteText.setVisibility(View.VISIBLE);
        } else {
            mEditButton.setText(R.string.save);
            mImm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            mDeleteText.setVisibility(View.GONE);
        }
        mIsEditMode = !mIsEditMode;
    }

    /**
     * Starts the user change application event chain.
     *
     * @author Ricard Pinilla Barnes
     */
    private void applyChanges() {
        mProgressBar.setVisibility(View.VISIBLE);

        String name = mNameBox.getText().toString().trim();
        String priceString = mPriceBox.getText().toString().trim();

        if (!name.isEmpty() && !priceString.isEmpty()) {
            float price = Float.parseFloat(priceString);

            Option option = new Option();
            option.setId(mOptionId);
            option.setName(name);
            option.setPrice(price);

            RelationalOption relationalOption = new RelationalOption(option);
            relationalOption.setAttributeId(mBundle.getAttributeId());

            mOptionRepo.patchOption(relationalOption).observe(this, isPatched -> {
                if (isPatched) {
                    mOption = relationalOption;

                    Toast.makeText(
                            this,
                            "Option successfully updated",
                            Toast.LENGTH_SHORT
                    ).show();
                }

                mProgressBar.setVisibility(GONE);
            });

        } else {
            Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Handles the edited option deletion.
     *
     * @param view The triggered view.
     * @author Ricard Pinilla Barnes
     */
    public void deleteOption(View view) {
        if (mParentAttribute.getOptions().size() > 1) {
            new AlertDialog.Builder(this)
                    .setTitle("Delete option")
                    .setMessage("Are you sure that you want to proceed?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        mProgressBar.setVisibility(View.VISIBLE);

                        mOptionRepo.deleteOption(mOption).observe(this, isDeleted -> {
                            mOptionRepo.deleteLocalOption(mOption);

                            Toast.makeText(
                                    this,
                                    "Option successfully deleted.",
                                    Toast.LENGTH_SHORT
                            ).show();

                            Intent intent = new Intent(this, AttributeEditActivity.class);
                            intent.putExtra(Key.REFORM_TYPE_ID, mBundle.getReformTypeId());
                            intent.putExtra(Key.ATTRIBUTE_ID, mBundle.getAttributeId());
                            startActivity(intent);
                            finish();
                        });

                        mProgressBar.setVisibility(GONE);
                    }).setNegativeButton(android.R.string.no, null).show();
        } else {
            Toast.makeText(
                    this,
                    "At least one option is needed. Delete the attribute instead.",
                    Toast.LENGTH_LONG
            ).show();
        }

    }

    /**
     * Turns the user back to the attribute edition activity.
     *
     * @author Ricard Pinilla Barnes
     */
    private void goBack() {
        mOptionRepo.deleteLocalOption(mOption); // Cleans the room and free some space
        Intent intent = new Intent(this, AttributeEditActivity.class);
        intent.putExtra(Key.REFORM_TYPE_ID, mBundle.getReformTypeId());
        intent.putExtra(Key.ATTRIBUTE_ID, mBundle.getAttributeId());
        startActivity(intent);
        finish();
    }

}
