public class Main {

    public static void main (String[] args) {
	// The random seed must be fixed, so that the tests are the same for everyone
	// and are reproducible.
	new StableMatchingTest (
	    new StableMatchingA (),
      //new StableMatchingB (),
	    System.out,
	    new java.util.Random (0L) // The program should also work correctly for other values of random seed - try specifying a different integer
	).test('A');
    }

}
