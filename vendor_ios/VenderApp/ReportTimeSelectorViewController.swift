/**
* CedCommerce
*
* NOTICE OF LICENSE
*
* This source file is subject to the End User License Agreement (EULA)
* that is bundled with this package in the file LICENSE.txt.
* It is also available through the world-wide-web at this URL:
* http://cedcommerce.com/license-agreement.txt
*
* @category  Ced
* @package   MageNative MultiVendor
* @author    CedCommerce Core Team <connect@cedcommerce.com >
* @copyright Copyright CEDCOMMERCE (http://cedcommerce.com/)
* @license      http://cedcommerce.com/license-agreement.txt
*/

import UIKit

class ReportTimeSelectorViewController: ced_VendorBaseClass,UITextFieldDelegate
{
    
    @IBOutlet weak var fetchReportButton: UIButton!
    
    var datePickerView = UIDatePicker();
    let screenSize: CGRect = UIScreen.main.bounds;
     var textFieldForDate=UITextField()
    var baseURL = settings.baseUrl
    var heightToUse : CGFloat = 0;
    var globalTextFieldTag = 0;
    let orderReportRangeView = OrderReportRangeView();
    var payment_state = "All";
    var period = "Day";
    
    override func viewDidLoad()
    {
        super.viewDidLoad()

        view.backgroundColor = DynamicColor.ViewBackgroundColor
        // Do any additional setup after loading the view.
        title="Order Reports".localized
        self.navigationController?.navigationBar.titleTextAttributes =
            [NSAttributedString.Key.foregroundColor: UIColor.white,NSAttributedString.Key.font: UIFont(name: "Roboto-Bold", size: 18)!]
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        fetchReportButton.setThemeColor()
        self.fetchReportButton.titleLabel?.font=UIFont(name: "Roboto-Light", size: 21)
        self.fetchReportButton.addTarget(self, action: #selector(ReportTimeSelectorViewController.sendProductReportFilterRequest(_:)), for: UIControl.Event.touchUpInside);
    ced_navigationBarController().addNavButton(self,str:"no")
        self.showProductReportRangeview();

    }

    override func didReceiveMemoryWarning()
    {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @objc func showProductReportRangeview()
    {
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        
        
       
        orderReportRangeView.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
        //orderReportRangeView.frame = CGRectMake(15, heightToUse, screenSize.width-30,CGFloat(476));
        
        //orderReportRangeView.center.x = self.view.center.x;
        orderReportRangeView.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width-40,height: CGFloat(335));
        
        orderReportRangeView.center = CGPoint(x: screenSize.width/2,y: screenSize.height/2+heightToUse);
        orderReportRangeView.topLabel.font=UIFont(name: "Roboto-Bold", size: 17)
        orderReportRangeView.periodLabel.font=UIFont(name: "Roboto-Medium", size: 15)
        orderReportRangeView.periodButton.titleLabel?.font=UIFont(name: "Roboto-Medium", size: 12)
        orderReportRangeView.topLabel.text = "  "+"Order Reports".localized.uppercased();
        orderReportRangeView.topLabel.backgroundColor = DynamicColor.secondaryColor;
        
        orderReportRangeView.periodLabel.text = "  "+"Period : ".localized;
        orderReportRangeView.periodButton.addTarget(self, action: #selector(ReportTimeSelectorViewController.showPeriodDropdown(_:)), for: UIControl.Event.touchUpInside);
        orderReportRangeView.periodButton.tag = 79;
        orderReportRangeView.periodButton.setTitle("Day".localized, for: UIControl.State());
        PopupLookImprovement.designButtonAsTextView(orderReportRangeView.periodButton);
        
        orderReportRangeView.paymentStatusLabel.font=UIFont(name: "Roboto-Medium", size: 15)
        orderReportRangeView.paymentStatusLabel.text = "  "+"Vendor Payment Status : ".localized;
        orderReportRangeView.paymentButton.titleLabel?.font=UIFont(name: "Roboto-Medium", size: 12)
        orderReportRangeView.paymentButton.addTarget(self, action: #selector(ReportTimeSelectorViewController.showpaymentStatusDropdown(_:)), for: UIControl.Event.touchUpInside);
        orderReportRangeView.paymentButton.tag = 80;
        orderReportRangeView.paymentButton.setTitle("All".localized, for: UIControl.State());
        PopupLookImprovement.designButtonAsTextView(orderReportRangeView.paymentButton);
        
        orderReportRangeView.startLabel.font=UIFont(name: "Roboto-Medium", size: 15)
        orderReportRangeView.startDate.font=UIFont(name: "Roboto-Medium", size: 12)
        orderReportRangeView.startLabel.text = "  "+"Start Date".localized+" : ".localized;
        orderReportRangeView.startDate.delegate = self;
        orderReportRangeView.startDate.tag = 77;
        
        orderReportRangeView.endLabel.font=UIFont(name: "Roboto-Medium", size: 15)
        orderReportRangeView.endDate.font=UIFont(name: "Roboto-Medium", size: 12)
        orderReportRangeView.endLabel.text = "  "+"End Date".localized+" : ".localized;
        orderReportRangeView.endDate.delegate = self;
        orderReportRangeView.endDate.tag = 78;
        
        heightToUse += CGFloat(330);
        self.view.addSubview(orderReportRangeView);
    }

    
    
    @objc func showPeriodDropdown(_ sender:UIButton)
    {
        let dataSource = ["Day".localized , "Month".localized , "Year".localized];
        let dataSourceValue = ["Day" , "Month" , "Year"];
        
        let dropDown = DropDown();
        dropDown.dataSource = dataSource;
        dropDown.selectionAction = {(index, item) in
            sender.setTitle(item, for: UIControl.State());
            self.period = dataSourceValue[index]
        }
        
        dropDown.anchorView = sender
        dropDown.bottomOffset = CGPoint(x: 0, y:sender.bounds.height)
        
        if dropDown.isHidden {
            dropDown.show();
        } else {
            dropDown.hide();
        }
    }
    
    @objc func showpaymentStatusDropdown(_ sender:UIButton)
    {
        let dataSource = ["All".localized,"Pending_status".localized , "Paid".localized ,"Refunded Status".localized , "Cancelled".localized];//
        let dataSourceValue = ["All" , "Pending" , "Paid", "Refunded" , "Canceled"]; //
        
        let dropDown = DropDown();
        dropDown.dataSource = dataSource;
        dropDown.selectionAction = {(index, item) in
            sender.setTitle(item, for: UIControl.State());
            self.payment_state = dataSourceValue[index]
        }
        
        dropDown.anchorView = sender
        dropDown.bottomOffset = CGPoint(x: 0, y:sender.bounds.height)
        
        if dropDown.isHidden {
            dropDown.show();
        } else {
            dropDown.hide();
        }
    }
    
    
    
    @objc func textFieldDidBeginEditing(_ textField: UITextField) {
        
      
        textField.resignFirstResponder();
        
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
        
        
        if textField.tag == 78
        {
            if orderReportRangeView.startDate.text != ""
            {
                // print(filterView.value21.text)
                let isoDate = orderReportRangeView.startDate.text
                let dateFormatter = DateFormatter()
                dateFormatter.dateFormat = "yyyy-MM-dd"
                dateFormatter.locale = Locale(identifier: "en_US_POSIX") // set locale to reliable US_POSIX
                let date = dateFormatter.date(from:isoDate!) ?? Date()
                let calendar = Calendar.current
                let components = calendar.dateComponents([.year, .month, .day, .hour], from: date)
                let finalDate = calendar.date(from:components)
                datePickerView.minimumDate = finalDate
                datePickerView.maximumDate = Date()
            }
            else
            {
                ShowSimpleAlert.showSimpleAlert(self, showTitle: "Information".localized, showMsg: "please select the first limit of the date".localized)
                return
            }
        }else if textField.tag == 77{
            let isoDate = orderReportRangeView.endDate.text ?? ""
            let dateFormatter = DateFormatter()
            dateFormatter.dateFormat = "yyyy-MM-dd"
            dateFormatter.locale = Locale(identifier: "en_US_POSIX") // set locale to reliable US_POSIX
            let date = dateFormatter.date(from:isoDate) ?? Date()
            let calendar = Calendar.current
            let components = calendar.dateComponents([.year, .month, .day, .hour], from: date)
            let finalDate = calendar.date(from:components)
            datePickerView.minimumDate = nil
            datePickerView.maximumDate = finalDate
        }
        
        datePickerView.locale = Locale(identifier: ced_storeVC.selectLangauge)
        datePickerView.datePickerMode = UIDatePicker.Mode.date;
        datePickerView.setValue(UIColor.black, forKeyPath: "textColor");
        datePickerView.backgroundColor = UIColor.white;
        datePickerView.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
        datePickerView.frame = CGRect(x: 0, y: CGFloat(30), width: tempInrView.frame.width,height: CGFloat(200));
        if(period == "Day"){
            datePickerView.datePickerMode = .date
        }else{
            if #available(iOS 17.4, *) {
                datePickerView.datePickerMode = .yearAndMonth
            } else {
                datePickerView.datePickerMode = .date
            }
        }
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
        dateFormatter.dateStyle = DateFormatter.Style.short
        dateFormatter.timeStyle = DateFormatter.Style.none
        dateFormatter.locale = Locale(identifier: "en_US_POSIX")
        dateFormatter.dateFormat = "yyyy-MM-dd";
        textFieldForDate.text = dateFormatter.string(from: datePickerView.date);
        textFieldForDate.resignFirstResponder();
        
        self.view.viewWithTag(161)?.removeFromSuperview();
    }
    
    
    @objc func sendProductReportFilterRequest(_ sender:UIButton)
    {
        
        var to = "";
        var from = "";
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        //print(userData)
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        
        if let textField = self.view.viewWithTag(77) as? UITextField
        {
            if(textField.text == nil || textField.text == "")
            {
                self.view.showToastMsg("Please Select Date Range".localized);
                return;
            }
            from = textField.text!;
        }
        if let textField = self.view.viewWithTag(78) as? UITextField
        {
            if(textField.text == nil || textField.text == "")
            {
                self.view.showToastMsg("Please Select Date Range".localized);
                return;
            }
            to = textField.text!;
        }
        let secondsStamp = Int(Date().timeIntervalSince1970)
        var postString = "vendor_id="+vendorId;
        postString += "&hashkey="+hashKey;
        postString += "&period="+period;
        postString += "&payment_state="+payment_state;
        postString += "&to="+to;
        postString += "&from="+from;
        postString += "&t=\(secondsStamp)";
        let viewcontrol = self.storyboard?.instantiateViewController(withIdentifier: "ordersReports") as! ced_viewOrdersReports;
        viewcontrol.postString = postString
        viewcontrol.payment_state = payment_state
        self.navigationController?.pushViewController(viewcontrol, animated: true)
    }
    
}
