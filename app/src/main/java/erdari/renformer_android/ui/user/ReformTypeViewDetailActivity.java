package erdari.renformer_android.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import erdari.renformer_android.R;
import erdari.renformer_android.config.AppConstants;
import erdari.renformer_android.config.Key;
import erdari.renformer_android.data.model.Attribute;
import erdari.renformer_android.data.model.Category;
import erdari.renformer_android.data.model.Option;
import erdari.renformer_android.data.model.ReformBudget;
import erdari.renformer_android.data.model.ReformType;
import erdari.renformer_android.data.repository.ReformBudgetRepository;
import erdari.renformer_android.data.repository.ReformTypeRepository;
import erdari.renformer_android.tools.DpToPixelConverter;

/**
 * The reform type form activity.
 *
 * @author Ricard Pinilla Barnes
 */
public class ReformTypeViewDetailActivity extends AppCompatActivity {

    private ReformBudgetRepository mReformBudgetRepo;
    private ReformType mReformType;

    private RadioGroup mCategoryGroup;
    private LinearLayout mAttributeLayout;

    private EditText mMeterBox;
    private TextView mNoCategories;
    private TextView mNoAttributes;
    private ProgressBar mCategoriesProgressBar;
    private ProgressBar mAttributesProgressBar;
    private ProgressBar mBudgetProgressBar;
    private Button mSubmitButton;

    private List<Category> mCategories;
    private List<Attribute> mAttributes;

    private ReformBudget.Builder mBudgetBuilder;
    private Map<Long, LinearLayout> mLayoutMap;
    private Map<Long, Long> mSelectedOptions; // Map<attributeId, optionId>
    private long mSelectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reformtypeview_detail);
        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        setTitle(getIntent().getStringExtra(Key.REFORM_TYPE_NAME));

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mCategoriesProgressBar = findViewById(R.id.categoriesProgressBar);
        mAttributesProgressBar = findViewById(R.id.attributesProgressBar);
        mBudgetProgressBar = findViewById(R.id.budgetProgressBar);
        mSubmitButton = findViewById(R.id.submitButton);

        mBudgetProgressBar.setVisibility(View.GONE);

        mReformBudgetRepo = new ReformBudgetRepository(this);
        mLayoutMap = new HashMap<>();
        mSelectedOptions = new HashMap<>();

        mCategoryGroup = findViewById(R.id.categoryRadioGroup);
        mAttributeLayout = findViewById(R.id.attributeLayout);
        mMeterBox = findViewById(R.id.metersBox);
        mNoAttributes = findViewById(R.id.noAttributesText);
        mNoCategories = findViewById(R.id.noCategoriesText);

        mMeterBox.clearFocus();
        LinearLayout mainLayout = findViewById(R.id.mainLinearLayout);
        mainLayout.requestFocus();

        setUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, ReformTypeViewListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void submitForm(View view) {
        String meterString = mMeterBox.getText().toString().trim();

        if (!meterString.isEmpty()) {

            if (mSelectedCategory != 0) {

                if (mSelectedOptions != null && !mSelectedOptions.isEmpty()) {

                    for (Long optionId : mSelectedOptions.values()) {
                        if (optionId == null) {
                            Toast.makeText(
                                    this,
                                    "Some option needs to be checked",
                                    Toast.LENGTH_SHORT
                            ).show();
                            return; // One or more attributes have no option checked
                        }
                    }

                    // If every attribute has an option checked:
                    mBudgetBuilder.setSquareMeters(Float.parseFloat(meterString));
                    ReformBudget reformBudget = mBudgetBuilder.build();

                    mSubmitButton.setVisibility(View.GONE);
                    mBudgetProgressBar.setVisibility(View.VISIBLE);

                    mReformBudgetRepo.createBudget(reformBudget)
                            .observe(this, returnedReformBudget -> {
                                if (returnedReformBudget != null) {
                                    float amount = returnedReformBudget.getAmount();
                                    String formattedAmount = String.format(Locale.UK, "%.2f", amount)
                                            + AppConstants.CURRENCY_SYMBOL;

                                    mBudgetProgressBar.setVisibility(View.GONE);
                                    mSubmitButton.setVisibility(View.VISIBLE);

                                    new AlertDialog.Builder(this)
                                            .setTitle("Budget")
                                            .setMessage("The estimated price for your reform is:\n"
                                                    + formattedAmount)
                                            .setIcon(android.R.drawable.ic_dialog_info)
                                            .setPositiveButton(android.R.string.ok, null)
                                            .show();

                                }
                            });
                } else {
                    Toast.makeText(
                            this,
                            "At least one attribute is needed",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            } else {
                Toast.makeText(
                        this,
                        "A category is needed",
                        Toast.LENGTH_SHORT
                ).show();
            }

        } else {
            Toast.makeText(
                    this,
                    "Square meters are mandatory",
                    Toast.LENGTH_SHORT
            ).show();
        }

    }

    private void categoriesSetup() {
        if (!mCategories.isEmpty()) {
            for (Category category : mCategories) {
                RadioButton button = new RadioButton(this);
                button.setText(category.getName());
                button.setTag(category);
                button.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        mSelectedCategory = category.getId();
                        mBudgetBuilder.addCategory(category);
                    } else {
                        mBudgetBuilder.removeCategory(category);
                    }
                });

                mCategoryGroup.addView(button);
            }
        } else {
            mNoCategories.setVisibility(View.VISIBLE);
        }

        mCategoriesProgressBar.setVisibility(View.GONE);
    }

    private void attributesSetup() {
        if (!mAttributes.isEmpty()) {
            for (Attribute attribute : mAttributes) {
                long attributeId = attribute.getId();
                Switch switchBtn = new Switch(this);
                switchBtn.setTag(attribute);
                switchBtn.setText(attribute.getName());
                switchBtn.setOnCheckedChangeListener((buttonView, isSwitchChecked) -> {

                    LinearLayout layout = mLayoutMap.get(attributeId);

                    if (isSwitchChecked) {
                        mSelectedOptions.put(attributeId, null);

                        List<Option> options = attribute.getOptions();
                        if (options != null && !options.isEmpty()) {
                            RadioGroup radioGroup = new RadioGroup(this);
                            radioGroup.setTag(attribute);

                            for (Option option : options) {
                                long optionId = option.getId();
                                mSelectedOptions.put(attributeId, null);
                                RadioButton radioButton = new RadioButton(this);
                                radioButton.setTag(option);
                                radioButton.setText(option.getName());
                                radioButton.setOnCheckedChangeListener((radioBtn, isChecked) -> {
                                    if (isChecked) {
                                        mSelectedOptions.put(attributeId, optionId);
                                        mBudgetBuilder.addAttributeAndOption(attribute, option);
                                    } else {
                                        mBudgetBuilder.removeAttributeAndOption(attribute);
                                    }
                                });
                                radioGroup.addView(radioButton);
                            }

                            if (layout != null) {
                                layout.addView(radioGroup);
                                mLayoutMap.put(attributeId, layout);
                            }
                        }
                    } else {
                        mSelectedOptions.remove(attributeId);

                        if (layout != null) {
                            layout.removeAllViews();
                        }
                    }
                });

                int lateralPaddingPixels = DpToPixelConverter.parseDpToPixels(this, 32);
                int bottomPaddingPixels = DpToPixelConverter.parseDpToPixels(this, 8);

                LinearLayout attributeLayout = new LinearLayout(this); // Layout for the attribute Switch
                attributeLayout.setOrientation(LinearLayout.VERTICAL);
                attributeLayout.setPadding(lateralPaddingPixels, 0, lateralPaddingPixels, bottomPaddingPixels);
                attributeLayout.setTag(attribute);

                attributeLayout.addView(switchBtn);

                LinearLayout optionLayout = new LinearLayout(this); // Layout for the the option RadioButtons
                optionLayout.setOrientation(LinearLayout.VERTICAL);
                optionLayout.setPadding(lateralPaddingPixels, 0, lateralPaddingPixels, 0);

                attributeLayout.addView(optionLayout);
                mLayoutMap.put(attribute.getId(), optionLayout);

                mAttributeLayout.addView(attributeLayout);
            }
        } else {
            mNoAttributes.setVisibility(View.VISIBLE);
        }

        mAttributesProgressBar.setVisibility(View.GONE);
    }

    /**
     * General set up for this activity GUI.
     *
     * @author Ricard Pinilla Barnes
     */
    private void setUp() {
        long mReformTypeId = getIntent().getLongExtra(Key.REFORM_TYPE_ID, 0L);

        ReformTypeRepository reformTypeRepo = new ReformTypeRepository(this);
        reformTypeRepo.findReformTypeById(mReformTypeId).observe(this, reformType -> {
            if (reformType != null) {
                mReformType = reformType;
                mBudgetBuilder = new ReformBudget.Builder(reformType);

                List<Category> categories = mReformType.getCategories(); // Unexpected changes

//                CategoryRepository categoryRepo = new CategoryRepository(this);
//                categoryRepo.findAllCategoriesByReformTypeId(mReformTypeId)
//                        .observe(this, categories -> {
                if (categories != null) {
                    mCategories = categories;
                    mNoCategories.setVisibility(View.GONE);
                    categoriesSetup();

                    List<Attribute> attributes = mReformType.getAttributes(); // Unexpected changes

//                    AttributeRepository attributeRepo = new AttributeRepository(this);
//                    attributeRepo.findAllAttributesByReformTypeId(mReformTypeId)
//                            .observe(this, attributes -> {
                    if (attributes != null) {
                        mAttributes = attributes;

////                        OptionRepository optionRepo = new OptionRepository(this);
//                        for (Attribute attribute : mAttributes) {
//                            List<Option> options = attribute.getOptions();
//
////                            optionRepo.findAllOptionsByAttributeId(attribute.getId())
////                                    .observe(this, options -> {
//                            if (options != null) {
//                                attribute.setOptions(new ArrayList<>());
//
//                                if (!options.isEmpty()) {
//                                    for (Option option : options) {
//                                        attribute.getOptions().add(option);
//                                    }
//                                }
//                            }
////                                    });
//                        }

                        mNoAttributes.setVisibility(View.GONE);
                        attributesSetup();
                    }
//                            });
                }
//                        });

            } else {
                finish();
            }
        });
    }
}
