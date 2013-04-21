lottery
=======

Ex-DST Lottery Syndicate Results Checker

Until fully automated:
vi src/com/martindavey/lotto/LottoResultReader.java
Add 7 to the date on the line:
String lastDateChecked = "20130406"; // TODO Derive from file

To build and run from the shell:
cd ~/workspace/lottery
javac -d bin -sourcepath src -cp /home/martin/Downloads/javamail-1.4.6/mail.jar src/com/martindavey/lotto/*.java
Confirm date of *.class files:
ls -ltr bin/com/martindavey/lotto/
To run:
java -cp bin/:/home/martin/Downloads/javamail-1.4.6/mail.jar com.martindavey.lotto.LottoResultReader
