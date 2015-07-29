package redmine.action;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;

import com.taskadapter.redmineapi.IssueManager;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.RedmineManagerFactory;
import com.taskadapter.redmineapi.UserManager;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.IssueFactory;
import com.taskadapter.redmineapi.bean.TrackerFactory;
import com.taskadapter.redmineapi.bean.User;

public class CreateAndUpdate {
	
	public void createIssuesFromExcel(List sheetData) {
        //
        // Iterates the data and print it out to the console.
        //
    	CheckMember mem=new CheckMember();
    	boolean ismem=false;
    	String memname,worktodo;
    	Double l,issue1Id;
    	int h,IId,userId,trackerId,uId;
    	
    	System.out.println("creating");
    	String uri = "http://10.4.6.10/redmine";
	    String apiAccessKey = "2adfe4208dfed392f3ec89a123312a4f33d493ed";
	    RedmineManager mgr = RedmineManagerFactory.createWithApiKey(uri, apiAccessKey);
	    
	    IssueManager issueManager = mgr.getIssueManager();
	    UserManager manage=mgr.getUserManager();
	    TrackerFactory rt=new TrackerFactory();
	    TrackerId TID=new TrackerId();
    	
    	
    	
    	String sub,track,priorityId,updateId;
    	Issue cache=new Issue();
    	
     for (int i = 1; i < sheetData.size(); i++) {
    	// HSSFRow row = sheetData.getRow(i);
            List list = (List) sheetData.get(i);
       
            HSSFCell cellwork=(HSSFCell) list.get(0);
            HSSFCell projectId=(HSSFCell) list.get(1);
            
            worktodo=cellwork.getRichStringCellValue().getString();
            
            l=projectId.getNumericCellValue();
            h=l.intValue();
           
           
            HSSFCell cella = (HSSFCell) list.get(5);
              
            memname=cella.getRichStringCellValue().getString();
            
            ismem=mem.checkMember(memname,h);
               
                if(ismem){
                	
                    userId=mem.checkId(memname,h);
                    
                   if(worktodo.equals("create")){
                	  
                    	   
                           HSSFCell cellsub = (HSSFCell) list.get(2);
                           
                           HSSFCell cellt = (HSSFCell) list.get(6);
                           HSSFCell cellp = (HSSFCell) list.get(7);   
                           priorityId=cellp.getRichStringCellValue().getString();
                    	                  		   
                        try{
                	      User assi=manage.getUserById(userId);
                          sub=cellsub.getRichStringCellValue().getString();
                          track=cellt.getRichStringCellValue().getString();
                          Issue issueToCreate = IssueFactory.create(h,sub);
      		              Issue createdIssue = issueManager.createIssue( issueToCreate);
      		              cache=createdIssue;
      		              trackerId=TID.findTID(track);
      		              createdIssue.setTracker(rt.create(trackerId,track));
      		              createdIssue.setPriorityId(TID.findPriority(priorityId));
      		              createdIssue.setStatusId(1);
      		              createdIssue.setAssignee(assi);
      		              
      		              issueManager.update(createdIssue);
             
                
                           }catch(RedmineException z){} 
                	    }
              
                	
                      if(worktodo.equals("update")){
                    	  
                    	  
                    	 
                    	  HSSFCell cellp = (HSSFCell) list.get(7);   
                          priorityId=cellp.getRichStringCellValue().getString();
                    	  
            	          StatusMapper map=new StatusMapper();
                          HSSFCell cellIssueId = (HSSFCell) list.get(3);
                          HSSFCell cellStatus = (HSSFCell) list.get(4);
                          try{
                           User assig=manage.getUserById(userId);
                           
                           issue1Id=cellIssueId.getNumericCellValue();
                     	   IId=issue1Id.intValue();
                     	   
                           Issue issu = issueManager.getIssueById(IId);
                     	   cache=issu;
                     	
                     	   cache.setPriorityId(TID.findPriority(priorityId));
                     	   cache.setAssignee(assig);
                     
                     	
                     	   updateId=cellStatus.getRichStringCellValue().getString();
                     	   uId=map.mapper(updateId);
                     
                     	   cache.setStatusId(uId);
                   		   issueManager.update(cache);}catch(RedmineException d){}
                     
                      }
                     
                      }//ismem

       
        }//for
        
    }//function
	
}//class
