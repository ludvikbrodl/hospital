openssl req -new -x509 -keyout CAkey.pem -out CA.crt -passout pass:password -subj /CA=SE/ST=Skane/O=LU/localityName=LTH/unitName=LTH/commonName=CA/emailAdress=CA@certificate.com
echo Y | keytool -import -alias firstCA -file CA.crt -keystore clienttruststore -storepass password
echo Y | keytool -importcert -alias CAcert -file CA.crt -keystore clientkeystore -trustcacerts -storepass password


#Add gov
			keytool -genkeypair -keystore clientkeystore -alias aliasgov -dname CN=gov/,O=Government,L=Lund,ST=Skane,C=SE -storepass password -keypass password

			keytool -certreq -file certreq.csr -alias aliasgov -keystore clientkeystore -storepass password -keypass password

			openssl x509 -req -in certreq.csr -CA CA.crt -CAkey CAkey.pem -CAcreateserial -out signedCSR.csr -passin pass:password

			echo Y | 	keytool -importcert -alias aliasgov -file signedCSR.csr -keystore clientkeystore -storepass password -keypass password
