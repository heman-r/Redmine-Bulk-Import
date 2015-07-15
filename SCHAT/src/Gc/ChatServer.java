package Gc;

import java.sql.*;

import java.io.IOException;
import java.util.*;
import javax.websocket.*;
import javax.websocket.server.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@ServerEndpoint(value = "/chat-server",
        subprotocols={"chat"},
        decoders = {ChatDecoder.class},
        encoders = {ChatEncoder.class},
        configurator=ChatServerConfigurator.class)
public class ChatServer {
    private static String USERNAME_KEY = "username";
    private static String USERNAMES_KEY = "usernames";
    private Session session;
    private ServerEndpointConfig endpointConfig;
    private Transcript transcript;
    private static String c="all";
    private static int count=0;

    
   
    @OnOpen
    public void startChatChannel(EndpointConfig config, Session session) {
        this.endpointConfig = (ServerEndpointConfig) config;
        ChatServerConfigurator csc = (ChatServerConfigurator) endpointConfig.getConfigurator();
        this.transcript = csc.getTranscript();
        this.session = session;
        try{
            Class.forName("com.mysql.jdbc.Driver");}
           catch (ClassNotFoundException t)
           {}
        
        
    }

    @OnMessage
    public void handleChatMessage(ChatMessage message) {
        switch (message.getType()){
            case NewUserMessage.USERNAME_MESSAGE:
               this.processNewUser((NewUserMessage) message);
               break;
            case ChatMessage.CHAT_DATA_MESSAGE:
                this.processChatUpdate((ChatUpdateMessage) message);
                break;
            case ChatMessage.SIGNOFF_REQUEST:
                this.processSignoffRequest((UserSignoffMessage) message);
        }
    }
    
    @OnError
    public void myError(Throwable t) {
        System.out.println("Error: " + t.getMessage());
    }
    
    @OnClose
    public void endChatChannel() {
        if (this.getCurrentUsername() != null) {
            
            this.removeUser();
        }
    }
    
    void processNewUser(NewUserMessage message) {
        String newUsername = this.validateUsername(message.getUsername());
        NewUserMessage uMessage = new NewUserMessage(newUsername);
        try {
            session.getBasicRemote().sendObject(uMessage);
        } catch (IOException | EncodeException ioe) {
            System.out.println("Error signing " + message.getUsername() + " into chat : " + ioe.getMessage());
        } 
        this.registerUser(newUsername);
        
 try {
       ResultSet rs = null,ds=null;
       String z,x;
            String url1 = "jdbc:mysql://localhost:3306/chat"; 
            Connection conn1 = DriverManager.getConnection(url1,"root","root"); 
            Statement st1 = conn1.createStatement(); 
            Statement st2 = conn1.createStatement(); 
            String myQuery = "SELECT Message,Recepient FROM Log WHERE User = '"+newUsername+"'";
            System.out.println(myQuery);
          rs = st1.executeQuery ("SELECT Message,Recepient FROM Log WHERE User = '"+newUsername+"';");
             while(rs.next()){
            	 z=rs.getString("Message");
            	 x=rs.getString("Recepient");
            	 ChatUpdateMessage cdm1 = new ChatUpdateMessage(newUsername,z,x);
             	try {
                     session.getBasicRemote().sendObject(cdm1);
                 } catch (IOException | EncodeException ex) {
                     System.out.println("Error updating a client : " + ex.getMessage());
                 }  
            	 
            	       }
           
             ds= st2.executeQuery ("SELECT Message,User FROM Log WHERE Recepient = '"+newUsername+"';");
             while(ds.next()){
            	 z=ds.getString("Message");
            	 x=ds.getString("User");
            	 ChatUpdateMessage cdm2 = new ChatUpdateMessage(x,z,newUsername);
             	try {
                     session.getBasicRemote().sendObject(cdm2);
                 } catch (IOException | EncodeException ex) {
                     System.out.println("Error updating a client : " + ex.getMessage());
                 }  
            	 
            	       }
            
        } catch (Exception e) { 
            System.err.println("Got an exception! "); 
            System.err.println(e.getMessage()); 
        } 
  
        
       
       this.broadcastUserListUpdate();
        
    }

    void processChatUpdate(ChatUpdateMessage message) {
    	String g,m,k;
    	m=message.getUsername();
    	g=message.getMessage();
    	k=message.getRec();
    	
        
        try {
        	
            String url = "jdbc:mysql://localhost:3306/chat"; 
            Connection conn = DriverManager.getConnection(url,"root","root"); 
            Statement st = conn.createStatement(); 
           st.executeUpdate("INSERT INTO Log(User,Message,Recepient) " + 
              "VALUE ('"+m+"','"+g+"','"+k+"')"); 
           
           
        } catch (Exception e) { 
            System.err.println("Got an exception! "); 
            System.err.println(e.getMessage()); 
        } 

        
    	
    	
    	
        this.addMessage(message.getMessage(),message.getRec());
            }

    void processSignoffRequest(UserSignoffMessage drm) {
       
        this.removeUser();   
    }
    
    private String getCurrentUsername() {
        return (String) session.getUserProperties().get(USERNAME_KEY);
    }
    
    private void registerUser(String username) {
    	System.out.println("Inside registration");
        session.getUserProperties().put(USERNAME_KEY, username);
        this.updateUserList();
    }
    
    private void updateUserList() {
        List<String> usernames = new ArrayList<>();
        for (Session s : session.getOpenSessions()) {
            String uname = (String) s.getUserProperties().get(USERNAME_KEY);
            usernames.add(uname);
        }
        this.endpointConfig.getUserProperties().put(USERNAMES_KEY, usernames);
    }
    
    private List<String> getUserList() {
        List<String> userList = (List<String>) this.endpointConfig.getUserProperties().get(USERNAMES_KEY);
        return (userList == null) ? new ArrayList<String>() : userList;
    }

    
    private String validateUsername(String newUsername) {
        if (this.getUserList().contains(newUsername)) {
            return this.validateUsername(newUsername + "1");
        }
        return newUsername;
    }

    private void broadcastUserListUpdate() {
        UserListUpdateMessage ulum = new UserListUpdateMessage(this.getUserList());
        for (Session nextSession : session.getOpenSessions()) {
            try {
                nextSession.getBasicRemote().sendObject(ulum);
            } catch (IOException | EncodeException ex) {
                System.out.println("Error updating a client : " + ex.getMessage());
            }
        }
    }

    private void removeUser() {
        try {
            this.updateUserList();
            this.broadcastUserListUpdate();
            this.session.getUserProperties().remove(USERNAME_KEY);
            this.session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "User logged off"));
        } catch (IOException e) {
            System.out.println("Error removing user");
        }
    }

   
   
        
    private void broadcastTranscriptUpdate() {
    	String v=this.transcript.getRec();
        String g;
    	if(c.equals(v))
    	{
        for (Session nextSession : session.getOpenSessions()) {
        	
            ChatUpdateMessage cdm = new ChatUpdateMessage(this.transcript.getLastUsername(), this.transcript.getLastMessage(),this.transcript.getRec());
            
            try {
                nextSession.getBasicRemote().sendObject(cdm);
            } catch (IOException | EncodeException ex) {
                System.out.println("Error updating a client : " + ex.getMessage());
            }   
        }}
    	else
    		{for (Session nextSession : session.getOpenSessions()) {
            	String check=this.transcript.getRec();
                ChatUpdateMessage cdm = new ChatUpdateMessage(this.transcript.getLastUsername(), this.transcript.getLastMessage(),this.transcript.getRec());
                g=(String) nextSession.getUserProperties().get(USERNAME_KEY);
                if(v.equals(g))
                		{try {
                            nextSession.getBasicRemote().sendObject(cdm);
                        } catch (IOException | EncodeException ex) {
                            System.out.println("Error updating a client : " + ex.getMessage());
                        } 
                	}
                		}}
    	}

    private void addMessage(String message,String rec) {
        this.transcript.addEntry(this.getCurrentUsername(), message,rec);
        this.broadcastTranscriptUpdate();
    }
   

}