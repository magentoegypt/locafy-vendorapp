

//
//  ced_MainDrawerNew.swift
//  VenderApp
//
//  Created by Macmini on 25/12/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit
//ced_MainDrawerNew
class ced_MainDrawerNew: UIViewController,UITableViewDelegate,UITableViewDataSource,UIGestureRecognizerDelegate{
    
    //    ,UITableViewDelegate,UITableViewDataSource,UIGestureRecognizerDelegate
    @IBOutlet weak var drawerTable: UITableView!
    var previouslySelectedHeaderIndex: Int?
    var selectedHeaderIndex: Int?
    var selectedItemIndex: Int?
    var dataSource = expandingCells()
    var data = [String]()
    let defaults = UserDefaults.standard
    let screenSize = UIScreen.main.bounds
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
    }
    override func viewWillAppear(_ animated: Bool) {
        dataSource = expandingCells()
        setUpTheDrawerData()
        drawerTable.reloadData()
    }
    
    
    
    
    @objc func setUpTheDrawerData(){
        getDataFromDefault(fromkey: "cedNavTransact", for: "Transaction")
        getDataFromDefault(fromkey: "cedNavMemberShipPlans", for: "Membership Plans")
        getDataFromDefault(fromkey: "cedNavReport", for: "Reports")
        getDataFromDefault(fromkey: "cedNavSett", for: "Settings")
        getDataFromDefault(fromkey: "cedNavOrder", for: "Orders")
        getDataFromDefault(fromkey: "cedCMSData", for: "Vendor CMS")
        getDataFromDefault(fromkey: "cedAttrData", for: "Product Attributes")
        addtionalDefaultData(from: ["Create Deals","List Deals","Deals Setting"], fromkey: "Ced_VDealApi", for: "Vendor Deals")
        addtionalDefaultData(from: ["Out Of Stock Products","Product Views","Product Sales","Payment Report","Return Report"], fromkey: "Ced_VReportApi", for: "Advance Report")
      //  addtionalDefaultData(from: ["Out Of Stock Products","Sold Products","Product Views","Product Sales","Payment Report","Return Report"], fromkey: "Ced_VReportApi", for: "Advance Report")
        addtionalDefaultData(from: ["Quotations List","Assigned Quotations"], fromkey: "Ced_VPoApi", for: "PO Quotations")
        addtionalDefaultData(from: ["Quotations List","Assigned Quotations"], fromkey: "Ced_VOrderApi", for: "PO Quotations")
        addtionalDefaultData(from: ["Add Auction","List Auction"], fromkey: "Ced_VAuctionApi", for: "Auction")
        getDataFromDefault(fromkey: "cedSelectSell", for: "Select and Sell")
        addtionalDefaultData(from:  ["Manage RMA Request"], fromkey: "Ced_VRmaApi", for: "RMA")
        addtionalDefaultData(from:  ["ManageQuotes","View Po"], fromkey: "Ced_VRfqApi", for: "Request For Quotation")
        addtionalDefaultData(from:  ["Catalog Price Rules","Shopping Cart Price Rules"], fromkey: "Ced_Vpro", for: "Promotions")
        addtionalDefaultData(from:["View Stores"], fromkey: "Ced_VStorepickupApi", for: "Store Pickup")
        addtionalDefaultData(from:["Vendor Admin","Vendor Customer"], fromkey: "Ced_VMessagingApi", for: "Messaging")
        if let subProfileIndex = data.index(of: "Sub-Vendor Profile"){
            if let profileIndex = data.index(of: "Vendor Profile") {
                data.remove(at: subProfileIndex)
            }
        }
        drawerTable.delegate = self
        drawerTable.dataSource = self
        drawerTable.reloadData()
    }
    
    
    fileprivate func getDataFromDefault(fromkey value :String,for headerValue:String) {
        print(value)
        print(headerValue)
        dataSource.append(expandingCells.headerValues(value: headerValue, image: "", cate: "", haschild: "true"))
        if let  MemberShip = defaults.value(forKey: value) as? [String]
        {
            for items in MemberShip{
                dataSource.append(expandingCells.cell(value: items, imageUrl: "", cateId: "", hasChild: "false"))
            }
        }
    }
    
    @objc func addtionalDefaultData(from arrayOfData :[String],fromkey value :String,for headerValue:String){
        
        if Ced_CommonVendor().checkModule("Ced_VDealApi"){
            dataSource.append(expandingCells.headerValues(value: headerValue, image: "", cate: "", haschild: "true"))
            for items in arrayOfData{
                dataSource.append(expandingCells.cell(value: items, imageUrl: "", cateId: "", hasChild: "false"))
            }
        }
    }
    
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        if(indexPath.row == 0){
            let cell = tableView.dequeueReusableCell(withIdentifier: "profileCell") as? ced_mainDrawerprofileCell
            let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
            let vendorName = userData["vendorName"] as? String
            cell?.label1.text = "Hello,".localized + " \(vendorName!)"
            cell?.label1.font=UIFont(name: "Roboto-Bold", size: 17)
            let url = userData["profilePicUrl"] as? String
            print(url)
            let sessionConfig = URLSessionConfiguration.default
//            sessionConfig.httpAdditionalHeaders = [
//                "Authorization": "Basic \(Ced_CommonVendor.getAuthData())",
//            ]
            sessionConfig.timeoutIntervalForRequest = timeoutIntervalForRequest
            sessionConfig.timeoutIntervalForResource = timeoutIntervalForResource
            let session = URLSession(configuration: sessionConfig)
            let request = session.dataTask(with: URL(string: url!)!, completionHandler: {
                data,error,response in
                DispatchQueue.main.async
                    {
                        if let data = data{
                            let image = UIImage(data: data)
                            
                            cell?.imageViee.image = image
                            cell?.imageViee.layer.cornerRadius =  cell!.imageViee.frame.size.width/2
                            cell?.imageViee.clipsToBounds = true
                            cell?.imageViee.layer.masksToBounds = true
                            cell?.imageViee.layer.borderWidth = 3
                            cell?.imageViee.layer.borderColor = UIColor.white.cgColor
                            cell?.imageViee.isUserInteractionEnabled = true;
                            let tapRecognizer = UITapGestureRecognizer(target: self, action: #selector(self.imageTapped(_:)))
                            cell?.imageViee.addGestureRecognizer(tapRecognizer);
                        }
                        
                }
            })
            request.resume()
            return cell!
        }else if (indexPath.section == 1){
            let item = self.dataSource.totalitems[(indexPath as NSIndexPath).row]
            let value = item.textValue
            _ = item.imageUrl
            
            if let cell = tableView.dequeueReusableCell(withIdentifier: "account_cell") as? accountcell {
                
                if item as? expandingCells.headerValues != nil {
                    let label = cell.viewWithTag(123) as? UILabel
                    label?.text = value
                    label?.font = UIFont.systemFont(ofSize: 14)
                    if item.hasChild != "false" {
                        
                        cell.rightView.isHidden = false
                        let img1=cell.rightView.image
                        if ced_storeVC.selectLangauge == "ar"
                        {
                            cell.rightView.image=UIImage(cgImage: (img1?.cgImage)!, scale: (img1?.scale)!, orientation: UIImage.Orientation.upMirrored)
                        }
                        else if ced_storeVC.selectLangauge == "en"
                        {
                            cell.rightView.image=UIImage(cgImage: (img1?.cgImage)!, scale: (img1?.scale)!, orientation: UIImage.Orientation.downMirrored)
                        }
                        cell.cell_view.backgroundColor = .white
                    }else{
                        cell.rightView.isHidden = true
                        if  item.textValue=="View All Categories"{
                            //                            cell.cell_view.setThemeColor()
                            label?.textColor=UIColor.white
                        }else{
                            //                             label?.textColor=UIColor.black
                            cell.cell_view.backgroundColor = .white
                        }
                    }
                    cell.ImageView.isHidden = true
                    return cell
                    
                }else{
                    let subMenuCell = tableView.dequeueReusableCell(withIdentifier: "subMenuCell") as? accountcell
                    let label = subMenuCell?.viewWithTag(1214) as? UILabel
                    label?.text = value
                    return subMenuCell!
                }
                
            }
        }
        return UITableViewCell()
    }
    
    
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if(indexPath.section == 0){
            if(indexPath.row == 0){
                return 205
            }
        }else{
            let item = self.dataSource.totalitems[(indexPath as NSIndexPath).row]
            if item is expandingCells.headerValues {
                return 45
            } else if (item.hidden) {
                return 0
            } else {
                return 35
            }
        }
        return 0
        
    }
    
    
    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if section == 0{
            return 1
        }else if section == 1{
            return dataSource.totalitems.count
        }else{
            return 0
        }
    }
    
    @objc func numberOfSections(in tableView: UITableView) -> Int {
        return 2
    }
    
    
    @objc func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let navigation = self.sideDrawerViewController?.mainViewController as! ced_navigationBarController
        
        if(indexPath.section == 0){
            self.sideDrawerViewController?.toggleDrawer()
            let viewControl = storyboard?.instantiateViewController(withIdentifier: "dashRootView") as! UINavigationController
            self.sideDrawerViewController?.transitionFromMainViewController(viewControl, duration: 1, options: UIView.AnimationOptions.transitionFlipFromTop, animations: nil, completion: nil)
            return
        }
        else if(indexPath.section == 1){
            let item = self.dataSource.totalitems[(indexPath as NSIndexPath).row]
            print(item)
            if item is expandingCells.headerValues {
                let cell = drawerTable.cellForRow(at: indexPath) as? accountcell
                if self.selectedHeaderIndex == nil {
                    self.selectedHeaderIndex = (indexPath as NSIndexPath).row
                } else {
                    self.previouslySelectedHeaderIndex = self.selectedHeaderIndex
                    self.selectedHeaderIndex = (indexPath as NSIndexPath).row
                }
                if let previouslySelectedHeaderIndex = self.previouslySelectedHeaderIndex {
                    cell?.rightView.image = UIImage(named:"IQButtonBarArrowRight")
                    let indexPath = NSIndexPath(row: previouslySelectedHeaderIndex, section: 1)
                    let prevcell = self.drawerTable.cellForRow(at: indexPath as IndexPath) as? accountcell
                    let img1=UIImage(named:"IQButtonBarArrowRight")
                    
                    if ced_storeVC.selectLangauge == "ar"
                    {
                        cell?.rightView.image=UIImage(cgImage: (img1?.cgImage)!, scale: (img1?.scale)!, orientation: UIImage.Orientation.upMirrored)
                        prevcell?.rightView.image=UIImage(cgImage: (img1?.cgImage)!, scale: (img1?.scale)!, orientation: UIImage.Orientation.upMirrored)
                    }
                    else if (ced_storeVC.selectLangauge == "en")
                    {
                        cell?.rightView.image=UIImage(cgImage: (img1?.cgImage)!, scale: (img1?.scale)!, orientation: UIImage.Orientation.downMirrored)
                        prevcell?.rightView.image=UIImage(cgImage: (img1?.cgImage)!, scale: (img1?.scale)!, orientation: UIImage.Orientation.downMirrored)
                    }
                    prevcell?.lineView.isHidden = false
                    cell?.lineView.isHidden = false
                    self.dataSource.collapse(previouslySelectedHeaderIndex)
                }
                
                if self.previouslySelectedHeaderIndex != self.selectedHeaderIndex {
                    cell?.rightView.image = UIImage(named:"IQButtonBarArrowDown")
                    cell?.lineView.isHidden = true
                    self.dataSource.expand(self.selectedHeaderIndex!)
                } else {
                    self.selectedHeaderIndex = nil
                    self.previouslySelectedHeaderIndex = nil
                }
                self.drawerTable.beginUpdates()
                self.drawerTable.endUpdates()
                
            } else {
                self.sideDrawerViewController?.toggleDrawer()
                makePushEventHere()
            }
            
        }
        
    }
    
    
    
    @objc func makePushEventHere(){
        print("Some Things pressed")
    }
    
    
    
    @objc func imageTapped(_ gestureRecognizer: UITapGestureRecognizer)
    {
        /* background view */
        let bgCView = UIView();
        bgCView.tag=121;
        bgCView.frame = UIScreen.main.bounds;
        bgCView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        bgCView.backgroundColor = UIColor(red:0, green:0, blue:0, alpha:0.5);
        
        self.addgesturesTohideView(self.view);
        
        let imgPopupView = UIImageView();
        imgPopupView.tag = 131;
        imgPopupView.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
        imgPopupView.frame = CGRect(x: 0, y: CGFloat(0), width: screenSize.width/2,height: screenSize.height/2);
        imgPopupView.center = CGPoint(x: bgCView.frame.size.width  / 2,
                                      y: bgCView.frame.size.height / 2);
        let tappedImageView = gestureRecognizer.view as! UIImageView;
        imgPopupView.image = tappedImageView.image;
        
        bgCView.addSubview(imgPopupView);
        self.view.addSubview(bgCView);
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
    
    //@objc function to dismiss the custom popover
    @objc func hideView(_ recognizer: UITapGestureRecognizer)
    {
        self.view.viewWithTag(121)?.removeFromSuperview()
        
        
    }
    
}
