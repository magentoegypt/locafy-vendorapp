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

class ProductReportRangeViewController: ced_VendorBaseClass,UITextFieldDelegate
{

    @IBOutlet weak var showReportButton: UIButton!
    var textFieldForDate=UITextField()
    var datePickerView = UIDatePicker();
    let screenSize: CGRect = UIScreen.main.bounds;
    let productReportRangeView = ProductReportRangeView();
    var heightToUse : CGFloat = 0;
    var globalTextFieldTag = 0;
 
    override func viewDidLoad()
    {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        view.backgroundColor = DynamicColor.ViewBackgroundColor
        self.showReportButton.addTarget(self, action: #selector(ProductReportRangeViewController.sendProductReportFilterRequest(_:)), for: UIControl.Event.touchUpInside);
        title="Product Reports".localized
        self.navigationController?.navigationBar.titleTextAttributes =
            [NSAttributedString.Key.foregroundColor: UIColor.white,NSAttributedString.Key.font: UIFont(name: "Roboto-Bold", size: 18)!]
        
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        self.showReportButton.backgroundColor = color;
        ced_navigationBarController().addNavButton(self,str:"no")
        showReportButton.titleLabel?.font=UIFont(name: "Roboto-Light", size: 21)
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
        
        
        
        productReportRangeView.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
        productReportRangeView.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width-40,height: CGFloat(195));
    
        productReportRangeView.center = CGPoint(x: screenSize.width/2,y: screenSize.height/2+heightToUse);
        productReportRangeView.topLabel.font=UIFont(name: "Roboto-Bold", size: 17)
        productReportRangeView.startLabel.font=UIFont(name: "Roboto-Medium", size: 15)
        productReportRangeView.endLabel.font=UIFont(name: "Roboto-Medium", size: 15)
        productReportRangeView.topLabel.text = "  "+"Product Reports".localized.uppercased();
        productReportRangeView.startDate.font=UIFont(name: "Roboto-Medium", size: 12)
        productReportRangeView.endDate.font=UIFont(name: "Roboto-Medium", size: 12)
        productReportRangeView.topLabel.backgroundColor = color;
        
        productReportRangeView.startLabel.text = "  "+"Start Date".localized+" : ";
        productReportRangeView.startDate.delegate = self;
        productReportRangeView.startDate.tag = 77;
    
        productReportRangeView.endLabel.text = "  "+"End Date".localized+" : ";
        productReportRangeView.endDate.delegate = self;
        productReportRangeView.endDate.tag = 78;
        
        heightToUse += CGFloat(195);
    
        self.view.addSubview(productReportRangeView);
    
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
        tempInrView.backgroundColor = DynamicColor.ViewBackgroundColor//UIColor.white;
        tempInrView.center = CGPoint(x: bgCView.frame.size.width  / 2,y: bgCView.frame.size.height / 2);
        
        let label = UILabel();
        label.frame = CGRect(x: 0, y: CGFloat(0), width: tempInrView.frame.width,height: CGFloat(30));
        label.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        label.text = "Select Date".localized.uppercased();
        label.textAlignment = NSTextAlignment.center;
        label.textColor = UIColor.white;
        label.backgroundColor = color;
        tempInrView.addSubview(label);
        
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd"
        dateFormatter.locale = Locale(identifier: "en_US_POSIX")
        if textField.tag == 78
        {
            if productReportRangeView.startDate.text != ""
            {
                // print(filterView.value21.text)
                let isoDate = productReportRangeView.startDate.text
                 // set locale to reliable US_POSIX
                let date = dateFormatter.date(from:isoDate!)!
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
            let isoDate = productReportRangeView.endDate.text ?? ""
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
        
        var postString = "vendor_id="+vendorId;
        postString += "&hashkey="+hashKey;
        postString += "&to="+to;
        postString += "&from="+from;
        
        
        let viewcontrol = self.storyboard?.instantiateViewController(withIdentifier: "productreports") as! ced_viewproductReports;
        viewcontrol.postString = postString;
        self.navigationController?.pushViewController(viewcontrol, animated: true)
    }

    

}
