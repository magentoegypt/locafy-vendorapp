//
//  ced_BrandViewController.swift
//  VenderApp
//
//  Created by cedcoss on 24/02/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit
class ced_BrandViewController: ced_VendorBaseClass, UITableViewDelegate, UITableViewDataSource, UIGestureRecognizerDelegate, UITextFieldDelegate {
    @IBOutlet weak var filterButton: UIButton!
    @IBOutlet weak var addBrand: UIButton!
    @IBOutlet weak var tableView: UITableView!
    var loading = true
    var filter = String()
    var baseURL = String()
    var brandData = [[String:String]]()
    var page = 1;
     var del_button_tag:Int = 0
    var datePickerView = UIDatePicker()
    var textFieldForDate=UITextField()
    var id = "";
    var name = "";
    //var type = "";
    var creation = "";
    let screenSize: CGRect = UIScreen.main.bounds;
//    var qty = "";
//    var status = "";
    let storeFilterView = storeFilter();
    override func viewDidLoad() {
        super.viewDidLoad()
        addBrand.layer.cornerRadius=2
        addBrand.tintColor = UIColor.white
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as! String
        addBrand.backgroundColor =  Ced_CommonVendor.UIColorFromRGB(colorString)
        addBrand.addTarget(self, action: #selector(SendData(_:)), for: .touchUpInside)
        filterButton.addTarget(self, action: #selector(filterButtonPressed(_:)), for: .touchUpInside)
//        filterButton.layer.borderColor = Ced_CommonVendor.UIColorFromRGB(colorString).cgColor
//        filterButton.layer.borderWidth = 5
        loadData()
    }
    
    @objc func initializeData()
    {
        brandData = [[String:String]]()
        page = 1;
        filter = "";
        loadData()
    }
    
     @objc func SendData(_ sender:UIButton)
     {
        let viewcontrol = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "BrandAdd") as? ced_AddBrandViewController
        viewcontrol?.vc = self;
        self.navigationController?.pushViewController(viewcontrol!, animated: true)
        
     }
    @objc func loadData()
    {
       
        baseURL = "/vendorapi/brand/item"
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        var postString = "vendor_id=" + vendorId + "&hashkey="+hashKey+"&page=\(page)"
        if(filter != "")
        {
            postString += "&filter="+filter;
        }
        print(baseURL)
        print(postString)
        self.sendRequest(url: baseURL, parameters: postString)
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
    }
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(true)
        loadData()
    }

    @objc func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return brandData.count
    }

    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell=tableView.dequeueReusableCell(withIdentifier: "BrandCell") as! ced_BrandListTableViewCell
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as! String
        cell.BrandName.text = brandData[indexPath.row]["name"]
        cell.brandId.text = brandData[indexPath.row]["id"]
        cell.creation.text = brandData[indexPath.row]["creation_time"]
        let imgRequest =  URL(string: brandData[indexPath.row]["image"]!.addingPercentEncoding(withAllowedCharacters: CharacterSet.urlQueryAllowed) ?? "")
        cell.ImageLogo.sd_setImage(with: imgRequest, placeholderImage: UIImage(named: "placeholder"))
        let imgRequest2 =  URL(string: brandData[indexPath.row]["thumbnail"]!.addingPercentEncoding(withAllowedCharacters: CharacterSet.urlQueryAllowed) ?? "")
        cell.ImageThumbnail.sd_setImage(with: imgRequest2, placeholderImage: UIImage(named: "placeholder"))
        cell.ContainerView.makeCard(cell.ContainerView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4)
        cell.twoButtons.cancelButton.backgroundColor =  Ced_CommonVendor.UIColorFromRGB(colorString)
        cell.twoButtons.cancelButton.setTitle("Edit", for: UIControl.State())
        cell.twoButtons.cancelButton.layer.cornerRadius = 2
        cell.twoButtons.doneButton.layer.cornerRadius = 2
        cell.twoButtons.cancelButton.tintColor = UIColor.white
        cell.twoButtons.cancelButton.tag = indexPath.row
        cell.twoButtons.cancelButton.addTarget(self, action: #selector(EditButtonClicked(_:)), for: .touchUpInside)
        cell.twoButtons.doneButton.addTarget(self, action: #selector(deleteProductRequestConfirmation(_:)), for: .touchUpInside)
        cell.twoButtons.doneButton.backgroundColor = Ced_CommonVendor.UIColorFromRGB("#ff0000");
        cell.twoButtons.doneButton.tintColor = UIColor.white
        cell.twoButtons.doneButton.tag = indexPath.row
        cell.twoButtons.doneButton.setTitle("Delete", for: UIControl.State())
        cell.selectionStyle = UITableViewCell.SelectionStyle.none
        return cell
    }
    
    @objc func EditButtonClicked(_ sender:UIButton)
    {
        let viewcontrol = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "BrandAdd") as? ced_AddBrandViewController
        viewcontrol?.backData = true
        viewcontrol?.id = brandData[sender.tag]["id"]!
        self.navigationController?.pushViewController(viewcontrol!, animated: true)
    }
    
    @objc func deleteProductRequestConfirmation(_ sender:UIButton)
    {
        let showTitle = "Confirmation".localized;
        let showMsg = "Are You Sure You Want To Delete This Brand?";
        if #available(iOS 8.0, *) {
            let confirmationAlert = UIAlertController(title: showTitle, message: showMsg, preferredStyle: UIAlertController.Style.alert)
            confirmationAlert.addAction(UIAlertAction(title: "Yes".localized, style: .default, handler: { (action: UIAlertAction!) in
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
        
        let id = brandData[sender.tag]["id"]!
        baseURL = "vendorapi/brand/delete"
        let postString1 = "brand_id="+id+"&vendor_id="+vendorId+"&hashkey="+hashKey
        del_button_tag=sender.tag
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        self.sendRequest(url: baseURL, parameters: postString1)
    }
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        
        if(requestUrl=="vendorapi/brand/delete")
        {
            if let data = data{
                guard let json = try? JSON(data: data) else {return}
                print(json)
                if(json["data"]["status"].stringValue.lowercased() == "false"){
                    self.view.showToastMsg(json["data"]["message"].stringValue)
                }else if(json["data"]["status"].stringValue.lowercased() == "true"){
                    self.view.showToastMsg(json["data"]["message"].stringValue)
                    let indexPath = IndexPath(item: del_button_tag, section: 0)
                    self.brandData.remove(at: del_button_tag)
                    Alert_File.removeLoadingIndicator(self);
                    self.tableView.deleteRows(at: [indexPath], with: UITableView.RowAnimation.right)
                    self.tableView.reloadData()
                }
            }
        }
        if(requestUrl=="/vendorapi/brand/item")
        {
            if let data = data{
                guard let json = try? JSON(data: data) else {return}
                print(json)
                if(json["data"]["success"].stringValue.lowercased() == "false"){
                    Alert_File.removeLoadingIndicator(self);
                    if(page == 1)
                    {
                       // self.view.makeToast(json["data"]["message"].stringValue, duration: 2.0, position: .center);
                        self.tableView.setEmptyMessage(json["data"]["message"].stringValue)
                        return
                    }
                    self.loading = false
                    self.page = 1
                    Ced_CommonVendor.delay(2.0, closure: {
                        self.tableView.reloadData()
                        
                    })
                }else if(json["data"]["success"].stringValue.lowercased() == "true"){
                    //   self.view.showToastMsg(json["data"]["message"].stringValue)
                    Alert_File.removeLoadingIndicator(self);
                    print(json)
                    self.parseProductdata(json)
                    self.tableView.restore()
                    self.tableView.reloadData()
                }
            }
            
            
        }
        
    }
    @objc func filterButtonPressed(_ sender:UIButton)
    {
        print("filterButtonPressed");
        /* background view */
        
//        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
////        _ = Ced_CommonVendor.UIColorFromRGB(colorString!)
        let bgCView = UIView();
        bgCView.tag=151;
        bgCView.frame = UIScreen.main.bounds;
        bgCView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        bgCView.backgroundColor = UIColor(red:0, green:0, blue:0, alpha:0.5);
        storeFilterView.frame = CGRect(x: 0, y: 0, width: self.view.frame.width - 20, height: 210);
        storeFilterView.center.x = self.view.center.x;
        storeFilterView.center.y = self.view.center.y+20;
        self.addgesturesTohideView(self.view);
        storeFilterView.filterButton.setTitle("Filter", for: UIControl.State());
        storeFilterView.filterButton.addTarget(self, action: #selector(applyFilterTapped(_:)), for: UIControl.Event.touchUpInside);
        storeFilterView.tag = 181;
        storeFilterView.resetButton.setTitle("Reset", for: UIControl.State());
        storeFilterView.resetButton.addTarget(self, action: #selector(resetFilterTapped(_:)), for: UIControl.Event.touchUpInside);
        storeFilterView.statusHeight.constant = 0
        storeFilterView.emailHeight.constant = 0
          storeFilterView.productPrice.delegate = self
        //storeFilterView.productStatus.addTarget(self, action: #selector(statusClicked(_:)), for: .touchUpInside)
        storeFilterView.Name.text = "Brand Name"
        storeFilterView.creation.text = "Creation Date"
        storeFilterView.id.text = "Brand Id"
        storeFilterView.productId.addTarget(self, action:#selector(isValidNmber(_:)), for: .editingDidEnd);
         storeFilterView.productName.addTarget(self, action:#selector(isValidNme(_:)), for: .editingDidEnd);
        storeFilterView.productId.text = id
        storeFilterView.productName.text = name
        storeFilterView.productPrice.text = creation
//        storeFilterView.productName.delegate = self
//        storeFilterView.productId.delegate = self
        bgCView.addSubview(storeFilterView)
        self.view.addSubview(bgCView);
    }
    @objc func addgesturesTohideView(_ v:UIView)
    {
        //code to add gestures to dismiss the popover
        let tapGesture = UITapGestureRecognizer (target: self, action:#selector(ManageProductViewController.hideView(_:)))
        v.addGestureRecognizer(tapGesture);
        tapGesture.delegate=self;
        let upSwipe = UISwipeGestureRecognizer(target: self, action: #selector(ManageProductViewController.hideView(_:)));
        upSwipe.direction = UISwipeGestureRecognizer.Direction.up;
        v.addGestureRecognizer(upSwipe)
        upSwipe.delegate=self;
        let downSwipe = UISwipeGestureRecognizer(target: self, action: #selector(ManageProductViewController.hideView(_:)));
        downSwipe.direction = UISwipeGestureRecognizer.Direction.down;
        v.addGestureRecognizer(downSwipe)
        downSwipe.delegate=self;
        let rightSwipe = UISwipeGestureRecognizer(target: self, action: #selector(ManageProductViewController.hideView(_:)));
        rightSwipe.direction = UISwipeGestureRecognizer.Direction.right;
        v.addGestureRecognizer(rightSwipe)
        rightSwipe.delegate=self;
        let leftSwipe = UISwipeGestureRecognizer(target: self, action: #selector(ManageProductViewController.hideView(_:)));
        leftSwipe.direction = UISwipeGestureRecognizer.Direction.left;
        v.addGestureRecognizer(leftSwipe)
        leftSwipe.delegate=self;
    }
    @objc func gestureRecognizer(_ gestureRecognizer: UIGestureRecognizer, shouldReceive touch: UITouch) -> Bool
    {
        if(touch.view!.isDescendant(of: storeFilterView))
        {
            return false;
        }
        if let innerView = self.view.viewWithTag(181) as? storeFilter {
            if(touch.view!.isDescendant(of: innerView))
            {
                return false;
            }
            return true;
        }
        return true;
    }
    
    //@objc function to dismiss the custom popover
    @objc func hideView(_ recognizer: UITapGestureRecognizer)
    {
        self.view.viewWithTag(121)?.removeFromSuperview()
        self.view.viewWithTag(151)?.removeFromSuperview();
    }
    
    @objc func applyFilterTapped(_ sender:UIButton)
    {
        if let view = self.view.viewWithTag(181) as? storeFilter
        {
            var statusCode = "";
            if(view.productId.text != nil){
                id = view.productId.text!; //storeid
            }
            if(view.productName.text != nil){
                name = view.productName.text!; //store name
            }
            if(view.productPrice.text != nil){
                creation = view.productPrice.text!; //managernamee
            }
            filter = "";
            filter += "{";
            filter += "\""+"brand_id"+"\":\""+id+"\",";
            filter += "\""+"name"+"\":\""+name+"\",";
            filter += "\""+"creation_time"+"\":\""+creation+"\"}";
            print(filter);
            self.view.viewWithTag(151)?.removeFromSuperview();
            self.page = 1;
            brandData = [[String:String]]();
            self.loadData()
            //bgCView = UIView();
        }
        
    }
    
    @objc func resetFilterTapped(_ sender:UIButton)
    {
        print("resetFilterTapped");
        id = "";
        name = "";
        creation = "";
        filter = String();
        self.page = 1;
        self.view.viewWithTag(151)?.removeFromSuperview();
        brandData = [[String:String]]();
        self.loadData()
    }
    
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */
    func parseProductdata(_ json:JSON){
        self.brandData = []
        print(json)
        var creation_time = ""
        var id = ""
        var name = ""
        var image = ""
        var thumbnail = ""
        for result in json["data"]["brand_list"].arrayValue{
            if (result["id"].string != nil)
            {
                id = (result["id"].string)!
            }
            if (result["creation_time"].string != nil)
            {
                creation_time = (result["creation_time"].string)!
            }
            if (result["name"].string != nil)
            {
                name = (result["name"].string)!
            }
            if (result["image"].string != nil)
            {
                image = (result["image"].string)!
            }
            if (result["thumbnail"].string != nil)
            {
                thumbnail = (result["thumbnail"].string)!
            }
            
            let temp = ["id":id,"creation_time":creation_time,"name":name,"image":image,"thumbnail":thumbnail]
            Alert_File.removeLoadingIndicator(self);
            self.brandData.append(temp )
            
            // self.tableView.reloadData()
        }
        tableView.delegate = self
        tableView.dataSource = self
        tableView.reloadData()
        print(brandData)
    }
    
 
    @objc func textFieldDidBeginEditing(_ textField: UITextField) {
        
    
   // @objc func textViewDidBeginEditing(_ textView: UITextView) {
        
        //textField.resignFirstResponder();
          textField.resignFirstResponder();
//        if textField.tag == 70
//        {
//             if !self.isValidNumber(Number: textField.text!)
//            {
//                self.view.showToastMsg("Please enter correct Id.")
//                return
//            }
//        }
//            else if textField.tag == 60
//        {
//            if !self.isValidName(testStr: textField.text!)
//            {
//                self.view.showToastMsg("Please enter Correct Name.")
//                return
//            }
//        }
//        else{
        self.textFieldForDate=textField
        
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
        tempInrView.frame = CGRect(x: 0, y: CGFloat(0), width: screenSize.width-50,height: CGFloat(270));
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
        
        datePickerView.locale = Locale(identifier: ced_storeVC.selectLangauge)
        datePickerView.datePickerMode = UIDatePicker.Mode.date;
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
        
        
        twoButtonView.doneButton.addTarget(self, action: #selector(pickDateFromDatePickerView(_:)), for: UIControl.Event.touchUpInside);
        twoButtonView.doneButton.backgroundColor = colorGreen;
        
        tempInrView.makeCardUsingThemeColor(tempInrView, cornerRadius: 2, color: UIColor.black, shadowOpacity: 0.4);
        
        bgCView.addSubview(tempInrView);
        self.view.addSubview(bgCView);
  
    }
    
    @objc func cancelDatePickerView(_ sender:UIButton)
    {
        
        textFieldForDate.resignFirstResponder();
        
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
extension UIViewController
{
    @objc public func isValidEmil(_ textField: UITextField)
    {
        let emailRegEx = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}";
        let emailTest = NSPredicate(format:"SELF MATCHES %@", emailRegEx);
        if !emailTest.evaluate(with: textField.text)
        {
            textField.text = ""
            ShowSimpleAlert.showSimpleAlert(self, showTitle: "Information", showMsg: "Enter Proper Email")
        }
    }
    
    @objc public func isValidNme(_ textField: UITextField)
    {
        let RegEx = "[a-zA-Z ]*$";
        let Test = NSPredicate(format:"SELF MATCHES %@", RegEx);
        if !Test.evaluate(with: textField.text)
        {
            textField.text = ""
            ShowSimpleAlert.showSimpleAlert(self, showTitle: "Information", showMsg: "Please Enter Albhabets")
        }
    }
    @objc public func isValidNmber(_ textField: UITextField)  {
        let number = "^([0-9]+)?(\\.([0-9]{1,2})?)?$";
        let Test = NSPredicate(format:"SELF MATCHES %@", number);
        if !Test.evaluate(with: textField.text)
        {
            textField.text = ""
            ShowSimpleAlert.showSimpleAlert(self, showTitle: "Information", showMsg: "Please Enter Number")
        }
    }
    @objc public func isEmilValid(_ textView: UITextView)
    {
        let emailRegEx = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}";
        let emailTest = NSPredicate(format:"SELF MATCHES %@", emailRegEx);
        if !emailTest.evaluate(with: textView.text)
        {
            textView.text = ""
            ShowSimpleAlert.showSimpleAlert(self, showTitle: "Information", showMsg: "Enter Proper Email")
        }
    }
    
    @objc public func isNmeValid(_ textView: UITextView)
    {
        let RegEx = "[a-zA-Z ]*$";
        let Test = NSPredicate(format:"SELF MATCHES %@", RegEx);
        if !Test.evaluate(with: textView.text)
        {
            textView.text = ""
            ShowSimpleAlert.showSimpleAlert(self, showTitle: "Information", showMsg: "Use Alphabets")
        }
        
    }
    @objc public func isNmberValid(_ textView: UITextView){
        let number = "^([0-9]+)?(\\.([0-9]{1,2})?)?$";
        let Test = NSPredicate(format:"SELF MATCHES %@", number);
        if !Test.evaluate(with: textView.text)
        {
            textView.text = ""
            ShowSimpleAlert.showSimpleAlert(self, showTitle: "Information", showMsg: "UseNumbers")
        }
    }
    
    open override func awakeFromNib() {
           navigationItem.backBarButtonItem = UIBarButtonItem(title: "", style: .plain, target: nil, action: nil)
       }
    
    open override func awakeAfter(using coder: NSCoder) -> Any? {
        if #available(iOS 14.0, *) {
            navigationItem.backButtonDisplayMode = .minimal
        } else {
            // Fallback on earlier versions
        } // This will help us to remove text
            return super.awakeAfter(using: coder)
        }
}
