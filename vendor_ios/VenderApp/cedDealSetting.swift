//
//  cedDealSetting.swift
//  VenderApp
//
//  Created by cedcoss on 04/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class cedDealSetting: ced_VendorBaseClass {
    
    @IBOutlet weak var dealStatusButton: UIButton!
    @IBOutlet weak var headingView: UIView!
    
    @IBOutlet weak var messageView: UIView!
    @IBOutlet weak var headingLbl: UILabel!
    
    @IBOutlet weak var msgLbl: UILabel!
    @IBOutlet weak var dealMessageLabel: UITextField!
    
    @IBOutlet weak var submitButton: UIButton!
    
    var settingData = [String:String]();
    
    var statusArray = ["Enable".localized,"Disable".localized];
    
    override func viewDidLoad() {
        super.viewDidLoad()
        headingLbl.text = "DEAL SETTING".localized
        msgLbl.text = "Deal Message".localized
        submitButton.setTitle("submittext".localized, for: .normal)
        headingView.setThemeColor()
        submitButton.setThemeColor()
        submitButton.addTarget(self, action: #selector(submitButtonClicked(_:)), for: .touchUpInside)
        dealStatusButton.addTarget(self, action: #selector(dealStatusClicked(_:)), for: .touchUpInside)
        //dealStatusButton.setTitle(statusArray[0], for: .normal);
        loadData()
        // Do any additional setup after loading the view.
    }

    @objc func loadData()
    {
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        let postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey;
        self.sendRequest(url: "vdealapi/setting/view", parameters: postString)
    }
    
    @objc func dealStatusClicked(_ sender: UIButton)
    {
        let dropDown = DropDown();
        
        dropDown.dataSource = statusArray;
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
    
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        Alert_File.removeLoadingIndicator(self);
        if let data = data{
            guard let json = try? JSON(data: data) else {return}
            print(json)
            if(requestUrl == "vdealapi/setting/save")
            {
                if(json["data"]["success"].stringValue == "true")
                {
                    Alert_File.showValidationError(self,title:"Success".localized,message:json["data"]["message"].stringValue);
                    
                    
                }
                else
                {
                    Alert_File.showValidationError(self,title:"Error".localized,message:json["data"]["message"].stringValue);
                }
            }
            else
            {
              
                if(json["data"]["success"].stringValue == "true")
                {
                  
                    let setting = json["data"];
                      print(setting)
                    settingData = ["setting_id":setting["setting_id"].stringValue,"setting_status":setting["setting_status"].stringValue,"message":setting["message"].stringValue];
                    //dealStatusButton.setTitle(settingData["setting_status"], for: .normal);
                    dealMessageLabel.text = settingData["message"];
                }
                else if(json["data"]["success"].stringValue == "false")
                {
                    let showTitle = "Error";
                    let showMsg = json["data"]["message"].stringValue;
                    
                
                    
                    
                }
                
            }
            
        }
    }
    
    @objc func submitButtonClicked(_ sender: UIButton)
    {
//        if let settingId = settingData["setting_id"] {
            if(dealMessageLabel.text == "")
            {
                Alert_File.showValidationError(self,title:"Empty".localized,message:"Please enter message".localized);
                return;
            }
            let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
            let vendorId = userData["vendorId"] as! String
            let hashKey    = userData["hashKey"] as! String
            var postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey;
            postString += "&deal_message=" + dealMessageLabel.text!;
            
            postString += "&setting_id=" + (settingData["setting_id"] ?? "")
            postString += "&status=" + "enable";
            Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
            self.sendRequest(url: "vdealapi/setting/save", parameters: postString)
//        }
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
}
