# SwindleSheet Android App

This is an loaction based expense management app which does not need any user input for expense. It automaticlly read and keep track of expense. Just give SMS and Location permission, it maintain stats about your expense based on location. Where you spend how much.

## To build on you system
-install latest android studio and rest requirment android studio will install.
-and send below keys to mentioned email address.

### Facebook integration steps:
	1. Generate Key hash
	For Windows user, type this on cmd : 
		Requirment for below command 
		1. Key and Certificate Management Tool (keytool) from the Java Development Kit
		2. Openssl-for-windows openssl library for Windows from the Google Code Archive
		To generate a development key hash, run the following command in a command prompt in the Java SDK folder: 	
			keytool -exportcert -alias androiddebugkey -keystore "C:\Users\USERNAME\.android\debug.keystore" | "PATH_TO_OPENSSL_LIBRARY\bin\openssl" sha1 -binary | "PATH_TO_OPENSSL_LIBRARY\bin\openssl" base64
		3. Enter password: 'android'

	For Mac/Linux user, type this on terminal :
		keytool -exportcert -alias androiddebugkey -keystore ~/.android/debug.keystore | openssl sha1 -binary | openssl base64 
		Enter password: 'android'

	2. Mail the generated key hash to ashutosh16012@iiitd.ac.in

### Google Maps Api and firebase integration:
	1. Generate SHA-1 fingerprint
	For Windows user
		1. type this on cmd : keytool -exportcert -list -v -alias androiddebugkey -keystore %USERPROFILE%\.android\debug.keystore
		2. default password is 'android'
	For Mac/Linux user
		1. type this on terminal : keytool -exportcert -list -v -alias androiddebugkey -keystore ~/.android/debug.keystore
		2. deafault password id 'android'
	2. Mail the generated SHA-1 fingerprint to ashutosh16012@iiitd.ac.in
	

Incase of any build or installation issue feel free to reach out at ashutosh16012@iiitd.ac.in
