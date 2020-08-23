
// Java implementation of  Server side 
// It contains two classes : Server and ClientHandler 
  
import java.io.*;  
import java.net.*; 

import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
  
// Server class 
public class Server  
{ 
    public static void main(String[] args) throws IOException  
    {    //The server performs the indexing of the documents
    	 String indexDir = args[0];
    	 String dataDir = args[1];
    	 Indexer indexer;
    	 
    	 try{
    		 indexer = new Indexer(indexDir);
    	     int numIndexed = indexer.createIndex(dataDir, new TextFileFilter());
    	     indexer.close();
    	     System.out.println(numIndexed+" File indexed. ");	
    	 }catch (IOException e) {
             e.printStackTrace();
         } 
        // The server is listening on port 5025 
        ServerSocket ss = new ServerSocket(5025); 
          
        // running infinite loop for getting 
        // client request 
        while (true)  
        { 
            Socket s = null; 
              
            try 
            { 
                // socket object for the server to receive incoming client requests 
                s = ss.accept(); 
                  
                System.out.println("A new client is connected : " + s); 
                  
                System.out.println("Assigning new thread for this client"); 
  
                // create a new thread object 
                Thread t = new ClientHandler(s,indexDir); 
  
                // Invoking the start() method 
                t.start(); 
                  
            } 
            catch (Exception e){ 
                s.close(); 
                e.printStackTrace(); 
            } 
        } 
    } 
} 
  
// ClientHandler class 
class ClientHandler extends Thread  
{ 
    final BufferedReader is; 
    final PrintStream os; 
    final Socket s; 
    final String indexDir;  
    // Constructor 
    public ClientHandler(Socket s, String indexDir) throws IOException
    { 
        this.s = s; 
     // obtaining input and output streams 
        is = new BufferedReader(new InputStreamReader(s.getInputStream())); 
        os = new PrintStream(s.getOutputStream()); 
        this.indexDir=indexDir;
    } 
  
    @Override
    public void run()  
    { 
        String received; 
        
        while (true)  
        { 
            try { 
  
                // Ask the user for a keyword to search 
                os.println("Type keyword(s) to search.." + " Type Exit to terminate connection."); 
                  
                // receive the answer from client 
                received = is.readLine(); 
                  
                if(received.equals("Exit")) 
                {   
                    System.out.println("Client " + this.s + " sends exit..."); 
                    System.out.println("Closing this connection."); 
                    this.s.close(); 
                    System.out.println("Connection closed"); 
                    break; 
                } 
                  
                // The ClientHandler performs the searching 
                //for his client's requested keyword
                try{
                	Searcher searcher = new Searcher(indexDir);
                    TopDocs hits = searcher.search(received);
                    
                    os.println(hits.totalHits + " documents found. ");
                    for(ScoreDoc scoreDoc : hits.scoreDocs) {
                       Document doc = searcher.getDocument(scoreDoc);
                       os.println("File: " + doc.get(LuceneConstants.FILE_PATH));
                    }
                    os.println("end");
                    searcher.close();
                }catch (IOException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                   e.printStackTrace();
                }
                  
                
            } catch (IOException e) { 
                e.printStackTrace(); 
            } 
        } 
          
        try
        { 
            // closing resources 
            this.is.close(); 
            this.os.close(); 
              
        }catch(IOException e){ 
            e.printStackTrace(); 
        } 
    } 
} 
