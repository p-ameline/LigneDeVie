package com.ldv.server.model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.paddings.BlockCipherPadding;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.paddings.ZeroBytePadding;
import org.bouncycastle.crypto.params.KeyParameter;

import com.ldv.server.Logger;

/**
 * Manage files and directories 
 * 
 **/
public class LdvFilesManager 
{
	private String _sLdvId ;
	private String _sBaseDir ;
	private String _sDirSeparator ;
	
	public LdvFilesManager(final String sLdvId, final String sBaseDir, final String sDirSeparator)
	{
		_sLdvId        = sLdvId ;
		_sBaseDir      = sBaseDir ;
		_sDirSeparator = sDirSeparator ;
	}
	
	/**
	 * Convert a Document into a String 
	 * 
	 * @param doc : document to be serialized
	 * @return An XML representation as a String
	 * 
	 **/
	public boolean writeDocumentToDisk(String sContent, String sFileName, final String sBaseDir, final String sDirSeparator)
	{
		if ((null == sFileName) || sFileName.equals(""))
			return false ;
		
		// Get complete file name
		//
		String sCompleteFileName = getWorkingDirectoryName() + sFileName ;
		
		// Write file
		//		
		return writeDocumentToDisk(sContent, sCompleteFileName) ;
	}
	
	/**
	 * Convert a Document into a String 
	 * 
	 * @param doc : document to be serialized
	 * @return An XML representation as a String
	 * 
	 **/
	public boolean writeDocumentToDisk(final String sContent, final String sCompleteFileName)
	{
		if ((null == sCompleteFileName) || "".equals(sCompleteFileName))
			return false ;
		
		// Open output file
		//
		FileOutputStream out = null ;
		try
    {
			out = new FileOutputStream(sCompleteFileName, false) ;
    } 
		catch (FileNotFoundException eOpen)
    {
			Logger.trace("LdvFilesManager.writeDocumentToDisk: cannot create file " + sCompleteFileName + " ; stackTrace:" + eOpen.getStackTrace(), -1, Logger.TraceLevel.ERROR) ;
	    return false ;
    }
		
		// Write string to disk
		//
		boolean bSuccess = true ;
		
		byte data[] = sContent.getBytes() ;
		try
    {
	    out.write(data, 0, data.length) ;
    } 
		catch (IOException eWrite)
    {
			Logger.trace("LdvFilesManager.writeDocumentToDisk: error writing file " + sCompleteFileName + " ; stackTrace:" + eWrite.getStackTrace(), -1, Logger.TraceLevel.ERROR) ;
			bSuccess = false ;
    }
		finally
		{		
			try
      {
	      out.flush() ;
      } 
			catch (IOException eFlush)
      {
				Logger.trace("LdvFilesManager.writeDocumentToDisk: error flushing file " + sCompleteFileName + " ; stackTrace:" + eFlush.getStackTrace(), -1, Logger.TraceLevel.ERROR) ;
	      bSuccess = false ;
      }
			try
      {
	      out.close() ;
      } 
			catch (IOException eClose)
      {
				Logger.trace("LdvFilesManager.writeDocumentToDisk: error closing file " + sCompleteFileName + " ; stackTrace:" + eClose.getStackTrace(), -1, Logger.TraceLevel.ERROR) ;
	      bSuccess = false ;
      }
		}		
		return bSuccess ;
	}
		
	/**
	 * Tells if working environment is open    
	 * 
	 * @return true if files are deployed in working directory
	 * 
	 **/
	public boolean isWorkingEnvironmentOpen()
	{
		String sPersonnalDirectory = getMainDirectoryName() ;
		return createDirectory(sPersonnalDirectory) ;
	}
	
	/**
	 * Create main directory for a given LdV    
	 * 
	 * @return true if everything went well
	 * 
	 **/
	public boolean createMainDirectory()
	{
		Logger.trace("LdvFilesManager.createMainDirectory: creating Main Directory for ldvId " + _sLdvId, -1, Logger.TraceLevel.STEP) ;
		
		String sPersonnalDirectory = getMainDirectoryName() ;
		if (false == createDirectory(sPersonnalDirectory))
			return false ;
		
		return createWorkingDirectory() ;
	}
	
	/**
	 * Create main directory for a given LdV    
	 * 
	 * @return true if everything went well
	 * 
	 **/
	public boolean createWorkingDirectory()
	{
		Logger.trace("LdvFilesManager.createWorkingDirectory: creating Working Directory for ldvId " + _sLdvId, -1, Logger.TraceLevel.STEP) ;
		
		String sWorkingDirectory = getWorkingDirectoryName() ;		
		return createDirectory(sWorkingDirectory) ;
	}
	
	/**
	 * Create main directory for a given LdV    
	 * 
	 * @return true if everything went well
	 * 
	 **/
	private boolean createDirectory(String sDirectory)
	{
		try
		{
			boolean success = (new File(sDirectory)).mkdir() ;
			if (success)
				Logger.trace("LdvFilesManager.createDirectory: directory " + sDirectory + " created", -1, Logger.TraceLevel.DETAIL) ;
			else
			{
				Logger.trace("LdvFilesManager.createDirectory: cannot create directory " + sDirectory, -1, Logger.TraceLevel.ERROR) ;
				return false ;
			}
		}
		catch (SecurityException e)
		{
			Logger.trace("LdvFilesManager.createDirectory: cannot create directory " + sDirectory + " ; stackTrace:" + e.getStackTrace(), -1, Logger.TraceLevel.ERROR) ;
			return false ;
		}
		return true ;
	}
	
	/**
	 * Uncipher zip file, then unzip all files into working directory 
	 * 
	 * @return true if all went well
	 * 
	 **/
	public boolean openWorkingEnvironment(String sSecretKey)
	{
		// First step, go from the .ldv cyphered file to a regular .zip file
		//
		if (false == decypherZipFileBC(sSecretKey))
			return false ;
		
		// Unzip this file into a set of XML files
		//
		if (false == unzipWorkingFiles())
			return false ;
		
		return true ;
	}
	
	/**
	 * Zip all files, then cipher zip file 
	 * 
	 * @return true if all went well
	 * 
	 **/
	public boolean closeWorkingEnvironment(String sSecretKey)
	{
		// First step, zip all files from working directory
		//
		if (false == zipWorkingFiles())
			return false ;
		
		// Second step, cipher zip file
		//
		if (false == cypherZipFileBC(sSecretKey))
			return false ;
		
		return true ;
	}
	
	/**
	 * Assemble all files into a single zip 
	 * 
	 * @return true if all went well
	 * 
	 **/
	public boolean zipWorkingFiles()
	{
		final int BUFFERSIZE = 2048 ;
		
		String sAllFiles = getWorkingDirectoryName() + "." ;
		String sZipFile  = getZipFileName() ;
		
		try 
		{
			BufferedInputStream origin = null ;
      
      // get the list of files from user directory
      //
      File f = new File(sAllFiles) ;
      String files[] = f.list() ;

      FileOutputStream dest = new FileOutputStream(sZipFile) ;
      ZipOutputStream  out  = new ZipOutputStream(new BufferedOutputStream(dest)) ;
      //out.setMethod(ZipOutputStream.DEFLATED);
      byte data[] = new byte[BUFFERSIZE] ;
      
      for (int i = 0 ; i < files.length ; i++) 
      {
      	// System.out.println("Adding: "+files[i]);
      	
      	String sFileName = getWorkingDirectoryName() + files[i] ;
      	FileInputStream fi = new FileInputStream(sFileName) ;
      	
      	// Add file to zip
      	//
      	origin = new BufferedInputStream(fi, BUFFERSIZE) ;
      	ZipEntry entry = new ZipEntry(files[i]) ;
        out.putNextEntry(entry) ;
        int count ;
        while((count = origin.read(data, 0, BUFFERSIZE)) != -1) 
        	out.write(data, 0, count) ;
        
        origin.close() ;
        
        // Delete file
        //
        File fDel = new File(sFileName) ;
        fDel.delete() ;
      }
      out.close() ;
		} 
		catch(Exception e) 
		{
			Logger.trace("LdvFilesManager.zipWorkingFiles: exception for ldvId = " + _sLdvId + " ; stackTrace:" + e.getStackTrace(), -1, Logger.TraceLevel.ERROR) ;
			return false ;
		}
		
   	return true ;
	}
	
	/**
	 * Extract all files from zip 
	 * 
	 * @return true of all went well
	 * 
	 **/
	public boolean unzipWorkingFiles()
	{
		final int BUFFERSIZE = 2048 ;
		
		String sFilesDir = getWorkingDirectoryName() ;
		String sZipFile  = getZipFileName() ;
		
		try 
		{
			BufferedOutputStream dest = null ;
			FileInputStream fis = new FileInputStream(sZipFile) ;
			
			ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
			ZipEntry entry ;
			while ((entry = zis.getNextEntry()) != null) 
			{
				// System.out.println("Extracting: " + entry) ;
				int count ;
				byte data[] = new byte[BUFFERSIZE] ;
	            
				// write the files to the disk
				FileOutputStream fos = new FileOutputStream(sFilesDir + entry.getName()) ;
				dest = new BufferedOutputStream(fos, BUFFERSIZE) ;
				while ((count = zis.read(data, 0, BUFFERSIZE)) != -1) 
					dest.write(data, 0, count) ;
	            
				dest.flush() ;
				dest.close() ;
			}
			zis.close() ;
		} 
		catch(Exception e) {
			Logger.trace("LdvFilesManager.unzipFiles: exception for ldvId = " + _sLdvId + " ; stackTrace:" + e.getStackTrace(), -1, Logger.TraceLevel.ERROR) ;
			return false ;
		}
		
   	return true ;
	}
	
	/**
	 * Encrypt zip file
	 * 
	 * @param sDirectory Where to write files
	 * @param sSecretKey Secret key for symmetrical encryption 
	 * @return true if all went well
	 * 
	 **/
	public boolean cypherZipFile(String sSecretKey)
	{
		String sZipFile = getZipFileName() ;
		
		try {
			
			// File to cypher
			//
			File f = new File(sZipFile) ;
			byte[] buffer = new byte[(int)f.length()] ;
			DataInputStream in = new DataInputStream(new FileInputStream(f)) ;
			in.readFully(buffer) ;
			in.close() ;

			// Setting Initial Value
			//
			byte[] iv = { (byte) 0xc9, (byte) 0x36, (byte) 0x78, (byte) 0x99,
						  (byte) 0x52, (byte) 0x3e, (byte) 0xea, (byte) 0xf2 };

			IvParameterSpec salt = new IvParameterSpec(iv) ;
			
			// Secret key
			//
			byte[] raw = sSecretKey.getBytes();
			SecretKey skeySpec = new SecretKeySpec(raw, "Blowfish");

			// File encryption
			//
			Cipher c = Cipher.getInstance("Blowfish/CBC/PKCS5Padding", "BC");    
		    c.init(Cipher.ENCRYPT_MODE, skeySpec, salt);
			byte[] buf_crypt = c.doFinal(buffer);

			FileOutputStream envfos = new FileOutputStream("fichier_chiffre");
			envfos.write(buf_crypt);
			envfos.close();

			// Déchiffrement du fichier
			c = Cipher.getInstance("Blowfish/CBC/PKCS5Padding", "BC");    
			c.init(Cipher.DECRYPT_MODE, skeySpec, salt);
			byte[] buf_decrypt = c.doFinal(buf_crypt);

			envfos = new FileOutputStream("fichier_dechiffre");
			envfos.write(buf_decrypt);
			envfos.close();

		} 
		catch (Exception e) {
			Logger.trace("LdvFilesManager.cypherZipFile: exception for ldvId = " + _sLdvId + " ; stackTrace:" + e.getStackTrace(), -1, Logger.TraceLevel.ERROR) ;
			return false ;
		}
		
		return true ;
	}
	
	/**
	 * Encrypt zip file
	 * 
	 * @param sDirectory Where to write files
	 * @param sSecretKey Secret key for symmetrical encryption 
	 * @return true if all went well
	 * 
	 **/
	public boolean cypherZipFileBC(String sSecretKey) 
	{
		String sZipFile = getZipFileName() ;
		String sAESFile = getCipheredFileName() ;

		boolean bSuccess = cypherFileBC(sZipFile, sAESFile, sSecretKey, true) ; 
		
		if (bSuccess)
		{
			// Delete zip file
      //
      File fDel = new File(sZipFile) ;
      fDel.delete() ;
		}
		
		return bSuccess ;
	}
	
	/**
	 * Encrypt zip file
	 * 
	 * @param sDirectory Where to write files
	 * @param sSecretKey Secret key for symmetrical encryption 
	 * @return true if all went well
	 * 
	 **/
	public boolean decypherZipFileBC(String sSecretKey) 
	{
		String sZipFile = getZipFileName() ;
		String sAESFile = getCipheredFileName() ;
	
		boolean bSuccess = cypherFileBC(sAESFile, sZipFile, sSecretKey, false) ; 
		
		return bSuccess ;		
	}

	/**
	 * Encrypt zip file
	 * 
	 * @param sDirectory Where to write files
	 * @param sSecretKey Secret key for symmetrical encryption 
	 * @return true if all went well
	 * 
	 **/
	private boolean cypherFileBC(String sInputFile, String sOutputFile, String sSecretKey, boolean forEncryption) 
	{
		if (false == isValidFileName(sInputFile))
		{
			Logger.trace("LdvFilesManager.cypherFileBC: invalid input file name: " + sInputFile, -1, Logger.TraceLevel.ERROR) ;
			return false ;
		}
		if (false == isValidFileName(sOutputFile))
		{
			Logger.trace("LdvFilesManager.cypherFileBC: invalid output file name: " + sOutputFile, -1, Logger.TraceLevel.ERROR) ;
			return false ;
		}
		if (false == isValidSecretKey(sSecretKey))
		{
			Logger.trace("LdvFilesManager.cypherFileBC: invalid secret key for ldvId = " + _sLdvId, -1, Logger.TraceLevel.ERROR) ;
			return false ;
		}
		
		// File to cypher or decypher
		//
		File f = new File(sInputFile) ;
		
		// Create a file length wide buffer 
		//
		byte[] buffer = new byte[(int)f.length()] ;
		
		// Open input file
		//
		DataInputStream in ;
		try
		{
			in = new DataInputStream(new FileInputStream(f)) ;
		} 
		catch (FileNotFoundException eNotFound)
		{
			Logger.trace("LdvFilesManager.cypherFileBC: exception when opening input file " + sInputFile + " ; stackTrace:" + eNotFound.getStackTrace(), -1, Logger.TraceLevel.ERROR) ;
			return false ;
		}
		
		try
		{
			in.readFully(buffer) ;
			in.close() ;
		} 
		catch (IOException eIO)
		{
			Logger.trace("LdvFilesManager.cypherFileBC: exception when reading input file " + sInputFile + " ; stackTrace:" + eIO.getStackTrace(), -1, Logger.TraceLevel.ERROR) ;
			return false ;
		}
			
		// Secret key
		//
		byte[] key = sSecretKey.getBytes() ;
		assert key.length == 32 ; // 32 bytes == 256 bits
		CipherParameters cipherParameters = new KeyParameter(key) ;

		/*
		 * A full list of BlockCiphers can be found at http://www.bouncycastle.org/docs/docs1.6/org/bouncycastle/crypto/BlockCipher.html
		 */
		BlockCipher blockCipher = new AESEngine() ;

		/*
		 * Paddings available (http://www.bouncycastle.org/docs/docs1.6/org/bouncycastle/crypto/paddings/BlockCipherPadding.html):
		 *   - ISO10126d2Padding
		 *   - ISO7816d4Padding
		 *   - PKCS7Padding
		 *   - TBCPadding
		 *   - X923Padding
		 *   - ZeroBytePadding
		 */
		BlockCipherPadding blockCipherPadding = new ZeroBytePadding() ;

		BufferedBlockCipher bufferedBlockCipher = new PaddedBufferedBlockCipher(blockCipher, blockCipherPadding) ;

		byte[] buf_crypt;
		try
		{
			buf_crypt = process(buffer, bufferedBlockCipher, cipherParameters, forEncryption) ;
		} 
		catch (InvalidCipherTextException eCipher)
		{
			Logger.trace("LdvFilesManager.cypherFileBC: Cipher exception for ldvId = " + _sLdvId + " ; stackTrace:" + eCipher.getStackTrace(), -1, Logger.TraceLevel.ERROR) ;
			return false ;
		}
			
		// Open output file
		//
		FileOutputStream envfos ;
		try
		{
			envfos = new FileOutputStream(sOutputFile) ;
		} 
		catch (FileNotFoundException eNotFound)
		{
			Logger.trace("LdvFilesManager.cypherFileBC: exception when opening output file " + sOutputFile + " ; stackTrace:" + eNotFound.getStackTrace(), -1, Logger.TraceLevel.ERROR) ;
			return false ;
		}
		
		try
		{
			envfos.write(buf_crypt) ;
			envfos.close() ;
		} 
		catch (IOException eIO)
		{
			Logger.trace("LdvFilesManager.cypherFileBC: exception when writing output file " + sOutputFile + " ; stackTrace:" + eIO.getStackTrace(), -1, Logger.TraceLevel.ERROR) ;
			return false ;
		}
			
		return true ;
	}
	
	private byte[] encrypt(byte[] input, BufferedBlockCipher bufferedBlockCipher, CipherParameters cipherParameters) throws InvalidCipherTextException 
	{
    boolean forEncryption = true ;
    return process(input, bufferedBlockCipher, cipherParameters, forEncryption) ;
	}

	private byte[] decrypt(byte[] input, BufferedBlockCipher bufferedBlockCipher, CipherParameters cipherParameters) throws InvalidCipherTextException 
	{
		boolean forEncryption = false ;
    return process(input, bufferedBlockCipher, cipherParameters, forEncryption) ;
	}

	private byte[] process(byte[] input, BufferedBlockCipher bufferedBlockCipher, CipherParameters cipherParameters, boolean forEncryption) throws InvalidCipherTextException 
	{
    bufferedBlockCipher.init(forEncryption, cipherParameters) ;

    int inputOffset = 0;
    int inputLength = input.length;

    int maximumOutputLength = bufferedBlockCipher.getOutputSize(inputLength);
    byte[] output = new byte[maximumOutputLength];
    int outputOffset = 0;
    int outputLength = 0;

    int bytesProcessed;

    bytesProcessed = bufferedBlockCipher.processBytes(
            input, inputOffset, inputLength,
            output, outputOffset
        );
    outputOffset += bytesProcessed;
    outputLength += bytesProcessed;

    bytesProcessed = bufferedBlockCipher.doFinal(output, outputOffset);
    outputOffset += bytesProcessed;
    outputLength += bytesProcessed;

    if (outputLength == output.length) {
        return output;
    } else {
        byte[] truncatedOutput = new byte[outputLength];
        System.arraycopy(
                output, 0,
                truncatedOutput, 0,
                outputLength
            );
        return truncatedOutput;
    }
	}
	
	/**
	 * Get Main Directory name
	 *     
	 * @return directory name as a String
	 * 
	 **/
	public String getMainDirectoryName()
	{
		return _sBaseDir + _sLdvId + _sDirSeparator ;
	}
	
	/**
	 * Get Working Directory name
	 *     
	 * @return directory name as a String
	 * 
	 **/
	public String getWorkingDirectoryName()
	{
		// String sBaseDir = Ldv_DbParameters._sFilesDir ;
		String sWorkingDirectory = _sBaseDir + _sLdvId + _sDirSeparator + "atWork" + _sDirSeparator ;
		
		return sWorkingDirectory ;
	}
	
	/**
	 * Check if Working Directory exists
	 *     
	 * @return true if working directory exists 
	 * 
	 **/
	public boolean existWorkingDirectoryName()
	{
		String sWorkingDirectory = getWorkingDirectoryName() ; 
		File f = new File(sWorkingDirectory) ;
		
		return f.exists() ;
	}
	
	/**
	 * Check if a file exists inside Working Directory
	 *     
	 * @return true if file exists 
	 * 
	 **/
	public boolean existWorkingDirectoryFile(String sShortFileName)
	{
		String sCompleteFileName = getWorkingFileCompleteName(sShortFileName) ; 
		return existFile(sCompleteFileName) ;
	}
	
	/**
	 * Get Garbage Directory name
	 *     
	 * @return garbage directory name as a String
	 * 
	 **/
	public String getGarbageDirectoryName() {
		return _sBaseDir + _sLdvId + _sDirSeparator + "garbage" + _sDirSeparator ;
	}
	
	/**
	 * Get Complete name for a Working Directory file
	 * 
	 * @param sFileName simple file name
	 * @return complete file name inside working directory
	 * 
	 **/
	public String getWorkingFileCompleteName(String sFileName)
	{
		if ((null == sFileName) || sFileName.equals(""))
			return "" ;
		
		return getWorkingDirectoryName() + sFileName ;
	}
	
	/**
	 * Get zip file name
	 *     
	 * @return zip file name as a String
	 * 
	 **/
	public String getZipFileName() {
		return getMainDirectoryName() + _sLdvId + ".zip" ;
	}
	
	/**
	 * Get ciphered file name
	 *     
	 * @return ciphered file name as a String
	 * 
	 **/
	public String getCipheredFileName() {
		return getMainDirectoryName() + _sLdvId + ".ldv" ;
	}
	
	/**
	 * Check if a file exists inside Working Directory
	 *     
	 * @return true if file exists 
	 * 
	 **/
	public boolean existCipheredFile()
	{
		String sCompleteFileName = getCipheredFileName() ; 
		return existFile(sCompleteFileName) ;
	}
	
	/**
	 * Check if a file exists
	 *     
	 * @return true if file exists 
	 **/
	public boolean existFile(String sCompleteFileName)
	{
		File f = new File(sCompleteFileName) ;		
		return f.exists() ;
	}
	
	/**
	 * Verifies that the specified file name is valid
	 * 	  
	 * @param sFileName the file name to validate
	 * @return true if valid, false if invalid
	 */
	public static boolean isValidFileName(String sFileName) 
	{
		if ((null == sFileName) || (sFileName.equals(""))) 
			return false ;
		
		return true ;
	}
	
	/**
	 * Verifies that the specified string is a valid secret key
	 * 	  
	 * @param sSecretKey the secret key to validate
	 * @return true if valid, false if invalid
	 */
	public static boolean isValidSecretKey(String sSecretKey)
	{
		if ((null == sSecretKey) || (sSecretKey.equals(""))) 
			return false ;
		
		if (sSecretKey.length() != 32)
			return false ;
		
		return true ;
	}
	
	/**
	 * Get Complete name for a file, simply as base directory + file name
	 * 
	 * @param sFileName simple file name
	 * @return complete file name in base directory
	 * 
	 **/
	public String getFileCompleteName(String sFileName)
	{
		if ((null == sFileName) || sFileName.equals(""))
			return "" ;
		
		return _sBaseDir + sFileName ;
	}
}
