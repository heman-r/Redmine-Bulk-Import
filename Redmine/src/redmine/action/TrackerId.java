package redmine.action;

public class TrackerId {
	

int findTID(String Tid){
	int id=0;
	switch(Tid){
case "Bug":
	id=1;
	break;
	
case "Feature":
	id=2;
	break;
	
case "Support":
	id=3;
	break;
	
default:
	id=0;
	break;
	}
	return id;
}



int findPriority(String p)
{int id;
switch(p){
case "Immediate":
	id=5;
	break;
	
case "Urgent":
	id=4;
	break;
	
case "High":
	id=3;
	break;
case "Normal":
	id=2;
	break;
	
case "Low":
	id=1;
	break;
default:
	id=0;
	break;
	}
return id;
}}