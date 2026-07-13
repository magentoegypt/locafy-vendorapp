//
//  ced_NewShippingMethodVC.swift
//  VenderApp
//
//  Created by cedcoss on 02/11/20.
//  Copyright © 2020 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_NewShippingMethodVC: ced_VendorBaseClass,UITableViewDelegate,UITableViewDataSource {

    
    @IBOutlet weak var shippingTableView: UITableView!
    
    @IBOutlet weak var topWrapperView: UIView!
    @IBOutlet weak var leftButton: UIButton!
    @IBOutlet weak var rightButton: UIButton!
    @IBOutlet weak var rightButtonWidth: NSLayoutConstraint!
    

        let screenSize: CGRect = UIScreen.main.bounds;
        
        var baseURL = String();
        
        var valuesArray = [JSON]();
        var shippingMethodsFields = [String: [[String:Any]]]();
        
        var heightToUse = CGFloat(0);
        let dropDownSectionHeight : CGFloat = 70;
        let checkboxHeight : CGFloat = 30;
        let padding : CGFloat = 5;
        
        var dropDownValuesArray = [String]();
        var dropDownInfoArray = [String:String]();
        
        var activeShippingMethodsValueArray = [String]();
        var shippingMethodsArray = [String:String]();
         var fixedHeight : CGFloat = 70;
        var upsActive = "";
        var selectedSelectArray = [Int:[String:String]]()
        var makeFieldsArray = [Int:[String:Any]]()
        
        var newAddedzoneViews = [Int:[String:String]]()
        var zoneTypeArray = ["Fixed", "Per/Metric"]
        var initialZoneViewHeight : CGFloat = 110
        var i = 0
    var viewHeight: NSLayoutConstraint?
        let zoneStackView = labelStackcombo()
        override func viewDidLoad() {
            super.viewDidLoad()
            self.view.backgroundColor = DynamicColor.ViewBackgroundColor
            shippingTableView.backgroundColor = DynamicColor.ViewBackgroundColor
            let tlabel = UILabel(frame: CGRect(x: 0, y: 0, width: 200, height: 40));
            self.title = NSLocalizedString("Shipping Methods", comment: "")
            tlabel.text = self.title
            tlabel.textColor = UIColor.white
            tlabel.font = UIFont(name: "Roboto-Bold", size: 21.0)
            tlabel.backgroundColor = UIColor.clear
            tlabel.adjustsFontSizeToFitWidth = true
            self.navigationItem.titleView = tlabel
            
            // Do any additional setup after loading the view.
            
            self.leftButton.titleLabel?.font=UIFont(name: "Roboto-Regular", size: 17)
            self.rightButton.titleLabel?.font=UIFont(name: "Roboto-Regular", size: 17)
            self.leftButton.setTitle(NSLocalizedString("Shipping Methods", comment: ""), for: UIControl.State());
            self.rightButton.setTitle(NSLocalizedString("Save".localized, comment: ""), for: UIControl.State());
            self.rightButton.addTarget(self, action: #selector(saveShippingMethods(_:)), for: UIControl.Event.touchUpInside);
            self.rightButtonWidth.constant = screenSize.width/3;
            
            ced_navigationBarController().addNavButton(self,str:"no")
            
            let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
            let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
            let colorGreen = Ced_CommonVendor.UIColorFromRGB("#002f63");
            rightButton.backgroundColor = colorGreen
            topWrapperView.backgroundColor = color
           //rightButton.setButtonGreenColor()
            
           
            self.shippingTableView.estimatedRowHeight = 100
            self.shippingTableView.rowHeight = UITableView.automaticDimension
            self.fetchShippingMethodsInfo();
            reloadTable()
        }
    func reloadTable(){
        self.shippingTableView.dataSource = self;
                   self.shippingTableView.delegate = self;
        self.shippingTableView.reloadData()
         viewHeight =  zoneStackView.heightAnchor.constraint(equalToConstant: initialZoneViewHeight)
        viewHeight?.isActive = true
        //zoneStackView.heightAnchor.constraint(equalToConstant: initialZoneViewHeight).isActive = true
    }
        override func didReceiveMemoryWarning() {
            super.didReceiveMemoryWarning()
            // Dispose of any resources that can be recreated.
        }
        
        // MARK: - Table view data source
        
        @objc func numberOfSections(in tableView: UITableView) -> Int
        {
            return 1;
        }
        
        @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
        {
            if(self.shippingMethodsFields.count == 0)
            {
                return 0;
            }
            return 1;
        }
        
        @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
        {
            let cell = shippingTableView.dequeueReusableCell(withIdentifier: "longSingleCell", for: indexPath) as! LongSingleTableViewCell;
            cell.contentView.backgroundColor = DynamicColor.ViewBackgroundColor
            
            for views in cell.mainStack.subviews{
                views.removeFromSuperview()
            }
            
            
            
            var counter = 1000;
            for(key,value) in shippingMethodsFields
            {
                //*** top label **//
                heightToUse += CGFloat(10);
                let ts_label_view = TS_Label_View();
                ts_label_view.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
                //ts_label_view.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width-10,height: CGFloat(40));
               ts_label_view.heightAnchor.constraint(equalToConstant: 40).isActive = true
                heightToUse += CGFloat(50);
                ts_label_view.center.x = cell.mainStack.center.x;
                if let keyArray = key.components(separatedBy: "#") as? [String]
                {
                    ts_label_view.entityNameLabel.text = keyArray[1];
                }
                
                
                //design code
                let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
                let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
                ts_label_view.backgroundColor = color
                ts_label_view.entityNameLabel.font=UIFont(name: "Roboto-Regular",size: 15)
                ts_label_view.entityNameLabel.textAlignment = NSTextAlignment.center;
                ts_label_view.entityNameLabel.textColor = UIColor.white;
                ts_label_view.entityNameLabel.backgroundColor = color;
                ts_label_view.layer.borderColor = color.cgColor
                ts_label_view.layer.borderWidth = 1.0
                ts_label_view.layer.cornerRadius = 5
                
                
                heightToUse += CGFloat(5);
                
                cell.mainStack.addArrangedSubview(ts_label_view);
                var x = 0;
                var fieldToPost = ""
                for val in value{
                    if let fieldtopost = val["field_to_post"] as? JSON{
                        fieldToPost = fieldtopost.stringValue
                    }
                    print("val shipping = \(val)")
                    if let type = val["type"] as? JSON{
                        if(type.stringValue == "select")
                        {
                            let label_DropDown_Combo_View = Label_DropDown_Combo_View();
                            label_DropDown_Combo_View.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
                            //label_DropDown_Combo_View.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width-30,height: dropDownSectionHeight);
                            label_DropDown_Combo_View.heightAnchor.constraint(equalToConstant: dropDownSectionHeight).isActive = true
                            label_DropDown_Combo_View.center.x = cell.mainStack.center.x;
                            heightToUse += dropDownSectionHeight;
                            heightToUse += padding;
                            label_DropDown_Combo_View.entityNameLabel.font=UIFont(name: "Roboto-Regular",size: 15)
                            if let label = val["label"] as? JSON{
                                label_DropDown_Combo_View.entityNameLabel.text = label.stringValue;
                                if label.stringValue == "Condition"{
                                 
                                 label_DropDown_Combo_View.dropDownButton.titleLabel?.font=UIFont(name: "Roboto-Regular",size: 15)
                                 //label_DropDown_Combo_View.dropDownButton.setTitle( upsActive, for: UIControlState());
                                 label_DropDown_Combo_View.dropDownButton.addTarget(self, action: #selector(showRequiredDropdown(_:)), for: UIControl.Event.touchUpInside);
                                 label_DropDown_Combo_View.dropDownButton.tag = counter;
                                
                                 if let values = val["values"] as? JSON{
                                     var valuesArray = [String:String]();
                                     for valEach in values.arrayValue
                                     {
                                         if let value = val["value"] as? JSON{
                                             if(value.stringValue == valEach["value"].stringValue)
                                             {
                                                 label_DropDown_Combo_View.dropDownButton.setTitle(valEach["label"].stringValue, for: .normal);
                                             }
                                            
                                            
                                         }
                                         valuesArray[valEach["label"].stringValue] = valEach["value"].stringValue;
                                     }
                                     selectedSelectArray[counter] = valuesArray;
                                 }
                                     }else{
                            
                            label_DropDown_Combo_View.dropDownButton.titleLabel?.font=UIFont(name: "Roboto-Regular",size: 15)
                            //label_DropDown_Combo_View.dropDownButton.setTitle( upsActive, for: UIControlState());
                            label_DropDown_Combo_View.dropDownButton.addTarget(self, action: #selector(showRequiredDropdown(_:)), for: UIControl.Event.touchUpInside);
                            label_DropDown_Combo_View.dropDownButton.tag = counter;
                           
                            if let values = val["values"] as? JSON{
                                var valuesArray = [String:String]();
                                for valEach in values.arrayValue
                                {
                                    if let value = val["value"] as? JSON{
                                        if(value.stringValue == valEach["value"].stringValue)
                                        {
                                            label_DropDown_Combo_View.dropDownButton.setTitle(valEach["label"].stringValue, for: .normal);
                                        }
                                    }
                                    valuesArray[valEach["label"].stringValue] = valEach["value"].stringValue;
                                }
                                selectedSelectArray[counter] = valuesArray;
                            }
                                }
                            }
                            shippingMethodsFields[key]![x]["field"] = label_DropDown_Combo_View.dropDownButton;
                            //design
                            label_DropDown_Combo_View.makeCard(label_DropDown_Combo_View, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
                            
                            cell.mainStack.addArrangedSubview(label_DropDown_Combo_View);
                            
                        }else if(type.stringValue == "text")
                        {
                                if fieldToPost == "shipping_zones"
                                {
                                    var topPt = heightToUse
                                 //   var valuesArray = [String:String]();
                                    var zoneStackHeight : CGFloat = 0
                                    //initialZoneViewHeight = 110
                                    
                                    //zoneStackView.view.backgroundColor = #colorLiteral(red: 0.8549019694, green: 0.250980407, blue: 0.4784313738, alpha: 1)
                                    zoneStackView.autoresizingMask = [.flexibleLeftMargin,.flexibleRightMargin];
                                   // zoneStackView.translatesAutoresizingMaskIntoConstraints = false;
                                    
                                      zoneStackView.headingLabel.font=UIFont(name: "Roboto-Regular", size: 15)
                                      guard  let valLbl = val["label"] as? JSON else{return UITableViewCell()}
                                      zoneStackView.headingLabel.text = valLbl.stringValue
                                    guard  let valValue = val["value"] as? JSON else{return UITableViewCell()}
                                    for index in zoneStackView.verticalStackView.arrangedSubviews
                                    {
                                        index.removeFromSuperview()
                                    }
                                    if valValue.arrayValue.count > 0
                                    {
                                        for ind in valValue.arrayValue{
                                        let zone = zoneView()
                                         zoneStackView.verticalStackView.addArrangedSubview(zone)
                                         zone.translatesAutoresizingMaskIntoConstraints = false;
                                         zoneStackView.verticalStackView.addConstraint(NSLayoutConstraint(item: zone, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .notAnAttribute, multiplier: 1, constant: 100))
                                            
                                            zone.typeButton.setTitle(ind["type"].stringValue, for: .normal)
                                            zone.fromText.text = ind["from"].stringValue
                                            zone.toText.text = ind["to"].stringValue
                                            zone.priceText.text = ind["price"].stringValue
                                            
                                            var zoneInitialValues = [String:String]()
                                            zoneInitialValues = ["from": ind["from"].stringValue, "to": ind["to"].stringValue, "price": ind["price"].stringValue, "type": ind["type"].stringValue]
                                            newAddedzoneViews[zone.tag] = zoneInitialValues;
                                            
                                            viewHeight?.constant += 100
                                         initialZoneViewHeight += 100
                                         
                                        initialZoneViewHeight += padding
                                         heightToUse += 100
                                         zoneStackView.stackHeight.constant += 100
                                         zone.tag = 850 + i
                                         zone.removeBtn.tag = 850 + i
                                         zone.removeLabel.text = ""
                                         
                                         zone.typeButton.setTitle(zoneTypeArray[0], for: .normal)
                                         zone.typeButton.addTarget(self, action: #selector(showzoneTypes(_:)), for: .touchUpInside)
                                         zone.removeBtn.addTarget(self, action: #selector(removeZoneView(_:)), for: .touchUpInside)
                                        
                                        
                                        
                                        }
                                        
                                        
                                    }
                                    
//                                        print("newAddedzoneViews = \(newAddedzoneViews)")
//                                        if newAddedzoneViews.count > 0
//                                        {
//                                            var ht : CGFloat = 40
//                                            for index in newAddedzoneViews {
//                                                let zone = zoneView()
//                                                zoneStackView.verticalStackView.addArrangedSubview(zone)
//                                                zone.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
//                                                // zone.frame = CGRect(x: 0, y: ht, width: screenSize.width-20,height: 100);
//                                                //zone.heightAnchor.constraint(equalToConstant: 100).isActive = true
//                                                zoneStackView.verticalStackView.addConstraint(NSLayoutConstraint(item: zone, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .notAnAttribute, multiplier: 1, constant: 100))
//                                                ht += 105
//                                                initialZoneViewHeight += 105
//                                                initialZoneViewHeight += padding
//                                                zoneStackHeight += 100
//                                                heightToUse += initialZoneViewHeight;
//                                                heightToUse += padding;
//                                                zoneStackView.stackHeight.constant = zoneStackHeight
//                                              //  zoneStackView.verticalStackView.addConstraint(NSLayoutConstraint(item: zone, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .notAnAttribute, multiplier: 1, constant: initialZoneViewHeight))
//                                                //zoneStackView.outerViewHeight.constant = initialZoneViewHeight
//
//                                            }
//                                        }
                                        else
                                        {
                                            zoneStackView.stackHeight.constant = 0
                                            
                                        }
                                    zoneStackView.addZoneButton.tag = counter
                                    zoneStackView.addZoneButton.addTarget(self, action: #selector(addNewZoneView(_:)), for: .touchUpInside)
                                    //selectedSelectArray[counter] = valuesArray
                                    // zoneStackView.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width-20,height: initialZoneViewHeight);
                                   // zoneStackView.heightAnchor.constraint(equalToConstant: initialZoneViewHeight).isActive = true
                                    heightToUse += initialZoneViewHeight;
                                    heightToUse += padding;
                                   //zoneStackView.outerView.makeCard(zoneStackView.outerView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4);
                                  
                                    cell.mainStack.addArrangedSubview(zoneStackView);
                                }
                                else
                                {
                                    let label_TextView_Combo_View = Label_TextView_Combo_View();
                                      var valuesArray = [String:String]();
                                      label_TextView_Combo_View.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
                                     // label_TextView_Combo_View.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width-20,height: fixedHeight);
                                    label_TextView_Combo_View.heightAnchor.constraint(equalToConstant: fixedHeight).isActive = true
                                      heightToUse += fixedHeight;
                                      heightToUse += padding;
                                     // label_TextView_Combo_View.center.x = cell.mainWrapperView.center.x;
                                      label_TextView_Combo_View.entityNameLabel.font=UIFont(name: "Roboto-Regular", size: 15)
                                      guard  let valLbl = val["label"] as? JSON else{return UITableViewCell()}
                                      label_TextView_Combo_View.entityNameLabel.text = valLbl.stringValue
                                    guard  let valValue = val["value"] as? JSON else{return UITableViewCell()}
                                      label_TextView_Combo_View.entityValueTxtField.text = valValue.stringValue
                                      label_TextView_Combo_View.entityValueTxtField.tag = counter;
                                      shippingMethodsFields[key]![x]["field"] = label_TextView_Combo_View.entityValueTxtField
                                     valuesArray[valLbl.stringValue] = valValue.stringValue;
                                      
                                      
                                      print(val["label"])
                                      print("taggg of this value \(counter)")
                                      selectedSelectArray[counter] = valuesArray
                                      label_TextView_Combo_View.makeCard(label_TextView_Combo_View, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4);
                                      
                                      cell.mainStack.addArrangedSubview(label_TextView_Combo_View);
                                }
                                
                           /* --edited
                            let label_TextView_Combo_View = Label_TextView_Combo_View();
                            var valuesArray = [String:String]();
                            label_TextView_Combo_View.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
                            label_TextView_Combo_View.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width-20,height: fixedHeight);
                            heightToUse += fixedHeight;
                            heightToUse += padding;
                            label_TextView_Combo_View.center.x = cell.mainWrapperView.center.x;
                            label_TextView_Combo_View.entityNameLabel.font=UIFont(name: "Roboto-Regular", size: 15)
                            guard  let valLbl = val["label"] as? JSON else{return UITableViewCell()}
                            label_TextView_Combo_View.entityNameLabel.text = valLbl.stringValue
                          guard  let valValue = val["value"] as? JSON else{return UITableViewCell()}
                            label_TextView_Combo_View.entityValueTxtField.text = valValue.stringValue
                            label_TextView_Combo_View.entityValueTxtField.tag = counter;
                            shippingMethodsFields[key]![x]["field"] = label_TextView_Combo_View.entityValueTxtField
                           valuesArray[valLbl.stringValue] = valValue.stringValue;
                            
                            
                            print(val["label"])
                            print("taggg of this value \(counter)")
                            selectedSelectArray[counter] = valuesArray
                            label_TextView_Combo_View.makeCard(label_TextView_Combo_View, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4);
                            
                            cell.mainWrapperView.addSubview(label_TextView_Combo_View); */
                        }
                        else if(type.stringValue == "multiselect")
                        {
                            let topPoint = heightToUse;
                            var viewHeight = CGFloat(0);
                           guard let valueString = val["value"]  as? JSON else{return UITableViewCell()}
                            let valueArray = valueString.stringValue.characters.split{$0 == ","}.map(String.init);
            
                            self.activeShippingMethodsValueArray = valueArray;
                            
                            let view = UIView();
                            view.backgroundColor = DynamicColor.ViewBackgroundColor;
                           // print(self.valuesArray[counter]);
                            
                            let labelView = LabelView();
                            labelView.autoresizingMask = [.flexibleLeftMargin,.flexibleRightMargin];
                            labelView.frame = CGRect(x: 0, y: viewHeight, width: screenSize.width-30,height: checkboxHeight);
                            //labelView.heightAnchor.constraint(equalToConstant: checkboxHeight).isActive = true
                            labelView.center.x = self.view.center.x;
                            heightToUse += checkboxHeight;
                            heightToUse += padding;
                            
                            viewHeight += (checkboxHeight+padding);
                            labelView.label.font=UIFont(name: "Roboto-Regular", size: 15)
                            guard  let valLbl = val["label"] as? JSON else{return UITableViewCell()}
                                                   
                            labelView.label.text = " "+(valLbl.stringValue.uppercased());
                            
                            //design
                            labelView.label.textColor = UIColor.white;
                            labelView.label.backgroundColor = color;
                            labelView.label.textColor = UIColor.white;
                            
                            
                            view.addSubview(labelView);
                            if let values = val["values"] as? JSON{
                            
                            for val in values.arrayValue
                            {
                                self.shippingMethodsArray[val["label"].stringValue] = val["value"].stringValue;
                                
                                let subCategoryView = SubCategoryView();
                                subCategoryView.autoresizingMask = [.flexibleLeftMargin,.flexibleRightMargin];
                                subCategoryView.frame = CGRect(x: 0, y: viewHeight, width: screenSize.width-30,height: checkboxHeight);
                                //subCategoryView.heightAnchor.constraint(equalToConstant: checkboxHeight).isActive = true
                                subCategoryView.center.x = self.view.center.x;
                                heightToUse += checkboxHeight;
                                heightToUse += padding;
                                
                                viewHeight += (checkboxHeight+padding);
                                
                                subCategoryView.childFilterButton.titleLabel?.font=UIFont(name: "Roboto-Regular", size: 15)
                                subCategoryView.childFilterButton.setTitle(val["label"].stringValue, for: .normal);
                                
                                if valueArray.contains(val["value"].stringValue) {
                                    subCategoryView.childFilterButton.setImage(UIImage(named: "checked_checkbox"), for: .normal);
                                }
                                else
                                {
                                    subCategoryView.childFilterButton.setImage(UIImage(named: "unchecked_checkbox"), for: .normal);
                                }
                                
                                
                                subCategoryView.childFilterButton.addTarget(self, action: #selector(self.checkboxButtonClicked(_:)), for: .touchUpInside);
                                
                                view.addSubview(subCategoryView);
                                
                            }
                            
                            //view.frame = CGRect(x: 15, y: topPoint+10, width: screenSize.width-30, height: viewHeight);
                            view.heightAnchor.constraint(equalToConstant: viewHeight).isActive = true
                            view.makeCard(view, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
                            
                            //view.layer.borderWidth = 1;
                            //view.layer.borderColor = UIColor.grayColor().CGColor;
                            cell.mainStack.addArrangedSubview(view);
                        }
                        }
                        counter += 1;
                    }
                    x += 1;
                }
            }
            
            return cell;
        }
        
        @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat
        {
//            print("heightToUse = \(heightToUse)")
//           return heightToUse+20;
           return UITableView.automaticDimension
        }
        
        @objc func checkboxButtonClicked(_ sender:UIButton)
        {
            let key = self.shippingMethodsArray[(sender.titleLabel?.text)!]!;
            
            if(sender.imageView!.image == UIImage(named:"unchecked_checkbox"))
            {
                sender.setImage(UIImage(named: "checked_checkbox"), for: UIControl.State());
                self.activeShippingMethodsValueArray.append(key);
            }
            else
            {
                sender.setImage(UIImage(named: "unchecked_checkbox"), for: UIControl.State());
                if let indexToRemove = activeShippingMethodsValueArray.index(of: key) {
                    self.activeShippingMethodsValueArray.remove(at: indexToRemove);
                }
            }
            
            print(self.activeShippingMethodsValueArray);
            
        }
        @objc func addNewZoneView(_ sender:UIButton){
            print("AddNewZones:-\(sender.superview?.superview)")
            
            
            
            //if let superView = sender.superview?.superview as? labelStackcombo{
                if zoneStackView.verticalStackView.arrangedSubviews.count > 0
                {
                    i = zoneStackView.verticalStackView.arrangedSubviews.count
                }
                
                let zone = zoneView()
                zoneStackView.verticalStackView.addArrangedSubview(zone)
                zone.translatesAutoresizingMaskIntoConstraints = false;
                zoneStackView.verticalStackView.addConstraint(NSLayoutConstraint(item: zone, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .notAnAttribute, multiplier: 1, constant: 100))
                viewHeight?.constant += 100
                initialZoneViewHeight += 100
                
               initialZoneViewHeight += padding
                heightToUse += 100
                zoneStackView.stackHeight.constant += 100
                zone.tag = 850 + i
                zone.removeBtn.tag = 850 + i
                zone.removeLabel.text = ""
                zone.fromText.text = ""
                zone.toText.text = ""
                zone.priceText.text = ""
                zone.typeButton.setTitle(zoneTypeArray[0], for: .normal)
                zone.typeButton.addTarget(self, action: #selector(showzoneTypes(_:)), for: .touchUpInside)
                zone.removeBtn.addTarget(self, action: #selector(removeZoneView(_:)), for: .touchUpInside)
                var zoneInitialValues = [String:String]()
                zoneInitialValues = ["from": "", "to": "", "price": "", "type": zoneTypeArray[0]]
                newAddedzoneViews[zone.tag] = zoneInitialValues;
                shippingTableView.beginUpdates()
                shippingTableView.layoutIfNeeded()
                shippingTableView.endUpdates()
            
//                let index = IndexPath(row: 0, section: 0)
//             shippingTableView.reloadRows(at: [index], with: .automatic)
           // }
            //reloadTable()
            
        }
      /* @objc func addNewZoneView(_ sender:UIButton)
        {
            print("addNewZoneView")
            print(sender.superview?.superview)
            if let superView = sender.superview?.superview as? labelStackcombo{
                
                if superView.verticalStackView.arrangedSubviews.count > 0
                {
                    i = superView.verticalStackView.arrangedSubviews.count
                }
                
                let zone = zoneView()
                superView.verticalStackView.addArrangedSubview(zone)
                zone.translatesAutoresizingMaskIntoConstraints = false;
                superView.verticalStackView.addConstraint(NSLayoutConstraint(item: zone, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .notAnAttribute, multiplier: 1, constant: 100))
                initialZoneViewHeight += 100
               initialZoneViewHeight += padding
                zone.tag = 850 + i
                zone.removeBtn.tag = 850 + i
                zone.removeLabel.text = ""
                zone.fromText.text = ""
                zone.toText.text = ""
                zone.priceText.text = ""
                zone.typeButton.setTitle(zoneTypeArray[0], for: .normal)
                zone.typeButton.addTarget(self, action: #selector(showzoneTypes(_:)), for: .touchUpInside)
                zone.removeBtn.addTarget(self, action: #selector(removeZoneView(_:)), for: .touchUpInside)
                var zoneInitialValues = [String:String]()
                zoneInitialValues = ["from": "", "to": "", "price": "", "type": zoneTypeArray[0]]
                newAddedzoneViews[zone.tag] = zoneInitialValues;
                self.shippingMethodTableView.reloadData()
            }
        }*/
        
        @objc func removeZoneView(_ sender: UIButton)
        {
            print("removeZoneView")
           // print(sender.superview?.superview?.superview?.superview)
            if let superview = self.view.viewWithTag(sender.tag) as? zoneView//sender.superview?.superview?.superview?.superview as? zoneView
            {
                print("Get zone\(superview)")
                superview.removeFromSuperview()
                 zoneStackView.stackHeight.constant -= 100
                viewHeight?.constant -= 100
            }
        }
        
        @objc func showzoneTypes(_ sender: UIButton)
        {
            let dropDown = DropDown();
            let tag = sender.tag;
            print(tag)
            let dropdownArray = zoneTypeArray;
            let dataSource = zoneTypeArray;
            
            dropDown.dataSource = dataSource;
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
        
        @objc func showRequiredDropdown(_ sender:UIButton)
        {
            let dropDown = DropDown();
            let tag = sender.tag;
            let dropdownArray = selectedSelectArray[tag];
            let dataSource = Array(dropdownArray!.keys);
            
            dropDown.dataSource = dataSource;
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
        
        @objc func fetchShippingMethodsInfo()
        {
            Alert_File.addLoadingIndicator(self, msg: NSLocalizedString("Please Wait...", comment: ""))
            
            let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
            //print(userData)
            let vendor_id = userData["vendorId"] as! String
            let hashkey    = userData["hashKey"] as! String
            
            //baseURL = "vupsshippingapi/index/shippingMethods/";
         //---   baseURL = "vstorepickupapi/setting/shippingMethods/vendor_id/\(vendor_id)/hashkey/\(hashkey)"
            baseURL = "vmultishippingApi/setting/shippingMethods/vendor_id/\(vendor_id)/hashkey/\(hashkey)"
            print(baseURL);
            
            let postString = ""//"vendor_id="+vendor_id+"&hashkey="+hashkey;
            self.getRequest(url: baseURL)
            
            
        }
        override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
            
            let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
            let vendor_id = userData["vendorId"] as! String
            let hashkey    = userData["hashKey"] as! String
            
            //baseURL = "vupsshippingapi/index/shippingMethods/";
           //--- baseURL = "vstorepickupapi/setting/shippingMethods/vendor_id/\(vendor_id)/hashkey/\(hashkey)"
             baseURL = "vmultishippingApi/setting/shippingMethods/vendor_id/\(vendor_id)/hashkey/\(hashkey)"
            let baseURL2 = "vmultishippingApi/setting/saveShippingMethod"
            if(requestUrl==baseURL)
            {
               print(NSString(data:data!,encoding:String.Encoding.utf8.rawValue))
                do{
                    
                    let jsonData = try JSON(data: data!)
                    print(jsonData)
                    Alert_File.removeLoadingIndicator(self);
                    if jsonData["data"]["success"].stringValue == "false"{
                       // self.view.makeToast(jsonData["data"]["message"].stringValue, duration: 2.0, position: .center)
                        self.rightButton.isHidden = true
                        self.shippingTableView.setEmptyMessage(jsonData["data"]["message"].stringValue)
                        return
                    }
                    
                    for(mainkey,mainval) in jsonData["data"]
                    {
                        var methodArray = [[String:Any]]()
                        for index in mainval.arrayValue{
                            var checkArray = [String:Any]()
                            for(key,val) in index
                            {
                                checkArray[key] = val;
                            }
                            methodArray.append(checkArray);
                            
                        }
                        
                        /*for (_,UPS_Fileds) in mainval
                         {
                         var tempArray = [String:String]();
                         for (key,val) in UPS_Fileds
                         {
                         print(key);
                         print(val);
                         
                         
                         if(key == "values")
                         {
                         self.valuesArray.append(val);
                         }
                         else
                         {
                         tempArray[key] = val.stringValue;
                         }
                         }
                         methodArray.append(tempArray);
                         }*/
                        shippingMethodsFields[mainkey] = methodArray;
                    }
                    
                    
                    self.shippingTableView.restore()
                    print(self.shippingMethodsFields);
                    
                    self.shippingTableView.reloadData();
                }catch let err{
                    print(err.localizedDescription)
                }
            }
                
            else if(requestUrl == baseURL2)
            {
                do{
                 let jsonData = try JSON(data: data!)
                print(jsonData)
                if(jsonData["data"]["success"].stringValue != "true")
                {
                    let title = NSLocalizedString("Error", comment: "");
                    let message = NSLocalizedString("Unfortunately Data Not Saved", comment: "");
                    Alert_File.showValidationError(self,title:title,message:message);
                    return;
                }
                
                let title = NSLocalizedString("Success", comment: "");
                let message = jsonData["data"]["message"].stringValue
                Alert_File.showValidationError(self,title:title,message:message);
                return;
                
                }catch let err{
                    print(err.localizedDescription)
                }
            }
        }
        
        @objc func saveShippingMethods(_ sender:UIButton)
        {
            var shipping_county_data = ""
            var shipping_zones_data = ""
            var shipping_method_data = "[";
//           var mainPostData = [String:Any]()
//             var postData = [String:Any]()
//            var zoneArr = [[String:String]]()
//            var zoneKey = ""
             var keysVal = ""
            for(key,val) in shippingMethodsFields{
               
                if let keyArray = key.components(separatedBy: "#") as? [String]
                {
                   keysVal = keyArray[0]
                     shipping_method_data += "{\"\(keysVal)\":{"
                }
              
                for index in val{
                    if let type = index["type"] as? JSON{
                        if(type.stringValue == "select")
                        {
                            if let field = index["field"] as? UIButton{
                                if(field.currentTitle != "")
                                {
                                    print(field.tag);
                                    print(field.currentTitle);
                                    if let postField = index["field_to_post"] as? JSON{
                                        let datatosend = selectedSelectArray[field.tag]![field.currentTitle!]!;
                                        shipping_method_data += "\"\(postField.stringValue)\":\"\(datatosend)\","
                                        //postData[postField.stringValue] = datatosend
                                    }
                                }
                            }
                        }
                        if(type.stringValue == "text"){
                            
                            if let field = index["field"] as? UITextView{
                                print("TextView text:\(field.text)")
                                print(field.tag)
                                if let postField = index["field_to_post"] as? JSON{
                                    let datatosend = field.text ?? "" //selectedSelectArray[field.tag]![field.text ?? ""]
                                    shipping_method_data += "\"\(postField.stringValue)\":\"\(datatosend)\","
                                    //postData[postField.stringValue] = datatosend
                                }
                            }
                            if let zoneFields = index["field_to_post"] as? JSON{
                                print("Zone Fields:\(zoneFields.stringValue)")
                                
                                if zoneFields.stringValue == "shipping_zones"{
                                       shipping_zones_data += "\"\(zoneFields.stringValue)\":["
                                    
                                    var mainZoneString = ""
                                    
                                    
                                    for vw in zoneStackView.verticalStackView.subviews{
                                        var insideArray = [String:String]()
                                        if let view = vw as? zoneView{
                                            /*insideArray["from"] = view.fromText.text ?? ""
                                            insideArray["to"] = view.toText.text ?? ""
                                            insideArray["price"] = view.priceText.text ?? ""
                                            insideArray["type"] = view.typeButton.currentTitle ?? ""
                                            
                                            print(view.fromText.text)
                                             print(view.toText.text)
                                             print(view.priceText.text)
                                             print(view.typeButton.currentTitle)*/
                                             
                                             var insideData = ""
                                             insideData += "{"
                                             insideData += "\"from\":"+"\"\(view.fromText.text ?? "")\","
                                             insideData += "\"to\":"+"\"\(view.toText.text ?? "")\","
                                             insideData += "\"price\":"+"\"\(view.priceText.text ?? "")\","
                                             insideData += "\"type\":"+"\"\(view.typeButton.currentTitle ?? "")\","
                                             
                                             insideData = insideData.substring(to: insideData.characters.index(before: insideData.endIndex));
                                             insideData += "},";
                                             shipping_zones_data += insideData
                                            }
                                            //zoneArr.append(insideArray)
                                        
                                    }
                                    shipping_zones_data = shipping_zones_data.substring(to: shipping_zones_data.characters.index(before: shipping_zones_data.endIndex));
                                    shipping_zones_data += "]";
                                  //  mainZoneString += convertParamsToJSON(params: zoneArr).debugDescription
                                   // zoneData[] = zoneArr
                                   //zoneKey = zoneFields.stringValue
                                }
//                                shipping_method_data = shipping_method_data.substring(to: shipping_method_data.characters.index(before: shipping_method_data.endIndex));
//                                shipping_method_data += "]";
                                
                            }
                        }
                        if(type.stringValue == "multiselect"){
                            if let postField = index["field_to_post"] as? JSON{
                                 
                                let datatosend = activeShippingMethodsValueArray
                                shipping_county_data += "\"\(postField.stringValue)\":\(datatosend),"
                                 //postData[postField.stringValue]  =  datatosend
                            }
                        }
                    }
                  
                }
                if(shipping_method_data != "[{\"\(keysVal)\":{")
                {
                    shipping_method_data += shipping_county_data
                    shipping_method_data = shipping_method_data.substring(to: shipping_method_data.characters.index(before: shipping_method_data.endIndex));
                   // shipping_method_data +=  ","+shipping_zones_data//+"}";
                    shipping_method_data += "]"
                }
                shipping_method_data.removeLast()
                shipping_method_data += "}}"
                shipping_method_data += ","
            }
            
            shipping_method_data = shipping_method_data.substring(to: shipping_method_data.characters.index(before: shipping_method_data.endIndex));
            shipping_method_data += "]";
            /*for val in activeShippingMethodsValueArray
            {
                shipping_method_data += "\""+val+"\",";
            }
            //shipping_method_data += "\""+"groups[delhivery][active]"+"\",";
            shipping_method_data = shipping_method_data.substring(to: shipping_method_data.characters.index(before: shipping_method_data.endIndex));
            if(activeShippingMethodsValueArray.count > 0)
            {
                shipping_method_data += "]},";
            }
            else
            {
                shipping_method_data = "";
            }
            
            if let dataIn = dropDownInfoArray[upsActive] {
                shipping_method_data += "{\"active\":";
                shipping_method_data += dataIn
                shipping_method_data += "}]";
            }
           */
            
            
          // print(shipping_method_data);
            
            //shipping_method_data = shipping_method_data.addingPercentEncoding(withAllowedCharacters: .urlHostAllowed)!;
            
           
            let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary;
            let vendor_id = userData["vendorId"] as! String;
            let hashkey    = userData["hashKey"] as! String;
            //baseURL = "vupsshippingapi/index/saveShippingMethods/";
        //    baseURL = "vstorepickupapi/setting/saveShippingMethod/hashkey/\(hashkey)"
            baseURL = "vmultishippingApi/setting/saveShippingMethod"
            print(baseURL);
//            var shipData = [String:Any]()
//            shipData[keysVal] = postData
//            shipData[zoneKey] = zoneArr
//            print(postData)
//            mainPostData["vendor_id"] = Int(vendor_id)
//            mainPostData["hashkey"] = hashkey
//            mainPostData["shipping_method_data"] = [shipData]
//           print(mainPostData)
            
            var postString = "&hashkey="+hashkey+"&vendor_id="+vendor_id;
            postString += "&shipping_method_data="+shipping_method_data;
            print(postString);
            
            Alert_File.addLoadingIndicator(self, msg: NSLocalizedString("Please Wait...", comment: ""))
           sendRequest(url: baseURL, parameters: postString);
          //  sendAnyRequest(url: baseURL, params: mainPostData, withRest: false)
        }
        
    }
 
