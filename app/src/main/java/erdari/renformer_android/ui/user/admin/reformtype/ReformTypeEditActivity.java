package erdari.renformer_android.ui.user.admin.reformtype;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import erdari.renformer_android.R;
import erdari.renformer_android.config.Key;
import erdari.renformer_android.data.model.ReformType;
import erdari.renformer_android.data.repository.ReformTypeRepository;

import static android.view.View.GONE;

/**
 * Holds the reform type edition GUI.
 *
 * @author Ricard Pinilla Barnes
 */
public class ReformTypeEditActivity extends AppCompatActivity {

    private boolean mIsEditMode;
    private boolean mIsError;

    private long mReformTypeId;
    private ReformType mReformType;
    private ReformTypeRepository mReformTypeRepo;

    private TextView mIdText;
    private TextView mNameBox;
    private TextView mPriceBox;
    private TextView mDeleteText;

    private Button mEditButton;
    private ProgressBar mProgressBar;

    private InputMethodManager mImm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reformtype);

        setTitle(getString(R.string.reform_type));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mProgressBar = findViewById(R.id.categoriesProgressBar);
        mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mIdText = findViewById(R.id.userIdText);
        mNameBox = findViewById(R.id.nameBox);
        mPriceBox = findViewById(R.id.meterPriceBox);
        mEditButton = findViewById(R.id.buttonEdit);
        mDeleteText = findViewById(R.id.deleteText);

        mReformTypeId = getIntent().getLongExtra(Key.REFORM_TYPE_ID, 0);

        mReformTypeRepo = new ReformTypeRepository(this);
        mReformTypeRepo.findLocalReformTypeById(mReformTypeId).observe(
                this, reformType -> {
                    if (reformType != null) {
                        mReformType = reformType;
                        mIdText.setText(String.valueOf(mReformType.getId()));
                        mNameBox.setText(mReformType.getName());
                        mPriceBox.setText(String.valueOf(mReformType.getPrice()));
                    }
                    mProgressBar.setVisibility(GONE);
                });
    }

    /**
     * Stands for the home button action. Returns to the parent reform type.
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

    @Override
    public void onBackPressed() {
        goBack();
    }

    /**
     * Toggles the edit/save button.
     *
     * @param view The triggered view.
     * @author Ricard Pinilla Barnes
     */
    public void triggerSave(View view) {
        toggleSave();
    }

    /**
     * Starts the reform type change application event chain.
     *
     * @author Ricard Pinilla Barnes
     */
    private void applyChanges() {
        mProgressBar.setVisibility(View.VISIBLE);

        String name = mNameBox.getText().toString().trim();
        String priceStr = mPriceBox.getText().toString().trim();

        if (!name.isEmpty() /* && !priceStr.isEmpty() */) {
            float price = Float.parseFloat(priceStr);
            ReformType reformType = new ReformType();
            reformType.setId(mReformTypeId);
            reformType.setName(name);
            reformType.setPrice(price);

            mReformTypeRepo.patchReformType(reformType).observe(this, isPatched -> {
                if (isPatched) {
                    mReformType.setName(name);
                    mReformType.setPrice(price);
                    mReformTypeRepo.insertLocalReformType(mReformType);
                    Toast.makeText(this, "Reform type successfully updated.", Toast.LENGTH_SHORT).show();
                } else {
                    mIsError = true;
                    mIsEditMode = !mIsEditMode;
                    toggleSave();
                    mNameBox.setText(mReformType.getName());
                    mPriceBox.setText(String.valueOf(mReformType.getPrice()));
                }
            });
        }

        mProgressBar.setVisibility(GONE);
    }

    /**
     * Handles the edited attribute deletion.
     *
     * @param view The triggered view.
     * @author Ricard Pinilla Barnes
     */
    public void deleteReform(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Delete reform type")
                .setMessage("Are you sure that you want to proceed?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    mProgressBar.setVisibility(View.VISIBLE);

                    mReformTypeRepo.deleteReformType(mReformType).observe(
                            this, isDeleted -> {
                                if (isDeleted) {
                                    Toast.makeText(
                                            this,
                                            "Reform type successfully deleted.",
                                            Toast.LENGTH_SHORT
                                    ).show();

                                    Intent intent = new Intent(this, ReformTypeListActivity.class);
                                    startActivity(intent);

                                    finish();
                                }
                            });

                    mProgressBar.setVisibility(GONE);
                }).setNegativeButton(android.R.string.no, null).show();
    }

    /**
     * Handles the actions to return to the previous screen.
     * <p>
     * New intent is to ensure the refresh of the previous screen content.
     *
     * @author Ricard Pinilla Barnes
     */
    private void goBack() {
        Intent intent = new Intent(this, ReformTypeDetailActivity.class);
        intent.putExtra(Key.REFORM_TYPE_ID, mReformTypeId);
        intent.putExtra(Key.REFORM_TYPE_NAME, mReformType.getName());
        startActivity(intent);
        finish();
    }

    /**
     * Avoids the view parameter for the triggerSave method, so it can be reused out of a view.
     *
     * @author Ricard Pinilla Barnes
     */
    private void toggleSave() {
        mNameBox.setEnabled(!mIsEditMode);
        mPriceBox.setEnabled(!mIsEditMode);
        if (mIsEditMode) {
            if (!mIsError) {
                applyChanges();
            } else {
                mIsError = false;
            }
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

}
