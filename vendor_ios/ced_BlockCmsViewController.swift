//
//  ced_BlockCmsViewController.swift
//  VenderApp
//
//  Created by cedcoss on 06/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_BlockCmsViewController: ced_VendorBaseClass, UITableViewDelegate, UITableViewDataSource,UIGestureRecognizerDelegate,UITextViewDelegate {
    
    @IBOutlet weak var addCmsBlock: UIButton!
    @IBOutlet weak var filterButton: UIButton!
    @IBOutlet weak var maintitle: UILabel!
    @IBOutlet weak var tableView: UITableView!
    var pageLayout = [String:String]();
    let screenSize: CGRect = UIScreen.main.bounds
    var filter = String();
    var baseURL = String();
    var cmsData = [[String:String]]()
    var statusData = ""
    var value11 = ""
    var value12  = ""
    var value21 = ""
    var value22 = ""
    var value31  = ""
    var value32 = ""
    var value4 = ""
    var value5  = ""
    var value6 = ""
    var value7 = ""
    var page = 1;
    var loading = true
    var bgCView = UIView();
    let tempView=UIView() /////datepicke
    let pickerview = ced_datepicker()
    var from_Date_Filter = ""
    var datetextfeild = UITextView()
    var del_button_tag:Int = 0
    var isLoadedFrom = false
    override func viewDidLoad() {
        super.viewDidLoad()
        filterButton.setTitle("Continue".localized, for: UIControl.State());
        filterButton.addTarget(self, action: #selector(filterButtonPressed(_:)), for: UIControl.Event.touchUpInside);
        if isLoadedFrom
        {
            addCmsBlock.setTitle("Add Cms Page", for: UIControl.State())
        }
        else
        {
            addCmsBlock.setTitle("Add Cms Block", for: UIControl.State())
        }
        addCmsBlock.layer.cornerRadius=2
        addCmsBlock.tintColor = UIColor.white
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as! String
        addCmsBlock.backgroundColor =  Ced_CommonVendor.UIColorFromRGB(colorString)
        addCmsBlock.addTarget(self, action: #selector(SendData(_:)), for: .touchUpInside)
    }
    
    @objc func loadData(){
        
        if isLoadedFrom
        {
            baseURL =  "vcmsapi/vcmspage/listing"
        }
        else
        {
            baseURL = "vcmsapi/vcmsblock/listing"
        }
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        var postString = "vendor_id=" + vendorId + "&hashkey="+hashKey+"&page=\(page)"
        if(filter != "")
        {
            postString += "&filter="+filter
        }
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        self.sendRequest(url: baseURL, parameters: postString)
    }
    
    
    override func viewWillAppear(_ animated: Bool) {
        page = 1
        loadData()
    }
    
    
    @objc func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if cmsData.count != 0{
            return cmsData.count
        }
        else
        {
            return 0
        }
    }
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if isLoadedFrom
        {
            return 330
        }
        else
        {
            return 300
        }
    }
    
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        var colorString = Ced_CommonVendor.getInfoPlist("themecolor") as! String
        let cell=tableView.dequeueReusableCell(withIdentifier: "CmsBlockCell") as! ced_CmsBlockTableViewCell
        cell.Containerview.makeCard(cell.Containerview, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
        let approve = cmsData[indexPath.row]["is_approve"] ?? ""
        colorString = Ced_CommonVendor.getInfoPlist("themecolor") as! String
        if approve == "1"
        {
            cell.approved.text = "Approved"
            cell.approved.textColor = Ced_CommonVendor.UIColorFromRGB(colorString)
        }
        else
        {
            cell.approved.text = "Pending"
            cell.approved.textColor = Ced_CommonVendor.UIColorFromRGB(colorString)
        }
        if isLoadedFrom
        {
            cell.id.text = cmsData[indexPath.row]["page_id"]
            cell.Layout.text = cmsData[indexPath.row]["page_layout"]
        }
        else
        {
            cell.id.text = cmsData[indexPath.row]["block_id"]
        }
        let active = cmsData[indexPath.row]["is_active"]
        if(active == "1")
        {
            cell.status.text = "Enable"
            cell.status.textColor = Ced_CommonVendor.UIColorFromRGB(colorString)
            statusData = cell.status.text!
        }
        else
        {
            cell.status.text = "Disabled"
            cell.status.textColor = Ced_CommonVendor.UIColorFromRGB(colorString)
            statusData = cell.status.text!
        }
        if !isLoadedFrom
        {
            cell.LayoutHeigth.constant = 0
            cell.LayoutContent.constant = 0
        }
        cell.title.text = cmsData[indexPath.row]["title"]
        cell.urlKey.text = cmsData[indexPath.row]["identifier"]
        cell.created.text = cmsData[indexPath.row]["creation_time"]
        cell.lastModified.text = cmsData[indexPath.row]["update_time"]
        cell.view.cancelButton.backgroundColor =  Ced_CommonVendor.UIColorFromRGB(colorString)
        cell.view.cancelButton.setTitle("Edit", for: UIControl.State.normal)
        cell.view.cancelButton.layer.cornerRadius = 2
        cell.view.doneButton.layer.cornerRadius = 2
        cell.view.cancelButton.tintColor = UIColor.white
        cell.view.cancelButton.tag = indexPath.row
        cell.view.cancelButton.addTarget(self, action: #selector(EditButtonClicked(_:)), for: .touchUpInside)
        cell.view.doneButton.addTarget(self, action: #selector(deleteProductRequestConfirmation(_:)), for: .touchUpInside)
        cell.view.doneButton.backgroundColor = Ced_CommonVendor.UIColorFromRGB("#ff0000");
        cell.view.doneButton.tintColor = UIColor.white
        cell.view.doneButton.tag = indexPath.row
        cell.view.doneButton.setTitle("Delete", for: UIControl.State())
        return cell
    }
    @objc func EditButtonClicked(_ sender:UIButton)
    {
        if isLoadedFrom
        {
            let viewcontrol = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "AddCmsPage") as? ced_AddCmsPageViewController
            viewcontrol?.dataFlag = true
            viewcontrol?.pageLayout = pageLayout;
            viewcontrol?.page_id = cmsData[sender.tag]["page_id"]!
            self.navigationController?.pushViewController(viewcontrol!, animated: true)
        }
        else
        {
            let viewcontrol = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "AddCms") as? ced_AddCmsViewController
            viewcontrol?.backData = true
            viewcontrol?.id = cmsData[sender.tag]["block_id"]!
            self.navigationController?.pushViewController(viewcontrol!, animated: true)
        }
    }
    @objc func deleteProductRequestConfirmation(_ sender:UIButton)
    {
        let showTitle = "Confirmation".localized;
        let showMsg = "Are You Sure You Want To Delete This?";
        
        if #available(iOS 8.0, *) {
            let confirmationAlert = UIAlertController(title: showTitle, message: showMsg, preferredStyle: UIAlertController.Style.alert)
            confirmationAlert.addAction(UIAlertAction(title: "yes".localized, style: .default, handler: { (action: UIAlertAction!) in
                
                self.DeleteButtonClicked(sender);
                
            }));
            
            confirmationAlert.addAction(UIAlertAction(title: "No".localized, style: .default, handler: { (action: UIAlertAction!) in
                //print("Handle Cancel Logic here")
            }));
            
            present(confirmationAlert, animated: true, completion: nil)
        } else {
            // Fallback on earlier versions
        }
        
        
    }
    @objc func DeleteButtonClicked(_ sender:UIButton)
    {
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        if isLoadedFrom
        {
            let page_id = cmsData[sender.tag]["page_id"]!
            baseURL = "vcmsapi/vcmspage/delete"
            let postString1 = "page_id="+page_id+"&vendor_id="+vendorId+"&hashkey="+hashKey
            del_button_tag=sender.tag
            Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
            self.sendRequest(url: baseURL, parameters: postString1)
        }
        else
        {
            let block_id = cmsData[sender.tag]["block_id"]!
            baseURL = "vcmsapi/vcmsblock/delete"
            let postString1 = "block_id="+block_id+"&vendor_id="+vendorId+"&hashkey="+hashKey
            del_button_tag=sender.tag
            Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
            self.sendRequest(url: baseURL, parameters: postString1)
        }
    }
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        Alert_File.removeLoadingIndicator(self);
        if let data = data{
            if isLoadedFrom == false
            {
                if(requestUrl=="vcmsapi/vcmsblock/delete")
                {
                    guard let json = try? JSON(data: data) else {return}
                    
                    if(json["data"]["success"].stringValue.lowercased() == "false"){
                        self.view.showToastMsg(json["message"].stringValue)
                        
                    }else if(json["data"]["success"].stringValue.lowercased() == "true"){
                        self.view.makeToast(json["data"]["message"].stringValue, duration: 2.0, position: .center)
                        Ced_CommonVendor.delay(2.0, closure: { [weak self] in
                            let indexPath = IndexPath(item: self?.del_button_tag ?? 0, section: 0)
                            self?.cmsData.remove(at: self?.del_button_tag ?? 0)
                            
                            self?.tableView.deleteRows(at: [indexPath], with: UITableView.RowAnimation.right)
                            self?.reloadTableView()
                        })
                        //self.view.showToastMsg(json["data"]["message"].stringValue)
                        
                    }
                    
                }
                else
                {
                    guard let json = try? JSON(data: data) else {return}
                    //(json)
                    if(json["data"]["success"].stringValue.lowercased() == "false"){
                        
                        loading = false
                        if(page == 1)
                        {
                           // self.view.makeToast(json["data"]["message"].stringValue, duration: 2.0, position: .center)
                            self.tableView.setEmptyMessage(json["data"]["message"].stringValue)
                            //                            Ced_CommonVendor.delay(2.0, closure: {
                            //                                self.navigationController?.popViewController(animated: true)
                            //                            })
                        }
                        //page = 1
                    }else if(json["data"]["success"].stringValue.lowercased() == "true"){
                        cmsData = []
                        filter = ""
                        self.parseProductdata(json)
                        //self.parseJson()
                    }
                }
            }
            else
            {
                if(requestUrl=="vcmsapi/vcmspage/delete")
                {
                    guard let json = try? JSON(data: data) else {return}
                    //(json)
                    if(json["data"]["success"].stringValue.lowercased() == "false"){
                        self.view.showToastMsg(json["message"].stringValue)
                    }else if(json["data"]["success"].stringValue.lowercased() == "true"){
                        self.view.showToastMsg(json["data"]["message"].stringValue)
                        let indexPath = IndexPath(item: del_button_tag, section: 0)
                        self.cmsData.remove(at: del_button_tag)
                        Alert_File.removeLoadingIndicator(self);
                        self.tableView.deleteRows(at: [indexPath], with: UITableView.RowAnimation.middle)
                        reloadTableView()
                    }
                }
                else
                {
                    guard let json = try? JSON(data: data) else {return}
                    //(json)
                    if(json["data"]["success"].stringValue.lowercased() == "false"){
                        loading = false;
                        if(page == 1)
                        {
                           // self.view.makeToast(json["data"]["message"].stringValue, duration: 2.0, position: .center);
                            self.tableView.setEmptyMessage(json["data"]["message"].stringValue)
                            //                            Ced_CommonVendor.delay(2.0, closure: {
                            //                                self.navigationController?.popViewController(animated: true)
                            //                            })
                            
                        }
                        
                        
                    }else if(json["data"]["success"].stringValue.lowercased() == "true"){
                        cmsData = []
                        self.tableView.restore()
                        self.parseProductdata(json)
                        //self.parseJson()
                    }
                }
            }
            
            reloadTableView()
            
        }
    }
    
    @objc func reloadTableView(){
        self.tableView.delegate = self
        self.tableView.dataSource = self
        self.tableView.reloadData()
    }
    
    
    func parseProductdata(_ json:JSON){
        
        print(json)
        var page_layout = ""
        var updateTime = ""
        var creationTime = ""
        var id = ""
        var title = ""
        var is_active = ""
        var is_approve = ""
        var identifier = ""
        if isLoadedFrom
        {
            for(pagekey,pagevalue) in json["data"]["page_layout"]
            {
                pageLayout[pagevalue.stringValue] = pagekey
            }
            for result in json["data"]["lists"].arrayValue{
                if (result["identifier"].string != nil)
                {
                    identifier = (result["identifier"].string)!
                }
                if (result["page_id"].string != nil)
                {
                    id = (result["page_id"].string)!
                }
                if (result["update_time"].string != nil)
                {
                    updateTime = (result["update_time"].string)!
                }
                if (result["creation_time"].string != nil)
                {
                    creationTime = (result["creation_time"].string)!
                }
                if (result["title"].string != nil)
                {
                    title = (result["title"].string)!
                }
                if (result["is_active"].string != nil)
                {
                    is_active = (result["is_active"].string)!
                }
                if (result["is_approve"].string != nil)
                {
                    is_approve = (result["is_approve"].string)!
                }
                if (result["page_layout"].string != nil)
                {
                    page_layout = (result["page_layout"].string)!
                }
                //
                let temp = ["updated_at":updateTime,"creation_time":creationTime,"page_id":id,"title":title,"is_active":is_active,"is_approve":is_approve,"identifier":identifier,"page_layout":page_layout]
                //(temp)
                Alert_File.removeLoadingIndicator(self);
                self.cmsData.append(temp )
                
                reloadTableView()
            }
        }
        else
        {
            for result in json["data"]["items"].arrayValue{
                
                if (result["update_time"].string != nil)
                {
                    updateTime = (result["update_time"].string)!
                }
                if (result["creation_time"].string != nil)
                {
                    creationTime = (result["creation_time"].string)!
                }
                if (result["title"].string != nil)
                {
                    title = (result["title"].string)!
                }
                if (result["is_active"].string != nil)
                {
                    is_active = (result["is_active"].string)!
                }
                if (result["is_approve"].string != nil)
                {
                    is_approve = (result["is_approve"].string)!
                }
                let id = result["block_id"].string
                let identifier = result["identifier"].string
                let temp = ["update_time":updateTime,"creation_time":creationTime,"block_id":id,"title":title,"is_active":is_active,"is_approve":is_approve,"identifier":identifier]
                Alert_File.removeLoadingIndicator(self)
                self.cmsData.append(temp as! [String : String])
                //(cmsData)
                reloadTableView()
            }
        }
    }
    let filterView = CmsPageFilter()
    @objc func filterButtonPressed(_ sender:UIButton)
    {
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        bgCView.tag=151;
        bgCView.frame = UIScreen.main.bounds;
        bgCView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        bgCView.backgroundColor = UIColor(red:0, green:0, blue:0, alpha:0.5)
        self.addgesturesTohideView(self.view)
        filterView.tag = 181
        filterView.backgroundColor = UIColor.black
        if isLoadedFrom
        {
            filterView.frame = CGRect(x: 0, y: 0, width: self.view.frame.width - 20, height: 340);
        }
        else
        {
            filterView.frame = CGRect(x: 0, y: 0, width: self.view.frame.width - 20, height: 315);
        }
        filterView.center.x = self.view.center.x
        filterView.center.y = self.view.center.y+20
        filterView.leftButton.backgroundColor = color
        filterView.rightButton.backgroundColor = color
        filterView.label1.text = "Product ID"
        filterView.label2.text = "Last Modified"
        filterView.label3.text = "Created"
        filterView.label4.text = "Product Title"
        filterView.label5.text = "Url Key"
        filterView.label6.text = "Product Status"
        filterView.value6.tag = 180
        filterView.value7.tag = 190
        filterView.value6.addTarget(self, action: #selector(showProductStatusDropdown(_:)), for: UIControl.Event.touchUpInside);
        filterView.label7.text = "Layout";
        filterView.value7.setTitle(value7, for: UIControl.State());
        filterView.value7.addTarget(self, action: #selector(showProductStatusDropdown(_:)), for: UIControl.Event.touchUpInside);
        
        filterView.value6.layer.borderColor = UIColor(red: 0.9, green: 0.9, blue: 0.9, alpha: 1.0).cgColor
        filterView.value6.layer.borderWidth = 1.0
        filterView.value6.layer.cornerRadius = 5
        
        if isLoadedFrom != true
        {
            filterView.LayoutHeibht.constant = 0
            filterView.LayoutButton.constant = 0
        }
        // filter.datepicker.sele
        filterView.value11.text = value11;
        //      filter.value12.text = value12;
        filterView.value21.text = value21;
        filterView.value22.text = value22;
        filterView.value31.text = value31;
        filterView.value32.text = value32;
        filterView.value4.text = value4;
        filterView.value5.text = value5;
        
        PopupLookImprovement.designTextView(filterView.value11);
        PopupLookImprovement.designTextView(filterView.value21);
        PopupLookImprovement.designTextView(filterView.value22);
        PopupLookImprovement.designTextView(filterView.value31);
        PopupLookImprovement.designTextView(filterView.value32);
        PopupLookImprovement.designTextView(filterView.value4);
        PopupLookImprovement.designTextView(filterView.value5);
        
        filterView.leftButton.setTitle("Filter", for: UIControl.State());
        // filter.leftButton.addTarget(self, action: #selector(applyFilterTapped(_:)), for: UIControlEvents.touchUpInside);
        filterView.value21.delegate = self
        filterView.value21.tag=11
        filterView.value22.delegate = self
        filterView.value22.tag=12
        filterView.value31.delegate = self
        filterView.value31.tag=13
        filterView.value32.delegate = self
        filterView.value32.tag=14
        filterView.leftButton.addTarget(self, action: #selector(applyFilterTapp(_:)), for: .touchUpInside)
        filterView.rightButton.addTarget(self, action: #selector(resetFilterTapped(_:)), for: .touchUpInside)
        filterView.rightButton.setTitle("Reset", for: UIControl.State());
        //   pickerview.date.addTarget(self, action:#selector(datePickerChanged(_:)) , for: .valueChanged)
        //for: .touchUpInside)
        self.addgesturesTohideView(filterView)
        bgCView.addSubview(filterView)
        self.view.addSubview(bgCView);
        
    }
    var datePickerView = UIDatePicker();
    var textFieldForDate=UITextView()
    @objc func textViewDidBeginEditing(_ textView: UITextView) {
        datePickerView = UIDatePicker()
        datePickerView.locale = Locale(identifier: ced_storeVC.selectLangauge)
        textView.resignFirstResponder()
        self.textFieldForDate=textView
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
        tempInrView.tag=131
        tempInrView.frame = CGRect(x: 0, y: CGFloat(0), width: screenSize.width-50,height: CGFloat(270));
        tempInrView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth]
        tempInrView.backgroundColor = UIColor.white
        tempInrView.center = CGPoint(x: bgCView.frame.size.width  / 2,y: bgCView.frame.size.height / 2)
        let label = UILabel()
        label.frame = CGRect(x: 0, y: CGFloat(0), width: tempInrView.frame.width,height: CGFloat(30))
        label.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth]
        label.text = "Select Date".localized.uppercased()
        label.textAlignment = NSTextAlignment.center
        label.textColor = UIColor.white
        label.backgroundColor = color
        tempInrView.addSubview(label)
        if textView.tag == 12
        {
            if filterView.value21.text != ""
            {
                print(filterView.value21.text)
                let isoDate = filterView.value21.text
                let dateFormatter = DateFormatter()
                dateFormatter.dateFormat = "yyyy-MM-dd"
                dateFormatter.locale = Locale(identifier: "en_US_POSIX") // set locale to reliable US_POSIX
                let date = dateFormatter.date(from:isoDate!)!
                let calendar = Calendar.current
                let components = calendar.dateComponents([.year, .month, .day, .hour], from: date)
                let finalDate = calendar.date(from:components)
                datePickerView.minimumDate = finalDate
            }
            else
            {
                ShowSimpleAlert.showSimpleAlert(self, showTitle: "Information", showMsg: "Please Select the  limit of the date")
                return
            }
        }
        else if textView.tag == 14
        {
            let isoDate = filterView.value31.text
            let dateFormatter = DateFormatter()
            dateFormatter.dateFormat = "yyyy-MM-dd"
            dateFormatter.locale = Locale(identifier: "en_US_POSIX") // set locale to reliable US_POSIX
            let date = dateFormatter.date(from:isoDate ?? "") ?? Date()
            let calendar = Calendar.current
            let components = calendar.dateComponents([.year, .month, .day, .hour], from: date)
            let finalDate = calendar.date(from:components)
            datePickerView.minimumDate = finalDate
        }
        datePickerView.maximumDate = Date()
        datePickerView.datePickerMode = UIDatePicker.Mode.date
        datePickerView.setValue(UIColor.black, forKeyPath: "textColor")
        datePickerView.backgroundColor = UIColor.white
        datePickerView.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin]
        datePickerView.frame = CGRect(x: 0, y: CGFloat(30), width: tempInrView.frame.width,height: CGFloat(200))
        tempInrView.addSubview(datePickerView)
        let twoButtonView = TwoButtonView()
        twoButtonView.tag=141
        twoButtonView.frame = CGRect(x: 0, y: CGFloat(230), width: tempInrView.frame.width,height: CGFloat(40))
        twoButtonView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth]
        tempInrView.addSubview(twoButtonView)
        twoButtonView.cancelButton.addTarget(self, action: #selector(cancelDatePickerView(_:)), for: UIControl.Event.touchUpInside)
        twoButtonView.cancelButton.backgroundColor = colorRed
        twoButtonView.doneButton.addTarget(self, action: #selector(pickDateFromDatePickerView(_:)), for: UIControl.Event.touchUpInside)
        twoButtonView.doneButton.backgroundColor = colorGreen
        tempInrView.makeCardUsingThemeColor(tempInrView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
        bgCView.addSubview(tempInrView)
        self.view.addSubview(bgCView)
    }
    
    @objc func cancelDatePickerView(_ sender:UIButton)
    {
        
        
        
        
        textFieldForDate.resignFirstResponder();
        textFieldForDate.text = ""
        self.view.viewWithTag(161)?.removeFromSuperview();
    }
    
    
    @objc func pickDateFromDatePickerView(_ sender:UIButton)
    {
        let dateFormatter = DateFormatter()
        dateFormatter.locale = Locale(identifier: "en_US_POSIX")
        dateFormatter.dateStyle = DateFormatter.Style.short
        dateFormatter.timeStyle = DateFormatter.Style.none
        dateFormatter.dateFormat = "yyyy-MM-dd";
        textFieldForDate.text = dateFormatter.string(from: datePickerView.date);
        textFieldForDate.resignFirstResponder();
        self.view.viewWithTag(161)?.removeFromSuperview();
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
        if(touch.view!.isDescendant(of: filterView))
        {
            return false;
        }
        if let innerView = self.view.viewWithTag(181) as? CmsPageFilter {
            if(touch.view!.isDescendant(of: innerView))
            {
                return false;
            }
            return true;
        }
        return true;
    }
    @objc func showProductStatusDropdown(_ sender:UIButton)
    {
        let dropDown = DropDown()
        if sender.tag == 180
        {
            dropDown.dataSource = ["Enable","Disable"]
        }
        else
        {
            dropDown.dataSource = Array(pageLayout.keys)
            //dropDown.dataSource = ["Vendor Empty Layout","Vendor Panel Layout","1 Column","2 Columns with left bar","2 Columns with right bar","3 Columns","Empty"]
        }
        dropDown.selectionAction = {(index, item) in
            sender.setTitle(item, for: UIControl.State())
        }
        dropDown.anchorView = sender
        dropDown.bottomOffset = CGPoint(x: 0, y:sender.bounds.height)
        if dropDown.isHidden {
            dropDown.show()
        } else {
            dropDown.hide()
        }
    }
    @objc func applyFilterTapp(_ sender:UIButton)
    {
        //("applyFilterTapped");
        
        if let view = self.view.viewWithTag(181) as? CmsPageFilter
        {
            if(view.value11.text != nil){
                value11 =  view.value11.text!
                print(value11)
            }
            if(view.value21.text != nil){
                value21 = view.value21.text!
                print(value21)
            }
            if(view.value22.text != nil){
                value22 = view.value22.text!
                //(value22)
            }
            if(view.value31.text != nil){
                value31 = view.value31.text!
                print(value31)
            }
            if(view.value32.text != nil){
                value32 = view.value32.text!
                print(value32)
            }
            if(view.value4.text != nil){
                value4 = view.value4.text!
                print(value4)
            }
            if(view.value5.text != nil){
                value5 = view.value5.text!
                print(value5)
            }
            if(view.value6.titleLabel!.text != nil){
                value6 = view.value6.titleLabel!.text!
                //(value6)
                if value6 == "Enable"
                {
                    value6 = "1"
                }
                else
                {
                    value6 = "0"
                }
            }
            if isLoadedFrom
            {
                if(view.value7.titleLabel!.text != nil){
                    value7 = view.value7.titleLabel!.text!
                }
            }
            if isLoadedFrom
            {
                filter = "";
                filter += "{";
                filter += "\""+"page_id"+"\":\""+value11+"\",";
                filter += "\""+"update_time"+"\":{\"from\":\""+value21+"\",\"to\":\""+value22+"\"},";
                filter += "\""+"creation_time"+"\":{\"from\":\""+value31+"\",\"to\":\""+value32+"\"},";
                filter += "\""+"title"+"\":\""+value4+"\",";
                filter += "\""+"identifier"+"\":\""+value5+"\",";
                if(value7 != "")
                {
                    
                    filter += "\""+"page_layout"+"\":\""+pageLayout[value7]!+"\",";
                }
                else
                {
                    filter += "\""+"page_layout"+"\":\""+""+"\",";
                }
                
                filter += "\""+"is_active"+"\":\""+value6;
                
                filter += "\"}";
            }
            else
            {
                filter = "";
                filter += "{";
                filter += "\""+"block_id"+"\":\""+value11+"\",";
                filter += "\""+"update_time"+"\":{\"from\":\""+value21+"\",\"to\":\""+value22+"\"},";
                filter += "\""+"creation_time"+"\":{\"from\":\""+value31+"\",\"to\":\""+value32+"\"},";
                filter += "\""+"title"+"\":\""+value4+"\",";
                filter += "\""+"identifier"+"\":\""+value5+"\",";
                filter += "\""+"is_active"+"\":\""+value6+"\"}";
            }
            print(filter)
            self.view.viewWithTag(151)?.removeFromSuperview();
            self.page = 1;
            cmsData = []
            loadData()
            bgCView = UIView()
        }
    }
    @objc func resetFilterTapped(_ sender:UIButton)
    {
        value11 = ""
        value12  = ""
        value21 = ""
        value22 = ""
        value31  = ""
        value32 = ""
        value4 = ""
        value5  = ""
        value6 = ""
        value7 = ""
        filter = String()
        self.page = 1
        self.view.viewWithTag(151)?.removeFromSuperview();
        self.loadData();
    }
    
    
    @objc func SendData(_ sender:UIButton)
    {
        if isLoadedFrom
        {
            let viewcontrol = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "AddCmsPage") as? ced_AddCmsPageViewController
            self.navigationController?.pushViewController(viewcontrol!, animated: true)
        }
        else
        {
            let viewcontrol = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "AddCms") as? ced_AddCmsViewController
            self.navigationController?.pushViewController(viewcontrol!, animated: true)
        }
        
    }
    @objc func scrollViewDidEndDragging(_ scrollView: UIScrollView, willDecelerate decelerate: Bool) {
        
        let currentOffset = scrollView.contentOffset.y
        let maximumOffset = scrollView.contentSize.height - scrollView.frame.size.height
        if (maximumOffset - currentOffset) <= 40 {
            if loading{
                self.page += 1;
                self.loadData()
                tableView.reloadData();
            }
        }
    }
}
