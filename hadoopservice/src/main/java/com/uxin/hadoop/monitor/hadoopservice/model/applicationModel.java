package com.uxin.hadoop.monitor.hadoopservice.model;

public class applicationModel {
	public String id;// The application id
	public String user;// The user who started the application
	public String name;// The application name
	public String queue;// The queue the application was submitted to
	public String state;// The application state according to the
						// ResourceManager - valid values are members of the
						// YarnApplicationState enum: NEW, NEW_SAVING,
						// SUBMITTED, ACCEPTED, RUNNING, FINISHED, FAILED,
						// KILLED
	public String finalStatus;// The final status of the application if finished
								// - reported by the application itself - valid
								// values are: UNDEFINED, SUCCEEDED, FAILED,
								// KILLED
	public float progress;// The progress of the application as a percent
	public String trackingUI;// Where the tracking url is currently pointing -
								// History (for history server) or
								// ApplicationMaster
	public String trackingUrl;// The web URL that can be used to track the
								// application
	public String diagnostics;// Detailed diagnostics information
	public long clusterId;// The cluster id
	public String applicationType;// The application type
	public String applicationTags;// applications matching any of the given
									// application tags, specified as a
									// comma-separated list.
	public long startedTime;// The time in which application started (in ms
							// since epoch)
	public long finishedTime;// The time in which the application finished (in
								// ms since epoch)
	public int elapsedTime;// The elapsed time since the application started (in
							// ms)
	public String amContainerLogs;// The URL of the application master container
									// logs
	public String amHostHttpAddress;// The nodes http address of the application
									// master
	public int allocatedMB;// The sum of memory in MB allocated to the
							// application’s running containers
	public int allocatedVCores;// The sum of virtual cores allocated to the
								// application’s running containers
	public int runningContainers;// The number of containers currently running
									// for the application
	public long memorySeconds;// The amount of memory the application has
								// allocated (megabyte-seconds)
	public long vcoreSeconds;// The amount of CPU resources the application has
								// allocated (virtual core-seconds)
	public int preemptedResourceMB;//
	public int preemptedResourceVCores;//
	public int numNonAMContainerPreempted;//
	public int numAMContainerPreempted;//

}
