/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Archie
 */
import Transportation.*;
import GUI.WinTransport;

public class Main {
 
    public static void main(String args[]){
        System.out.println("Running the transportation example");
        Transportation_solver ts = new Transportation_solver("actual_file_name.txt");
        ts.calculate_demand_suminister();
        ts.print_object_information();
        System.out.println(ts.createLpModel());
        ts.calcResult();
        ts.printResult();
        ts.print_result_in_nodes();
        
        //WinTransport GUI = new WinTransport(ts);
    }
    
    
}
