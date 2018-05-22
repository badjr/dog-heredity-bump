package dog.heredity.android;

import android.widget.ImageView;

//***** Attribute order: NAME, GENDER, COAT COLOR, EYE COLOR, TAIL TYPE
public class Dog {

	private String name;
	private String genderPair;
	private String coatColorPair;
	private String eyeColorPair;
	private String tailTypePair;

	public Dog(String name, String genderPair, String coatColorPair,
			String eyeColorPair, String tailTypePair) {

		this.name = name;
		this.genderPair = genderPair;
		this.coatColorPair = coatColorPair;
		this.eyeColorPair = eyeColorPair;
		this.tailTypePair = tailTypePair;

	}

	public Dog() { // Empty constructor dog for mating.
	}

	// Start of attributes.
	public String getName() {
		return name;
	}

	public String getGender() {

		if (this.genderPair.compareTo("XX") == 0) return "M";
		else if (this.genderPair.compareTo("XY") == 0) return "F";
		else if (this.genderPair.compareTo("YX") == 0) return "F";
		else return null;
	}

	public String getCoatColor() {
		if (this.coatColorPair.compareTo("BB") == 0
				|| this.coatColorPair.compareTo("Bb") == 0
				|| this.coatColorPair.compareTo("bB") == 0) return "Black";
		else if (this.coatColorPair.compareTo("bb") == 0) return "Brown";
		else return null;
	}

	public String getEyeColor() {
		if (this.eyeColorPair.compareTo("BB") == 0
				|| this.eyeColorPair.compareTo("Bb") == 0
				|| this.eyeColorPair.compareTo("bB") == 0) return "Brown";
		else if (this.eyeColorPair.compareTo("bb") == 0) return "Hazel";
		else return null;
	}

	public String getTailType() {
		if (this.tailTypePair.compareTo("TT") == 0
				|| this.tailTypePair.compareTo("Tt") == 0
				|| this.tailTypePair.compareTo("tT") == 0) return "Short";
		else if (this.tailTypePair.compareTo("tt") == 0) return "Long";
		else return null;
	}
	// End of attributes.
	
	// Start of pairs to return. IE "XY" for genderPair "Bb" for coat colorPair
	public String getGenderPair() {
		return this.genderPair;
	}
	
	public String getCoatColorPair() {
		return this.coatColorPair;
	}
	
	public String getEyeColorPair() {
		return this.eyeColorPair;
	}
	
	public String getTailTypePair() {
		return this.tailTypePair;
	}
	// End of pairs to return

	private String getChromosomePairFromParents(String d1Pair, String d2Pair) {

		//Getting random number between 0 and 1.
		int i = (int) (Math.random() * ((1) + 1));
		int j = (int) (Math.random() * ((1) + 1));

		//IE d1Pair = "Bb" and d2Pair = "bb", get one letter from each dog from
		//the 0th or 1st posistion of the chromosome pair string and
		//concatenate them.
		return d1Pair.charAt(i) + "" + d2Pair.charAt(j);

	}

	public String getAttributeString() {
		return this.getName() + " " + this.getGender() + " "
				+ this.getCoatColor() + " " + this.getEyeColor() + " "
				+ this.getTailType();
	}
	
	public void loadImage(ImageView dogImg) {
		if (this.getCoatColor().equals("Black") && this.getEyeColor().equals("Brown") && this.getTailType().equals("Long")) dogImg.setImageResource(R.drawable.bl_br_l);
		else if (this.getCoatColor().equals("Black") && this.getEyeColor().equals("Brown") && this.getTailType().equals("Short")) dogImg.setImageResource(R.drawable.bl_br_s);
		else if (this.getCoatColor().equals("Black") && this.getEyeColor().equals("Hazel") && this.getTailType().equals("Long")) dogImg.setImageResource(R.drawable.bl_ha_l);
		else if (this.getCoatColor().equals("Black") && this.getEyeColor().equals("Brown") && this.getTailType().equals("Short")) dogImg.setImageResource(R.drawable.bl_ha_s);
		else if (this.getCoatColor().equals("Brown") && this.getEyeColor().equals("Brown") && this.getTailType().equals("Long")) dogImg.setImageResource(R.drawable.br_br_l);
		else if (this.getCoatColor().equals("Brown") && this.getEyeColor().equals("Brown") && this.getTailType().equals("Short")) dogImg.setImageResource(R.drawable.br_br_s);
		else if (this.getCoatColor().equals("Brown") && this.getEyeColor().equals("Hazel") && this.getTailType().equals("Long")) dogImg.setImageResource(R.drawable.br_ha_l);
		else if (this.getCoatColor().equals("Brown") && this.getEyeColor().equals("Hazel") && this.getTailType().equals("Short")) dogImg.setImageResource(R.drawable.br_ha_s);
		
	}

	public Dog breed(Dog d1, Dog d2) {

		if (d1.genderPair.equals(d2.genderPair)) return null;

		this.name = d1.name + " X " + d2.name;
		this.genderPair = getChromosomePairFromParents(d1.getGenderPair(),
				d2.getGenderPair());
		this.coatColorPair = getChromosomePairFromParents(d1.getCoatColorPair(),
				d2.getCoatColorPair());
		this.eyeColorPair = getChromosomePairFromParents(d1.getEyeColorPair(),
				d2.getEyeColorPair());
		this.tailTypePair = getChromosomePairFromParents(d1.getTailTypePair(),
				d2.getTailTypePair());

		return this;

	}

}
