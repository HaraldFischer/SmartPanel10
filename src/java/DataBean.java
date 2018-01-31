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
import java.net.Socket;


@ManagedBean(name = "data")
@SessionScoped
public class DataBean implements Serializable {
    /**
     * Creates a new instance of DataBean
     */
    
    private static final long serialVersionUID = 0L;
    private BufferedReader      InReader = null;
    private ObjectOutputStream  OutWriter= null;
    
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
    
    private Socket ClientSocket = null;
    
    private String AbsolutePath = "";
    
    private String Id     = "";
    Map<String,String> params = new HashMap();
    
    public DataBean() {
    }
  
    @PostConstruct
    public void postConstruct(){
        //System.out.println(new File("SmartPanel10.cfg").getAbsolutePath());
        boolean success = loadFromFile();
        if (success==false){
            showMessage("Error Reading Configuration File\n Loading Defaults");
            loadDefaults();
        }
        closeSocket();
        initSocket();
    }
    
    @PreDestroy
    public void saveBean(CloseEvent event){
        writeToFile();
        closeSocket();
    }

    public void initSocket(){
        try{
            closeSocket();
            ClientSocket = new Socket(Node,Integer.parseInt(Port));
            InReader = new BufferedReader(new InputStreamReader(ClientSocket.getInputStream()));
            OutWriter= new ObjectOutputStream(ClientSocket.getOutputStream());
            ClientSocket.setSoTimeout(1000);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void closeSocket(){
        try{
            if (InReader != null){
                InReader.close();
                InReader = null;
            }
            if (OutWriter != null){
                OutWriter.close();
                OutWriter = null;
            }
            if (ClientSocket != null){ 
                ClientSocket.close();
                ClientSocket = null;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    
        
    public Socket getSocket(){
        return ClientSocket;
    }
    
    public void setSocket(Socket s){
        ClientSocket = s;
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
        closeSocket();
        initSocket();
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
        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("What", 0);
        context.addCallbackParam("Scene0",Scene0);
        return  Scene0;
    }
    
    public void setScene0(){
        String str = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("rcsetscene0");
        Scene0 = str;
    }

    public String getScene1(){
        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("What", 1);
        context.addCallbackParam("Scene1",Scene1);        
        return Scene1;
    }
    
    public void setScene1(){
        String str = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("rcsetscene1");
        Scene1 = str;
    }
    
    public String getScene2(){
        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("What", 2);
        context.addCallbackParam("Scene2",Scene2);        
        
        return Scene2;
    }
    
    public void setScene2(){
        String str = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("rcsetscene2");
        Scene2 = str;
    }
    public String getScene3(){
        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("What", 3);
        context.addCallbackParam("Scene3",Scene3);        
        
        return Scene3;
    }
    
    public void setScene3(){
        String str = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("rcsetscene3");
        Scene3 = str;
    }
    
    public String getScene4(){
        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("What", 4);
        context.addCallbackParam("Scene4",Scene4);        
        
        return Scene4;
    }

    public void setScene4(){
        String str = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("rcsetscene4");
        Scene4 = str;
    }
    
    public String getScene5(){
        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("What", 5);
        context.addCallbackParam("Scene5",Scene5);        
        
        return Scene5;
    }

    public void setScene5(){
        String str = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("rcsetscene5");
        Scene5 = str;
    }
    
    public String getScene6(){
        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("What", 6);
        context.addCallbackParam("Scene6",Scene6);        
        
        return Scene6;
    }
    
    public void setScene6(){
        String str = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("rcsetscene6");
        Scene6 = str;
    }
    
    public String getScene7(){
        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("What", 7);
        context.addCallbackParam("Scene7",Scene7);        
        
        return Scene7;
    }

    public void setScene7(){
        String str = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("rcsetscene7");
        Scene7 = str;
    }
    
    public String getScene8(){
        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("What", 8);
        context.addCallbackParam("Scene8",Scene8);        
        
        return Scene8;
    }
    
    public void setScene8(){
        String str = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("rcsetscene8");
        Scene8 = str;
    }
    
    public String getScene9(){
        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("What", 9);
        context.addCallbackParam("Scene9",Scene9);        
        
        return Scene9;
    }
    
    public void setScene9(){
        String str = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("rcsetscene9");
        Scene9 = str;
    }
    
    
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
    
    
    public void showMessage(String msg){
        RequestContext.getCurrentInstance().showMessageInDialog(new FacesMessage(FacesMessage.SEVERITY_INFO, "Message", msg));
    }
          

    public void scene0Clicked(){
        String in = null;
        try{
            String str = Scene0 + "\r\n";
            OutWriter.write(str.getBytes());
            OutWriter.flush();
            in = InReader.readLine();
            System.out.println(in);
        }
        catch(Exception e){
            e.printStackTrace();
            showMessage(e.getMessage());
        }
    }
    
    public void scene1Clicked(){
        String in = null;
        try{
            String str = Scene1 + "\r\n";
            OutWriter.write(str.getBytes());
            OutWriter.flush();
            in = InReader.readLine();
        }
        catch(Exception e){
            e.printStackTrace();
            showMessage(e.getMessage());
        }
    }
    
    public void scene2Clicked(){
        String in = null;
        try{
            String str = Scene2 + "\r\n";
            OutWriter.write(str.getBytes());
            OutWriter.flush();
            in = InReader.readLine();
        }
        catch (Exception e){
            e.printStackTrace();
            showMessage(e.getMessage());
        }
    }
    
    public void scene3Clicked(){
        String in = null;
        try{
            String str = Scene3 + "\r\n";
            OutWriter.write(str.getBytes());
            OutWriter.flush();
            in = InReader.readLine();
        }
        catch (Exception e){
            e.printStackTrace();
            showMessage(e.getMessage());
        }
    }
    
    public void scene4Clicked(){
        String in = null;
        try{
            String str = Scene4 + "\r\n";
            OutWriter.write(str.getBytes());
            OutWriter.flush();
            in = InReader.readLine();
        }
        catch (Exception e){
            e.printStackTrace();
            showMessage(e.getMessage());
        }
    }
    
    public void scene5Clicked(){
        String in = null;
        try{
            String str = Scene5 + "\r\n";
            OutWriter.write(str.getBytes());
            OutWriter.flush();
            in = InReader.readLine();
        }
        catch (Exception e){
            e.printStackTrace();
            showMessage(e.getMessage());
        }
    }
    
    public void scene6Clicked(){
        String in = null;
        try{
            String str = Scene6 + "\r\n";
            OutWriter.write(str.getBytes());
            OutWriter.flush();
            in = InReader.readLine();
        }
        catch (Exception e){
            e.printStackTrace();
            showMessage(e.getMessage());
        }
    }
    
    public void scene7Clicked(){
        String in = null;
        try{
            String str = Scene7 + "\r\n";
            OutWriter.write(str.getBytes());
            OutWriter.flush();
            in = InReader.readLine();
        }
        catch (Exception e){
            e.printStackTrace();
            showMessage(e.getMessage());
        }
    }
    
    public void scene8Clicked(){
        String in = null;
        try{
            String str = Scene8 + "\r\n";
            OutWriter.write(str.getBytes());
            OutWriter.flush();
            in = InReader.readLine();
        }
        catch (Exception e){
            e.printStackTrace();
            showMessage(e.getMessage());
        }
    }
    
    public void scene9Clicked(){
        String in = null;
        try{
            String str = Scene9 + "\r\n";
            OutWriter.write(str.getBytes());
            OutWriter.flush();
            in = InReader.readLine();
        }
        catch (Exception e){
            e.printStackTrace();
            showMessage(e.getMessage());
        }
    }
    
    public void writeToFile(){
       Writer os = null;
        try{
           os = new FileWriter("/home/hfischer/NetBeansProjects/SmartPanel10v1_7/SmartPanel10.cfg");
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
            buffReader= new BufferedReader(new FileReader("/home/hfischer/NetBeansProjects/SmartPanel10v1_7/SmartPanel10.cfg"));
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
