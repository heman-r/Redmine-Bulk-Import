package redmine;



import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.json.JSONException;
import org.json.JSONObject;

import com.taskadapter.redmineapi.Include;
import com.taskadapter.redmineapi.IssueManager;

import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.RedmineManagerFactory;
import com.taskadapter.redmineapi.bean.*;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.User;
//import redmine.action.*;

@Path("/getissues/{projectId}") 
	  public class GetIssues {
			 
    @GET
	  @Produces("application/json")
	  public Response convertFtoCfromInput(@PathParam("projectId") String projectId) throws JSONException {
			  String issueobj="",id,userName,pID,sID,subject,pId,sId;
              int ID;
		  String uri = "http://10.4.6.10/redmine";
		    String apiAccessKey = "2adfe4208dfed392f3ec89a123312a4f33d493ed";
		    System.out.println(projectId);
		    Integer queryId = null; // any
		    RedmineManager mgr = RedmineManagerFactory.createWithApiKey(uri, apiAccessKey);
		    
		    //IssueManager issueManager = mgr.getIssueManager();
		    
		    try{
		    IssueManager issueManager = mgr.getIssueManager();
		    
		    List<Issue> issues = issueManager.getIssues(projectId, queryId);
			
		    for (Issue issue : issues) {
		    	
		    	pId=issue.getPriorityText();
		    	sId=issue.getStatusName();
		    	ID=issue.getId();
		    	subject=issue.getSubject();
		       issueobj=issueobj+"Isssue ID:"+ID+"    Subject:  "+subject+"           Priority: "+pId+"         Status:"+sId;
		       
		       issueobj=issueobj+"\n\n";
		    }
  		    
		    }catch(RedmineException t){}
		  
		  
		  
		JSONObject jsonObject = new JSONObject();
		
		jsonObject.put("value", issueobj);
        System.out.println("Working");
		String result = ""+ jsonObject;
		return Response.status(200).entity(result).build();
		
	  }
	  

}
