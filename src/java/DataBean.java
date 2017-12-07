/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.io.*;
import javax.annotation.PreDestroy;
import javax.annotation.PostConstruct;
import org.primefaces.event.CloseEvent;
import javax.faces.event.ValueChangeEvent;

import javax.swing.JOptionPane;
import org.primefaces.context.RequestContext;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.bean.ManagedProperty;
import javax.faces.event.ActionEvent;

/**RequestContext.getCurrentInstance().execute("alert('Construct')");
 *Map<String,String> params =
                FacesContext.getExternalContext().getRequestParameterMap();
 * @author hfischer
 */

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ApplicationScoped;
import java.util.*;

@ManagedBean(name = "data")
@SessionScoped
public class DataBean implements Serializable {
    /**
     * Creates a new instance of DataBean
     */
    //@ManagedProperty("#{param.name}");
    
    private static final long serialVersionUID = 0L;
    //@ManagedProperty("#{param.Node}")
    private String Node="192.168.0.12";
    private String Port="2048";
    private String Address = "0";
    private String Timer = "0";
    private String Pir = "0";
    private String White="0";
    
    
    private String Scene0 = "#000000";
    private String Scene1 = "#000000";    
    private String Scene2 = "#000000";
    private String Scene3 = "#000000";
    private String Scene4 = "#000000";
    private String Scene5 = "#000000";
    private String Scene6 = "#000000";
    private String Scene7 = "#000000";
    private String Scene8 = "#000000";
    private String Scene9 = "#000000";
    
    private String Id     = "";
    Map<String,String> params = new HashMap();
    
    public DataBean() {
        this.Id = "databean";
    }
  
    @PostConstruct
    public void postConstruct(){
        boolean success = loadFromFile();
        if (success==false){
            showMessage("Error Reading Configuration File\n Loading Defaults");
            loadDefaults();
        }
    }
    
    public void updateValues(){
        
    }
    
    public String getNode(){
        return Node;
    }
    
    public void setNode(String node){
       Node=node;
    }
    
    public void setParams(){
        
        FacesContext fc = FacesContext.getCurrentInstance();
        params = fc.getExternalContext().getRequestParameterMap();
        String node = params.get("Node");
        if (node!= null){
            Node = node;
        }
        
        String port = params.get("Port");
        if (port!=null){
            Port = port;
        }
        String address = params.get("Address");
        if (address!=null){
            Address = address;
        }
        String timer = params.get("Timer");
        if (timer!=null){
            Timer = timer;
        }
        String pir = params.get("Pir");
        if (pir!=null){
            Pir = pir;
        }
        String white = params.get("White");
        if (white!=null){
            White = white;
        }
        
    }
    
    public String getPort(){
        return Port;
    }
    
    
    public void setPort(String port){         
        Port=port;
    }
       
    public String getAddress(){
        return Address;
    }
    
    public void setAddress(String address){
        Address = address;
    }
    
    public String getTimer(){
        return Timer;
    }
    
    public void setTimer(String timer){
        Timer = timer;
    }
    
    public String getPir(){
        return Pir;
    }
    
    public void setPir(String pir){
        Pir = pir;
    }
    
    public String getWhite(){
        return White;
    }
    
    public void setWhite(String white){
        White = white;
    }
    
    public String getScene0(){
        return  Scene0;
    }
    
    public void setScene0(String scene0){
        this.Scene0  = scene0;
    }

    public String getScene1(){
        return Scene1;
    }
    
    public void setScene1(String scene1){
        this.Scene1 = scene1;
    }
    
    public String getScene2(){
        return Scene2;
    }
    
    public void setScene2(String scene2){
        Scene2 = scene2;
    }
    public String getScene3(){
        return Scene3;
    }
    
    public void setScene3(String scene3){
        Scene3 = scene3;
    }
    
    public String getScene4(){
        return Scene4;
    }

    public void setScene4(String scene4){
        Scene4 = scene4;
    }
    
    public String getScene5(){
        return Scene5;
    }

    public void setScene5(String scene5){
        Scene5 = scene5;
    }
    
    public String getScene6(){
        return Scene6;
    }
    
    public void setScene6(String scene6){
        Scene6 = scene6;
    }
    
    public String getScene7(){
        return Scene7;
    }

    public void setScene7(String scene7){
        Scene7 = scene7;
    }
    
    public String getScene8(){
        return Scene8;
    }
    
    public void setScene8(String scene8){
        Scene8 = scene8;
    }
    
    public String getScene9(){
        return Scene9;
    }
    
    public void setScene9(String scene9){
        Scene9 = scene9;
    }
    
    public void colorParams(){
        /*
        FacesContext fc = FacesContext.getCurrentInstance();
        params = fc.getExternalContext().getRequestParameterMap();
        String str0 = params.get("X0");
        String str1 = params.get("X1");
        
        showMessage(str0 + ":colorParams:" + str1);        
        */
    }
    /*
    public void setParams(){
      
       showMessage("setParams");
       FacesContext fc = FacesContext.getCurrentInstance();
       params = fc.getExternalContext().getRequestParameterMap();
       String str = params.get("Node");
       Node = str;
       showMessage(str);
       
       str = params.get("port");
       Port = str;
       
       str = params.get("address");
       Address = str;
       
       str = params.get("timer");
       Timer = str;
       str = params.get("pir");
       Pir = str;
       str = params.get("white");
       White = str;
       showMessage(Node + Port + Timer + Pir + White);
       writeToFile();
      
    }
    */
    
    
    public void loadDefaults(){
        Node = "192.168.0.12";
        Port = "2048";
        Address="0";
        Timer= "0";
        Pir  = "0";
        White= "0";
        Scene0 = "#000000";
        Scene1 = "#000000";
        Scene2 = "#000000";
        Scene3 = "#000000";
        Scene4 = "#000000";
        Scene5 = "#000000";
        Scene6 = "#000000";
        Scene7 = "#000000";
        Scene8 = "#000000";
        Scene9 = "#000000";
    }
    
    public void changeSettings(ValueChangeEvent e){
        showMessage("changeSettings");
    }
    
    public void showMessage(String msg){
        RequestContext.getCurrentInstance().showMessageInDialog(new FacesMessage(FacesMessage.SEVERITY_INFO, "Message", msg));
    }
    
    public void reload(){
        //loadFromFile();
    }
    
    @PreDestroy
    public void destroy(CloseEvent event){
    }
    
    public void validateIp(){
        showMessage("Validate");
    }
    
    public void writeToFile(){
       Writer os = null;
        try{
           os = new FileWriter("GreenControl.cfg");
           os.write("Node:" + Node + "\n");
           os.write("Port:" + Port + "\n");
           os.write("Address:" + Address + "\n");
           os.write("Timer:" + Timer + "\n");
           os.write("Pir:" + Pir + "\n");
           os.write("White:" + White + "\n");
           os.write("Scene0:" + Scene0 + "\n");
           os.write("Scene1:" + Scene1 + "\n");
           os.write("Scene2:" + Scene2 + "\n");
           os.write("Scene3:" + Scene3 + "\n");
           os.write("Scene4:" + Scene4 + "\n");
           os.write("Scene5:" + Scene5 + "\n");
           os.write("Scene6:" + Scene6 + "\n");
           os.write("Scene7:" + Scene7 + "\n");
           os.write("Scene8:" + Scene8 + "\n");
           os.write("Scene9:" + Scene9 + "\n");
           os.flush();
        }
        catch(Exception e){
          showMessage(e.getMessage());
          
        }
        finally{
            try{
                if (os!=null) os.close();
            }
            catch (IOException ioe){
                showMessage(ioe.getMessage());
            }
        }
        
    }
    
    public boolean loadFromFile(){
        boolean success = true;
        BufferedReader buffReader = null;
        try{
            buffReader= new BufferedReader(new FileReader("GreenControl.cfg"));
            String str = buffReader.readLine();
            while(str != null){
                String[] line = str.split(":");
                if (line[0].equalsIgnoreCase("Node")) Node=line[1];
                if (line[0].equalsIgnoreCase("Port")) Port=line[1];
                if (line[0].equalsIgnoreCase("Address"))Address=line[1];
                if (line[0].equalsIgnoreCase("Pir")) Pir=line[1];
                if (line[0].equalsIgnoreCase("Timer"))Timer=line[1];
                if (line[0].equalsIgnoreCase("White"))White=line[1];
                if (line[0].equalsIgnoreCase("Scene0")) Scene0=line[1];
                if (line[0].equalsIgnoreCase("Scene1")) Scene1=line[1];
                if (line[0].equalsIgnoreCase("Scene2")) Scene2=line[1];
                if (line[0].equalsIgnoreCase("Scene3")) Scene3=line[1];
                if (line[0].equalsIgnoreCase("Scene4")) Scene4=line[1];
                if (line[0].equalsIgnoreCase("Scene5")) Scene5=line[1];
                if (line[0].equalsIgnoreCase("Scene6")) Scene6=line[1];
                if (line[0].equalsIgnoreCase("Scene7")) Scene7=line[1];
                if (line[0].equalsIgnoreCase("Scene8")) Scene8=line[1];
                if (line[0].equalsIgnoreCase("Scene9")) Scene9=line[1];
                str = buffReader.readLine();
            }
        }
        catch(IOException ioe){
            success = false;
            showMessage(ioe.getMessage());
        }
        finally{
            try{
                if (buffReader!=null) buffReader.close();
            }
            catch(IOException ioe){
                showMessage(ioe.getMessage());
            }
        }
        return success;
    }
    
}
