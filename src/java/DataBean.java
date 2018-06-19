/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import javax.annotation.PreDestroy;
import javax.annotation.PostConstruct;
import org.primefaces.context.RequestContext;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedProperty;
import java.util.*;
import java.net.Socket;
import javax.faces.event.ValueChangeEvent;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.validation.constraints.*;
 



@ManagedBean(name = "data")
@SessionScoped
public class DataBean implements Serializable {
    /**
     * Creates a new instance of DataBean
     */
    
    private static final long serialVersionUID = 0L;
    private BufferedReader      InReader = null;
    private DataOutputStream    OutWriter= null;
    
    private boolean changed = false;
    
    public void setChanged(boolean c){
        this.changed = c;
    }
    
    public boolean getChanged(){
        return this.changed;
    }
    
    public class MessageItem extends Object{
        
        private String what = "";
        private String msg  = "";
        
        public MessageItem(){
            
        }
               
        public void setWhat(String what){
            this.what = what;
        }
        
        public String getWhat(){
            return this.what;
        }
        
        public void setMsg(String msg){
            this.msg = msg;
        }
        
        public String getMsg(){
            return this.msg;
        }
    }
    
    private List<String> errMsg = new ArrayList();
    private List<MessageItem> msgList = new ArrayList();
    
    @ManagedProperty(value = "#{node}")
    @NotNull(message = "Node Must Not Be Null")
    @Pattern(regexp = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$", message = "Node Value must be a valid IP Address")
    private String node = "192.168.0.12";
    @ManagedProperty(value = "#{port}")
    @NotNull(message = "Port Must Not Be Null")
    @Pattern(regexp = "^((6553[0-5])|(655[0-2][0-9])|(65[0-4][0-9]{2})|(6[0-4][0-9]{3})|([1-5][0-9]{4})|([0-5]{0,5})|([0-9]{1,4}))$", message = "Port Value must be 0 - 65535")
    private String port="2048";
    @ManagedProperty(value = "#{address}")
    @NotNull(message = "Address Must Not Be Null")
    @Pattern(regexp = "^(([0-9][1-9])|([1-9][0-9])|[0-9])$", message = "Address Value must be 0 - 99")
    private String address = "0";
    @ManagedProperty(value = "#{timer}")
    @NotNull(message = "Timer Must Not Be Null")
    @Pattern(regexp = "^[0-9]{1,4}$", message = "Timer Value must be 0 - 9999")
    private String timer = "0";
    @ManagedProperty(value = "#{pir}")
    @NotNull(message = "Pir Must Not Be Null")
    @Pattern(regexp = "^[0-9]{1,4}$", message = "Pir Value must be 0 - 9999")
    private String pir = "0";
    @ManagedProperty(value = "#{white}")
    @NotNull(message = "White Must Not Be Null")
    @Pattern(regexp = "([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])", message = "White Value must be 0 - 255")
    private String white="0";
    
    
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
    
    Map<String,String> params = new HashMap();
    
    public DataBean() {
    }
  
    @PostConstruct
    public void postConstruct(){
        
        boolean success = loadFromFile();
        if (success==false){
            errMsg.add("Error Reading Configuration File\n Loading Defaults");
            //showMessage("Error Reading Configuration File\n Loading Defaults");
            loadDefaults();
        }

        closeSocket();
        initSocket();
        
    }
    
    @PreDestroy
    public void preDestroy(){
        writeToFile();
        closeSocket();
    }
    
    public String updateData(ValueChangeEvent event){        
        this.changed = true;
        showMessage("Changed");
        return "Message";
    }
    
    public void save(){ 
       
    }
    
    public void setMsgList(List<MessageItem> list){
        this.msgList = list;
    }
    
    public List<MessageItem> getMsgList(){
        return msgList;
    }
    
    public void addMsgItem(MessageItem item){
        msgList.add(item);
    }
    
    public void removeMsgItem(){
        if (!msgList.isEmpty()) msgList.remove(msgList.size()-1);
    }
    
    public void clearMsgList(){
        msgList.clear();
    }
    
    public void initSocket(){
        try{
            closeSocket();
            ClientSocket = new Socket(node,Integer.parseInt(port));
            InReader = new BufferedReader(new InputStreamReader(ClientSocket.getInputStream()));
            OutWriter= new DataOutputStream(ClientSocket.getOutputStream());
            ClientSocket.setSoTimeout(1000);
        }
        catch (Exception e){
            e.printStackTrace();
            errMsg.add("Init Socket Exception:" + e.getMessage() + " " + "Node:" + node + " " + "Port:" + port );
            //showMessage("Socket Exception:" + e.getMessage());
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
            //showMessage("Socket Exception:" + e.getMessage() + " " + "Node:" + node + " " + "Port:" + port );
            e.printStackTrace();
        }
    }
    
    public void clickButtonOn(){
        
        String msg = "smP" + address + "_ON;" + "\r\n";
        MessageItem request = new MessageItem();
        request.setWhat("Request");
        request.setMsg(msg);
        addMsgItem(request);
        writeOutputStream(msg);
        String str = readInputStream();
        MessageItem response = new MessageItem();
        response.setWhat("Response");
        response.setMsg(str);
        addMsgItem(response);
        
    }
    
    public void clickButtonOff(){
        
        String msg = "smP" + address + "_OF;" + "\r\n";
        MessageItem request = new MessageItem();
        request.setWhat("Request");
        request.setMsg(msg);
        addMsgItem(request);
        writeOutputStream(msg);
        String str = readInputStream();
        MessageItem response = new MessageItem();
        response.setWhat("Response");
        response.setMsg(str);
        addMsgItem(response);
        
    }
    
    public void clickButtonUp(){
        
        String msg = "smP" + address + "_UP;" + "\r\n";
        MessageItem request = new MessageItem();
        request.setWhat("Request");
        request.setMsg(msg);
        addMsgItem(request);
        writeOutputStream(msg);
        String str = readInputStream();
        MessageItem response = new MessageItem();
        response.setWhat("Response");
        response.setMsg(str);
        addMsgItem(response);
        
    }
    
    public void clickButtonDown(){
        
        String msg = "smP" + address + "_DN;" + "\r\n";
        MessageItem request = new MessageItem();
        request.setWhat("Request");
        request.setMsg(msg);
        addMsgItem(request);
        writeOutputStream(msg);
        String str = readInputStream();
        MessageItem response = new MessageItem();
        response.setWhat("Response");
        response.setMsg(str);
        addMsgItem(response);
        
    }
    
    public void clickButtonSave(){
        
    }
    
    public void clickButtonLoad(){
        
    }
    
    public void clickButtonTimer(){
        
    }
    
    public void clickButtonPir(){
        
    }
    
    public void clickButtonWhite(){
        
    }
    
    public void writeOutputStream(String msg){
        try{
            OutWriter.writeChars(msg);
        }
        catch(Exception e){
            showMessage("Write Socket Exception:" + e.getMessage());
        }
    }
    
    public String readInputStream(){
        String response = "";
        try{
            response = InReader.readLine();
        }
        catch(Exception e){
            showMessage("Read Socket Exception:" + e.getMessage());
        }
        return response;
    }
            
    public Socket getSocket(){
        return ClientSocket;
    }
    
    public void setSocket(Socket s){
        ClientSocket = s;
    }
        
    public String getNode(){
        return this.node;
    }
    
    public void setNode(String node){
       this.node=node;
    }
    
    
    public String getPort(){
        return port;
    }
    
    
    public void setPort(String port){         
        this.port=port;
    }
       
    public String getAddress(){
        return this.address;
    }
    
    public void setAddress(String address){
        this.address = address;
    }
    
    public String getTimer(){
        return this.timer;
    }
    
    public void setTimer(String timer){
        this.timer = timer;
    }
    
    public String getPir(){
        return this.pir;
    }
    
    public void setPir(String pir){
        this.pir = pir;
    }
    
    public String getWhite(){
        return this.white;
    }
    
    public void setWhite(String white){
        this.white = white;
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
        node = "192.168.0.12";
        port = "2048";
        address="0";
        timer= "0";
        pir  = "0";
        white= "0";
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
    
    public void postMessage(){
        if (errMsg.size() != 0){
            for (int i = 0; i < errMsg.size(); i++){
                showMessage(errMsg.get(i));
            }
            errMsg.clear();
        }
    }
    
    public void showMessage(String msg){
            
         FacesContext.getCurrentInstance().addMessage("idmessages", new FacesMessage(FacesMessage.SEVERITY_INFO, "Message",  msg));
    }
          

    public void scene0Clicked(){
        String in = null;
        try{
            String str = Scene0 + "\r\n";
            OutWriter.writeChars(str);
            OutWriter.flush();
            in = InReader.readLine();
        }
        catch(Exception e){
            e.printStackTrace();
            showMessage("Socket Exception:" + e.getMessage());
        }
    }
    
    public void scene1Clicked(){
        String in = null;
        try{
            String str = Scene1 + "\r\n";
            OutWriter.writeChars(str);
            OutWriter.flush();
            in = InReader.readLine();
        }
        catch(Exception e){
            e.printStackTrace();
            showMessage("Socket Exception:" + e.getMessage());
        }
    }
    
    public void scene2Clicked(){
        String in = null;
        try{
            String str = Scene2 + "\r\n";
            OutWriter.writeChars(str);
            OutWriter.flush();
            in = InReader.readLine();
        }
        catch (Exception e){
            e.printStackTrace();
            showMessage("Socket Exception:" + e.getMessage());
        }
    }
    
    public void scene3Clicked(){
        String in = null;
        try{
            String str = Scene3 + "\r\n";
            OutWriter.writeChars(str);
            OutWriter.flush();
            in = InReader.readLine();
        }
        catch (Exception e){
            e.printStackTrace();
            showMessage("Socket Exception:" + e.getMessage());
        }
    }
    
    public void scene4Clicked(){
        String in = null;
        try{
            String str = Scene4 + "\r\n";
            OutWriter.writeChars(str);
            OutWriter.flush();
            in = InReader.readLine();
        }
        catch (Exception e){
            e.printStackTrace();
            showMessage("Socket Exception:" + e.getMessage());
        }
    }
    
    public void scene5Clicked(){
        String in = null;
        try{
            String str = Scene5 + "\r\n";
            OutWriter.writeChars(str);
            OutWriter.flush();
            in = InReader.readLine();
        }
        catch (Exception e){
            e.printStackTrace();
            showMessage("Socket Exception:" + e.getMessage());
        }
    }
    
    public void scene6Clicked(){
        String in = null;
        try{
            String str = Scene6 + "\r\n";
            OutWriter.writeChars(str);
            OutWriter.flush();
            in = InReader.readLine();
        }
        catch (Exception e){
            e.printStackTrace();
            showMessage("Socket Exception:" + e.getMessage());
        }
    }
    
    public void scene7Clicked(){
        String in = null;
        try{
            String str = Scene7 + "\r\n";
            OutWriter.writeChars(str);
            OutWriter.flush();
            in = InReader.readLine();
        }
        catch (Exception e){
            e.printStackTrace();
            showMessage("Socket Exception:" + e.getMessage());
        }
    }
    
    public void scene8Clicked(){
        String in = null;
        try{
            String str = Scene8 + "\r\n";
            OutWriter.writeChars(str);
            OutWriter.flush();
            in = InReader.readLine();
        }
        catch (Exception e){
            e.printStackTrace();
            showMessage("Socket Exception:" + e.getMessage());
        }
    }
    
    public void scene9Clicked(){
        String in = null;
        try{
            String str = Scene9 + "\r\n";
            OutWriter.writeChars(str);
            OutWriter.flush();
            in = InReader.readLine();
        }
        catch (Exception e){
            e.printStackTrace();
            showMessage("Socket Exception:" + e.getMessage());
        }
    }
    
    public void writeToFile(){
       Writer os = null;
        try{
           os = new FileWriter("SmartPanel10.cfg");
           os.write("Node:" + node + "\n");
           os.write("Port:" + port + "\n");
           os.write("Address:" + address + "\n");
           os.write("Timer:" + timer + "\n");
           os.write("Pir:" + pir + "\n");
           os.write("White:" + white + "\n");
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
          e.printStackTrace();
          showMessage(e.getMessage());
          
        }
        finally{
            try{
                if (os!=null) os.close();
            }
            catch (IOException ioe){
                ioe.printStackTrace();
                showMessage(ioe.getMessage());
            }
        }
        
    }
    
    public boolean loadFromFile(){
        boolean success = true;
        BufferedReader buffReader = null;
        try{
            buffReader= new BufferedReader(new FileReader("SmartPanel10.cfg"));
            String str = buffReader.readLine();
            while(str != null){
                String[] line = str.split(":");
                if (line[0].equalsIgnoreCase("Node")) node=line[1];
                else if (line[0].equalsIgnoreCase("Port")) port=line[1];
                else if (line[0].equalsIgnoreCase("Address"))address=line[1];
                else if (line[0].equalsIgnoreCase("Pir")) pir=line[1];
                else if (line[0].equalsIgnoreCase("Timer"))timer=line[1];
                else if (line[0].equalsIgnoreCase("White"))white=line[1];
                else if (line[0].equalsIgnoreCase("Scene0")) Scene0=line[1];
                else if (line[0].equalsIgnoreCase("Scene1")) Scene1=line[1];
                else if (line[0].equalsIgnoreCase("Scene2")) Scene2=line[1];
                else if (line[0].equalsIgnoreCase("Scene3")) Scene3=line[1];
                else if (line[0].equalsIgnoreCase("Scene4")) Scene4=line[1];
                else if (line[0].equalsIgnoreCase("Scene5")) Scene5=line[1];
                else if (line[0].equalsIgnoreCase("Scene6")) Scene6=line[1];
                else if (line[0].equalsIgnoreCase("Scene7")) Scene7=line[1];
                else if (line[0].equalsIgnoreCase("Scene8")) Scene8=line[1];
                else if (line[0].equalsIgnoreCase("Scene9")) Scene9=line[1];
                else;
                str = buffReader.readLine();
            }

        }
        catch(IOException ioe){
            success = false;
            ioe.printStackTrace();
            showMessage(ioe.getMessage());
        }
        finally{
            try{
                if (buffReader!=null) buffReader.close();
            }
            catch(IOException ioe){
                ioe.printStackTrace();
                showMessage(ioe.getMessage());
            }
        }
        return success;
    }
    
}
