package jenkins.plugins.build_flow_stats;

import java.text.DecimalFormat;

public class BuildResults {

	private int successes;
	private int failures;
	private int aborts;
	private int unstables;
	private int nobuilds;
	private int totalbuilds;

	public BuildResults() {
		successes = 0;
		failures = 0;
		aborts = 0;
		unstables = 0;
		nobuilds = 0;
		totalbuilds = 0;
	}

	public void addSuccess() {
		successes ++;
		totalbuilds ++;
	}

	public void addFailure() {
		failures ++;
		totalbuilds ++;
	}

	public void addAbort() {
		aborts ++;
		totalbuilds ++;
	}

	public void addUnstable() {
		unstables ++;
		totalbuilds ++;
	}

	public void addNoBuild() {
		nobuilds ++;
		totalbuilds ++;
	}

	public double getFailureRate() {
		if (totalbuilds == 0) {
			return 0;
		} else {
			return Double.valueOf(new DecimalFormat("#.##").format((double)failures/totalbuilds*100)); //For max 2 decimals
		}	
	}

	public String toString() {
		return "[Successes: " + successes + ", Failures: " + failures + ", Aborts: " + aborts 
		+ ", Unstables: " + unstables + ", Not Built: " + nobuilds + ", Total Builds: " + totalbuilds 
		+ ", Failure Rate: " + getFailureRate() + "%]";
	}
}