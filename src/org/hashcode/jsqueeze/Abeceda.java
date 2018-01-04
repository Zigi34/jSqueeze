package org.hashcode.jsqueeze;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class Abeceda {
	private List<Code> codes = new LinkedList<>();

	public void add(Character sign, Double prob) {
		codes.add(new Code(sign, prob));
	}

	public Character getSign(int index) {
		return codes.get(index).getSign();
	}

	public Double getProbability(int index) {
		return codes.get(index).getProb();
	}

	public double getEntropy() {
		Double val = 0.0;
		for (int i = 0; i < codes.size(); i++) {
			val += codes.get(i).getProb() * log2(codes.get(i).getProb());
		}
		return -val;
	}

	private void sort() {
		codes.sort(new Comparator<Code>() {
			@Override
			public int compare(Code o1, Code o2) {
				if (o1.getProb() > o2.getProb())
					return 1;
				else if (o1.getProb() < o2.getProb())
					return -1;
				else
					return 0;
			}

		});
	}

	public void generatePrefix() {
		sort();
		prefix(codes, null);
	}

	private void prefix(List<Code> codes, Character sign) {
		if (sign != null) {
			for (Code code : codes) {
				code.setPrefix(code.getPrefix() + sign);
			}
		}
		if (codes.size() > 1) {
			double maxVal = 0.0;
			for (Code code : codes) {
				maxVal += code.getProb();
			}

			List<Double> abs = new LinkedList<>();
			Double cumulative = 0.0;
			for (Code code : codes) {
				cumulative += code.getProb();
				abs.add(Math.abs(maxVal / 2.0 - cumulative));
			}

			int index = 0;
			Double minVal = 0.0;
			for (Double val : abs) {
				if (val < minVal) {
					minVal = val;
					index = abs.indexOf(val);
				}
			}

			List<Code> code1 = new LinkedList<Code>();
			for (int i = 0; i <= index; i++) {
				code1.add(codes.get(i));
			}

			prefix(code1, '1');

			List<Code> code2 = new LinkedList<Code>();
			for (int i = index + 1; i < codes.size(); i++) {
				code2.add(codes.get(i));
			}

			prefix(code2, '0');
		}
	}

	public Double getRedundance() {
		return log2(codes.size()) - getEntropy();
	}

	private double log2(double x) {
		return Math.log(x) / Math.log(2.0);
	}

	public List<Code> getCodes() {
		return codes;
	}

}
