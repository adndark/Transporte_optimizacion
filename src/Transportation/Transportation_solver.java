/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Archie
 */
package Transportation;
import java.io.*;
import java.util.StringTokenizer;


public class Transportation_solver {
    
    private double cost_matrix[][];
    private double augmented_cost_matrix[][];
    private double origin_nodes[];
    private double destiny_nodes[];
    private int origin_nodes_counter;
    private int destiny_nodes_counter;
    private int total_nodes;
    
    public Transportation_solver(String filename){
        read_config_file(filename);
        
        
    }
    
    public boolean read_config_file(String config_file){
        //Opening file
        try{
            FileInputStream fstream = new FileInputStream(config_file);
            DataInputStream in =  new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String bfLine;

            bfLine = br.readLine();
            bfLine = br.readLine();
            total_nodes = Integer.parseInt(bfLine);
            this.cost_matrix = new double[total_nodes][total_nodes];
            //this.augmented_cost_matrix = new double[total_nodes + origin_nodes_counter][total_nodes + destiny_nodes_counter];
            this.origin_nodes = new double[total_nodes];
            this.destiny_nodes = new double[total_nodes];
            
            while((bfLine = br.readLine()) != null){
                
                
                
                if(bfLine.equals("Origen:")){
                    while(!(bfLine = br.readLine()).equals("Destino:")){
                        bfLine = bfLine.substring(0, bfLine.length()-1);
                        StringTokenizer tokens = new StringTokenizer(bfLine, ",");
                        String origin = tokens.nextToken();
                        double suministry = Double.parseDouble(tokens.nextToken());
                        this.origin_nodes[(int)(origin.charAt(0) - 'A')] = suministry;
                        this.origin_nodes_counter++;
                        //System.out.println(suministry);
                    }
                }
                
                if(bfLine.equals("Destino:")){
                    while(!(bfLine = br.readLine()).equals("Costos 1:")){
                        bfLine = bfLine.substring(0, bfLine.length()-1);
                        StringTokenizer tokens = new StringTokenizer(bfLine, ",");
                        String destiny = tokens.nextToken();
                        double demand = Double.parseDouble(tokens.nextToken());
                        this.destiny_nodes[destiny.charAt(0) - 'A'] = demand;
                        this.destiny_nodes_counter++;
                        //System.out.println(demand);
                    }
                }
                
                if(bfLine.equals("Costos 1:")){
                    while((bfLine = br.readLine())!= null){
                        bfLine = bfLine.substring(0, bfLine.length()-1);
                        StringTokenizer tokens = new StringTokenizer(bfLine, ",");
                        String node = tokens.nextToken();
                        String tmp_value;
                        double cost = 0;
                        int node_counter = 0;
                        while(tokens.hasMoreTokens()){
                            tmp_value = tokens.nextToken();
                            if(tmp_value.charAt(0) == '-'){
                                cost = 1000;
                            }else{
                                cost = Integer.parseInt(tmp_value);
                            }
                            System.out.println(cost);
                            this.cost_matrix[node.charAt(0) - 'A'][node_counter] = cost;
                            node_counter++;
                        }
                        //total_nodes++;
                    }
                }
            }
        this.augmented_cost_matrix = new double[total_nodes + origin_nodes_counter][total_nodes + destiny_nodes_counter];
        copy_cost_matrix_to_augmented_matrix();
                
        }catch(IOException e){
               System.out.println("'Reading error");
        }
        return true;
    }
    
    private void copy_cost_matrix_to_augmented_matrix(){
        int row_counter = 0;
        for(int i = 0; i < total_nodes; i++){
            if(this.origin_nodes[i] != 0){
                for(int row = row_counter; row < this.origin_nodes_counter; row++){
                    for(int col = 0; col < this.total_nodes + this.destiny_nodes_counter; col++){
                        if(col == i){
                            this.augmented_cost_matrix[row][col] = 0;
                        }else{
                            this.augmented_cost_matrix[row][col] = 1000;
                        }
                    }
                }
                row_counter++;
            }
        }
        int col_counter = 0;
        for(int i = 0; i < total_nodes; i++){
            if(this.destiny_nodes[i] != 0){
                for(int col = this.total_nodes + col_counter; col < this.total_nodes + this.destiny_nodes_counter; col++){
                    for(int row = this.origin_nodes_counter; row < this.total_nodes + this.origin_nodes_counter; row++){
                        if(row - this.origin_nodes_counter == i){
                            this.augmented_cost_matrix[row][col] = 0;
                        }else{
                            this.augmented_cost_matrix[row][col] = 1000;
                        }
                    }
                }
                col_counter++;
            }
        }
        
        for(int i = this.origin_nodes_counter; i < this.total_nodes + this.origin_nodes_counter; i++){
            for(int j = 0; j < this.total_nodes; j++){
                this.augmented_cost_matrix[i][j] = this.cost_matrix[i - this.origin_nodes_counter][j];
            }
        }
        
        
        
    }
    
    
    
    public void print_object_information(){
        char node = 'A';
        System.out.println("Cost matrix");
        for(int i =0; i < total_nodes; i++){
            node = (char) ('A' + i);
            System.out.print(node + " ");
            for(int j = 0; j < total_nodes; j++){
                System.out.printf("%.0f\t", cost_matrix[i][j]);
            }
            System.out.println();
        }
        
        System.out.println("Total origin nodes "+ this.origin_nodes_counter);
        System.out.println("Origin array");
        for(int i = 0; i < total_nodes; i++){
            System.out.print(this.origin_nodes[i] + " ");
        }System.out.println();
        
        System.out.println("Total destiny nodes "+ this.destiny_nodes_counter);
        System.out.println("Destiny array");
        for(int i = 0; i < total_nodes; i++){
            System.out.print(this.destiny_nodes[i] + " ");
        }System.out.println();
        
        System.out.println("Augemented Cost matrix");
        for(int i =0; i < total_nodes + this.origin_nodes_counter; i++){
            node = (char) ('A' + i - this.origin_nodes_counter);
            if(i >= this.origin_nodes_counter){
                System.out.print(node + " ");
            }else{
                System.out.print("S"+ (i+1)+" ");
            }
            for(int j = 0; j < total_nodes + this.destiny_nodes_counter; j++){
                System.out.printf("%.0f",augmented_cost_matrix[i][j]);
                System.out.print( "\t");
            }
            System.out.println();
        }
        
    }
    
}
