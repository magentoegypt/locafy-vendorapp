//
//  addAuctionViewController.swift
//  VenderApp
//
//  Created by MacMini on 22/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class addAuctionViewController: ced_VendorBaseClass,UITextFieldDelegate {

    
    @IBOutlet weak var topView: UIView!
    
    
    
    
    @IBOutlet weak var superViewWidth: NSLayoutConstraint!
    @IBOutlet weak var superViewHeight: NSLayoutConstraint!
    
    @IBOutlet weak var productId: UILabel!
    
    @IBOutlet weak var productName: UILabel!
    
    @IBOutlet weak var startingPrice: UITextField!
    
    @IBOutlet weak var maxPrice: UITextField!
    
    @IBOutlet weak var startDateTime: UITextField!
    
    @IBOutlet weak var endDateTime: UITextField!
    
    @IBOutlet weak var status: DropDownButtonView!
    
    @IBOutlet weak var saveButton: UIButton!
    var info=[String:JSON]()
    var isEdit=false
    var proId=""
    var proName=""
    var auction_id=""
    override func viewDidLoad() {
        super.viewDidLoad()

        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as! String
        topView.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
        superViewWidth.constant=self.view.frame.width
        superViewHeight.constant=self.view.frame.height
        productId.text=proId
        productName.text=proName
        status.dropDownButton.addTarget(self, action: #selector(statusButtonTapped(_:)), for: .touchUpInside)
        saveButton.addTarget(self, action: #selector(saveButtonPressed(_:)), for: .touchUpInside)
        startDateTime.delegate=self
        
        endDateTime.delegate=self
        endDateTime.tag = 120
        if isEdit{
            startingPrice.text=info["starting_price"]?.stringValue
            maxPrice.text=info["max_price"]?.stringValue
            startDateTime.text=info["start_datetime"]?.stringValue
            endDateTime.text=info["end_datetime"]?.stringValue
            status.dropDownButton.setTitle(info["status"]?.stringValue, for: .normal)
        }
        
        
        // Do any additional setup after loading the view.
    }

    
    var datePickerView = UIDatePicker();
    let screenSize: CGRect = UIScreen.main.bounds;
    
    var textFieldForDate=UITextField()
    @objc func textFieldDidBeginEditing(_ textField: UITextField)
    {
        textField.resignFirstResponder();
        textFieldForDate=textField
        
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
        datePickerView.maximumDate = Date()
        datePickerView.datePickerMode = UIDatePicker.Mode.date;
        datePickerView.setValue(UIColor.black, forKeyPath: "textColor");
        datePickerView.backgroundColor = UIColor.white;
        datePickerView.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
        datePickerView.frame = CGRect(x: 0, y: CGFloat(30), width: tempInrView.frame.width,height: CGFloat(200));
        tempInrView.addSubview(datePickerView);
        
        if textField.tag == 120
        {
            if  startDateTime.text != ""
            {
                // print(filterView.value21.text)
                let isoDate = startDateTime.text
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
                ShowSimpleAlert.showSimpleAlert(self, showTitle: "Information", showMsg: "Please Select Start date")
                return
            }
        }
        
        
        
        
        
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
        dateFormatter.dateStyle = DateFormatter.Style.short
        dateFormatter.timeStyle = DateFormatter.Style.none
        dateFormatter.locale = Locale(identifier: "en_US_POSIX")
        dateFormatter.dateFormat = "yyyy-MM-dd";
        textFieldForDate.text = dateFormatter.string(from: datePickerView.date);
        textFieldForDate.resignFirstResponder();
        
        self.view.viewWithTag(161)?.removeFromSuperview();
    }
    
    
    @objc func saveButtonPressed(_ sender: UIButton){
        
        let userData = UserDefaults.standard.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        var postString = "vendor_id="+vendorId+"&hashkey="+hashKey
        if startDateTime.text=="" || endDateTime.text=="" || status.dropDownButton.currentTitle=="--Please Select--" || startingPrice.text==""{
            ShowSimpleAlert.showSimpleAlert(self, showTitle: "", showMsg: "Please fill all required fields.")
            return
        }
        postString+="&product_id="+proId+"&product_name="
        postString+=productName.text!+"&start_datetime="+startDateTime.text!
        postString+="&end_datetime="+endDateTime.text!+"&starting_price="+startingPrice.text!
        postString+="&max_price="+maxPrice.text!+"&status="+status.dropDownButton.currentTitle!
        if isEdit{
            postString+="&auction_id="+auction_id
        }
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        self.sendRequest(url: "vauctionapi/auction/save", parameters: postString)
    }
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        
        if requestUrl=="vauctionapi/auction/save"{
            if let data = data{
                guard let json = try? JSON(data: data) else {return}
                print(json)
                print(NSString(data: data, encoding: String.Encoding.utf8.rawValue))
                if json["data"]["success"].stringValue=="true"{
                    self.view.makeToast(json["data"]["message"].stringValue, duration: 2.0, position: .center);
                    Ced_CommonVendor.delay(2.0, closure: {
                        self.navigationController?.popViewController(animated: true);
                    })
                }
                else
                {
                    self.view.makeToast(json["data"]["message"].stringValue, duration: 2.0, position: .center);
                    
                }
            }
            
        }
    }
    @objc func statusButtonTapped(_ sender: UIButton){
        let dropDown = DropDown();
        let dropDownToShow = ["Processing","NotStarted","Cancelled","Completed","BidClosed"]
        //dropDownToShow = dropDownToShow.sorted();
        
        
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
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
