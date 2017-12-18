package com.ldv.server.handler4caldav;

import java.io.InputStream;
import java.net.SocketException;
import java.net.URISyntaxException;
import java.util.Properties;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Calendar;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.osaf.caldav4j.exceptions.CalDAV4JException;
import org.osaf.caldav4j.credential.CaldavCredential;
import org.osaf.caldav4j.methods.CalDAV4JMethodFactory;
import org.osaf.caldav4j.methods.HttpClient;
import org.osaf.caldav4j.methods.MkCalendarMethod;
import org.osaf.caldav4j.methods.PutMethod;

/**
 * Base class for CalDAV4j connection,
 * .TODO use SSL?
 * FIXME it shouldn't be possibile to change serverhost/port:
 *  	these should be changed at the same time re-creating the hostConfig
 */
public class BaseCaldavClient extends HttpClient
{
	protected HostConfiguration hostConfig = new HostConfiguration();
	    
	private String  _serverHost       = "" ;   // CaldavCredential.CALDAV_SERVER_HOST ;
	private int     _serverPort       = 0 ;    // CaldavCredential.CALDAV_SERVER_PORT;
	private String  _serverProtocol   = "" ;   // CaldavCredential.CALDAV_SERVER_PROTOCOL;
	private String  _serverWebDavRoot = "" ;   // CaldavCredential.CALDAV_SERVER_WEBDAV_ROOT;
	private String  _serverUserName   = "" ;   // CaldavCredential.CALDAV_SERVER_USERNAME;
	private String  _serverPassword   = "" ;   // CaldavCredential.CALDAV_SERVER_PASSWORD;
	private boolean _bAddUserToRoot   = true ; // is root built as host + "/" + user + "/"
	    
	public CalDAV4JMethodFactory methodFactory = new CalDAV4JMethodFactory();

	public BaseCaldavClient() 
	{
		super() ;
		
		initializeFromCredential() ;
	}
	
	/**
	 * Connect with credentials
	 * 
	 * @param credential The CaldavCredential adapted to the proper server
	 */
	public BaseCaldavClient(final CaldavCredential credential) 
	{
		super() ;

		this._serverHost       = credential._host ;
		this._serverPort       = credential._port ;
		this._serverProtocol   = credential._protocol ;
		this._serverWebDavRoot = credential._home ;
		this._serverUserName   = credential._user ;
		this._serverPassword   = credential._password ;
		this._bAddUserToRoot   = credential._bAddUserToRoot ;
	    	
		initializeFromCredential() ;
	}
	
	/**
	 * connect with credentials
	 * @param serverHost
	 * @param serverPort
	 * @param serverProtocol
	 * @param serverWebDavRoot
	 * @param serverUserName
	 * @param serverPassword
	 */
	public BaseCaldavClient(String serverHost, String serverPort, 
	    						String serverProtocol, String serverWebDavRoot,
	    						String serverUserName, String serverPassword) 
	{
		super() ;
		
		this._serverHost       = serverHost ;
		this._serverPort       = Integer.parseInt(serverPort) ;
		this._serverProtocol   = serverProtocol ;
		this._serverWebDavRoot = serverWebDavRoot ;
		this._serverUserName   = serverUserName ;
		this._serverPassword   = serverPassword ;
	    	
		initializeFromCredential() ;
	}
	
	protected void initializeFromCredential()
	{
		try {
			setCredentials(this._serverUserName, this._serverPassword) ;
			hostConfig.setHost(this._serverHost, this._serverPort, this._serverProtocol) ;
		} catch (Exception e) {
			e.printStackTrace() ;
			System.out.println("can't create BaseCaldavClient") ;
		}
	}
	
	/**
	 * TODO this method misses the property file, needs to be implemented
	 * connect without credentials using  properties set 
	 * @param props
	 */
	public BaseCaldavClient(Properties props) 
	{
		this(props.get("CALDAV_SERVER_HOST").toString(),
	    	 props.get("CALDAV_SERVER_PORT").toString(),
	    	 props.get("CALDAV_SERVER_PROTOCOL").toString(),
	    	 props.get("CALDAV_SERVER_WEBDAV_ROOT").toString()) ;
	}
	    
	/**
	 * connect without credentials
	 * @param host
	 * @param port
	 * @param protocol
	 * @param webDavRoot
	 */
	public BaseCaldavClient(String host, String port, String protocol, String webDavRoot) {
		this(host, port, protocol, webDavRoot, null, null) ;
	}
    
	public String getCalDavServerHost() {
		return _serverHost ;
	}   
	public void setCalDavServerHost(String serverHost) {
		this._serverHost = serverHost ;
	}
	    
	public int getCalDavServerPort() {
		return _serverPort ;
	}   
	public void setCalDavServerPort(int serverPort) {
		this._serverPort = serverPort ;
	}
	    
	public String getCalDavSeverProtocol() {
		return _serverProtocol ;
	}  
	public void setCalDavSeverProtocol(String serverProtocol) {
		this._serverProtocol = serverProtocol ;
	}
    
	public String getCalDavSeverWebDAVRoot() {
		return _serverWebDavRoot ;
	}    
	public void setCalDavSeverWebDAVRoot(String serverWebDavRoot) {
		this._serverWebDavRoot = serverWebDavRoot ;
	}
	    
	public String getCalDavSeverUsername() {
		return _serverUserName ;
	}    
	public void setCalDavSeverUsername(String serverUserName) {
		this._serverUserName = serverUserName ;
	}
	    
	public String getCalDavSeverPassword() {
		return _serverPassword ;
	}    
	public void setCalDavSeverPassword(String serverPassword) {
		this._serverPassword = serverPassword ;
	}
	    
	public boolean mustAddUserToRoot() {
		return _bAddUserToRoot ;
	}
	public void setMustAddUserToRoot(boolean bAddUserToRoot) {
		this._bAddUserToRoot = bAddUserToRoot ;
	}
	
	/**
	 * set credentials
	 * @return
	 */
	protected void setCredentials(String user, String pass) 
	{
		Credentials credentials = new UsernamePasswordCredentials(user, pass) ;
	        
		getParams().setAuthenticationPreemptive(true) ;
		getState().setCredentials(AuthScope.ANY, credentials) ;
	}
	
	protected Calendar getCalendarResource(String resourceName) 
	{
		Calendar cal ;

		InputStream stream = this.getClass().getClassLoader().getResourceAsStream(resourceName) ;
		CalendarBuilder cb = new CalendarBuilder() ;
	        
		try {
			cal = cb.build(stream) ;
		} catch (Exception e) {
			throw new RuntimeException(e) ;
		}
	        
		return cal ;
	}    
	    
	protected void put(String resourceFileName, String path) 
	{
		PutMethod put = methodFactory.createPutMethod() ;
		InputStream stream = this.getClass().getClassLoader().getResourceAsStream(resourceFileName) ;
	        
		// put.setRequestBody(stream);  deprecated
		put.setRequestEntity(new InputStreamRequestEntity(stream)) ;
	        
		put.setPath(path) ;
		try {
			executeMethod(hostConfig, put) ;
		} catch (Exception e) {
			throw new RuntimeException(e) ;
		}
	}
	    
	protected void del(String path) 
	{
		DeleteMethod delete = new DeleteMethod() ;
		delete.setPath(path) ;
		try {
			executeMethod(hostConfig, delete) ;
		} catch (Exception e) {
			throw new RuntimeException(e) ;
		}
	}
	    
/*
	    protected void mkdir(String path) 
	    {
	        MkCalendarMethod mk = new MkCalendarMethod();
	        mk.setPath(path);
	        try {
	        	executeMethod(hostConfig, mk);
	        } catch (Exception e){
	            throw new RuntimeException(e);
	        }
	    }
*/
	    
	// ************* test *****************
	public static void main(String[] args) throws CalDAV4JException, SocketException, URISyntaxException 
	{
		BaseCaldavClient cli = new BaseCaldavClient() ;	
	}
}
