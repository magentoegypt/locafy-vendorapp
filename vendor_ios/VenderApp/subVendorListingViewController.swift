//
//  subVendorListingViewController.swift
//  VenderApp
//
//  Created by MacMini on 24/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class subVendorListingViewController: ced_VendorBaseClass,UITableViewDelegate,UITableViewDataSource,UIGestureRecognizerDelegate {
    
    @IBOutlet weak var topLabel: UILabel!
    
    
    
    
    @IBOutlet weak var tableView: UITableView!
    
    @IBOutlet weak var sendRequestButton: UIButton!
    
    
    @IBOutlet weak var filterBitton: UIButton!
    
    var subVendorData=[[String:JSON]]()
    var pageNo=1
    var filters=""
    var flagForSubVendors=true
    
    override func viewDidLoad() {
        super.viewDidLoad()
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as! String
        topLabel.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
        sendRequestButton.backgroundColor=Ced_CommonVendor.UIColorFromRGB(Ced_CommonVendor.getInfoPlist("subthemecolor") as! String)
        filterBitton.addTarget(self, action: #selector(filterBittonPressed(_:)), for: .touchUpInside)
        loadSubVendor()
        sendRequestButton.addTarget(self, action: #selector(sendRequestButtonPressed(_:)), for: .touchUpInside)
        tableView.dataSource=self
        tableView.delegate=self
        // Do any additional setup after loading the view.
    }
    
    let bgCView = UIView();
    var filterView=subVendorFilterView()
    @objc func filterBittonPressed(_ sender: UIButton){
        let bgCView = UIView();
        bgCView.tag=151;
        bgCView.frame = UIScreen.main.bounds;
        bgCView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        bgCView.backgroundColor = UIColor(red:0, green:0, blue:0, alpha:0.5);
        //filterView=subVendorFilterView()
        filterView.frame=CGRect(x: 0, y: 0, width: bgCView.frame.width-30, height: 230)
        filterView.center=bgCView.center
        filterView.tag=121;
        filterView.status.dropDownButton.addTarget(self, action: #selector(statusSelected(_:)), for: .touchUpInside)
        filterView.applyButton.addTarget(self, action: #selector(applyButtonPressed(_:)), for: .touchUpInside)
        filterView.clearButton.addTarget(self, action: #selector(clearButtonPressed(_:)), for: .touchUpInside)
        
        filterView.firstName.addTarget(self, action: #selector(isValidNme(_:)), for: .editingDidEnd)
        filterView.lastName.addTarget(self, action: #selector(isValidNme(_:)), for: .editingDidEnd)
        filterView.email.addTarget(self, action: #selector(isValidEmil(_:)), for: .editingDidEnd)
        
        
        self.addgesturesTohideView(bgCView)
        bgCView.addSubview(filterView)
        self.view.addSubview(bgCView)
    }
    
    @objc func statusSelected(_ sender: UIButton){
        let dropDown = DropDown();
        let dropDownToShow = ["Pending","Approved","Disapproved"];
        dropDown.dataSource = dropDownToShow;
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
    
    @objc func addgesturesTohideView(_ v:UIView)
    {
        //code to add gestures to dismiss the popover
        let tapGesture = UITapGestureRecognizer (target: self, action:#selector(hideView(_:)))
        v.addGestureRecognizer(tapGesture);
        tapGesture.delegate=self;
        
        let upSwipe = UISwipeGestureRecognizer(target: self, action: #selector(hideView(_:)));
        upSwipe.direction = UISwipeGestureRecognizer.Direction.up;
        v.addGestureRecognizer(upSwipe)
        upSwipe.delegate=self;
        
        let downSwipe = UISwipeGestureRecognizer(target: self, action: #selector(hideView(_:)));
        downSwipe.direction = UISwipeGestureRecognizer.Direction.down;
        v.addGestureRecognizer(downSwipe)
        downSwipe.delegate=self;
        
        let rightSwipe = UISwipeGestureRecognizer(target: self, action: #selector(hideView(_:)));
        rightSwipe.direction = UISwipeGestureRecognizer.Direction.right;
        v.addGestureRecognizer(rightSwipe)
        rightSwipe.delegate=self;
        
        let leftSwipe = UISwipeGestureRecognizer(target: self, action: #selector(hideView(_:)));
        leftSwipe.direction = UISwipeGestureRecognizer.Direction.left;
        v.addGestureRecognizer(leftSwipe)
        leftSwipe.delegate=self;
    }
    
    @objc func hideView(_ recognizer: UITapGestureRecognizer)
    {
        self.view.viewWithTag(121)?.removeFromSuperview()
        self.view.viewWithTag(151)?.removeFromSuperview();
        
    }
    
    
    @objc func gestureRecognizer(_ gestureRecognizer: UIGestureRecognizer, shouldReceive touch: UITouch) -> Bool
    {
        print("TOUCHED")
        if touch.view!.isDescendant(of: bgCView)
        {
            return false;
        }
        
        if(touch.view!.isDescendant(of: filterView))
        {
            return false;
        }
        
        if let innerView = self.view.viewWithTag(181) as? subVendorFilterView {
            if(touch.view!.isDescendant(of: innerView) )
            {
                return false;
            }
            return true;
        }
        
        
        return true;
        
        
    }
    
    
    
    
    
    
    @objc func applyButtonPressed(_ sender: UIButton){
        
        
        filters = "";
        filters += "{";
        filters += "\""+"first_name"+"\":\""+filterView.firstName.text!+"\",";
        filters += "\""+"last_name"+"\":\""+filterView.lastName.text!+"\",";
        filters += "\""+"email"+"\":\""+filterView.email.text!+"\",";
        
        if filterView.status.dropDownButton.currentTitle == "Pending"{
            filters += "\""+"status"+"\":\""+"0"+"\"}";
        }else if filterView.status.dropDownButton.currentTitle == "Approved"{
            filters += "\""+"status"+"\":\""+"1"+"\"}";
        }else if filterView.status.dropDownButton.currentTitle == "Disapproved"{
            filters += "\""+"status"+"\":\""+"2"+"\"}";
        }else{
            filters += "\""+"status"+"\":\""+""+"\"}";
        }
        
        
        
        pageNo=1
        subVendorData=[[String:JSON]]()
        flagForSubVendors=true
        loadSubVendor()
        self.view.viewWithTag(121)?.removeFromSuperview()
        self.view.viewWithTag(151)?.removeFromSuperview();
        
    }
    @objc func clearButtonPressed(_ sender: UIButton){
        filterView.firstName.text=""
        filterView.lastName.text=""
        filterView.email.text=""
        filterView.status.dropDownButton.setTitle("", for: .normal)
        filters=""
        pageNo=1
        subVendorData=[[String:JSON]]()
        flagForSubVendors=true
        loadSubVendor()
        
        self.view.viewWithTag(121)?.removeFromSuperview()
        self.view.viewWithTag(151)?.removeFromSuperview();
    }
    
    
    
    @objc func sendRequestButtonPressed(_ sender: UIButton){
        let vc=UIStoryboard(name: "subVendorStoryboard", bundle: nil).instantiateViewController(withIdentifier: "sendInvitationViewController") as! sendInvitationViewController
        self.navigationController?.pushViewController(vc, animated: true)
    }
    
    @objc func loadSubVendor(){
        
        if flagForSubVendors{
            let userData = UserDefaults.standard.object(forKey: "userInfoDict") as! NSDictionary
            let vendorId = userData["vendorId"] as! String
            let hashKey    = userData["hashKey"] as! String
            let postString = "vendor_id="+vendorId+"&hashkey="+hashKey+"&page=\(pageNo)&filter="+filters
            self.sendRequest(url: "vsubaccountapi/customer/item", parameters: postString)
        }
    }
    
    
    @objc func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return subVendorData.count
    }
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell=tableView.dequeueReusableCell(withIdentifier: "subVendorListingCell") as! subVendorListingCell
        
        let data=subVendorData[indexPath.row]
        
        cell.firstName.text=data["first_name"]?.stringValue
        cell.lastName.text=data["last_name"]?.stringValue
        cell.email.text=data["email"]?.stringValue
        if data["status"]?.stringValue=="0"{
            cell.status.text="Pending"
        }else if data["status"]?.stringValue=="1"{
            cell.status.text="Approved"
        }else if data["status"]?.stringValue=="2"{
            cell.status.text="Disapproved"
        }
        
        cell.approveButton.tag=Int((data["id"]?.stringValue)!)!
        cell.disapproveButton.tag=Int((data["id"]?.stringValue)!)!
        cell.deleteButton.tag=Int((data["id"]?.stringValue)!)!
        
        
        cell.approveButton.addTarget(self, action: #selector(approveButtonPressed(_:)), for: .touchUpInside)
        cell.disapproveButton.addTarget(self, action: #selector(disapproveButtonPressed(_:)), for: .touchUpInside)
        cell.deleteButton.addTarget(self, action: #selector(deleteButtonPressed(_:)), for: .touchUpInside)
        if indexPath.row==subVendorData.count-1{
            
            
        }
        return cell
        
    }
    @objc func approveButtonPressed(_ sender: UIButton){
        //vsubaccountapi/customer/approve
        let userData = UserDefaults.standard.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        let postString = "vendor_id="+vendorId+"&hashkey="+hashKey+"&sub_id=\(sender.tag)"
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        self.sendRequest(url: "vsubaccountapi/customer/approve", parameters: postString)
        
    }
    @objc func disapproveButtonPressed(_ sender: UIButton){
        //vsubaccountapi/customer/disapprove
        let userData = UserDefaults.standard.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        let postString = "vendor_id="+vendorId+"&hashkey="+hashKey+"&sub_id=\(sender.tag)"
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        self.sendRequest(url: "vsubaccountapi/customer/disapprove", parameters: postString)
    }
    @objc func deleteButtonPressed(_ sender: UIButton){
        //vsubaccountapi/customer/delete
        let userData = UserDefaults.standard.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        let postString = "vendor_id="+vendorId+"&hashkey="+hashKey+"&sub_id=\(sender.tag)"
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        self.sendRequest(url: "vsubaccountapi/customer/delete", parameters: postString)
    }
    
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 220
    }
    @objc func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let data=subVendorData[indexPath.row]
        
        
        
        let viewControl=UIStoryboard(name: "subVendorStoryboard", bundle: nil).instantiateViewController(withIdentifier: "cedsubVendorView") as! cedsubVendorView
        
        viewControl.id = (data["id"]?.stringValue)!
        
        self.navigationController?.pushViewController(viewControl, animated: true)
        //vsubaccountapi/customer/view
        /*let userData = UserDefaults.standard.object(forKey: "userInfoDict") as! NSDictionary
         let vendorId = userData["vendorId"] as! String
         let hashKey    = userData["hashKey"] as! String
         var postString = "vendor_id="+vendorId+"&hashkey="+hashKey
         postString+="&sub_id="+(data["id"]?.stringValue)!
         self.sendRequest(url: "vsubaccountapi/customer/view", parameters: postString)*/
        
    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @objc func scrollViewDidEndDragging(_ scrollView: UIScrollView, willDecelerate decelerate: Bool) {
        
        if flagForSubVendors{
            pageNo+=1
            Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
            loadSubVendor()
        }
        
    }
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        if let data = data{
            if requestUrl=="vsubaccountapi/customer/item"{
                guard let json = try? JSON(data: data) else {return}
                print(json)
                if json["data"]["success"].stringValue=="true"{
                    if let subaccountList=json["data"]["subaccount_list"].array{
                        for item in subaccountList{
                            subVendorData.append(item.dictionary!)
                        }
                    }
                }
                else{
                    //ShowSimpleAlert.showSimpleAlert(self, showTitle: "", showMsg: json["data"]["message"].stringValue)
                    self.tableView.setEmptyMessage(json["data"]["message"].stringValue)
                    flagForSubVendors=false
                    return
                }
                tableView.restore()
                tableView.reloadData()
                
            }
            else if requestUrl=="vsubaccountapi/customer/disapprove" || requestUrl=="vsubaccountapi/customer/approve" || requestUrl=="vsubaccountapi/customer/delete"{
                guard let json = try? JSON(data: data) else {return}
                
                print(json)
                if json["data"]["status"].stringValue=="true"{
                    ShowSimpleAlert.showSimpleAlert(self, showTitle: "", showMsg: json["data"]["message"].stringValue)
                    
                    
                    filters=""
                    pageNo=1
                    subVendorData=[[String:JSON]]()
                    flagForSubVendors=true
                    loadSubVendor()
                    
                }
            }
            else if requestUrl=="vsubaccountapi/customer/view"{
                guard let json = try? JSON(data: data) else {return}
                print(json)
            }
        }
        
    }
    /*
     // MARK: - Navigation
     
     // In a storyboard-based application, you will often want to do a little preparation before navigation
     override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
     // Get the new view controller using segue.destinationViewController.
     // Pass the selected object to the new view controller.
     }
     */
    
}

