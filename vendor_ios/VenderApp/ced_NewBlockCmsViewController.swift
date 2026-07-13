//
//  ced_NewBlockCmsViewController.swift
//  VenderApp
//
//  Created by cedcoss on 6/24/21.
//  Copyright © 2021 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit
import RxCocoa
import RxSwift

class ced_NewBlockCmsViewController: ced_VendorBaseClass, UIGestureRecognizerDelegate, UITextViewDelegate  {
  
    
    @IBOutlet weak var FilterBttn: UIButton!
    @IBOutlet weak var AddCmsBlockBttn: UIButton!
    @IBOutlet weak var MainTable: UITableView!
    
    let disposeBag = DisposeBag()
    let filterView = CmsPageFilter()
    var bgCView = UIView();
    let tempView=UIView() /////datepicke
    let pickerview = ced_datepicker()
    
    var isLoadedFrom = false
    var storeData = [[String:String]]()
    var defaultView = false
    var defaultStore = false
    var baseURL = String();
    var array = [String]()
    var page = 1;
    var filter = String();
    var cmsData = [[String:String]]()
    var JsonData = [JSON]()
    var pageLayout = [String:String]();
    var del_button_tag:Int = 0
    
    
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
    
    var newfilter = [String:Any]()
    var isactive = Bool()
    
    var layoutdata = [String:String]()
    
    
    var statusdata = [String:String]()
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        
        if isLoadedFrom
        {
            AddCmsBlockBttn.setTitle("Add Cms Page".localized, for: UIControl.State())
        }
        else
        {
            AddCmsBlockBttn.setTitle("Add Cms Block".localized, for: UIControl.State())
        }
        
        AddCmsBlockBttn.layer.cornerRadius=2
        AddCmsBlockBttn.tintColor = UIColor.white
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as! String
        AddCmsBlockBttn.backgroundColor =  Ced_CommonVendor.UIColorFromRGB(colorString)
        AddCmsBlockBttn.addTarget(self, action: #selector(SendData(_:)), for: .touchUpInside)
        
        FilterBttn.setImage(UIImage(named: "filtericon"), for: .normal)
        FilterBttn.addTarget(self, action: #selector(filterButtonPressed(_:)), for: UIControl.Event.touchUpInside);
        // Do any additional setup after loading the view.
    }
    
    override func viewWillAppear(_ animated: Bool) {
        page = 1
        
        MainTable.delegate=self
        MainTable.dataSource=self
        loadData()
        filterdata()
    }
    
    @objc func loadData(){
        
        var para = [String:String]()
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        
        if isLoadedFrom
        {
            baseURL =  "rest/all/V1/vcmspage/listing"
            para["vendor_id"] = vendorId
            para["page"] = "\(page)"
        }
        else
        {
            baseURL = "rest/all/V1/vcmsblock/listing"
            para["vendor_id"] = vendorId
            para["page"] = "\(page)"
        }
       
//
//        var postString = "vendor_id=" + vendorId + "&hashkey="+hashKey+"&page_id=\(page)"
//        var params = "data=" + postString
//        if(filter != "")
//        {
//            params += "&filter="+filter
//
//        }
        
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
        self.sendRequestCms(url: baseURL, params: para, filter:newfilter)
       // self.sendRequest(url: baseURL, parameters: params)
    }
    
    //    MARK:-  Filter View
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
        filterView.label1.text = "Page Id".localized
        filterView.label2.text = "Last Modified".localized
        filterView.label3.text = "Created".localized
        filterView.label4.text = "Title".localized
        filterView.label5.text = "Url Key".localized
        filterView.label6.text = "Status".localized
        filterView.value6.tag = 180
        filterView.value7.tag = 190
        filterView.value6.addTarget(self, action: #selector(showProductStatusDropdown(_:)), for: UIControl.Event.touchUpInside);
        filterView.label7.text = "Layout".localized;
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
        
        filterView.leftButton.setTitle("Filter".localized, for: UIControl.State());
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
        filterView.rightButton.setTitle("Reset".localized, for: UIControl.State());
        //   pickerview.date.addTarget(self, action:#selector(datePickerChanged(_:)) , for: .valueChanged)
        //for: .touchUpInside)
        self.addgesturesTohideView(filterView)
        bgCView.addSubview(filterView)
        self.view.addSubview(bgCView);
        
    }
     
    @objc func applyFilterTapp(_ sender:UIButton)
    {
        if let view = self.view.viewWithTag(181) as? CmsPageFilter
        {
            if(view.value11.text != nil && view.value11.text != ""){
                if isLoadedFrom{
                    value11 =  view.value11.text!
                    print(value11)
                    
                    newfilter["page_id"]=value11
                }else{
                    value11 =  view.value11.text!
                    print(value11)
                    
                    newfilter["block_id"]=value11
                }
               
            }
            var updatetime = [String:String]()
            var createdtime = [String:String]()
           
            
          
            if(view.value21.text != nil && view.value21.text != ""){
                value21 = view.value21.text!
                updatetime["from"]=value21
                print(value21)
            }
            if(view.value22.text != nil && view.value22.text != ""){
                value22 = view.value22.text!
                updatetime["to"]=value22
                newfilter["updated_at"]=updatetime
                //(value22)
            }
            if(view.value31.text != nil && view.value31.text != ""){
                value31 = view.value31.text!
                createdtime["from"]=value31
                print(value31)
            }
            if(view.value32.text != nil && view.value32.text != ""){
                value32 = view.value32.text!
                createdtime["to"]=value32
                newfilter["created_at"]=createdtime
                print(value32)
            }
            if(view.value4.text != nil && view.value4.text != ""){
                value4 = view.value4.text!
                newfilter["title"]=value4
                print(value4)
            }
            if(view.value5.text != nil && view.value5.text != ""){
                value5 = view.value5.text!
                newfilter["identifier"]=value5
                print(value5)
            }
            if(view.value6.titleLabel!.text != nil && view.value6.titleLabel!.text != ""){
                value6 = view.value6.titleLabel!.text!
                //(value6)
                newfilter["is_active"] = statusdata[value6]
                value6 = statusdata[value6]!
//                if value6 == "Enabled"
//                {
//                    value6 = "1"
//                    isactive=true
//                    newfilter["is_active"]=isactive
//                }
//                else
//                {
//                    value6 = "0"
//                    isactive=false
//                    newfilter["is_active"]=isactive
//                }
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
                filter += "\""+"updated_at"+"\":{\"from\":\""+value21+"\",\"to\":\""+value22+"\"},";
                filter += "\""+"creation_time"+"\":{\"from\":\""+value31+"\",\"to\":\""+value32+"\"},";
                filter += "\""+"title"+"\":\""+value4+"\",";
                filter += "\""+"identifier"+"\":\""+value5+"\",";
                if(view.value7.titleLabel!.text != nil && view.value7.titleLabel!.text != "")
                {
                    value7 = view.value7.titleLabel!.text!
                    print(value7)
                    print(layoutdata)
//                    value7 = layoutdata[value7] ?? ""
                    filter += "\""+"page_layout"+"\":\""+value7+"\",";
                    newfilter["page_layout"]=layoutdata[value7]
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
                filter += "\""+"updated_at"+"\":{\"from\":\""+value21+"\",\"to\":\""+value22+"\"},";
                filter += "\""+"created_at"+"\":{\"from\":\""+value31+"\",\"to\":\""+value32+"\"},";
                filter += "\""+"title"+"\":\""+value4+"\",";
                filter += "\""+"identifier"+"\":\""+value5+"\",";
                filter += "\""+"is_active"+"\":\""+value6+"\"}";
            }
            print(filter)
            self.view.viewWithTag(151)?.removeFromSuperview();
            self.page = 1;
            cmsData = []
            JsonData = []
            MainTable.reloadData()
            self.loadData()
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
        newfilter.removeAll()
        JsonData = []
        MainTable.reloadData()
        self.page = 1
        self.view.viewWithTag(151)?.removeFromSuperview();
        self.loadData();
    }
    
    @objc func showProductStatusDropdown(_ sender:UIButton)
    {
        let dropDown = DropDown()
        if sender.tag == 180
        {
            dropDown.dataSource = Array( self.statusdata.keys)
        }
        else
        {
           // dropDown.dataSource = Array(pageLayout.keys)
            dropDown.dataSource = Array(self.layoutdata.keys)
          //  dropDown.dataSource = ["Vendor Empty Layout","Vendor Panel Layout","1 Column","2 Columns with left bar","2 Columns with right bar","3 Columns","Empty"]
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
      
    @objc func filterdata()
    {
        self.getRequest(url: "rest/all/V1/vcms/dropdownoptions")
        
    }
    

    
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

    
    
    
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
    //MARK:- Edit section
    @objc func EditButtonClicked(_ sender:UIButton)
    {
        if isLoadedFrom
        {
            let viewcontrol = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "AddCmsPage") as? ced_AddCmsPageViewController
            viewcontrol?.dataFlag = true
            viewcontrol?.pageLayout = pageLayout;
            viewcontrol?.page_id = "\(JsonData[sender.tag]["page_id"].intValue)"
            self.navigationController?.pushViewController(viewcontrol!, animated: true)
        }
        else
        {
            let viewcontrol = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "AddCms") as? ced_AddCmsViewController
            viewcontrol?.backData = true
            viewcontrol?.id = JsonData[sender.tag]["block_id"].stringValue
            self.navigationController?.pushViewController(viewcontrol!, animated: true)
        }
    }
    
    // MARK:- Delete section
    @objc func deleteProductRequestConfirmation(_ sender:UIButton)
    {
        let showTitle = "Confirmation".localized;
        let showMsg = "Are You Sure You Want To Delete This?".localized;
        
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
            print(sender.tag)
            let page_id = "\(JsonData[sender.tag]["page_id"].intValue)"
            
            baseURL = "rest/all/V1/vcmspage/delete"
//            "rest/all/V1/vcmspage/delete"
            var poststring1 = [String:Int]()
            poststring1["page_id"] = Int(page_id)
            poststring1["vendor_id"] = Int(vendorId)
            let postString1 = "page_id="+page_id+"&vendor_id="+vendorId+"&hashkey="+hashKey
            del_button_tag=sender.tag
            print(poststring1)
            
            Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
            self.sendRequestCms(url: baseURL, params: poststring1)
        }
        else
        {
            let block_id = "\(JsonData[sender.tag]["block_id"].intValue)"
            //cmsData[sender.tag]["block_id"]!
            baseURL = "rest/all/V1/vcmsblock/delete"
            var poststring1 = [String:Int]()
            poststring1["block_id"] = Int(block_id)
            poststring1["vendor_id"] = Int(vendorId)
            
         //   let postString1 = "block_id="+block_id+"&vendor_id="+vendorId+"&hashkey="+hashKey
            del_button_tag=sender.tag
            Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
            self.sendRequestCms(url: baseURL, params: poststring1)
            //self.sendRequest(url: baseURL, parameters: postString1)
        }
    }
    
    
    @objc func reloadTableView(){
       
        self.MainTable.reloadData()
    }
    
    //MARK:- Parsing the data
    
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
            for result in json["cms_list"].arrayValue{
                if (result["identifier"].string != nil)
                {
                    identifier = (result["identifier"].string)!
                }
                if (result["page_id"].int != nil)
                {
                    id = "\((result["page_id"].intValue))"
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
                let temp = ["update_time":updateTime,"creation_time":creationTime,"page_id":id,"title":title,"is_active":is_active,"is_approve":is_approve,"identifier":identifier,"page_layout":page_layout]
                //(temp)
                Alert_File.removeLoadingIndicator(self);
                self.cmsData.append(temp )
                print(cmsData)
                //reloadTableView()
            }
//            reloadTableView()
        }
        else
        {
            for result in json["cms_list"].arrayValue{
                
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
    
    
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        Alert_File.removeLoadingIndicator(self);
        if let data = data{
            if (requestUrl == "rest/all/V1/vcms/dropdownoptions")
            {
                guard let json = try? JSON(data: data) else {return}
                //(json)
                print(json)
                 
                if(json["is_success"].stringValue.lowercased() == "false"){
                   // loading = false;
                    if(page == 1)
                    {
                       // self.view.makeToast(json["message"].stringValue, duration: 2.0, position: .center);
                        //                            Ced_CommonVendor.delay(2.0, closure: {
                        //                                self.navigationController?.popViewController(animated: true)
                        //                            })
                        self.MainTable.setEmptyMessage(json["message"].stringValue)
                    }
                    
                    
                }else if(json["is_success"].stringValue.lowercased() == "true"){
                    cmsData = []
                    
                      let layoutda = json["dropdown_data"]["layout_data"].arrayValue
                    
                    for x in 0..<layoutda.count
                    {
                        let key = json["dropdown_data"]["layout_data"][x]["label"].stringValue
                        let val = json["dropdown_data"]["layout_data"][x]["key"].stringValue
                        
                        
                        self.layoutdata[key] = val
                    }
                    
                    for z in 0..<json["dropdown_data"]["status"].count {
                        
                        
                        let key = json["dropdown_data"]["status"][z]["label"].stringValue
                        let val = json["dropdown_data"]["status"][z]["key"].stringValue
                        
                        self.statusdata[key]=val
                    }
                    
//                        self.JsonData=json
//                        self.parseProductdata(json)
                    //self.parseJson()
//                    self.MainTable.reloadData()
                }
            }
            
            
            if isLoadedFrom == true
            {
                if(requestUrl=="rest/all/V1/vcmspage/listing")
                {
                    guard let json = try? JSON(data: data) else {return}
                    //(json)
                    print(json)
                    print(json["success"].stringValue)
//                    self.JsonData=json
                    if(json["success"].stringValue.lowercased() == "false"){
                        
                      //  loading = false
                        if(page == 1)
                        {
                           // self.view.makeToast(json["message"].stringValue, duration: 2.0, position: .center)
                            self.MainTable.setEmptyMessage(json["message"].stringValue)
                            //                            Ced_CommonVendor.delay(2.0, closure: {
                            //                                self.navigationController?.popViewController(animated: true)
                            //                            })
                        }
                        //page = 1
                    }else if(json["success"].stringValue.lowercased() == "true"){
                        if page == 1{
                            cmsData = []
                            self.JsonData = json["cms_list"].arrayValue
                        }else{
                            json["cms_list"].arrayValue.forEach{ self.JsonData.append($0)}
                        }
                        
                        filter = ""
                        self.parseProductdata(json)
                        self.MainTable.reloadData()
                        //self.parseJson()
                    }
                }
                if(requestUrl=="rest/all/V1/vcmspage/delete")
                {
                    guard let json = try? JSON(data: data) else {return}
                    (json)
                     
                    if(json["success"].stringValue.lowercased() == "false"){
                       // loading = false;
                        if(page == 1)
                        {
                           // self.view.makeToast(json["message"].stringValue, duration: 2.0, position: .center);
                            self.MainTable.setEmptyMessage(json["message"].stringValue)
                            //                            Ced_CommonVendor.delay(2.0, closure: {
                            //                                self.navigationController?.popViewController(animated: true)
                            //                            })
                            
                        }
                        
                        
                    }else if(json["success"].stringValue.lowercased() == "true"){
                        cmsData = []
                        page = 1
                        self.loadData()
                    //    self.parseProductdata(json)
                        //self.parseJson()
                    }
                }
       

                 
            }
            else
            {
                if requestUrl == "rest/all/V1/vcmsblock/delete"
                {
                    guard let json = try? JSON(data: data) else {return}
                    (json)
                     
                    if(json["success"].stringValue.lowercased() == "false"){
                       // loading = false;
                        if(page == 1)
                        {
                           // self.view.makeToast(json["message"].stringValue, duration: 2.0, position: .center);
                            self.MainTable.setEmptyMessage(json["message"].stringValue)
                            //                            Ced_CommonVendor.delay(2.0, closure: {
                            //                                self.navigationController?.popViewController(animated: true)
                            //                            })
                            
                        }
                        
                        
                    }else if(json["success"].stringValue.lowercased() == "true"){
                        cmsData = []
                        page = 1
                        self.loadData()
                    //    self.parseProductdata(json)
                        //self.parseJson()
                    }
                }
                
                if(requestUrl=="rest/all/V1/vcmsblock/listing")
                {
                    guard let json = try? JSON(data: data) else {return}
                    (json)
                     
                    if(json["success"].stringValue.lowercased() == "false"){
                       // loading = false;
                        if(page == 1)
                        {
                           // self.view.makeToast(json["message"].stringValue, duration: 2.0, position: .center);
                            self.MainTable.setEmptyMessage(json["message"].stringValue)
                            //                            Ced_CommonVendor.delay(2.0, closure: {
                            //                                self.navigationController?.popViewController(animated: true)
                            //                            })
                            
                        }
                        
                        
                    }else if(json["success"].stringValue.lowercased() == "true"){
                        cmsData = []
                        if page == 1{
                            self.JsonData=json["cms_list"].arrayValue
                        }else{
                            json["cms_list"].arrayValue.forEach{ self.JsonData.append($0)}
                           
                        }
                        
                        self.parseProductdata(json)
                        //self.parseJson()
                    }
                }
                 
                 
            }
            
           // self.MainTable.reloadData()
       //     self.reloadTableView()
            
        }
    }
    
    

}



// MARK:- Table View Extentions

extension ced_NewBlockCmsViewController : UITableViewDelegate, UITableViewDataSource
{
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return JsonData.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = MainTable.dequeueReusableCell(withIdentifier: "NewCmsBlockCell", for: indexPath) as! ced_NewBlockCell
        // let cell=MainTable.dequeueReusableCell(withIdentifier: "NewCmsBlockCell") as! ced_NewBlockCell
        cell.data = JsonData[indexPath.row]
        cell.isLoadedFrom=isLoadedFrom
        cell.PopulateData()
        
         
        let  colorString = Ced_CommonVendor.getInfoPlist("themecolor") as! String
//
//        cell.id.text = JsonData["page_id"].stringValue
//
//        let active = JsonData["is_active"].stringValue
//        if(active == "1")
//        {
//            cell.status.text = "Enable"
//            cell.status.textColor = Ced_CommonVendor.UIColorFromRGB(colorString)
//           // statusData = cell.status.text!
//        }
//        else
//        {
//            cell.status.text = "Disabled"
//            cell.status.textColor = Ced_CommonVendor.UIColorFromRGB(colorString)
//          //  statusData = cell.status.text!
//        }
//
//
//        cell.title.text = JsonData["title"].stringValue
//
//        let approve = JsonData["is_approve"] ?? ""
//
//        if approve == "1"
//        {
//            cell.approved.text = "Approved"
//            cell.approved.textColor = Ced_CommonVendor.UIColorFromRGB(colorString)
//        }
//        else
//        {
//            cell.approved.text = "Pending"
//            cell.approved.textColor = Ced_CommonVendor.UIColorFromRGB(colorString)
//        }
//
//
//
//        cell.layout.text = ""
//        cell.created.text = JsonData["creation_time"].stringValue
//        cell.lastmodified.text = JsonData["update_time"].stringValue
//        cell.lblurl.text = JsonData["identifier"].stringValue
//
//        cell.BttnPreview.setTitle("", for: .normal)
//        cell.BttnEdit.setTitle("", for: .normal)
        
        cell.bttnview.cancelButton.backgroundColor =  Ced_CommonVendor.UIColorFromRGB(colorString)
        cell.bttnview.cancelButton.setTitle("Edit".localized, for: UIControl.State.normal)
        cell.bttnview.cancelButton.layer.cornerRadius = 2
        cell.bttnview.doneButton.layer.cornerRadius = 2
        cell.bttnview.cancelButton.tintColor = UIColor.white
        cell.bttnview.cancelButton.tag = indexPath.row
        cell.bttnview.cancelButton.addTarget(self, action: #selector(EditButtonClicked(_:)), for: .touchUpInside)
        cell.bttnview.doneButton.addTarget(self, action: #selector(deleteProductRequestConfirmation(_:)), for: .touchUpInside)
        cell.bttnview.doneButton.backgroundColor = Ced_CommonVendor.UIColorFromRGB("#ff0000");
        cell.bttnview.doneButton.tintColor = UIColor.white
        cell.bttnview.doneButton.tag = indexPath.row
        cell.bttnview.doneButton.setTitle("Delete".localized, for: UIControl.State())
         
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 400
    }
    
    @objc func scrollViewDidEndDragging(_ scrollView: UIScrollView, willDecelerate decelerate: Bool) {
        let currentOffset = scrollView.contentOffset.y
        let maximumOffset = scrollView.contentSize.height - scrollView.frame.size.height
        if (maximumOffset - currentOffset) <= 40 {
                self.page += 1;
                self.loadData()
        }
    }
}


extension ced_NewBlockCmsViewController:UITextFieldDelegate{
    @objc func textFieldDidBeginEditing(_ textField: UITextField) {
        textField.resignFirstResponder()
 
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
        
        let datePickerView = UIDatePicker()
        datePickerView.locale = Locale(identifier: ced_storeVC.selectLangauge)
        datePickerView.datePickerMode = UIDatePicker.Mode.date;
        datePickerView.maximumDate = Date()
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
        
        
//        twoButtonView.doneButton.addTarget(self, action: #selector(pickDateFromDatePickerView(_:)), for: UIControl.Event.touchUpInside);
        twoButtonView.doneButton.backgroundColor = colorGreen;
        twoButtonView.doneButton.rx.tap.bind{
            let dateFormatter = DateFormatter()
            dateFormatter.locale = Locale(identifier: "en_US_POSIX")
            dateFormatter.dateStyle = DateFormatter.Style.short
            dateFormatter.timeStyle = DateFormatter.Style.none
            dateFormatter.dateFormat = "yyyy-MM-dd";
            let timeFormatter = DateFormatter()
            timeFormatter.dateFormat = "hh:mm:ss"
            timeFormatter.locale = Locale(identifier: "en_US_POSIX")
            let time = timeFormatter.string(from: Date())
            let dateDisplay = dateFormatter.string(from: datePickerView.date);
            textField.text = dateDisplay
            textField.resignFirstResponder();
            self.view.viewWithTag(161)?.removeFromSuperview();
        }.disposed(by: disposeBag)
        
        tempInrView.makeCardUsingThemeColor(tempInrView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4);
        
        bgCView.addSubview(tempInrView);
        self.view.addSubview(bgCView);
  
    }
  
    @objc func cancelDatePickerView(_ sender:UIButton){
        
        sender.resignFirstResponder();
        
        self.view.viewWithTag(161)?.removeFromSuperview();
    }

    func textViewDidBeginEditing(_ textView: UITextView) {
        textView.resignFirstResponder()
 
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
        
        let datePickerView = UIDatePicker()
        datePickerView.locale = Locale(identifier: ced_storeVC.selectLangauge)
        datePickerView.datePickerMode = UIDatePicker.Mode.date;
        datePickerView.maximumDate = Date()
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
        
        
//        twoButtonView.doneButton.addTarget(self, action: #selector(pickDateFromDatePickerView(_:)), for: UIControl.Event.touchUpInside);
        twoButtonView.doneButton.backgroundColor = colorGreen;
        twoButtonView.doneButton.rx.tap.bind{
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
            textView.text = dateDisplay
            textView.resignFirstResponder();
            self.view.viewWithTag(161)?.removeFromSuperview();
        }.disposed(by: disposeBag)
        
        tempInrView.makeCardUsingThemeColor(tempInrView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4);
        
        bgCView.addSubview(tempInrView);
        self.view.addSubview(bgCView);
  
    }
}
