//
//  cedsubVendorView.swift
//  VenderApp
//
//  Created by CEDCOSS Technologies Private Limited on 24/01/17.
//  Copyright © 2017 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class cedsubVendorView: UIViewController,UITableViewDelegate,UITableViewDataSource {
    
    @IBOutlet weak var topContainerView: UIView!
    
    @IBOutlet weak var selectCustomAll: UIButton!
    @IBOutlet weak var tableView: UITableView!
    var categoriesList = [String:[String]]();
    @IBOutlet weak var saveButton: UIButton!
    var id:String?
    let defaults = UserDefaults.standard
    
    var heightToUse1 = CGFloat(0);
    var heightToUse2 = CGFloat(0);
    let fixedHeight = CGFloat(30);
    let padding = CGFloat(5);
    let screenSize: CGRect = UIScreen.main.bounds;
    let sub = CGFloat(20);
    var selectedFiltersIds = [String]();//use also at edit time
    var selectedWebsitesIds = [String]();//use also at edit time
    var selectionArray = [String:String]()
    var selectedFiltersIdsString = String();
    var selectedWebsitesIdsString = String();
    var type = "custom"
    
    override func viewDidLoad() {
        super.viewDidLoad()
        tableView.delegate = self
        tableView.dataSource = self
        let color = Ced_CommonVendor.UIColorFromRGB("#ff0000");
        saveButton.setTitle("Save".localized, for: .normal);
        saveButton.addTarget(self, action: #selector(saveButtonPressed(_:)), for: UIControl.Event.touchUpInside);
        selectCustomAll.addTarget(self, action: #selector(showResourceType(_:)), for: .touchUpInside)
        saveButton.backgroundColor = color;
        // Do any additional setup after loading the view.
         ced_navigationBarController().addNavButton(self,str:"back")
        self.loadVendorViewData()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    @objc func numberOfSections(in tableView: UITableView) -> Int {
        return 1;
    }
    
    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        if categoriesList.count  > 0{
            return 1
        }
        return 0
    }
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        
        if(indexPath.row == 0)
        {
            let cell = tableView.dequeueReusableCell(withIdentifier: "toggleViewCell",for:indexPath) as! ToggleViewCell;
            
            
            //cell.makeCard(cell.wrapperView, cornerRadius: 2, color: UIColor.blackColor(), shadowOpacity: 0.4)
            
            if(cell.renderData)
            {
                return cell;
            }
            cell.renderData = true;
            
            cell.topButton.setTitle("Select Resource", for: UIControl.State.normal);
            
            
            cell.topButton.backgroundColor = color;
            
            for (key,val) in categoriesList
            {
                let parentCategoryView = ParentCategoryView();
                parentCategoryView.backgroundColor = UIColor.black;
                parentCategoryView.frame = CGRect(x: 0, y: CGFloat(heightToUse2), width: screenSize.width-sub, height: fixedHeight+5)
                parentCategoryView.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
                let impArray = key.components(separatedBy: "#")
                parentCategoryView.parentFilterButton.setTitle(impArray.last!, for: UIControl.State.normal);
                
                if selectedFiltersIds.count > 0 {
                    if let _ = selectedFiltersIds.index(of: impArray.first!) {
                        parentCategoryView.parentFilterButton.setImage(UIImage(named: "checked_checkbox"), for: UIControl.State.normal);
                    }
                }
                else
                {
                    parentCategoryView.parentFilterButton.setImage(UIImage(named: "unchecked_checkbox"), for: UIControl.State.normal);
                }
                selectionArray[impArray.last!] = impArray.first
                
                parentCategoryView.parentFilterButton.addTarget(self, action: #selector(makeFilterSelected(_:)), for: UIControl.Event.touchUpInside);
                cell.innerView.addSubview(parentCategoryView);
                heightToUse2 += fixedHeight+5;
                
                for inrVal in val
                {
                    let subCategoryView = SubCategoryView();
                    subCategoryView.backgroundColor = UIColor.black;
                    subCategoryView.frame = CGRect(x: 20, y: CGFloat(heightToUse2), width: screenSize.width-sub, height: fixedHeight)
                    subCategoryView.autoresizingMask = [UIView.AutoresizingMask.flexibleRightMargin];
                    let impArray = inrVal.components(separatedBy: "#")
                    subCategoryView.childFilterButton.setTitle(impArray.last!, for: UIControl.State.normal);
                    if selectedFiltersIds.count > 0 {
                        if let _ = selectedFiltersIds.index(of: impArray.first!) {
                            subCategoryView.childFilterButton.setImage(UIImage(named: "checked_checkbox"), for: UIControl.State.normal);
                        }
                    }
                    else
                    {
                        subCategoryView.childFilterButton.setImage(UIImage(named: "unchecked_checkbox"), for: UIControl.State.normal);
                    }
                    selectionArray[impArray.last!] = impArray.first
                    subCategoryView.childFilterButton.addTarget(self, action: #selector(makeFilterSelected(_:)), for: UIControl.Event.touchUpInside);
                    cell.innerView.addSubview(subCategoryView);
                    heightToUse2 += fixedHeight;
                    
                }
                
            }
            
            
            return cell;
            
        }
        return UITableViewCell()
    }
    
    
    @objc func saveButtonPressed(_ sender:UIButton){
        if type.lowercased() == "custom".lowercased(){
            if(selectedFiltersIds.count > 0)
            {
                selectedFiltersIdsString = "[";
                for i in 0..<selectedFiltersIds.count
                {
                    if i == selectedFiltersIds.count-1{
                        selectedFiltersIdsString += "\""+String(selectedFiltersIds[i])+"\"";
                    }
                    else
                    {
                        selectedFiltersIdsString += "\""+String(selectedFiltersIds[i])+"\",";
                    }
                    
                }
                
                selectedFiltersIdsString = selectedFiltersIdsString.substring(to: selectedFiltersIdsString.endIndex)
                
                //selectedFiltersIdsString = selectedFiltersIdsString.substringToIndex(selectedFiltersIdsString.endIndex.predecessor());
                selectedFiltersIdsString += "]";
            }else{
                return
            }
        }
        print(selectedFiltersIdsString)
        
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        var baseUrl = settings.baseUrl
        baseUrl += "vsubaccountapi/customer/saveresource"
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        var postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey
        if type.lowercased() == "all".lowercased(){
            postString += "&resource=" + "[1]"
        }else{
            postString += "&resource=" + selectedFiltersIdsString
        }
        if let id = id {
            postString += "&sub_id=" + id
        }
        let request =  NSMutableURLRequest(url: NSURL(string:baseUrl)! as URL)
        request.httpMethod = "POST"
        print(baseUrl)
        print(postString)
        request.httpBody = postString.data(using: String.Encoding.utf8)
        let requestHeader = Ced_CommonVendor.getInfoPlist("requestheader") as! String
        request.setValue(requestHeader, forHTTPHeaderField: "Mobiconnectheader")
        let sessionConfig = URLSessionConfiguration.default
//        sessionConfig.httpAdditionalHeaders = [
//            "Authorization": "Basic \(Ced_CommonVendor.getAuthData())",
//        ]
        sessionConfig.timeoutIntervalForRequest = timeoutIntervalForRequest
        sessionConfig.timeoutIntervalForResource = timeoutIntervalForResource
        let session = URLSession(configuration: sessionConfig)
        let task = session.dataTask(with: request as URLRequest){
            data,response,error in
            
            guard error == nil && data != nil else
            {
                DispatchQueue.main.async {
                    Alert_File.removeLoadingIndicator(self)
                    ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error", showMsg: error!.localizedDescription)
                }
                print("error=\(error)")
                return;
                
            }
            // check for http errors
            if let httpStatus = response as? HTTPURLResponse, httpStatus.statusCode != 200
            {
                print("statusCode should be 200, but is \(httpStatus.statusCode)")
                print("response = \(response)")
                DispatchQueue.main.async
                    {
                        Alert_File.removeLoadingIndicator(self)
                }
                return;
            }
            DispatchQueue.main.async
            {
                Alert_File.removeLoadingIndicator(self)
                if let data = data{
                    guard let json = try? JSON(data: data) else {return}
                    if json["data"]["status"].stringValue == "true"{
                        self.view.showToastMsg(json["data"]["message"].stringValue)
                        Ced_CommonVendor.delay(1, closure: {
                            self.navigationController?.popViewController(animated: true)
                        })
                    }else{
                        ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error", showMsg: json["data"]["message"].stringValue)
                    }
                }
            }
        }
        task.resume()
    }
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if type == "all" || type == ""{
            return 100
        }
        return heightToUse2+100;
    }
    
    
    
    @objc func loadVendorViewData(){
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        var baseUrl = settings.baseUrl
        baseUrl += "vsubaccountapi/customer/view"
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        var postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey
        if let id = id {
            postString += "&sub_id=" + id
        }
        let request =  NSMutableURLRequest(url: NSURL(string:baseUrl)! as URL)
        request.httpMethod = "POST"
        print(baseUrl)
        print(postString)
        request.httpBody = postString.data(using: String.Encoding.utf8)
        let requestHeader = Ced_CommonVendor.getInfoPlist("requestheader") as! String
        request.setValue(requestHeader, forHTTPHeaderField: "Mobiconnectheader")
        let sessionConfig = URLSessionConfiguration.default
//        sessionConfig.httpAdditionalHeaders = [
//            "Authorization": "Basic \(Ced_CommonVendor.getAuthData())",
//        ]
        sessionConfig.timeoutIntervalForRequest = timeoutIntervalForRequest
        sessionConfig.timeoutIntervalForResource = timeoutIntervalForResource
        let session = URLSession(configuration: sessionConfig)
        let task = session.dataTask(with: request as URLRequest){
            data,response,error in
            
            guard error == nil && data != nil else
            {
                DispatchQueue.main.async
                    {
                        Alert_File.removeLoadingIndicator(self)
                        ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error", showMsg: error!.localizedDescription)
                }
                print("error=\(error)")
                return;
            }
            // check for http errors
            if let httpStatus = response as? HTTPURLResponse, httpStatus.statusCode != 200
            {
                print("statusCode should be 200, but is \(httpStatus.statusCode)")
                print("response = \(response)")
                DispatchQueue.main.async
                    {
                        Alert_File.removeLoadingIndicator(self)
                }
                return;
            }
            DispatchQueue.main.async
            {
                Alert_File.removeLoadingIndicator(self)
                if let data = data{
                    guard let json = try? JSON(data: data) else {return}
                    print(json)
                    
                    if json["data"]["status"].stringValue == "true"{
                        for selectedResource in json["data"]["selected"].arrayValue {
                            
                            if  selectedResource.stringValue.lowercased() != "all".lowercased(){                            self.selectedFiltersIds.append(selectedResource.stringValue)
                            }
                            
                        }
                        if self.selectedFiltersIds.count == 0{
                            if json["data"]["selected"].arrayValue.first?.stringValue.lowercased() == "all".lowercased(){
                                self.type = "All"
                                self.tableView.isHidden = true
                                self.selectCustomAll.setTitle("All", for: .normal)
                            }else{
                                self.type = "custom"
                                self.tableView.isHidden = false
                                self.selectCustomAll.setTitle("Custom", for: .normal)
                            }
                            
                        }else{
                            self.type = "custom"
                            self.tableView.isHidden = false
                            self.selectCustomAll.setTitle("Custom", for: .normal)
                        }
                        self.fectchParentAndChildCategories(browseByJSON: json["data"]["resource_tree"])
                        
                        self.tableView.reloadData()
                    }
                }
                
            }
        }
        task.resume()
        
    }
    
    func fectchParentAndChildCategories(browseByJSON:JSON)
    {
        // print(browseByJSON);
        for (_,val) in browseByJSON
        {
            var temp = [String]();
            for (_,inrVal) in val["children"]
            {
                temp.append(inrVal["data"].stringValue);
            }
            self.categoriesList[val["data"].stringValue] = temp;
        }
        print("browseByJSONbrowseByJSONbrowseByJSON")
        print(categoriesList)
        self.tableView.reloadData()
        
    }
    
    @objc func makeFilterSelected(_ sender:UIButton)
    {
        print("Button Pressed")
        
        if(sender.imageView!.image == UIImage(named:"unchecked_checkbox"))
        {
            print("checked")
            sender.setImage(UIImage(named: "checked_checkbox"), for: UIControl.State.normal);
            if let text = sender.titleLabel?.text {
                self.selectedFiltersIds.append("\(selectionArray[text]!)");
            }
        }
        else
        {
              print("Unchecked")
            sender.setImage(UIImage(named: "unchecked_checkbox"), for: UIControl.State.normal);
            if let text = sender.titleLabel?.text {
                if let indexToRemove = selectedFiltersIds.index(of: "\(selectionArray[text]!)") {
                    self.selectedFiltersIds.remove(at: indexToRemove);
                }
            }
        }
        
        //print(self.selectedFiltersIds);
        //print("");
    }
    
    //Mark:Show Select Custom/All
    @objc func showResourceType(_ sender:UIButton){
        let dropDown = DropDown();
        dropDown.dataSource = ["Custom","All"];
        dropDown.selectionAction = {(index, item) in
            sender.setTitle(item, for: .normal);
            self.type = item
            if item.lowercased() == "Custom".lowercased(){
                self.tableView.isHidden = false
            }else{
                self.tableView.isHidden = true
            }
            
        }
        dropDown.anchorView = sender
        dropDown.bottomOffset = CGPoint(x: 0, y:sender.bounds.height)
        
        if dropDown.isHidden {
            dropDown.show();
        } else {
            dropDown.hide();
        }
        
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
