package erdari.renformer_android.ui.user.admin.attribute;

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
import erdari.renformer_android.data.model.relational.RelationalAttribute;
import erdari.renformer_android.config.Key;
import erdari.renformer_android.data.model.Attribute;
import erdari.renformer_android.data.repository.AttributeRepository;
import erdari.renformer_android.tools.FirstOptionCreator;

/**
 * Holds the attribute creation GUI.
 *
 * @author Ricard Pinilla Barnes
 */
public class AttributeCreateActivity extends AppCompatActivity {

    private EditText mNameBox;

    private long mReformTypeId;

    private InputMethodManager mImm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attribute_create);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (mImm != null) {
            mImm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }

        mNameBox = findViewById(R.id.nameBox);

        mReformTypeId = getIntent().getLongExtra(Key.REFORM_TYPE_ID, 0);
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

            Intent intent = new Intent(this, ReformTypeDetailActivity.class);
            intent.putExtra(Key.REFORM_TYPE_ID, mReformTypeId);
            startActivity(intent);

            finish();
        }
        return true;
    }

    /**
     * Triggers the attribute creation event chain.
     *
     * @param view The triggered view.
     * @author Ricard Pinilla Barnes
     */
    public void createAttribute(View view) {
        String name = mNameBox.getText().toString().trim();

        if (!name.isEmpty()) {
            Attribute attribute = new Attribute();
            attribute.setName(name);

            RelationalAttribute relationalAttribute = new RelationalAttribute(attribute);
            relationalAttribute.setReformTypeId(mReformTypeId); // Non-OOP adaption.

            AttributeRepository attributeRepo = new AttributeRepository(this);
            attributeRepo.createAttribute(relationalAttribute).observe(
                    this, foundAttribute -> {
                        if (foundAttribute != null) {
                            mImm.hideSoftInputFromWindow(mNameBox.getWindowToken(), 0);

                            Toast.makeText(
                                    this,
                                    "Attribute has been successfully created.",
                                    Toast.LENGTH_SHORT
                            ).show();

                            FirstOptionCreator.createFirstOption(this, foundAttribute)
                                    .observe(this, isCreated -> {
                                                if (isCreated) {
                                                    Intent intent = new Intent(this, ReformTypeDetailActivity.class);
                                                    intent.putExtra(Key.REFORM_TYPE_ID, mReformTypeId);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    attributeRepo.deleteAttribute(attribute).observe(this, isDeleted -> {
                                                        Toast.makeText(
                                                                this,
                                                                "Some error happened...",
                                                                Toast.LENGTH_SHORT
                                                        ).show();
                                                    });
                                                    finish();
                                                }
                                            }
                                    );
                        }
                    });
        } else {
            Toast.makeText(this, "Fields cannot be empty.", Toast.LENGTH_SHORT).show();
        }

    }
}
