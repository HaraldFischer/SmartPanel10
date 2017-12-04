/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
            //Component.utils.import("java.io.*");
            //Components.utils.import("resource://gre/modules/FileUtils.jsm");

            var colorList = [];
            var color;
            var defaultColor = "#0000FF";
            
            //window.addEventListener("load",startUp,true);

            function saveData(){
               
            }            
            function startUp(){ 
                alert("StartUp");
                colorList[0] = '#00FF00';
                alert(colorList[0]);
                try{
                    for (i=0;i<10;i++){
                        str = "colorWell"+i;   
                        color = document.querySelector("#" + str);                   
                        color.value = colorList[i];
                        color.addEventListener("input", updateFirst, false);
                        color.addEventListener("change", updateAll, false);
                        color.select();
                    }
                }
                catch(ident){
                    alert(ident);
                }
                
            }

            function updateFirst(event){
                colorWell0 = (event.target.value);
            }
            
            function updateAll(event){
                
                for (var i = 0; i<10;i++){
                    color[i] = event.target.value;
                }
                /*
                document.querySelectorAll("p").forEach(function(){
                    color = event.target.value;
                });
                colorWell0 = event.target.value;
                */
            }

            function onClick(){
                //RequestContext.getCurrentInstance().showMessageInDialog(new FacesMessage(FacesMessage.SEVERITY_INFO, "What we do in life", "Echoes in eternity."));
                //var context = RequestContext.getCurrentInstance();
                //context.execute("PrimeFaces.info('Hello from the Backing Bean');");
                
            }
            
            function refresh(){
                alert("Refresh");
            }
            
            
            function check(){
                alert("Check");
            };
       
            function submit(){
                alert("Submit");
            }
            
            function initHandler(){
                var input = document.getElementById('idNode');
                alert(input.toString());
            }

