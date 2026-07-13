//
//  ced_rfqSettingsViewController.swift
//  VenderApp
//
//  Created by cedcoss on 10/02/21.
//  Copyright © 2021 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_rfqSettingsViewController: ced_VendorBaseClass {
    var heightToUse = CGFloat(0);
    let dropDownSectionHeight : CGFloat = 70;
    let checkboxHeight : CGFloat = 30;
    let padding : CGFloat = 5;
    
    var dropDownValuesArray = [String]();
    var dropDownInfoArray = [String:String]();
    @IBOutlet weak var saveButton: UIButton!
    @IBOutlet weak var topLabel: UILabel!
    @IBOutlet weak var rfqTable: UITableView!
    var rfqSettingData = [String:[[String:Any]]]()
    var fixedHeight : CGFloat = 70;
    var upsActive = "";
    var selectedSelectArray = [Int:[String:String]]()
    var makeFieldsArray = [Int:[String:Any]]()
    override func viewDidLoad() {
        super.viewDidLoad()
        topLabel.setThemeColor()
        topLabel.text = " RFQ Settings".localized
        self.rfqTable.delegate = self
        self.rfqTable.dataSource = self
        self.rfqTable.separatorStyle = .none
        saveButton.backgroundColor = DynamicColor.secondaryColor
        saveButton.setTitleColor(.white, for: .normal)
        saveButton.setTitle("Save".localized, for: .normal)
        saveButton.addTarget(self, action: #selector(saveRfqSetting(_:)), for: .touchUpInside)
        loadData()
        // Do any additional setup after loading the view.
    }
    
    func loadData()
    {
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
        let baseUrl = "vrfqapi/setting/index"
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        print(userData)
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        var postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey
        
        print(postString)
        sendRequest(url: baseUrl, parameters: postString);
    }
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        //print(response)
               Alert_File.removeLoadingIndicator(self)
               if let data = data{
                   if(requestUrl=="vrfqapi/setting/index")
                   {
                      print(NSString(data:data,encoding:String.Encoding.utf8.rawValue))
                       do{
                           
                           let jsonData = try JSON(data: data)
                           print(jsonData)
                           Alert_File.removeLoadingIndicator(self);
                           if jsonData["data"]["success"].stringValue == "false"{
                              // self.view.makeToast(jsonData["data"]["message"].stringValue, duration: 2.0, position: .center)
                               self.rfqTable.setEmptyMessage(jsonData["data"]["message"].stringValue)
                               return
                           }
                           
                           for(mainkey,mainval) in jsonData["data"]["fieldset"]
                           {
                               var methodArray = [[String:Any]]()
                           // for index in mainval.arrayValue{
                                   var checkArray = [String:Any]()
                            for(key,val) in mainval.dictionaryValue
                                   {
                                       checkArray[key] = val;
                                    
                                   }
                                   methodArray.append(checkArray);
                                   
                              // }
                               
                               rfqSettingData[mainkey] = methodArray;
                           }
                           
                           
                           
                           print(self.rfqSettingData);
                        self.rfqTable.dataSource = self
                           self.rfqTable.restore()
                           self.rfqTable.reloadData();
                       }catch let err{
                           print(err.localizedDescription)
                       }
                   }else{
                    do{
                        let jsonData = try JSON(data: data)
                        print(jsonData)
                        if(jsonData["data"]["success"].stringValue != "true")
                        {
                            let title = NSLocalizedString("Error".localized, comment: "");
                            let message = NSLocalizedString("Unfortunately Data Not Saved".localized, comment: "");
                            Alert_File.showValidationError(self,title:title,message:message);
                            return;
                        }
                        
                        let title = NSLocalizedString("Success".localized, comment: "");
                        let message = jsonData["data"]["message"].stringValue ?? ""
                        Alert_File.showValidationError(self,title:title,message:message);
                        return;
                        
                    }catch let arr{
                        print(arr.localizedDescription)
                    }
                }
        }
    }
}

extension ced_rfqSettingsViewController: UITableViewDataSource{
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
                let cell = rfqTable.dequeueReusableCell(withIdentifier: "longSingleCell", for: indexPath) as! LongSingleTableViewCell;
                cell.contentView.backgroundColor = UIColor.white;
                
                for views in cell.mainStack.subviews{
                    views.removeFromSuperview()
                }
                var counter = 1000;
                for(key,value) in rfqSettingData
                {
                    //*** top label **//
                    heightToUse += CGFloat(10);
                    let ts_label_view = TS_Label_View();
                    ts_label_view.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
                    //ts_label_view.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width-10,height: CGFloat(40));
                   ts_label_view.heightAnchor.constraint(equalToConstant: 40).isActive = true
                    heightToUse += CGFloat(50);
                    ts_label_view.center.x = cell.mainStack.center.x;
                   // if let keyArray = key.components(separatedBy: "#") as? [String]
                   // {
                    for val in value{
                        ts_label_view.entityNameLabel.text = val["label"] as? String
                    }
                   // }
                    
                    
                    //design code
                    let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
                    let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
                    ts_label_view.backgroundColor = color
                    ts_label_view.entityNameLabel.font=UIFont(name: "Roboto-Regular",size: 15)
                    ts_label_view.entityNameLabel.textAlignment = NSTextAlignment.center;
                    ts_label_view.entityNameLabel.textColor = UIColor.white;
                    ts_label_view.entityNameLabel.backgroundColor = color;
                    ts_label_view.layer.borderColor = color.cgColor
                    ts_label_view.layer.borderWidth = 1.0
                    ts_label_view.layer.cornerRadius = 5
                    
                    
                    heightToUse += CGFloat(5);
                    
                    cell.mainStack.addArrangedSubview(ts_label_view);
                    var x = 0;
                    var fieldToPost = ""
                    for val in value{
                        if let fieldtopost = val["field_to_post"] as? JSON{
                            fieldToPost = fieldtopost.stringValue
                        }
                        print("val shipping = \(val)")
                        if let type = val["type"] as? JSON{
                            if(type.stringValue == "select")
                            {
                                let label_DropDown_Combo_View = Label_DropDown_Combo_View();
                                label_DropDown_Combo_View.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
                                //label_DropDown_Combo_View.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width-30,height: dropDownSectionHeight);
                                label_DropDown_Combo_View.heightAnchor.constraint(equalToConstant: dropDownSectionHeight).isActive = true
                                label_DropDown_Combo_View.center.x = cell.mainStack.center.x;
                                heightToUse += dropDownSectionHeight;
                                heightToUse += padding;
                                label_DropDown_Combo_View.entityNameLabel.font=UIFont(name: "Roboto-Regular",size: 15)
                                if let label = val["label"] as? JSON{
                                    label_DropDown_Combo_View.entityNameLabel.text = label.stringValue;
                                    if label.stringValue == "Condition"{
                                     
                                     label_DropDown_Combo_View.dropDownButton.titleLabel?.font=UIFont(name: "Roboto-Regular",size: 15)
                                     //label_DropDown_Combo_View.dropDownButton.setTitle( upsActive, for: UIControlState());
                                     label_DropDown_Combo_View.dropDownButton.addTarget(self, action: #selector(showRequiredDropdown(_:)), for: UIControl.Event.touchUpInside);
                                     label_DropDown_Combo_View.dropDownButton.tag = counter;
                                    
                                     if let values = val["values"] as? JSON{
                                         var valuesArray = [String:String]();
                                         for valEach in values.arrayValue
                                         {
                                             if let value = val["value"] as? JSON{
        //                                         if(value.stringValue == valEach["value"].stringValue)
        //                                         {
        //                                             label_DropDown_Combo_View.dropDownButton.setTitle(valEach["label"].stringValue, for: .normal);
        //                                         }
                                                
                                                
                                             }
                                             valuesArray[valEach["label"]["label"].stringValue] = valEach["value"].stringValue;
                                         }
                                         selectedSelectArray[counter] = valuesArray;
                                     }
                                         }else{
                                
                                label_DropDown_Combo_View.dropDownButton.titleLabel?.font=UIFont(name: "Roboto-Regular",size: 15)
                                //label_DropDown_Combo_View.dropDownButton.setTitle( upsActive, for: UIControlState());
                                label_DropDown_Combo_View.dropDownButton.addTarget(self, action: #selector(showRequiredDropdown(_:)), for: UIControl.Event.touchUpInside);
                                label_DropDown_Combo_View.dropDownButton.tag = counter;
                               
                                if let values = val["values"] as? JSON{
                                    var valuesArray = [String:String]();
                                    for valEach in values.arrayValue
                                    {
                                        if let value = val["value"] as? JSON{
                                            if(value.stringValue == valEach["value"].stringValue)
                                            {
                                                label_DropDown_Combo_View.dropDownButton.setTitle(valEach["label"].stringValue, for: .normal);
                                            }
                                        }
                                        valuesArray[valEach["label"].stringValue] = valEach["value"].stringValue;
                                    }
                                    selectedSelectArray[counter] = valuesArray;
                                }
                                    }
                                }
                                rfqSettingData[key]![x]["field"] = label_DropDown_Combo_View.dropDownButton;
                                //design
                                label_DropDown_Combo_View.makeCard(label_DropDown_Combo_View, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
                                
                                cell.mainStack.addArrangedSubview(label_DropDown_Combo_View);
                                
                            }else if(type.stringValue == "text")
                            {
                                        let label_TextView_Combo_View = Label_TextView_Combo_View();
                                          var valuesArray = [String:String]();
                                          label_TextView_Combo_View.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
                                         // label_TextView_Combo_View.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width-20,height: fixedHeight);
                                        label_TextView_Combo_View.heightAnchor.constraint(equalToConstant: fixedHeight).isActive = true
                                          heightToUse += fixedHeight;
                                          heightToUse += padding;
                                         // label_TextView_Combo_View.center.x = cell.mainWrapperView.center.x;
                                          label_TextView_Combo_View.entityNameLabel.font=UIFont(name: "Roboto-Regular", size: 15)
                                          guard  let valLbl = val["label"] as? JSON else{return UITableViewCell()}
                                          label_TextView_Combo_View.entityNameLabel.text = valLbl.stringValue
                                        guard  let valValue = val["value"] as? JSON else{return UITableViewCell()}
                                          label_TextView_Combo_View.entityValueTxtField.text = valValue.stringValue
                                          label_TextView_Combo_View.entityValueTxtField.tag = counter;
                                          rfqSettingData[key]![x]["field"] = label_TextView_Combo_View.entityValueTxtField
                                         valuesArray[valLbl.stringValue] = valValue.stringValue;
                                          
                                          
                                          print(val["label"])
                                          print("taggg of this value \(counter)")
                                          selectedSelectArray[counter] = valuesArray
                                          label_TextView_Combo_View.makeCard(label_TextView_Combo_View, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4);
                                          
                                          cell.mainStack.addArrangedSubview(label_TextView_Combo_View);
                                 
                            }
                            
                            counter += 1;
                        }
                        x += 1;
                    }
                }
                
                return cell;
            }
    @objc func showRequiredDropdown(_ sender:UIButton)
          {
              let dropDown = DropDown();
              let tag = sender.tag;
              let dropdownArray = selectedSelectArray[tag];
              let dataSource = Array(dropdownArray!.keys);
              
              dropDown.dataSource = dataSource;
              dropDown.selectionAction = {(index, item) in
                  sender.setTitle(item, for: UIControl.State());
              }
              dropDown.anchorView = sender
              dropDown.bottomOffset = CGPoint(x: 0, y:sender.bounds.height)
              
              if dropDown.isHidden {
                  dropDown.show();
              } else {
                  dropDown.hide();
              }
          }
    @objc func saveRfqSetting(_ sender:UIButton){
        var postData = [String:Any]()
        var dt = [String:Any]()
        for(key,val) in rfqSettingData{
            var keysVal = ""
            if let keyArray = key.components(separatedBy: "-") as? [String]
            {
                keysVal = keyArray[0]
                 
            for index in val{
                if let type = index["type"] as? JSON{
                    if(type.stringValue == "select")
                    {
                        if let field = index["field"] as? UIButton{
                            if(field.currentTitle != "")
                            {
                                print(field.tag);
                                print(field.currentTitle);
                                //if let postField = index["field_to_post"] as? JSON{
                                    let datatosend = selectedSelectArray[field.tag]![field.currentTitle!]!;
                                    
                                    dt[keyArray[1]] = datatosend
                                //}
                            }
                        }
                    }
                    if(type.stringValue == "textarea"){
                        
                        if let field = index["field"] as? UITextView {
                            print("TextView text:\(field.text)")
                            print(field.tag)
                            //if let postField = index["field_to_post"] as? JSON{
                                let datatosend = field.text ?? ""
                            dt[keyArray[1]] = datatosend
                                //postData[postField.stringValue] = datatosend
                           // }
                        }
                    }
                    
                    if(type.stringValue == "text"){
                        
                        if let field = index["field"] as? UITextView {
                            print("TextView text:\(field.text)")
                            print(field.tag)
                            //if let postField = index["field_to_post"] as? JSON{
                                let datatosend = field.text ?? ""
                            dt[keyArray[1]] = datatosend
                            //    postData[postField.stringValue] = datatosend
                            //}
                        }
                    }
                }
            }
            }
            postData[keysVal] = dt
        }
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary;
        let vendor_id = userData["vendorId"] as! String;
        let hashkey    = userData["hashKey"] as! String;
        var dataToPost = [String:Any]()
        let dtc = convertAnyDicTostring(str: postData)
        print(dtc)
//        dataToPost["hashkey"] = vendor_id
//        dataToPost["hashkey"] = hashkey
//        dataToPost["groups"] = dtc
        
      //  print(dataToPost)
        let newString = "hashkey=\(hashkey)&vendor_id=\(vendor_id)&groups=\(dtc)"
        sendRequest(url: "vrfqapi/setting/save", parameters: newString)
        Alert_File.addLoadingIndicator(self, msg: NSLocalizedString("Please Wait...", comment: ""))
        //sendAnyRequest(url: "vrfqapi/setting/save", params: dataToPost, withRest: false)
        
    }
}
extension ced_rfqSettingsViewController: UITableViewDelegate{
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return UITableView.automaticDimension
    }
}
