import java.util.Arrays;

public class StableMatching implements StableMatchingInterface {
	int[][] men;
	int[][] women;


	// default constructor
	StableMatching() {
		this.men = new int[0][0];
		this.women = new int[0][0];
	}

	// constructor for m men and w women, they are all unengaged
	StableMatching(int m, int w) {
		this.men = new int[w][m];
		this.women = new int[m][w];
	}

	// return the group containing the most numerous unengaged men and the number of
	// unengaged men of this group
	int[] maxIndex(int[][] men, int[] menGroupCount) {
		int maxIndex = 0;
		int max = 0;

		for (int i = 0; i < men.length; i++) {
			int engagedMen = engagedPeople(men[i]); // number of men engaged
			if (menGroupCount[i] - engagedMen > max) {
				max = menGroupCount[i] - engagedMen;
				maxIndex = i;
			}
		}
		return new int[] { maxIndex, max };
	}

	// return the number of engaged people in a group
	int engagedPeople(int[] people) {
		int count = 0;
		for (int j = 0; j < people.length; j++)
			count += people[j];
		return count;
	}

	// return the index of the element x in an array tab
	int indexOf(int[] tab, int x) {
		for (int i = 0; i < tab.length; i++) {
			if (tab[i] == x)
				return i;
		}
		return -1;
	}

	// return a copy of tab[l...h[
	int[] arrayCopy(int[] tab, int l, int h) {
		int[] newTab = new int[h - l];
		for (int i = l; i < h; i++)
			newTab[i - l] = tab[i];
		return newTab;
	}

	// useful to print your tab
	String toString(int[][] tab) {
		String s = "[";
		for (int i = 0; i < tab.length; i++) {
			s += "[";
			for (int j = 0; j < tab[i].length; j++) {
				s += tab[i][j] + ", ";
			}
			s += "],";
		}
		s += "]";
		return s;
	}

	// return the sum of all the elements of a tab
	int count(int[][] tab) {
		int count = 0;
		for (int i = 0; i < tab.length; i++) {
			for (int j = 0; j < tab[i].length; j++)
				count += tab[i][j];
		}
		return count;
	}

	// return the Stable Matching with grouped preferences
	public int[][] constructStableMatching(int[] menGroupCount, int[] womenGroupCount, int[][] menPrefs,
			int[][] womenPrefs) {
		int m = menGroupCount.length;
		int w = womenGroupCount.length;
		int n = 0;
		for (int i = 0; i < menGroupCount.length; i++) // n = total number of men and women
			n += menGroupCount[i];

		StableMatching SM = new StableMatching(m, w); // everyone is unengaged at beginning

		int count = count(SM.men);

		while (count != n) {
			int i = maxIndex(SM.men, menGroupCount)[0]; // index of the group containing the most unengaged men
			int a = maxIndex(SM.men, menGroupCount)[1]; // number of unengaged men of this group
			boolean success = false; // success of the proposal
			int j = 0;

			while (!success) {

				int womenGroup = menPrefs[i][j]; // j-th group of women on the list of the group i of men
				for (int x = 0; x < SM.women[womenGroup].length; x++) {
					int b = SM.women[womenGroup][x]; // number of women engaged with their x-th choice
					if (b != 0 && indexOf(womenPrefs[womenGroup], x) > indexOf(womenPrefs[womenGroup], i)) { // some women are engaged with a man they like less -> they got married with man x
						SM.women[womenGroup][x] -= Math.min(a, b);
						SM.men[x][womenGroup] -= Math.min(a, b);
						SM.women[womenGroup][i] += Math.min(a, b);
						SM.men[i][womenGroup] += Math.min(a, b);
						success = true;
					}
				}

				if (!success) {

					int b = womenGroupCount[womenGroup] - engagedPeople(SM.women[womenGroup]); // number of unengaged women -> some got married with man x
					SM.women[womenGroup][i] += Math.min(a, b);
					SM.men[i][womenGroup] += Math.min(a, b);
					if (b != 0)
						success = true;
				}
				j++;
			}
			count = count(SM.men);

		}
		return SM.men;
	}



}
