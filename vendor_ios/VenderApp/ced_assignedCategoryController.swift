//
//  ced_assignedCategoryController.swift
//  VenderApp
//
//  Created by cedcoss on 10/02/21.
//  Copyright © 2021 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit
import RATreeView
class ced_assignedCategoryController: ced_VendorBaseClass , RATreeViewDataSource , RATreeViewDelegate {
  
    @IBOutlet weak var topLabel: UILabel!
    @IBOutlet weak var saveBtn: UIButton!
    var selectedFiltersIds = [Int]();
    var selectedFilterIdsString = [String]();
    var categoryJson: JSON!
    var data : [DataObject] = []
    var treeView : RATreeView!
    var categoriesList = [String:[String]]()
    var savedValueArray = [Int]()
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
        topLabel.backgroundColor = DynamicColor.themeColor
        topLabel.text = " Assigned Categories"
        topLabel.font = UIFont.systemFont(ofSize: 16, weight: .semibold)
       // saveBtn.addTarget(self, action: #selector(filterButtonPressed(_:)), for: .touchUpInside)
        saveBtn.addTarget(self, action: #selector(saveButtonPressed(_:)), for: .touchUpInside)
        loadData();
        // Do any additional setup after loading the view.
    }

        @objc func loadData()
        {
            var postData = [String:String]()
            let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
            let vendorId = userData["vendorId"] as! String
            let hashKey    = userData["hashKey"] as! String
            //        var postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey + "&page=\(page)";
            //        if(filter != "")
            //        {
            //            postString += "&filter="+filter;
            //        }
            postData["vendor_id"] =  vendorId
            //postData["hashkey"] = hashKey
            
            Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
            // self.sendRequest(url: "vpoapi/assign/item", parameters: postString)
            self.sendPORequest(url: "vpoapi/getAssignedCategories", params: postData)
        }
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        if requestUrl == "vpoapi/getAssignedCategories"
        {
        Alert_File.removeLoadingIndicator(self);
        if let data = data{
            guard let json = try? JSON(data: data) else {return}
            print(json);
           // categoryJson = json
            
                if(json["vendor_data"]["success"].stringValue == "true")
                {
                  
                    topLabel.text = " Assigned Categories"
                    if (json["vendor_data"]["category_list"].exists())
                    {
                        print("hsdxghasv")
                        self.fectchParentAndChildCategories(json["vendor_data"]["category_list"]);
                        self.showData()
                    }
                }
                
                else
                {
                    Alert_File.showValidationError(self,title:"Error",message:json["data"]["message"].stringValue);
                }
        }
    }
        
    if requestUrl == "vpoapi/saveAssignedCategories"
    {
        Alert_File.removeLoadingIndicator(self);
        if let data = data{
            guard let json = try? JSON(data: data) else {return}
            print(json);
            if json["vendor_data"]["success"].stringValue == "true"
            {
                self.view.makeToast(json["vendor_data"]["message"].stringValue, duration: 2.0, position: .center)
//                self.loadData()
            }
            else {
                Alert_File.showValidationError(self, title: "Error", message: json["data"]["message"].stringValue)
            }
        }
    }
        
    }
    
    @objc func showData()
    {
        print("-----selected filter ids-----")
        print(selectedFiltersIds)
        var dataSource=[DataObject]()
        let maincatename="default"
        let maincateimage="unchecked"
        let maincateId="10000000"
        var hasChildd="false"
        if let catJson = categoryJson
        {
            hasChildd="true"
        }
        
        var subDataSource=[DataObject]()
        if hasChildd=="true"{
            subDataSource=addDataToDataSource(value: categoryJson, cateName: maincatename, cateId: maincateId, cateImage: maincateimage)
            dataSource=subDataSource
        }
        self.data=dataSource
        for index in categoryJson.arrayValue
        {
            print(index["saved_value"])
            savedValueArray.append(index["saved_value"].intValue)
            if (index["sub_categories"]).exists()
            {
                for index1 in index["sub_categories"].arrayValue
                {
                    print(index1["saved_value"])
                    savedValueArray.append(index1["saved_value"].intValue)
                }
            }
        }
        print(savedValueArray)
        setupTreeView()
    }
    
    func addDataToDataSource(value:JSON, cateName: String, cateId: String, cateImage: String)->[DataObject]{
        
        var cellDataSource=[DataObject]();
        for index in value.arrayValue
        {
            
            let mainName = index["main_category"].stringValue.components(separatedBy: "#").filter { !$0.isEmpty }
            print(mainName)
            if index["saved_value"].stringValue == "1"{
            self.selectedFiltersIds.append(Int(mainName[0]) ?? 0)
            self.selectedFilterIdsString.append(mainName[0])
            }
            let cateName=mainName[1]
            let cateImage="unchecked"
            let cateId=mainName[0]
            var hasChildd="false"
            if(index["sub_categories"].exists())
            {
                hasChildd="true"
            }
            if hasChildd=="true"{
                let dataSource=addDataToDataSource(value: index["sub_categories"], cateName: cateName, cateId: cateId, cateImage: cateImage)
                
                let data=DataObject(name: cateName, children: dataSource ,id: cateId,image:cateImage)
                cellDataSource.append(data)
            }
            else
            {
                let data=DataObject(name: cateName,id: cateId,image:cateImage)
                cellDataSource.append(data)
            }
        }
         
        
        return cellDataSource
        
    }
    
    @objc func setupTreeView() -> Void {
        let color = Ced_CommonVendor.UIColorFromRGB("#ff0000");
        
        /*selectCategoryView = SelectedCategoriesView(frame: CGRect(x: 0, y: 110, width: self.view.frame.width, height: 0))
        self.view.addSubview(selectCategoryView);
        selectCategoryView.stackViewHeight.constant = 0;*/
        treeView = RATreeView(frame: CGRect(x: 0, y: 150, width: self.view.frame.width, height: self.view.frame.height - 110))
        
        treeView.register(UINib(nibName: String(describing: CategoryTreeTableViewCell.self), bundle: nil), forCellReuseIdentifier: String(describing: CategoryTreeTableViewCell.self))
        treeView.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        treeView.delegate = self;
        treeView.dataSource = self;
        treeView.treeFooterView = UIView()
        treeView.backgroundColor = .clear
        view.addSubview(treeView)
        //treeView.expandRow(forItem: data[0])
    }
    
    func treeView(_ treeView: RATreeView, numberOfChildrenOfItem item: Any?) -> Int {
        if let item = item as? DataObject {
            return item.children.count
        } else {
            return self.data.count
        }
    }
    
    func treeView(_ treeView: RATreeView, cellForItem item: Any?) -> UITableViewCell {
        print("CELLCREATED")
        print(categoryJson)
        let cell = treeView.dequeueReusableCell(withIdentifier: String(describing: CategoryTreeTableViewCell.self)) as! CategoryTreeTableViewCell
        let item = item as! DataObject
        
        cell.tag = Int(item.id)!;
        if let index = selectedFiltersIds.index(of: Int(item.id)!)
        {
           // cell.img.image = UIImage(named: "checked")
                item.image="checked";
            

            /*if(!selectedCategories.contains(item.name))
            {
                selectedCategories.append(item.name)
            }*/

        }
       
        else
        {
            item.image="unchecked";
            /*if let categSelected = selectedCategories.index(of: item.name)
            {
                self.selectedCategories.remove(at: categSelected);
            }*/
            
        }
        //reloadCategories();
        print(item.name)
        print(item.id)
        print(item.children)
        let level = treeView.levelForCell(forItem: item)
        let detailsText = "Number of children \(item.children.count)"
        cell.selectionStyle = .none
        
        cell.setup(withTitle: item.name, detailsText: detailsText, level: level, additionalButtonHidden: false,img: item.image)
        
        return cell
    }
    
    func treeView(_ treeView: RATreeView, child index: Int, ofItem item: Any?) -> Any {
        if let item = item as? DataObject {
            return item.children[index]
        } else {
            
            return data[index] as AnyObject
            
        }
    }
    

    
    @objc func treeView(_ treeView: RATreeView, didSelectRowForItem item: Any) {
       
        if let cell=treeView.cell(forItem: item) as? CategoryTreeTableViewCell{
            let image=cell.rightImage.image
            let checkImage=UIImage(named: "IQButtonBarArrowRight")
            let currentImage = cell.img.image
            let checkboxImage = UIImage(named: "unchecked")
            let tag = cell.tag;
            let item = item as! DataObject
            print("--select tag--")
            print(tag)
            if(currentImage == checkboxImage)
            {
                cell.img.image = UIImage(named: "checked")
                selectedFiltersIds.append(tag);
                selectedFilterIdsString.append(String(tag))
                /*if(!selectedCategories.contains(item.name))
                {
                    selectedCategories.append(item.name)
                }*/
            }
            else
            {
                cell.img.image = UIImage(named: "unchecked")
                if let indexToRemove = selectedFiltersIds.index(of: tag) {
                    self.selectedFiltersIds.remove(at: indexToRemove);
                  
                    selectedFilterIdsString.remove(at: indexToRemove);
                    
                }
                /*if let categSelected = selectedCategories.index(of: item.name)
                {
                    self.selectedCategories.remove(at: categSelected);
                }*/
            }
            //reloadCategories()
            if image==checkImage{
                cell.rightImage.image=UIImage(named: "down-arrow")
            }
            else
            {
                cell.rightImage.image=UIImage(named: "IQButtonBarArrowRight")
                
            }
            if let data=item as? DataObject{
                if data.children.count == 0{
                    cell.rightImage.image=nil
                }
            }
            cell.rightImage.tintColor=UIColor.black
//            if let id = selectedFiltersIds.index(of: tag) {
//                let idString = String(id)
//                selectedFilterIdsString.append(idString)
//            }
//
            print(selectedFiltersIds)
           print(selectedFilterIdsString)
        }
    }

    
    func fectchParentAndChildCategories(_ browseByJSON:JSON)
    {
        print("hello")
        categoryJson = browseByJSON;
        print(browseByJSON);
        for (_,val) in browseByJSON
        {
            print("---------\(val)")
            if val["sub_categories"] != nil
            {
                var temp = [String]();
                for (_,inrVal) in val["sub_categories"]
                {
                    temp.append(inrVal["main_category"].stringValue);
                }
                self.categoriesList[val["main_category"].stringValue] = temp;
                print("--")
            }
        }
        print("---")
        print(categoriesList)
        //return;
        
    }
    
    
    
    @objc func saveButtonPressed(_ sender : UIButton)
    {
        print("hello")
        var postData = [String:String]()
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        postData["vendor_id"] =  vendorId
        var post = [String:String]()
        for i in 0..<selectedFiltersIds.count
        {
            print(selectedFilterIdsString)
            post["\(String(i))"] = selectedFilterIdsString[i]
        }
        var param = convertDicTostring(str: post)
        print(param)
        postData["categories"] = param
        print(post)
        print(postData)
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        // self.sendRequest(url: "vpoapi/assign/item", parameters: postString)
       // self.sendAnyRequest(url: "vpoapi/saveAssignedCategories", params: postData, withRest: true)
       // self.fetchDataThroughRequest(url: "vpoapi/saveAssignedCategories", params: postData, withRest: false)
        sendPORequest(url: "vpoapi/saveAssignedCategories", params: postData)
    }
    
    
     
    
    
}

