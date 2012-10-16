/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import javax.swing.JPanel;
import javax.swing.JTextField;

import Transportation.Transportation_solver;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JTextArea;
/**
 *
 * @author Archie
 */
public class WinTransport implements Runnable{
    
    private JFrame main_window_frame_;
    private JPanel number_of_nodes_panel_;
    private JPanel origin_nodes_panel_;
    private JPanel destiny_nodes_panel_;
    private JPanel cost_nodes_panel_;
    private JTextField input_nodes_demand_[];
    private Transportation_solver ObjTransSolver_;
    
    private int win_height_;
    private int win_width_;
    private int origin_nodes_panel_width_;
    private int origin_nodes_panel_height_;
    private int number_of_nodes_panel_width_;
    private int number_of_nodes_panel_height_;
    private JLabel number_of_nodes_label_;
    private JComboBox number_of_nodes_cb_;
    private JPanel origin_nodes_label_panel_container_;
    private JPanel destiny_nodes_label_panel_container_;
    private String num_of_nodes_string_[];
    private int number_of_nodes_int;
    private JTextField cost_matrix_[][];
    private JPanel cost_matrix_panel_;
    
    private JPanel result_panel_;
    private JTextArea result_text_area_;
            
    private JTextField input_nodes_suministry_[];
    
    private JButton button;
    private JButton button2;
    private String actual_file_name_;
    
    private JFileChooser file_choser;
    private JButton openButton;
    
    
    
    public WinTransport(Transportation_solver trans_sol_obj){
        
       this.number_of_nodes_int = 11;
        
       this.ObjTransSolver_ = trans_sol_obj;
       this.win_height_ = 800;
       this.win_width_ = 1200;
       
       this.main_window_frame_ = new JFrame("Transportation solver");
       this.main_window_frame_.setLayout(new FlowLayout(FlowLayout.LEFT,20,5));
       this.main_window_frame_.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       
       
       //number of nodes panel creation
       number_of_nodes_panel_width_ = 500;
       number_of_nodes_panel_height_ = 100;
       number_of_nodes_panel_ = new JPanel(new FlowLayout());
       this.number_of_nodes_panel_.setSize(number_of_nodes_panel_width_, number_of_nodes_panel_height_);
       this.number_of_nodes_label_ = new JLabel("Number of nodes");
       this.number_of_nodes_panel_.add(this.number_of_nodes_label_); 
       this.number_of_nodes_cb_ = new JComboBox();
       this.set_number_of_nodes_for_combobox(this.number_of_nodes_int);
       this.number_of_nodes_cb_.addActionListener(number_of_nodes_cb_listener);
       this.number_of_nodes_cb_.setSize(50, 50);
       this.number_of_nodes_panel_.add(this.number_of_nodes_cb_);
       this.main_window_frame_.add(this.number_of_nodes_panel_);
       this.number_of_nodes_panel_.setVisible(true);
       
       //label for origin nodes sumistry
       this.origin_nodes_label_panel_container_ = new JPanel();
       this.origin_nodes_label_panel_container_.setPreferredSize(new Dimension(1150,20));
       this.origin_nodes_label_panel_container_.setLayout(new FlowLayout(FlowLayout.LEFT));
       this.origin_nodes_label_panel_container_.add(new JLabel("Origin nodes suministry"));
       this.main_window_frame_.add(this.origin_nodes_label_panel_container_);
       
       
       
       //origin nodes panel creation
       this.origin_nodes_panel_ = new JPanel(new GridLayout(2,13));
       this.origin_nodes_panel_.setPreferredSize(new Dimension(1100,70));
       this.create_suministry_nodes_interface();
       this.main_window_frame_.add(this.origin_nodes_panel_);
       
       this.destiny_nodes_label_panel_container_ = new JPanel();
       this.destiny_nodes_label_panel_container_.setPreferredSize(new Dimension(1150,20));
       this.destiny_nodes_label_panel_container_.setLayout(new FlowLayout(FlowLayout.LEFT));
       this.destiny_nodes_label_panel_container_.add(new JLabel("Destiny nodes demand"));
       this.main_window_frame_.add(this.destiny_nodes_label_panel_container_);
       
       
       //destiny nodes panel creation
       this.destiny_nodes_panel_ = new JPanel(new GridLayout(2,13));
       this.destiny_nodes_panel_.setPreferredSize(new Dimension(1100,70));
       this.create_demand_nodes_interface();
       this.main_window_frame_.add(this.destiny_nodes_panel_);
       
       this.cost_matrix_panel_ = new JPanel();
        this.cost_matrix_panel_.setPreferredSize(new Dimension(600,500));
       this.main_window_frame_.add(this.cost_matrix_panel_);
       
       this.button = new JButton("Solve");
       this.button.setSize(100, 100);
       this.button.setVisible(true);
       this.button.addActionListener(test);
       this.main_window_frame_.add(button,0,1);
       
       this.button2 = new JButton("Open");
       this.button2.setSize(100, 100);
       this.button2.setVisible(true);
       //this.button2.addActionListener(open);
       //this.main_window_frame_.add(button2,0,2);

       this.result_panel_ = new JPanel();
       this.main_window_frame_.add(this.result_panel_);
       this.create_result_area();

       this.main_window_frame_.setSize(this.win_width_, this.win_height_);
       this.main_window_frame_.setVisible(true);
       
       this.actual_file_name_ = "actual_file_name.txt";
       this.file_choser = new JFileChooser();
       //this.main_window_frame_.add(this.file_choser);
       
    }
    
    private void set_number_of_nodes_for_combobox(int number){
        this.number_of_nodes_int = number;
        this.num_of_nodes_string_ = new String[number];
        this.number_of_nodes_cb_.removeAllItems();
        for(int i =0; i < 26; i++){
            this.number_of_nodes_cb_.addItem(String.valueOf(i+1));
        }
    }
    
    private void create_suministry_nodes_interface(){
        System.out.println("creating suministry nodes interface");
        this.input_nodes_suministry_ = new JTextField[26];
        JPanel cells[] = new JPanel[26];
        char node;
        this.origin_nodes_panel_.removeAll();
        //this.origin_nodes_panel_.setPreferredSize(new Dimension(500,100));
        for(int i = 0; i < 26; i++){
            node = (char) ('A' + i);
            this.input_nodes_suministry_[i] = new JTextField();
            this.input_nodes_suministry_[i].setPreferredSize(new Dimension(30,30));
            cells[i] = new JPanel();
            cells[i].setPreferredSize(new Dimension(100,30));
            cells[i].setLayout(new FlowLayout());
            
            if(i < this.number_of_nodes_int){
                cells[i].add(new JLabel(Character.toString(node)));
                cells[i].add(input_nodes_suministry_[i]);
            }else{
                cells[i].add(new JLabel(""));
            }
            this.input_nodes_suministry_[i].setVisible(true);
            this.input_nodes_suministry_[i].setText("0");
            this.origin_nodes_panel_.add(cells[i]);
        }
        this.origin_nodes_panel_.revalidate();
        this.origin_nodes_panel_.repaint();
    }
    
    private void create_demand_nodes_interface(){
        System.out.println("creating demand nodes interface");
        this.input_nodes_demand_ = new JTextField[26];
        JPanel cells[] = new JPanel[26];
        char node;
        this.destiny_nodes_panel_.removeAll();
        //this.origin_nodes_panel_.setPreferredSize(new Dimension(500,100));
        for(int i = 0; i < 26; i++){
            node = (char) ('A' + i);
            this.input_nodes_demand_[i] = new JTextField();
            this.input_nodes_demand_[i].setPreferredSize(new Dimension(30,30));
            cells[i] = new JPanel();
            cells[i].setPreferredSize(new Dimension(100,30));
            cells[i].setLayout(new FlowLayout());
            
            if(i < this.number_of_nodes_int){
                cells[i].add(new JLabel(Character.toString(node)));
                cells[i].add(input_nodes_demand_[i]);
            }else{
                cells[i].add(new JLabel(""));
            }
            this.input_nodes_demand_[i].setVisible(true);
            this.input_nodes_demand_[i].setText("0");
            this.destiny_nodes_panel_.add(cells[i]);
        }
        this.destiny_nodes_panel_.revalidate();
        this.destiny_nodes_panel_.repaint();
    }
    
    private void create_cost_matrix(){
        //JComponent mycomponensts = this.main_window_frame_.getComponents();
        
        this.main_window_frame_.remove(this.cost_matrix_panel_);
        
        this.cost_matrix_panel_ = new JPanel(new GridLayout(this.number_of_nodes_int+1, this.number_of_nodes_int+1));
        this.cost_matrix_panel_.setPreferredSize(new Dimension(600,500));
        this.cost_matrix_ = new JTextField[this.number_of_nodes_int][this.number_of_nodes_int];
        char node_label;
        this.cost_matrix_panel_.add(new JLabel(""));
        for(int i = 0; i < this.number_of_nodes_int; i++){
            node_label = (char) ('A' + i);
           this.cost_matrix_panel_.add(new JLabel(Character.toString(node_label), JLabel.CENTER));
        }
        
        for(int i = 0; i < this.number_of_nodes_int; i++){
            node_label = (char) ('A' + i);
           this.cost_matrix_panel_.add(new JLabel(Character.toString(node_label)));
            for(int j = 0; j < this.number_of_nodes_int; j++){
                this.cost_matrix_[i][j] = new JTextField();
                this.cost_matrix_panel_.add(this.cost_matrix_[i][j]);
                this.cost_matrix_[i][j].setHorizontalAlignment(JTextField.CENTER);
                if(i == j )this.cost_matrix_[i][j].setText("0");
                
            }
        }
        this.main_window_frame_.add(this.cost_matrix_panel_);
    }
    
    private void create_result_area(){
       this.main_window_frame_.remove(this.result_panel_);
       this.result_panel_ = new JPanel();
       this.result_panel_.setPreferredSize(new Dimension(450,450));
       this.result_panel_.setVisible(true);
       this.main_window_frame_.add(this.result_panel_);
       
       //this.result_panel_.add(new JLabel("Result area"), BorderLayout.NORTH);
       this.result_text_area_ = new JTextArea();
       this.result_panel_.setLayout(new BorderLayout());
       this.result_text_area_.setPreferredSize(new Dimension(350,350));
       this.result_text_area_.setVisible(true);
       this.result_panel_.add(this.result_text_area_, BorderLayout.CENTER);
       this.result_panel_.revalidate();
       this.result_panel_.repaint();
    }
    
    
    ActionListener number_of_nodes_cb_listener = new ActionListener() {
        public void actionPerformed(ActionEvent ae) {
          update_number_of_nodes();
        }
    };
    
    
    
    ActionListener test = new ActionListener() {
        public void actionPerformed(ActionEvent ae) {
          write_to_file(actual_file_name_);
          ObjTransSolver_ = new Transportation_solver(actual_file_name_);
          //ObjTransSolver_ = new Transportation_solver("config_file.txt");
          ObjTransSolver_.calculate_demand_suminister();
          ObjTransSolver_.print_object_information();
          ObjTransSolver_.createLpModel();
          ObjTransSolver_.calcResult();
          print_result();
        }
    };
    
    
    private void update_number_of_nodes(){
        this.number_of_nodes_int = this.number_of_nodes_cb_.getSelectedIndex()+1;
        this.create_suministry_nodes_interface();
        this.create_demand_nodes_interface();
        create_cost_matrix();
        this.create_result_area();
    }
    
    
    public void write_to_file(String file_name){
        try{
            // Create file 
            FileWriter fstream = new FileWriter(file_name);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write("Number of nodes:\n");
            out.write(this.number_of_nodes_int+"\n");
            
            out.write("Origen:\n");
            char node_label;
            for(int i = 0; i < this.number_of_nodes_int; i++){
                node_label = node_label = (char) ('A' + i);
                if(!this.input_nodes_suministry_[i].getText().equals("") && Integer.parseInt(this.input_nodes_suministry_[i].getText()) > 0){
                    out.write(node_label);
                    out.write(",");
                    out.write(this.input_nodes_suministry_[i].getText());
                    out.write(";\n");
                }
            }
            
            out.write("Destino:\n");
            for(int i = 0; i < this.number_of_nodes_int; i++){
                node_label = (char) ('A' + i);
                
                if(!this.input_nodes_demand_[i].getText().equals("") && Integer.parseInt(this.input_nodes_demand_[i].getText()) > 0){
                    out.write(node_label);
                    out.write(",");
                    out.write(this.input_nodes_demand_[i].getText());
                    out.write(";\n");
                }
            }
            
            out.write("Costos 1:\n");
            
            for(int i = 0; i < this.number_of_nodes_int; i++){
                node_label = (char) ('A' + i);
                out.append(node_label+",");
                for(int j = 0; j < this.number_of_nodes_int; j++){
                    if(this.cost_matrix_[i][j].getText().equals("")){
                        out.write("-");
                    }else{
                        if(Integer.parseInt(this.cost_matrix_[i][j].getText()) >= 0){
                            out.write(this.cost_matrix_[i][j].getText());
                        }else{
                            out.write("-");
                        }
                    }
                    if(j != this.number_of_nodes_int-1){
                        out.write(",");
                    }else{
                        out.write(";");
                    }
                }
                if(i != this.number_of_nodes_int-1)out.write("\n");
            }
            
            //Close the output stream
            out.close();
        }catch (Exception e){//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
 
    }
    

    public void calc_result_result(){
        this.ObjTransSolver_.calcResult();
    }
    
    public void print_result(){
        /*double result_matrix[][] = this.ObjTransSolver_.getResult_matrix();
        int origin_nodes =  this.ObjTransSolver_.getOrigin_nodes_counter();
        int destiny_nodes = this.ObjTransSolver_.getDestiny_nodes_counter();
        int total_nodes = this.ObjTransSolver_.getTotal_nodes();
        char origin_node_label;
        char destiny_node_label;
        String result = "";
        
        for(int row = origin_nodes; row < total_nodes + origin_nodes; row++){
            for(int col = 0; col < total_nodes; col++){
                if(result_matrix[row][col] > 0 && (row - origin_nodes != col)){
                    origin_node_label = (char) ('A' + row - origin_nodes);
                    destiny_node_label = (char) ('A' + col);
                    result += result_matrix[row][col] + " from " + origin_node_label + " to " + destiny_node_label + "\n";
                }
            }
        }
        */
        //System.out.println(result);
        this.ObjTransSolver_.printResult();
        this.ObjTransSolver_.print_result_in_nodes();
        this.result_text_area_.setText(this.ObjTransSolver_.getResult_in_nodes_());
        
    }
    
    
    @Override
    public void run() {
        //throw new UnsupportedOperationException("Not supported yet.");
        //this.main_window_frame_.setVisible(true);
    }
    
}