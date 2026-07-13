//
//  AddTableRate.swift
//  VenderApp
//
//  Created by cedcoss on 11/02/19.
//  Copyright © 2019 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class AddTableRate: ced_VendorBaseClass {

    @IBOutlet weak var deleteButton: UIButton!
    
    @IBOutlet weak var saveButton: UIButton!
    var id = "";
    @IBOutlet weak var mainStackView: UIStackView!
    @IBOutlet weak var mainWidth: NSLayoutConstraint!
    @IBOutlet weak var mainHeight: NSLayoutConstraint!
    var tags = 1000;
    var fieldsArray = [[String:Any]]()
    var dropDownArray = [Int: Dictionary<String,String>]()
    
    override func viewDidLoad() {
        super.viewDidLoad();
        mainHeight.constant = 0;
        mainWidth.constant = self.view.frame.width;
        deleteButton.addTarget(self, action: #selector(deleteClicked(_:)), for: .touchUpInside);
        saveButton.addTarget(self, action: #selector(saveData(_:)), for: .touchUpInside);
        loadData();

        // Do any additional setup after loading the view.
    }
    
    @objc func deleteClicked(_ sender: UIButton)
    {
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        
        //baseURL = Ced_CommonVendor.getInfoPlist("baseUrl") as! String
        
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        //var request = URLRequest(url: URL(string: baseURL)!);
        
        //request.httpMethod = "POST";
        var postString = "vendor_id="+vendorId+"&hashkey="+hashKey;
        if(id != "")
        {
            postString += "&id="+id;
        }
        self.sendRequest(url: "vendorapi/tablerate/delete", parameters: postString)
    }
    
    @objc func loadData()
    {
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        
        //baseURL = Ced_CommonVendor.getInfoPlist("baseUrl") as! String
        
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        //var request = URLRequest(url: URL(string: baseURL)!);
        
        //request.httpMethod = "POST";
        var postString = "vendor_id="+vendorId+"&hashkey="+hashKey;
        if(id != "")
        {
            postString += "&id="+id;
        }
        sendRequest(url: "vendorapi/tablerate/viewRate", parameters: postString)
    }
    
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        if let data = data{
            guard let json = try? JSON(data: data) else {return}
            print(json)
            if(requestUrl == "vendorapi/tablerate/viewRate")
            {
                for index in json["data"]["TABLERATE"].arrayValue
                {
                    switch(index["type"].stringValue)
                    {
                        case "select":
                            makeDropDown(set: index["label"].stringValue, textFeildText: index["value"].stringValue, index: tags, options: index["values"], fieldIndex: index)
                            tags += 1;
                            break;
                        case "text":
                            makeTextFeild(set: index["label"].stringValue, textFeildText: index["value"].stringValue, index: tags, type: index["input"].stringValue, fieldIndex: index);
                            tags += 1;
                            break;
                        default:
                            print("sdfsdfsdf")
                        
                    }
                }
            }
            else if(requestUrl == "vendorapi/tablerate/save")
            {
                if(json["data"]["success"].stringValue == "true")
                {
                    self.view.makeToast(json["data"]["message"].stringValue, duration: 2.0, position: .center);
                    
                }
                else
                {
                    self.view.makeToast(json["data"]["message"].stringValue, duration: 2.0, position: .center);
                }
            }
            else
            {
                if(json["data"]["success"].stringValue == "true")
                {
                    self.view.makeToast(json["data"]["message"].stringValue, duration: 2.0, position: .center);
                    Ced_CommonVendor.delay(2.0) {
                        self.navigationController?.popToRootViewController(animated: true);
                    }
                    
                }
                else
                {
                    self.view.makeToast(json["data"]["message"].stringValue, duration: 2.0, position: .center);
                }
            }
        }
    }
    
    @objc func showDropDown(_ sender:UIButton){
        let tag = sender.tag
        let dropDown = DropDown();
        let datasource = dropDownArray[tag];
        dropDown.dataSource = Array((datasource?.keys)!);
        dropDown.selectionAction = {(index, item) in
            sender.setTitle(item, for: UIControl.State());
        }
        dropDown.anchorView = sender
        dropDown.bottomOffset = CGPoint(x: 0, y:sender.bounds.height)
        
        if dropDown.isHidden {
            _ = dropDown.show()
        } else {
            dropDown.hide();
        }
        
        
    }
    
    func makeDropDown(set labelText:String , textFeildText:String,index IndexWithWholeData :Int, options: JSON,fieldIndex: JSON){
        let viewForTextFeild = Label_DropDown_Combo_View()
        viewForTextFeild.entityNameLabel.text = labelText
        viewForTextFeild.dropDownButton.setTitle(textFeildText, for: .normal)
        viewForTextFeild.dropDownButton.tag = IndexWithWholeData
        viewForTextFeild.dropDownButton.addTarget(self, action: #selector(showDropDown(_:)), for: UIControl.Event.touchUpInside);
        var dict = Dictionary<String,String>();
        for index in options.arrayValue{
            dict[index["label"].stringValue] = index["value"].stringValue
        }
        self.dropDownArray[IndexWithWholeData] = dict;
        fieldsArray.append(["type":fieldIndex["type"].stringValue,"field":viewForTextFeild.dropDownButton,"tag":fieldIndex["tag"].stringValue,"required":fieldIndex["required"].stringValue,"options":fieldIndex["values"],"label":fieldIndex["label"].stringValue]);
        self.mainStackView.addArrangedSubview(viewForTextFeild)
        viewForTextFeild.tag = IndexWithWholeData
        mainHeight.constant = mainHeight.constant + 90
        viewForTextFeild.translatesAutoresizingMaskIntoConstraints = false
        let heightConstrain = NSLayoutConstraint(item: viewForTextFeild, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .height, multiplier: 1.0, constant: 75)
        viewForTextFeild.addConstraint(heightConstrain)
    }
    
    func maketextView(set labelText:String , textFeildText:String,index IndexWithWholeData :Int,fieldIndex: JSON){
        let viewForTextFeild=Label_TextView_Combo_View()
        viewForTextFeild.entityNameLabel.text = labelText
        viewForTextFeild.entityValueTxtField.text = textFeildText;
        mainHeight.constant = mainHeight.constant + 140
        mainStackView.addArrangedSubview(viewForTextFeild)
        if(fieldIndex["input_type"].stringValue == "int")
        {
            viewForTextFeild.entityValueTxtField.keyboardType = .numberPad;
        }
        else if(fieldIndex["input_type"].stringValue == "decimal")
        {
            viewForTextFeild.entityValueTxtField.keyboardType = .decimalPad;
        }
        
        //fieldsArray[IndexWithWholeData] = ["field":viewForTextFeild.entityValueTxtField,"fieldIndex":fieldIndex];
        viewForTextFeild.translatesAutoresizingMaskIntoConstraints = false
        let heightConstrain = NSLayoutConstraint(item: viewForTextFeild, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .height, multiplier: 1.0, constant: 120)
        viewForTextFeild.addConstraint(heightConstrain)
        print("---height---")
        print(mainHeight.constant);
        viewForTextFeild.tag = IndexWithWholeData
    }
    
    
    
    func makeTextFeild(set labelText:String , textFeildText:String,index IndexWithWholeData :Int, type: String,fieldIndex: JSON){
        let viewForTextFeild = Label_TextFieldComboView()
        //        viewForTextFeild.backgroundColor = .red
        viewForTextFeild.headingLabel.text = labelText
        viewForTextFeild.textLabel.text = textFeildText
        viewForTextFeild.textLabel.isEnabled = true
        mainHeight.constant = mainHeight.constant + 90
        mainStackView.addArrangedSubview(viewForTextFeild)
        
        
        if(fieldIndex["input_type"].stringValue == "int")
        {
            viewForTextFeild.textLabel.keyboardType = .numberPad;
        }
        else if(fieldIndex["input_type"].stringValue == "decimal")
        {
            viewForTextFeild.textLabel.keyboardType = .decimalPad;
        }
        fieldsArray.append(["type":fieldIndex["type"].stringValue,"field":viewForTextFeild.textLabel,"required":fieldIndex["required"].stringValue,"tag":fieldIndex["tag"].stringValue,"label":fieldIndex["label"].stringValue]);
        viewForTextFeild.translatesAutoresizingMaskIntoConstraints = false
        let heightConstrain = NSLayoutConstraint(item: viewForTextFeild, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .height, multiplier: 1.0, constant: 75)
        viewForTextFeild.addConstraint(heightConstrain)
        viewForTextFeild.tag = IndexWithWholeData
    }
    
    @objc func saveData(_ sender: UIButton)
    {
        var postString = "";
        for index in fieldsArray
        {
            if let type = index["type"] as? String{
                switch(type){
                    case "text":
                        if let required = index["required"] as? String
                        {
                            if let field = index["field"] as? UITextField
                            {
                                if(required == "true")
                                {
                                    if(field.text == "")
                                    {
                                        if let label = index["label"] as? String
                                        {
                                            self.view.makeToast("\(label) cannot be blank", duration: 2.0, position: .center)
                                            return;
                                        }
                                        
                                    }
                                }
                                if let tag = index["tag"] as? String{
                                    postString += "{\"\(tag)\":\"\(field.text!)\"},";
                                }
                            }
                            
                        }
                        break;
                    case "select":
                        if let required = index["required"] as? String
                        {
                            if let field = index["field"] as? UIButton
                            {
                                if(required == "true")
                                {
                                    if(field.currentTitle == "")
                                    {
                                        if let label = index["label"] as? String
                                        {
                                            self.view.makeToast("\(label) cannot be blank", duration: 2.0, position: .center)
                                            return;
                                        }
                                        
                                    }
                                    
                                }
                                if let options = index["options"] as? JSON{
                                    for optionArray in options.arrayValue
                                    {
                                        if(optionArray["label"].stringValue == field.currentTitle)
                                        {
                                            if let tag = index["tag"] as? String{
                                                postString += "{\"\(tag)\":\"\(optionArray["value"].stringValue)\"},";
                                            }
                                            
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    
                    default:
                        print("sdfdsfds")
                }
            }
        }
        var params = "";
        if(postString != "")
        {
            postString = postString.substring(to: postString.characters.index(before: postString.endIndex));
            postString = "[\(postString)]"
            postString = postString.addingPercentEncoding(withAllowedCharacters: .urlHostAllowed)!;
            params = "shipping_settings_data=\(postString)&";
            
        }
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        
        //baseURL = Ced_CommonVendor.getInfoPlist("baseUrl") as! String
        
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        //var request = URLRequest(url: URL(string: baseURL)!);
        
        //request.httpMethod = "POST";
        params += "vendor_id="+vendorId+"&hashkey="+hashKey;
        if(id != "")
        {
            params += "&id="+id;
        }
        self.sendRequest(url: "vendorapi/tablerate/save", parameters: params);
        
        
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
