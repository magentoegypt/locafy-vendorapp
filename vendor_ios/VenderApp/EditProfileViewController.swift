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

class ced_EditProfile: ced_VendorBaseClass,UITableViewDataSource,UITableViewDelegate,UIImagePickerControllerDelegate,UINavigationControllerDelegate,UIGestureRecognizerDelegate, UITextViewDelegate
{
    //var imgNameToSend = [Int:String]();
    var imgLoop = 31;//imp new data for img upload
    var imagesToUpload = [Int]();// tags of image view to be uploaded
    var postString = "";
    var image1 = false
    var image2 = false
    var image3 = false
    var countryArray = [String:String]();
    var cityArray = [String:String]();
    var locationArray = [String:String]();
    var statesArray = [String:String]();
    var stateDropDownIsVisible = true;// to check dropdown is visible or textfield
    var stateSelectionViewTag = -1;//tag of the state view
    @IBOutlet weak var editProfileTableView: UITableView!
    @IBOutlet weak var topWrapperView: UIView!
    @IBOutlet weak var rightButton: UIButton!
    @IBOutlet weak var leftButton: UIButton!
    var vendorProfileInfo = [String:String]();
    var showPasswordFields = false;
    var currentImgTag = -1
    let screenSize: CGRect = UIScreen.main.bounds;
    var baseURL = String();
    var heightToUse : CGFloat = 0;
    var heightToUse1 : CGFloat = 0;
    var fixedHeight : CGFloat = 70;
    var padding : CGFloat = 5;
    
    var tagCounterToBeUsed = 50
    var countryDict = [[String: String]]()
    var countryList = [String]();
    
    @objc func browseImageToUplaod( _ sender:UIButton )
    {
        print(sender.tag)
        self.currentImgTag = sender.tag;
        self.imagesToUpload.append(sender.tag);
        let ImagePicker = UIImagePickerController();
        ImagePicker.delegate = self;
        ImagePicker.sourceType = UIImagePickerController.SourceType.photoLibrary;
        self.present(ImagePicker, animated: true, completion: nil);
    }
    
    @objc func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
// Local variable inserted by Swift 4.2 migrator.
let info = convertFromUIImagePickerControllerInfoKeyDictionary(info)

        
        if let img = self.view.viewWithTag(self.currentImgTag) as? UIImageView
        {
            img.image = info[convertFromUIImagePickerControllerInfoKey(UIImagePickerController.InfoKey.originalImage)] as? UIImage
        }
        self.dismiss(animated: true, completion: nil);
    }
    
    override func viewDidLoad()
    {
        super.viewDidLoad()
        print(vendorProfileInfo)
        self.title = "Edit Profile"
        self.navigationController?.navigationBar.titleTextAttributes =
            [NSAttributedString.Key.foregroundColor: UIColor.white,NSAttributedString.Key.font: UIFont(name: "Roboto-Bold", size: 18)!]
        self.editProfileTableView.dataSource = self;
        self.editProfileTableView.delegate = self;
        self.leftButton.titleLabel?.font=UIFont(name: "Roboto-Regular", size: 17)
        self.rightButton.titleLabel?.font=UIFont(name: "Roboto-Regular", size: 17)
        self.leftButton.setTitle("Edit Profile", for: UIControl.State());
        self.rightButton.setTitle("Save".localized, for: UIControl.State());
        self.rightButton.addTarget(self, action: #selector(sendSaveDetailsRequest(_:)), for: UIControl.Event.touchUpInside);
        let color = Ced_CommonVendor.UIColorFromRGB("#2abb9c");
        self.rightButton.backgroundColor = color;
        ced_navigationBarController().addNavButton(self,str:"back")
        self.fetchCountryListing()
    }
    
    // MARK: - Table view data source
    
    @objc func numberOfSections(in tableView: UITableView) -> Int
    {
        return 1;
    }
    
    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int
    {
        return 2;
    }
    
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
    {
        if(indexPath.row == 1)
        {
            let cell = editProfileTableView.dequeueReusableCell(withIdentifier: "longSingleCell", for: indexPath) as! LongSingleTableViewCell;
            cell.backgroundColor = UIColor.white;
            
            if(!cell.dataLoaded)
            {
                cell.dataLoaded = true;
            }
            else
            {
                return cell;
            }
            let checkboxButtonView = CheckboxButtonView();
            checkboxButtonView.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
            checkboxButtonView.frame = CGRect(x: 0, y: heightToUse1, width: screenSize.width,height: 30);
            heightToUse1 += 30;
            heightToUse1 += padding;
            checkboxButtonView.checkboxButton.titleLabel?.font=UIFont(name: "Roboto-Regular", size: 15)
            checkboxButtonView.checkboxButton.setTitle("Change Password :", for: UIControl.State());
            checkboxButtonView.checkboxButton.addTarget(self, action: #selector(showChangePasswordView(_:)), for: UIControl.Event.touchUpInside);
            checkboxButtonView.checkboxButton.setImage(UIImage(named: "unchecked_checkbox"), for: UIControl.State());
            cell.mainWrapperView.addSubview(checkboxButtonView);
            
            if(showPasswordFields)
            {
                checkboxButtonView.checkboxButton.setImage(UIImage(named: "checked_checkbox"), for: UIControl.State())
                let label_TextView_Combo_View_currentPass = Label_TextView_Combo_View();
                label_TextView_Combo_View_currentPass.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
                label_TextView_Combo_View_currentPass.frame = CGRect(x: 0, y: heightToUse1, width: screenSize.width,height: fixedHeight);
                heightToUse1 += fixedHeight;
                
                label_TextView_Combo_View_currentPass.entityNameLabel.text = "Current Password".localized;
                label_TextView_Combo_View_currentPass.entityValueTxtField.text = "";
                label_TextView_Combo_View_currentPass.entityValueTxtField.tag = 91;
                cell.mainWrapperView.addSubview(label_TextView_Combo_View_currentPass);
                
                /** New Password **/
                let label_TextView_Combo_View_newPass = Label_TextView_Combo_View();
                label_TextView_Combo_View_newPass.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
                label_TextView_Combo_View_newPass.frame = CGRect(x: 0, y: heightToUse1, width: screenSize.width,height: fixedHeight);
                heightToUse1 += fixedHeight;
                label_TextView_Combo_View_newPass.entityNameLabel.font=UIFont(name: "Roboto-Regular", size: 15)
                label_TextView_Combo_View_newPass.entityNameLabel.text = "New Password".localized;
                label_TextView_Combo_View_newPass.entityValueTxtField.text = "";
                label_TextView_Combo_View_newPass.entityValueTxtField.tag = 92;
                cell.mainWrapperView.addSubview(label_TextView_Combo_View_newPass);
            }
            return cell;
        }
        
        let cell = editProfileTableView.dequeueReusableCell(withIdentifier: "longSingleCell", for: indexPath) as! LongSingleTableViewCell;
        
        if(!cell.dataLoaded)
        {
            cell.dataLoaded = true;
        }
        else
        {
            return cell;
        }
        cell.backgroundColor = UIColor.white;
        /** Top View **/
        /*let profileTopView = ProfileTopView();
         profileTopView.autoresizingMask = [UIViewAutoresizing.FlexibleLeftMargin,UIViewAutoresizing.FlexibleRightMargin];
         profileTopView.frame = CGRectMake(0, heightToUse, screenSize.width-10,CGFloat(90));
         profileTopView.center.x = self.view.center.x;
         heightToUse += CGFloat(90);
         
         profileTopView.topButton.addTarget(self, action: Selector("sendSaveDetailsRequest:"), forControlEvents: UIControlEvents.TouchUpInside);
         cell.mainWrapperView.addSubview(profileTopView);
         */
        /** Public Name **/
        let label_TextView_Combo_View_name = Label_TextView_Combo_View();
        label_TextView_Combo_View_name.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
        label_TextView_Combo_View_name.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width,height: fixedHeight);
        heightToUse += fixedHeight;
        heightToUse += padding;
        label_TextView_Combo_View_name.entityNameLabel.font=UIFont(name: "Roboto-Regular", size: 15)
        label_TextView_Combo_View_name.entityNameLabel.text = "Public Name";
        label_TextView_Combo_View_name.entityValueTxtField.text = self.vendorProfileInfo["public_name"];
        label_TextView_Combo_View_name.entityValueTxtField.tag = tagCounterToBeUsed;
        tagCounterToBeUsed += 1;
        cell.mainWrapperView.addSubview(label_TextView_Combo_View_name);
        
        /** Representative **/
        let label_TextView_Combo_View_represent = Label_TextView_Combo_View();
        label_TextView_Combo_View_represent.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
        label_TextView_Combo_View_represent.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width,height: fixedHeight);
        heightToUse += fixedHeight;
        heightToUse += padding;
        label_TextView_Combo_View_represent.entityNameLabel.text = "Representative";
        label_TextView_Combo_View_represent.entityValueTxtField.text = self.vendorProfileInfo["name"];
        label_TextView_Combo_View_represent.entityValueTxtField.tag = tagCounterToBeUsed;
        tagCounterToBeUsed += 1;
        cell.mainWrapperView.addSubview(label_TextView_Combo_View_represent);
        
        /** Gender **/
        let label_TextView_Combo_View_gender = Label_DropDown_Combo_View();
        label_TextView_Combo_View_gender.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
        label_TextView_Combo_View_gender.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width,height: fixedHeight);
        heightToUse += fixedHeight;
        heightToUse += padding;
        label_TextView_Combo_View_gender.entityNameLabel.text = "Gender";
        label_TextView_Combo_View_gender.dropDownButton.titleLabel?.font=UIFont(name: "Roboto-Regular", size: 15)
        if(self.vendorProfileInfo["gender"]?.lowercased() == "male")
        {
            label_TextView_Combo_View_gender.dropDownButton.setTitle("Male", for: UIControl.State());
        }
        else
        {
            label_TextView_Combo_View_gender.dropDownButton.setTitle("Female", for: UIControl.State());
        }
        label_TextView_Combo_View_gender.dropDownButton.addTarget(self, action: #selector(showGenderDropdown(_:)), for: UIControl.Event.touchUpInside);
        label_TextView_Combo_View_gender.dropDownButton.tag = tagCounterToBeUsed;
        tagCounterToBeUsed += 1;
        cell.mainWrapperView.addSubview(label_TextView_Combo_View_gender);
        
        
        /** Profile Picture **/
        let label_TextView_Combo_View_profilePic = Label_ImageView_Combo_View();
        label_TextView_Combo_View_profilePic.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
        label_TextView_Combo_View_profilePic.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width,height: 110);
        heightToUse += 110;
        heightToUse += padding;
        label_TextView_Combo_View_profilePic.entityNameLabel.text = "Profile Picture";
        label_TextView_Combo_View_profilePic.imgView.tag = 31;
        label_TextView_Combo_View_profilePic.browseButton.tag = 31;
        label_TextView_Combo_View_profilePic.imgView.image = UIImage(named: "profle");
        PopupLookImprovement.designImageView(label_TextView_Combo_View_profilePic.imgView);
        
        label_TextView_Combo_View_profilePic.imgView.isUserInteractionEnabled = true;
        let tapRecognizer = UITapGestureRecognizer(target: self, action: #selector(imageTapped(_:)))
        label_TextView_Combo_View_profilePic.imgView.addGestureRecognizer(tapRecognizer);
        
        
        label_TextView_Combo_View_profilePic.browseButton.addTarget(self, action: #selector(browseImageToUplaod(_:)), for: UIControl.Event.touchUpInside);
        PopupLookImprovement.designButton(label_TextView_Combo_View_profilePic.browseButton);
        cell.mainWrapperView.addSubview(label_TextView_Combo_View_profilePic);
        
        //deletebutton of profile section
        /*let deleteCheckboxButtonView = CheckboxButtonView();
         deleteCheckboxButtonView.autoresizingMask = [UIViewAutoresizing.flexibleLeftMargin,UIViewAutoresizing.flexibleRightMargin];
         deleteCheckboxButtonView.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width,height: 20);
         heightToUse += 20;
         heightToUse += padding;
         deleteCheckboxButtonView.checkboxButton.titleLabel?.font=UIFont(name: "Roboto-Regular", size: 15)
         deleteCheckboxButtonView.checkboxButton.setTitle("Delete Image", for: UIControlState());
         deleteCheckboxButtonView.checkboxButton.addTarget(self, action: #selector(EditProfileViewController.deleteButtonPressed(_:)), for: UIControlEvents.touchUpInside);
         deleteCheckboxButtonView.checkboxButton.tag=31
         deleteCheckboxButtonView.checkboxButton.setImage(UIImage(named: "unchecked_checkbox"), for: UIControlState());
         cell.mainWrapperView.addSubview(deleteCheckboxButtonView);
         */
        
        
        
        
        print("vendorProfileInfo[\"profile_picture\"]");
        print(vendorProfileInfo["profile_picture"]);
        if(vendorProfileInfo["profile_picture"] != "")
        {
            var imgRequest1 = URLRequest(url: URL(string: vendorProfileInfo["profile_picture"]!)!);
            let requestHeader = Ced_CommonVendor.getInfoPlist("requestheader") as! String
            imgRequest1.setValue(requestHeader, forHTTPHeaderField: "Mobiconnectheader")
            let sessionConfig = URLSessionConfiguration.default
//            sessionConfig.httpAdditionalHeaders = [
//                "Authorization": "Basic \(Ced_CommonVendor.getAuthData())",
//            ]
            sessionConfig.timeoutIntervalForRequest = timeoutIntervalForRequest
            sessionConfig.timeoutIntervalForResource = timeoutIntervalForResource
            let session = URLSession(configuration: sessionConfig)
            let task1 = session.dataTask(with: imgRequest1, completionHandler: {
                // check for fundamental networking error
                data, response, error in
                guard error == nil && data != nil else
                {
                    print("error=\(error)")
                    return;
                }
                
                // check for http errors
                if let httpStatus = response as? HTTPURLResponse, httpStatus.statusCode != 200
                {
                    print("statusCode should be 200, but is \(httpStatus.statusCode)")
                    print("response = \(response)")
                    return;
                }
                
                // Convert the downloaded data in to a UIImage object
                if let data = data{
                    let image = UIImage(data: data)
                    // Store the image in to our cache
                    //self.imageCache[urlString] = image
                    // Update the cell
                    DispatchQueue.main.async
                        {
                            label_TextView_Combo_View_profilePic.imgView.image = image;
                    }
                }
                
            })
            
            task1.resume();
        }
        
        /** Email **/
        let label_TextView_Combo_View_email = Label_TextView_Combo_View();
        label_TextView_Combo_View_email.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
        label_TextView_Combo_View_email.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width,height: fixedHeight);
        heightToUse += fixedHeight;
        label_TextView_Combo_View_email.entityNameLabel.font=UIFont(name: "Roboto-Regular", size: 15)
        label_TextView_Combo_View_email.entityNameLabel.text = "Email";
        label_TextView_Combo_View_email.entityValueTxtField.text = self.vendorProfileInfo["email"];
        label_TextView_Combo_View_email.entityValueTxtField.tag = tagCounterToBeUsed;
        
        tagCounterToBeUsed += 1;
        cell.mainWrapperView.addSubview(label_TextView_Combo_View_email);
        print("bbb");
        /** Contact **/
        let label_TextView_Combo_View_contact = Label_TextView_Combo_View();
        label_TextView_Combo_View_contact.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
        label_TextView_Combo_View_contact.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width,height: fixedHeight);
        heightToUse += fixedHeight;
        label_TextView_Combo_View_contact.entityNameLabel.text = "Contact Number";
        label_TextView_Combo_View_contact.entityValueTxtField.text = self.vendorProfileInfo["contact_number"];
        label_TextView_Combo_View_contact.entityValueTxtField.tag = tagCounterToBeUsed;
        label_TextView_Combo_View_contact.entityValueTxtField.delegate = self
        tagCounterToBeUsed += 1;
        
        cell.mainWrapperView.addSubview(label_TextView_Combo_View_contact);
        print("ccc");
        /** Company **/
        let label_TextView_Combo_View_company = Label_TextView_Combo_View();
        label_TextView_Combo_View_company.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
        label_TextView_Combo_View_company.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width,height: fixedHeight);
        heightToUse += fixedHeight;
        label_TextView_Combo_View_company.entityNameLabel.text = "Company Name";
        label_TextView_Combo_View_company.entityValueTxtField.text = self.vendorProfileInfo["company_name"];
        label_TextView_Combo_View_company.entityValueTxtField.tag = tagCounterToBeUsed;
        tagCounterToBeUsed += 1;
        cell.mainWrapperView.addSubview(label_TextView_Combo_View_company);
        
        
        /** About **/
        let label_TextView_Combo_View_about = Label_TextView_Combo_View();
        label_TextView_Combo_View_about.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
        label_TextView_Combo_View_about.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width,height: fixedHeight);
        heightToUse += fixedHeight;
        label_TextView_Combo_View_about.entityNameLabel.text = "About";
        label_TextView_Combo_View_about.entityValueTxtField.text = self.vendorProfileInfo["about"];
        label_TextView_Combo_View_about.entityValueTxtField.tag = tagCounterToBeUsed;
        tagCounterToBeUsed += 1;
        cell.mainWrapperView.addSubview(label_TextView_Combo_View_about);
        
        /** Company Logo **/
        let label_TextView_Combo_View_logo = Label_ImageView_Combo_View();
        label_TextView_Combo_View_logo.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
        label_TextView_Combo_View_logo.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width,height: 110);
        heightToUse += 110;
        heightToUse += padding;
        label_TextView_Combo_View_logo.entityNameLabel.text = "Company Logo";
        label_TextView_Combo_View_logo.imgView.tag = 32;
        label_TextView_Combo_View_logo.imgView.image = UIImage(named: "profle");
        PopupLookImprovement.designImageView(label_TextView_Combo_View_logo.imgView);
        label_TextView_Combo_View_logo.browseButton.tag = 32;
        label_TextView_Combo_View_logo.browseButton.addTarget(self, action: #selector(browseImageToUplaod(_:)), for: UIControl.Event.touchUpInside);
        PopupLookImprovement.designButton(label_TextView_Combo_View_logo.browseButton);
        cell.mainWrapperView.addSubview(label_TextView_Combo_View_logo);
        
        //deletebutton of company logo section
        /*let deleteCheckboxButtonView1 = CheckboxButtonView();
         deleteCheckboxButtonView1.autoresizingMask = [UIViewAutoresizing.flexibleLeftMargin,UIViewAutoresizing.flexibleRightMargin];
         deleteCheckboxButtonView1.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width,height: 20);
         heightToUse += 20;
         heightToUse += padding;
         deleteCheckboxButtonView1.checkboxButton.titleLabel?.font=UIFont(name: "Roboto-Regular", size: 15)
         deleteCheckboxButtonView1.checkboxButton.setTitle("Delete Image", for: UIControlState());
         deleteCheckboxButtonView1.checkboxButton.addTarget(self, action: #selector(EditProfileViewController.deleteButtonPressed(_:)), for: UIControlEvents.touchUpInside);
         deleteCheckboxButtonView1.checkboxButton.tag=32
         deleteCheckboxButtonView1.checkboxButton.setImage(UIImage(named: "unchecked_checkbox"), for: UIControlState());
         cell.mainWrapperView.addSubview(deleteCheckboxButtonView1);
         */
        
        
        /** Company Banner **/
        let label_TextView_Combo_View_banner = Label_ImageView_Combo_View();
        label_TextView_Combo_View_banner.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
        label_TextView_Combo_View_banner.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width,height: 110);
        heightToUse += 110;
        heightToUse += padding;
        label_TextView_Combo_View_banner.entityNameLabel.text = "Company Banner";
        label_TextView_Combo_View_banner.imgView.tag = 33;
        label_TextView_Combo_View_banner.imgView.image = UIImage(named: "profle");
        
        PopupLookImprovement.designImageView(label_TextView_Combo_View_banner.imgView);
        
        label_TextView_Combo_View_banner.browseButton.tag = 33;
        label_TextView_Combo_View_banner.browseButton.addTarget(self, action: #selector(browseImageToUplaod(_:)), for: UIControl.Event.touchUpInside);
        PopupLookImprovement.designButton(label_TextView_Combo_View_banner.browseButton);
        cell.mainWrapperView.addSubview(label_TextView_Combo_View_banner);
        
        //deletebutton of company banner section
        /*let deleteCheckboxButtonView2 = CheckboxButtonView();
         deleteCheckboxButtonView2.autoresizingMask = [UIViewAutoresizing.flexibleLeftMargin,UIViewAutoresizing.flexibleRightMargin];
         deleteCheckboxButtonView2.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width,height: 20);
         heightToUse += 20;
         heightToUse += padding;
         deleteCheckboxButtonView2.checkboxButton.titleLabel?.font=UIFont(name: "Roboto-Regular", size: 15)
         deleteCheckboxButtonView2.checkboxButton.setTitle("Delete Image", for: UIControlState());
         deleteCheckboxButtonView2.checkboxButton.addTarget(self, action: #selector(EditProfileViewController.deleteButtonPressed(_:)), for: UIControlEvents.touchUpInside);
         deleteCheckboxButtonView2.checkboxButton.tag=33
         deleteCheckboxButtonView2.checkboxButton.setImage(UIImage(named: "unchecked_checkbox"), for: UIControlState());
         cell.mainWrapperView.addSubview(deleteCheckboxButtonView2);
         */
        label_TextView_Combo_View_logo.imgView.isUserInteractionEnabled = true;
        let tapRecognizer2 = UITapGestureRecognizer(target: self, action: #selector(imageTapped(_:)))
        label_TextView_Combo_View_logo.imgView.addGestureRecognizer(tapRecognizer2);
        
        
        label_TextView_Combo_View_banner.imgView.isUserInteractionEnabled = true;
        let tapRecognizer3 = UITapGestureRecognizer(target: self, action: #selector(imageTapped(_:)))
        label_TextView_Combo_View_banner.imgView.addGestureRecognizer(tapRecognizer3);
        
        if(vendorProfileInfo["company_logo"] != "")
        {
            
            var imgRequest2 = URLRequest(url: URL(string: vendorProfileInfo["company_logo"]!)!);
            let requestHeader = Ced_CommonVendor.getInfoPlist("requestheader") as! String
            imgRequest2.setValue(requestHeader, forHTTPHeaderField: "Mobiconnectheader")
            let sessionConfig = URLSessionConfiguration.default
//            sessionConfig.httpAdditionalHeaders = [
//                "Authorization": "Basic \(Ced_CommonVendor.getAuthData())",
//            ]
            sessionConfig.timeoutIntervalForRequest = timeoutIntervalForRequest
            sessionConfig.timeoutIntervalForResource = timeoutIntervalForResource
            let session = URLSession(configuration: sessionConfig)
            let task2 = session.dataTask(with: imgRequest2, completionHandler: {
                // check for fundamental networking error
                data, response, error in
                guard error == nil && data != nil else
                {
                    print("error=\(error)")
                    return;
                }
                
                // check for http errors
                if let httpStatus = response as? HTTPURLResponse, httpStatus.statusCode != 200
                {
                    print("statusCode should be 200, but is \(httpStatus.statusCode)")
                    print("response = \(response)")
                    return;
                }
                
                // Convert the downloaded data in to a UIImage object
                if let data = data{
                    let image = UIImage(data: data)
                    // Store the image in to our cache
                    //self.imageCache[urlString] = image
                    // Update the cell
                    DispatchQueue.main.async
                        {
                            label_TextView_Combo_View_logo.imgView.image = image;
                            
                            
                    }
                }
                
            })
            
            task2.resume();
        }
        
        if(vendorProfileInfo["company_banner"] != "")
        {
            
            var imgRequest3 = URLRequest(url: URL(string: vendorProfileInfo["company_banner"]!)!);
            let requestHeader = Ced_CommonVendor.getInfoPlist("requestheader") as! String
            imgRequest3.setValue(requestHeader, forHTTPHeaderField: "Mobiconnectheader")
            let sessionConfig = URLSessionConfiguration.default
//            sessionConfig.httpAdditionalHeaders = [
//                "Authorization": "Basic \(Ced_CommonVendor.getAuthData())",
//            ]
            sessionConfig.timeoutIntervalForRequest = timeoutIntervalForRequest
            sessionConfig.timeoutIntervalForResource = timeoutIntervalForResource
            let session = URLSession(configuration: sessionConfig)
            let task3 = session.dataTask(with: imgRequest3, completionHandler: {
                // check for fundamental networking error
                data, response, error in
                guard error == nil && data != nil else
                {
                    print("error=\(error)")
                    return;
                }
                
                // check for http errors
                if let httpStatus = response as? HTTPURLResponse, httpStatus.statusCode != 200
                {
                    print("statusCode should be 200, but is \(httpStatus.statusCode)")
                    print("response = \(response)")
                    return;
                }
                
                // Convert the downloaded data in to a UIImage object
                if let data = data{
                    let image = UIImage(data: data)
                    // Store the image in to our cache
                    //self.imageCache[urlString] = image
                    // Update the cell
                    DispatchQueue.main.async
                        {
                            label_TextView_Combo_View_banner.imgView.image = image;
                            
                    }
                }
                
            })
            task3.resume();
        }
        
        /** Company Address **/
        let label_TextView_Combo_View_location = Label_TextView_Combo_View();
        label_TextView_Combo_View_location.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
        label_TextView_Combo_View_location.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width,height: fixedHeight);
        heightToUse += fixedHeight;
        label_TextView_Combo_View_location.entityNameLabel.text = "Company Address";
        label_TextView_Combo_View_location.entityValueTxtField.text = self.vendorProfileInfo["company_address"];
        label_TextView_Combo_View_location.entityValueTxtField.tag = tagCounterToBeUsed;
        tagCounterToBeUsed += 1;
        cell.mainWrapperView.addSubview(label_TextView_Combo_View_location);
        
        /** Support Number **/
        let label_TextView_Combo_View_supportNum = Label_TextView_Combo_View();
        label_TextView_Combo_View_supportNum.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
        label_TextView_Combo_View_supportNum.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width,height: fixedHeight);
        heightToUse += fixedHeight;
        label_TextView_Combo_View_supportNum.entityNameLabel.text = "Support Number";
        label_TextView_Combo_View_supportNum.entityValueTxtField.text = self.vendorProfileInfo["support_number"];
        label_TextView_Combo_View_supportNum.entityValueTxtField.tag = tagCounterToBeUsed;
        label_TextView_Combo_View_supportNum.entityValueTxtField.delegate = self
        tagCounterToBeUsed += 1;
        cell.mainWrapperView.addSubview(label_TextView_Combo_View_supportNum);
        
        /** Support Email **/
        let label_TextView_Combo_View_supportEmail = Label_TextView_Combo_View();
        label_TextView_Combo_View_supportEmail.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
        label_TextView_Combo_View_supportEmail.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width,height: fixedHeight);
        heightToUse += fixedHeight;
        label_TextView_Combo_View_supportEmail.entityNameLabel.text = "Support Email";
        label_TextView_Combo_View_supportEmail.entityValueTxtField.text = self.vendorProfileInfo["support_email"];
        label_TextView_Combo_View_supportEmail.entityValueTxtField.tag = tagCounterToBeUsed;
        label_TextView_Combo_View_supportEmail.entityValueTxtField.delegate
            = self
        tagCounterToBeUsed += 1;
        cell.mainWrapperView.addSubview(label_TextView_Combo_View_supportEmail);
        
        /** Meta Keywords **/
        let label_TextView_Combo_View_MetaKeys = Label_TextView_Combo_View();
        label_TextView_Combo_View_MetaKeys.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
        label_TextView_Combo_View_MetaKeys.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width,height: fixedHeight);
        heightToUse += fixedHeight;
        label_TextView_Combo_View_MetaKeys.entityNameLabel.text = "Meta Keywords";
        label_TextView_Combo_View_MetaKeys.entityValueTxtField.text = self.vendorProfileInfo["meta_keywords"];
        label_TextView_Combo_View_MetaKeys.entityValueTxtField.tag = tagCounterToBeUsed;
        tagCounterToBeUsed += 1;
        cell.mainWrapperView.addSubview(label_TextView_Combo_View_MetaKeys);
        
        /** Meta Description **/
        let label_TextView_Combo_View_MetaDesc = Label_TextView_Combo_View();
        label_TextView_Combo_View_MetaDesc.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
        label_TextView_Combo_View_MetaDesc.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width,height: fixedHeight);
        heightToUse += fixedHeight;
        label_TextView_Combo_View_MetaDesc.entityNameLabel.text = "Meta Description";
        label_TextView_Combo_View_MetaDesc.entityValueTxtField.text = self.vendorProfileInfo["meta_description"];
        label_TextView_Combo_View_MetaDesc.entityValueTxtField.tag = tagCounterToBeUsed;
        tagCounterToBeUsed += 1;
        
        cell.mainWrapperView.addSubview(label_TextView_Combo_View_MetaDesc);
        
        /** Find Us On Facebook **/
        let label_TextView_Combo_View_facebook = Label_TextView_Combo_View();
        label_TextView_Combo_View_facebook.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
        label_TextView_Combo_View_facebook.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width,height: fixedHeight);
        heightToUse += fixedHeight;
        label_TextView_Combo_View_facebook.entityNameLabel.text = "Find Us On Facebook";
        label_TextView_Combo_View_facebook.entityValueTxtField.text = self.vendorProfileInfo["facebook_id"];
        label_TextView_Combo_View_facebook.entityValueTxtField.tag = tagCounterToBeUsed;
        tagCounterToBeUsed += 1;
        cell.mainWrapperView.addSubview(label_TextView_Combo_View_facebook);
        
        /** Find Us On Twitter **/
        let label_TextView_Combo_View_twitter = Label_TextView_Combo_View();
        label_TextView_Combo_View_twitter.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
        label_TextView_Combo_View_twitter.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width,height: fixedHeight);
        heightToUse += fixedHeight;
        label_TextView_Combo_View_twitter.entityNameLabel.text = "Find Us On Twitter";
        label_TextView_Combo_View_twitter.entityValueTxtField.text = self.vendorProfileInfo["twitter_id"];
        label_TextView_Combo_View_twitter.entityValueTxtField.tag = tagCounterToBeUsed;
        tagCounterToBeUsed += 1;
        cell.mainWrapperView.addSubview(label_TextView_Combo_View_twitter);
        
        /** Address **/
        let label_TextView_Combo_View_address = Label_TextView_Combo_View();
        label_TextView_Combo_View_address.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
        label_TextView_Combo_View_address.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width,height: fixedHeight);
        heightToUse += fixedHeight;
        label_TextView_Combo_View_address.entityNameLabel.text = "Address";
        label_TextView_Combo_View_address.entityValueTxtField.text = self.vendorProfileInfo["address"];
        label_TextView_Combo_View_address.entityValueTxtField.tag = tagCounterToBeUsed;
        tagCounterToBeUsed += 1;
        cell.mainWrapperView.addSubview(label_TextView_Combo_View_address);
        
        /** City **/
        /*let label_TextView_Combo_View_city = Label_TextView_Combo_View();
         label_TextView_Combo_View_city.autoresizingMask = [UIViewAutoresizing.flexibleLeftMargin,UIViewAutoresizing.flexibleRightMargin];
         label_TextView_Combo_View_city.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width,height: fixedHeight);
         heightToUse += fixedHeight;
         label_TextView_Combo_View_city.entityNameLabel.text = "City";
         label_TextView_Combo_View_city.entityValueTxtField.text = self.vendorProfileInfo["city"];
         label_TextView_Combo_View_city.entityValueTxtField.tag = tagCounterToBeUsed;
         tagCounterToBeUsed += 1;
         cell.mainWrapperView.addSubview(label_TextView_Combo_View_city);
         */
        let label_TextView_Combo_View_city = Label_DropDown_Combo_View();
        label_TextView_Combo_View_city.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
        label_TextView_Combo_View_city.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width,height: fixedHeight);
        heightToUse += fixedHeight;
        heightToUse += padding;
        label_TextView_Combo_View_city.entityNameLabel.text = "City";
        
        if(self.vendorProfileInfo["city"] != nil || self.vendorProfileInfo["city"] != "")
        {
            label_TextView_Combo_View_city.dropDownButton.setTitle(self.vendorProfileInfo["city"], for: UIControl.State());
        }
        else
        {
            label_TextView_Combo_View_city.dropDownButton.setTitle("", for: UIControl.State());
        }
        label_TextView_Combo_View_city.dropDownButton.addTarget(self, action: #selector(showCityDropdown(_:)), for: UIControl.Event.touchUpInside);
        label_TextView_Combo_View_city.dropDownButton.tag = tagCounterToBeUsed;
        tagCounterToBeUsed += 1;
        cell.mainWrapperView.addSubview(label_TextView_Combo_View_city);
        
        
        /*Location*/
        let label_TextView_Combo_View_loc = Label_DropDown_Combo_View();
        label_TextView_Combo_View_loc.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
        label_TextView_Combo_View_loc.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width,height: fixedHeight);
        heightToUse += fixedHeight;
        heightToUse += padding;
        label_TextView_Combo_View_loc.entityNameLabel.text = "Location";
        
        //if(self.vendorProfileInfo["vendor_location"] != nil || self.vendorProfileInfo["vendor_location"] != "")
        // {
        //label_TextView_Combo_View_loc.dropDownButton.setTitle(self.vendorProfileInfo["vendor_location"], for: UIControlState());
        label_TextView_Combo_View_loc.dropDownButton.setTitle("Please Select", for: UIControl.State());
        /* }
         else
         {
         label_TextView_Combo_View_loc.dropDownButton.setTitle("", for: UIControlState());
         }*/
        label_TextView_Combo_View_loc.dropDownButton.addTarget(self, action: #selector(showLocationDropdown(_:)), for: UIControl.Event.touchUpInside);
        label_TextView_Combo_View_loc.dropDownButton.tag = tagCounterToBeUsed;
        tagCounterToBeUsed += 1;
        cell.mainWrapperView.addSubview(label_TextView_Combo_View_loc);
        
        /** Zip/Postal Code **/
        let label_TextView_Combo_View_zip = Label_TextView_Combo_View();
        label_TextView_Combo_View_zip.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
        label_TextView_Combo_View_zip.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width,height: fixedHeight);
        heightToUse += fixedHeight;
        label_TextView_Combo_View_zip.entityNameLabel.text = "Zip/Postal Code";
        label_TextView_Combo_View_zip.entityValueTxtField.text = self.vendorProfileInfo["zip_code"];
        label_TextView_Combo_View_zip.entityValueTxtField.tag = tagCounterToBeUsed;
        label_TextView_Combo_View_zip.entityValueTxtField.delegate = self
        tagCounterToBeUsed += 1;
        cell.mainWrapperView.addSubview(label_TextView_Combo_View_zip);
        
        /** Complete Location **/
        let label_TextView_Combo_View_completeLocation = Label_TextView_Combo_View();
        label_TextView_Combo_View_completeLocation.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
        label_TextView_Combo_View_completeLocation.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width,height: fixedHeight);
        heightToUse += fixedHeight;
        label_TextView_Combo_View_completeLocation.entityNameLabel.text = "Complete Location";
        label_TextView_Combo_View_completeLocation.entityValueTxtField.text = self.vendorProfileInfo["vendor_complete_location"];
        label_TextView_Combo_View_completeLocation.entityValueTxtField.tag = tagCounterToBeUsed;
        tagCounterToBeUsed += 1;
        cell.mainWrapperView.addSubview(label_TextView_Combo_View_completeLocation);
        
        /** Latitude **/
        let label_TextView_Combo_View_latitude = Label_TextView_Combo_View();
        label_TextView_Combo_View_latitude.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
        label_TextView_Combo_View_latitude.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width,height: fixedHeight);
        heightToUse += fixedHeight;
        label_TextView_Combo_View_latitude.entityNameLabel.text = "Latitude";
        label_TextView_Combo_View_latitude.entityValueTxtField.text = self.vendorProfileInfo["vendor_latitude"];
        label_TextView_Combo_View_latitude.entityValueTxtField.tag = tagCounterToBeUsed;
        tagCounterToBeUsed += 1;
        cell.mainWrapperView.addSubview(label_TextView_Combo_View_latitude);
        
        
        /** Longitude **/
        let label_TextView_Combo_View_longitude = Label_TextView_Combo_View();
        label_TextView_Combo_View_longitude.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
        label_TextView_Combo_View_longitude.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width,height: fixedHeight);
        heightToUse += fixedHeight;
        label_TextView_Combo_View_longitude.entityNameLabel.text = "Longitude";
        label_TextView_Combo_View_longitude.entityValueTxtField.text = self.vendorProfileInfo["vendor_longitude"];
        label_TextView_Combo_View_longitude.entityValueTxtField.tag = tagCounterToBeUsed;
        tagCounterToBeUsed += 1;
        cell.mainWrapperView.addSubview(label_TextView_Combo_View_longitude);
        
        /** Country **/
        let label_TextView_Combo_View_country = Label_DropDown_Combo_View();
        label_TextView_Combo_View_country.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
        label_TextView_Combo_View_country.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width,height: fixedHeight);
        heightToUse += fixedHeight;
        heightToUse += padding;
        label_TextView_Combo_View_country.entityNameLabel.text = "Country";
        
        if(self.vendorProfileInfo["country_id"] != nil || self.vendorProfileInfo["country_id"] != "")
        {
            label_TextView_Combo_View_country.dropDownButton.setTitle(self.vendorProfileInfo["country_id"], for: UIControl.State());
        }
        else
        {
            label_TextView_Combo_View_country.dropDownButton.setTitle("", for: UIControl.State());
        }
        label_TextView_Combo_View_country.dropDownButton.addTarget(self, action: #selector(showCountryDropdown(_:)), for: UIControl.Event.touchUpInside);
        label_TextView_Combo_View_country.dropDownButton.tag = tagCounterToBeUsed;
        tagCounterToBeUsed += 1;
        cell.mainWrapperView.addSubview(label_TextView_Combo_View_country);
        
        /** State **/
        let label_TextView_Combo_View_state = NewStatePickerView();
        label_TextView_Combo_View_state.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
        label_TextView_Combo_View_state.frame = CGRect(x: 0, y: heightToUse, width: screenSize.width,height: fixedHeight);
        heightToUse += fixedHeight;
        heightToUse += padding;
        label_TextView_Combo_View_state.entityNameLabel.text = "State";
        label_TextView_Combo_View_state.textValue.text = self.vendorProfileInfo["region"];
        label_TextView_Combo_View_state.textValue.tag = tagCounterToBeUsed;
        label_TextView_Combo_View_state.tag = tagCounterToBeUsed;
        self.stateSelectionViewTag = tagCounterToBeUsed;
        self.stateDropDownIsVisible = false;
        label_TextView_Combo_View_state.dropDownButton.isHidden = true;
        
        label_TextView_Combo_View_state.dropDownButton.addTarget(self, action: #selector(showStatesDropdown(_:)), for: UIControl.Event.touchUpInside);
        
        tagCounterToBeUsed += 1;
        cell.mainWrapperView.addSubview(label_TextView_Combo_View_state);
        
        return cell;
    }
    
    @objc func imageTapped(_ gestureRecognizer: UITapGestureRecognizer)
    {
        /* background view */
        let bgCView = UIView();
        bgCView.tag=121;
        bgCView.frame = UIScreen.main.bounds;
        bgCView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        bgCView.backgroundColor = UIColor(red:0, green:0, blue:0, alpha:0.5);
        
        self.addgesturesTohideView(bgCView);
        
        let imgPopupView = UIImageView();
        imgPopupView.tag = 131;
        imgPopupView.autoresizingMask = [UIView.AutoresizingMask.flexibleLeftMargin,UIView.AutoresizingMask.flexibleRightMargin];
        imgPopupView.frame = CGRect(x: 0, y: CGFloat(0), width: screenSize.width/2,height: screenSize.width/2);//screenSize.height/2);
        imgPopupView.center = CGPoint(x: bgCView.frame.size.width  / 2, y: bgCView.frame.size.height / 2);
        let tappedImageView = gestureRecognizer.view as! UIImageView;
        imgPopupView.image = tappedImageView.image;
        
        bgCView.addSubview(imgPopupView);
        UIApplication.shared.keyWindow?.addSubview(bgCView);
    }
    @objc func addgesturesTohideView(_ v:UIView)
    {
        //code to add gestures to dismiss the popover
        let tapGesture = UITapGestureRecognizer (target: self, action:#selector(hideView(_:)))
        v.addGestureRecognizer(tapGesture);
        tapGesture.delegate=self;
        
        let upSwipe = UISwipeGestureRecognizer(target: self, action: #selector(hideView(_:)));
        upSwipe.direction = UISwipeGestureRecognizer.Direction.up;
        v.addGestureRecognizer(upSwipe)
        upSwipe.delegate=self;
        
        let downSwipe = UISwipeGestureRecognizer(target: self, action: #selector(hideView(_:)));
        downSwipe.direction = UISwipeGestureRecognizer.Direction.down;
        v.addGestureRecognizer(downSwipe)
        downSwipe.delegate=self;
        
        
        let rightSwipe = UISwipeGestureRecognizer(target: self, action: #selector(hideView(_:)));
        rightSwipe.direction = UISwipeGestureRecognizer.Direction.right;
        v.addGestureRecognizer(rightSwipe)
        rightSwipe.delegate=self;
        
        let leftSwipe = UISwipeGestureRecognizer(target: self, action: #selector(hideView(_:)));
        leftSwipe.direction = UISwipeGestureRecognizer.Direction.left;
        v.addGestureRecognizer(leftSwipe)
        leftSwipe.delegate=self;
    }
    
    @objc func deleteButtonPressed(_ sender:UIButton)
    {
        print("deleteButtonPressed called");
        print(sender.tag)
        if(sender.imageView!.image == UIImage(named:"unchecked_checkbox"))
        {
            sender.setImage(UIImage(named: "checked_checkbox"), for: UIControl.State());
            if let img = self.view.viewWithTag(sender.tag) as? UIImageView
            {
                img.image = UIImage(named: "profle");
            }
        }
        else
        {
            sender.setImage(UIImage(named: "unchecked_checkbox"), for: UIControl.State());
        }
    }
    
    //@objc function to dismiss the custom popover
    @objc func hideView(_ recognizer: UITapGestureRecognizer)
    {
        //self.view.viewWithTag(121)?.removeFromSuperview();
        UIApplication.shared.keyWindow?.viewWithTag(121)?.removeFromSuperview();
    }
    
    //delegate @objc function to handle touch events on the custom popover
    @objc func gestureRecognizer(_ gestureRecognizer: UIGestureRecognizer, shouldReceive touch: UITouch) -> Bool
    {
        if let imgPopupView = UIApplication.shared.keyWindow?.viewWithTag(131) as? UIImageView {
            if(touch.view!.isDescendant(of: imgPopupView))
            {
                return false;
            }
            return true;
        }
        return true;
    }
    
    @objc func showChangePasswordView(_ sender:UIButton)
    {
        print("showChangePasswordView called");
        
        if(sender.imageView!.image == UIImage(named:"unchecked_checkbox"))
        {
            sender.setImage(UIImage(named: "checked_checkbox"), for: UIControl.State());
        }
        else
        {
            sender.setImage(UIImage(named: "unchecked_checkbox"), for: UIControl.State());
        }
        
        self.heightToUse1 = 0;
        self.showPasswordFields = !showPasswordFields;
        self.editProfileTableView.beginUpdates();
        
        let indexPath = IndexPath(row: 1, section: 0);
        let cell = editProfileTableView.cellForRow(at: indexPath);
        if let cellToUse = cell as? LongSingleTableViewCell
        {
            cellToUse.dataLoaded = false;
        }
        self.editProfileTableView.reloadRows(at: [indexPath], with: UITableView.RowAnimation.fade);
        self.editProfileTableView.endUpdates();
        
    }
    
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat
    {
        if(indexPath.row == 1)
        {
            return heightToUse1;
        }
        return heightToUse;
    }
    
    
    @objc func showGenderDropdown(_ sender:UIButton)
    {
        let dropDown = DropDown();
        let dataSource = ["Female","Male"];
        
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
    @objc func citylisting()
    {
        var baseUrl = settings.baseUrl
        let url = "vproductfilterapi/index/configval"
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
        let task =  session.dataTask(with: request, completionHandler: {
            data,response,error in
            DispatchQueue.main.async
                {
                    //Alert_File.removeLoadingIndicator(self)
            }
            guard error == nil && data != nil else
            {
                DispatchQueue.main.async
                    {
                        
                        // Alert_File.removeLoadingIndicator(self)
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
                        
                        //Alert_File.removeLoadingIndicator(self)
                        print("statusCode should be 200, but is \(httpStatus.statusCode)")
                        print("response = \(response)")
                        print(baseUrl)
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
            
            DispatchQueue.main.async {
                if let data = data{
                    guard let json = try? JSON(data: data) else {return}
                    print("jsionb g gsjisn=\(json)")
                    if json["data"]["success"].stringValue == "true"
                    {
                        let cityListing = json["data"]["city"].arrayValue;
                        for d in 0..<cityListing.count
                        {
                            let key = cityListing[d]["city_id"].stringValue;
                            
                            let value = cityListing[d]["city_name"].stringValue;
                            
                            self.cityArray[value] = key;
                        }
                        
                    }
                }
                self.editProfileTableView.reloadData()
            }
        })
        task.resume()
        
    }
    @objc func showCityDropdown(_ sender:UIButton)
    {
        print("keopl")
        print(cityArray)
        let dropDown = DropDown();
        var dropDownToShow = Array(self.cityArray.keys);
        dropDownToShow = dropDownToShow.sorted();
        if let indexToRemove = dropDownToShow.index(of: "--Please Select--") {
            dropDownToShow.remove(at: indexToRemove);
        }
        dropDown.dataSource = dropDownToShow;
        dropDown.selectionAction = {(index, item) in
            let keyToUse = self.cityArray[item]!;
            sender.setTitle(item, for: UIControl.State());
            //            self.fetchlocationInsideCity(keyToUse);
        }
        dropDown.anchorView = sender
        dropDown.bottomOffset = CGPoint(x: 0, y:sender.bounds.height)
        
        if dropDown.isHidden {
            dropDown.show();
        } else {
            dropDown.hide();
        }
    }
    @objc func showLocationDropdown(_ sender:UIButton)
    {
        let dropDown = DropDown();
        var dropDownToShow = Array(self.locationArray.keys);
        dropDownToShow = dropDownToShow.sorted();
        /*if let indexToRemove = dropDownToShow.index(of: "--Please Select--") {
         dropDownToShow.remove(at: indexToRemove);
         }*/
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
    
    @objc func showCountryDropdown(_ sender:UIButton)
    {
        print("DEVENDRARARR")
        let dropDown = DropDown();
        var dropDownToShow = Array(self.countryArray.keys);
        dropDownToShow = dropDownToShow.sorted();
        if let indexToRemove = dropDownToShow.index(of: "--Please Select--") {
            dropDownToShow.remove(at: indexToRemove);
        }
        dropDown.dataSource = dropDownToShow;
        dropDown.selectionAction = {(index, item) in
            let keyToUse = self.countryArray[item]!;
            sender.setTitle(item, for: UIControl.State());
            self.fetchStatesInsideCountry(keyToUse);
        }
        dropDown.anchorView = sender
        dropDown.bottomOffset = CGPoint(x: 0, y:sender.bounds.height)
        
        if dropDown.isHidden {
            dropDown.show();
        } else {
            dropDown.hide();
        }
        
        
    }
    
    @objc func showStatesDropdown(_ sender:UIButton)
    {
        let dropDown = DropDown();
        var dropDownToShow = Array(statesArray.keys);
        dropDownToShow = dropDownToShow.sorted();
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
    
    
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        if let data = data{
            if requestUrl=="vendorapi/index/getcountry/"
            {
               guard let json = try? JSON(data: data) else {return}
                print("jsionsjisn=\(json)")
                Alert_File.removeLoadingIndicator(self);
                let countryListing = json["country"].arrayValue;
                for d in 0..<countryListing.count
                {
                    let key = countryListing[d]["value"].stringValue;
                    
                    let value = countryListing[d]["label"].stringValue;
                    
                    self.countryArray[value] = key;
                    //print("countryArray");
                }
                print("defrfw34r342")
                //                self.citylisting()
                //self.editProfileTableView.reloadData();
            }
            else if requestUrl=="vendorapi/index/update"
            {
                
                let jsonData=try! JSON(data: data)
                print("jsdfcrionsjisn=\(jsonData)")
                Alert_File.removeLoadingIndicator(self);
                print(jsonData);
                if(jsonData["data"]["success"].stringValue != "true")
                {
                    print("EROROROROR");
                    //return;
                }
                else if(jsonData["data"]["success"].stringValue == "true")
                {
                    self.view.showToastMsg(jsonData["data"]["message"].stringValue)
                    self.goToProfilePageAgain();
                    print("EROROROROR");
                    //return;
                }
            }
        }
        
    }
    @objc func fetchCountryListing()
    {
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        
        //baseURL = Ced_CommonVendor.getInfoPlist("baseUrl") as! String
        baseURL = "vendorapi/index/getcountry/";
        
        print(baseURL);
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        //var request = URLRequest(url: URL(string: baseURL)!);
        
        //request.httpMethod = "POST";
        let postString = "vendor_id="+vendorId+"&hashkey="+hashKey;
        self.sendRequest(url: baseURL, parameters: postString)
    }
    
    @objc func fetchlocationInsideCity(_ item:String)
    {
        print("vfsfedr")
        var baseUrl = settings.baseUrl
        let url = "vproductfilterapi/index/configval"
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
        let task =  session.dataTask(with: request, completionHandler: {
            data,response,error in
            DispatchQueue.main.async
                {
                    //Alert_File.removeLoadingIndicator(self)
            }
            guard error == nil && data != nil else
            {
                DispatchQueue.main.async
                    {
                        // Alert_File.removeLoadingIndicator(self)
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
                        
                        //Alert_File.removeLoadingIndicator(self)
                        print("statusCode should be 200, but is \(httpStatus.statusCode)")
                        print("response = \(response)")
                        print(baseUrl)
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
            
            DispatchQueue.main.async {
                if let data = data{
                    guard let json = try? JSON(data: data) else {return}
                    if json["data"]["success"].stringValue == "true"
                    {
                        self.locationArray=[:]
                        
                        let locListing = json["data"]["location"].arrayValue;
                        for d in 0..<locListing.count
                        {
                            let key = locListing[d]["city_id"].stringValue;
                            if key == item
                            {
                                let value = locListing[d]["city_location"].stringValue;
                                self.locationArray[value] = key;
                            }
                        }
                    }
                }
            }
            
        })
        task.resume()
    }
    
    
    
    @objc func fetchStatesInsideCountry(_ item:String)
    {
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        
        baseURL = settings.baseUrl
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
                        let jsonData = try! JSON(data: data);
                        print("jsonData[\"data\"]DDDEEEVVV");
                        print(jsonData);
                        
                        if(jsonData["success"].stringValue == "false")
                        {
                            print("Show TEXTBOX");
                            print(self.stateSelectionViewTag);
                            print(self.view.viewWithTag(self.stateSelectionViewTag));
                            if let newStatePickerView = self.view.viewWithTag(self.stateSelectionViewTag) as? NewStatePickerView
                            {
                                print("jkdfgdjksfgkdjsfg")
                                self.stateDropDownIsVisible = false;
                                newStatePickerView.dropDownButton.isHidden = true;
                                newStatePickerView.textValue.isHidden = false;
                            }
                        }
                        else
                        {
                            print("Show DropDown");
                            if let newStatePickerView = self.view.viewWithTag(self.stateSelectionViewTag) as? NewStatePickerView
                            {
                                self.stateDropDownIsVisible = true;
                                newStatePickerView.dropDownButton.isHidden = false;
                                newStatePickerView.textValue.isHidden = true;
                            }
                            
                            self.statesArray = [String:String]();
                            for (_,val) in jsonData["states"]
                            {
                                self.statesArray[val["name"].stringValue] = val["region_id"].stringValue;
                            }
                            
                            print("self.statesArray");
                            print(self.statesArray);
                        }
                    }
            }
        })
        
        task.resume();
        
    }
    
    
    @objc func sendSaveDetailsRequest(_ sender:UIButton)
    {
        
        
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary;
        let vendor_id = userData["vendorId"] as! String;
        let hashkey    = userData["hashKey"] as! String;
        
        postString += "vendor_id="+vendor_id+"&hashkey="+hashkey;
        
        
        print("sendSaveDetailsRequest");
        print(tagCounterToBeUsed);
        var keys = ["public_name", "name", "gender", "email", "contact_number", "company_name", "about", "company_address", "support_number", "support_email","meta_keywords", "meta_description", "facebook_id", "twitter_id", "address","city","location", "zip_code","complete_location","latitude","longitude", "country_id", "region", ];
        
        
        //var tagCounter = 50;
        var keyCounter = 0;
        for tagCounter in 50..<tagCounterToBeUsed
        {
            print("INSIDE LOOP");
            
            if(tagCounter == self.stateSelectionViewTag)
            {
                if let newStatePickerView = self.view.viewWithTag(self.stateSelectionViewTag) as? NewStatePickerView
                {
                    print("@#@#@");
                    print(self.stateDropDownIsVisible);
                    if(self.stateDropDownIsVisible == false)
                    {
                        print("@#@#@2222222");
                        var txt = "";
                        print("newStatePickerView.textValue.text");
                        print(newStatePickerView.textValue.text);
                        if(newStatePickerView.textValue.text != nil)
                        {
                            txt = newStatePickerView.textValue.text;
                        }
                        
                        postString += "&"+"region"+"="+txt;
                        postString += "&"+"region_id"+"="+"";
                        
                    }
                    else
                    {
                        var txt = "";
                        if(newStatePickerView.dropDownButton.titleLabel!.text != nil)
                        {
                            txt = newStatePickerView.dropDownButton.titleLabel!.text!;
                            txt = self.statesArray[txt]!;
                        }
                        
                        postString += "&"+"region"+"="+"";
                        postString += "&"+"region_id"+"="+txt;
                    }
                    
                }
                
                keyCounter += 1;
                continue;
            }
            
            if let label_TextView_Combo_View_represent = self.view.viewWithTag(tagCounter) as? UITextView
            {
                postString += "&"+keys[keyCounter]+"="+label_TextView_Combo_View_represent.text!;
            }
            else if let label_TextView_Combo_View_gender = self.view.viewWithTag(tagCounter) as? UIButton
            {
                if(label_TextView_Combo_View_gender.titleLabel!.text != nil)
                {
                    if(keys[keyCounter] == "gender")
                    {
                        let genderVal = label_TextView_Combo_View_gender.titleLabel?.text!;
                        var gender_id = "2";
                        if(genderVal?.lowercased() == "male")
                        {
                            gender_id = "1";
                        }
                        postString += "&"+keys[keyCounter]+"="+gender_id;
                    }
                    else if(keys[keyCounter] == "country_id")
                    {
                        // if key is country_id add it to poststring
                        let countryName = label_TextView_Combo_View_gender.titleLabel?.text!;
                        let country_id = self.countryArray[countryName!]!;
                        postString += "&"+keys[keyCounter]+"="+country_id;
                    }
                    else
                    {
                        postString += "&"+keys[keyCounter]+"="+(label_TextView_Combo_View_gender.titleLabel?.text!)!;
                    }
                }
            }
            keyCounter += 1;
        }
        
        if(showPasswordFields)
        {
            var currentpassword = "";
            if let label_TextView_Combo_View_represent = self.view.viewWithTag(91) as? UITextView
            {
                currentpassword = label_TextView_Combo_View_represent.text!;
            }
            var newpassword = "";
            if let label_TextView_Combo_View_represent = self.view.viewWithTag(92) as? UITextView
            {
                newpassword = label_TextView_Combo_View_represent.text!;
            }
            
            postString += "&currentpassword="+currentpassword;
            postString += "&newpassword="+newpassword;
        }
        
        print(postString);
        
        
        print("fetchVendorShopListingData");
        //baseURL = Ced_CommonVendor.getInfoPlist("baseUrl") as! String;
        
        doImageUpload()
        
    }
    
    
    @objc func doImageUpload()
    {
        print("imageLoop----\(imgLoop)")
        print("images-------\(imagesToUpload)");
        if(imgLoop <= 33)
        {
            /*if let img = imagesToUpload.index(of: imgLoop) {
             
             if let imgView = self.view.viewWithTag(imagesToUpload[img]) as? UIImageView
             {
             print("doImgUploadActivity");
             myImageUploadRequest(imgView);
             imgLoop += 1;
             }
             
             }
             else
             {
             print("ITTTSSSS FINNAL");
             self.goToProfilePageAgain();
             }*/
            var keys = ["profile_picture","company_logo","company_banner"]
            for i in 0..<imagesToUpload.count{
                if let imgView = self.view.viewWithTag(imagesToUpload[i]) as? UIImageView
                {
                    print("doImgUploadActivity");
                    print(imagesToUpload.count)
                    for x in 31..<34
                    {
                        if(x == imagesToUpload[i])
                        {
                            
                            imgLoop=imagesToUpload[i];
                            let tempData=imgView.image!.jpegData(compressionQuality: 0.5)!
                            print(tempData)
                            let chat_file=(tempData.base64EncodedString())
                            let dictionary = ["name":"124\(imgLoop).jpg","base64_encoded_data":chat_file]
                            let convertedstring=convertDicTostring(str: dictionary)
                            print(keys[i])
                            if(imgLoop == 31)
                            {
                                postString += "&"+keys[0]+"="+convertedstring as String
                            }
                            else if(imgLoop == 32)
                            {
                                postString += "&"+keys[1]+"="+convertedstring as String
                            }
                            else
                            {
                                postString += "&"+keys[2]+"="+convertedstring as String
                            }
                            
                        }
                    }
                    
                    //  postS1tring += "&"+convertedstring as String
                    //  myImageUploadRequest(imgView);
                }
            }
            Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
            print(postString)
            baseURL = "vendorapi/index/update";
            self.sendRequest(url: baseURL, parameters: postString)
            //  self.goToProfilePageAgain();
        }
        else
        {
            print("ITTTSSSS FINNAL");
            self.goToProfilePageAgain();
        }
    }
    
    
    @objc func textViewDidEndEditing(_ textView: UITextView) {
        print(textView.tag)
        if textView.tag == 67
        {
            if !self.isValidNumber(Number: textView.text!)
            {
                textView.text = ""
                self.view.showToastMsg("Please enter correct Pin Code.")
                return
            }
            else if textView.text.count < 6
            {
                textView.text = ""
                self.view.showToastMsg("Please enter correct Pin Code.")
                return
            }
        }
        else if textView.tag == 59
        {
            if !self.isValidEmail(testStr: textView.text!)
            {
                self.view.showToastMsg("Invalid Email".localized)
                return
            }
        }
        else
        {
            if !self.isValidNumber(Number: textView.text!)
            {
                textView.text = ""
                self.view.showToastMsg("Please enter correct Number.")
                return
            }
        }
    }
    
    @objc func goToProfilePageAgain()
    {
        self.view.showToastMsg("Profile Updated Successfully.")
        let viewcontrollor = self.storyboard?.instantiateViewController(withIdentifier: "vendorProfileViewController") as! VendorProfileViewController;
        self.navigationController?.pushViewController(viewcontrollor, animated: true)
    }
    
    ///// IMAGE UPLOAD CODE //////
    
    //@objc function to upload the profile image to server
    @objc func myImageUploadRequest(_ imgView:UIImageView)
    {
        //        print("imgView.image");
        //        print(imgView.image);
        //
        //        if(imgView.image == nil)
        //        {
        //            return;
        //        }
        //
        //        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary;
        //        let vendor_id = userData["vendorId"] as! String;
        //        //let hashkey    = userData["hashKey"] as! String;
        //
        //        baseURL = Ced_CommonVendor.getInfoPlist("baseUrl") as! String;
        //
        //        baseURL += "vendorapi/index/update";
        //        print(baseURL);
        //
        //        let myUrl = URL(string: baseURL);
        //
        //        var request = URLRequest(url:myUrl!);
        //        request.httpMethod = "POST";
        //        let requestHeader = Ced_CommonVendor.getInfoPlist("requestheader") as! String
        //        request.setValue(requestHeader, forHTTPHeaderField: "Mobiconnectheader")
        //        let boundary = generateBoundaryString()
        //
        //        request.setValue("multipart/form-data; boundary=\(boundary)",forHTTPHeaderField: "Content-Type")
        //        request.setValue(vendor_id, forHTTPHeaderField: "Authorization");
        
        
        
        
        
        //        let initialSize:CGSize=imgView.image!.size;
        //
        //        var imageData=Data();
        //
        //        if(initialSize.height > 200 || initialSize.width > 200)
        //        {
        //            print("above");
        //
        //            let newSize:CGSize = CGSize(width: 250, height: 250);
        //            let rect = CGRect(x: 0, y: 0, width: newSize.width, height: newSize.height)
        //            UIGraphicsBeginImageContextWithOptions(newSize, false, 1.0)
        //            imgView.image?.draw(in: rect);
        //            let newImage = UIGraphicsGetImageFromCurrentImageContext()
        //            UIGraphicsEndImageContext()
        //
        //            imageData = UIImageJPEGRepresentation(newImage!, 0.5)!
        //        }
        //        else
        //        {
        //            print("below");
        //            imageData = UIImageJPEGRepresentation(imgView.image!, 0.5)!
        //        }
        
        if(imgLoop == 31)
        {
            image1=true
            let tempData=imgView.image!.jpegData(compressionQuality: 0.5)!
            print(tempData)
            let chat_file=(tempData.base64EncodedString())
            let dictionary = ["name":"1245454545.jpg","base64_encoded_data":chat_file]
            let convertedstring=convertDicTostring(str: dictionary)
            postString += "&profile_picture="+convertedstring as String
            
            //            print("Uploading For");
            //            print("profile_picture");
            //            request.httpBody = createBodyWithParameters(nil, filePathKey: "profile_picture", imageDataKey: imageData, boundary: boundary)
        }
        else if(imgLoop == 33)
        {
            image2=true
            let tempData=imgView.image!.jpegData(compressionQuality: 0.5)!
            print(tempData)
            let chat_file=(tempData.base64EncodedString())
            let dictionary = ["name":"1245454545.jpg","base64_encoded_data":chat_file]
            let convertedstring=convertDicTostring(str: dictionary)
            postString += "&company_banner="+convertedstring as String
            print("Uploading For");
            print("company_banner");
            
            //  request.httpBody = createBodyWithParameters(nil, filePathKey: "company_banner", imageDataKey: imageData, boundary: boundary)
        }
        else if(imgLoop == 32)
        {
            image3=true
            let tempData=imgView.image!.jpegData(compressionQuality: 0.5)!
            print(tempData)
            let chat_file=(tempData.base64EncodedString())
            let dictionary = ["name":"1245454545.jpg","base64_encoded_data":chat_file]
            let convertedstring=convertDicTostring(str: dictionary)
            postString += "&company_logo="+convertedstring as String
            print("Uploading For");
            print("company_logo");
            
            //request.httpBody = createBodyWithParameters(nil, filePathKey: "company_logo", imageDataKey: imageData, boundary: boundary)
        }
        
        
        //
        //        if image1==true && image2 == true && image3 == true
        //        {
        //            Alert_File.addLoadingIndicator(self, msg: "Please Wait...")
        //            baseURL = "vendorapi/index/update";
        //            print(postString);
        //            self.sendRequest(url: baseURL, parameters: postString)
        //        }
        
        
        //        Alert_File.addLoadingIndicator(self, msg: "Please Wait...")
        //        let task = URLSession.shared.dataTask(with: request, completionHandler: {
        //            // check for fundamental networking error
        //            data, response, error in
        //            guard error == nil && data != nil else
        //            {
        //                print("error=\(error)")
        //                return;
        //            }
        //
        //            // check for http errors
        //            if let httpStatus = response as? HTTPURLResponse, httpStatus.statusCode != 200
        //            {
        //                print("statusCode should be 200, but is \(httpStatus.statusCode)")
        //                print("response = \(response)")
        //                return;
        //            }
        //
        //            // code to fetch values from response :: start
        //            let jsonData = JSON(data: data!);
        //            DispatchQueue.main.async
        //                {
        //                    Alert_File.removeLoadingIndicator(self);
        //                    print(jsonData);
        //                    print("UPLOADEDDDD");
        //                    /*if(self.imgLoop <= 33)
        //                     {
        //                     self.doImageUpload();
        //                     }
        //                     else
        //                     {
        //                     print("DONENNENE");
        //                     self.goToProfilePageAgain();
        //                     }*/
        //            }
        //        })
        //
        //        task.resume();
        
    }//end of @objc function to upload the profile image to server
    
    //@objc functions for making image uplaod request
    @objc func createBodyWithParameters(_ parameters: [String: String]?, filePathKey: String?, imageDataKey: Data, boundary: String) -> Data {
        let body = NSMutableData();
        
        if parameters != nil {
            for (key, value) in parameters! {
                body.appendString("--\(boundary)\r\n")
                body.appendString("Content-Disposition: form-data; name=\"\(key)\"\r\n\r\n")
                body.appendString("\(value)\r\n")
            }
        }
        
        let aRandomInt = arc4random_uniform(499)+1  // returns a random number within the given range.
        let filename = "profile_Pic"+aRandomInt.description+".jpg";
        
        let mimetype = "image/jpg";
        
        body.appendString("--\(boundary)\r\n")
        body.appendString("Content-Disposition: form-data; name=\"\(filePathKey!)\"; filename=\"\(filename)\"\r\n")
        body.appendString("Content-Type: \(mimetype)\r\n\r\n")
        body.append(imageDataKey)
        body.appendString("\r\n")
        
        body.appendString("--\(boundary)--\r\n")
        
        return body as Data
    }
    
    
    @objc func generateBoundaryString() -> String
    {
        return "Boundary-\(UUID().uuidString)"
    }
    //end of @objc functions for making image uplaod request
    
}



/**
 ** Extensions
 **/

//some code to change the image name
extension NSMutableData {
    
    @objc func appendString(_ string: String) {
        let data = string.data(using: String.Encoding.utf8, allowLossyConversion: true)
        append(data!)
    }
}

extension Int
{
    static func random(_ range: Range<Int> ) -> Int
    {
        var offset = 0
        
        if range.lowerBound < 0   // allow negative ranges
        {
            offset = abs(range.lowerBound)
        }
        
        let mini = UInt32(range.lowerBound + offset)
        let maxi = UInt32(range.upperBound   + offset)
        
        return Int(mini + arc4random_uniform(maxi - mini)) - offset
    }
}
//end of code


// Helper function inserted by Swift 4.2 migrator.
fileprivate func convertFromUIImagePickerControllerInfoKeyDictionary(_ input: [UIImagePickerController.InfoKey: Any]) -> [String: Any] {
	return Dictionary(uniqueKeysWithValues: input.map {key, value in (key.rawValue, value)})
}

// Helper function inserted by Swift 4.2 migrator.
fileprivate func convertFromUIImagePickerControllerInfoKey(_ input: UIImagePickerController.InfoKey) -> String {
	return input.rawValue
}
