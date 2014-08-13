#Build Flow Stats - A Jenkins Plugin 
##Introduction
The build flow stats is a plugin for visualizing build statistics for builds that use the [Build Flow Plugin](https://wiki.jenkins-ci.org/display/JENKINS/Build+Flow+Plugin). The plugin can visualize build flows using an indentation presentation view described further down in this document. The plugin stores and presents the build results for a flow build and all of its sub builds (even additional flow builds) as well as the failure causes for unstable and failed sub builds.

When searching for problems that result in unsuccessful builds one might manually read through the build logs and search for failure causes. This may be very time consuming and inefficient, especially if there are lots of big build logs for the different jobs. It will probably also be hard to get an overview of the current problems. The plugin _Build Flow Stats Plugin_ was created in order to help Jenkins users with this issue. The user can add failure causes that the plugin matches with builds. This can help visualizing the problems that should be prioritized.

The plugin adds a build step called _Store Flow Build Statistics_ that can collect data for different jobs. A link to the main part of the plugin, where it is possible to present and delete data and edit failure causes, can be found when pressing the _Manage Jenkins_-button from the Jenkins main page.

##How to use the plugin

###Data Collection
The build step that the plugin provides is responsible for collecting data. Start with creating a job that should collect data by pressing the button _New Item_ from the side panel in the Jenkins main page. Give the job a name and use a free-style-project. In the view for the new job, press the button _Configure_ and press the button _Add build step_. Choose the option _Store Flow Build Statistics_ from the options in the list. A view will be added that should look something like this:

![]( http://i.imgur.com/aYrZMjA.jpg)

Choose the job that you want to collect data from and the first date that data should be collected from. Data is collected when the data collection job is built. 

###Get to the Plugin Main View
Get to the main view of the plugin by pressing the _Manage Jenkins_-button from the Jenkins main page. Look for a button that looks like this and press it:

![](http://i.imgur.com/HpcFVPS.jpg)

###Data Presentation

In the main view there is a section called _Data Presentation_. All jobs that  data has been collected for are available in the list with jobs. Choose the job and the time period and press the button _Present Data_ to get to the presentation view.

![]( http://i.imgur.com/dcHAZAf.jpg) 

If data is presented for a job with sub jobs the plugin will present data of the job as a build tree. The tree contains build statistics for the main job and all its sub jobs. The failure causes for the regular jobs can also be seen in the tree. Under the section _Most Common Failure Causes_ the most common failure causes are listed. In this section they are not divided into different jobs as in the tree. The presentation for a job with sub jobs can be seen in the following picture:

![](http://i.imgur.com/RR7b3gQ.jpg)

If the selected job is not a _Flow Build Project_ build statistics and failure causes will be presented for the job as is shown in the following picture:

![](http://i.imgur.com/odp3T8g.jpg)

###Data Deletion
The section _Delete Data_ is available in the main view of the plugin. Choose the job and time period to delete data for and press the _Erase Selected Data_-button to remove the data.

![](http://i.imgur.com/cDoFijE.jpg)

###Edit Failure Causes 
In order for the plugin to be able to match failure causes with builds the user has to add them. This can be done under the section _Failure Analysis_ in the main view.

![](http://i.imgur.com/xx5a723.jpg)

When storing data the plugin uses the failure causes listed in the view called _Failure Causes Editor_. Failure causes can be added, deleted and edited. They should have a name, a description and one or several patterns. When collecting data the plugin will search for the pattern (or patterns) in the build log and match the failure causes with the builds. Press the button _Save Changes_ to save the changes that were made.

![](http://i.imgur.com/SdNhIYo.jpg)

##Tips
* All data is stored in a folder called _flow-build-stats_ located directly under the main Jenkins folder. This includes data for the failure causes and collected data for the jobs.
* The collecting of data is separated from the other parts of the plugin, meaning that no data collection is being done when the user is in the main view of the plugin. This means that in order for newly added failure causes to be detected, the data needs to be collected again.
* XML-files are used for storage of data and as long as the data is in the right format (with the right names of the files) the plugin will recognize and use the data. It is therefore possible to delete and edit data using the file system.
* By the current implementation the first failure cause that is found in the log is the one that is considered to be the failure cause of that build. This means that each build can only have one failure cause.
* By the current implementation not-built and aborted builds will be listed in the build tree as a failure cause. This means that only unstable and failed builds are analysed for user-defined failure causes by the failure analyser. Builds that the analyser are unable to match with any failure cause will get the failure cause _Unknown failure cause_.
