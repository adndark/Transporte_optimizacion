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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;
import lpsolve.*;


//testing github
public class Transportation_solver {
    
    private double cost_matrix[][];
    private double augmented_cost_matrix[][];
    private double result_matrix[][];
    private double origin_nodes[];
    private double destiny_nodes[];
    private int origin_nodes_counter;
    private int destiny_nodes_counter;
    private int total_nodes;
    private double suministry_array[];
    private double demand_array[];
    private String result_in_nodes_;
    
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
                            //System.out.println(cost);
                            this.cost_matrix[node.charAt(0) - 'A'][node_counter] = cost;
                            node_counter++;
                        }
                        //total_nodes++;
                    }
                }
            }
            
        this.augmented_cost_matrix = new double[total_nodes + origin_nodes_counter][total_nodes + destiny_nodes_counter];
        this.suministry_array = new double[this.total_nodes + this.origin_nodes_counter];
        this.demand_array = new double[this.total_nodes + this.destiny_nodes_counter];
        
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
    
    
    public void calculate_demand_suminister(){
        
        boolean ready_to_calculate_matrix[][] = new boolean[this.total_nodes][this.total_nodes];
        double suministry_matrix[][] = new double[this.total_nodes][this.total_nodes];
        boolean is_node_calculated_array[] = new boolean[this.total_nodes];
        Queue<Integer> nodes_to_visit = new LinkedList<Integer>();
        
        for(int row = 0; row < this.total_nodes; row++ ){
            is_node_calculated_array[row] = false;
            for(int col = 0; col < this.total_nodes; col++){
                if(this.cost_matrix[row][col] > 0 && this.cost_matrix[row][col] != 1000){
                    ready_to_calculate_matrix[row][col] = true;
                }else{
                    ready_to_calculate_matrix[row][col] = false;
                }
                suministry_matrix[row][col] = 0;
            }
        }
        
        //setting initial nodes to visit, origin nodes;
        for(int i = 0; i < this.total_nodes; i++){
            if(this.origin_nodes[i] > 0){
                nodes_to_visit.add(i);
            }
        }
        
        int current_node = 0;
        int current_node_suministry = 0;
        int current_suministry_sum = 0;
        boolean node_ready_to_push = false;
        
        int counter = 0;
        while(!nodes_to_visit.isEmpty()){
            //System.out.println("counter "+ counter++);
            current_suministry_sum = 0;
            current_node = nodes_to_visit.poll();
            current_node_suministry = (int) this.origin_nodes[current_node];
            if(is_node_calculated_array[current_node] == true){
                //System.out.println("continue because of is_node_calculated_array current_node "+ current_node);
                continue;
            }
            
            for(int row = 0; row < this.total_nodes; row++){
                if((node_ready_to_push = ready_to_calculate_matrix[row][current_node])== true)break;
                //if((ready_to_calculate_matrix[row][current_node])== true)break;
                current_suministry_sum += suministry_matrix[row][current_node];
            }
            if(node_ready_to_push != false){
                //System.out.println("continue because of node_ready_to_push");
                continue;
            }
            
            this.suministry_array[current_node + this.origin_nodes_counter] = current_suministry_sum + current_node_suministry;
            
            for(int col = 0; col < this.total_nodes; col++){
                if(ready_to_calculate_matrix[current_node][col] == true){
                    suministry_matrix[current_node][col] += this.suministry_array[current_node + this.origin_nodes_counter];
                    ready_to_calculate_matrix[current_node][col] = false;
                    nodes_to_visit.add(col);
                }
            }
            //System.out.println("finished with new node");
            is_node_calculated_array[current_node] = true;
        }
        int current_index = 0;
        for(int j = 0; j < this.total_nodes; j++ ){
            if(this.origin_nodes[j] > 0 && this.origin_nodes[j] != 1000){
                this.suministry_array[current_index++] = this.origin_nodes[j];
            }
        }
        
        for(int i = 0; i < this.total_nodes; i++){
            this.demand_array[i] = this.suministry_array[i+this.origin_nodes_counter];
        }
        current_index = this.total_nodes;
        for(int i = 0; i < this.total_nodes; i++){
            if(this.destiny_nodes[i] > 0 && this.destiny_nodes[i] != 1000){
                this.demand_array[current_index++] = this.destiny_nodes[i];
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
        
        System.out.println("\nTotal origin nodes "+ this.origin_nodes_counter);
        System.out.println("Origin array");
        for(int i = 0; i < total_nodes; i++){
            System.out.print(this.origin_nodes[i] + " ");
        }System.out.println();
        
        System.out.println("\nTotal destiny nodes "+ this.destiny_nodes_counter);
        System.out.println("Destiny array");
        for(int i = 0; i < total_nodes; i++){
            System.out.print(this.destiny_nodes[i] + " ");
        }System.out.println();
        
        System.out.println("\nAugemented Cost matrix");
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
        
        System.out.println("\nSuministry_array");
        for(int i = 0; i < this.total_nodes + this.origin_nodes_counter; i++){
            System.out.printf("%.0f ", this.suministry_array[i]);
        }
        
        System.out.println("\nDemand_array");
        for(int i = 0; i < this.total_nodes + this.destiny_nodes_counter; i++){
            System.out.printf("%.0f ", this.demand_array[i]);
        }
        
    }
    
    public String createLpModel(){
               
        //here goes magic
        int x = total_nodes + origin_nodes_counter;
        int y = total_nodes + destiny_nodes_counter;        
        String dosEquis[][] = new String[x][y];        
        NumberFormat form = new DecimalFormat("00");
        String file = "";
        
        for(int i = 0; i < x; i++){
            for(int j = 0; j < y; j++){
                dosEquis[i][j] = "x" + form.format(i + 1) + form.format(j + 1);
            }
        }
        
        file = "\n/*Modelo LP Auto-generado*/\n";
        
        file = file + "min:\n";
        
        String aux = "";
        for(int i = 0; i < x; i++){
            for(int j = 0; j < y; j++){
                if(i == x-1 && j == y-1){
                    aux = aux + (int)augmented_cost_matrix[i][j] + dosEquis[i][j] + ";";
                } else {
                    aux = aux + (int)augmented_cost_matrix[i][j] + dosEquis[i][j] + " + ";
                }
            }
            aux = aux + "\n";
        }
        file = file + aux;
        
        file = file + "\n/* Restricciones de Suministros */\n";
        
        aux = "";
        for(int i = 0; i < x; i++){
            for(int j = 0; j < y; j++){
                if(j == y-1){
                    aux = aux + dosEquis[i][j] + " <= " + (int)suministry_array[i] + ";";
                } else {
                    aux = aux + dosEquis[i][j] + " + ";
                }
            }
            aux = aux + "\n";
        }
        file = file + aux;
        
        file = file + "\n/* Restricciones de Demanda */\n";
        
        aux = "";
        for(int col = 0; col < y; col++){
            for(int row = 0; row < x; row++){
                if(row == x - 1){
                    aux = aux + dosEquis[row][col] + " >= " + (int)demand_array[col] + ";";
                } else {
                    aux = aux + dosEquis[row][col] + " + ";
                }
            }
            aux = aux + "\n";
        }
        file = file + aux;
        
        file = file + "\nint\n";
                
        aux = "";
        for(int i = 0; i < x; i++){
            for(int j = 0; j < y; j++){
                if(i == x-1 && j == y-1){
                    aux = aux + dosEquis[i][j] + ";";
                } else {
                    aux = aux + dosEquis[i][j] + " , ";
                }
            }
            aux = aux + "\n";
        }
        file = file + aux;
        
        //write to file magic
        try {
            FileWriter fstream = new FileWriter("trans.lp");
            BufferedWriter fout = new BufferedWriter(fstream);
            fout.write(file);
            fout.close();
        } catch(IOException e){
          System.out.println(e.getMessage());
        }
        
        return file;
    }
    
    public void calcResult(){
    
        //if(result_matrix == null)return;
        //LPSOLVE!
        try{
            //readLp(filename, verbose, modelName);
            LpSolve solver = LpSolve.readLp("trans.lp", 0, null);
            
            //resolver
            solver.solve();
                        
            //pasar el arreglo de resultados a matriz
            int x = total_nodes + origin_nodes_counter;
            int y = total_nodes + destiny_nodes_counter;
            
            result_matrix = new double[x][y];
            double[] var = solver.getPtrVariables();
            
            for(int i = 0; i < x; i++){
                for(int j = 0; j < y; j++){
                    result_matrix[i][j] = var[(i * x) + j];
                }
            }
            
            //liberar la memoria
            solver.deleteLp();
        } catch(LpSolveException e){
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    public void printResult(){
    
        int x = result_matrix.length;
        int y = result_matrix[0].length;
        
        System.out.println("\nMatriz Resultado:");
        for(int i = 0; i < x; i++){
            for(int j = 0; j < y; j++){
                System.out.print((int)result_matrix[i][j] + "\t");
            }
            System.out.println("\n");
       }
    }
    
    public void print_result_in_nodes(){
        char origin_node_label;
        char destiny_node_label;
        String result = "";
        
        for(int row = 0 ; row < this.origin_nodes_counter + this.total_nodes; row++){
            for(int col = 0; col < this.total_nodes; col++){
                if(this.result_matrix[row][col] > 0 && (row >= this.origin_nodes_counter) && row-this.origin_nodes_counter != col){
                    origin_node_label = (char) ('A' + row - this.origin_nodes_counter);
                    destiny_node_label = (char) ('A' + col);
                    result += this.result_matrix[row][col] + " from " + origin_node_label + " to " + destiny_node_label + "\n";
                }
            }
        }
        
        System.out.println(result);
        this.result_in_nodes_ = result;
        //this.result_text_area_.setText(result);
    }

    public double[][] getCost_matrix() {
        return cost_matrix;
    }

    public double[][] getAugmented_cost_matrix() {
        return augmented_cost_matrix;
    }

    public double[][] getResult_matrix() {
        return result_matrix;
    }

    public double[] getOrigin_nodes() {
        return origin_nodes;
    }

    public double[] getDestiny_nodes() {
        return destiny_nodes;
    }

    public int getOrigin_nodes_counter() {
        return origin_nodes_counter;
    }

    public int getDestiny_nodes_counter() {
        return destiny_nodes_counter;
    }

    public int getTotal_nodes() {
        return total_nodes;
    }

    public double[] getSuministry_array() {
        return suministry_array;
    }

    public double[] getDemand_array() {
        return demand_array;
    }
    
    
    
}
