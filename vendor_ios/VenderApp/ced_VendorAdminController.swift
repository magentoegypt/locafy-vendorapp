//
//  ced_VendorAdminController.swift
//  VenderApp
//
//  Created by cedcoss on 12/02/21.
//  Copyright © 2021 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit
struct adminInbox{
 let sender_name: String
   let sender: String
   let id: String
   let customer_id: String
   let created_at: String
   let subject: String
   let message: String
   let message_status: String
   let updated_at: String
   let receiver_name: String
}
class ced_VendorAdminController: ced_VendorBaseClass ,UITableViewDelegate, UITableViewDataSource, UITextFieldDelegate {
    @IBOutlet weak var topLabel: UILabel!
    @IBOutlet weak var filterButton: UIButton!
      var adminComposeButton: UIButton!
   
    @IBOutlet var tableView: UITableView!
   var currentPage = 1
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
    var bgColor = UIColor()
    
    
    var adminInboxArray = [adminInbox]()
   
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        topLabel.text = " Admin Inbox".localized
        topLabel.setThemeColor()
//        tableView.delegate = self
//        tableView.dataSource = self
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        bgColor = Ced_CommonVendor.UIColorFromRGB(colorString!)
        filterButton.addTarget(self, action: #selector(filterButtonTapped(_:)), for: .touchUpInside)
        
       
    }
    
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        adminInboxArray = []
        currentPage = 1;
        
        adminRequest()
        adminActionFloatingButton()
        
    }
    func adminActionFloatingButton(){
        
        let bounds = self.view.bounds
        adminComposeButton = UIButton(frame: CGRect(x: bounds.width-100, y: bounds.height-150, width: 50, height: 50))
        let imageview = UIImageView(frame: CGRect(x: bounds.width-100, y: bounds.height-150, width: 50, height: 50))
        imageview.image = UIImage(named: "add_attribute")
        imageview.layer.cornerRadius = imageview.frame.width/2
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        adminComposeButton.backgroundColor = UIColor.clear
        imageview.backgroundColor = color
        imageview.contentMode = .center
        adminComposeButton.addTarget(self, action: #selector(adminComposeAction(_:)), for: .touchUpInside)
        self.view.insertSubview(imageview, aboveSubview: self.tableView)
        self.view.insertSubview(adminComposeButton, aboveSubview: self.tableView)
    }
    
      @objc func adminComposeAction(_ sender : UIButton){
             print("rt45yrt")
             let viewController = UIStoryboard(name: "Main" ,bundle:  nil).instantiateViewController(withIdentifier: "ced_messageComposeController") as! ced_messageComposeController
             viewController.isFromAdmin = true
             self.navigationController?.pushViewController(viewController, animated: true)
         }
    func adminRequest(){
        var params = Dictionary<String,String>()
       let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
      //  let hashKey    = userData["hashKey"] as! String
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
        params["vendor_id"] = vendorId
        params["page"] = "\(currentPage)"
        if filter != "" {
            params["filter"] = filter
        }

        self.sendRequestWithData(url: "rest/V1/vmessgingapi/admininbox", params: params)
    }
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        guard let data = data else {return}
        
         do {
                   let json = try JSON(data: data)
                   print(json)
                  // json = json[0]
            
            if json["vendor_data"]["success"].stringValue == "true" {
                for list in json["vendor_data"]["inbox_list"].arrayValue {
                    let temp = adminInbox(sender_name: list["sender_name"].stringValue,
                                          sender: list["sender"].stringValue,
                                          id: list["id"].stringValue,
                                          customer_id: list["customer_id"].stringValue,
                                          created_at: list["created_at"].stringValue,
                                          subject: list["subject"].stringValue,
                                          message: list["message"].stringValue,
                                          message_status: list["message_status"].stringValue,
                                          updated_at: list["updated_at"].stringValue,
                                          receiver_name: list["receiver_name"].stringValue)
                    
                    adminInboxArray.append(temp)
            }
            }
            if json["vendor_data"]["success"] == false && currentPage == 1 {
               // self.view.makeToast(json["vendor_data"]["message"].stringValue, duration: 2.0, position: .center)
                self.tableView.setEmptyMessage(json["vendor_data"]["message"].stringValue)
            }
            if json["vendor_data"]["success"] == false {
                self.loading = false
            }
      /*      if json["vendor_data"]["success"].stringValue == "false" {
                self.view.makeToast(json["vendor_data"]["message"].stringValue, duration: 1.0, position: .center)
                tableView.isHidden = true
                return
            }
            
            for list in json["vendor_data"]["inbox_list"].arrayValue {
                let temp = adminInbox(sender_name: list["sender_name"].stringValue,
                                      sender: list["sender"].stringValue,
                                      id: list["id"].stringValue,
                                      customer_id: list["customer_id"].stringValue,
                                      created_at: list["created_at"].stringValue,
                                      subject: list["subject"].stringValue,
                                      message: list["message"].stringValue,
                                      message_status: list["message_status"].stringValue,
                                      updated_at: list["updated_at"].stringValue,
                                      receiver_name: list["receiver_name"].stringValue)
                
                adminInboxArray.append(temp)
            }  */
            tableView.delegate = self
            tableView.dataSource = self
            tableView.reloadData()
            tableView.isHidden = false
         
            
            
         }
            catch let error {
            print(error.localizedDescription)
        }
        
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
       return adminInboxArray.count
       }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "ced_adminInboxTC", for: indexPath) as! ced_adminInboxTC
        cell.cellCardView.cardView()
       
        cell.adminSenderLabel.text = adminInboxArray[indexPath.row].sender_name
        cell.adminRecieverLabel.text = adminInboxArray[indexPath.row].receiver_name
        cell.adminCreatedByLabel.text = adminInboxArray[indexPath.row].created_at
        cell.adminUpdatedAtLabel.text = adminInboxArray[indexPath.row].updated_at
        cell.adminSubjectLabel.text = adminInboxArray[indexPath.row].subject
        cell.newMessageLabel.text = adminInboxArray[indexPath.row].message_status
        cell.adminViewBtn.addTarget(self, action: #selector(viewButtontapped(_:)), for: .touchUpInside)
        cell.adminViewBtn.setThemeColor()
        cell.adminViewBtn.tag = Int(adminInboxArray[indexPath.row].id) ?? 0
        return cell
        
        
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 260
    }
//
    @objc func viewButtontapped(_ sender: UIButton) {
       let viewController = UIStoryboard(name: "Main" ,bundle:  nil).instantiateViewController(withIdentifier: "ced_messageChatController") as! ced_messageChatController
        viewController.id = "\(sender.tag)"
        viewController.isFromCustomer = false
        self.navigationController?.pushViewController(viewController, animated: true)
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
         adminInboxArray = [adminInbox]()
        self.adminRequest()
       
        
        
        
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
         adminInboxArray = [adminInbox]()
        self.adminRequest()
        
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
    
    @objc func scrollViewDidEndDragging(_ scrollView: UIScrollView, willDecelerate decelerate: Bool) {
        let currentOffset = scrollView.contentOffset.y
        let maximumOffset = scrollView.contentSize.height - scrollView.frame.size.height
        if (maximumOffset - currentOffset) <= 40 {
            if loading{
                self.currentPage += 1;
                self.adminRequest()
                
            }
        }
    }
    
    
   
    
}

