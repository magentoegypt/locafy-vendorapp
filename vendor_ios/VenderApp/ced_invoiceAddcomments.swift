//
//  ced_invoiceAddcomments.swift
//  VenderApp
//
//  Created by CEDCOSS Technologies Private Limited on 01/07/16.
//  Copyright © 2016 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_invoiceAddcomments: ced_VendorBaseClass {

    @IBOutlet weak var selectStatus: UIButton!
    @IBOutlet weak var closeView: UIButton!
    @IBOutlet weak var containerView: UIView!
    @IBOutlet weak var notifycustomer: DLRadioButton!
    @IBOutlet weak var visibleOnfrontend: DLRadioButton!
    @IBOutlet weak var topLabel: UILabel!
    @IBOutlet weak var textView: UITextView!
    @IBOutlet weak var saveButton: UIButton!
    var invoiceId = String()
    var prodView  = String()
   
    var notifyuser = false
    var visibleOn = false
    var list  = [String]()
    var value = [String]()
    var statusArray = [String:String]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationController?.navigationBar.titleTextAttributes =
            [NSAttributedString.Key.foregroundColor: UIColor.white,NSAttributedString.Key.font: UIFont(name: "Roboto-Bold", size: 18)!]
        self.title = "Add Comments".localized
        notifycustomer.isMultipleSelectionEnabled = true
        visibleOnfrontend.isMultipleSelectionEnabled = true
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        topLabel.backgroundColor = color
        saveButton.backgroundColor = color
        // Do any additional setup after loading the view.
        containerView.makeCard(containerView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
        closeView.addTarget(self, action: #selector(ced_invoiceAddcomments.closeView(_:)), for: UIControl.Event.touchUpInside)
        saveButton.addTarget(self, action: #selector(ced_invoiceAddcomments.savecomment(_:)), for: UIControl.Event.touchUpInside)
        notifycustomer.addTarget(self, action: #selector(ced_invoiceAddcomments.selecttr(_:)), for: UIControl.Event.touchUpInside)
        visibleOnfrontend.addTarget(self, action: #selector(ced_invoiceAddcomments.selecttr(_:)), for: UIControl.Event.touchUpInside)
        selectStatus.addTarget(self, action: #selector(ced_invoiceAddcomments.SelectStatus(_:)), for: UIControl.Event.touchUpInside)
        if(prodView == "orderview"){
          selectStatus.isHidden = false
          self.loadStatus()
        }
    }

    
    //@objc function to select status
    @objc func SelectStatus(_ sender:UIButton){
        let dropDown = DropDown();
        dropDown.dataSource = list
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
    
    @objc func selecttr(_ sender:UIButton){
        if sender == notifycustomer{
            if(notifyuser){
                notifyuser = false
            }else{
                notifyuser = true
            }
        }else if sender == visibleOnfrontend{
            if(visibleOn){
                visibleOn = false
            }else{
                visibleOn = true
            }
        }
    }
    
    @objc func loadStatus(){
        let baseUrl = "vorderapi/vorders/viewCommentStatus"
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        let postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey + "&order_id=" + invoiceId
        sendRequest(url: baseUrl, parameters: postString);  
    }
    
    override func recieveResponse(data:Data?,requestUrl:String?,response:URLResponse?){
        if let data = data{
            if(requestUrl == "vorderapi/vorders/viewCommentStatus")
            {
                guard let json = try? JSON(data: data) else {return}
                print(json)
                if(json["data"]["success"].stringValue == "true"){
                    for data in json["data"]["status"].arrayValue {
                        self.list.append(data["label"].stringValue)
                        self.value.append(data["value"].stringValue)
                        if(json["data"]["current_order_status"].stringValue != "")
                        {
                            if(data["value"].stringValue == json["data"]["current_order_status"].stringValue)
                                {
                                    selectStatus.setTitle(data["label"].stringValue, for: .normal);
                            }
                        }
                        self.statusArray[data["label"].stringValue] = data["value"].stringValue;
                    }
                    
                }
            }
            else{
                guard let json = try? JSON(data: data) else {return}
                print(json)
                if(json["data"]["success"].stringValue == "true"){
                    self.view.showToastMsg(json["data"]["message"].stringValue)
                    Ced_CommonVendor.delay(1, closure: {
                        self.dismiss(animated: true, completion: nil)
                        
                    })
                    
                }else{
                    ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error", showMsg: json["data"]["message"].stringValue)
                }
                
            }
        }

    }
    
    @objc func closeView(_ sender:UIButton){
        self.dismiss(animated: true, completion: nil)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    @objc func savecomment(_ sender:UIButton){
        print(notifyuser)
        print(visibleOn)
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        let comment = textView.text.addingPercentEncoding(withAllowedCharacters: CharacterSet.urlQueryAllowed)
        if(comment == ""){
            textView.resignFirstResponder()
            Alert_File.removeLoadingIndicator(self)
            self.view.showToastMsg("Please Enter Comment".localized)
            
            return
        }
        var baseUrl="";
        if(prodView == "creditmemo"){
        baseUrl += "vorderapi/vcreditmemo/addComment"
        }else if(prodView == "invoice"){
        baseUrl += "vorderapi/vinvoice/addComment"
        }else if (prodView == "orderview"){
            baseUrl += "vorderapi/vorders/addComment"
        }else if (prodView == "shipment"){
            baseUrl += "vorderapi/vshipment/addComment"
        }
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
     
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        var postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey + "&is_visible_on_front=" + "\(visibleOn)" +  "&is_customer_notified=" + "\(notifyuser)"
        postString    += "&comment="+comment!
        if(prodView == "creditmemo"){
         postString += "&creditmemo_id="+invoiceId
        }else if(prodView == "invoice"){
             postString += "&invoice_id="+invoiceId
        }else if (prodView == "orderview"){
            postString += "&order_id="+invoiceId
            if(selectStatus.titleLabel?.text != "Select Status")
            {
                postString += "&status=" + statusArray[(selectStatus.titleLabel?.text)!]!
            }
        }else if (prodView == "shipment"){
            postString += "&shipment_id="+invoiceId
        }
        print("!!!!!!!!!!!\(postString)")
        sendRequest(url: baseUrl, parameters: postString)
    }

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
