package showtracker;

import java.util.HashMap;

public enum Rating {

	NO_RATING	(0, "No rating"),
	ONE_STAR	(1, "★"),
	TWO_STARS	(2, "★★"),
	THREE_STARS	(3, "★★★"),
	FOUR_STARS	(4, "★★★★"),
	FIVE_STARS	(5, "★★★★★");

	private static final Rating[] intSet = new Rating[6];
	private static final HashMap<String, Rating> strMap = new HashMap<>(6);

	private final int 	 intVal;
	private final String strVal;

	Rating(int nVal, String sVal) {
		intVal = nVal;
		strVal = sVal;

	}

	public int getIntValue() {
		return intVal;
	}

	public String getStrValue() {
		return strVal;
	}

	public static Rating get(int rating) {
		if (rating < 0 || rating > 5)
			return NO_RATING;

		if (intSet[0] == null) {
			intSet[0] = NO_RATING;
			intSet[1] = ONE_STAR;
			intSet[2] = TWO_STARS;
			intSet[3] = THREE_STARS;
			intSet[4] = FOUR_STARS;
			intSet[5] = FIVE_STARS;
		}

		return intSet[rating];
	}

	public static Rating get(String rating) {
		if (rating == null)
			return NO_RATING;

		if (!strMap.containsKey("No rating")) {
			strMap.put("No rating", NO_RATING);
			strMap.put("★", ONE_STAR);
			strMap.put("★★", TWO_STARS);
			strMap.put("★★★", THREE_STARS);
			strMap.put("★★★★", FOUR_STARS);
			strMap.put("★★★★★", FIVE_STARS);
		}

		Rating result = NO_RATING;
		if (strMap.containsKey(rating))
			result = strMap.get(rating);

		return result;
	}

}
