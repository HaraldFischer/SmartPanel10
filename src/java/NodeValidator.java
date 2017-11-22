/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.util.Map;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.primefaces.validate.ClientValidator;
/**
 *
 * @author hfischer
 */
//@ManagedBean
//@RequestScoped
@FacesValidator("custom.nodeValidator")
public class NodeValidator implements Validator, ClientValidator {
    
    private Pattern pattern;
  
    private static final String NODE_PATTERN = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$";
  
    /**
     * Creates a new instance of NodeValidator
     */
    public NodeValidator() {
        pattern = Pattern.compile(NODE_PATTERN);
    }
    
    public boolean test(String str){
        return true;
    }
    
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if(value == null) {
            return;
        }
         
        if(!pattern.matcher(value.toString()).matches()) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Validation Error", 
                        value + " is not a valid node"));
        }
    }    
    
    @Override
    public String getValidatorId() {
        return "custom.nodeValidator";
    }
    
    @Override
    public Map<String, Object> getMetadata() {
        return null;
    }
}
