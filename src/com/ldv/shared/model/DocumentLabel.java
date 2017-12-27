package com.ldv.shared.model;

/**
 *  Set of data that contains a document label information
 * 
 **/
public class DocumentLabel
{
	private String  _sDocumentId ;
	
	private LdvTime _timeCreationDate = new LdvTime(0) ; // For the label - means when it has been registered
	
	private String  _sCreatorId ;    // The user when document is created/imported
	private String  _sAuthorId ;     // The genuine author
	private String  _sRecipientId ;  // The target
	
	private String  _sDocumentTitle ;
	private String  _sSemanticDocumentType ;  
	private String  _sFullDocumentType ;
	
	private String  _sDocumentURI ;
	private String  _sDocumentTemplate ;
	private String  _sDocumentHeader ;
	private String  _sSemanticDocumentContent ;
	private String  _sFullDocumentContent ;
	private LdvTime _timeContentDate = new LdvTime(0) ;

	public DocumentLabel() 
	{
		init() ;
	}
	
	public DocumentLabel(final String sDocumentId, final String sCreatorId, final String sDocumentType, final String sDocumentTitle, final String sDocumentContent, final LdvTime timeCreationDate) 
	{
		init() ;
		
		_sDocumentId          = sDocumentId ;
		_sCreatorId           = sCreatorId ;
		_sFullDocumentType    = sDocumentType ;
		_sDocumentTitle       = sDocumentTitle ;
		_sFullDocumentContent = sDocumentContent ;
		_timeCreationDate     = timeCreationDate ;
	}

	public void init()
	{
		_sDocumentId = "" ;
		
		_timeCreationDate.init() ; 
		
		_sCreatorId               = "" ;
		_sAuthorId                = "" ; 
		_sRecipientId             = "" ;
		
		_sDocumentTitle           = "" ;
		_sSemanticDocumentType    = "" ;  
		_sFullDocumentType        = "" ;
		
		_sDocumentURI             = "" ;
		_sDocumentTemplate        = "" ;
		_sDocumentHeader          = "" ;
		_sSemanticDocumentContent = "" ;
		_sFullDocumentContent     = "" ;
		
		_timeContentDate.init() ;
	}

	public String getDocumentId()
  {
  	return _sDocumentId ;
  }
	public void setDocumentId(String sDocumentId)
  {
  	_sDocumentId = sDocumentId ;
  }

	public LdvTime getCreationDate()
  {
  	return _timeCreationDate ;
  }
	public void setCreationDate(LdvTime timeCreationDate)
  {
  	_timeCreationDate = timeCreationDate ;
  }

	public String getCreatorId()
  {
  	return _sCreatorId ;
  }
	public void setCreatorId(String sCreatorId)
  {
  	_sCreatorId = sCreatorId ;
  }

	public String getAuthorId()
  {
  	return _sAuthorId ;
  }
	public void setAuthorId(String sAuthorId)
  {
  	_sAuthorId = sAuthorId ;
  }

	public String getRecipientId()
  {
  	return _sRecipientId ;
  }
	public void setRecipientId(String sRecipientId)
  {
  	_sRecipientId = sRecipientId ;
  }
	
	public String getDocumentTitle()
  {
  	return _sDocumentTitle ;
  }
	public void setDocumentTitle(String sDocumentTitle)
  {
		_sDocumentTitle = sDocumentTitle ;
  }

	public String getSemanticDocumentType()
  {
  	return _sSemanticDocumentType ;
  }
	public void setSemanticDocumentType(String sSemanticDocumentType)
  {
  	_sSemanticDocumentType = sSemanticDocumentType ;
  }

	public String getFullDocumentType()
  {
  	return _sFullDocumentType ;
  }
	public void setFullDocumentType(String sFullDocumentType)
  {
  	_sFullDocumentType = sFullDocumentType ;
  }

	public String getDocumentURI()
  {
  	return _sDocumentURI ;
  }
	public void setDocumentURI(String sDocumentURI)
  {
  	_sDocumentURI = sDocumentURI ;
  }

	public String getDocumentTemplate()
  {
  	return _sDocumentTemplate ;
  }
	public void setDocumentTemplate(String sDocumentTemplate)
  {
  	_sDocumentTemplate = sDocumentTemplate ;
  }

	public String getDocumentHeader()
  {
  	return _sDocumentHeader ;
  }
	public void setDocumentHeader(String sDocumentHeader)
  {
  	_sDocumentHeader = sDocumentHeader ;
  }

	public String getSemanticDocumentContent()
  {
  	return _sSemanticDocumentContent ;
  }
	public void setSemanticDocumentContent(String sSemanticDocumentContent)
  {
  	_sSemanticDocumentContent = sSemanticDocumentContent ;
  }

	public String getFullDocumentContent()
  {
  	return _sFullDocumentContent ;
  }
	public void setFullDocumentContent(String sFullDocumentContent)
  {
  	_sFullDocumentContent = sFullDocumentContent ;
  }

	public LdvTime getContentDate()
  {
  	return _timeContentDate ;
  }
	public void setContentDate(LdvTime timeContentDate)
  {
  	_timeContentDate = timeContentDate ;
  }
}
