package erdari.renformer_android.ui.user.admin.attribute;

import android.app.AlertDialog;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import erdari.renformer_android.ui.user.admin.attribute.option.OptionCreateActivity;
import erdari.renformer_android.ui.user.admin.attribute.option.OptionListAdapter;
import erdari.renformer_android.ui.user.admin.reformtype.ReformTypeDetailActivity;
import erdari.renformer_android.R;
import erdari.renformer_android.config.Key;
import erdari.renformer_android.data.auxiliary.ReformIdObjectBundle;
import erdari.renformer_android.data.model.Attribute;
import erdari.renformer_android.data.model.Option;
import erdari.renformer_android.data.model.relational.RelationalAttribute;
import erdari.renformer_android.data.repository.AttributeRepository;
import erdari.renformer_android.data.repository.OptionRepository;

import static android.view.View.GONE;

/**
 * Holds the attribute edition UI.
 *
 * @author Ricard Pinilla Barnes
 */
public class AttributeEditActivity extends AppCompatActivity {

    private boolean mIsEditMode;

    private long mReformTypeId;
    private long mAttributeId;

    private Attribute mAttribute;
    private List<Option> mOptions;

    private TextView mIdBox;
    private TextView mNameBox;
    private TextView mDeleteText;

    private Button mEditButton;
    private ProgressBar mProgressBar;

    private InputMethodManager mImm;
    private RecyclerView mOptionsRecycler;
    private OptionListAdapter mOptionsAdapter;
    private OptionRepository mOptionRepo;
    private AttributeRepository mAttributeRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_attribute);

        setTitle(getString(R.string.attribute_title));
        mProgressBar = findViewById(R.id.categoriesProgressBar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mIdBox = findViewById(R.id.userIdText);
        mNameBox = findViewById(R.id.nameBox);
        mEditButton = findViewById(R.id.buttonEdit);
        mDeleteText = findViewById(R.id.deleteOptionLabel);

        mReformTypeId = getIntent().getLongExtra(Key.REFORM_TYPE_ID, 0);
        mAttributeId = getIntent().getLongExtra(Key.ATTRIBUTE_ID, 0);

        ReformIdObjectBundle bundle = new ReformIdObjectBundle();
        bundle.setReformTypeId(mReformTypeId);
        bundle.setAttributeId(mAttributeId);

        mAttributeRepo = new AttributeRepository(this);
        mAttributeRepo.findLocalAttributeById(getIntent().getLongExtra(Key.ATTRIBUTE_ID, 0))
                .observe(this, attribute -> {
                    if (attribute != null) {
                        mAttribute = attribute;
                        mIdBox.setText(String.valueOf(mAttribute.getId()));
                        mNameBox.setText(mAttribute.getName());
                        mOptions = new ArrayList<>();
                        mOptionsRecycler = findViewById(R.id.optionsRecycler);
                        mOptionsRecycler.setLayoutManager(new LinearLayoutManager(this));
                        mOptionsAdapter = new OptionListAdapter(this, mOptions, bundle);
                        mOptionsRecycler.setAdapter(mOptionsAdapter);
                        mOptionRepo = new OptionRepository(this);
                        mOptionRepo.findAllOptionsByAttributeId(mAttribute.getId()).observe(
                                this, options -> {
                                    if (options != null) {
                                        mAttribute.setOptions(options);
                                        mOptions.addAll(options);
                                        mOptionsAdapter.notifyDataSetChanged();
                                    }
                                });
                    }

                    mProgressBar.setVisibility(GONE);
                });
    }

    @Override
    public void onBackPressed() {
        goBack();
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

    /**
     * Toggles the edit/save button.
     *
     * @param view The triggered view.
     * @author Ricard Pinilla Barnes
     */
    public void toggleSave(View view) {
        mNameBox.setEnabled(!mIsEditMode);
        if (mIsEditMode) {
            applyChanges();
            mEditButton.setText("Edit");
            mImm.hideSoftInputFromWindow(mNameBox.getWindowToken(), 0);
            mDeleteText.setVisibility(View.VISIBLE);
        } else {
            mEditButton.setText("Save");
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

        if (!name.isEmpty()) {
            Attribute attribute = new Attribute();
            attribute.setId(mAttributeId);
            attribute.setName(name);

            // Non-OOP adaption for the Api
            RelationalAttribute relationalAttribute = new RelationalAttribute(attribute);
            relationalAttribute.setReformTypeId(mReformTypeId);

            mAttributeRepo.patchAttribute(relationalAttribute).observe(this, isPatched -> {
                if (isPatched) {
                    Toast.makeText(this, "Attribute successfully updated.", Toast.LENGTH_SHORT).show();
                }
            });

            mProgressBar.setVisibility(GONE);
        }

    }

    /**
     * Handles the edited attribute deletion.
     *
     * @param view The triggered view.
     * @author Ricard Pinilla Barnes
     */
    public void triggerAttributeDeletion(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Attribute deletion")
                .setMessage("The attribute and all its options will be deleted.\n\nProceed?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> deleteAttribute())
                .setNegativeButton(android.R.string.no, null).show();
    }

    /**
     * Starts the option creation activity.
     *
     * @param view The triggered view.
     * @author Ricard Pinilla Barnes
     */
    public void createOption(View view) {
        Intent intent = new Intent(this, OptionCreateActivity.class);
        intent.putExtra(Key.REFORM_TYPE_ID, mReformTypeId);
        intent.putExtra(Key.ATTRIBUTE_ID, mAttribute.getId());
        startActivity(intent);
        finish();
    }

    /**
     * Turns the user back to the reform type detail activity.
     *
     * @author Ricard Pinilla Barnes
     */
    private void goBack() {
        mAttributeRepo.deleteLocalAttribute(mAttribute); // Cleans the room and free some space
        Intent intent = new Intent(this, ReformTypeDetailActivity.class);
        intent.putExtra(Key.REFORM_TYPE_ID, mReformTypeId);
        startActivity(intent);
        finish();
    }

    /**
     * Triggers the attribute deletion.
     *
     * @author Ricard Pinilla Barnes
     */
    private void deleteAttribute() {
        mProgressBar.setVisibility(View.VISIBLE);
        if (mOptions != null && !mOptions.isEmpty()) {
            mOptionRepo.deleteOption(mOptions.get(0)).observe(this, isDeleted -> {
                if (isDeleted) {
                    mOptions.remove(0);
                    deleteAttribute();
                }
            });
        } else {
            mAttributeRepo.deleteAttribute(mAttribute).observe(
                    this, isDeleted -> {
                        if (isDeleted) {
                            mAttributeRepo.deleteLocalAttribute(mAttribute);

                            Toast.makeText(
                                    this,
                                    "Attribute and its options successfully deleted.",
                                    Toast.LENGTH_SHORT
                            ).show();

                            Intent intent = new Intent(this, ReformTypeDetailActivity.class);
                            intent.putExtra(Key.REFORM_TYPE_ID, mReformTypeId);
                            startActivity(intent);

                            finish();
                        }
                    });
        }
        mProgressBar.setVisibility(GONE);
    }

}
