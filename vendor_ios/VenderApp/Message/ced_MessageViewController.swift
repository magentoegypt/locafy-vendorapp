//
//  ced_MessageViewController.swift
//  VenderApp
//
//  Created by cedcoss on 04/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_MessageViewController: ced_VendorBaseClass,UITableViewDelegate,UITableViewDataSource {
    
   
    @IBOutlet weak var readView: UIScrollView!
    @IBOutlet weak var readTitle: UILabel!
    @IBOutlet weak var width: NSLayoutConstraint!
    @IBOutlet weak var subjct: UILabel!
    @IBOutlet weak var reply: UIButton!
    
    @IBOutlet weak var bck: UIButton!
    @IBOutlet weak var datetime: UILabel!
    @IBOutlet weak var msg: UILabel!
    @IBOutlet weak var contentView: UIView!
    @IBOutlet weak var mailAdmin: CheckboxButtonView!
    @IBOutlet weak var customerInbox: DropDownButtonView!
    @IBOutlet weak var showStatus: UIView!
    @IBOutlet weak var Send: UIButton!
    @IBOutlet weak var composeWidth: NSLayoutConstraint!
    @IBOutlet weak var composeView: UIView!
    @IBOutlet weak var composeTitle: UILabel!
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var OwnerTextField: UITextField!
    @IBOutlet weak var Subject: UITextField!
    @IBOutlet weak var Description: UITextView!
    @IBOutlet weak var InboxView: UIView!
    @IBOutlet weak var segmentControl: UISegmentedControl!
    @IBOutlet weak var scrollView1: UIScrollView!
    var baseURL = String();
    var InboxData = [[String:String]]()
    var storeData = [[String:String]]()
    var changeFlag = false
    var isloadedInbox = false
    var isloadedSent = false
    var isCustomer = false
    var  isChecked = false
    var names = [String : String]()
    var page = 1
    var loading = true
    var customer_id = ""
    var fromReply = false
    override func viewDidLoad() {
        super.viewDidLoad()
        
        baseURL = "vmessagingapi/customer/configval"
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        let postString = "vendor_id=" + vendorId + "&hashkey="+hashKey;
        self.sendRequest(url: baseURL, parameters: postString)
        if isCustomer
        {
            customerInbox.isHidden = false
        }
        else
        {
            customerInbox.isHidden = true
        }
        mailAdmin.checkboxButton.setTitle("Email this message to Vendor", for: UIControl.State())
        mailAdmin.checkboxButton.titleLabel?.font = UIFont(name:"Roboto-Regular" , size: 12)
        reply.addTarget(self, action: #selector(replyClicked(_:)), for: .touchUpInside)
        reply.tag = 180
        bck.tag = 210
        bck.layer.cornerRadius=2
        reply.layer.cornerRadius=2
        bck.setTitle("Back", for: UIControl.State())
        bck.addTarget(self, action: #selector(replyClicked(_:)), for: .touchUpInside)
        mailAdmin.checkboxButton.addTarget(self, action: #selector(sendToAdmin(_:)), for: .touchUpInside)
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as! String
        segmentControl.tintColor =  Ced_CommonVendor.UIColorFromRGB(colorString)
        composeTitle.textColor = Ced_CommonVendor.UIColorFromRGB(colorString)
        self.navigationController?.navigationBar.titleTextAttributes =
            [NSAttributedString.Key.foregroundColor: UIColor.white,NSAttributedString.Key.font: UIFont(name: "Roboto-Bold", size: 15)!]
        OwnerTextField.layer.cornerRadius = 5
        Subject.layer.cornerRadius = 5
        Description.layer.borderWidth = 1
        Description.layer.cornerRadius = 5
        composeWidth.constant = self.view.frame.width
        width.constant = self.view.frame.width
        Send.backgroundColor =  Ced_CommonVendor.UIColorFromRGB(colorString)
        reply.backgroundColor =  Ced_CommonVendor.UIColorFromRGB(colorString)
        reply.setTitleColor(.white, for: .normal)
        let color = Ced_CommonVendor.UIColorFromRGB("#ff0000");
        bck.backgroundColor =  color
        bck.setTitleColor(.white, for: .normal)
        Send.setTitle("Send", for: .normal)
        Send.setTitleColor(.white, for: .normal)
        Send.layer.cornerRadius=5
        Send.addTarget(self, action: #selector(SendData(_:)), for: .touchUpInside)
        customerInbox.dropDownButton.addTarget(self, action: #selector(showdata(_:)), for: .touchUpInside)
        customerInbox.dropDownButton.tintColor = UIColor.black
        customerInbox.dropDownButton.setTitleColor(UIColor.black, for: UIControl.State())
    }
    @objc func sendToAdmin(_ sender:UIButton)
    {
        if isChecked
        {
            sender.setImage(UIImage(named: "unchecked_checkbox"), for: UIControl.State());
            isChecked = false
        }
        else
        {
            sender.setImage(UIImage(named: "checked_checkbox"), for: UIControl.State());
            isChecked = true
        }
    }
    @objc func showdata(_ sender:UIButton)
    {
        let dropDown = DropDown();
        var dropDownToShow = Array(self.names.keys);
        dropDownToShow = dropDownToShow.sorted();
        if let indexToRemove = dropDownToShow.index(of: "--Please Select--") {
            dropDownToShow.remove(at: indexToRemove);
        }
        
        
        dropDown.dataSource = dropDownToShow;
        dropDown.selectionAction = {(index, item) in
            _ = self.names[item]!;
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
    @IBAction @objc func SegmentOption(_ sender: Any) {
        switch segmentControl.selectedSegmentIndex
        {
        case 0:
            self.title = "Compose"
            self.scrollView1.isHidden = false
            self.composeView.isHidden = false
            self.InboxView.isHidden = true
            self.readView.isHidden = true
            self.isloadedSent = false
        case 1:
            self.title = "Inbox"
            changeFlag = true
              self.composeView.isHidden = true
            self.scrollView1.isHidden = true
            self.InboxView.isHidden = false
            self.readView.isHidden = true
            page = 1
            loading = true
            if isloadedInbox
            {
                self.tableView.reloadData()
            }
            else
            {
                Request()
            }
        case 2:
            self.readView.isHidden = true
            self.title = "Sent"
            changeFlag = false
            loading = true
            composeView.isHidden = true
            self.scrollView1.isHidden = true
            self.InboxView.isHidden = false
            page = 1
            if isloadedSent
            {
                self.tableView.reloadData()
            }
            else
            {
                Request()
                
            }
        default:
            break;
        }
    }
    @objc func SendData(_ sender:UIButton)
    {
        if Description.text! == ""
        {
            ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error", showMsg:"Description can't be Empty")
        }
        else if Subject.text! == ""
        {
            ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error", showMsg:"Subject can't be Empty")
        }
        else
        {
            if isCustomer
            {
                baseURL = "vmessagingapi/customer/sent"
                let subject = Subject.text!
                let describtion = Description.text!
                let email_name =   customerInbox.dropDownButton.currentTitle
                var email = ""
                if let email_id = self.names[email_name!]
                {
                    email = email_id
                }
                let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
                let vendorId = userData["vendorId"] as! String
                let hashKey    = userData["hashKey"] as! String
                var postString = "vendor_id="+vendorId+"&hashkey="+hashKey+"&subject="+subject+"&message="+describtion
                
                print(postString)
                if isChecked
                {
                    let value = "1"
                    postString += "&mail_send="+value
                }
                if fromReply
                {
                    postString += "&customer_id="+customer_id
                }
                else
                {
                    postString += "&email="+email
                }
                Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
                self.sendRequest(url: baseURL, parameters: postString)
            }
            else
            {
                baseURL = "vmessagingapi/admin/sent"
                let subject = Subject.text!
                let describtion = Description.text!
                print(subject)
                print(describtion)
                let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
                let vendorId = userData["vendorId"] as! String
                let hashKey    = userData["hashKey"] as! String
                let postString = "vendor_id="+vendorId+"&hashkey="+hashKey+"&subject="+subject+"&message="+describtion
                Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
                self.sendRequest(url: baseURL, parameters: postString)
            }
        }
    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    @objc func numberOfSections(in tableView: UITableView) -> Int {
        return 2
    }
    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if changeFlag == true
        {
            if section == 0
            {
                return 1
            }
            else
            {
                return InboxData.count
            }
        }
        else
        {
            if section == 0
            {
                return 1
            }
            else
            {
                return storeData.count
            }
        }
    }
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if indexPath.section==0{
            if InboxData.count == 0 && changeFlag == true
            {
                return 150
            }
            else if storeData.count == 0 && changeFlag == false
            {
                return 150
            }
            else
            {
                return 50
            }
        }
        else
        {
            return 150
        }
    }
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        if changeFlag == true
        {
            if indexPath.section == 0
            {
                let cell=tableView.dequeueReusableCell(withIdentifier: "MessageCell") as! ced_MessageTableViewCell
                cell.InboxTitle.text = "Inbox"
                cell.InboxTitle.textColor = color
                cell.NoMessage.text = "There are no messages for you by the admin."
                cell.selectionStyle = UITableViewCell.SelectionStyle.none
                // cell.InboxTitle.backgroundColor = color
                return cell
                
            }
            else
            {
                let cell=tableView.dequeueReusableCell(withIdentifier: "InboxContent") as! ced_MessageTableViewCell
                print(InboxData)
                 cell.sendername.text = "Sender's Mail"
                cell.sendermail.text = InboxData[indexPath.row]["receiver_name"]
                let time = InboxData[indexPath.row]["time"]
                cell.Date.text = InboxData[indexPath.row]["date"]!+" "+time!
                if InboxData[indexPath.row]["message"]! == ""
                {
                    cell.message.text = "..."
                }
                else
                {
                    cell.message.text = InboxData[indexPath.row]["message"]!
                }
                if InboxData[indexPath.row]["subject"]! == ""
                {
                    cell.subject.text = "..."
                }
                else
                {
                    cell.subject.text = InboxData[indexPath.row]["subject"]!
                }
                cell.inboxCell.makeCard(cell.inboxCell, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
                cell.selectionStyle = UITableViewCell.SelectionStyle.none
                return cell
            }
        }
        else
        {
            if indexPath.section == 0
            {
                let cell=tableView.dequeueReusableCell(withIdentifier: "MessageCell") as! ced_MessageTableViewCell
                cell.InboxTitle.text = "Sent"
                cell.InboxTitle.textColor = color
                cell.NoMessage.text = "There are no messages sent to you by the admin."
                cell.selectionStyle = UITableViewCell.SelectionStyle.none
                return cell
            }
            else
            {
                let cell=tableView.dequeueReusableCell(withIdentifier: "InboxContent") as! ced_MessageTableViewCell
                cell.sendername.text = "Receiver Name"
                if storeData[indexPath.row]["receiver_name"]! == ""
                {
                    cell.sendermail.text = "..."
                }
                else
                {
                    cell.sendermail.text = storeData[indexPath.row]["receiver_name"]!
                }
                if storeData[indexPath.row]["message"]! == ""
                {
                    cell.message.text = "..."
                }
                else
                {
                    cell.message.text = storeData[indexPath.row]["message"]!
                }
                if storeData[indexPath.row]["subject"]! == ""
                {
                    cell.subject.text = "..."
                }
                else
                {
                    cell.subject.text = storeData[indexPath.row]["subject"]!
                }
                let time = storeData[indexPath.row]["time"]
                cell.Date.text = storeData[indexPath.row]["date"]!+" "+time!
                cell.inboxCell.makeCard(cell.inboxCell, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
                cell.selectionStyle = UITableViewCell.SelectionStyle.none
                return cell
            }
        }
        
    }
    @objc func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        if indexPath.section != 0
        {
    
            var id = ""
            if changeFlag
            {
                id = InboxData[indexPath.row]["id"]!
            }
           else
            {
                id = storeData[indexPath.row]["id"]!
            }
            baseURL = "vmessagingapi/customer/view"
            let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
            let vendorId = userData["vendorId"] as! String
            let hashKey    = userData["hashKey"] as! String
            let postString = "vendor_id="+vendorId+"&hashkey="+hashKey+"&id="+id
            Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
            self.sendRequest(url: baseURL, parameters: postString)
        }
//        if changeFlag == true
//        {
//            if indexPath.section != 0
//            {
//                self.readView.isHidden = false
//                self.InboxView.isHidden = true
//                let time = InboxData[indexPath.row]["time"]
//                datetime.text = InboxData[indexPath.row]["date"]!+" "+time!
//                msg.text = InboxData[indexPath.row]["message"]!
//                subjct.text = InboxData[indexPath.row]["subject"]!
//            }
//        }
//        else
//        {
//            if indexPath.section != 0
//            {
//                self.readView.isHidden = false
//                self.InboxView.isHidden = true
//                let time = storeData[indexPath.row]["time"]
//                datetime.text = storeData[indexPath.row]["date"]!+" "+time!
//                msg.text = storeData[indexPath.row]["message"]!
//                subjct.text = storeData[indexPath.row]["subject"]!
//            }
//        }
    }
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        if let data = data{
            if(requestUrl=="vmessagingapi/customer/view")
            {
                guard let json = try? JSON(data:data) else {return}
                print(json)
                if(json["data"]["status"].stringValue.lowercased() == "false"){
                    self.view.showToastMsg(json["data"]["message"].stringValue)
                }else if(json["data"]["status"].stringValue.lowercased() == "true"){
                    self.readMail(json)
                }
            }
            if isCustomer
            {
                if(requestUrl=="vmessagingapi/customer/inbox")
                {
                    isloadedInbox = true
                    guard let json = try? JSON(data:data) else {return}
                    print(json)
                    if(json["data"]["status"].stringValue.lowercased() == "false"){
                        //self.view.showToastMsg(json["data"]["message"].stringValue)
                        loading = false
                        self.tableView.delegate = self
                        self.tableView.dataSource = self
                        self.tableView.reloadData()
                    }else if(json["data"]["status"].stringValue.lowercased() == "true"){
                        self.parseProductdata(json)
                        self.tableView.reloadData()
                    }
                }
                else if(requestUrl=="vmessagingapi/customer/sentmessage")
                {
                    isloadedSent = true
                    guard let json = try? JSON(data:data) else {return}
                    print(json)
                    if(json["data"]["status"].stringValue.lowercased() == "false"){
                        //self.view.showToastMsg(json["data"]["message"].stringValue)
                        self.tableView.delegate = self
                        self.tableView.dataSource = self
                        self.tableView.reloadData()
                        loading = false
                    }else if(json["data"]["status"].stringValue.lowercased() == "true"){
                        //  self.tableView.isHidden = false
                        self.parseProductdata(json)
                    }
                }
                else if(requestUrl=="vmessagingapi/customer/configval")
                {
                    guard let json = try? JSON(data:data) else {return}
                    print(json)
                    if(json["data"]["success"].stringValue.lowercased() == "false"){
                        self.view.showToastMsg(json["data"]["message"].stringValue)
                    }else if(json["data"]["success"].stringValue.lowercased() == "true"){
                        self.parseName(json)
                    }
                }
                    
                else if(requestUrl=="vmessagingapi/customer/sent")
                {
                    guard let json = try? JSON(data:data) else {return}
                    print(json)
                    if(json["data"]["success"].stringValue.lowercased() == "false"){
                        self.view.showToastMsg(json["data"]["message"].stringValue)
                        
                    }else if(json["data"]["success"].stringValue.lowercased() == "true"){
                        Subject.text = ""
                        Description.text = ""
                        customerInbox.dropDownButton.setTitle("--Please Select--", for: UIControl.State())
                        self.view.showToastMsg(json["data"]["message"].stringValue)
                    }
                }
            }
            else
            {
                if(requestUrl=="vmessagingapi/admin/inbox")
                {
                    isloadedInbox = true
                    guard let json = try? JSON(data:data) else {return}
                    print(json)
                    if(json["data"]["status"].stringValue.lowercased() == "false"){
                        //self.view.showToastMsg(json["data"]["message"].stringValue)
                        // isEmpty = true
                        loading = false
                        self.tableView.delegate = self
                        self.tableView.dataSource = self
                        self.tableView.reloadData()
                    }else if(json["data"]["status"].stringValue.lowercased() == "true"){
                        self.parseProductdata(json)
                    }
                }
                else if(requestUrl=="vmessagingapi/admin/sentmessage")
                {
                    isloadedSent = true
                    guard let json = try? JSON(data:data) else {return}
                    print(json)
                    if(json["data"]["status"].stringValue.lowercased() == "false"){
                        //self.view.showToastMsg(json["data"]["message"].stringValue)
                        loading = false
                        self.tableView.delegate = self
                        self.tableView.dataSource = self
                        self.tableView.reloadData()
                    }else if(json["data"]["status"].stringValue.lowercased() == "true"){
                        // isEmpty = false
                        self.parseProductdata(json)
                    }
                }
                else if(requestUrl=="vmessagingapi/admin/sent")
                {
                   guard let json = try? JSON(data:data) else {return}
                    print(json)
                    if(json["data"]["success"].stringValue.lowercased() == "false"){
                        self.view.showToastMsg(json["data"]["message"].stringValue)
                        
                    }else if(json["data"]["success"].stringValue.lowercased() == "true"){
                        
                        self.view.showToastMsg(json["data"]["message"].stringValue)
                    }
                }
            }
        }
        
    }
    func readMail(_ json:JSON){
        readView.isHidden = false
        let result = json["data"]["read_info"]
        msg.text = result["message"].string
        subjct.text = result["subject"].string
        datetime.text = result["date"].string
        if result["customer_id"].string != nil
        {
            customer_id = result["customer_id"].string!
        }
        InboxView.isHidden = true
        
    }
    func parseProductdata(_ json:JSON){
        if changeFlag
        {
            if isCustomer {
                for result in json["data"]["customer_inbox_messages"].arrayValue{
                    let receiver_name = result["receiver_name"].string
                    let message = result["message"].string
                    let subject = result["subject"].string
                    let date = result["date"].string
                    let time = result["time"].string
                     let id  = result["id"].string
                    let temp = ["message":message,"receiver_name":receiver_name,"subject":subject,"date":date,"time":time,"id":id]
                    Alert_File.removeLoadingIndicator(self);
                    self.InboxData.append(temp as! [String : String])
                }
            }
            else
            {
                for result in json["data"]["vendor_inbox_messages"].arrayValue{
                    
                    let sender_email = result["sender_email"].string
                    let message = result["message"].string
                    let subject = result["subject"].string
                    let date = result["date"].string
                    let time = result["time"].string
                     let id  = result["id"].string
                    let temp = ["message":message,"sender_email":sender_email,"subject":subject,"date":date,"time":time,"id":id]
                    Alert_File.removeLoadingIndicator(self);
                    self.InboxData.append(temp as! [String : String])
                }
            }
            self.tableView.delegate = self
            self.tableView.dataSource = self
            self.tableView.reloadData()
        }
        else
        {
            if isCustomer
            {
                for result in json["data"]["customer_sent_messages"].arrayValue{
                    
                    let receiver_name = result["receiver_name"].string
                    let message = result["message"].string
                    let subject = result["subject"].string
                    let date = result["date"].string
                    let time = result["time"].string
                    let id  = result["id"].string
                    let temp = ["message":message,"receiver_name":receiver_name,"subject":subject,"date":date,"time":time,"id":id]
                    Alert_File.removeLoadingIndicator(self);
                    self.storeData.append(temp as! [String : String])
                }
            }
            else
            {
                for result in json["data"]["vendor_sent_message"].arrayValue{
                    
                    let receiver_name = result["receiver_name"].string
                    let message = result["message"].string
                    let subject = result["subject"].string
                    let date = result["date"].string
                    let time = result["time"].string
                    let id  = result["id"].string
                    let temp = ["message":message,"receiver_name":receiver_name,"subject":subject,"date":date,"time":time,"id":id]
                    Alert_File.removeLoadingIndicator(self);
                    self.storeData.append(temp as! [String : String])
                }
            }
        }
        self.tableView.delegate = self
        self.tableView.dataSource = self
        self.tableView.reloadData()
    }
    func parseName(_ json:JSON){
        let result = json["data"]["customer_details"].arrayValue
        print(result)
        let email_to_admin = json["data"]["email_to_admin"].string
        let email_to_vendor = json["data"]["email_to_vendor"].string
        if email_to_admin == "1" && email_to_vendor == "1"
        {
            mailAdmin.isHidden = false
        }
        else
        {
            mailAdmin.isHidden = true
        }
        for item in result
        {
            // Name.append(item.string!)
            let key = item["name"].string;
            let value = item["email"].string;
            self.names[key!] = value;
        }
        Alert_File.removeLoadingIndicator(self);
    }
    @objc func replyClicked(_ sender:UIButton)
    {
        if sender.tag == 180
        {
        fromReply = true
        scrollView1.isHidden = false
        composeView.isHidden = false
        InboxView.isHidden = true
        readView.isHidden = true
        segmentControl.selectedSegmentIndex = 0
        }
        else
        {
            InboxView.isHidden = false
            scrollView1.isHidden = true
            composeView.isHidden = true
            readView.isHidden = true
           
        }
    }
    @objc func Request() {
        if changeFlag
        {
            if isCustomer
            {
                
                baseURL = "vmessagingapi/customer/inbox"
            }
            else
            {
                baseURL = "vmessagingapi/admin/inbox"
            }
        }
        else
        {
            if isCustomer
            {
                baseURL = "vmessagingapi/customer/sentmessage"
            }
            else
            {
                baseURL = "vmessagingapi/admin/sentmessage"
            }
        }
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        let postString = "vendor_id=" + vendorId + "&hashkey="+hashKey+"&page=\(page)"
        self.sendRequest(url: baseURL, parameters: postString)
    }
    @objc func scrollViewDidEndDragging(_ scrollView: UIScrollView, willDecelerate decelerate: Bool) {
        
        let currentOffset = scrollView.contentOffset.y
        let maximumOffset = scrollView.contentSize.height - scrollView.frame.size.height
        if (maximumOffset - currentOffset) <= 40 {
            if loading{
                
                self.page += 1;
                self.Request()
                tableView.reloadData();
            }
        }
    }
}

