/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

            var colorList = new Array();
            var color;
            
            function saveData(){
               
            }           
            
            function handleComplete(xhr,status,args){
                if (args.What === 0){
                    colorList[0] = args.Scene0;
                }
                if (args.What === 1){
                    colorList[1] = args.Scene1;
                }
                if (args.What === 2){
                    colorList[2] = args.Scene2;
                }
                if (args.What === 3){
                    colorList[3] = args.Scene3;
                }
                if (args.What === 4){
                    colorList[4] = args.Scene4;
                }
                if (args.What === 5){
                    colorList[5] = args.Scene5;
                }
                if (args.What === 6){
                    colorList[6] = args.Scene6;
                }
                if (args.What === 7){
                    colorList[7] = args.Scene7;
                }
                if (args.What === 8){
                    colorList[8] = args.Scene8;
                }
                if (args.What === 9){
                    colorList[9] = args.Scene9;
                }
                
            }
            
            function loadData(){
                for (var i = 0;i < 10;i++){
                    var str = "rcgetscene" + i + "()";
                    eval(str);
                }
            }
            
            function startUp(){
                loadData();
                
                var checkExist = setInterval(function() {
                   if (colorList.length>0) {
                      alert("Exists!");
                      clearInterval(checkExist);
                   }
                }, 100);
                
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
                //alert("UpdateFirst:" + colorList[0]);
                
                //colorWell0 = (event.target.value);
            }
            
            function updateAll(event){
                //alert("UpdateAll:" + colorList[0]);
                
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

