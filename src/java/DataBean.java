/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.sun.media.jfxmedia.logging.Logger;
import java.io.*;
import javax.annotation.PreDestroy;
import javax.annotation.PostConstruct;
import org.primefaces.context.RequestContext;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedProperty;
import java.util.*;
import java.net.Socket;
import java.nio.charset.Charset;
import javax.faces.event.ValueChangeEvent;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.validation.constraints.*;
import javax.faces.context.ExternalContext;
import javax.servlet.ServletContext; 
import org.springframework.context.annotation.Bean;
import org.primefaces.event.SlideEndEvent;
import javax.faces.event.ActionEvent;

@ManagedBean(name = "data")
@ApplicationScoped
public class DataBean {
    /**
     * Creates a new instance of DataBean
     */
    
    private static final long serialVersionUID = 1L;
    private BufferedReader      InReader = null;
    private DataOutputStream    OutWriter= null;
    public boolean              isDirty  = false;
    public String               slider   = "0";
    
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
    public void initDataBean(){
        
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
    public void closeDataBean(){
        writeToFile();
        closeSocket();
    }
        
    public void reConnect(){ 
        errMsg.add("Re-Connect");
        closeSocket();
        initSocket();
        postMessage();
    }
    
    public void save(){ 
       //isDirty = true;
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
            errMsg.add("Close Socket Exception:" + e.getMessage() + node + " " + port);
            //showMessage("Socket Exception:" + e.getMessage() + " " + "Node:" + node + " " + "Port:" + port );
            e.printStackTrace();
        }
    }
    
    public void setSlider(String sliderpos){
        slider = sliderpos;
    }
    
    public String getSlider(){
        return slider;
    }
    
    public void onMasterRatio(){
        String value = slider;
        
    }
    
    public void onInputChanged(ValueChangeEvent event) {
        if (event != null){
            slider = String.valueOf(event.getNewValue());            
        }
    }
    
    public void onSlideEnd(SlideEndEvent event){
        
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
    
    public void clickButtonSave(int i){
        String msg = "smP" + address + "_SS" + "0" + i + ";\r\n";
        MessageItem request = new MessageItem();
        request.setWhat("Request");
        request.setMsg(msg);
        addMsgItem(request);
        writeOutputStream(msg);
        String in = readInputStream();
        MessageItem response = new MessageItem();
        response.setWhat("Response");
        response.setMsg(in);
        addMsgItem(response);
    }
    
    public void clickButtonLoad(int i){
        String msg = "smP" + address + "_SL" + "0" + i + ";\r\n";
        MessageItem request = new MessageItem();
        request.setWhat("Request");
        request.setMsg(msg);
        addMsgItem(request);
        writeOutputStream(msg);
        String in = readInputStream();
        MessageItem response = new MessageItem();
        response.setWhat("Response");
        response.setMsg(in);
        addMsgItem(response);
    }
    
    public void clickButtonTimer(){
        String val = String.format("%04d", Integer.parseInt(timer));
        String str = "smP" + address + "_TI" + val + ";\r\n";
        MessageItem request = new MessageItem();
        request.setWhat("Request");
        request.setMsg(str);
        addMsgItem(request);
        
        writeOutputStream(str);
        String in = readInputStream();
        MessageItem response = new MessageItem();
        response.setWhat("Response");
        response.setMsg(in);
        addMsgItem(response);
    }
    
    public void clickButtonPir(){
        String val = String.format("%04d",Integer.parseInt(pir));
        String str = "smP" + address + "_PI" + val + ";\r\n";
        MessageItem request = new MessageItem();
        request.setWhat("Request");
        request.setMsg(str);
        addMsgItem(request);
        
        writeOutputStream(str);
        String in = readInputStream();
        MessageItem response = new MessageItem();
        response.setWhat("Response");
        response.setMsg(in);
        addMsgItem(response);
    }
    
    public void clickButtonWhite(){
        String val = Integer.toHexString(Integer.parseInt(white));
        String str = "smP" + address + "_CW" + val + ";\r\n";
        MessageItem request = new MessageItem();
        request.setWhat("Request");
        request.setMsg(str);
        addMsgItem(request);
        
        writeOutputStream(str);
        String in = readInputStream();
        MessageItem response = new MessageItem();
        response.setWhat("Response");
        response.setMsg(in);
        addMsgItem(response);
    }
    
    public void writeOutputStream(String msg){
        
        try{
            String str = new String(msg.getBytes(),Charset.forName("US-ASCII"));
            OutWriter.write(str.getBytes(Charset.forName("US-ASCII")));
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
        slider="0";
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
          
    public String getRed(String scene){
        String var = scene.substring(1,3);
        return var;
    }
    
    public String getGreen(String scene){
        String var = scene.substring(3,5);
        return var;
    }
    
    public String getBlue(String scene){
        String var = scene.substring(5,7);
        return var;
    }
    
    public void sceneClicked(int scene,String what){
        String val = null;
        if (scene == 0){
            val = String.format("%02d", 0);
        }
        else if (scene == 1){
            val = String.format("%02d", 1);
        }
        else if (scene == 2){
            val = String.format("%02d", 2);
        }
        else if (scene == 3){
            val = String.format("%02d", 3);
        }
        else if (scene == 4){
            val = String.format("%02d", 4);
        }
        else if (scene == 5){
            val = String.format("%02d", 5);
        }
        else if (scene == 6){
            val = String.format("%02d", 6);
        }
        else if (scene == 7){
            val = String.format("%02d", 7);
        }
        else if (scene == 8){
            val = String.format("%02d", 8);
        }
        else if (scene == 9){
            val = String.format("%02d", 9);
        }
        
        if (what.equals("Load")){
            String str = "smP" + address + "_SL" + val + ";\r\n";
            MessageItem request = new MessageItem();
            request.setWhat("Request");
            request.setMsg(str);
            addMsgItem(request);
            writeOutputStream(str);

            String in = readInputStream();
            MessageItem response = new MessageItem();
            response.setWhat("Response");
            response.setMsg(in);
            addMsgItem(response);
        }
        else if (what.equals("Save")){
            String str  = "smP" + address + "_SS" + val + ";\r\n";
            MessageItem request = new MessageItem();
            request.what = "Request";
            request.setMsg(str);
            addMsgItem(request);
            writeOutputStream(str);
            
            String in = readInputStream();
            MessageItem response = new MessageItem();
            response.what = "Response";
            response.setMsg(in);
            addMsgItem(response);
        }
    }
    
    
    
    public void uploadClicked(String scene){
        String str = null;
        String in  = null;
        
        String colorRed     = getRed(scene);
        String colorGreen   = getGreen(scene);
        String colorBlue    = getBlue(scene);
        
        str = "smP" + address + "_CR" + colorRed + ";\r\n";
        MessageItem request = new MessageItem();
        request.setWhat("Request");
        request.setMsg(str);
        addMsgItem(request);
        
        writeOutputStream(str);
        in = readInputStream();
        MessageItem response = new MessageItem();
        response.setWhat("Response");
        response.setMsg(in);
        addMsgItem(response);
        
        str = "smP" + address + "_CG" + colorGreen + ";\r\n";
        request = new MessageItem();
        request.setWhat("Request");
        request.setMsg(str);
        addMsgItem(request);
        
        writeOutputStream(str);
        in = readInputStream();
        response = new MessageItem();
        response.setWhat("Response");
        response.setMsg(in);
        addMsgItem(response);
        
        str = "smP" + address + "_CB" + colorBlue + ";\r\n";
        request = new MessageItem();
        request.setWhat("Request");
        request.setMsg(str);
        addMsgItem(request);
        
        writeOutputStream(str);
        in = readInputStream();
        response = new MessageItem();
        response.setWhat("Response");
        response.setMsg(in);
        addMsgItem(response);
    }
    
    public void upload0Clicked(){
        uploadClicked(Scene0);
    }
    
    public void upload1Clicked(){
        uploadClicked(Scene1);
    }
    
    public void upload2Clicked(){
        uploadClicked(Scene2);
    }
    
    public void upload3Clicked(){
        uploadClicked(Scene3);
    }
    
    public void upload4Clicked(){
        uploadClicked(Scene4);
    }
    
    public void upload5Clicked(){
        uploadClicked(Scene5);
    }
    
    public void upload6Clicked(){
        uploadClicked(Scene6);
    }
    
    public void upload7Clicked(){
        uploadClicked(Scene7);
    }
    
    public void upload8Clicked(){
        uploadClicked(Scene8);
    }
    
    public void upload9Clicked(){
        uploadClicked(Scene9);
    }
    
    
    public void writeToFile(){
        OutputStream os = null;
        
        try{
           Properties properties = new Properties();
           properties.setProperty("Node", node);
           properties.setProperty("Port", port);
           properties.setProperty("Address", address);
           properties.setProperty("Timer", timer);
           properties.setProperty("Pir", pir);
           properties.setProperty("White", white);
           properties.setProperty("Master", slider);
           properties.setProperty("Scene0", Scene0);
           properties.setProperty("Scene1", Scene1);
           properties.setProperty("Scene2", Scene2);
           properties.setProperty("Scene3", Scene3);
           properties.setProperty("Scene4", Scene4);
           properties.setProperty("Scene5", Scene5);
           properties.setProperty("Scene6", Scene6);
           properties.setProperty("Scene7", Scene7);
           properties.setProperty("Scene8", Scene8);
           properties.setProperty("Scene9", Scene9);
           ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
           os = new FileOutputStream(context.getRealPath("/WEB-INF/SmartPanel10.cfg"));         
           properties.store(os, "SmartPanel10 Data");
           
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
           ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
           Properties properties = new Properties();
           properties.load(externalContext.getResourceAsStream("/WEB-INF/SmartPanel10.cfg"));
           node = properties.getProperty("Node", "192.168.0.11");
           port = properties.getProperty("Port", "2048");
           address = properties.getProperty("Address", "0");
           pir = properties.getProperty("Pir", "0");
           timer = properties.getProperty("Timer", "0");
           white = properties.getProperty("White", "0");
           slider= properties.getProperty("Master","0");
           Scene0= properties.getProperty("Scene0", "000000");
           Scene1= properties.getProperty("Scene1", "000000");
           Scene2= properties.getProperty("Scene2", "000000");
           Scene3= properties.getProperty("Scene3", "000000");
           Scene4= properties.getProperty("Scene4", "000000");
           Scene5= properties.getProperty("Scene5", "000000");
           Scene6= properties.getProperty("Scene6", "000000");
           Scene7= properties.getProperty("Scene7", "000000");
           Scene8= properties.getProperty("Scene8", "000000");
           Scene9= properties.getProperty("Scene9", "000000");           
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
