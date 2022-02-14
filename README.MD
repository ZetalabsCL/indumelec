keytool -genkeypair -alias app -keyalg RSA -keysize 2048 -keystore app.jks -validity 3650 -dname "CN=app.indumelec.cl, O=Indumelec, OU=Indumelec, L=Santiago, ST=Santiago, C=CL"

keytool -genkeypair -alias pruebas -keyalg RSA -keysize 2048 -keystore pruebas.jks -validity 3650 -dname "CN=pruebas.indumelec.cl, O=Indumelec, OU=Indumelec, L=Santiago, ST=Santiago, C=CL"

keytool -importkeystore -srckeystore pruebas.jks -destkeystore pruebas.p12 -deststoretype pkcs12

keytool -importkeystore -srckeystore app.jks -destkeystore app.p12 -deststoretype pkcs12


-Dspring.profiles.active=dev