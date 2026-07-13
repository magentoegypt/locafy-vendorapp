//
//  cedCreateTableRateShipping.swift
//  VenderApp
//
//  Created by cedcoss on 22/12/17.
//  Copyright © 2017 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class cedCreateTableRateShipping: ced_VendorBaseClass {
    
    @IBOutlet weak var stackView: UIStackView!
    @IBOutlet weak var price: UITextField!
    @IBOutlet weak var conditionValue: UITextField!
    @IBOutlet weak var conditionName: UIButton!
    @IBOutlet weak var destinationZipcode: UITextField!
    @IBOutlet weak var destinationRegionId: UITextField!
    @IBOutlet weak var destinationCountryId: UIButton!
    @IBOutlet weak var saveSettings: UIButton!
    var textFieldTemp = UITextField()
    var countryArray = [String:String]()
    var countryList = [String]()
    var conditionArray = ["Weight vs. Destination":"package_weight","Price vs. Destination":"package_value","# of Items vs. Destination":"package_qty"]
    var conditionList = ["Weight vs. Destination","Price vs. Destination","# of Items vs. Destination"]
    
    var statesArray = [String:String]();
    var stateDropDownIsVisible = true;// to check dropdown is visible or textfield
    var stateSelectionViewTag = -1;//tag of the state view
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        saveSettings.addTarget(self, action: #selector(cedCreateTableRateShipping.saveSettings(sender: )), for: .touchUpInside)
        destinationCountryId.addTarget(self, action: #selector(cedCreateTableRateShipping.showCountryDropDown(_:)), for: .touchUpInside)
        conditionName.addTarget(self, action: #selector(cedCreateTableRateShipping.showRequiredDropdown(_:)), for: .touchUpInside)
        self.fetchCountryListing()
    }
    
    @objc func fetchCountryListing()
    {
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        let baseURL = "vendorapi/index/getcountry/";
        print(baseURL);
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        
        let postString = "vendor_id="+vendorId+"&hashkey="+hashKey;
        self.sendRequest(url: baseURL, parameters: postString)
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        if let data = data {
            if requestUrl=="vendorapi/index/getcountry/"
            {
                guard let json = try? JSON(data: data) else {return}
                Alert_File.removeLoadingIndicator(self);
                let countryListing = json["country"].arrayValue;
                for d in 0..<countryListing.count
                {
                    let key = countryListing[d]["value"].stringValue;
                    let value = countryListing[d]["label"].stringValue
                    self.countryArray[value] = key;
                    self.countryList.append(value)
                }
            }else if requestUrl == "vendorapi/vtable/saveRates/"{
                guard let json = try? JSON(data: data) else {return}
                print(NSString(data: data, encoding: String.Encoding.utf8.rawValue))
                if json["status"].stringValue == "false" {
                    ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error", showMsg: json["message"].stringValue)
                }else{
                    self.navigationController?.popViewController(animated: true)
                }
                print(json)
            }
        }
    }
    
    @objc func saveSettings(sender:UIButton) {
        if self.conditionValue.text == "" || self.price.text == "" || self.destinationCountryId.titleLabel?.text == "" {
            ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error", showMsg: "Please Fill Required Fields")
            return
        }
        
        if let priceValue = price.text {
            if let conditionValue = self.conditionValue.text {
                if let zipcode = destinationZipcode.text {
                     var regionId = destinationRegionId.text
                    if self.stateDropDownIsVisible {
                       if let state = self.view.viewWithTag(3422) as? UIButton {
                            if let label = state.titleLabel?.text {
                             regionId = self.statesArray[label]
                            }
                        }
                    }
                        if let contitioname = conditionName.titleLabel?.text {
                            if let countryName = destinationCountryId.titleLabel?.text {
                                  var postData  = ""
                                var conditionName = self.conditionArray[contitioname]
                                if let countryID = self.countryArray[countryName] {
                                    postData  = "dest_country_id="+countryID+"&dest_region_id="
                                }
                                postData += regionId!+"&condition_value="+conditionValue
                                 if self.stateDropDownIsVisible {
                                    postData += "&region="+regionId!
                                }
                                if conditionName == nil {
                                    conditionName = ""
                                }
                                postData += "&price="+priceValue+"&condition_name="+conditionName!+"&dest_zip="+zipcode
                               
                               
                                let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
                                let vendorId = userData["vendorId"] as! String
                                let hashKey    = userData["hashKey"] as! String
                                let baseURL = "vendorapi/index/getcountry/";
                                print(baseURL);
                                Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
                                
                                let postString = postData + "&vendor_id="+vendorId+"&hashkey="+hashKey;
                                print(postString)
                                self.sendRequest(url: "vendorapi/vtable/saveRates/", parameters:postString)
                            }
                        }
                    
                }
            }
        }
    }
    
    @objc func showCountryDropDown(_ sender:UIButton)
    {
        let dropDown = DropDown();
        dropDown.dataSource = countryList
        dropDown.selectionAction = {(index, item) in
            sender.setTitle(item, for: UIControl.State());
            self.fetchStatesInsideCountry(self.countryArray[item]!)
        }
        dropDown.anchorView = sender
        dropDown.bottomOffset = CGPoint(x: 0, y:sender.bounds.height)
        
        if dropDown.isHidden {
            dropDown.show();
        } else {
            dropDown.hide();
        }
    }
    
    @objc func showRequiredDropdown(_ sender:UIButton)
    {
        let dropDown = DropDown();
        dropDown.dataSource = conditionList
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
    
    
    @objc func fetchStatesInsideCountry(_ item:String)
    {
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        
       var baseURL = settings.baseUrl
        baseURL += "vendorapi/index/getcountry/";
        
        print(baseURL);
        
        var request = URLRequest(url: URL(string: baseURL)!);
        
        request.httpMethod = "POST";
        var postString = "vendor_id="+vendorId+"&hashkey="+hashKey;
        postString += "&country_code="+item;
        
        print(postString);
        
        request.httpBody = postString.data(using: String.Encoding.utf8);
        
        let requestHeader = Ced_CommonVendor.getInfoPlist("requestheader") as! String
        request.setValue(requestHeader, forHTTPHeaderField: "Mobiconnectheader")
        
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        
        let sessionConfig = URLSessionConfiguration.default
//        sessionConfig.httpAdditionalHeaders = [
//            "Authorization": "Basic \(Ced_CommonVendor.getAuthData())",
//        ]
        sessionConfig.timeoutIntervalForRequest = timeoutIntervalForRequest
        sessionConfig.timeoutIntervalForResource = timeoutIntervalForResource
        let session = URLSession(configuration: sessionConfig)
        let task = session.dataTask(with: request, completionHandler: {
            // check for fundamental networking error
            data, response, error in
            guard error == nil && data != nil else
            {
                print("error=\(error)");
                DispatchQueue.main.async
                    {
                        Alert_File.removeLoadingIndicator(self)
                        ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error", showMsg: error!.localizedDescription)
                }
                
                return;
            }
            
            // check for http errors
            if let httpStatus = response as? HTTPURLResponse, httpStatus.statusCode != 200
            {
                print("statusCode should be 200, but is \(httpStatus.statusCode)")
                print("response = \(response)");
                DispatchQueue.main.async
                    {
                        Alert_File.removeLoadingIndicator(self);
                        if(httpStatus.statusCode == -1009){
                            ced_showError.showNoNetWork(self)
                        }else{
                            ced_showError.showNoModule(self)
                        }
                        return;
                }
                return;
            }
            
            // code to fetch values from response :: start
            
            DispatchQueue.main.async
            {
                Alert_File.removeLoadingIndicator(self);
                if let data = data{
                    guard let jsonData = try? JSON(data: data) else {return}
                    print("jsonData[\"data\"]DDDEEEVVV");
                    print(jsonData);
                    
                    if(jsonData["success"].stringValue == "false")
                    {
                        self.stateDropDownIsVisible = false
                        if let dropDownButtonView = self.stackView.arrangedSubviews[3] as? DropDownButtonView {
                            self.stackView.removeArrangedSubview(dropDownButtonView);
                            dropDownButtonView.removeFromSuperview();
                            
                            self.stackView.insertArrangedSubview(self.textFieldTemp, at: 3)
                            
                            self.textFieldTemp.heightAnchor.constraint(equalToConstant: 40).isActive = true;
                            self.setLeadingAndTralingSpaceFormParentView(self.textFieldTemp, parentView:self.stackView);
                        }
                        
                        
                    }
                    else
                    {
                        self.stateDropDownIsVisible = true
                        for (_,val) in jsonData["states"]
                        {
                            self.statesArray[val["name"].stringValue] = val["region_id"].stringValue;
                        }
                        
                        print("self.statesArray");
                        print(self.statesArray);
                        if let textField = self.destinationRegionId {
                            print(textField.text as Any);
                            self.textFieldTemp = textField
                            self.stackView.removeArrangedSubview(textField);
                            textField.removeFromSuperview();
                            let dropDownButtonView = DropDownButtonView();
                            dropDownButtonView.translatesAutoresizingMaskIntoConstraints = false;
                            dropDownButtonView.dropDownButton.titleLabel?.font = UIFont.systemFont(ofSize: 12)
                            dropDownButtonView.dropDownButton.tag = 3422
                            dropDownButtonView.dropDownButton.addTarget(self, action: #selector(cedCreateTableRateShipping.showStatesDropdown(_:)), for: UIControl.Event.touchUpInside);
                            
                            self.stackView.insertArrangedSubview(dropDownButtonView, at: 3)
                            
                            dropDownButtonView.heightAnchor.constraint(equalToConstant: 40).isActive = true;
                            self.setLeadingAndTralingSpaceFormParentView(dropDownButtonView, parentView:self.stackView);
                        }
                        
                    }
                }
            }
        })
        
        task.resume();
        
    }

    @objc func showStatesDropdown(_ sender:UIButton)
    {
        let dropDown = DropDown();
        var dropDownToShow = Array(statesArray.keys);
       // dropDownToShow = dropDownToShow.sorted();
        if let indexToRemove = dropDownToShow.index(of: "--Please Select--") {
            dropDownToShow.remove(at: indexToRemove);
        }
        
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
    
    
    
}
