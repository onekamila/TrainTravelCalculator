# TrainTravelCalculator
Repo for INFO 315 Project

Train Travel Calculator or Train Tracker aims to help users understand the average delay in their Amtrak journey by taking in their input of point of origin, destination, and date. 
We scrrape data from the Amtrak official website as well as use ETL to retreive data from an archive of older Amtrak trains going back 10 years. 
We hope to provide them a historical basis for delays as well as current train status. 


## To Run ETL Package:
`javac -sourcepath .\src\main\java\*.java -classpath "C:\Program Files\Java\jdk-17.0.2\lib\ojdbc11.jar C:\Users\oneka\Documents\School\Undergrad\UGY4\INFO315\Project\ETL\out\production\ETL" -d classes
java java -cp "C:\Program Files\Java\jdk-17.0.2\lib\ojdbc11.jar;C:\Users\oneka\Documents\School\Undergrad\UGY4\INFO315\Project\ETL\out\production\ETL" main.java.ETLMain`