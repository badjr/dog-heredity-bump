package dog.heredity.android;

import java.util.Scanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bump.api.BumpAPIIntents;
import com.bump.api.IBumpAPI;

public class DogHeredityActivity extends Activity implements OnClickListener {

	static TextView t;
	Button bump;
	CheckBox cb;
	RadioButton breedOption;
	RadioButton tradeOption;
	ListView lv;
	Button newDogButton;
	Button editNameButton;
	Button deleteDogButton;

	static String selectedDogString = null;
	static Dog selectedDog = null;
	Dog theirDog = null;

	String brandString = android.os.Build.BRAND;
	String modelString = android.os.Build.MODEL;

	// Context appContext = this;

	DataHandler dataHandler;

	static long channelID;

	static int selectedPositionInt = -1;

	static Animation animation;
	
	ImageView dogImg;
//	Button testBtn;
//	TextView testTV;

	// ***************************ON CREATE BEGIN***********************
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		t = (TextView) findViewById(R.id.status);
		bump = (Button) findViewById(R.id.bumpButton);
		cb = (CheckBox) findViewById(R.id.checkBox1);
		breedOption = (RadioButton) findViewById(R.id.breed_option);
		tradeOption = (RadioButton) findViewById(R.id.trade_option);
		lv = (ListView) findViewById(R.id.listView1);
		newDogButton = (Button) findViewById(R.id.newButton);
		editNameButton = (Button) findViewById(R.id.edit_name_button);
		deleteDogButton = (Button) findViewById(R.id.deleteButton);

		bump.setOnClickListener(this);
		breedOption.setOnClickListener(this);
		tradeOption.setOnClickListener(this);
		newDogButton.setOnClickListener(this);
		editNameButton.setOnClickListener(this);
		deleteDogButton.setOnClickListener(this);
		// lv.setOnItemClickListener(this);

		dataHandler = new DataHandler(this);

		cb.setClickable(false);
		breedOption.setChecked(true);
		
		dogImg = (ImageView) findViewById(R.id.dog_img);
//		testBtn = (Button) findViewById(R.id.testBtn);
//		testBtn.setOnClickListener(this);
//		testTV = (TextView) findViewById(R.id.testTV);

		
		//When dog selected, perform these actions.
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				Scanner s = new Scanner(arg0.getItemAtPosition(position)
						.toString());
				
				selectedPositionInt = s.nextInt();

				DatabaseHelper DB = new DatabaseHelper(getApplicationContext());
				DB.open();
				selectedDog = DB.getDog(selectedPositionInt);
				t.setText("Selected dog: " + selectedDog.getAttributeString());
				
				selectedDog.loadImage(dogImg);
				
				DB.close();

				s.close();

			}
		});
		

		// ------------Bump essentials ---------------------
		bindService(new Intent(IBumpAPI.class.getName()), connection,
				Context.BIND_AUTO_CREATE);

		IntentFilter filter = new IntentFilter();
		filter.addAction(BumpAPIIntents.CHANNEL_CONFIRMED);
		filter.addAction(BumpAPIIntents.DATA_RECEIVED);
		filter.addAction(BumpAPIIntents.NOT_MATCHED);
		filter.addAction(BumpAPIIntents.MATCHED);
		filter.addAction(BumpAPIIntents.CONNECTED);
		filter.addAction(BumpAPIIntents.BUMPED);
		registerReceiver(receiver, filter);
		// --------------End of bump essentials -------------------------
		
		
		// ------------IF NO DOGS, CREATE DEFAULT DOG----------------------
		DatabaseHelper entry = null;
		entry = new DatabaseHelper(this);
		entry.open();
		if (entry.getCount() == 0) {
			// ******** Dog attributes are in this order: NAME, GENDER, COAT
			// COLOR, EYE COLOR, TAIL TYPE
			entry.createEntry("Wishbone", "XX", "BB", "BB", "tt");

		}
		entry.close();
		updateList();

	}
	// ***************************ON CREATE END *******************************
	

	public void updateList() { //Updates the ListView after adding/deleting/editing dogs.
		DatabaseHelper info = new DatabaseHelper(this);
		info.open();
		String[] values = info.getData();
		// *****************************ListView*******************
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, values);

		// Assign adapter to ListView
		lv.setAdapter(adapter);
		// *****************************End of ListView************
		info.close();
	}

	public void onStart() {
		Log.i("BumpTest", "onStart");
		super.onStart();
	}

	public void onRestart() {
		Log.i("BumpTest", "onRestart");
		super.onRestart();
	}

	public void onResume() {
		Log.i("BumpTest", "onResume");
		super.onResume();
	}

	public void onPause() {
		Log.i("BumpTest", "onPause");

		try {
			api.disableBumping();
		}
		catch (RemoteException e) {
			Toast.makeText(getApplicationContext(),
					"Unable to disable bumping.", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}

		super.onPause();
	}

	public void onStop() {
		Log.i("BumpTest", "onStop");
		finish();
		super.onStop();
	}

	public void onDestroy() {
		Log.i("BumpTest", "onDestroy");
		unbindService(connection);
		unregisterReceiver(receiver);
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_SPACE: {
			try {
				api.simulateBump();
			}
			catch (Exception e) {
				// e.printStackTrace();
				Toast.makeText(getApplicationContext(), "Spacebar error",
						Toast.LENGTH_LONG).show();
			}
			return true;
		}
		case KeyEvent.KEYCODE_BACK:
			finish();
		}
		return false;
	}

	static {
		System.loadLibrary("android-api");
	}

	public static IBumpAPI api;

	private ServiceConnection connection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder binder) {

			api = IBumpAPI.Stub.asInterface(binder);

			// Toast.makeText(getApplicationContext(), "Service connected.",
			// Toast.LENGTH_LONG).show();

			cb.setChecked(true);
			try {
				api.configure("42689567e18a4b90a40bdea6079c338e", "Brett");
				// Toast.makeText(getApplicationContext(), "Connected",
				// Toast.LENGTH_LONG).show();
			}
			catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						"Error configuring api.", Toast.LENGTH_LONG).show();
			}

		}

		public void onServiceDisconnected(ComponentName name) {
			cb.setChecked(false);
		}
	};

	// *************Bump broadcast receiver******************
	private final BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, final Intent intent) {
			final String action = intent.getAction();
			try {

				if (action.equals(BumpAPIIntents.BUMPED)) {
					// Only if you want feedback for a bump.
					
//					Toast.makeText(getApplicationContext(), "Bump detected.",
//							Toast.LENGTH_LONG).show();

				}
				else if (action.equals(BumpAPIIntents.MATCHED)) {
					api.confirm(intent.getLongExtra("proposedChannelID", 0),
							true);
					// Toast.makeText(getApplicationContext(),
					// "You just matched.", Toast.LENGTH_LONG).show();
				}
				else if (action.equals(BumpAPIIntents.CHANNEL_CONFIRMED)) {

					channelID = intent.getLongExtra("channelID", 0);

					Toast.makeText(getApplicationContext(),
							"You just confirmed channel: " + channelID,
							Toast.LENGTH_LONG).show();

					if (breedOption.isChecked()) dataHandler.sendSelectedDog();

					else Toast.makeText(getApplicationContext(),
							"Trade not implemented yet.", Toast.LENGTH_LONG)
							.show();

				}
				else if (action.equals(BumpAPIIntents.DATA_RECEIVED)) {
					Log.i("Bump Test",
							"Received data from: "
									+ api.userIDForChannelID(intent
											.getLongExtra("channelID", 0)));
					Log.i("Bump Test",
							"Data: "
									+ new String(intent
											.getByteArrayExtra("data")));

					// Toast.makeText(getApplicationContext(),
					// "You just received data from: " +
					// api.userIDForChannelID(intent
					// .getLongExtra("channelID", 0)) + ": " + new String(intent
					// .getByteArrayExtra("data")), Toast.LENGTH_LONG)
					// .show();

					dataHandler.handleData(channelID,
							new String(intent.getByteArrayExtra("data")));

				}
				else if (action.equals(BumpAPIIntents.CONNECTED)) {
					api.enableBumping();
					// Toast.makeText(getApplicationContext(),
					// "You just connected and enabled bumping.",
					// Toast.LENGTH_LONG).show();
				}
				else if (action.equals(BumpAPIIntents.NOT_MATCHED)) {
					Toast.makeText(getApplicationContext(),
							"There was no match.", Toast.LENGTH_LONG).show();
				}
				else if (action.equals(BumpAPIIntents.DISCONNECTED)) {
					Toast.makeText(getApplicationContext(),
							"You just disconnected from the channel."
							// + intent.getLongExtra("channelID", 0)
							, Toast.LENGTH_LONG).show();
				}
			}
			catch (RemoteException e) {
				Toast.makeText(getApplicationContext(),
						"Broadcast receiver error.", Toast.LENGTH_LONG).show();

			}
			

		}
	};
	// *************End Bump broadcast receiver******************
	

	// ************BUTTONS*****************************
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.bumpButton:

			try {
				api.simulateBump();
			}
			catch (RemoteException e) {
				Toast.makeText(getApplicationContext(),
						"Error trying to bump.", Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
			break;

		case R.id.newButton:

			startActivity(new Intent(this, DogCreatorActivity.class));

			break;

		case R.id.edit_name_button:

			if (selectedPositionInt != -1) {

				final DatabaseHelper DB = new DatabaseHelper(this);
				DB.open();

				final EditText input = new EditText(this);

				new AlertDialog.Builder(this)
						.setTitle("New name")
						.setMessage("Enter new name for dog:")
						.setView(input)
						.setPositiveButton("Ok",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {

										DB.updateEntry(selectedPositionInt,
												input.getText().toString());

										selectedDog = DB
												.getDog(selectedPositionInt);

										t.setText("Selected dog: "
												+ selectedDog
														.getAttributeString());
										DB.close();
										updateList();
									}
								})
						.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										// Do nothing.
										DB.close();
									}
								}).show();
			}

			else Toast.makeText(getApplicationContext(),
					"You don't have a dog selected.", Toast.LENGTH_LONG).show();
			break;

		case R.id.deleteButton:

			if (selectedPositionInt != -1) {
				DatabaseHelper dbH = new DatabaseHelper(this);
				dbH.open();
				dbH.deleteEntry(selectedPositionInt);
				dbH.close();
				updateList();
				t.setText("Selected dog: none");
				selectedPositionInt = -1;

				selectedDog = null;
				
				//TODO Make this clear the dog image.
//				dogImg = null;
				

			}
			else Toast.makeText(getApplicationContext(),
					"You don't have a dog selected.", Toast.LENGTH_LONG).show();
			break;
		case R.id.breed_option:
			if (tradeOption.isChecked()) tradeOption.setChecked(false);
			break;
		case R.id.trade_option:
			if (breedOption.isChecked()) breedOption.setChecked(false);
			break;
			
//			Use test button to test if genes are passing down in dogs correctly.
//			Must uncomment the testBtn below and all "testBtn" and "testTV"
//			occurrences in this file and main.xml
			
//		case R.id.testBtn:
//			Dog testDog1 = new Dog("Sam", "XX", "BB", "BB", "tt");
//			Dog testDog2 = new Dog("Peaches", "XY", "bb", "bb", "TT");
//			
//			Dog puppy1 = new Dog().breed(testDog1, testDog2);
//			Dog puppy2 = new Dog().breed(testDog1, testDog2);
//			Dog puppy3 = new Dog().breed(testDog1, testDog2);
//			
//			testTV.setText("Puppy 1: " + puppy1.getAttributeString() + " " + puppy1.getGenderPair() + puppy1.getCoatColorPair() + puppy1.getEyeColorPair() + puppy1.getTailTypePair() + "\n"
//						 + "Puppy 2: " + puppy2.getAttributeString() + " " + puppy2.getGenderPair() + puppy2.getCoatColorPair() + puppy2.getEyeColorPair() + puppy2.getTailTypePair() + "\n"
//						 + "Puppy 3: " + puppy3.getAttributeString() + " " + puppy3.getGenderPair() + puppy3.getCoatColorPair() + puppy3.getEyeColorPair() + puppy3.getTailTypePair());
//			
//			if (puppy1.getGender().compareTo(puppy2.getGender()) != 0) {
//				Dog grandChildPuppy1 = new Dog().breed(puppy1, puppy2);
//				testTV.append("\nGrand child puppy 1 X 2: " + grandChildPuppy1.getAttributeString() + " " + grandChildPuppy1.getGenderPair() + grandChildPuppy1.getCoatColorPair() + grandChildPuppy1.getEyeColorPair() + grandChildPuppy1.getTailTypePair());
//			}
//			if (puppy2.getGender().compareTo(puppy3.getGender()) != 0) {
//				Dog grandChildPuppy2 = new Dog().breed(puppy2, puppy3);
//				testTV.append("\nGrand child puppy 2 X 3" + grandChildPuppy2.getAttributeString() + " " + grandChildPuppy2.getGenderPair() + grandChildPuppy2.getCoatColorPair() + grandChildPuppy2.getEyeColorPair() + grandChildPuppy2.getTailTypePair() );
//			}
//			if (puppy1.getGender().compareTo(puppy3.getGender()) != 0) {
//				Dog grandChildPuppy3 = new Dog().breed(puppy1, puppy3);
//				testTV.append("\nGrand child puppy 1 X 3" + grandChildPuppy3.getAttributeString() + " " + grandChildPuppy3.getGenderPair() + grandChildPuppy3.getCoatColorPair() + grandChildPuppy3.getEyeColorPair() + grandChildPuppy3.getTailTypePair() );
//			}
//			break;
			
			

		}
	}

	// **************END BUTTONS***********************

}
