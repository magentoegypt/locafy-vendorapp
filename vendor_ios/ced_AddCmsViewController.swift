//
//  ced_AddCmsViewController.swift
//  VenderApp
//
//  Created by cedcoss on 06/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_AddCmsViewController: ced_VendorBaseClass, UITableViewDataSource, UITableViewDelegate {
    
    
    @IBOutlet weak var save: UIButton!
    @IBOutlet weak var GeneralInfo: UIButton!
    @IBOutlet weak var mainTitle: UILabel!
    @IBOutlet weak var tableView: UITableView!
    var generalInfoClicked = true
    var isclicked = false
    var height = 0
    var baseURL = String();
    var backData = false
    var fieldcell = ced_AddTableViewCell()
    var id = ""
    var title2 = ""
    var status = ""
    
    // var urlK = ""
    // var content = ""
    var defaultView = false
    var defaultStore = false
    var button1 = UIButton()
    var button2 = UIButton()
    var storeData = [[String:String]]()
    var storeViews = [[String:String]]()
    var array = [String]()
    var temp = [String:String]()
    override func viewDidLoad() {
        super.viewDidLoad()
        
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as! String
        save.layer.cornerRadius=2
        let color = Ced_CommonVendor.UIColorFromRGB("#ff0000");
        save.backgroundColor = color;
        save.tintColor = UIColor.white
        save.addTarget(self, action: #selector(sendClicked(_:)), for: .touchUpInside)
        save.setTitle("Save Block".localized, for: .normal)
        GeneralInfo.setTitle("General Information".localized, for: .normal)
        GeneralInfo.layer.cornerRadius=2
        GeneralInfo.backgroundColor =  Ced_CommonVendor.UIColorFromRGB(colorString)
        mainTitle.backgroundColor = Ced_CommonVendor.UIColorFromRGB(colorString)
        mainTitle.textColor = UIColor.white
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        if backData
        {
            mainTitle.text = "  Edit Block".localized
            baseURL = "rest/all/V1/vcmsblock/view"
            var poststring = [String:String]()
            poststring["vendor_id"] = vendorId
            poststring["block_id"]=id
            
            let postString = "vendor_id="+vendorId+"&hashkey="+hashKey+"&block_id="+id
            //self.sendRequest(url: baseURL, parameters: postString)
            
            self.sendRequestCms(url: baseURL, params: poststring)
            Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
        }
        else
        {
            mainTitle.text = "  Create Cms Block".localized
            getStoreView()
            self.tableView.delegate = self
            self.tableView.dataSource = self
        }
        
        GeneralInfo.tintColor = UIColor.white
        GeneralInfo.addTarget(self, action: #selector(SendData(_:)), for: .touchUpInside)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    @objc func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 1
    }
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if generalInfoClicked
        {
            return 550
        }
        else
        {
            return (CGFloat(550 - height))
        }
    }
    
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        var colorString = Ced_CommonVendor.getInfoPlist("themecolor") as! String
        let cell=tableView.dequeueReusableCell(withIdentifier: "cmsBody") as! ced_AddTableViewCell
        fieldcell = cell
        
        cell.textView.layer.cornerRadius = 1
        cell.textView.layer.borderWidth = 1
        height = Int(cell.Top.constant) + Int(cell.viewHeight.constant)
        cell.statusView.dropDownButton.addTarget(self, action: #selector(statusClicked(_:)), for: .touchUpInside)
        //  cell.StoreView.layer.borderWidth = 1
        //  cell.DefautValue.tag = 5
        // cell.defaultStore.tag = 6
        //  button1 = cell.DefautValue
        //  cell.DefautValue.addTarget(self, action: #selector(DefaultSelected(_:)), for: .touchUpInside)
        //  button2 = cell.defaultStore
        //  cell.defaultStore.addTarget(self, action: #selector(DefaultSelected(_:)), for: .touchUpInside)
        cell.statusView.entityNameLabel.text = "Status *".localized
        cell.statusView.entityNameLabel.font=UIFont(name: "Roboto-Bold", size: 17)
        if backData
        {
            cell.textView.text = temp["content"]
            cell.Blocktitle.text = temp["title"]
            if temp["is_active"] == "0"
            {
                cell.statusView.dropDownButton.setTitle("Disable".localized, for: UIControl.State())
            }
            else
            {
                cell.statusView.dropDownButton.setTitle("Enable".localized, for: UIControl.State())
            }
            //                if storeData[indexPath.row]["store_id"] == "0"
            //                {
            //                    cell.defaultStore.isSelected = false
            //                    cell.DefautValue.isSelected = true
            //                }
            //                else
            //                {
            //                    cell.defaultStore.isSelected = true
            //                    cell.DefautValue.isSelected = false
            //                }
            //  cell.status.setTitle(status, for: UIControlState())
            cell.url.text = temp["identifier"]
        }
        return cell
    }
    //     @objc func DefaultSelected(_ sender:UIButton)
    //     {
    //        if sender.tag == 5
    //        {
    //            if defaultView
    //            {
    //                button1.isSelected = false
    //                defaultView = false
    //            }
    //            else
    //            {
    //                button1.isSelected = true
    //                defaultView = true
    //            }
    //        }
    //        else if sender.tag == 6
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
    
    
    @objc func sendClicked(_ sender:UIButton)
    {
        if (fieldcell.Blocktitle.text == "") || (fieldcell.url.text == "")
        {
            
            self.view.showToastMsg(" Title and Identifier  is Required.".localized);
        }
        else
        {
            let title1 = fieldcell.Blocktitle.text
            let urlk = fieldcell.url.text
            var status = fieldcell.statusView.dropDownButton.currentTitle
            var state = Bool()
            if status == "Enable".localized
            {
                state=true
                status = "1"
            }
            else
            {
                state=false
                status = "0"
            }
            let content1  = fieldcell.textView.text
            print(array)
            let  storestring = JSONConvert(data: array)
            let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
            let vendorId = userData["vendorId"] as! String
            let hashKey    = userData["hashKey"] as! String
            Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
            if backData
            {
                baseURL = "rest/all/V1/vcmsblock/update"
                
                var poststring = [String:Any]()
                
                poststring["block_id"] = id
                poststring["title"] = title1
                poststring["vendor_id"] = Int(vendorId)
                poststring["identifier"] = urlk
                poststring["content"] = content1
                poststring["is_active"] = state
                poststring["store_id"] = array
                
                
                var postString = "vendor_id="+vendorId+"&hashkey="+hashKey+"&block_id="+id+"&title="+title1!+"&urlkey="+urlk!+"&content="+content1!+"&status="+status!
                postString += "&store="+storestring
                print(postString)
                print(poststring)
                self.sendRequestAddCms(url: baseURL, params: poststring ,mainkey: "cmsBlockData" )
            }
            else
            {
                baseURL = "rest/all/V1/vcmsblock/save"
                
                var poststring = [String:Any]()
                
           
               poststring[ "title"] =  title1
               poststring[ "vendor_id"] =  Int(vendorId)
               poststring[ "identifier"] =  urlk
               poststring[ "content"] =  content1
               poststring[ "is_active"] =  state
               poststring[ "store_id"] =  array
                
                var postString = "vendor_id="+vendorId+"&hashkey="+hashKey+"&title="+title1!+"&urlkey="+urlk!+"&content="+content1!+"&status="+status!
                postString += "&store="+storestring
                print(postString)
                
                self.sendRequestAddCms(url: baseURL, params: poststring ,mainkey: "cmsBlockData" )
                
                //self.sendRequest(url: baseURL, parameters: postString)
                //var request = URLRequest(url: URL(string: baseURL)!);
            }
        }
        
    }
    @objc func statusClicked(_ sender:UIButton)
    {
        let dropDown = DropDown();
        dropDown.dataSource = ["Enable".localized,"Disable".localized];
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
    
    @objc func SendData(_ sender:UIButton)
    {
        if isclicked == true
        {
            generalInfoClicked = false
            isclicked = false
            self.tableView.reloadData()
        }
        else
        {
            isclicked = true
            generalInfoClicked = true
            self.tableView.reloadData()
        }
    }
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        Alert_File.removeLoadingIndicator(self)
        if let data = data{
            if(requestUrl=="rest/all/V1/vcmsblock/save")
            {
                
                guard let json = try? JSON(data: data) else {return}
                print(json)
                if(json["success"].stringValue.lowercased() == "false"){
                    self.view.showToastMsg(json["message"].stringValue)
                }else if(json["success"].stringValue.lowercased() == "true"){
                    self.view.showToastMsg(json["message"].stringValue)
                    Ced_CommonVendor.delay(1.0) {
                        self.navigationController?.popViewController(animated: true)
                    }
                }
            }
            else if(requestUrl=="rest/all/V1/vcmsblock/update")
            {
                Alert_File.removeLoadingIndicator(self)
                guard let json = try? JSON(data: data) else {return}
                print(json)
                if(json["status"].stringValue.lowercased() == "false"){
                    self.view.showToastMsg(json["message"].stringValue)
                }else if(json["success"].stringValue.lowercased() == "true"){
                    self.view.showToastMsg(json["message"].stringValue)
                    Ced_CommonVendor.delay(1.0) {
                        self.navigationController?.popViewController(animated: true)
                    }
                }
                
            }
            else
            {
                guard let json = try? JSON(data: data) else {return}
                print(json)
                if(json["success"].stringValue.lowercased() == "false"){
                    self.view.showToastMsg(json["message"].stringValue)
                }else if(json["success"].stringValue.lowercased() == "true"){
                    self.parseProductdata(json)
                }
                
            }
        }
        
    }
    var store = String()
    func parseProductdata(_ json:JSON){
        
        let jsonData = json["cms_list"][0].dictionary
        var content = ""
        let title = jsonData!["title"]?.string
        let id = jsonData!["block_id"]?.intValue
        if jsonData!["content"]?.string != nil
        {
            content = (jsonData!["content"]?.string)!
        }
        let store_id = jsonData!["store_id"]?.arrayValue
        let status = jsonData!["is_active"]?.stringValue
        let identifier = jsonData!["identifier"]?.string
        store = jsonData!["store_id"]![0].stringValue
        //      let page_layout = result["page_layout"].string
        temp = ["block_id":"\(id)","title":title!,"is_active":status ?? "1","content":content,"identifier":identifier!,"store_id":"\(store_id!)"]
        Alert_File.removeLoadingIndicator(self);
       
        //self.storeData.append(temp as! [String : String])
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
                        ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error", showMsg: error!.localizedDescription)
                }
                print("error=\(error)")
                return;
            }
            
            // check for http errors
            if let httpStatus = response as? HTTPURLResponse, httpStatus.statusCode != 200
            {
                DispatchQueue.main.async
                    {
                        
                        Alert_File.removeLoadingIndicator(self)
                        print("statusCode should be 200, but is \(httpStatus.statusCode)")
                        print("response = \(response)")
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
            DispatchQueue.main.async
            {
                Alert_File.removeLoadingIndicator(self)
                if let data = data{
                    print("HELLO JSON")
                    var temp1=[String:String]()
                    guard let json = try? JSON(data: data) else {return}
                    let val = json["dropdown_data"]["store_ids"].array
                    self.fieldcell.scrollwidth.constant = self.view.frame.width
                    for item in val!
                    {
                        print(item)
                        temp1["label"]=item["label"].string!
                        let button = UIButton(frame: CGRect(x: 0, y: 0, width:100 , height: 20))
                        if item["value"] == 0
                        {
                            print(self.temp["store_id"])
                            if self.temp["store_id"] != ""
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
                            button.contentHorizontalAlignment = UIControl.ContentHorizontalAlignment.left
                            button.setTitleColor(.black, for: UIControl.State())
                            button.addTarget(self, action: #selector(self.DefaultSelected(_:)),for: .touchUpInside)
                            self.fieldcell.stackView.addArrangedSubview(button)
                        }
                        else
                        {
                            if item["value"].array?.count != 0
                            {
                                // print(temp1["label"])
                                button.setTitle(String(describing: temp1["label"]!), for: UIControl.State())
                                button.setTitleColor(.black, for: UIControl.State())
                                button.titleLabel?.font =  UIFont(name: "Roboto-Bold", size: 18)
                                button.contentHorizontalAlignment = UIControl.ContentHorizontalAlignment.left
                                button.layer.cornerRadius = 2
                                self.fieldcell.stackHeight.constant += 30
                                
                                self.fieldcell.stackView.addArrangedSubview(button)
                                
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
                                    if self.temp["store_id"] != "" && self.backData
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
                                    self.fieldcell.stackHeight.constant += 30
                                    self.fieldcell.stackView.addArrangedSubview(button)
                                }
                            }
                            else
                            {
                                let button = UILabel(frame: CGRect(x: 0, y: 0, width:100 , height: 20))
                                button.font =  UIFont(name: "Roboto-Bold", size: 16)
                                button.text =  temp1["label"]!
                                button.textColor = UIColor.black
                                button.textAlignment = NSTextAlignment.left;
                                self.fieldcell.stackHeight.constant += 30
                                self.fieldcell.stackView.addArrangedSubview(button)
                            }
                        }
                    }
                }
                
            }
        })
        task.resume()
    }
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

