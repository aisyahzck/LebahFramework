/* ************************************************************************
LEBAH PORTAL FRAMEWORK
Copyright (C) 2007  Shamsul Bahrin

* ************************************************************************ */



package lebah.app;

import java.util.Date;
import java.util.Vector;

import lebah.util.CDate;
import lebah.util.DateTool;

public class Task {
	
	private String projectId;
	private String id;
	private String name;
	private Date startDate;
	private Date endDate;
	private int lengthInDays;
	private String taskColor;
    private String[] assignees;
    private int percentOfCompletion;

	/**
	 * @return Returns the endDate.
	 */
	public Date getEndDate() {
		return new CDate(endDate);
	}
	/**
	 * @param endDate The endDate to set.
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return Returns the lengthInDays.
	 */
	public int getLengthInDays() {
		return lengthInDays;
	}
	/**
	 * @param lengthInDays The lengthInDays to set.
	 */
	public void setLengthInDays(int lengthInDays) {
		this.lengthInDays = lengthInDays;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the projectId.
	 */
	public String getProjectId() {
		return projectId;
	}
	/**
	 * @param projectId The projectId to set.
	 */
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	/**
	 * @return Returns the startDate.
	 */
	public Date getStartDate() {
		return new CDate(startDate);
	}
	/**
	 * @param startDate The startDate to set.
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public String getStartDateStr() {
		return DateTool.getDateFormatted(startDate);
	}
	
	public String getEndDateStr() {
		return DateTool.getDateFormatted(endDate);
	}
	/**
	 * @return Returns the taskColor.
	 */
	public String getTaskColor() {
		return taskColor;
	}
	/**
	 * @param taskColor The taskColor to set.
	 */
	public void setTaskColor(String taskColor) {
		this.taskColor = taskColor;
	}
    public String[] getAssignees() {
        return assignees;
    }
    public void setAssignees(String[] assignees) {
        this.assignees = assignees;
    }	
	public void setAssignees(Vector assignees) {
        //this.assignees = (String[]) assignees.toArray();
        this.assignees = new String[assignees.size()];
        for ( int i=0; i < assignees.size(); i++ )
            this.assignees[i] = (String) assignees.elementAt(i);
    }
    
    public boolean hasAssignee(String id) {
        if ( assignees == null ) return false;
        for ( int i=0; i < assignees.length; i++ ) {
           if ( id.equals(assignees[i])) return true;
        }
        return false;
    }
    public int getPercentOfCompletion() {
        return percentOfCompletion;
    }
    public void setPercentOfCompletion(int percentOfCompletion) {
        this.percentOfCompletion = percentOfCompletion;
    }

}
