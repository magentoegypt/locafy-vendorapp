//
//  ced_editprofileViewController.swift
//  VenderApp
//
//  Created by cedcoss on 12/02/19.
//  Copyright © 2019 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit
class VendorProfileViewController: ced_VendorBaseClass {
    
    
    @IBOutlet weak var mainStackView: UIStackView!
    @IBOutlet weak var mainViewwidth: NSLayoutConstraint!
    @IBOutlet weak var mainViewHeight: NSLayoutConstraint!
    @IBOutlet weak var deleteAccountbtn: UIButton!
    var links = [Int: String]()
    
    @IBOutlet weak var editButton: UIButton!
    
    @IBOutlet weak var CustumerName: UIButton!
    
    struct ProductFeild {
        var headingValue = String()
        var dataValue = [JSON]()
    }
    var allProductFeild = [ProductFeild]()
    override func viewDidLoad() {
        super.viewDidLoad()
    
        view.backgroundColor = DynamicColor.ViewBackgroundColor
        
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        let postString = "vendor_id="+vendorId+"&hashkey="+hashKey
        let baseURL = "vendorapi/index/infos"
        Alert_File.addLoadingIndicator(self, msg: NSLocalizedString("Please Wait...".localized, comment: ""));
        
        sendRequest(url: baseURL, parameters: postString)
        deleteAccountbtn.setThemeColor();
        editButton.setThemeColor();
        editButton.setTitle( NSLocalizedString("Edit Profile".localized, comment: ""), for: .normal)
        editButton.addTarget(self, action: #selector(goToVendorEditInfoPage(_:)), for: .touchUpInside)
        deleteAccountbtn.addTarget(self, action: #selector(deleteAccountbtntpd(_:)), for: .touchUpInside)
    }
    
    @objc func goToVendorEditInfoPage(_ sender:UIButton)
    {
        //        Alert_File.addLoadingIndicator(self, msg: NSLocalizedString("Please Wait...", comment: ""));
        let viewcontrol = self.storyboard?.instantiateViewController(withIdentifier: "editProfileViewController") as! EditProfileViewController;
        self.navigationController?.pushViewController(viewcontrol, animated: true)
        //        Alert_File.removeLoadingIndicator(self);
    }
    
    @objc func deleteAccountbtntpd(_ sender: UIButton!) {
            let confirmationAlert = UIAlertController(title: "Confirmation".localized, message: "Do you want to delete account".localized, preferredStyle: UIAlertController.Style.alert)
            confirmationAlert.addAction(UIAlertAction(title: "Yes".localized, style: .default, handler: { (action: UIAlertAction!) in
                (UIApplication.shared.delegate as! AppDelegate).deleteAccount()
                self.defaults.set(false, forKey: "isLogin")
                self.defaults.removeObject(forKey: "userInfoDict")
                self.defaults.removeObject(forKey: "cedNavTransact")
                self.defaults.removeObject(forKey: "cedNavReport")
                self.defaults.removeObject(forKey: "cedNavOrder")
                self.defaults.removeObject(forKey: "cedNavMemberShipPlans")
                self.defaults.removeObject(forKey: "cedNaveitems")
                let storyboard = UIStoryboard(name: "ced_vendorLogin", bundle: nil)
                (UIApplication.shared.delegate as! AppDelegate).changeModules()
                
            }));
            confirmationAlert.addAction(UIAlertAction(title: "No".localized, style: .default, handler: { (action: UIAlertAction!) in
                //print("Handle Cancel Logic here")
            }));
            present(confirmationAlert, animated: true, completion: nil)
        }
    
    
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        Alert_File.removeLoadingIndicator(self);
        
        if let data = data{
            guard let jsonData = try? JSON(data: data) else {return}
            print(jsonData)
            mainViewHeight.constant = 5
            if jsonData["success"].stringValue == "true"{
                if(jsonData["badges"].exists())
                {
                    var badges = [String:String]()
                    for(key,val) in jsonData["badges"]
                    {
                        badges[key] = val.stringValue
                    }
                    makeBadge(badgeArray: badges)
                }
                for (key,values) in jsonData["data"]{
                    print(key)
                    print(values)
                    let product = ProductFeild(headingValue: key, dataValue: values.arrayValue)
                    allProductFeild.append(product)
                }
                let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
                //print(userData)
                let vendorId = userData["vendorId"] as! String
                let hashKey    = userData["hashKey"] as! String
                let customer_id = userData["customerId"] as! String;
                let vendor_name = userData["vendorName"] as! String;
                let profile_picture = userData["profilePicUrl"] as! String;
                let profile_complete = userData["profile_complete"] as! String
                let valerts = jsonData["valerts"].stringValue
                //saving value in NSUserDefault to use later on :: start
                let dict = ["customerId": customer_id, "hashKey": hashKey,"vendorName":vendor_name,"vendorId":vendorId,"profilePicUrl":profile_picture,"valerts":valerts,"profile_complete":profile_complete];
                self.defaults.set(dict, forKey: "userInfoDict");
                makeViewProfile()
            }
        }
    }
    
    @objc func makeBadge(badgeArray: [String:String]){
        let badgeView = BadgeInfoView()
        
        badgeView.topLabel.font=UIFont(name: "Roboto-Medium", size: 17)
        badgeView.topLabel.text = "   Your Badges".localized.uppercased()
        //badgeView.badgeImage.tag=2234
        //badgeView.badgeButton.tag=2235
        badgeView.badgeImage.sd_setImage(with: URL(string: badgeArray["order"]!), placeholderImage: UIImage(named: "badge"))
        //badgeView.badgeImage.sd_setImage(with: URL(string: badgeImage))
        badgeView.badgeButton.setTitle(badgeArray["order_count"]!+" "+"sales".localized, for: .normal)
        badgeView.badgeButton.setTitleColor(UIColor.black, for: .normal)
        badgeView.badgeButton.titleLabel?.font=UIFont(name: "Roboto-Medium", size: 18)
        //badgeView.reviewImage.tag=2236
        //badgeView.reviewRating.tag=2237
        badgeView.reviewImage.sd_setImage(with: URL(string: badgeArray["review"]!))
        let heightConstrain = NSLayoutConstraint(item: badgeView, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .height, multiplier: 1.0, constant: 150)
        badgeView.addConstraint(heightConstrain)
        badgeView.ratingView.isUserInteractionEnabled = false;
        //badgeView.reviewRating.text=reviewStar.description
        badgeView.ratingView.halfRatings = true;
        badgeView.ratingView.rating = Float(badgeArray["review_count"]!)!/20.0
        
        mainStackView.addArrangedSubview(badgeView);
        mainViewHeight.constant += 160;
        
        // let star = reviewStar
        
        /*badgeView.starWidth.constant=0
         for _ in 0..<5
         {
         let starButton=UIImageView()
         starButton.frame=CGRect(x: 0, y: 0, width: 15, height: 15)
         starButton.image=UIImage(named: "star")
         badgeView.starStack.addArrangedSubview(starButton)
         badgeView.starWidth.constant += 17
         badgeView.starStack.distribution = .fillEqually
         }*/
    }
    
    override func viewWillLayoutSubviews() {
        super.viewWillLayoutSubviews()
        mainViewwidth.constant = self.view.frame.width
    }
    
    
    func makeViewProfile(){
        var height = 0
        var tag = 100;
        for items in allProductFeild{
            let headerView = HeaderView()
            headerView.topLabel.text = items.headingValue
            let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
            let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
            headerView.topLabel.backgroundColor = DynamicColor.secondaryColor
            headerView.topLabel.textColor = .white
            
            var heightConstrain = NSLayoutConstraint(item: headerView, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .height, multiplier: 1.0, constant: 59)
            headerView.addConstraint(heightConstrain)
            mainViewHeight.constant += 69
            mainStackView.addArrangedSubview(headerView)
        
            for val in items.dataValue{
                print(val)
                print(val["label"].stringValue) //value
                if(val["label"].stringValue.lowercased() == "Shop Url Done".lowercased()){
                    continue
                }
                if val["type"].stringValue == "image"{
                    let profile = LabelImage()
                    profile.keyvalues.text = val["label"].stringValue
                    let image = val["value"].stringValue
                    let imageString = image.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)
                    
                        profile.value.sd_setImage(with: URL(string: imageString ?? ""), placeholderImage: UIImage(named: "placeholder"))
                    
                    mainStackView.addArrangedSubview(profile)
                    heightConstrain = NSLayoutConstraint(item: profile, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .height, multiplier: 1.0, constant: 80)
                    profile.addConstraint(heightConstrain)
                    mainViewHeight.constant += 90
                    
                }else if val["type"].stringValue == "file"{
                    
                    let profile = FileLinkView()
                    profile.titleLabel.text = val["label"].stringValue
                    
                    //profile.button.setTitle(image, for: .normal);
                    profile.button.addTarget(self, action: #selector(linkClicked(_:)), for: .touchUpInside);
                    profile.button.tag = tag;
                    links[tag] = val["value"].stringValue
                    mainStackView.addArrangedSubview(profile)
                    heightConstrain = NSLayoutConstraint(item: profile, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .height, multiplier: 1.0, constant: 40)
                    profile.addConstraint(heightConstrain)
                    mainViewHeight.constant += 50
                    tag += 1;
                }else{
                    let profile = detailsViewEditProfile()
                    profile.keyvalues.text = val["label"].stringValue
                    profile.value.text = val["value"].stringValue
                    mainStackView.addArrangedSubview(profile)
                    heightConstrain = NSLayoutConstraint(item: profile, attribute: .height, relatedBy: .equal, toItem: nil, attribute: .height, multiplier: 1.0, constant: 40)
                    profile.addConstraint(heightConstrain)
                    mainViewHeight.constant += 50
                }
                
            }
            //            mainViewHeight.constant += 100
        }
        print(height)
        //mainViewHeight.constant = CGFloat(height)
        
    }
    
    @objc func linkClicked(_ sender: UIButton)
    {
        if let linkUrl = links[sender.tag]
        {
            let viewControl = storyboard?.instantiateViewController(withIdentifier: "ced_WebView") as! ced_WebView
            viewControl.pageUrl = linkUrl;
            self.navigationController?.pushViewController(viewControl, animated: true)
        }
        
    }
    
}



extension String {
    func deletingPrefix(_ prefix: String) -> String {
        guard self.hasPrefix(prefix) else { return self }
        return String(self.dropFirst(prefix.count))
    }
}
