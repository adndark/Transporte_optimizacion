/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Archie
 */
import Transportation.*;

public class Main {
 
    public static void main(String args[]){
        System.out.println("Running the transportation example");
        Transportation_solver ts = new Transportation_solver("config_file.txt");
        ts.print_object_information();
    }
    
    
}
