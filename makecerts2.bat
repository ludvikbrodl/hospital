divisions=(HeartUnit CancerUnit OntIFotenUnit)

openssl req -new -x509 -keyout CAkey.pem -out CA.crt -passout pass:CApassword -subj /CA=SE/ST=Skane/O=LU/localityName=LTH/unitName=LTH/commonName=CA/emailAdress=CA@certificate.com
echo Y | keytool -import -alias firstCA -file CA.crt -keystore clienttruststore -storepass password



#Add gov
			echo Y | keytool -importcert -alias CAcert -file CA.crt -keystore clientkeystore_gov -trustcacerts -storepass passgov

			keytool -genkeypair -keystore clientkeystore_gov -alias aliasgov -dname CN=gov/,O=Government,L=Lund,ST=Skane,C=SE -storepass passgov -keypass passgov

			keytool -certreq -file certreq.csr -alias aliasgov -keystore clientkeystore_gov -storepass passgov -keypass passgov

			openssl x509 -req -in certreq.csr -CA CA.crt -CAkey CAkey.pem -CAcreateserial -out signedCSR.csr -passin pass:CApassword

			echo Y | 	keytool -importcert -alias aliasgov -file signedCSR.csr -keystore clientkeystore_gov -storepass passgov -keypass passgov


#Add doctors
for i in `seq 1 3`; 
        do
      		echo Y | keytool -importcert -alias CAcert -file CA.crt -keystore clientkeystore_doctor$i -trustcacerts -storepass doctor$i
        	keytool -genkeypair -keystore clientkeystore_doctor$i -alias aliasdoc$i -dname CN=doc/doctor$i,OU=${divisions[($i-1) % ${#divisions[@]}]},O=Hospital,L=Lund,ST=Skane,C=SE -storepass doctor$i -keypass doctor$i

			keytool -certreq -file certreq.csr -alias aliasdoc$i -keystore clientkeystore_doctor$i -storepass doctor$i -keypass doctor$i

			openssl x509 -req -in certreq.csr -CA CA.crt -CAkey CAkey.pem -CAcreateserial -out signedCSR.csr -passin pass:CApassword

			echo Y | 	keytool -importcert -alias aliasdoc$i -file signedCSR.csr -keystore clientkeystore_doctor$i -storepass doctor$i -keypass doctor$i
        done


#Add nurses
for i in `seq 1 4`; 
        do
			echo Y | keytool -importcert -alias CAcert -file CA.crt -keystore clientkeystore_nurse$i -trustcacerts -storepass nurse$i

        	keytool -genkeypair -keystore clientkeystore_nurse$i -alias aliasnurse$i -dname CN=nurse/nurse$i,OU=${divisions[($i-1) % ${#divisions[@]}]},O=Hospital,L=Lund,ST=Skane,C=SE -storepass nurse$i -keypass nurse$i

			keytool -certreq -file certreq.csr -alias aliasnurse$i -keystore clientkeystore_nurse$i -storepass nurse$i -keypass nurse$i

			openssl x509 -req -in certreq.csr -CA CA.crt -CAkey CAkey.pem -CAcreateserial -out signedCSR.csr -passin pass:CApassword

			echo Y | 	keytool -importcert -alias aliasnurse$i -file signedCSR.csr -keystore clientkeystore_nurse$i -storepass nurse$i -keypass nurse$i
        done


#Add patients
for i in `seq 1 3`; 
        do
			echo Y | keytool -importcert -alias CAcert -file CA.crt -keystore clientkeystore_patient$i -trustcacerts -storepass patient$i

        	keytool -genkeypair -keystore clientkeystore_patient$i -alias aliaspatient$i -dname CN=patient/patient$i,OU=${divisions[($i-1) % ${#divisions[@]}]},O=Hospital,L=Lund,ST=Skane,C=SE -storepass patient$i -keypass patient$i

			keytool -certreq -file certreq.csr -alias aliaspatient$i -keystore clientkeystore_patient$i -storepass patient$i -keypass patient$i

			openssl x509 -req -in certreq.csr -CA CA.crt -CAkey CAkey.pem -CAcreateserial -out signedCSR.csr -passin pass:CApassword

			echo Y | 	keytool -importcert -alias aliaspatient$i -file signedCSR.csr -keystore clientkeystore_patient$i -storepass patient$i -keypass patient$i
        done


#Add server (With new keystore)

echo Y | keytool -import -alias firstCA -file CA.crt -keystore serverkeystore -storepass passserver
echo Y | keytool -importcert -alias CAcert -file CA.crt -keystore serverkeystore -trustcacerts -storepass passserver

keytool -genkeypair -keystore serverkeystore -alias aliasserver -dname CN=Server,O=Hospital,L=Lund,ST=Skane,C=SE -storepass passserver -keypass passserver
keytool -certreq -file certreq.csr -alias aliasserver -keystore serverkeystore -storepass passserver -keypass passserver
openssl x509 -req -in certreq.csr -CA CA.crt -CAkey CAkey.pem -CAcreateserial -out signedCSR.csr -passin pass:CApassword
echo Y | 	keytool -importcert -alias aliasserver -file signedCSR.csr -keystore serverkeystore -storepass passserver -keypass passserver

cp clienttruststore servertruststore

