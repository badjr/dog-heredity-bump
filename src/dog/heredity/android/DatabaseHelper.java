package dog.heredity.android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class DatabaseHelper {

	public static final String KEY_ROWID = "_id";
	public static final String KEY_NAME = "dog_name";
	public static final String KEY_GENDER = "dog_gender";
	public static final String KEY_COAT_COLOR = "dog_coat_color";
	public static final String KEY_EYE_COLOR = "dog_eye_color";
	public static final String KEY_TAIL_TYPE = "dog_tail_type";

	private static final String DATABASE_NAME = "DogDB";
	private static final String DATABASE_TABLE = "DogTable";
	private static final int DATABASE_VERSION = 1;

	private DbHelper ourHelper;
	private final Context ourContext;
	private SQLiteDatabase ourDatabase;

	private static class DbHelper extends SQLiteOpenHelper {

		public DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL("CREATE TABLE " + DATABASE_TABLE + " (" + KEY_ROWID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_NAME
					+ " TEXT NOT NULL, " + KEY_GENDER + " TEXT NOT NULL, "
					+ KEY_COAT_COLOR + " TEXT NOT NULL, " + KEY_EYE_COLOR
					+ " TEXT NOT NULL," + KEY_TAIL_TYPE + " TEXT NOT NULL);");

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
		}

	}

	public DatabaseHelper(Context c) {
		ourContext = c;
	}

	public DatabaseHelper open() throws SQLException {
		ourHelper = new DbHelper(ourContext);
		ourDatabase = ourHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		ourHelper.close();
	}

	public long createEntry(String name, String gender, String coatColor,
			String eyeColor, String tailType) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put(KEY_NAME, name);
		cv.put(KEY_GENDER, gender);
		cv.put(KEY_COAT_COLOR, coatColor);
		cv.put(KEY_EYE_COLOR, eyeColor);
		cv.put(KEY_TAIL_TYPE, tailType);
		return ourDatabase.insert(DATABASE_TABLE, null, cv);
	}

	public long createEntry(Dog dog) {
		ContentValues cv = new ContentValues();
		cv.put(KEY_NAME, dog.getName());
		cv.put(KEY_GENDER, dog.getGenderPair());
		cv.put(KEY_COAT_COLOR, dog.getCoatColorPair());
		cv.put(KEY_EYE_COLOR, dog.getEyeColorPair());
		cv.put(KEY_TAIL_TYPE, dog.getTailTypePair());
		return ourDatabase.insert(DATABASE_TABLE, null, cv);
	}

	public String[] getData() {

		String result[] = new String[(int) this.getCount()];

		String[] columns = new String[] { KEY_ROWID, KEY_NAME, KEY_GENDER,
				KEY_COAT_COLOR, KEY_EYE_COLOR, KEY_TAIL_TYPE };
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null,
				null, null);

		int rowColumn = c.getColumnIndex(KEY_ROWID);
		int nameColumn = c.getColumnIndex(KEY_NAME);
		int genderColumn = c.getColumnIndex(KEY_GENDER);
		int coatColorColumn = c.getColumnIndex(KEY_COAT_COLOR);
		int eyeColorColumn = c.getColumnIndex(KEY_EYE_COLOR);
		int tailTypeColumn = c.getColumnIndex(KEY_TAIL_TYPE);

		Dog dogToSelect;

		int i = 0;
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			dogToSelect = new Dog(c.getString(nameColumn),
					c.getString(genderColumn), c.getString(coatColorColumn),
					c.getString(eyeColorColumn), c.getString(tailTypeColumn));
			
			result[i++] = c.getString(rowColumn) + " " + 
			dogToSelect.getAttributeString();
		}

		return result;
	}

	public long getCount() {
		SQLiteStatement statement = ourDatabase
				.compileStatement("SELECT COUNT(*) FROM " + DATABASE_TABLE);
		long count = statement.simpleQueryForLong();
		return count;
	}

	public int getRowID() {
		String[] columns = new String[] { KEY_ROWID, KEY_NAME, KEY_GENDER,
				KEY_COAT_COLOR, KEY_EYE_COLOR, KEY_TAIL_TYPE };
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns,
				null, null, null, null, null);
		c.moveToFirst();
		int rowID = c.getInt(c.getColumnIndex(KEY_ROWID));
		return rowID;
	}

	public String getName(int position) {
		// TODO Auto-generated method stub
		String[] columns = new String[] { KEY_ROWID, KEY_NAME, KEY_GENDER,
				KEY_COAT_COLOR, KEY_EYE_COLOR, KEY_TAIL_TYPE };
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_ROWID + "="
				+ position, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
			String name = c.getString(1);
			return name;
		}
		return null;
	}

	public String getGender(int position) {
		// TODO Auto-generated method stub

		String[] columns = new String[] { KEY_ROWID, KEY_NAME, KEY_GENDER,
				KEY_COAT_COLOR, KEY_EYE_COLOR, KEY_TAIL_TYPE };
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_ROWID + "="
				+ position, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
			String name = c.getString(2);
			return name;
		}

		return null;
	}

	public String getCoatColor(int position) {
		// TODO Auto-generated method stub
		String[] columns = new String[] { KEY_ROWID, KEY_NAME, KEY_GENDER,
				KEY_COAT_COLOR, KEY_EYE_COLOR, KEY_TAIL_TYPE };
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_ROWID + "="
				+ position, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
			String name = c.getString(3);
			return name;
		}
		return null;
	}

	public String getEyeColor(int position) {
		// TODO Auto-generated method stub
		String[] columns = new String[] { KEY_ROWID, KEY_NAME, KEY_GENDER,
				KEY_COAT_COLOR, KEY_EYE_COLOR, KEY_TAIL_TYPE };
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_ROWID + "="
				+ position, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
			String name = c.getString(4);
			return name;
		}
		return null;
	}

	public String getTailType(int position) {
		// TODO Auto-generated method stub
		String[] columns = new String[] { KEY_ROWID, KEY_NAME, KEY_GENDER,
				KEY_COAT_COLOR, KEY_EYE_COLOR, KEY_TAIL_TYPE };
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_ROWID + "="
				+ position, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
			String name = c.getString(5);
			return name;
		}
		return null;
	}

	public Dog getDog(int position) { //Select dog from database and return.

		String[] columns = new String[] { KEY_ROWID, KEY_NAME, KEY_GENDER,
				KEY_COAT_COLOR, KEY_EYE_COLOR, KEY_TAIL_TYPE };
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_ROWID + "="
				+ position, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}

		Dog d = new Dog(c.getString(1), c.getString(2), c.getString(3),
				c.getString(4), c.getString(5));
		return d;

	}

	public void updateEntry(int row, String newName) {
		// TODO Auto-generated method stub
		ContentValues cvUpdate = new ContentValues();
		cvUpdate.put(KEY_NAME, newName);
		ourDatabase.update(DATABASE_TABLE, cvUpdate, KEY_ROWID + "=" + row,
				null);
	}

	public void deleteEntry(int row) {
		// TODO Auto-generated method stub
		ourDatabase.delete(DATABASE_TABLE, KEY_ROWID + "=" + row, null);
	}
}
