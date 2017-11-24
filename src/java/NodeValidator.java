/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
/**
 *
 * @author hfischer
 */
@ManagedBean

public class NodeValidator {
    
    //private Pattern pattern;
  
    //private static final String NODE_PATTERN = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$";
  
    /**
     * Creates a new instance of NodeValidator
     */
    
 
    public NodeValidator() {
        //pattern = Pattern.compile(NODE_PATTERN);
    }
    public void save() {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage("Welcome"));
    }   
 }
