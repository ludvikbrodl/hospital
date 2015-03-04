divisions=(HeartUnit CancerUnit OntIFotenUnit)

openssl req -new -x509 -keyout CAkey.pem -out CA.crt -passout pass:password -subj /CA=SE/ST=Skane/O=LU/localityName=LTH/unitName=LTH/commonName=CA/emailAdress=CA@certificate.com
echo Y | keytool -import -alias firstCA -file CA.crt -keystore clienttruststore -storepass password
echo Y | keytool -importcert -alias CAcert -file CA.crt -keystore clientkeystore -trustcacerts -storepass password


#Add gov
			keytool -genkeypair -keystore clientkeystore -alias aliasgov -dname CN=gov,O=Government,L=Lund,ST=Skane,C=SE -storepass password -keypass passgov

			keytool -certreq -file certreq.csr -alias aliasgov -keystore clientkeystore -storepass password -keypass passgov

			openssl x509 -req -in certreq.csr -CA CA.crt -CAkey CAkey.pem -CAcreateserial -out signedCSR.csr -passin pass:password

			echo Y | 	keytool -importcert -alias aliasgov -file signedCSR.csr -keystore clientkeystore -storepass password -keypass passgov


#Add doctors
for i in `seq 1 4`; 
        do
        	keytool -genkeypair -keystore clientkeystore -alias aliasdoc$i -dname CN=doctor/doctor$i,OU=${divisions[($i-1) % ${#divisions[@]}]},O=Hospital,L=Lund,ST=Skane,C=SE -storepass password -keypass passdoc$i

			keytool -certreq -file certreq.csr -alias aliasdoc$i -keystore clientkeystore -storepass password -keypass passdoc$i

			openssl x509 -req -in certreq.csr -CA CA.crt -CAkey CAkey.pem -CAcreateserial -out signedCSR.csr -passin pass:password

			echo Y | 	keytool -importcert -alias aliasdoc$i -file signedCSR.csr -keystore clientkeystore -storepass password -keypass passdoc$i
        done

#Add nurses
for i in `seq 1 8`; 
        do
        	keytool -genkeypair -keystore clientkeystore -alias aliasnurse$i -dname CN=nurse/nurse$i,OU=${divisions[($i-1) % ${#divisions[@]}]},O=Hospital,L=Lund,ST=Skane,C=SE -storepass password -keypass passnurse$i

			keytool -certreq -file certreq.csr -alias aliasnurse$i -keystore clientkeystore -storepass password -keypass passnurse$i

			openssl x509 -req -in certreq.csr -CA CA.crt -CAkey CAkey.pem -CAcreateserial -out signedCSR.csr -passin pass:password

			echo Y | 	keytool -importcert -alias aliasnurse$i -file signedCSR.csr -keystore clientkeystore -storepass password -keypass passnurse$i
        done

#Add patients
for i in `seq 1 5`; 
        do
        	keytool -genkeypair -keystore clientkeystore -alias aliaspatient$i -dname CN=patient/patient$i,OU=${divisions[($i-1) % ${#divisions[@]}]},O=Hospital,L=Lund,ST=Skane,C=SE -storepass password -keypass passpatient$i

			keytool -certreq -file certreq.csr -alias aliaspatient$i -keystore clientkeystore -storepass password -keypass passpatient$i

			openssl x509 -req -in certreq.csr -CA CA.crt -CAkey CAkey.pem -CAcreateserial -out signedCSR.csr -passin pass:password

			echo Y | 	keytool -importcert -alias aliaspatient$i -file signedCSR.csr -keystore clientkeystore -storepass password -keypass passpatient$i
        done

#Add server (With new keystore)

echo Y | keytool -import -alias firstCA -file CA.crt -keystore serverkeystore -storepass password
echo Y | keytool -importcert -alias CAcert -file CA.crt -keystore serverkeystore -trustcacerts -storepass password

keytool -genkeypair -keystore serverkeystore -alias aliasserver -dname CN=Server,O=Hospital,L=Lund,ST=Skane,C=SE -storepass password -keypass passserver
keytool -certreq -file certreq.csr -alias aliasserver -keystore serverkeystore -storepass password -keypass passserver
openssl x509 -req -in certreq.csr -CA CA.crt -CAkey CAkey.pem -CAcreateserial -out signedCSR.csr -passin pass:password
echo Y | 	keytool -importcert -alias aliasserver -file signedCSR.csr -keystore serverkeystore -storepass password -keypass passserver

cp clienttruststore servertruststore



