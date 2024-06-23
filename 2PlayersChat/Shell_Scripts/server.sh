# . -> Compile the java classes of the chosen path to this directory
javac -d . ../src/main/java/org/example/ClientHandler.java
javac -d . ../src/main/java/org/example/Server.java

# Execute the java classes
java -cp . org.example.Server

