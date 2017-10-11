package com.uxin.hadoop.monitor.hadoopservice.model;

public class applicationUrlParamsModel {
	public String state;// [deprecated] - state of the application
	public String states;// applications matching the given application states,
							// specified as a comma-separated list.
	public String finalStatus;// the final status of the application - reported
								// by the application itself
	public String user;// user name
	public String queue;// queue name
	public int limit;// total number of app objects to be returned
	public long startedTimeBegin;// applications with start time beginning with
									// this time, specified in ms since epoch
	public long startedTimeEnd;// applications with start time ending with this
								// time, specified in ms since epoch
	public long finishedTimeBegin;// applications with finish time beginning
									// with this time, specified in ms since
									// epoch
	public long finishedTimeEnd;// applications with finish time ending with
								// this time, specified in ms since epoch
	public String applicationTypes;// applications matching the given
									// application types, specified as a
									// comma-separated list.
	public String applicationTags;// applications matching any of the given
									// application tags, specified as a
									// comma-separated list.
}
