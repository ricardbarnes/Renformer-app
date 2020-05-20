package erdari.renformer_android.ui.user.admin.category;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import erdari.renformer_android.ui.user.admin.reformtype.ReformTypeDetailActivity;
import erdari.renformer_android.R;
import erdari.renformer_android.data.model.relational.RelationalCategory;
import erdari.renformer_android.config.Key;
import erdari.renformer_android.data.model.Category;
import erdari.renformer_android.data.repository.CategoryRepository;

/**
 * Holds the category edition GUI.
 *
 * @author Ricard Pinilla Barnes
 */
public class CategoryEditActivity extends AppCompatActivity {

    private boolean mIsEditMode;

    private long mReformTypeId;
    private Category mCategory;

    private TextView mIdBox;
    private EditText mNameBox;
    private EditText mFactorBox;

    private Button mSaveButton;
    private ProgressBar mProgressBar;

    private CategoryRepository mCategoryRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        setTitle(getString(R.string.category_title));

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mProgressBar = findViewById(R.id.categoriesProgressBar);
        mIdBox = findViewById(R.id.userIdText);
        mNameBox = findViewById(R.id.nameBox);
        mFactorBox = findViewById(R.id.factorBox);
        mSaveButton = findViewById(R.id.saveButton);

        mReformTypeId = getIntent().getLongExtra(Key.REFORM_TYPE_ID, 0);

        mCategoryRepo = new CategoryRepository(this);
        mCategoryRepo.findLocalCategoryById(getIntent().getLongExtra(Key.CATEGORY_ID, 0))
                .observe(this, category -> {
                    if (category != null) {
                        mCategory = category;
                        mCategoryRepo.deleteLocalCategory(category);
                        mIdBox.setText(String.valueOf(mCategory.getId()));
                        mNameBox.setText(mCategory.getName());
                        mFactorBox.setText(String.valueOf(mCategory.getFactor()));
                    }

                    mProgressBar.setVisibility(View.GONE);
                });
    }

    /**
     * Stands for the home button action. Returns to the user (by role) list instead of the
     * AdminActivity or whatever static context.
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
    public void toggleSave(View view) {
        mNameBox.setEnabled(!mIsEditMode);
        mFactorBox.setEnabled(!mIsEditMode);
        if (mIsEditMode) {
            applyChanges();
            mSaveButton.setText(R.string.edit);
        } else {
            mSaveButton.setText(R.string.save);
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
        float factor = Float.parseFloat(mFactorBox.getText().toString().trim());

        if (!name.isEmpty()) {
            Category category = new Category();
            category.setId(mCategory.getId());
            category.setName(name);
            category.setFactor(factor);

            //Non-OOP adaption
            RelationalCategory relationalCategory = new RelationalCategory(category);
            relationalCategory.setReformTypeId(mReformTypeId);

            mCategoryRepo.patchCategory(relationalCategory).observe(this, isPatched -> {
                if (isPatched) {
                    mCategory = relationalCategory;
                    Toast.makeText(this, "Category successfully updated.", Toast.LENGTH_SHORT).show();
                }
                mNameBox.setText(mCategory.getName());
                mFactorBox.setText(String.valueOf(mCategory.getFactor()));
                mProgressBar.setVisibility(View.GONE);
            });
        } else {
            Toast.makeText(this, "Fields cannot be empty.", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Handles the edited category deletion.
     *
     * @param view The triggered view.
     * @author Ricard Pinilla Barnes
     */
    public void deleteCategory(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Delete category")
                .setMessage("Are you sure that you want to proceed?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    mProgressBar.setVisibility(View.VISIBLE);

                    mCategoryRepo.deleteCategory(mCategory).observe(this, isDeleted -> {
                        Toast.makeText(this, "Account successfully deleted.", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(this, ReformTypeDetailActivity.class);
                        intent.putExtra(Key.REFORM_TYPE_ID, mReformTypeId);
                        startActivity(intent);

                        finish();
                    });

                    mProgressBar.setVisibility(View.GONE);
                }).setNegativeButton(android.R.string.no, null).show();
    }

    private void goBack() {
        Intent intent = new Intent(this, ReformTypeDetailActivity.class);
        intent.putExtra(Key.REFORM_TYPE_ID, mReformTypeId);
        startActivity(intent);
        finish();
    }
}
