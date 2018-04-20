/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

            var colorList = new Array();
            var color;
            
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
            
            function saveData(what){
               if (what === 0) rcsetscene0([{name:'rcsetscene0',value:colorList[0]}]);
               if (what === 1) rcsetscene1([{name:'rcsetscene1',value:colorList[1]}]);
               if (what === 2) rcsetscene2([{name:'rcsetscene2',value:colorList[2]}]);
               if (what === 3) rcsetscene3([{name:'rcsetscene3',value:colorList[3]}]);
               if (what === 4) rcsetscene4([{name:'rcsetscene4',value:colorList[4]}]);
               if (what === 5) rcsetscene5([{name:'rcsetscene5',value:colorList[5]}]);
               if (what === 6) rcsetscene6([{name:'rcsetscene6',value:colorList[6]}]);
               if (what === 7) rcsetscene7([{name:'rcsetscene7',value:colorList[7]}]);
               if (what === 8) rcsetscene8([{name:'rcsetscene8',value:colorList[8]}]);
               if (what === 9) rcsetscene9([{name:'rcsetscene9',value:colorList[9]}]);
               
            }           
            

            function startUp(){
                var count = 0;
                
                var p = new Promise(function(resolve,reject){
                    this.loadData();
                    var interval = window.setInterval(function(){
                        count++;
                        if (colorList.length >= 10){
                            clearInterval(interval);
                            resolve();
                        }
                        
                        else if (count > 200){
                            clearInterval(interval);
                            count = 0;
                            alert("ColorList Timed Out");
                            reject();
                        
                        }
                    },100);                    
                                        
                });
                
                p.then(function(){
                    try{
                        for (i = 0;i < colorList.length; i++){
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
                    
                });
                
            }

            
            function updateFirst(event){
                
                var what = event.target.getAttribute('id');
                                
                if (what === "colorWell0"){
                    colorList[0] = event.target.value;
                    saveData(0);
                }
                else if (what === "colorWell1"){
                    colorList[1] = event.target.value;
                    saveData(1);
                }
                else if (what === "colorWell2"){
                    colorList[2] = event.target.value;
                    saveData(2);
                }
                else if (what === "colorWell3"){
                    colorList[3] = event.target.value;
                    saveData(3);
                }
                else if (what === "colorWell4"){
                    colorList[4] = event.target.value;
                    saveData(4);
                }
                else if (what === "colorWell5"){
                    colorList[5] = event.target.value;
                    saveData(5);
                }
                else if (what === "colorWell6"){
                    colorList[6] = event.target.value;
                    saveData(6);
                }
                else if (what === "colorWell7"){
                    colorList[7] = event.target.value;
                    saveData(7);
                }
                else if (what === "colorWell8"){
                    colorList[8] = event.target.value;
                    saveData(8);
                }
                else if (what === "colorWell9"){
                    colorList[9] = event.target.value;
                    saveData(9);
                }
                else alert("Color Selection Failed");
                
            }
            
            function updateAll(event){
                
            }
            
            function checkWhite(str){
                var retVal = false;
                if (isNaN(str) === false){
                    var number = Number(str);
                    if (number>=0 && number <=255){
                        retVal = true;
                    }
                }
                return retVal;
            }

            function buttonClicked(event){
                var id = event.target.getAttribute('id');
                
                if (id === "idform:idbtnscene0"){
                    rcscene0clicked();
                }
                if (id === "idform:idbtnscene1"){
                    rcscene1clicked();
                }
                if (id === "idform:idbtnscene2"){
                    rcscene2clicked();
                }
                if (id === "idform:idbtnscene3"){
                    rcscene3clicked();
                }
                if (id === "idform:idbtnscene4"){
                    rcscene4clicked();
                }
                if (id === "idform:idbtnscene5"){
                    rcscene5clicked();
                }
                if (id === "idform:idbtnscene6"){
                    rcscene6clicked();
                }
                if (id === "idform:idbtnscene7"){
                    rcscene7clicked();
                }
                if (id === "idform:idbtnscene8"){
                    rcscene8clicked();
                }
                if (id === "idform:idbtnscene9"){
                    rcscene9clicked();
                }
            }
            
            function sceneComplete(xhr,status,args){
              
            }
            
            function clickBtnOn(){
                alert("On");
            }
            
            function clickBtnOff(){
                alert("Off");
            }
            
            function clickBtnUp(){
                alert("Up");
            }

            function clickBtnDown(){
                alert("Down");
            }
            
            function clickBtnSave(){
                alert("Save");
            }
            
            function clickBtnLoad(){
                alert("Load");
            }
            
            function clickBtnTimer(){
                alert("Timer");
            }
            
            function clickBtnPir(){
                alert("Pir");
            }
            
            function clickBtnWhite(){
                alert("White");
            }
            
