package me.curiosus.nback.algos;

import java.util.*;

/**
 * Created by jepeterson@gmail.com on 2/14/15.
 */
public class TurnAndMatchCalculator {

	private static final int BASE_LEVEL = 20;
	private static final int MATCHES = 6;
	private static final int VALUE_CEILING = 9;
	private static final Random random = new Random(System.currentTimeMillis());

	public int [] calculateTurns(int nBack) {
		List<Integer> idxs = new ArrayList<>(MATCHES);
		List<Integer> matchValues = new ArrayList<>(MATCHES);
		for (int i = 0; i < MATCHES; i++) {
			int idx = nextIdx(nBack);
			while (idxs.contains(idx)) {
				idx = nextIdx(nBack);
			}
			idxs.add(idx);
			Integer value = Integer.valueOf(random.nextInt(VALUE_CEILING));
			matchValues.add(value);
		}

		int [] turns = new int [BASE_LEVEL + nBack];
		for (int i = 0; i < BASE_LEVEL + nBack; i++) {
			turns[i] = -1;
		}

		for (int i = 0; i < idxs.size(); i++) {
			Integer value = matchValues.get(i);
			Integer idx = idxs.get(i);
			Integer idxBack = idx - nBack;
			turns[idxBack] = value;
			turns[idx] = value;
		}

		for (int i = 0; i < turns.length; i++) {
			if (turns[i] == -1) {
				int value = random.nextInt(VALUE_CEILING);
				while (matchValues.contains(value)) {
					value = random.nextInt(VALUE_CEILING);
				}
				turns[i] = value;
			}
		}

		return turns;
	}


	private int nextIdx(int nBack) {
		int matchIndex = random.nextInt(BASE_LEVEL + nBack);
		if (matchIndex < nBack) {
			matchIndex = nBack + 1;
		}
		return matchIndex;
	}

	public static void main(String[] args) {
		TurnAndMatchCalculator turnAndMatchCalculator = new TurnAndMatchCalculator();

		for (int i = 1; i < 9; i++) {
			int[] turns = turnAndMatchCalculator.calculateTurns(i);
			System.out.printf("%d nBack  ", i);
			for (int j = 0; j < turns.length; j++) {
				System.out.printf("%d, ", turns[j]);
			}
			System.out.println();
			System.out.println();
		}
		System.out.println("done");
	}
}
