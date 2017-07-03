package com.rkylin.settle.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.Security;
import java.security.Signature;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.BERConstructedOctetString;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.asn1.cms.IssuerAndSerialNumber;
import org.bouncycastle.asn1.cms.SignedData;
import org.bouncycastle.asn1.cms.SignerIdentifier;
import org.bouncycastle.asn1.cms.SignerInfo;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.TBSCertificateStructure;
import org.bouncycastle.asn1.x509.X509CertificateStructure;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessable;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Store;
import org.bouncycastle.util.encoders.Base64;

public class PKCS7Util {

	private String mUserCertFilePath;

	private String mUserCertPassword;

	private String mUserCertAlias;

	private X509Certificate mUserCert;

	private PrivateKey mPrivateKey;

	public static final String DSA_OID = "1.2.840.10040.4.3";

	public static final String RSA_OID = "1.2.840.113549.1.1.1";

	public static final String MD5_OID = "1.2.840.113549.2.5";

	public static final String SHA1_OID = "1.3.14.3.2.26";

	private DERObject makeObj(byte abyte0[]) throws IOException {
		if (abyte0 == null) {
			return null;
		} else {
			ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(abyte0);
			ASN1InputStream asn1inputstream = new ASN1InputStream(bytearrayinputstream);
			return asn1inputstream.readObject();
		}
	}

	private AlgorithmIdentifier makeAlgId(String s, byte abyte0[]) throws IOException {
		if (abyte0 != null)
			return new AlgorithmIdentifier(new DERObjectIdentifier(s), makeObj(abyte0));
		else
			return new AlgorithmIdentifier(new DERObjectIdentifier(s), new DERNull());
	}

	public CMSSignedData generate(byte[] plain, byte[] signature, X509Certificate x509certificate, boolean flag) throws Exception {
		CMSProcessable cmsprocessable = new CMSProcessableByteArray(plain);
		SignerInfo signInf = toSignerInfo(signature, x509certificate);
		DERObjectIdentifier derobjectidentifier = PKCSObjectIdentifiers.data;

		ASN1EncodableVector asn1encodablevector = new ASN1EncodableVector();
		ASN1EncodableVector asn1encodablevector1 = new ASN1EncodableVector();
		AlgorithmIdentifier algorithmidentifier;
		algorithmidentifier = makeAlgId(MD5_OID, null);
		asn1encodablevector.add(algorithmidentifier);
		asn1encodablevector1.add(signInf);

		DERSet derset = null;
		ASN1EncodableVector asn1encodablevector2 = new ASN1EncodableVector();
		asn1encodablevector2.add(new X509CertificateStructure((ASN1Sequence) makeObj(x509certificate.getEncoded())));
		derset = new DERSet(asn1encodablevector2);

		DERSet derset1 = null;

		ContentInfo contentinfo;
		if (flag) {
			ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
			try {
				cmsprocessable.write(bytearrayoutputstream);
			} catch (IOException ioexception2) {
				throw new CMSException("encapsulation error.", ioexception2);
			}
			BERConstructedOctetString berconstructedoctetstring = new BERConstructedOctetString(bytearrayoutputstream.toByteArray());
			contentinfo = new ContentInfo((ASN1ObjectIdentifier) derobjectidentifier, berconstructedoctetstring);
		} else {
			contentinfo = new ContentInfo((ASN1ObjectIdentifier) derobjectidentifier, null);
		}
		SignedData signeddata = new SignedData(new DERSet(asn1encodablevector), contentinfo, derset, derset1, new DERSet(asn1encodablevector1));
		ContentInfo contentinfo1 = new ContentInfo(PKCSObjectIdentifiers.signedData, signeddata);
		return new CMSSignedData(cmsprocessable, contentinfo1);
	}

	public SignerInfo toSignerInfo(byte[] signature, X509Certificate x509certificate) throws Exception {

		DEROctetString deroctetstring = new DEROctetString(signature);

		AlgorithmIdentifier algorithmidentifier = makeAlgId(MD5_OID, null);
		AlgorithmIdentifier algorithmidentifier1 = makeAlgId(RSA_OID, null);

		ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(x509certificate.getTBSCertificate());
		ASN1InputStream asn1inputstream = new ASN1InputStream(bytearrayinputstream);
		TBSCertificateStructure tbscertificatestructure = TBSCertificateStructure.getInstance(asn1inputstream.readObject());
		IssuerAndSerialNumber issuerandserialnumber = new IssuerAndSerialNumber(tbscertificatestructure.getIssuer(), tbscertificatestructure.getSerialNumber().getValue());
		return new SignerInfo(new SignerIdentifier(issuerandserialnumber), algorithmidentifier, null, algorithmidentifier1, deroctetstring, null);
	}

	private byte[] sign(byte[] inbuf, PrivateKey key) {
		byte sig[] = null;
		try {
			Signature dsa = Signature.getInstance("MD5withRSA");
			dsa.initSign(key);
			dsa.update(inbuf);
			sig = dsa.sign();
			System.err.println("sig:" + sig.length);
		} catch (Exception e) {
			System.err.println("Caught exception " + e.toString());
		}
		return sig;
	}

	private void getPrivateKeyAndCertificate() throws Exception {
		FileInputStream keyStoreStream = null;
		KeyStore keyStore = null;
		try {
			File certFile = new File(mUserCertFilePath);
			String certFileName = certFile.getName();
			char password[] = mUserCertPassword.toCharArray();
			if (certFileName.toLowerCase().indexOf(".pfx") != -1) {
				keyStoreStream = new FileInputStream(certFile);
				keyStore = KeyStore.getInstance("PKCS12");
				keyStore.load(keyStoreStream, password);
				for (Enumeration aliasesEnum = keyStore.aliases(); aliasesEnum.hasMoreElements();) {
					String alias = (String) aliasesEnum.nextElement();
					if (mPrivateKey == null) {
						PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, password);
						mPrivateKey = privateKey;
					}
					if (mUserCert == null)
						mUserCert = (X509Certificate) keyStore.getCertificate(alias);
				}

				keyStoreStream.close();
			} else if (certFileName.toLowerCase().indexOf(".ks") != -1) {
				keyStoreStream = new FileInputStream(certFile);
				keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
				keyStore.load(keyStoreStream, password);
				mPrivateKey = (PrivateKey) keyStore.getKey(mUserCertAlias, password);
				mUserCert = (X509Certificate) keyStore.getCertificate(mUserCertAlias);
				keyStoreStream.close();
			} else {
				throw new Exception("No KeyStore Found!");
			}
		} catch (Exception e) {
			throw e;
		}

	}

	public static String byteToHex(byte abyte0[]) {
		StringBuffer stringbuffer = new StringBuffer();
		for (int i = 0; i < abyte0.length; i++) {
			String s = Integer.toHexString(abyte0[i] & 0xff);
			if (s.length() != 2)
				stringbuffer.append('0').append(s);
			else
				stringbuffer.append(s);
		}

		return new String(stringbuffer);
	}

	public static byte[] hexToByte(String s) {
		int i = s.length() / 2;
		byte abyte0[] = new byte[i];
		for (int j = 0; j < i; j++) {
			String s1 = s.substring(j * 2, j * 2 + 2);
			abyte0[j] = (byte) Integer.parseInt(s1, 16);
		}

		return abyte0;
	}

	/**
	 * 签名方法
	 * @param plain  签名内容
	 * @param certFilePath证书路径
	 * @param certPassword证书密码
	 * @param certAlias
	 * @return
	 * @throws Exception
	 */
	public String getSignedData(byte[] plain, String certFilePath, String certPassword, String certAlias) throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		mUserCertFilePath = certFilePath;
		mUserCertPassword = certPassword;
		mUserCertAlias = certAlias;
		getPrivateKeyAndCertificate();
		byte[] signed = sign(plain, mPrivateKey);
		CMSSignedData cms = generate(plain, signed, mUserCert, false);
		String signedData = new String(Base64.encode(cms.getEncoded()));
		return signedData;
	}

	public static boolean verify(byte[] sign, String plaintext, String publicKeyPath) {
		boolean ok = true;
		try {
			byte[] cms = Base64.decode(sign);
			ASN1InputStream asn1InputStream = new ASN1InputStream(cms);
			DERSequence sequence = (DERSequence) asn1InputStream.readObject();
			ASN1ObjectIdentifier contentType = (ASN1ObjectIdentifier) sequence.getObjectAt(0);

			byte[] _plaintext = plaintext.getBytes();
			byte[] sha1Hash = MessageDigest.getInstance("SHA1").digest(_plaintext);
			Map<String, Object> hashes = new HashMap();
			hashes.put("1.2.156.10197.1.410", sha1Hash);
			hashes.put("1.3.14.3.2.26", sha1Hash);

			CMSSignedData cmsSignedData = new CMSSignedData(hashes, cms);
			SignerInformationStore signerInfos = cmsSignedData.getSignerInfos();
			Store certs = cmsSignedData.getCertificates();
			Collection signers = signerInfos.getSigners();
			Iterator it = signers.iterator();
			X509Certificate x509Certificate = null;
			while (it.hasNext()) {
				Object obj1 = it.next();
				SignerInformation signer = (SignerInformation) obj1;
				Collection certCollection = certs.getMatches(signer.getSID());
				Iterator certIt = certCollection.iterator();
				X509CertificateHolder cert = (X509CertificateHolder) certIt.next();
				x509Certificate = getX509Cert(cert);
				CertificateFactory cerfactory = CertificateFactory.getInstance("X.509");
				FileInputStream bais = new FileInputStream(publicKeyPath);
				X509Certificate publicCert = (X509Certificate) cerfactory.generateCertificate(bais);
				ok = signer.verify(x509Certificate, "BC") && publicCert.getSerialNumber().equals(x509Certificate.getSerialNumber());
				if (!ok) {
					return ok;
				}
			}
		} catch (Exception e) {
			ok = false;
		}
		return ok;
	}

	public static X509Certificate getX509Cert(X509CertificateHolder x509CertificateHolder) throws IOException, CertificateException {
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		InputStream is1 = new ByteArrayInputStream(x509CertificateHolder.toASN1Structure().getEncoded());
		X509Certificate theCert = (X509Certificate) cf.generateCertificate(is1);
		is1.close();
		return theCert;
	}
}