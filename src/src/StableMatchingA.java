
public class StableMatchingA implements StableMatchingInterface {

	public class menGroup {
		int id;
		int totalNumber;    // total number of men in the group
		int unengagedNumber;    // number of unengaged men in the group
		int[] prefs;     // list of preferences of the group of men
		int nextProposition;   // index in their preference list of the next women group they will make a proposition : next womenGroup = menPrefs[id][nextProposition]

		public menGroup(int id, int[] menGroupCount, int[][] menPrefs) {
			this.id = id;
			this.totalNumber = menGroupCount[id];
			this.unengagedNumber = menGroupCount[id];  // they all begin unengaged
			this.prefs = menPrefs[id];
			this.nextProposition = 0;
		}
	}


	public class womenGroup {
		int id;
		int totalNumber;   // total number of women in the group
		int unengagedNumber;   // number of unengaged women in the group
		int[] grades;     // tab of grades the women group attributes to each men group : the i-th men group in their preference list has the grade i (j = womenPrefs[id][i]  <=>  grades[j] = i)
		int worstProposition;  // index of the worst men group who some women of the group are engaged with

		public womenGroup(int id, int[] womenGroupCount, int[][] womenPrefs) {
			this.id = id;
			this.totalNumber = womenGroupCount[id];
			this.unengagedNumber = womenGroupCount[id];  // they all begin unengaged
			this.grades = new int[womenPrefs[id].length];  // j = womenPrefs[id][i]  <=>  grades[j] = i
 			for (int i=0; i<this.grades.length; i++) {
				int j = womenPrefs[id][i];
				this.grades[j] = i;
			}
			this.worstProposition = -1;  // initialization value
		}
	}

	// refresh the worst proposition a women group can have : we go up in their preference list until we find another men group who some women of the group are engaged with, this men group will become their new worst proposition
	public void updateWorstProposition(womenGroup womenGroup, int[][] mar, int[] prefs, int worstProposition, int men) {
		int worst = prefs[womenGroup.grades[worstProposition] - 1];
		while (mar[worst][womenGroup.id] == 0 && womenGroup.grades[worst] > womenGroup.grades[men])
			worst = prefs[womenGroup.grades[worst] - 1];
		womenGroup.worstProposition = worst;
	}

	// return the next men group to study in order to optimize time complexity
	public int nextMenGroup(menGroup[]  men, int totalUnengagedMen, int m) {
		int i = 0;
		while (i<m-1 && men[i].unengagedNumber <= totalUnengagedMen/(2*m))   //TODO : montrer que ça termine bien
			i++;
		return i;
	}


	// return the matrix M, with M[i][j] denoting the number of of men from the i-th group of men engaged to women from the j-th group of women
	public int[][] constructStableMatching (int[] menGroupCount, int[] womenGroupCount, int[][] menPrefs, int[][] womenPrefs){
		// initialization
		int m = menGroupCount.length;
		int w = womenGroupCount.length;
		int[][] mar = new int[m][w];
		menGroup[] men = new menGroup[m];
		womenGroup[] women = new womenGroup[w];

		int n = 0;
		for (int i=0; i<m; i++) {
			men[i] = new menGroup(i, menGroupCount, menPrefs);
			n += menGroupCount[i];
		}
		for (int i=0; i<w; i++)
			women[i] = new womenGroup(i, womenGroupCount, womenPrefs);

		int totalUnengagedMen = n;

		while (totalUnengagedMen != 0) {
			int i = nextMenGroup(men, totalUnengagedMen, m);  // choice of the next group of men to study
			int a = men[i].unengagedNumber;
			boolean success = false;   // success of the proposal

			while (!success) {
				womenGroup womenGroup = women[menPrefs[i][men[i].nextProposition]];   // next group of women on their preference list

				// proposition to women still unengaged
				if (womenGroup.unengagedNumber != 0) {
					int b = womenGroup.unengagedNumber;
					int c = Math.min(a,b);
					mar[i][womenGroup.id] += c;
					womenGroup.unengagedNumber = womenGroup.unengagedNumber - c;
					men[i].unengagedNumber -= c;
					totalUnengagedMen -= c;
					success = true;
					if (womenGroup.worstProposition == -1 || womenGroup.grades[i] > womenGroup.grades[womenGroup.worstProposition])
						womenGroup.worstProposition = i;     // update of the worst proposition of this women group
				}

				// proposition to women who are engaged with men they like less than the men of men[i]
				else if (womenGroup.grades[i] < womenGroup.grades[womenGroup.worstProposition]) {
					int b = mar[womenGroup.worstProposition][womenGroup.id];
					int c = Math.min(a,b);
					mar[i][womenGroup.id] += c;
					men[i].unengagedNumber -= c;
					mar[womenGroup.worstProposition][womenGroup.id] -= c;   // women break-up with the men they like less
					men[womenGroup.worstProposition].unengagedNumber += c;
					success = true;
					if (b <= a)  // update of the worst proposition of the women group
						updateWorstProposition(womenGroup,mar,womenPrefs[womenGroup.id],womenGroup.worstProposition,men[i].id);
				}

				// the proposition of men[i] to womenGroup fails
				else {
					men[i].nextProposition++;  //no man from men[i] will ever propose to a woman from womenGroup in the future
				}
			}
		}
		return mar;
	 }
}
