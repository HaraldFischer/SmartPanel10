/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author hfischer
 */
 
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
 
@ManagedBean
public class ImagesView {
     
    private List<String> images;
     
    @PostConstruct
    public void init() {
        images = new ArrayList<String>();
        images.add("colorwheel.png");
        images.add("rgbcircle.png");
        images.add("rgblight.jpeg");
        images.add("rainbowrose.jpeg");
        images.add("rgbled.jpeg");
        images.add("rgbkeyboard.jpeg");
        images.add("castle.jpeg");
    }
 
    public List<String> getImages() {
        return images;
    }
}