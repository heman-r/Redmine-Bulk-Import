package redmine.action;


import java.util.List;
import com.taskadapter.redmineapi.MembershipManager;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.RedmineManagerFactory;
import com.taskadapter.redmineapi.bean.Membership;
import com.taskadapter.redmineapi.bean.User;

public class CheckMember {
	String name;
	  String uri = "http://10.4.6.10/redmine";
	    String apiAccessKey = "2adfe4208dfed392f3ec89a123312a4f33d493ed";
	    String projectKey = "bulkimporttestproj";
	    
	    public boolean checkMember(String uname,int projId){
	    
	    	boolean isMem=false;
	    	String checkName=uname,test;
	    
	    try{
	    RedmineManager mgr = RedmineManagerFactory.createWithApiKey(uri, apiAccessKey);
	    MembershipManager mm = mgr.getMembershipManager();
	   List <Membership> v= mm.getMemberships(projId);
	   for (Membership issue : v) {
		   User u=issue.getUser();
		 test=u.getFirstName();
		 if (test.equals(checkName))
		 {isMem=true;
		 return isMem; }
	   }
	       
	    
	    }catch(RedmineException nm){}
	    return isMem;
}
	    public int checkId(String uname2,int proId)
	    {
	    	
	        
	    String checkName=uname2,test;
	    
	    try{
	    RedmineManager mgr = RedmineManagerFactory.createWithApiKey(uri, apiAccessKey);
	    MembershipManager mm = mgr.getMembershipManager();
	   List <Membership> v= mm.getMemberships(proId);
	   for (Membership issue : v) {
		   User u=issue.getUser();
		 test=u.getFirstName();
		 if (test.equals(checkName))
		 {
		 return u.getId(); }
	   }
	      
	    
	    }catch(RedmineException nm){}
	    return 0;
}
	    
}
