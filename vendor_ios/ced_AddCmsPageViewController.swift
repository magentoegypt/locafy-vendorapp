//
//  ced_AddCmsPageViewController.swift
//  VenderApp
//
//  Created by cedcoss on 08/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_AddCmsPageViewController: ced_VendorBaseClass, UITableViewDataSource, UITableViewDelegate {
    
    var pageLayout = [String:String]()
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var CmsTitle: UILabel!
    @IBOutlet weak var save: UIButton!
    var storeData = [[String:String]]()
    var defaultView = false
    var defaultStore = false
    var baseURL = String();
    var array = [String]()
    var isclicked = false
    var height = 0
    var pageInfoClicked = false
    var contentClicked = false
    var DesignClicked = false
    var contentHeight = 0
    var designHeight = 0
    var MetaClicked = false
    var metaHeight = 0
    var dataFlag = false
    var page_id = ""
    var button1 = UIButton()
    var button2 = UIButton()
    var fieldcell1 = ced_CmsPageTableViewCell()
    var fieldcell2 = ced_CmsPageTableViewCell()
    var fieldcell3 = ced_CmsPageTableViewCell()
    var fieldcell4 = ced_CmsPageTableViewCell()
    var contenArr = ced_CmsPageTableViewCell()
    var designArr = ced_CmsPageTableViewCell()
    var temp = [String:String]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        pageInfoClicked = true
        let color = Ced_CommonVendor.UIColorFromRGB("#ff0000");
        save.setTitle("Save".localized, for: .normal)
        save.backgroundColor = color;
        CmsTitle.textColor = UIColor.white
        save.tintColor = UIColor.white
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as! String
        CmsTitle.backgroundColor = Ced_CommonVendor.UIColorFromRGB(colorString)
        save.addTarget(self, action: #selector(SaveData(_:)), for: .touchUpInside)
        if dataFlag
        {
            CmsTitle.text = "  Edit Cms Page".localized
            let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
            let vendorId = userData["vendorId"] as! String
            let hashKey    = userData["hashKey"] as! String
            baseURL = "rest/all/V1/vcmspage/view";
            var poststring = [String:String]()
            poststring["vendor_id"] = vendorId
            poststring["page_id"]=page_id
            let postString = "vendor_id="+vendorId+"&hashkey="+hashKey+"&page_id="+page_id;
            self.sendRequestCms(url: baseURL, params: poststring)
         //   self.sendRequest(url: baseURL, parameters: postString)
            Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
        }
        else
        {
            CmsTitle.text = "  Create Cms Page".localized
            getStoreView()
            tableView.delegate = self
            tableView.dataSource = self
        }
    }
    
    @objc func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 4
    }
    
    
    
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if indexPath.row == 0
        {
            if pageInfoClicked
            {
                return 480
            }
            else
            {
                return (CGFloat(480 - height))
            }
        }
        if indexPath.row == 1
        {
            if contentClicked
            {
                return 260
            }
            else
            {
                return (CGFloat(260 - contentHeight))
            }
            
        }
        else if indexPath.row == 2
        {
            
            if DesignClicked
            {
                return 300
            }
            else
            {
                return (CGFloat(300 - designHeight))
            }
        }
        else
        {
            if MetaClicked
            {
                return 300
            }
            else
            {
                return (CGFloat(300 - metaHeight))
            }
        }
    }
    
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        var colorString = Ced_CommonVendor.getInfoPlist("themecolor") as! String
        if indexPath.row == 0
        {
            colorString = Ced_CommonVendor.getInfoPlist("themecolor") as! String
            let cell=tableView.dequeueReusableCell(withIdentifier: "cmsPageBody") as! ced_CmsPageTableViewCell
            fieldcell1 = cell
            cell.PageInfo.setTitle("Page Information".localized, for: .normal)
            
            cell.pageTitleLbl.text = "Page Title*".localized
            cell.urlKeyLbl.text = "Url Key*".localized
            cell.storeViewLbl.text = "Store View*".localized
            
            cell.PageInfo.tag = indexPath.row
            cell.PageInfo.layer.cornerRadius=2
            cell.PageInfo.backgroundColor =  Ced_CommonVendor.UIColorFromRGB(colorString)
            cell.PageInfo.tintColor = UIColor.white
            cell.PageInfo.addTarget(self, action: #selector(SendData(_:)), for: .touchUpInside)
            height = Int(cell.TopSpace.constant) + Int(cell.Viewheight.constant)
            cell.VendorPage.entityNameLabel.text = "Set As Vendor Home Page *".localized
            cell.VendorPage.entityNameLabel.font=UIFont(name: "Roboto-Bold", size: 17)
            cell.status.entityNameLabel.text = "Status *".localized
            cell.status.entityNameLabel.font=UIFont(name: "Roboto-Bold", size: 17)
            cell.VendorPage.dropDownButton.addTarget(self, action: #selector(statusSelected(_:)),for: .touchUpInside)
            cell.status.dropDownButton.tag = 110
            cell.VendorPage.dropDownButton.tag = 120
            cell.status.dropDownButton.addTarget(self, action: #selector(statusSelected(_:)),for: .touchUpInside)
            if dataFlag
            {
                cell.pageTitle.text = temp["title"]
                cell.urlKey.text = temp["identifier"]
                if temp["is_home"] == "false"
                {
                    cell.VendorPage.dropDownButton.setTitle("No".localized, for: UIControl.State())
                }
                else
                {
                    cell.VendorPage.dropDownButton.setTitle("Yes".localized, for: UIControl.State())
                }
                if temp["is_active"] == "false"
                {
                    cell.status.dropDownButton.setTitle("Disable".localized, for: UIControl.State())
                }
                else
                {
                    cell.status.dropDownButton.setTitle("Enable".localized, for: UIControl.State())
                }
            }
            cell.PageView.makeCard(cell.PageView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
            return cell
        }
        
        if indexPath.row == 1
        {
            print("inside content")
            colorString = Ced_CommonVendor.getInfoPlist("themecolor") as! String
            
            let cell=tableView.dequeueReusableCell(withIdentifier: "cmsContentBody") as! ced_CmsPageTableViewCell
            if cell.iscontentLoaded{
                fieldcell2 = cell
                cell.iscontentLoaded = false
                cell.content.tag = indexPath.row
                cell.content.layer.cornerRadius=2
                cell.content.backgroundColor =  Ced_CommonVendor.UIColorFromRGB(colorString)
                cell.content.setTitle("Content".localized, for: .normal)
                cell.contentHeadingLbl.text = "Content Heading*".localized
                cell.contentLbl.text = "Content*".localized
                
                cell.content.tintColor = UIColor.white
                cell.content.addTarget(self, action: #selector(ContentData(_:)), for: .touchUpInside)
                cell.ContentDescribtion.layer.borderWidth = 1
                cell.ContentDescribtion.layer.cornerRadius = 5
                contentHeight = Int(cell.ContentHeight.constant) + Int(cell.top.constant)
                cell.contentView2.makeCard(cell.contentView2, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
                if dataFlag
                {
                    cell.contentHeading.text =  temp["content_heading"]
                    cell.ContentDescribtion.text = temp["content"]
                }
                contenArr=cell
            }
            return cell
        }
        else if indexPath.row == 2
        {
            print("inside degisn")
            colorString = Ced_CommonVendor.getInfoPlist("themecolor") as! String
            let cell=tableView.dequeueReusableCell(withIdentifier: "cmsDesignBody") as! ced_CmsPageTableViewCell
            
            if cell.isDesignLoaded{
                fieldcell3 = cell
                cell.isDesignLoaded = false
                cell.Desgin.setTitle("Design".localized, for: .normal)
                cell.layoutXmlLbl.text = "Layout Update Xml*".localized
                
                cell.Desgin.tag = indexPath.row
                cell.Desgin.layer.cornerRadius=2
                cell.Desgin.backgroundColor =  Ced_CommonVendor.UIColorFromRGB(colorString)
                cell.Desgin.tintColor = UIColor.white
                cell.layoutDropdown.dropDownButton.tag = 190
                cell.layoutDropdown.dropDownButton.addTarget(self, action: #selector(statusSelected(_:)),for: .touchUpInside)
                cell.layoutDropdown.entityNameLabel.text = "Layout*".localized
                cell.layoutDropdown.entityNameLabel.font=UIFont(name: "Roboto-Bold", size: 17)
                
                cell.LayoutDescribtion.layer.borderWidth = 1
                cell.LayoutDescribtion.layer.cornerRadius = 5
                cell.Desgin.addTarget(self, action: #selector(DesignData(_:)), for: .touchUpInside)
                designHeight = Int(cell.DesignHeight.constant) + Int(cell.DesignTop.constant)
                cell.DesignView.makeCard(cell.DesignView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
                if dataFlag
                {
                    cell.layoutDropdown.dropDownButton.setTitle(temp["page_layout"], for: UIControl.State())
                    cell.LayoutDescribtion.text = temp["layout_update_xml"]
                }
                designArr=cell
            }
            return cell
        }
            
            
        else
        {
            colorString = Ced_CommonVendor.getInfoPlist("themecolor") as! String
            
            let cell=tableView.dequeueReusableCell(withIdentifier: "cmsMetaBody") as! ced_CmsPageTableViewCell
            fieldcell4 = cell
            
            cell.MetaButton.setTitle("Meta Data".localized, for: .normal)
            cell.keyboardLbl.text = "Keywords".localized
            cell.descriptionLbl.text = "Description".localized
            
            cell.MetaButton.tag = indexPath.row
            cell.MetaButton.layer.cornerRadius=2
            cell.MetaButton.backgroundColor =  Ced_CommonVendor.UIColorFromRGB(colorString)
            cell.keyword.layer.borderWidth = 1
            cell.keyword.layer.cornerRadius = 5
            cell.metaDescribtion.layer.borderWidth = 1
            cell.metaDescribtion.layer.cornerRadius = 5
            cell.MetaButton.tintColor = UIColor.white
            cell.MetaButton.addTarget(self, action: #selector(MetaData(_:)), for: .touchUpInside)
            metaHeight = Int(cell.MetaViewHeight.constant) + Int(cell.metaViewTop.constant)
            cell.MetaView.makeCard(cell.MetaView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
            if dataFlag
            {
                cell.metaDescribtion.text = temp["meta_description"]
                cell.keyword.text = temp["meta_keywords"]
            }
            
            return cell
        }
    }
    
    //    @objc func DefaultSelected(_ sender:UIButton)
    //    {
    //        if sender.tag == 5
    //        {
    //        if defaultView
    //        {
    //            button1.isSelected = false
    //            defaultView = false
    //        }
    //        else
    //        {
    //            button1.isSelected = true
    //            defaultView = true
    //        }
    //        }
    //        else
    //        {
    //            if defaultStore
    //            {
    //                button2.isSelected = false
    //                defaultStore = false
    //            }
    //            else
    //            {
    //                button2.isSelected = true
    //                defaultStore = true
    //            }
    //        }
    //    }
    //
    
    @objc func statusSelected(_ sender:UIButton)
    {
        let dropDown = DropDown();
        if sender.tag == 110
        {
            dropDown.dataSource = ["Disable".localized,"Enable".localized]
             //   ["Enable","Disable"];
        }
        else if sender.tag == 120
        {
            dropDown.dataSource = ["Yes".localized,"No".localized];
        }
        else
        {
            if(pageLayout.count>0)
            {
                dropDown.dataSource = Array(pageLayout.values);
                //dropDown.dataSource = ["Vendor Empty Layout","Vendor Panel Layout","1 Column","2 Columns with left bar","2 Columns with right bar","3 Columns","Empty"]
            }
            
        }
        dropDown.selectionAction = {(index, item) in
            sender.setTitle(item, for: .normal);
            
        }
        dropDown.anchorView = sender
        dropDown.bottomOffset = CGPoint(x: 0, y:sender.bounds.height)
        
        if dropDown.isHidden {
            dropDown.show();
        } else {
            dropDown.hide();
        }
    }
    @objc func SaveData(_ sender:UIButton)
    {
        if (fieldcell1.pageTitle.text == "") || (fieldcell1.pageTitle.text == "")
        {
            
            self.view.showToastMsg(" Title and Url key are Required.".localized);
        }
        else
        {
            let title1 = fieldcell1.pageTitle.text
            let urlk = fieldcell1.urlKey.text
            var status = fieldcell1.status.dropDownButton.currentTitle
            var state=Bool()
            if status == "Enable".localized
            {
                state=true
                status = "true"
            }
            else
            {
                state=false
                status = "false"
            }
            var vendorPage  = fieldcell1.VendorPage.dropDownButton.currentTitle
            var vendorpage=Bool()
            if vendorPage == "Yes".localized
            {
                vendorpage=true
                vendorPage = "1"
            }
            else
            {   vendorpage=false
                vendorPage = "0"
            }
            
            let  storestring = JSONConvert(data: array)
            print(storestring)
            let cheading = fieldcell2.contentHeading.text
            let cDescribtion = fieldcell2.ContentDescribtion.text
            var  layoutTitle = "";
            if(pageLayout.count>0)
            {
                print(pageLayout)
                print(fieldcell3.layoutDropdown.dropDownButton.currentTitle!)
                layoutTitle = fieldcell3.layoutDropdown.dropDownButton.currentTitle ?? ""
            }
            else
            {
                layoutTitle = fieldcell3.layoutDropdown.dropDownButton.currentTitle!
            }
            let layoutDescribtion = fieldcell3.LayoutDescribtion.text
            let keyword = fieldcell4.keyword.text
            let metaDescribtion = fieldcell4.metaDescribtion.text
            
            let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
            let vendorId = userData["vendorId"] as! String
            let hashKey    = userData["hashKey"] as! String
            
            if dataFlag
            {
                baseURL = "rest/all/V1/vcmspage/update"
                
                var poststring = [String:Any]()
               
                    
                    poststring["title"]=title1

                    poststring["page_layout"] = layoutTitle
                    poststring["vendor_id"]=vendorId
                    poststring["meta_keywords"]=keyword
                    poststring["meta_description"]=metaDescribtion
                    poststring["identifier"]=urlk
                    poststring["content_heading"]=cheading
                    poststring["content"]=cDescribtion
                    
                    poststring["is_active"]=state
                    poststring["is_home"]=vendorpage
                    poststring["sort_order"]=0
                    poststring["layout_update_xml"]=layoutDescribtion
                     
                var store = [Int]()
                for items in array
                {
                    store.append(Int(items)!)
                }
                
                    poststring["store_id"] = array
                    poststring["page_id"] = page_id
                
        
                   
                let postString = "vendor_id="+vendorId+"&hashkey="+hashKey+"&page_id="+page_id+"&store="+storestring+"&title="+title1!+"&urlkey="+urlk!+"&cheading="+cheading!+"&status="+status!+"&content="+cDescribtion!+"&is_home="+vendorPage!+"&meta_keywords="+keyword!+"&meta_description="+metaDescribtion!
                print(postString)
                
                self.sendRequestAddCms(url: baseURL, params: poststring ,mainkey: "cmsPageData")
                //self.sendRequest(url: baseURL, parameters: postString)
                Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
                
            }
            else
            {
                baseURL = "rest/all/V1/vcmspage/save"
                
                var poststring = [String:Any]()
               
                   poststring["is_approve"] = status
                    poststring["title"]=title1

                    poststring["page_layout"] = layoutTitle
                    poststring["vendor_id"]=vendorId
                    poststring["meta_keywords"]=keyword
                    poststring["meta_description"]=metaDescribtion
                    poststring["identifier"]=urlk
                    poststring["content_heading"]=cheading
                    poststring["content"]=cDescribtion
                
                    poststring["is_active"]=state
                    poststring["is_home"]=vendorPage
                    poststring["sort_order"]=0
                    poststring["layout_update_xml"]=layoutDescribtion
              
                    poststring["store_id"] = array

                     
//                cmsPageData
                
                let postString = "vendor_id="+vendorId+"&hashkey="+hashKey+"&store="+storestring+"&title="+title1!+"&urlkey="+urlk!+"&cheading="+cheading!+"&status="+status!+"&content="+cDescribtion!+"&is_home="+vendorPage!+"&meta_keywords="+keyword!+"&meta_description="+metaDescribtion!
                print(postString)
                print(poststring)
                self.sendRequestAddCms(url: baseURL, params: poststring, mainkey: "cmsPageData" )
               // self.sendRequest(url: baseURL, parameters: postString)
                Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
            }
        }
        
        
    }
    @objc func SendData(_ sender:UIButton)
    {
        if pageInfoClicked == true
        {
            pageInfoClicked = false
            self.tableView.reloadRows(at: [IndexPath(row: 0, section: 0)], with: .fade)
        }
        else
        {
            pageInfoClicked = true
            //  let indexpath = IndexPath(row: sender.tag, section: 0)
            self.tableView.reloadRows(at: [IndexPath(row: 0, section: 0)], with: .fade)
        }
        
    }
    @objc func ContentData(_ sender:UIButton)
    {
        if contentClicked == true
        {
            contentClicked = false
            self.tableView.reloadRows(at: [IndexPath(row: 1, section: 0)], with: .fade)
        }
        else
        {
            contentClicked = true
            self.tableView.reloadRows(at: [IndexPath(row: 1, section: 0)], with: .fade)
            
        }
        
    }
    @objc func DesignData(_ sender:UIButton)
    {
        if DesignClicked == true
        {
            DesignClicked = false
            self.tableView.reloadRows(at: [IndexPath(row: 2, section: 0)], with: .fade)
            
        }
        else
        {
            DesignClicked = true
            self.tableView.reloadRows(at: [IndexPath(row: 2, section: 0)], with: .fade)
        }
    }
    @objc func MetaData(_ sender:UIButton)
    {
        if MetaClicked == true
        {
            MetaClicked = false
            self.tableView.reloadRows(at: [IndexPath(row: 3, section: 0)], with: .fade)
            
        }
        else
        {
            MetaClicked = true
            self.tableView.reloadRows(at: [IndexPath(row: 3, section: 0)], with: .fade)
            
        }
    }
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        Alert_File.removeLoadingIndicator(self)
        if let data = data{
            if(requestUrl=="rest/all/V1/vcmspage/view")
            {
                guard let json = try? JSON(data: data) else {return}
                print(json)
                if(json["success"].stringValue.lowercased() == "false")
                {
                   // self.view.showToastMsg(json["message"].stringValue)
                    self.tableView.setEmptyMessage(json["message"].stringValue)
                    
                }
                else if(json["success"].stringValue.lowercased() == "true"){
                    self.parseProductdata(json)
                    //self.view.showToastMsg(json["data"]["message"].stringValue)
                    
                }
                
            }
            else if(requestUrl=="rest/all/V1/vcmspage/save")
            {
                guard let json = try? JSON(data: data) else {return}
                print(json)
                if(json["success"].stringValue.lowercased() == "false"){
                   // self.view.showToastMsg(json["message"].stringValue)
                    self.tableView.setEmptyMessage(json["message"].stringValue)
                }else if(json["success"].stringValue.lowercased() == "true"){
                    self.view.showToastMsg(json["message"].stringValue)
                    Ced_CommonVendor.delay(1.0) {
                        self.navigationController?.popViewController(animated: true)
                    }
                }
            }
            else if(requestUrl=="rest/all/V1/vcmspage/update")
            {
                guard let json = try? JSON(data: data) else {return}
                print(json)
                if(json["success"].stringValue.lowercased() == "false"){
                   // self.view.showToastMsg(json["message"].stringValue)
                    self.tableView.setEmptyMessage(json["message"].stringValue)
                }else if(json["success"].stringValue.lowercased() == "true"){
                    self.view.showToastMsg(json["message"].stringValue)
                    Ced_CommonVendor.delay(1.0) {
                        self.navigationController?.popViewController(animated: true)
                    }
                }
            }
        }
        
        
    }
    
    func parseProductdata(_ json:JSON){
        
        var meta_description = ""
        
        var  meta_keywords = ""
        var page_layout = ""
        var content_heading = ""
        var content = ""
        var layout_xml = ""
        if let jsonData = json["cms_list"][0].dictionary
        {
            if (jsonData["meta_description"]?.string != nil)
            {
                meta_description = (jsonData["meta_description"]?.string)!
            }
            if (jsonData["meta_keywords"]?.string != nil)
            {
                meta_keywords = (jsonData["meta_keywords"]?.string)!
                
            }
            if (jsonData["page_layout"]?.string != nil)
            {
                page_layout = (jsonData["page_layout"]?.string)!
            }
            
            if (jsonData["content_heading"]?.string != nil)
            {
                content_heading = (jsonData["content_heading"]?.string)!
            }
            if (jsonData["content"]?.string != nil)
            {
                content = (jsonData["content"]?.string)!
            }
            if (jsonData["layout_update_xml"]?.string != nil)
            {
                layout_xml = (jsonData["layout_update_xml"]?.string)!
            }
            let is_active = jsonData["is_active"]?.stringValue
            let title = jsonData["title"]?.string
            let identifier = jsonData["identifier"]?.string
            let is_home = jsonData["is_home"]?.stringValue
            let store_id = jsonData["store_id"]!.arrayValue
            print(store_id)
            let store = Array(store_id)
            print(store)
            temp = ["meta_description":meta_description,"meta_keywords":meta_keywords,"page_layout":page_layout,"is_active":is_active ?? "true","title":title!,"identifier":identifier!, "is_home":is_home ?? "true","store_id":"\(store_id)","content":content,"content_heading":content_heading,"layout_update_xml":layout_xml]
            
            Alert_File.removeLoadingIndicator(self);
        }
        print(temp)
        self.tableView.delegate = self
        self.tableView.dataSource = self
        tableView.reloadData()
        getStoreView()
    }
    @objc func getStoreView()
    {
        let url = "rest/all/V1/vcms/dropdownoptions"
        var baseUrl = settings.baseUrl
        baseUrl += url
        var request =  URLRequest(url: URL(string:baseUrl)!)
        
        request.httpMethod = "GET"
        let sessionConfig = URLSessionConfiguration.default
//        sessionConfig.httpAdditionalHeaders = [
//            "Authorization": "Basic \(Ced_CommonVendor.getAuthData())",
//        ]
        sessionConfig.timeoutIntervalForRequest = timeoutIntervalForRequest
        sessionConfig.timeoutIntervalForResource = timeoutIntervalForResource
        let session = URLSession(configuration: sessionConfig)
        let task = session.dataTask(with: request, completionHandler: {
            data,response,error in
            DispatchQueue.main.async
                {
                    Alert_File.removeLoadingIndicator(self)
            }
            guard error == nil && data != nil else
            {
                DispatchQueue.main.async
                    {
                        
                        Alert_File.removeLoadingIndicator(self)
                        ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error".localized, showMsg: error!.localizedDescription)
                }
                print("error=\(error)")
                return;
            }
            
            // check for http errors
            if let httpStatus = response as? HTTPURLResponse, httpStatus.statusCode != 200
            {
                DispatchQueue.main.async {
                        
                        Alert_File.removeLoadingIndicator(self)
                        print("statusCode should be 200, but is \(httpStatus.statusCode)")
                        print("response = \(response)")
                        print(baseUrl)
                        //self.verdorOrderTable.isHidden = true
                        if(httpStatus.statusCode == -1009)
                        {
                            ced_showError.showNoNetWork(self)
                        }else
                        {
                            ced_showError.showNoModule(self)
                        }
                }
                return;
            }
            DispatchQueue.main.async {
                    Alert_File.removeLoadingIndicator(self)
                    if let data = data{
                        print("HELLO JSON")
                        var temp1=[String:String]()
                        guard let json = try? JSON(data: data) else {return}
                        print(json)
                        
                        for x in 0..<json["dropdown_data"]["layout_data"].count
                        {
                            let key = json["dropdown_data"]["layout_data"][x]["label"].stringValue
                            let val = json["dropdown_data"]["layout_data"][x]["key"].stringValue
                            
                            
                            self.pageLayout[key] = val
                        }
                        
                        for z in 0..<json["dropdown_data"]["status"].count {
                            
                            
                            let key = json["dropdown_data"]["status"][z]["label"].stringValue
                            let val = json["dropdown_data"]["status"][z]["key"].stringValue
                            
                            self.statusdata[key]=val
                        }
                        
                        
                        
                        let val = json["dropdown_data"]["store_ids"].array
                        self.fieldcell1.scrollWidth.constant = self.view.frame.width
                        for item in val!
                        {
                            print(item)
                            temp1["label"]=item["label"].string!
                            let button = UIButton(frame: CGRect(x: 0, y: 0, width:100 , height: 20))
                            if item["value"] == 0
                            {
                                if self.dataFlag
                                {
                                    if (self.temp["store_id"]?.first!) == "0"
                                    {
                                        button.isSelected = true
                                        button.backgroundColor = UIColor.gray
                                        self.array.append(String(button.tag))
                                    }
                                }
                                button.setTitle(String(describing: item["label"]), for: UIControl.State())
                                button.titleLabel?.font =  UIFont(name: "Roboto-Regular", size: 18)
                                button.setTitleColor(.black, for: UIControl.State())
                                button.addTarget(self, action: #selector(self.DefaultSelected(_:)),for: .touchUpInside)
                                self.fieldcell1.storeStack.addArrangedSubview(button)
                            }
                            else
                            {
                                if item["value"].array?.count != 0
                                {
                                    button.setTitle(String(describing: temp1["label"]!), for: UIControl.State())
                                    button.setTitleColor(.black, for: UIControl.State())
                                    button.titleLabel?.font =  UIFont(name: "Roboto-Bold", size: 18)
                                    button.contentHorizontalAlignment = UIControl.ContentHorizontalAlignment.left
                                    button.layer.cornerRadius = 2
                                    self.fieldcell1.stackHeight.constant += 20
                                    
                                    self.fieldcell1.storeStack.addArrangedSubview(button)
                                    
                                    temp1["value"]=item["value"].string
                                    for valuesw in item["value"].arrayValue
                                    {
                                        temp1["label"]=valuesw["label"].string!
                                        temp1["value"]=valuesw["value"].string!
                                        let button = UIButton(frame: CGRect(x: 0, y: 0, width:100 , height: 20))
                                        // Y += 30
                                        button.setTitle(String(describing: temp1["label"]!), for: UIControl.State())
                                        button.setTitleColor(.black, for: UIControl.State())
                                        button.contentHorizontalAlignment = UIControl.ContentHorizontalAlignment.left
                                        button.titleLabel?.font =  UIFont(name: "Roboto-Regular", size: 18)
                                        button.addTarget(self, action: #selector(self.DefaultSelected(_:)),for: .touchUpInside)
                                        button.tag = Int(temp1["value"]!)!
                                        if self.dataFlag
                                        {
                                            let store = Array(self.temp["store_id"]!)
                                            for item in store
                                            {
                                                if item.description == temp1["value"]
                                                {
                                                    button.isSelected = true
                                                    button.backgroundColor = UIColor.gray
                                                    self.array.append(String(button.tag))
                                                }
                                            }
                                        }
                                        button.layer.cornerRadius = 2
                                        self.fieldcell1.stackHeight.constant += 20
                                        self.fieldcell1.storeStack.addArrangedSubview(button)
                                    }
                                }
                                else
                                {
                                    let button = UILabel(frame: CGRect(x: 0, y: 0, width:100 , height: 20))
                                    button.font =  UIFont(name: "Roboto-Bold", size: 16)
                                    button.text =  temp1["label"]!
                                    button.textColor = UIColor.black
                                    button.textAlignment = NSTextAlignment.left;
                                    self.fieldcell1.stackHeight.constant += 20
                                    self.fieldcell1.storeStack.addArrangedSubview(button)
                                }
                            }
                        }
                    }
            }
        })
        task.resume()
    }
    
    var layoutarray = [String]()
    var layoutdata = [String:String]()
    var storeiddata = JSON()
    var statusarray = [String]()
    var statusdata = [String:String]()
    
    @objc func DefaultSelected(_ sender:UIButton)
    {
        let sender_id  = String(sender.tag)
        if defaultView
        {
            sender.backgroundColor=UIColor.white
            defaultView = false
            sender.isSelected = false
            for item in array
            {
                if item == sender_id
                {
                    let indexOfA = array.index(of: item)
                    array.remove(at: indexOfA!)
                }
            }
        }
        else
        {
            sender.backgroundColor = UIColor.gray
            defaultView = true
            sender.isSelected = true
            array.append(sender_id)
        }
        print(array)
    }
    @objc func JSONConvert(data :[String]) -> String
    {
        var ConvertData = ""
        let x = data as [String]
        do{
            let JSONData = try JSONSerialization.data(
                withJSONObject: x ,
                options: JSONSerialization.WritingOptions(rawValue: 0))
            ConvertData = NSString(data: JSONData,
                                   encoding: String.Encoding.utf8.rawValue)! as String
        }
        catch{
            print("error in data encoding")
        }
        print(ConvertData)
        return ConvertData
    }
    
}
