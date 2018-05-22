package dog.heredity.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class DogCreatorActivity extends Activity implements OnClickListener {

	ArrayAdapter<CharSequence> adapter;
	TextView tv;
	EditText editTextName;
	RadioButton maleButton;
	RadioButton femaleButton;
	Spinner coatColorSpinner;
	Spinner eyeColorSpinner;
	Spinner tailTypeSpinner;
	Button createButton;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dog_creator_layout);

		tv = (TextView) findViewById(R.id.textViewDogInfo);
		editTextName = (EditText) findViewById(R.id.editTextName);
		createButton = (Button) findViewById(R.id.create_button);
		maleButton = (RadioButton) findViewById(R.id.male_button);
		femaleButton = (RadioButton) findViewById(R.id.female_button);

		createButton.setOnClickListener(this);
		maleButton.setOnClickListener(this);
		femaleButton.setOnClickListener(this);

		// **********COAT COLOR SPINNER*****************
		coatColorSpinner = (Spinner) findViewById(R.id.spinner1);
		adapter = ArrayAdapter
				.createFromResource(this, R.array.coat_colors_array,
						android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		coatColorSpinner.setAdapter(adapter);

		// **********EYE COLOR SPINNER*****************
		eyeColorSpinner = (Spinner) findViewById(R.id.spinner2);
		adapter = ArrayAdapter.createFromResource(this,
				R.array.eye_colors_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		eyeColorSpinner.setAdapter(adapter);
		

		// **********TAIL TYPE SPINNER*****************
		tailTypeSpinner = (Spinner) findViewById(R.id.spinner3);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.tail_types_array,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		tailTypeSpinner.setAdapter(adapter);

	}
	

	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.create_button:
			DatabaseHelper dh = new DatabaseHelper(this);
			dh.open();
			dh.createEntry(editTextName.getText().toString(),
					getGenderSelected(), getCoatColorPair(), getEyeColorPair(),
					getTailTypePair());
			dh.close();
			if (getGenderSelected() == null) Toast.makeText(this,
					"You need to select a gender!", Toast.LENGTH_LONG)
					.show();
			else {

				startActivity(new Intent(this, DogHeredityActivity.class));
			}
			break;

		case R.id.male_button:

			if (femaleButton.isChecked()) femaleButton.setChecked(false);

			break;

		case R.id.female_button:

			if (maleButton.isChecked()) maleButton.setChecked(false);

			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK: {
			startActivity(new Intent(this, DogHeredityActivity.class));
			return true;
		}
		}
		return false;
	}

	public void onStop() {
		Log.i("BumpTest", "onStop");
		finish();
		super.onStop();
	}

	private String getGenderSelected() {
		if (maleButton.isChecked()) return "XX";
		else if (femaleButton.isChecked()) return "XY";
		else return null;
	}

	private String getCoatColorPair() {
		if (coatColorSpinner.getSelectedItem().toString().equals("Black")) return "BB";
		else if (coatColorSpinner.getSelectedItem().toString().equals("Brown")) return "bb";
		else return null;
	}

	private String getEyeColorPair() {
		if (eyeColorSpinner.getSelectedItem().toString().equals("Brown")) return "BB";
		else if (eyeColorSpinner.getSelectedItem().toString().equals("Hazel")) return "bb";
		else return null;
	}

	private String getTailTypePair() {
		if (tailTypeSpinner.getSelectedItem().toString().equals("Short")) return "TT";
		else if (tailTypeSpinner.getSelectedItem().toString().equals("Long")) return "tt";
		else return null;
	}

}
