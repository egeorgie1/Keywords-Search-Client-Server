
// Java implementation for a client 
  
import java.io.*; 
import java.net.*; 
  
// Client class 
public class Client  
{ 
    public static void main(String[] args) throws IOException  
    { 
        try
        { 
            // getting localhost ip 
            InetAddress ip = InetAddress.getByName("localhost"); 
      
            // establish the connection with server port 5025 
            Socket s = new Socket(ip, 5025); 
			
			BufferedReader in= new BufferedReader(
					new InputStreamReader(s.getInputStream()));
			
			PrintStream out= new PrintStream(s.getOutputStream());
			
			BufferedReader cons= new BufferedReader(
					new InputStreamReader(System.in));
      
            // the following loop performs the exchange of 
            // information between client and client handler 
            while (true)  
            { 
                System.out.println(in.readLine()); 
                String tosend = cons.readLine(); 
                out.println(tosend); 
                  
                // If client sends exit,close this connection  
                // and then break from the while loop 
                if(tosend.equals("Exit")) 
                { 
                    System.out.println("Closing this connection : " + s); 
                    s.close(); 
                    System.out.println("Connection closed"); 
                    break; 
                } 
                  
                // receiving and printing the results from the server
                String next= "";
        		do {
        			try {
        				next = in.readLine();
        			} catch (IOException e) {
        				e.printStackTrace();
        			}
        			System.out.println(next);
        		} while (!next.equals("end"));
                
            } 
              
            // closing resources 
            cons.close(); 
            in.close(); 
            out.close(); 
        }catch(Exception e){ 
            e.printStackTrace(); 
        } 
    } 
} 
