package erdari.renformer_android.ui.user.admin.reformtype;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import erdari.renformer_android.R;
import erdari.renformer_android.data.model.ReformType;
import erdari.renformer_android.data.repository.ReformTypeRepository;

/**
 * Holds the attribute edition GUI.
 *
 * @author Ricard Pinilla Barnes
 */
public class ReformTypeCreateActivity extends AppCompatActivity {

    private EditText mNameBox;
    private EditText mPriceBox;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_reformtype);

        setTitle(getString(R.string.new_reform_type));
        mProgressBar = findViewById(R.id.categoriesProgressBar);
        mNameBox = findViewById(R.id.reformNameBox);
        mPriceBox = findViewById(R.id.reformPriceBox);

        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ReformTypeListActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Triggers the reform type creation.
     *
     * @param view The triggered view.
     * @author Ricard Pinilla Barnes
     */
    public void createReformType(View view) {
        mProgressBar.setVisibility(View.VISIBLE);

        String name = mNameBox.getText().toString().trim();
        String priceString = mPriceBox.getText().toString().trim();

        if (!name.isEmpty() && !priceString.isEmpty()) {
            float price = Float.parseFloat(priceString);
            ReformType reformType = new ReformType();
            reformType.setName(name);
            reformType.setPrice(price);

            ReformTypeRepository reformTypeRepo = new ReformTypeRepository(this);
            reformTypeRepo.createReformType(reformType).observe(this, isCreated -> {
                if (isCreated) {
                    Toast.makeText(
                            this,
                            "Reform type has been successfully created.",
                            Toast.LENGTH_SHORT
                    ).show();

                    Intent intent = new Intent(this, ReformTypeListActivity.class);
                    startActivity(intent);

                    finish();
                }


            });
        } else {
            Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
        }

        mProgressBar.setVisibility(View.GONE);
    }
}
