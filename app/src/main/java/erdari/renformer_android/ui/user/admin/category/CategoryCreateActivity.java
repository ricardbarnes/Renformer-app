package erdari.renformer_android.ui.user.admin.category;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
 * Holds the category creation GUI
 *
 * @author Ricard Pinilla Barnes
 */
public class CategoryCreateActivity extends AppCompatActivity {

    private EditText mNameBox;
    private EditText mFactorBox;

    private long mReformTypeId;

    private InputMethodManager mImm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_create);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (mImm != null) {
            mImm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }

        mNameBox = findViewById(R.id.nameBox);
        mFactorBox = findViewById(R.id.factorBox);

        mReformTypeId = getIntent().getLongExtra(Key.REFORM_TYPE_ID, 0);

        mFactorBox.setText(String.valueOf(1));
    }

    /**
     * Stands for the home button action. Returns to the Reform type (by id) list.
     *
     * @param item A menu item.
     * @return always true.
     */
    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {

            Intent intent = new Intent(this, ReformTypeDetailActivity.class);
            intent.putExtra(Key.REFORM_TYPE_ID, mReformTypeId);
            startActivity(intent);

            finish();
        }
        return true;
    }

    /**
     * Triggers the category creation.
     *
     * @param view The triggered view.
     * @author Ricard Pinilla Barnes
     */
    public void createCategory(View view) {
        String name = mNameBox.getText().toString().trim();
        String factorString = mFactorBox.getText().toString().trim();
        float factor = Float.parseFloat(factorString);

        if (!name.isEmpty() && !factorString.isEmpty()) {
            Category category = new Category();
            category.setName(name);
            category.setFactor(factor);
            RelationalCategory relationalCategory = new RelationalCategory(category);
            relationalCategory.setReformTypeId(mReformTypeId); // Non-OOP adaption.

            CategoryRepository categoryRepo = new CategoryRepository(this);
            categoryRepo.createCategory(relationalCategory).observe(this, isCreated -> {
                if (isCreated) {
                    Toast.makeText(
                            this,
                            "Category has been successfully created.",
                            Toast.LENGTH_SHORT
                    ).show();

                    Intent intent = new Intent(this, ReformTypeDetailActivity.class);
                    intent.putExtra(Key.REFORM_TYPE_ID, mReformTypeId);
                    startActivity(intent);

                    finish();
                }
            });
        } else {
            Toast.makeText(this, "Fields cannot be empty.", Toast.LENGTH_SHORT).show();
        }

    }
}
