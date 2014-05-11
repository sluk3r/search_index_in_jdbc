database of search_schema is required.

---------------
1, Source codes are copied from http://mprabhat.wordpress.com/2012/08/13/create-lucene-index-in-database-using-jdbcdirectory/. Thanks a lot.


2, As depicted, jdbc url of "jdbc:mysql://localhost:3306/search_schema?emulateLocators=true&useUnicode=true&characterEncoding=UTF-8&useFastDateParsing=false" is hard-coded, and both of user name and password ard hard-coded as "root".


3, run "mvn clean test".



-----------
the real data of index are stored in database as blob format.