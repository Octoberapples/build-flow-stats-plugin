package jenkins.plugins.build_flow_stats;

import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import jenkins.*;
import jenkins.model.*;
import hudson.*;
import hudson.model.*;

import com.cloudbees.plugins.flow.FlowRun;
import com.cloudbees.plugins.flow.JobInvocation;
import java.util.concurrent.ExecutionException;

public class StoreData {

	public static void storeBuildInfoToXML(PrintStream stream, String jobName, Date startDateObject, String startDate) {
		stream.println("Collect and store data to XML-file for " + jobName);
			
		Date endDateObject = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
		String endDate = sdf.format(endDateObject);

		stream.println("Collecting data from " + startDate + " to " + endDate);

		//Create path for storing data
		Jenkins jenkins = Jenkins.getInstance();
		String rootDir = jenkins.getRootDir().toString();
		String storePath = rootDir + "/userContent/build-flow-stats/" + jobName + "/";
		new File(storePath).mkdirs();

		//TODO: This should be made in a more general way with different names for different dates being generated automatically
		String filename = endDate + ".xml";

		// Get project object 
		Project project = (Project) jenkins.getItem(jobName);

	  	try {
	  		File file = new File(storePath + filename);
			BufferedWriter output = new BufferedWriter(new FileWriter(file));
			output.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
			output.newLine();
			output.write("<Builds>");
			//Recursively add all builds from the job
			Iterator<Build> runIterator = project.getBuilds().byTimestamp(startDateObject.getTime(),endDateObject.getTime()).iterator();;
		  	while (runIterator.hasNext()) {
		  		writeBuildToXML(runIterator.next(), 1, output);
		 	}
		  	output.newLine();
		  	output.write("</Builds>");
		  	output.close();
	  	} catch (IOException e) {
	          e.printStackTrace();
		}
	}

	public static void writeBuildToXML(Build build, int tabLevel, BufferedWriter output) throws IOException {
		output.newLine();
		if (build.getClass().toString().equals("class com.cloudbees.plugins.flow.FlowRun")) {
			FlowRun flowBuild = (FlowRun) build;
			output.write(createTabLevelString(tabLevel) + "<FlowBuild>");
			writeFlowBuildInfoToXML(flowBuild, tabLevel + 1, output);
			output.newLine();
			output.write(createTabLevelString(tabLevel) + "</FlowBuild>");
		} else {
	    	output.write(createTabLevelString(tabLevel) + "<Build>");
	    	writeBuildInfoToXML(build, tabLevel + 1, output);
	    	output.newLine();
	    	output.write(createTabLevelString(tabLevel) + "</Build>");
		}
	}

	public static void writeFlowBuildInfoToXML(FlowRun flowBuild, int tabLevel, BufferedWriter output) throws IOException {
		addJobNameToXML(flowBuild, tabLevel, output);
		addBuildNumberToXML(flowBuild, tabLevel, output);
		addDateToXML(flowBuild, tabLevel, output);
		addResultToXML(flowBuild, tabLevel, output);

		Iterator<JobInvocation> subBuilds = flowBuild.getJobsGraph().vertexSet().iterator();
		while (subBuilds.hasNext()) {
			try {
				Build subBuild = (Build) subBuilds.next().getBuild();
				if (subBuild != null && !subBuild.getParent().getFullName().equals(flowBuild.getParent().getFullName())) {
					writeBuildToXML(subBuild, tabLevel + 1, output);
				}
			} catch (ExecutionException ee) {} //Fix the exception handling
			catch (InterruptedException ie) {} //Fix the exception handling
		}
	}

	public static void writeBuildInfoToXML(Build build, int tabLevel, BufferedWriter output) throws IOException {
	  addJobNameToXML(build, tabLevel, output);
	  addBuildNumberToXML(build, tabLevel, output);
	  addDateToXML(build, tabLevel, output);
	  addResultToXML(build, tabLevel, output);
	  if (!build.getResult().toString().equals("SUCCESS")) {
	  	addFailureCauseToXML(build.getResult().toString(), tabLevel, output);
	  }
	}

	public static void addJobNameToXML(Build build, int tabLevel, BufferedWriter output) throws IOException {
		output.newLine();
		output.write(createTabLevelString(tabLevel) + "<JobName>" + build.getParent().getFullName() + "</JobName>");
	}

	public static void addBuildNumberToXML(Build build, int tabLevel, BufferedWriter output) throws IOException {
		output.newLine();
		output.write(createTabLevelString(tabLevel) + "<BuildNumber>" + build.getNumber() + "</BuildNumber>");
	}

	public static void addDateToXML(Build build, int tabLevel, BufferedWriter output) throws IOException {
		output.newLine();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
		output.write(createTabLevelString(tabLevel) + "<Date>" + sdf.format(build.getTime()) + "</Date>");
	}

	public static void addResultToXML(Build build, int tabLevel, BufferedWriter output) throws IOException {
		output.newLine();
		output.write(createTabLevelString(tabLevel) + "<Result>" + build.getResult() + "</Result>");
	}

	//TODO: Make this much neater. Of course it will have to be rewritten anyway because of the machiune learning part.
	public static void addFailureCauseToXML(String result, int tabLevel, BufferedWriter output) throws IOException {
	  	//The logs should eventually be analysed instead of this mumbojumbo of course
		output.newLine();
		String failureCause = "";
	    if (result.equals("UNSTABLE")) {
	    	failureCause = "Unstable Build";
	    } else if (result.equals("NOT_BUILT")) {
	   		failureCause = "Not Built";
	    } else if (result.equals("ABORTED")) {
	    	failureCause = "Aborted";
	    } else {
	    	double random = Math.random();
	    	if (random < 0.25) {
	    		failureCause = "Temporary fail explanation 1"; 
	    	} else if (0.25 <= random && random < 0.5) {
	    		failureCause = "Temporary fail explanation 2"; 
	      	} else if (0.5 <= random && random < 0.75) {
	        	failureCause = "Temporary fail explanation 3"; 
	      	} else {
	        	failureCause = "Temporary fail explanation 4"; 
	    	}
	    }
		output.write(createTabLevelString(tabLevel) + "<FailureCause>" + failureCause + "</FailureCause>");
	}

	public static String createTabLevelString(int i) {
		String tabString = "";
		while (i > 0) {
			tabString += "\t";
			i -= 1;
		}
		return tabString;
	}

}