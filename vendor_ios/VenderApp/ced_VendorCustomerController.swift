//
//  ced_VendorCustomerController.swift
//  VenderApp
//
//  Created by cedcoss on 13/02/21.
//  Copyright © 2021 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit
struct vendorInbox {
    let sender_name: String
    let sender: String
    let id: String
    let vendor_id: String
    let created_at: String
    let subject: String
    let message: String
    let message_status: String
    let updated_at: String
    let receiver_name: String
}

struct customerNameList {
    let customer_id: String
    let first_name: String
}

class ced_VendorCustomerController: ced_VendorBaseClass, UITextFieldDelegate {
    @IBOutlet weak var filterButton: UIButton!
    @IBOutlet weak var topLabel: UILabel!
    
    var vendorInboxList = [vendorInbox]()
    var currentPage = 1
    var bgColor = UIColor()
    
    var customerName = [customerNameList]()
    let filterView = ced_VendorCustomerFilterView()
    var sender = String()
    var reciever = String()
    var fromCreatedAt = String()
    var toCreatedAt = String()
    var fromUpdatedAt = String()
    var toUpdatedAt = String()
    var subject = String()
    var filter = String()
    var textFieldForDate = UITextField()
    var datePickerView = UIDatePicker()
    var loading = true
    
    @IBOutlet weak var inboxTableView: UITableView!
    
     var customerComposeButton: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        topLabel.text = " Customer Inbox".localized
        topLabel.setThemeColor()
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        bgColor = Ced_CommonVendor.UIColorFromRGB(colorString!)
     
//        composeMessageBtn.setThemeColor()
//        composeMessageBtn.layer.cornerRadius = 5
        customerActionFloatingButton()
        filterButton.addTarget(self, action: #selector(filterButtonTapped(_:)), for: .touchUpInside)
    }
    
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        vendorInboxList = []
        customerName = []
        currentPage = 1;
        loading = true
        filter = String()
        inboxTableView.reloadData()
        self.loadData()
    }
    
    func loadData() {
        var params = Dictionary<String,String>()
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
           let vendorId = userData["vendorId"] as! String
         //  let hashKey    = userData["hashKey"] as! String
        params["vendor_id"] = vendorId
        params["page"] = "\(currentPage)"
        if filter !=  "" {
            params["filter"] = filter
        }
           
          self.sendRequestWithData(url: "rest/V1/vmessgingapi/customerinbox", params: params)
    }
      func customerActionFloatingButton(){
            
            let bounds = self.view.bounds
            customerComposeButton = UIButton(frame: CGRect(x: bounds.width-100, y: bounds.height-150, width: 50, height: 50))
            let imageview = UIImageView(frame: CGRect(x: bounds.width-100, y: bounds.height-150, width: 50, height: 50))
            imageview.image = UIImage(named: "add_attribute")
            imageview.layer.cornerRadius = imageview.frame.width/2
            let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
            let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
            customerComposeButton.backgroundColor = UIColor.clear
            imageview.backgroundColor = color
            imageview.contentMode = .center
            customerComposeButton.addTarget(self, action: #selector(adminComposeAction(_:)), for: .touchUpInside)
            self.view.insertSubview(imageview, aboveSubview: self.inboxTableView)
            self.view.insertSubview(customerComposeButton, aboveSubview: self.inboxTableView)
        }
        
        @objc func adminComposeAction(_ sender : UIButton){
            print("rt45yrt")
            let viewController = UIStoryboard(name: "Main" ,bundle:  nil).instantiateViewController(withIdentifier: "ced_messageComposeController") as! ced_messageComposeController
            
            viewController.customerList = self.customerName
            viewController.isFromAdmin = false
            self.navigationController?.pushViewController(viewController, animated: true)
        }
    
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        guard let data = data else{return}
        do {
            let json = try JSON(data: data)
            print(json)
            customerName = [customerNameList]()
            if json["vendor_data"]["success"].stringValue == "true" {
               
                
                for list in json["vendor_data"]["inbox_list"].arrayValue {
                    let temp = vendorInbox(sender_name: list["sender_name"].stringValue,
                                           sender: list["sender"].stringValue,
                                           id: list["id"].stringValue,
                                           vendor_id: list["vendor_id"].stringValue,
                                           created_at: list["created_at"].stringValue,
                                           subject: list["subject"].stringValue,
                                           message: list["message"].stringValue,
                                           message_status: list["message_status"].stringValue,
                                           updated_at: list["updated_at"].stringValue,
                                           receiver_name: list["receiver_name"].stringValue)
                    
                    vendorInboxList.append(temp)
                    print(vendorInboxList)
                }
            }
            
            for list in json["vendor_data"]["customer_list"].arrayValue{        // for vendor name dropdown
                let temp = customerNameList(customer_id: list["customer_id"].stringValue, first_name: list["first_name"].stringValue)
                customerName.append(temp)
            }
            
            if json["vendor_data"]["success"] == false && currentPage == 1 {
               // self.view.makeToast(json["vendor_data"]["message"].stringValue, duration: 2.0, position: .center)
                self.inboxTableView.setEmptyMessage(json["vendor_data"]["message"].stringValue)
                return
            }
            if json["vendor_data"]["success"] == false {
                self.loading = false
            }
            
         /*   if json["vendor_data"]["status"].stringValue == "false" {
                self.view.makeToast(json["vendor_data"]["message"].stringValue, duration: 1.0, position: .center)
                inboxTableView.isHidden = true
                return
            }
            
            for list in json["vendor_data"]["inbox_list"].arrayValue {
                let temp = vendorInbox(sender_name: list["sender_name"].stringValue,
                                       sender: list["sender"].stringValue,
                                       id: list["id"].stringValue,
                                       vendor_id: list["vendor_id"].stringValue,
                                       created_at: list["created_at"].stringValue,
                                       subject: list["subject"].stringValue,
                                       message: list["message"].stringValue,
                                       message_status: list["message_status"].stringValue,
                                       updated_at: list["updated_at"].stringValue,
                                       receiver_name: list["receiver_name"].stringValue)
                
                vendorInboxList.append(temp)
                print(vendorInboxList)
            }
            for list in json["vendor_data"]["customer_list"].arrayValue{        // for vendor name dropdown
                let temp = customerNameList(customer_id: list["customer_id"].stringValue, first_name: list["first_name"].stringValue)
                customerName.append(temp)
            }  */
            inboxTableView.restore()
            inboxTableView.delegate = self
            inboxTableView.dataSource = self
            inboxTableView.reloadData()
            inboxTableView.isHidden = false
        } catch let error {
            print(error.localizedDescription)
        }
        
    }
    
    @objc func filterButtonTapped(_ sender : UIButton) {
        print("filterButtonPressed")
        let bgView = UIView()
        bgView.frame = UIScreen.main.bounds;
        bgView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        bgView.tag = 1234;
        bgView.backgroundColor = UIColor(red:0, green:0, blue:0, alpha:0.5);
        
        let tapGesture = UITapGestureRecognizer()
        tapGesture.addTarget(self, action: #selector(dismissView(_:)))
        bgView.addGestureRecognizer(tapGesture)
        filterView.frame = CGRect(x: 0, y: 0, width: self.view.frame.width - 20, height: 300);
        filterView.center.x = self.view.center.x;
        filterView.center.y = self.view.center.y+20;
        filterView.tag = 181
        filterView.vendorAppBtn.backgroundColor = bgColor
        filterView.vendorAppBtn.setTitleColor(.white, for: .normal)
        filterView.vendorAppBtn.titleLabel?.font = UIFont.boldSystemFont(ofSize: 15.0)
        filterView.filterBtn.setTitle("Filter".localized, for: .normal)
        filterView.filterBtn.backgroundColor = bgColor
        filterView.filterBtn.setTitleColor(.white, for: .normal)
        filterView.filterBtn.titleLabel?.font = UIFont.boldSystemFont(ofSize: 15.0)
        filterView.resetBtn.setTitle("Reset".localized, for: .normal)
        filterView.resetBtn.backgroundColor = bgColor
        filterView.resetBtn.setTitleColor(.white, for: .normal)
        filterView.filterBtn.addTarget(self, action: #selector(applyFilterPressed(_:)), for: .touchUpInside)
        filterView.resetBtn.addTarget(self, action: #selector(resetFilterPressed(_:)), for: .touchUpInside)
        filterView.resetBtn.titleLabel?.font = UIFont.boldSystemFont(ofSize: 15.0)
        filterView.fromUpdatedAt.delegate = self
        filterView.toUpdatedAt.delegate = self
        filterView.fromCreatedAt.delegate = self
        filterView.toCraetedAt.delegate = self
        bgView.addSubview(filterView)
        self.view.addSubview(bgView);
    }
    
    @objc func dismissView(_ gesture : UITapGestureRecognizer) {
        self.view.viewWithTag(1234)?.removeFromSuperview()
    }
    
    @objc func applyFilterPressed(_ sender : UIButton) {
        if let view = self.view.viewWithTag(181) as? ced_VendorCustomerFilterView
        {
            if view.senderField.text != nil {
                self.sender = view.senderField.text!
            }
            if view.recieverField.text != nil {
                reciever = view.recieverField.text!
            }
            if view.fromCreatedAt.text != nil {
                fromCreatedAt = view.fromCreatedAt.text!
            }
            if view.toCraetedAt.text != nil {
                toCreatedAt = view.toCraetedAt.text!
            }
            if view.fromUpdatedAt.text != nil {
                fromUpdatedAt = view.fromUpdatedAt.text!
            }
            if view.toUpdatedAt.text != nil {
                toUpdatedAt = view.toUpdatedAt.text!
            }
            if view.subject.text != nil {
                subject = view.subject.text!
            }
        }
        filter = "";
        filter += "{";
        filter += "\""+"sender_name"+"\":\""+self.sender+"\",";
        filter += "\""+"receiver_name"+"\":\""+reciever+"\",";
        filter += "\""+"created_at"+"\":{\"from\":\""+fromCreatedAt+"\",\"to\":\""+toCreatedAt+"\"},";
        filter += "\""+"updated_at"+"\":{\"from\":\""+fromUpdatedAt+"\",\"to\":\""+toUpdatedAt+"\"},";
        filter += "\""+"subject"+"\":\""+subject+"\"";
        filter += "}";
        print(filter)
        self.view.viewWithTag(1234)?.removeFromSuperview()
        self.currentPage = 1;
        self.vendorInboxList = [vendorInbox]()
        self.loadData()
       
        
        
        
    }
    
    @objc func resetFilterPressed(_ sender : UIButton) {
        
        self.sender = "";
        reciever = "";
        fromCreatedAt = "";
        toCreatedAt = "";
        fromUpdatedAt = "";
        toUpdatedAt = "";
        subject = "";
        filter = "";
        self.view.viewWithTag(1234)?.removeFromSuperview()
        self.currentPage = 1;
        self.vendorInboxList = [vendorInbox]()
        self.loadData()
        
    }
    
    
    
    @objc func textFieldDidBeginEditing(_ textField: UITextField) {
        
 
        textField.resignFirstResponder();
       
        textFieldForDate = textField
        /* background view */
        let bgCView = UIView();
        bgCView.tag=161;
        bgCView.frame = UIScreen.main.bounds;
        bgCView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        bgCView.backgroundColor = UIColor(red:0, green:0, blue:0, alpha:0.5);
        
        
        let colorGreen = Ced_CommonVendor.UIColorFromRGB("#ff0000");
        let colorRed = Ced_CommonVendor.UIColorFromRGB("#ff0000");
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String;
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!);
        
        let tempInrView = UIView();
        tempInrView.tag=131;
        tempInrView.frame = CGRect(x: 0, y: CGFloat(0), width: UIScreen.main.bounds.width-100,height: CGFloat(270));
        tempInrView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        tempInrView.backgroundColor = UIColor.white;
        tempInrView.center = CGPoint(x: bgCView.frame.size.width  / 2,y: bgCView.frame.size.height / 2);
        
        let label = UILabel();
        label.frame = CGRect(x: 0, y: CGFloat(0), width: tempInrView.frame.width,height: CGFloat(30));
        label.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        label.text = "Select Date".localized.uppercased();
        label.textAlignment = NSTextAlignment.center;
        label.textColor = UIColor.white;
        label.backgroundColor = color;
        tempInrView.addSubview(label);
        
        datePickerView.locale = Locale(identifier: ced_storeVC.selectLangauge)
        datePickerView.datePickerMode = UIDatePicker.Mode.date;
        datePickerView.setValue(UIColor.black, forKeyPath: "textColor");
        datePickerView.backgroundColor = UIColor.white;
        datePickerView.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
        datePickerView.frame = CGRect(x: 0, y: CGFloat(30), width: tempInrView.frame.width,height: CGFloat(200));
        tempInrView.addSubview(datePickerView);
        
        
        let twoButtonView = TwoButtonView();
        twoButtonView.tag=141;
        twoButtonView.frame = CGRect(x: 0, y: CGFloat(230), width: tempInrView.frame.width,height: CGFloat(40));
        twoButtonView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        tempInrView.addSubview(twoButtonView);
        
        twoButtonView.cancelButton.addTarget(self, action: #selector(cancelDatePickerView(_:)), for: UIControl.Event.touchUpInside);
        twoButtonView.cancelButton.backgroundColor = colorRed;
        
        
        twoButtonView.doneButton.addTarget(self, action: #selector(pickDateFromDatePickerView(_:)), for: UIControl.Event.touchUpInside);
        twoButtonView.doneButton.backgroundColor = colorGreen;
        
        tempInrView.makeCardUsingThemeColor(tempInrView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4);
        
        bgCView.addSubview(tempInrView);
        self.view.addSubview(bgCView);
  
    }
    
    @objc func pickDateFromDatePickerView(_ sender : UIButton) {
        let dateFormatter = DateFormatter()
        dateFormatter.locale = Locale(identifier: "en_US_POSIX")
        dateFormatter.dateStyle = DateFormatter.Style.short
        dateFormatter.timeStyle = DateFormatter.Style.none
        dateFormatter.dateFormat = "yyyy-MM-dd";
        let timeFormatter = DateFormatter()
        timeFormatter.locale = Locale(identifier: "en_US_POSIX")
        timeFormatter.dateFormat = "hh:mm:ss"
        let time = timeFormatter.string(from: Date())
        let dateDisplay = dateFormatter.string(from: datePickerView.date);
        textFieldForDate.text = dateDisplay + " " + time
        textFieldForDate.resignFirstResponder();
        self.view.viewWithTag(161)?.removeFromSuperview();
    }
    

    
    @objc func cancelDatePickerView(_ sender:UIButton)
    {
        
        sender.resignFirstResponder();
        
        self.view.viewWithTag(161)?.removeFromSuperview();
    }
    
    
}
extension ced_VendorCustomerController : UITableViewDelegate, UITableViewDataSource{
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return vendorInboxList.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = inboxTableView.dequeueReusableCell(withIdentifier: "ced_customerInboxTC", for: indexPath) as! ced_customerInboxTC
        cell.senderLabel.text = vendorInboxList[indexPath.row].sender_name
        cell.receiverLabel.text = vendorInboxList[indexPath.row].receiver_name
        cell.createdAtLabel.text = vendorInboxList[indexPath.row].created_at
        cell.updatedAtLabel.text = vendorInboxList[indexPath.row].updated_at
        cell.subjectLabel.text = vendorInboxList[indexPath.row].subject
        cell.newMessageLabel.text = vendorInboxList[indexPath.row].message_status
        cell.viewButton.setThemeColor()
        cell.viewButton.addTarget(self, action: #selector(viewButtontapped(_:)), for: .touchUpInside)
        cell.viewButton.tag = indexPath.row
        
        
    return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 260
    }
    
    @objc func viewButtontapped(_ sender: UIButton) {
       let viewController = UIStoryboard(name: "Main" ,bundle:  nil).instantiateViewController(withIdentifier: "ced_messageChatController") as! ced_messageChatController

        viewController.id = vendorInboxList[sender.tag].id
        viewController.vendorId = vendorInboxList[sender.tag].vendor_id
        viewController.isFromCustomer = true
        self.navigationController?.pushViewController(viewController, animated: true)
    }
    
    @objc func scrollViewDidEndDragging(_ scrollView: UIScrollView, willDecelerate decelerate: Bool) {
        let currentOffset = scrollView.contentOffset.y
        let maximumOffset = scrollView.contentSize.height - scrollView.frame.size.height
        if (maximumOffset - currentOffset) <= 40 {
            if loading{
                self.currentPage += 1;
                self.loadData()
                
            }
        }
    }
    
}

