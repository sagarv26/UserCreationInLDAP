
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.util.Hashtable;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.annotation.Generated;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.AttributeInUseException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;


public class CreateUserInLDAP {
	public static final String LDAPctxfactory =  "com.sun.jndi.ldap.LdapCtxFactory";
	public static final String UserName=" ";
	public static final String Password=" ";
	public static final String LDAPURL="ldap://localhost:20389";
	public static FileWriter fileWriter = null;
	public static int count=0;
	
	public static DirContext getConnection() throws NamingException{
	 Hashtable<String, String> env = new Hashtable<String, String>();
	    env.put(Context.INITIAL_CONTEXT_FACTORY, LDAPctxfactory);
	    env.put(Context.PROVIDER_URL, LDAPURL);
	    env.put(Context.SECURITY_AUTHENTICATION,"simple");
	    env.put(Context.SECURITY_PRINCIPAL,UserName); 
	    env.put(Context.SECURITY_CREDENTIALS,Password); 
	    System.out.println("Returning the Context");
	   return new InitialDirContext(env);
	}
	
	public static void main(String s[]) {
		DirContext dctx =null;
		BufferedReader br = null;
		String[] lineArray= new String[7];
		String line=null;
		String uid=null;
		
		String fileName="\\\\U:\\Shan03v\\Code\\b2b_output.csv";
		
		try {
			 dctx = getConnection();
			 String baseDN = "ou=people,ou=svharbor,o=supervalu.com";
			 br=new BufferedReader(new FileReader("\\\\c:\\users\\Code\\Java_b2b.csv"));
			 fileWriter = new FileWriter(fileName);
			 writeCsvFile( "firstName","lastName","employeenumber","uid","password");
			 
			 while((line=br.readLine())!=null){
			 lineArray=line.split(",");
			 
			 uid=RandomUID(lineArray[0],lineArray[1],lineArray[2],lineArray[3]);
			 
			 if(checkUserExistance(uid,baseDN,dctx)){
				
			 uid=RandomUID(lineArray[0],lineArray[1],lineArray[2],lineArray[3]);
			 
			 }
			
			 String pwd=RandomPassword();	
			 System.out.println("UID : "+uid);

			 CreateUsers(uid,lineArray[0],lineArray[1],pwd,lineArray[2],lineArray[4],dctx);
			 }	
			
	
		} catch (NamingException e2) {	
			e2.printStackTrace();
		}catch(java.lang.NumberFormatException Nfe){
			System.out.println("You should enter any digits\n");
			System.out.println("Please Try Again\n");
			//LoggerUtil.errorLog("User Entered Inavlid Choice \n Program Exiting !!!");
		System.exit(0);
		}
		catch (Exception e1) {
			e1.printStackTrace();
		}
		finally{
			System.out.println("Closing the Connection If it is opened\n");
			if(dctx != null){
				try {
					dctx.close();
					fileWriter.flush();
					fileWriter.close();
				} catch (NamingException e) {			
					System.out.println("Unable to Close the Connection\n");
				//	LoggerUtil.debugLog("Unable to Close the Connection\n");
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private static String RandomPassword() {
		SecureRandom random = new SecureRandom();

	    /** different dictionaries used */
	   String ALPHA_CAPS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	   String ALPHA = "abcdefghijklmnopqrstuvwxyz";
	   String NUMERIC = "0123456789";
	   String SPECIAL_CHARS = "@#";
	   
	   String dic=ALPHA_CAPS+ALPHA+NUMERIC;
	   
	   String result = "";
	    for (int i = 0; i < 5; i++) {
	        int index = random.nextInt(dic.length());
	        result += dic.charAt(index);
	    }
	    for (int i = 0; i < 1; i++) {
	    	result+=SPECIAL_CHARS.charAt(random.nextInt(SPECIAL_CHARS.length()));
	    }
	    for (int i = 0; i < 2; i++) {
	        int index = random.nextInt(dic.length());
	        result += dic.charAt(index);
	    }
		
		return result;
	}

	private static String RandomUID(String firstname,String lastname,String employeenumber,String company) {
		SecureRandom random = new SecureRandom();
		String digit = null;
//		String fn=firstname.substring(0, 2).toLowerCase();
//		String ln=lastname.substring(0, 2).toLowerCase();
//		String empno=employeenumber.substring(0, 2);
		String com=company.substring(0, 3).toLowerCase();
		if(count==100){
			count=0;
		}else{
			count++;
		}
		if(count>10){
		   digit=""+count;
		}else{
			digit="0"+count;
		}
			
		
					
		String result = com;
	    
	    for (int i = 0; i < 2; i++) {
	    	result+=firstname.toLowerCase().charAt(random.nextInt(firstname.length()));
	    }
	    
	    result+=digit;
	    
	    for (int i = 0; i < 2; i++) {
	    	result+=lastname.toLowerCase().charAt(random.nextInt(lastname.length()));
	    }
	    String uid=result;

	return uid;
	}

	private static void writeCsvFile(String firstName, String lastName, String uid, String pwd,
			String employeeNumber) {
		
		//Delimiter used in CSV file
		String COMMA_DELIMITER = ",";
		String NEW_LINE_SEPARATOR = "\n";
				
		try {		

			fileWriter.append(firstName);
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(lastName);
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(employeeNumber);
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(uid);
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(pwd);
			fileWriter.append(NEW_LINE_SEPARATOR);
	
			
			System.out.println("CSV file was updated successfully !!!");
			
		} catch (Exception e) {
			System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();
		} 
		
	
		
	}
	
	public static boolean CreateUsers(String uid,String firstName,String lastName,String password,String emplid,String b2atitle, DirContext dctx){
		       boolean UserCreated = false;
			   String baseDN = "ou=people,ou=svharbor,o=supervalu.com";
			   
		try{
		      if(!checkUserExistance(uid,baseDN,dctx)){
    	 
		       System.out.println(uid);
		       System.out.println(firstName);
		       System.out.println(lastName+ "\n");
    	
    		   Attribute givenName = new BasicAttribute("givenName",firstName);
               Attribute sn = new BasicAttribute("sn",lastName);
               Attribute cn = new BasicAttribute("cn",firstName+" "+lastName);
               Attribute employeenumber = new BasicAttribute("employeenumber",emplid);
               Attribute businesscategory = new BasicAttribute("businesscategory","person");
               Attribute employeetype = new BasicAttribute("svuidmusertype","B2B");            
               Attribute ID = new BasicAttribute("uid",uid);
               Attribute title = new BasicAttribute("title",b2atitle);
               Attribute jobaction = new BasicAttribute("absjobaction","Hired");
               Attribute carlicense = new BasicAttribute("carlicense","16777216");

               Attribute userPassword = new BasicAttribute("userPassword",password);
               Attribute Objectclass = new BasicAttribute("objectclass");
               Objectclass.add("top");
               Objectclass.add("person");
               Objectclass.add("organizationalPerson");       
               Objectclass.add("inetOrgPerson");
               Objectclass.add("oblixpersonpwdpolicy");
               
               Attribute Uniquemember = new BasicAttribute("uniquemember");
               Uniquemember.add(" ");
              
               Attributes battrs = new BasicAttributes();
               battrs.put(givenName);
               battrs.put(businesscategory);
               battrs.put(title);
               battrs.put(sn);
               battrs.put(jobaction);
               battrs.put(carlicense);             
               battrs.put(employeenumber);
               battrs.put(employeetype);
               battrs.put(cn);
               battrs.put(ID);
               battrs.put(Objectclass);
               battrs.put(userPassword);
               
               //Adding role
               battrs.put(Uniquemember);
               
               dctx.createSubcontext("uid="+uid+",ou=people,ou=svharbor,o=supervalu.com", battrs);
               writeCsvFile(firstName, lastName, uid, password, emplid);
               UserCreated = true;
     }
     else{
    	 System.out.println("User Already Present in LDAP\n");
    	// LoggerUtil.errorLog("User uid is already Present in LDAP\n");
         }
		
		}	catch(Exception e) {
        	// LoggerUtil.errorLog("Error While Adding the User\n");
			System.out.println("Exceptin is thrown");
			e.printStackTrace();
		}

	return UserCreated;
	
	}
				
	
	public static boolean checkUserExistance(String uid,String baseDN,DirContext dctx){
			boolean userExist = false;
			String filter = "(uid=" + uid +")";
			System.out.println("Validating existance of ID");
			System.out.println("filter is "+filter);
			System.out.println("base dn is " + baseDN);
			try {
				NamingEnumeration<?> answer = dctx.search(baseDN, filter,null);
				if(answer.hasMoreElements()){userExist = true;}
				else{
					System.out.println("User is not present\n");
				}
			} catch (NamingException e) {
				e.printStackTrace();
			}
			finally{
					return userExist;
			}
				}

}
