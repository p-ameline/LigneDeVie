package com.ldv.shared.database ;

import junit.framework.TestCase ;

public class LexiconTest extends TestCase
{

  public void testConstructor() 
  {
  	// Test default constructor
  	//
  	Lexicon lexicon = new Lexicon("Crohn [maladie|s|][de]", "PILEH8", "FS", "") ; 
  	assertNotNull(lexicon) ;
  	assertTrue(lexicon.getLabel().equals("Crohn [maladie|s|][de]")) ;
  	assertTrue(lexicon.getCode().equals("PILEH8")) ;
  	assertTrue(lexicon.getGrammar().equals("FS")) ;
  	assertTrue(lexicon.getFrequency().equals("")) ;
  } ;
  
  public void testLabelFunctions() 
  {
  	Lexicon lexicon = new Lexicon("Crohn [maladie|s|][de]", "PILEH8", "FS", "") ;
  	assertTrue(lexicon.getLabelWithoutFlexInfo().equals("Crohn [maladie][de]")) ;
  	assertTrue(lexicon.isNoon()) ;
  	assertTrue(lexicon.isFemaleGender()) ;
  	assertTrue(lexicon.isSingular()) ;
  	
  	assertFalse(lexicon.isMaleGender()) ;
  	assertFalse(lexicon.isNeutralGender()) ;
  	assertFalse(lexicon.isPlural()) ;
  	assertFalse(lexicon.isVerb()) ;
  	assertFalse(lexicon.isAdverb()) ;
  	assertFalse(lexicon.isAdjective()) ;
  	assertFalse(lexicon.isAdverb()) ;
  	
  	assertTrue(lexicon.getDisplayLabel(Lexicon.Declination.singular, "fr").equals("maladie de Crohn")) ;
  	assertTrue(lexicon.getDisplayLabel(Lexicon.Declination.nullDeclination, "fr").equals("maladie de Crohn")) ;
  	assertTrue(lexicon.getDisplayLabel(Lexicon.Declination.plural, "fr").equals("maladies de Crohn")) ;
  	
  	assertTrue(lexicon.getSelectionLabel().equals("Crohn (maladie)")) ;
  	
  	Lexicon lexicon2 = new Lexicon("Butler [lentic�ne|s| interne|s|][de]", "PBUTL2", "MS", "") ;
  	assertTrue(lexicon2.getLabelWithoutFlexInfo().equals("Butler [lentic�ne interne][de]")) ;
  	assertTrue(lexicon2.isNoon()) ;
  	assertTrue(lexicon2.isMaleGender()) ;
  	assertTrue(lexicon2.isSingular()) ;
  	
  	assertFalse(lexicon2.isFemaleGender()) ;
  	assertFalse(lexicon2.isNeutralGender()) ;
  	assertFalse(lexicon2.isPlural()) ;
  	assertFalse(lexicon2.isVerb()) ;
  	assertFalse(lexicon2.isAdverb()) ;
  	assertFalse(lexicon2.isAdjective()) ;
  	assertFalse(lexicon2.isAdverb()) ;
  	
  	assertTrue(lexicon2.getDisplayLabel(Lexicon.Declination.singular, "fr").equals("lentic�ne interne de Butler")) ;
  	assertTrue(lexicon2.getDisplayLabel(Lexicon.Declination.nullDeclination, "fr").equals("lentic�ne interne de Butler")) ;
  	assertTrue(lexicon2.getDisplayLabel(Lexicon.Declination.plural, "fr").equals("lentic�nes internes de Butler")) ;
  	
  	assertTrue(lexicon2.getSelectionLabel().equals("Butler (lentic�ne interne)")) ;
  	
  	Lexicon lexicon3 = new Lexicon("caillot  |s| adh�rent|s| {lors d'une h�morragie}", "SCAIL1", "MS", "") ;
  	assertTrue(lexicon3.getLabelWithoutFlexInfo().equals("caillot adh�rent {lors d'une h�morragie}")) ;  	
  	assertTrue(lexicon3.getDisplayLabel(Lexicon.Declination.singular, "fr").equals("caillot adh�rent")) ;
  	assertTrue(lexicon3.getDisplayLabel(Lexicon.Declination.nullDeclination, "fr").equals("caillot adh�rent")) ;
  	assertTrue(lexicon3.getDisplayLabel(Lexicon.Declination.plural, "fr").equals("caillots adh�rents")) ;
  	
  	assertTrue(lexicon3.getSelectionLabel().equals("caillot adh�rent {lors d'une h�morragie}")) ;
  }
  
  public void testUtilityFunctions() 
  {
  	Lexicon lexicon = new Lexicon("Crohn [maladie|s|][de]", "PILEH8", "FS", "") ;
  	assertTrue(lexicon.applyOrthography("maladie", "s", Lexicon.Declination.singular, "fr").equals("maladie")) ;
  	assertTrue(lexicon.applyOrthography("maladie", "s", Lexicon.Declination.plural, "fr").equals("maladies")) ;
  	assertTrue(lexicon.applyOrthography("cheval", "l/ux", Lexicon.Declination.singular, "fr").equals("cheval")) ;
  	assertTrue(lexicon.applyOrthography("cheval", "l/ux", Lexicon.Declination.plural, "fr").equals("chevaux")) ;
  	assertTrue(lexicon.applyOrthography("oeil", "oeil/yeux", Lexicon.Declination.singular, "fr").equals("oeil")) ;
  	assertTrue(lexicon.applyOrthography("oeil", "oeil/yeux", Lexicon.Declination.plural, "fr").equals("yeux")) ;
  }
  
  public void testStaticFunctions() 
  {
  	assertTrue(Lexicon.removeTrailingComments("").equals("")) ;
  	assertTrue(Lexicon.removeTrailingComments("test").equals("test")) ;
  	assertTrue(Lexicon.removeTrailingComments("test {with a comment}").equals("test")) ;
  	assertTrue(Lexicon.removeTrailingComments("test  {with a comment}").equals("test ")) ;
  	assertTrue(Lexicon.removeTrailingComments("test {with unclosed comment}").equals("test")) ;
  	
  	assertTrue(Lexicon.removeUselessBlanks("").equals("")) ;
  	assertTrue(Lexicon.removeUselessBlanks("nothing to do").equals("nothing to do")) ;
  	assertTrue(Lexicon.removeUselessBlanks("some work  to   get    done").equals("some work to get done")) ;
  }
}
