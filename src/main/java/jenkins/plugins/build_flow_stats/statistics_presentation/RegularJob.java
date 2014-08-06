package jenkins.plugins.build_flow_stats;

import java.util.ArrayList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Contains the relevant information for a regular job i.e. a job that 
 * is not a job that uses the Build Flow plugin. 
 */
public class RegularJob extends Job {
	
	protected FailureCauseList failureCauses;

	public RegularJob(String jobName) {
		super(jobName);
		failureCauses = new FailureCauseList();
	}

	public void addBuildFromXML(Node nonFlowBuildNode, FailureCauseList allFailureCauses) {
		Element nonFlowBuildElement = (Element) nonFlowBuildNode;
		String result = nonFlowBuildElement.getElementsByTagName("Result").item(0).getTextContent();
		addResultForBuild(result);
		if (!result.equals("SUCCESS")) {
			String failureCause = nonFlowBuildElement.getElementsByTagName("FailureCause").item(0).getTextContent();
			String buildNumber = nonFlowBuildElement.getElementsByTagName("BuildNumber").item(0).getTextContent();
			failureCauses.addFailureCauseForBuild(jobName, failureCause, buildNumber);
			allFailureCauses.addFailureCauseForBuild(jobName, failureCause, buildNumber);
		}	
	}

	public void createBuildTree(int tabLevel, BuildTree buildTree) {
		super.createBuildTree(tabLevel, buildTree);
		failureCauses.createBuildTree(tabLevel+1, buildTree);
	}
		
}