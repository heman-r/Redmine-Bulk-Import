    var xmlhttp;
    
    function init() {
       // put more code here in case you are concerned about browsers that do not provide XMLHttpRequest object directly
       xmlhttp = new XMLHttpRequest();
    }
function update(dtr)
    
    {    	var len=0,i,ind=0,sub;

    	var cel =  document.getElementById("result2");
    	cel.value=dtr;
    	
    	
    
    
    }

    
    function get() {
    	
        var far = document.getElementById("projectid");
        var url = "http://localhost:8080/Redmine/con/getissues/" + far.value;
        xmlhttp.open('GET',url,true);
        xmlhttp.send(null);
        xmlhttp.onreadystatechange = function() {
           

              
               
               if (xmlhttp.readyState == 4) {
                  if ( xmlhttp.status == 200) {
                       var det = eval( "(" +  xmlhttp.responseText + ")");
                       
                          str = det.value;
                          update(str);
                 }
                 else
                       alert("lo ->");
              }
        };
       xmlhttp.close(); 
    }
  
    function create() {
    	
    	var x = document.getElementById("uploadbutton").value; 
    	
    	x=x.replace("C:\\fakepath\\" , "" ); 
    	document.getElementById("demo").innerHTML = x;
        var id= document.getElementById("farenheit");
        var url = "http://localhost:8080/Redmine/con/redmine/" + x;
        xmlhttp.open('POST',url,true);
        xmlhttp.send(null);
        xmlhttp.onreadystatechange = function() {
           
        };
        xmlhttp.close(); 
    } 
    
        
   
