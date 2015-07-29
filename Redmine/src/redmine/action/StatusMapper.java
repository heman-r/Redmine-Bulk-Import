package redmine.action;



public class StatusMapper {
public int mapper(String key)
{
	String map=key;
	int id=0;
switch(map){
	case "New":
		id=1;
		break;
		
	case "In Progress":
		id=2;
		break;
		
	case "Resolved":
		id=3;
		break;
	case "Feedback":
		id= 4;
		break;
	case "Closed":
		id= 5;
		break;
	case "Rejected":
		id= 6;
		break;
}
return id;
}
}

