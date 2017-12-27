package com.ldv.client.bigbro;

import java.util.Iterator;
import java.util.Vector;

import com.google.inject.Inject;

import com.ldv.client.bigbro.BBControl.CTRL_ACTIVATION;
import com.ldv.client.bigbro.BBTransferInfo.CTRL_ACTIVITY;
import com.ldv.client.bigbro_mvp.MultipleDialogPresenter;
import com.ldv.client.util.LdvSupervisorBase;

import com.ldv.shared.graph.BBMessage;
import com.ldv.shared.graph.LdvModelNodeArray;
import com.ldv.shared.archetype.LdvArchetypeItem;
import com.ldv.shared.filsguides.BBItemData;

public class BBFilsItem 
{
	protected LdvSupervisorBase _supervisor ;
	
	protected String            _sEtiquette ;  // Label of branch in tree
  protected BBItem            _FatherItem ;
  protected BBSmallBrother    _BigBoss ;
  protected BBItemData        _ItemData ;		  // Data from GUIDES

  protected BBTransferInfo    _Transfert ;  	  // Pointeur sur la structure de transfert
  protected LdvArchetypeItem  _ArchetypeItem ;

  protected String            _sName ;       // Name of Fil Guide
  protected String            _sLabel ;
  
  protected boolean           _bIsActive ;
  protected boolean           _bIsMultiple ;
  protected boolean           _bIsExpandable ;
  protected boolean           _bEditMode ;
  
  protected MultipleDialogPresenter _multiDialogControler ;
  
  protected Vector<BBItem>    _aSons     = new Vector<BBItem>() ;
  protected Vector<String>    _aExcluded = new Vector<String>() ;
	
  @Inject
  public BBFilsItem(final BBItem fatherItem, final BBSmallBrother Big, final LdvSupervisorBase supervisor)
  {
  	init() ;
  	
  	_FatherItem    = fatherItem ;
  	_BigBoss       = Big ; 
  	
  	_supervisor    = supervisor ;
  }
  
  public void executeMultipleDialogs()
  {
    if (null != _multiDialogControler)
      return ;

    BBControl bbControl = _Transfert.getControl() ; 
    if ((null != bbControl) && bbControl.doesSelfManageMultiple())
      return ;

    if (_aSons.isEmpty())
    	return ;
    
    Iterator<BBItem> itemItr = _aSons.iterator() ;

  	BBItem firstBBitem = itemItr.next() ;
    if (false == firstBBitem.isUnique())
    {
    	_multiDialogControler = _supervisor.getInjector().getMultipleDialogPresenter() ;
    	
    	// pNsMultiDialog = new NsMultiDialog(PremierBBitem->donneFenetre(), "MULTI", this, pNSDLLModule) ;
    	_bIsMultiple = true ;
    }
  }

  public BBItemData getItemData() {
  	return _ItemData ;
  }
  public void setItemData(BBItemData itemData) {
  	_ItemData = itemData ;
  }
  
  public BBTransferInfo getTransfert() {  
  	return _Transfert ;
  }
  
  public BBItem getFatherItem() {          
  	return _FatherItem ;
  }
  
  public LdvArchetypeItem getArchetypeItem() {
  	return _ArchetypeItem ;
  }
  public void setArchetypeItem(LdvArchetypeItem archItem) {
  	_ArchetypeItem = archItem ;
  }
  
  public String getLabel() {
  	return _sLabel ;
  }
  public void setLabel(String sLabel) {
  	_sLabel = sLabel ;
  }
  
  public Vector<String> getExcluded() { 
  	return _aExcluded ;
  }
  public void clearExcluded() { 
  	_aExcluded.clear() ;
  }
  public void addExcluded(String sExcludedLabel) { 
  	_aExcluded.addElement(sExcludedLabel) ;
  }
  
  public boolean isExpandable() {
  	return _bIsExpandable ;
  }
  public void setIsExpandable(boolean bIsExpandable) {
  	_bIsExpandable = bIsExpandable ;
  }
  
  public boolean isActive() {         
  	return _bIsActive ;
  }
  public void setActive(boolean bActive) {         
  	_bIsActive = bActive ;
  }
  
  public boolean isInEditMode() {
  	return _bEditMode ;
  }
  public void setInEditMode(boolean bEditMode) {         
  	_bEditMode = bEditMode ;
  }
  
  public boolean isFreeText() { 
  	return ("#####1".equals(_sLabel) || "#####2".equals(_sLabel)) ; 
  }
  
  public MultipleDialogPresenter getMultipleDialogPresenter() {
  	return _multiDialogControler ;
  }
  
  public boolean IsUnique() {
		return _ItemData.IsUnique() ; 
	}
  
  public String getDialogName() {
		return _ItemData.getDialogName() ; 
	}
  public boolean isDialogNameIdem() {
		return _ItemData.isDialogNameIdem() ;
	}
  public boolean isDialogNameAuto() {
		return _ItemData.isDialogNameAuto() ;
	}
  
  public String getLevelShift() {
		return _ItemData.getLevelShift() ;
	}
	public void setLevelShift(String sLevelShift) {
		_ItemData.setLevelShift(sLevelShift) ;
	}
  
  /**
	 * Get the BBMessage from the attached BBTransferInfo   
	 * 
	 * @return <code>null</code> if the BBTransferInfo is null, its BBMessage if not
	 * 
	 **/
  public BBMessage getTransfertMessage() 
  {
  	if (null == _Transfert)
  		return null ;
  	
  	return _Transfert.getTransfertMessage() ;
  }
  
  /**
	 * Get the BBVectFatheredPatPathoArray from the attached BBTransferInfo   
	 * 
	 * @return <code>null</code> if the BBTransferInfo is null, its BBVectFatheredPatPathoArray if not
	 * 
	 **/
  public BBVectFatheredPatPathoArray getPatPatho() 
  {
  	if (null == _Transfert)
  		return null ;
  	
  	return _Transfert.getTransPatpatho() ;
  }
  
  /**
	 * Check if the BBVectFatheredPatPathoArray from the attached BBTransferInfo is empty   
	 * 
	 * @return <code>true</code> if the array is empty of only contains elements with empty PatPatho, <code>false</code> if not
	 * 
	 **/
  public boolean hasEmptyPatPatho() 
  {
  	BBVectFatheredPatPathoArray aVFPpt = getPatPatho() ;
  	if (null == aVFPpt)
  		return true ;
  	
  	return aVFPpt.hasEmptyContent() ;
  }
  
  public Vector<BBItem> getSons() {
  	return _aSons ;
  }
  public boolean hasSons() {
  	return (false == _aSons.isEmpty()) ;
  }
  
  public boolean isZeroShift() {
  	return "+00+00".equals(_ItemData.getLevelShift()) ;
  }
  
  public void init()
  {
  	_sEtiquette    = "" ;  
    _FatherItem    = null ;
    _BigBoss       = null ;
    _ItemData      = new BBItemData() ;
    _Transfert     = new BBTransferInfo(this) ;

    _ArchetypeItem = null ;

    _sName         = "" ;
    _sLabel        = "" ;
    
    _bIsActive     = false ;
    _bIsMultiple   = false ;
    _bIsExpandable = false ;
    _bEditMode     = false ;
    
    _aSons.clear() ;
    _aExcluded.clear() ;
    
    _multiDialogControler = null ;
  }
  
  public void Activate()
  {
    _Transfert.Activate() ;
    if (null != _FatherItem)
    	_FatherItem.ActivateFather(this) ;
  }
  
  /**
	 * Set the transfer object as unactivated, then unactivate sons and fake fathers    
	 * 
	 **/
  public void Unactivate()
  {
    _Transfert.Unactivate() ;
    UnactivateSons() ;
    
    if (null != _FatherItem)
    	_FatherItem.UnactivateFakeFathers(this) ;
  }
  
  public boolean isActivated() {
    return (CTRL_ACTIVITY.activeCtrl == _Transfert.getActivationStatus()) ;
  }
  
  /**
	 * Unactivate all sons located in the same panel   
	 * 
	 **/
  public void UnactivateSons()
  {
  	if (_aSons.isEmpty())
  		return ;
  	
  	for (Iterator<BBItem> itemItr = _aSons.iterator() ; itemItr.hasNext() ; )
		{
  		BBItem item = itemItr.next() ;
  		if (_FatherItem.isInSamePanel(item))
  			item.UnactivateSons() ;
		}
  }
  
  public void createSons(int sonIndex)
  {
  	BBVectFatheredPatPathoArray vect = getPatPatho() ;
  	if (null == vect)
  		return ;
  	
  	if (vect.isEmpty())
  		vect.addFatheredPatho(new BBFatheredPatPathoArray(null, new LdvModelNodeArray())) ;
  	
  	// Two cases:
  	// 1) if sonIndex == -1 we must create a BBItem for each entry in vect
  	// 2) if sonIndex >=  0 we must create a BBItem for the sonIndex th entry in vect
  	//
  	int iIndex = 0 ;
  	for (Iterator<BBFatheredPatPathoArray> Itr = vect.iterator() ; Itr.hasNext() ; iIndex++)
		{
  		BBFatheredPatPathoArray patho = Itr.next() ;
  		
  		if ((-1 == sonIndex) || (sonIndex == iIndex))
  			_aSons.addElement(new BBItem(this, _BigBoss, patho.getPatPatho(), _supervisor)) ;
		}
  	
  	if (false == _aSons.isEmpty())
  		for (Iterator<BBItem> Itr = _aSons.iterator() ; Itr.hasNext() ; )
  			Itr.next().setData(_ItemData) ;
  }
  
  /**
	 * Unactivate this element (and sons and fake fathers) then ask the control, if any, to switch off   
	 * 
	 **/
  public void fullyUnactivate()
  {
  	UnactivateSons() ;
  	Unactivate() ;
		
  	if ((null != _Transfert) && (null != _Transfert.getControl()))
  		_Transfert.activateControl(CTRL_ACTIVATION.unchecked, getTransfertMessage()) ; 
  }
  
  /**
	 * This element, for example a treeview node, has been manually destroyed by the user   
	 * 
	 **/
  public void manualDestruction(BBMessage message)
  {
  	// Iterate on BBItem sons
  	//
  	if (false == _aSons.isEmpty())
  		for (Iterator<BBItem> Itr = _aSons.iterator() ; Itr.hasNext() ; )
  			Itr.next().manualDestruction(message) ;

  	eraseSons() ;

    _Transfert.activateControl(CTRL_ACTIVATION.delete, message) ;
  }

  /**
	 * Function called when a UI wants to close   
	 * 
	 * @param bUnlinkFromControl if true, detach the command layer from widgets
	 * @param bGetInformation    if true, then get information from user interface
	 * 
	 **/
  public boolean canWeClose(boolean bGetInformation, boolean bUnlinkFromControl)
  {
  	// If we are on a leaf, we get its information back 
  	//
  	if (false == _bIsExpandable)
  	{
  		if (0 == _Transfert.Transfer(BBTransferInfo.TRANSFER_DIRECTION.tdGetData))
        return false ;
  		
  		// Utilis� pour le transfert de document type consultation pour la mise � jour de la base des textes libres
      // if (0 == _Transfert.TransferFinal(BBTransferInfo.TRANSFER_DIRECTION.tdGetData))
      //  return false ;
  	}
  	
  	// Recursively execute on sons
  	//
  	if (false == _aSons.isEmpty())
  	{
			for (Iterator<BBItem> itemItr = _aSons.iterator() ; itemItr.hasNext() ; )
			{
				BBItem son = itemItr.next() ;
				boolean bCanWe = son.canWeClose(bGetInformation, bUnlinkFromControl) ;
				
				if (false == hasEmptyPatPatho())
					Activate() ;
				
				if (false == bCanWe)
				{
					if (bUnlinkFromControl)
						_Transfert.unlinkFromControl() ;
					return bCanWe ;
				}
			}
			
			if (hasEmptyPatPatho() && (false == _ItemData.IsActiveWhenEmpty()))
				Unactivate() ;
  	}
  	
  	if (bUnlinkFromControl)
			_Transfert.unlinkFromControl() ;
  	
  	return true ;
  }
  
  /**
	 * Clear the BBItem sons array   
	 * 
	 **/
  protected void eraseSons() {
  	_aSons.clear() ;
  }
}
