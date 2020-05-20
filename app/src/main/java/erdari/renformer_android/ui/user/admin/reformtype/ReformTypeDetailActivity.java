package erdari.renformer_android.ui.user.admin.reformtype;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import erdari.renformer_android.R;
import erdari.renformer_android.config.Key;
import erdari.renformer_android.data.model.Attribute;
import erdari.renformer_android.data.model.Category;
import erdari.renformer_android.data.model.ReformType;
import erdari.renformer_android.data.repository.AttributeRepository;
import erdari.renformer_android.data.repository.CategoryRepository;
import erdari.renformer_android.data.repository.ReformTypeRepository;
import erdari.renformer_android.ui.user.admin.attribute.AttributeCreateActivity;
import erdari.renformer_android.ui.user.admin.attribute.AttributeListAdapter;
import erdari.renformer_android.ui.user.admin.category.CategoryCreateActivity;
import erdari.renformer_android.ui.user.admin.category.CategoryListAdapter;

/**
 * Holds the reform type GUI
 *
 * @author Ricard Pinilla Barnes
 */
public class ReformTypeDetailActivity extends AppCompatActivity {

    private long mReformTypeId;
    private ReformType mReformType;
    private ReformTypeRepository mReformTypeRepo;

    private RecyclerView mCategoryRecycler;
    private CategoryListAdapter mCategoryAdapter;
    private CategoryRepository mCategoryRepo;
    private List<Category> mCategories;

    private RecyclerView mAttributeRecycler;
    private AttributeListAdapter mAttributeAdapter;
    private AttributeRepository mAttributeRepo;
    private List<Attribute> mAttributes;

    private TextView mTitleText;
    private TextView mNoCategories;
    private TextView mNoAttributes;

    private ProgressBar mCategoriesProgressBar;
    private ProgressBar mAttributesProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reform_type_detail);

        setTitle(getString(R.string.reform_type));

        mCategoriesProgressBar = findViewById(R.id.categoriesProgressBar);
        mAttributesProgressBar = findViewById(R.id.attributesProgressBar);
        mTitleText = findViewById(R.id.titleText);
        mNoCategories = findViewById(R.id.noCategoriesText);
        mNoAttributes = findViewById(R.id.noAttributesText);

        mNoCategories.setVisibility(View.INVISIBLE);
        mNoAttributes.setVisibility(View.INVISIBLE);

        mReformTypeId = getIntent().getLongExtra(Key.REFORM_TYPE_ID, 0);

        mReformTypeRepo = new ReformTypeRepository(this);
        mReformTypeRepo.findReformTypeById(mReformTypeId).observe(this, reformType -> {
            if (reformType != null) {
                mReformType = reformType;
                setUpContent();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.reform_nav_menu, menu);
        return true;
    }

    /**
     * If the chosen item is "Reform settings" the user is leaded to the reform type edit activity.
     * If the chosen item is the Home arrow, the user is leaded back.
     *
     * @param item The selected item.
     * @return A boolean. See the super documentation.
     * @author Ricard Pinilla Barnes
     */
    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        if (item.getItemId() == R.id.reform_settings) {
            Intent intent = new Intent(getApplicationContext(), ReformTypeEditActivity.class);
            mReformTypeRepo.insertLocalReformType(mReformType);
            intent.putExtra(Key.REFORM_TYPE_ID, mReformTypeId);
            startActivity(intent);
            finish();
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            goBack();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    /**
     * Sets up the view content. Just to shorten the onCreate method.
     *
     * @author Ricard Pinilla Barnes
     */
    private void setUpContent() {
        mTitleText.setText(mReformType.getName());

        mCategories = new ArrayList<>();
        mCategoryRecycler = findViewById(R.id.categoryRecycler);
        mCategoryRecycler.setLayoutManager(new LinearLayoutManager(this));
        mCategoryAdapter = new CategoryListAdapter(this, mReformType, mCategories);
        mCategoryRecycler.setAdapter(mCategoryAdapter);
        mCategoryRepo = new CategoryRepository(this);
        mCategoryRepo.findAllCategoriesByReformTypeId(mReformType.getId()).observe(
                this, categories -> {
                    if (categories != null && categories.size() != 0) {
                        mNoCategories.setVisibility(View.GONE);
                        mCategories.addAll(categories);
                        mCategoryAdapter.notifyDataSetChanged();
                    } else {
                        mNoCategories.setVisibility(View.VISIBLE);
                    }

                    mCategoriesProgressBar.setVisibility(View.GONE);
                });

        mAttributes = new ArrayList<>();
        mAttributeRecycler = findViewById(R.id.attributeRecycler);
        mAttributeRecycler.setLayoutManager(new LinearLayoutManager(this));
        mAttributeAdapter = new AttributeListAdapter(this, mReformType, mAttributes);
        mAttributeRecycler.setAdapter(mAttributeAdapter);
        mAttributeRepo = new AttributeRepository(this);
        mAttributeRepo.findAllAttributesByReformTypeId(mReformType.getId()).observe(
                this, attributes -> {
                    if (attributes != null && attributes.size() != 0) {
                        mNoAttributes.setVisibility(View.GONE);
                        mAttributes.addAll(attributes);
                        mAttributeAdapter.notifyDataSetChanged();
                    } else {
                        mNoAttributes.setVisibility(View.VISIBLE);
                    }

                    mAttributesProgressBar.setVisibility(View.GONE);
                });


    }

    /**
     * Triggers the category creation event chain.
     *
     * @param view The triggered view.
     * @author Ricard Pinilla Barnes
     */
    public void createCategory(View view) {
        Intent intent = new Intent(this, CategoryCreateActivity.class);
        intent.putExtra(Key.REFORM_TYPE_ID, mReformType.getId());
        startActivity(intent);
        finish();
    }

    /**
     * Triggers the attribute creation event chain.
     *
     * @param view The triggered view.
     * @author Ricard Pinilla Barnes
     */
    public void createAttribute(View view) {
        Intent intent = new Intent(this, AttributeCreateActivity.class);
        intent.putExtra(Key.REFORM_TYPE_ID, mReformType.getId());
        startActivity(intent);
        finish();
    }

    /**
     * Handles the actions to return to the previous screen.
     * <p>
     * New intent is to ensure the refresh of the previous screen content.
     *
     * @author Ricard Pinilla Barnes
     */
    private void goBack() {
        Intent intent = new Intent(this, ReformTypeListActivity.class);
        startActivity(intent);
        finish();
    }

}
