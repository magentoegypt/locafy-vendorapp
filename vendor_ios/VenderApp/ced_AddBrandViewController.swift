//
//  ced_AddBrandViewController.swift
//  VenderApp
//
//  Created by cedcoss on 24/02/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_AddBrandViewController: ced_VendorBaseClass, UIImagePickerControllerDelegate, UINavigationControllerDelegate {
    
    
    @IBOutlet weak var brandName: UITextField!
    @IBOutlet weak var mainTitle: UILabel!
    
    @IBOutlet weak var save: UIButton!
    
    var vc = UIViewController()
    @IBOutlet weak var viewWidth: NSLayoutConstraint!
    @IBOutlet weak var dropView: Label_DropDown_Combo_View!
    
    @IBOutlet weak var brandLogo: browseImageView!
    
    @IBOutlet weak var urlKey: UITextField!
    
    @IBOutlet weak var describtion: UITextView!
    
    @IBOutlet weak var thumbnail: browseImageView!
    let picker = UIImagePickerController()
    var selectedBrowseView=browseImageView()
    var backData = false
    var id = ""
    var temp = [String:String]()
    var baseURL = String();
    var brandGroup = [String:String]();
    override func viewDidLoad() {
        super.viewDidLoad()

        viewWidth.constant = self.view.frame.width
        dropView.entityNameLabel.text = "Brand Group"
        dropView.dropDownButton.addTarget(self, action: #selector(showbrandGroupDropdown(_:)), for: .touchUpInside)
        
         brandLogo.browseButton.addTarget(self, action: #selector(self.browseButtonPressed(sender: )), for: .touchUpInside)
        brandLogo.fileAttachedLabel.isHidden = true;
        brandLogo.nameLabel.isHidden = true;
        thumbnail.fileAttachedLabel.isHidden = true;
        thumbnail.nameLabel.isHidden = true;
         thumbnail.browseButton.addTarget(self, action: #selector(self.browseButtonPressed(sender: )), for: .touchUpInside)
        requestData()
        
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as! String
        save.layer.cornerRadius=2
        let color = Ced_CommonVendor.UIColorFromRGB("#2abb9c");
        //save.backgroundColor = color;
        save.setThemeColor();
        save.tintColor = UIColor.white
        save.setTitle("Save".localized, for: UIControl.State())
        save.addTarget(self, action: #selector(sendClicked(_:)), for: .touchUpInside)
        mainTitle.backgroundColor =  Ced_CommonVendor.UIColorFromRGB(colorString)
        mainTitle.textColor = UIColor.white
        describtion.layer.borderWidth = 1
        describtion.layer.cornerRadius = 5
        
    }

 
    @objc func requestData()
    {
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        baseURL = "/vendorapi/brand/getgroup"
        let postString = "vendor_id="+vendorId+"&hashkey="+hashKey
        self.sendRequest(url: baseURL, parameters: postString)
    }
    @objc func image_picker()
    {
        picker.delegate=self
        picker.allowsEditing = false
        picker.sourceType = .photoLibrary
        picker.modalPresentationStyle = .fullScreen
        self.present(picker, animated: true, completion: nil)
    }
    
    @objc func browseButtonPressed(sender:UIButton)
    {
        if let browseView=sender.superview?.superview as? browseImageView{
            selectedBrowseView=browseView
            let actionSheetControllerIOS8: UIAlertController = UIAlertController(title: "Please select", message: "Option to select", preferredStyle: .actionSheet)
            
            let cancelActionButton = UIAlertAction(title: "Cancel", style: .cancel) { _ in
                print("Cancel")
            }
            actionSheetControllerIOS8.addAction(cancelActionButton)
            
            let saveActionButton = UIAlertAction(title: "upload image", style: .default)
            { _ in
                self.image_picker()
            }
            actionSheetControllerIOS8.addAction(saveActionButton)
            self.present(actionSheetControllerIOS8, animated: true, completion: nil)
        }
    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @objc func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
        picker.dismiss(animated: true, completion: nil)
    }
    
    internal func imagePickerController(_ picker: UIImagePickerController,
                                        didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any])
    {
// Local variable inserted by Swift 4.2 migrator.
let info = convertFromUIImagePickerControllerInfoKeyDictionary(info)

        let chosenImage = info[convertFromUIImagePickerControllerInfoKey(UIImagePickerController.InfoKey.originalImage)] as! UIImage
        selectedBrowseView.browseImage.image=chosenImage
        print("didFinishPickingMediaWithInfo")
        picker.dismiss(animated:true, completion: nil)
    }
    
    
    @objc func sendClicked(_ sender:UIButton)
    {
        //var des
        if (brandName.text == "") || (urlKey.text == "")
        {
            
            self.view.showToastMsg(" All fields are Required.".localized);
        }
        else
        {
            let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
            let vendorId = userData["vendorId"] as! String
            let hashKey    = userData["hashKey"] as! String
            let name = brandName.text
            let url_key = urlKey.text
            
        
            let description  = self.describtion.text
           
            let brandgroup = dropView.dropDownButton.currentTitle
            var group = self.brandGroup[brandgroup!];
            if group == nil
            {
                group = "1"
            }
                 var postString = "vendor_id="+vendorId+"&hashkey="+hashKey+"&url_key="+url_key!+"&description="+description!+"&name="+name!
            if thumbnail.browseImage.image != nil
            {
                let tempData2=thumbnail.browseImage.image!.jpegData(compressionQuality: 0.5)!
                let chat_file2=(tempData2.base64EncodedString())
                let formatter = DateFormatter()
                formatter.locale = Locale(identifier: "en_US_POSIX")
                formatter.dateFormat = "yyyy-MM-dd HH:mm:ss"
                
                let myString = formatter.string(from: Date()) + "thumbnail.jpg".replacingOccurrences(of: " ", with: "")
                
                let dictionary2 = ["name":myString.trimmingCharacters(in: .whitespaces),"base64_encoded_data":chat_file2]
                let thumbnailImage=convertDicTostring(str: dictionary2)
                postString += "&thumbnail="+thumbnailImage as String
            }
            if brandLogo.browseImage.image != nil
            {
                let tempData=brandLogo.browseImage.image!.jpegData(compressionQuality: 0.5)!
                let chat_file=(tempData.base64EncodedString())
                let formatter = DateFormatter()
                formatter.locale = Locale(identifier: "en_US_POSIX")
                formatter.dateFormat = "yyyy-MM-dd HH:mm:ss"
                
                let myString = formatter.string(from: Date()) + "image.jpg".replacingOccurrences(of: " ", with: "")
                let dictionary = ["name":myString.trimmingCharacters(in: .whitespaces),"base64_encoded_data":chat_file]
                let brandLogoImage=convertDicTostring(str: dictionary)
                postString += "&image="+brandLogoImage as String
            }
           
            
         
            Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
           
            
            baseURL = "vendorapi/brand/save"
           
           // var postString = "vendor_id="+vendorId+"&hashkey="+hashKey+"&url_key="+url_key!+"&thumbnail="+thumbnailImage+"&image="+brandLogoImage+"&description="+description!+"&name="+name!
            postString += "&group_id="+group!
            if backData
            {
                postString += "&brand_id="+id
            }
            print(postString)
            self.sendRequest(url: baseURL, parameters: postString)
            
            
        }
        
    }
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        if(requestUrl=="vendorapi/brand/save")
        {
            if let data = data{
                guard let json = try? JSON(data: data) else {return}
                print(json)
                if(json["data"]["success"].stringValue.lowercased() == "false"){
                    Alert_File.removeLoadingIndicator(self);
                    self.view.makeToast(json["data"]["message"].stringValue, duration: 2.0, position: .center)
                    Ced_CommonVendor.delay(2.0) {
                        self.navigationController?.popViewController(animated: true)
                    }
                }else if(json["data"]["success"].stringValue.lowercased() == "true"){
                    Alert_File.removeLoadingIndicator(self)
                    self.view.makeToast(json["data"]["message"].stringValue, duration: 2.0, position: .center)
                    Ced_CommonVendor.delay(2.0, closure: {
                        self.navigationController?.popViewController(completion: {
                            if let vc = self.vc as? ced_BrandViewController{
                                vc.initializeData();
                            }
                        })
                        /*if let vc = self.vc as? ced_manageProduct{
                         vc.orderId = self.invoiceId
                         vc.initializeData();
                         }*/
                    })
                }
            }
        }
        
        if(requestUrl=="/vendorapi/brand/info")
        {
            Alert_File.removeLoadingIndicator(self);
            if let data = data{
                guard let json = try? JSON(data: data) else {return}
                print(json)
                if(json["data"]["success"].stringValue.lowercased() == "false"){
                    self.view.showToastMsg(json["message"].stringValue)
                }else if(json["data"] != nil){
                    self.parseProductdata(json)
                }
            }
        }
        else if (requestUrl=="/vendorapi/brand/getgroup")
        {
            brandGroup = [:]
            if let data = data{
                guard let json = try? JSON(data: data) else {return}
                
                let countryListing = json.arrayValue;
                
                for d in 0..<countryListing.count
                {
                    let key = countryListing[d]["id"].stringValue;
                    let value = countryListing[d]["name"].stringValue;
                    self.brandGroup[value] = key;
                }
                if backData
                {
                    let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
                    let vendorId = userData["vendorId"] as! String
                    let hashKey    = userData["hashKey"] as! String
                    baseURL = "/vendorapi/brand/info"
                    let postString = "vendor_id="+vendorId+"&hashkey="+hashKey+"&brand_id="+id
                    self.sendRequest(url: baseURL, parameters: postString)
                }
                else
                {
                    Alert_File.removeLoadingIndicator(self);
                }
                print(brandGroup)
            }
        }
    }
    @objc func showbrandGroupDropdown(_ sender:UIButton)
    {
        let dropDown = DropDown();
        var dropDownToShow = Array(self.brandGroup.keys);
        dropDownToShow = dropDownToShow.sorted();
        if let indexToRemove = dropDownToShow.index(of: "--Please Select--") {
            dropDownToShow.remove(at: indexToRemove);
        }
        
        
        dropDown.dataSource = dropDownToShow;
        dropDown.selectionAction = {(index, item) in
            let keyToUse = self.brandGroup[item]!;
            sender.setTitle(item, for: UIControl.State());
        }
        dropDown.anchorView = sender
        dropDown.bottomOffset = CGPoint(x: 0, y:sender.bounds.height)
        
        if dropDown.isHidden {
            _ = dropDown.show();
        } else {
            _ = dropDown.hide();
        }
        
        
    }
    
    func parseProductdata(_ json:JSON){
        
        print(json)
        let result = json["data"].dictionary
        if (result!["url_key"]?.string != nil)
            {
                urlKey.text = (result!["url_key"]?.string)!
            }
        if (result!["image"]?.string != nil)
            {
                let imgRequest2 =  URL(string: (result!["image"]?.string?.addingPercentEncoding(withAllowedCharacters: CharacterSet.urlQueryAllowed))!)
                brandLogo.browseImage.sd_setImage(with: imgRequest2, placeholderImage: UIImage(named: "placeholder"))
            }
        if (result!["name"]?.string != nil)
            {
                brandName.text = (result!["name"]?.string)!
            }
        if (result!["thumbnail"]?.string != nil)
            {
                let imgRequest2 =  URL(string: (result!["thumbnail"]?.string?.addingPercentEncoding(withAllowedCharacters: CharacterSet.urlQueryAllowed))!)
                thumbnail.browseImage.sd_setImage(with: imgRequest2, placeholderImage: UIImage(named: "placeholder"))
            }
        if (result!["description"]?.string != nil)
            {
                describtion.text = (result!["description"]?.string)!
            }
      
        if (result!["group"]?.string != nil)
        {
            for (key,value) in brandGroup
            {
                if value.description == result!["group"]?.string
                {
                    dropView.dropDownButton.setTitle(key, for: UIControl.State())
                }
            }
        }
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

// Helper function inserted by Swift 4.2 migrator.
fileprivate func convertFromUIImagePickerControllerInfoKeyDictionary(_ input: [UIImagePickerController.InfoKey: Any]) -> [String: Any] {
	return Dictionary(uniqueKeysWithValues: input.map {key, value in (key.rawValue, value)})
}

// Helper function inserted by Swift 4.2 migrator.
fileprivate func convertFromUIImagePickerControllerInfoKey(_ input: UIImagePickerController.InfoKey) -> String {
	return input.rawValue
}
