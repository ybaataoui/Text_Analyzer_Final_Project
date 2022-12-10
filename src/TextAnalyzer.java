
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import org.apache.commons.lang.StringUtils;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;


/*
 * 1- import  
 * 2- load and register the driver
 * 3 -create connection
 * 4- create a statment
 * 5- execute the statment
 * 6- process the result
 * 7- close. 
 */
public class TextAnalyzer extends Application {
		
		static TextField text ;
		Label lblPrint;
		static Button btn;
		static Button btnDisplay;
		static TextArea ta;
			
		private static String url      = "jdbc:mysql://localhost:3306/words";   //database specific url.
	    private static String user     = "root";
	    private static String password = "AdminRoot";
		
		@Override // Override the start method in the Application class
		  public void start(Stage primaryStage) throws ClassNotFoundException, IOException {
			  
		    // Panel p to hold the label and text field
		    BorderPane paneForTextField = new BorderPane();
		    paneForTextField.setPadding(new Insets(5, 5, 5, 5)); 
		    paneForTextField.setStyle("-fx-border-color: white");
		    paneForTextField.setLeft(new Label("File name: "));
		    
		    text = new TextField();
		    text.setAlignment(Pos.BOTTOM_RIGHT);
		    paneForTextField.setCenter(text);
		    
		  //button 
		    BorderPane paneForButton = new BorderPane();
		    
		    btn = new Button("Analyse");
		    paneForButton.setLeft(btn);

		    btnDisplay = new Button("Display");
		    //btnDisplay.setAlignment(Pos.BOTTOM_RIGHT);
		    paneForButton.setRight(btnDisplay);
		    
		    BorderPane mainPane = new BorderPane();
		    // Text area to display contents
		    ta = new TextArea();
		    mainPane.setCenter(new ScrollPane(ta));
		    mainPane.setTop(paneForTextField);
		    mainPane.setBottom(paneForButton);
		    
		    //Create File chooser
		    FileChooser fileChooser = new FileChooser();

	        Button selectFile = new Button("Select File");
	        selectFile.setOnAction(e -> {
	            File file = fileChooser.showOpenDialog(primaryStage);
	            if (file != null) {
	            	text.setText(file.getPath());
	            }
	        });

	        //VBox vBox = new VBox(button);
	        paneForTextField.setRight(selectFile);
	        
		    
		 // Create a scene and place it in the stage
		    Scene scene = new Scene(mainPane, 450, 200, Color.BEIGE);
		    primaryStage.setTitle("Word Occurrences"); // Set the stage title
		    primaryStage.setScene(scene); // Place the scene in the stage
		    primaryStage.show(); // Display the stage
 
	        TextAnalyzer TA = new TextAnalyzer();
	        	
	        btn.setOnAction(e -> {
		    	//action();
		    	try {
					insertData();
					ta.appendText("Data inserted succesfully into database");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		    	text.clear();
		    });;
		    
		    btnDisplay.setOnAction(e -> {
		    	display();
		    });
		    
		  //Expand the textErea to fit the whole text
		    ta.textProperty().addListener((obs,old,niu)->{
	            Text t = new Text(old+niu);
	            t.setFont(ta.getFont());
	            StackPane pane = new StackPane(t);
	            pane.layout();
	            double height = t.getLayoutBounds().getHeight();
	            double padding = 25 ;
	            ta.setMinHeight(height+padding);
	        });

    }
	
	/**
	 * display is a method that connect to a database 
	 * Pull up the data from it 
	 * and display it in the textArea.
	 */
		
	public static void display() {
		//Print the first 20 words from the database ordered descendant
        try(Connection connection = DriverManager.getConnection(url, user, password)) { //Create a connect to database
  	      try(Statement statement = connection.createStatement()){ // Create statement
  	    	String sql = "select * from words order by Frequency desc limit 0, 20"; // sql statement that select all words from the words table
  	    	// sort them by frequency and print only the first 20
	          
	          try(ResultSet result = statement.executeQuery(sql)){ // execute the statement
	        	  int i = 1;
	        	  while(result.next()) {
	        		  String name = result.getString("name"); //Get the words name
		                int frequency = result.getInt("Frequency"); // get the words frequency
		                
		                ta.appendText(i + "- \"" +name + "\" " + frequency + "\n");
		                System.out.println(name + " " + frequency); // print statement
	        		  i++;
	        	  }
	        	
	          }      
  	      }
        	
        } catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
	//A function that substring a string between two strings.
	/**
	 * Between is a function that substratct a string from another string using start word and end word
	 * @param STR is a parameter represent the whole text
	 * @param FirstString is a parameter that represent the start word 
	 * @param LastString is a parameter that represent the end word
	 * @return a specific string
	 */
	public static String Between(String STR , String FirstString, String LastString)
    {       
           
        String FinalString = StringUtils.substringBetween(STR, FirstString, LastString);
        
        return FinalString;
    }

	/**
	 * insertData is a method that read the file path from the text field.
	 * Connect to database and insert words with their currencies to a table in the database. 
	 */
	
	 public static void insertData() throws IOException, ClassNotFoundException {
		 
		 Class.forName("com.mysql.cj.jdbc.Driver");
		 
		String content = Files.readString(Paths.get(text.getText()));
		
		String newContent1 = Between(content, "START", "LICENSE"); // substring the text wanted
		
		newContent1 = newContent1.replaceAll("\\<[^>] *>","");
		newContent1 = newContent1.replaceAll("&mdash;", " ");
		newContent1 = newContent1.replaceAll("[^a-zA-Z0-9\\s+]", "");
		String contentLowerCase = newContent1.toLowerCase();
		
		String[] array = contentLowerCase.split(" "); // split the string into words and put them in an array

        try(Connection connection = DriverManager.getConnection(url, user, password)) {

            for (String str : array) {

                try(Statement statement = connection.createStatement()){
                    String sql = "select * from words";

                    try(ResultSet result = statement.executeQuery(sql)){
                        while(result.next()) {
                            String name = result.getString("name");
                            int frequency = result.getInt("Frequency");
                            //System.out.println(frequency);
                            if(name.equalsIgnoreCase(str)) {
                                // create our java preparedstatement using a sql update query
                                PreparedStatement ps = connection.prepareStatement(
                                        "UPDATE words w SET w.Frequency = ? WHERE w.name =" + '"'+str+'"');

                                // set the preparedstatement parameters
                                ps.setInt(1,frequency + 1);

                                // call executeUpdate to execute our sql update statement and returns number of rows affected
                                ps.executeUpdate();
                                ps.close();
                            }

                            else if(!(str.equalsIgnoreCase(name))){
                                PreparedStatement insertNewWord = connection.prepareStatement(
                                        "INSERT IGNORE INTO words(name, frequency) VALUES (?,?)");// if not add the new word to the hashmap and put its frequency to 1
                                insertNewWord.setString(1, str);
                                insertNewWord.setInt(2, 1);
                                insertNewWord.executeUpdate();
                                //insertNewWord.close();
                            }
                            // System.out.println(name + " " + frequency);
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
	 
	 /**
	 * Main method to lunch the application
	 */
 
	 public static void main(String[] args) throws IOException {
			launch(args);
	 }
	

}







