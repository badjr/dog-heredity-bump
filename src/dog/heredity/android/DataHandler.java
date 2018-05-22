package dog.heredity.android;

import java.util.Scanner;

import android.os.RemoteException;
import android.widget.Toast;

public class DataHandler extends DogHeredityActivity {
	
	//This class controls data exchange between two devices when connected.

	Dog d1;
	Dog d2;

	private DogHeredityActivity appContext;

	String receivedBrandString;
	String receivedModelString;

	Dog theirDog = null;

	final int BRAND_AND_MODEL_DOG_CODE = 0;
	// final int MESSAGE_CODE = 1;
	final int NO_DOG_SELECTED_CODE = 2;
	final int TRADE_CODE = 3;
	final int DOG_CODE = 4;

	// Dog testDog = new Dog("TestDog", "F", "Yellow", "Blue", "Curly");

	DataHandler(DogHeredityActivity context) {
		/*
		 * This is needed to use the context of DogHeredityActivity inside this
		 * class since this or getApplicationContext() doesn't work.
		 */
		this.appContext = context;
	}

	public void handleData(long channelIDLong, String dataReceived) {
		// String dataReceived = new String(intent.getByteArrayExtra("data"));
		Scanner s = new Scanner(dataReceived);

		int CODE = s.nextInt();

		switch (CODE) {

		case NO_DOG_SELECTED_CODE:
			Toast.makeText(appContext,
					"The user you bumped with doesn't have a dog selected.",
					Toast.LENGTH_LONG).show();
			break;
		case TRADE_CODE:
			Toast.makeText(appContext, "They just pressed the trade button.",
					Toast.LENGTH_LONG).show();
			break;
		case DOG_CODE:
			// TODO receive dog bytes

			s.useDelimiter(",");
			 
			theirDog = new Dog(s.next().trim(), s.next(), s.next(), s.next(), s.next());

			// Toast.makeText(
			// appContext,
			// "Their dog: " + theirDog.getName() + " "
			// + theirDog.getGender() + " "
			// + theirDog.getCoatColor() + " "
			// + theirDog.getEyeColor() + " "
			// + theirDog.getTailType(), Toast.LENGTH_LONG).show();

			breedDogs();

			break;

		default:
			Toast.makeText(appContext,
					"You just received data from a " + dataReceived,
					Toast.LENGTH_LONG).show();
			s.close();
		}

		s.close();

	}

	public void sendSelectedDog() {

		if (selectedPositionInt == -1) {
			try {
				Toast.makeText(appContext, "You don't have a dog selected.",
						Toast.LENGTH_LONG).show();
				api.send(channelID, (NO_DOG_SELECTED_CODE + "").getBytes());
			}
			catch (RemoteException e) {
				// TODO Auto-generated catch block
				Toast.makeText(getApplicationContext(),
						"Error notifying no dog selected.", Toast.LENGTH_LONG)
						.show();
				e.printStackTrace();
			}
		}

		else {
			try {
				api.send(
						channelID,
						(DOG_CODE
								+ " "
								+ selectedDog.getName() + ","
								+ selectedDog.getGenderPair() + ","
								+ selectedDog.getCoatColorPair() + ","
								+ selectedDog.getEyeColorPair() + "," + selectedDog
								.getTailTypePair()).getBytes());

			}
			catch (RemoteException e) {
				Toast.makeText(getApplicationContext(),
						"Error sending phone and dog info.", Toast.LENGTH_LONG)
						.show();
				e.printStackTrace();
			}
		}

	}

	void breedDogs() { //Breed dogs after a bump.

		d1 = selectedDog;
		d2 = theirDog;

		int dogsBred = 2 + (int) (Math.random() * ((3 - 2) + 1));

		DatabaseHelper DB = new DatabaseHelper(appContext);

		DB.open();

		for (int i = 0; i < dogsBred; i++) {
			try { // Try to mate two dogs.

				Dog puppy = new Dog().breed(d1, d2);
				DB.createEntry(puppy);

			}
			catch (Exception e) { // If they are the same genderPair, it will not work.
				t.setText("Error: You cannot breed two dogs of the same genderPair!\n"
						+ d1.getName()
						+ " and "
						+ d2.getName()
						+ " are both "
						+ d1.getGender());
			}

			t.setText("You kept " + dogsBred + " of the dogs bred.");
			
			// TODO Find out why updateList won't work.
			// updateList();

		}

		 DB.close();
	}

}
