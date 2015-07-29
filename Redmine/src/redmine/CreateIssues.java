package redmine;

import javax.ws.rs.POST;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import redmine.action.ExcelRead;
import javax.ws.rs.core.MediaType;

 
@Path("/redmine/{fileName}") 
	  public class CreateIssues {
	@POST
		
		@Consumes(MediaType.APPLICATION_JSON)
		public void crunchifyREST(@PathParam("fileName") String fileName) {
			System.out.println("Entering");
			
		String fname="/home/user/rt/"+fileName;
			
		       System.out.println(fname);
           	try{
       		ExcelRead read = new ExcelRead();
       		read.readXls(fname);}catch(Exception y){}
         
		}
}
