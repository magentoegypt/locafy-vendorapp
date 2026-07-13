//
//  ced_messageComposeController.swift
//  VenderApp
//
//  Created by cedcoss on 13/02/21.
//  Copyright © 2021 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_messageComposeController: ced_VendorBaseClass {
    @IBOutlet weak var vendorHeaderLabel: UILabel!
    @IBOutlet weak var subjectTextField: UITextField!
    @IBOutlet weak var descriptionTextView: UITextView!
    @IBOutlet weak var addBtn: UIButton!
    var currentTagForFileUpload = Int()
    @IBOutlet weak var uncheckedButton: UIButton!
    @IBOutlet weak var sendButton: UIButton!
    var imageNameToSend = String()
    var  checked = false
    var convertString = ""
    var picker = UIImagePickerController()
    var isFromAdmin = false
    var imagePicked=String()
    var customerList = [customerNameList]()
    var imagViewArray = [String:UIImageView]()
    @IBOutlet weak var vendorNameButton: UIButton!
    
    @IBOutlet weak var imageNameLabel: UILabel!
    @IBOutlet weak var vendorButtonHeight: NSLayoutConstraint!
    var convertedFileString = ""
    var attachedData = [[String : String]]()
    var selectedVendorEmail = String()
    var selectedVendorId = String()
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.imageNameLabel.isHidden = true
        setupViewController()
        addBtn.setTitle("+ ADD".localized, for: .normal)
        uncheckedButton.setImage(UIImage(named: "unchecked"), for: .normal)
        uncheckedButton.setTitle("Notify Receiver by Email".localized, for: .normal)
        uncheckedButton.contentHorizontalAlignment = .leading
        uncheckedButton.titleEdgeInsets = .init(top: 0, left: 8, bottom: 0, right: 0)
        sendButton.setTitle("SEND".localized, for: .normal)
        subjectTextField.placeholder = "Subject".localized
        self.vendorNameButton.setTitle("--Select--".localized, for: .normal)
        self.vendorNameButton.addTarget(self, action: #selector(customerDropdownTapped(_:)), for: .touchUpInside)
    }
    
    @IBAction func toggleButtonAction(_ sender: Any) {
        if uncheckedButton.currentImage == UIImage(named: "checked"){
            uncheckedButton.setImage(UIImage(named: "unchecked"), for: .normal)
            checked = false
        }
        else{
            uncheckedButton.setImage(UIImage(named: "checked"), for: .normal)
            checked = true
        }
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as! String
        uncheckedButton.tintColor = UIColor.init(hexString: colorString)
    }
    
    
    
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        guard let data = data else{return}
        guard var json = try? JSON(data: data) else {return}
        print(json)
        
        if requestUrl == "rest/V1/vmessgingapi/customercompose"        // for send data through api
        {
            if json["vendor_data"]["success"].stringValue == "true"{
                self.view.makeToast(json["vendor_data"]["message"].stringValue, duration: 1.5, position: .center)
                Ced_CommonVendor.delay(1.5) {
                    self.navigationController?.popViewController(animated: true)
                }
                
            }
            print("send")
            
            
        }
        
        else if requestUrl == "rest/V1/vmessgingapi/admincompose"
        {
            if json["vendor_data"]["success"].stringValue == "true"{
                self.view.makeToast(json["vendor_data"]["message"].stringValue, duration: 1.5, position: .center)
                Ced_CommonVendor.delay(1.5) {
                    self.navigationController?.popViewController(animated: true)
                }
                
            }
            print("admin Send")
        }
        
    }
    
    func setupViewController() {
        descriptionTextView.cardView()
        //        uncheckedButton.layer.borderWidth = 0.5
        //        uncheckedButton.layer.cornerRadius = 5
        addBtn.layer.cornerRadius = 5
        sendButton.layer.cornerRadius = 5
        sendButton.setThemeColor()
        vendorNameButton.layer.cornerRadius = 5
        vendorNameButton.setThemeColor()
        addBtn.setThemeColor()
        addBtn.addTarget(self, action: #selector(addButtonTapped(_:)), for: .touchUpInside)
        
        sendButton.addTarget(self, action: #selector(sendVendorInboxData(_:)), for: .touchUpInside)
        if isFromAdmin{
            vendorHeaderLabel.text = "Admin Compose".localized
            vendorNameButton.isHidden = true
            vendorButtonHeight.constant = 0
        }
        else{
            vendorHeaderLabel.text = "Customer Compose".localized
            vendorNameButton.isHidden = false
            vendorButtonHeight.constant = 40
        }
        
        
        
    }
    @objc func sendVendorInboxData(_ sender: UIButton){
        
        let name = self.vendorNameButton.currentTitle!
        let subject = self.subjectTextField.text!
        let message = self.descriptionTextView.text!
        print(subject)
        print(message)
        //   let image = self.addBtn.text!
        if   subject != "" && message != ""
        {
            let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
            let vendorId = userData["vendorId"] as! String
            //  let hashKey    = userData["hashKey"] as! String
            if isFromAdmin {
                if convertedFileString != "" {
                    self.sendAnyRequestWithData(url: "rest/V1/vmessgingapi/admincompose", params:["vendor_id":vendorId,"message":message,"subject":subject,"send_email":checked,"image":"[\(convertedFileString)]"])
                }
                else {
                    self.sendAnyRequestWithData(url: "rest/V1/vmessgingapi/admincompose", params:["vendor_id":vendorId,"message":message,"subject":subject,"send_email":checked])
                }
                //"customer_id":"36",
            } else {
                if name != "--Select--".localized{
                    if convertedFileString != "" {
                        self.sendAnyRequestWithData(url: "rest/V1/vmessgingapi/customercompose", params:["vendor_id":vendorId,"message":message,"subject":subject,"send_email":checked,"image":"[\(convertedFileString)]","customer_id":"\(selectedVendorId)"])
                    }
                    else {
                        self.sendAnyRequestWithData(url: "rest/V1/vmessgingapi/customercompose", params:["vendor_id":vendorId,"message":message,"subject":subject,"send_email":checked,"customer_id":"\(selectedVendorId)"])
                    }
                    
                    
                }
            }
            
        }
        else{
            self.view.makeToast("Enter all data!".localized, duration: 1.0, position: .center)
        }
        
    }
    
    @objc func customerDropdownTapped(_ sender: UIButton) {
        let dropDown = DropDown()
        var customerNames = [String]()
        //  var vendorEmails = [String]()
        var customerId = [String]()
        for name in customerList {
            customerNames.append(name.first_name)
            //    vendorEmails.append(name.email)
            customerId.append(name.customer_id)
        }
        
        dropDown.dataSource = customerNames;
        dropDown.selectionAction = {(index, item) in
            sender.setTitle(item, for: .normal)
            //self.selectedVendorEmail = vendorEmails[index]
            self.selectedVendorId = customerId[index]
        }
        dropDown.anchorView = sender
        dropDown.bottomOffset = CGPoint(x: 0, y:sender.bounds.height)
        
        if dropDown.isHidden {
            _=dropDown.show();
        } else {
            dropDown.hide();
        }
        
    }
    
    @objc func addButtonTapped(_ sender: UIButton) {
        addBtn.tag = 777
        let actionSheetControllerIOS8: UIAlertController = UIAlertController(title: "Please select".localized, message: nil, preferredStyle: .actionSheet)
        
        let cancelActionButton = UIAlertAction(title: "Cancel".localized, style: .cancel) { _ in
            print("Cancel")
        }
        actionSheetControllerIOS8.addAction(cancelActionButton)
        
        let saveActionButton = UIAlertAction(title: "upload image".localized, style: .default)
        { _ in
            print("upload image")
            self.image_picker(_:sender)
        }
        actionSheetControllerIOS8.addAction(saveActionButton)
        
        let uploadpdf = UIAlertAction(title: "Upload Document".localized, style: .default)
        { _ in
            print("Delete")
            self.browseButtonPressed()
        }
        actionSheetControllerIOS8.addAction(uploadpdf)
        self.present(actionSheetControllerIOS8, animated: true, completion: nil)
    }
    
    func image_picker(_ sender : UIButton){
        print("browseButtonPressed")
        print(sender.tag)
        currentTagForFileUpload = sender.tag
        picker.delegate=self
        picker.allowsEditing = false
        picker.sourceType = .photoLibrary
        self.present(picker, animated: true, completion: nil)
    }
    func browseButtonPressed(){
        
        let doc = UIDocumentPickerViewController(documentTypes: ["public.text","public.content","public.image","public.data"], in: .import)
        doc.delegate=self
        doc.modalPresentationStyle = .popover
        self.present(doc, animated: true, completion: nil)
    }
    
    
}
extension ced_messageComposeController:UIImagePickerControllerDelegate,UINavigationControllerDelegate,UIDocumentPickerDelegate {
    
    
    //   UIDocumentMenuDelegate
    
    
    //func documentMenu(_ documentMenu: UIDocumentMenuViewController, didPickDocumentPicker documentPicker: UIDocumentPickerViewController) {
    //documentPicker.delegate=self
    //documentPicker.modalPresentationStyle = .fullScreen
    //self.present(documentPicker, animated: true, completion: nil)
    //}
    
    
    
    
    
    func documentPicker(_ controller: UIDocumentPickerViewController, didPickDocumentAt url: URL) {
        
        let fileName=url.absoluteString.components(separatedBy: "/")
        // docUrlLabel.text=url.absoluteString
        print("didPickDocumentAt")
        // FileSelectedOrNot.text = fileName[0]
        
        let isSecuredURL = url.startAccessingSecurityScopedResource() == true
        let coordinator = NSFileCoordinator()
        var error: NSError? = nil
        coordinator.coordinate(readingItemAt: url, options: [], error: &error) { (url) -> Void in
            do{
                let temp=try Data(contentsOf: url)
                //self.trainingDocData=temp.base64EncodedString()
                
                let temp1=fileName[fileName.count-1].components(separatedBy: ".")
                print("The file you have choosed is not in correct format")
                // print(temp1)
                if (temp1[1] == "pdf" || temp1[1] == "zip" )
                {
                    print(temp1)
                    let dataPdf = temp.base64EncodedString()
                    self.attachedData.append(["name":"xyz.pdf", "base64_encoded_data": dataPdf])
                }
                else
                {
                    let msg = "The file you have choosed is not in correct format"
                    print(msg)
                    return
                    
                }
            }catch let error{
                
                print(error.localizedDescription)
                
            }
        }
        if (isSecuredURL) {
            url.stopAccessingSecurityScopedResource()
        }
        print(url)
        controller.dismiss(animated: true, completion: nil)
    }
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        // print(info)
        //    if let image = info[UIImagePickerController.InfoKey.originalImage.rawValue] as? UIImage {
        //
        //
        //        let imgData=image.pngData()
        //let imageData=(imgData?.base64EncodedString())!
        //        let values = info[UIImagePickerController.InfoKey.referenceURL.rawValue] as? NSURL
        //let imgName = values?.lastPathComponent
        
        /*let image = info[UIImagePickerControllerOriginalImage] as? UIImage
         let imgsize:NSData = UIImagePNGRepresentation(image!)! as NSData
         let fileSize = Double(imgsize.length / 1024 / 1024)
         totalSize += fileSize
         if totalSize > 5.00 {
         picker.dismiss(animated: true, completion: nil)
         self.view.makeToast("Please upload a file of less than 5 Mb", duration: 2.0, position: .center)
         }else{ */
        
        //attachedData.append(["name":imgName ?? "xyz.png", "base64_encoded_data": imageData])
        //        print(attachedData)
        //picker.dismiss(animated: true, completion: nil)
        //}
        
        let info = convertFromUIImagePickerControllerInfoKeyDictionary(info)
        print(info)
        self.imageNameLabel.isHidden = false
        self.imageNameLabel.text = "Image Added".localized
        self.imageNameToSend = self.imageNameLabel.text!
        let image = info[convertFromUIImagePickerControllerInfoKey(UIImagePickerController.InfoKey.originalImage)] as? UIImage
        if let tempData=(image ?? UIImage()).jpegData(compressionQuality: 0.5)
        {
            let dateFormatter: DateFormatter = DateFormatter()
            dateFormatter.locale = Locale(identifier: "en_US_POSIX")
            dateFormatter.dateFormat = "MM/dd/yyyy hh:mm a"
            var selectedDate: String = dateFormatter.string(from: Date())
            selectedDate = selectedDate.replacingOccurrences(of: " ", with: "")
            let chat_file=(tempData.base64EncodedString())
            let dictionary = ["name":"124\(selectedDate).jpg","base64_encoded_data":chat_file]//,"type":"file"
            self.convertedFileString=convertDicTostring(str: dictionary)
            print(convertedFileString)
            
        }
        //self.imageNameLabel.text = info
        //Showing image in imageview after image is selected in ImagePickerController
        //  if let img = self.view.viewWithTag(currentTagForFileUpload) as? UIImageView
        //    {
        //        img.image = info[convertFromUIImagePickerControllerInfoKey(UIImagePickerController.InfoKey.originalImage)] as? UIImage
        //
        //        if let tempData=(img.image ?? UIImage()).jpegData(compressionQuality: 0.5)
        //        {
        //            let dateFormatter: DateFormatter = DateFormatter()
        //            dateFormatter.dateFormat = "MM/dd/yyyy hh:mm a"
        //            var selectedDate: String = dateFormatter.string(from: Date())
        //            selectedDate = selectedDate.replacingOccurrences(of: " ", with: "")
        //            let chat_file=(tempData.base64EncodedString())
        //            let dictionary = ["name":"124\(selectedDate).jpg","base64_encoded_data":chat_file,"type":"image"]
        //            self.convertString=convertDicTostring(str: dictionary)
        //            print(convertString)
        //
        //        }
        //        imagViewArray.updateValue(img, forKey: "image_document")
        //        print(imagViewArray["name"])
        //    }
        self.dismiss(animated: true, completion: nil);
        
    }
    
    func documentMenuWasCancelled(_ documentMenu: UIDocumentMenuViewController) {
        documentMenu.dismiss(animated: true, completion: nil)
    }
    func documentPickerWasCancelled(_ controller: UIDocumentPickerViewController) {
        controller.dismiss(animated: true, completion: nil)
    }
    func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
        picker.dismiss(animated: true, completion: nil)
    }
    
}

fileprivate func convertFromUIImagePickerControllerInfoKeyDictionary(_ input: [UIImagePickerController.InfoKey: Any]) -> [String: Any] {
    return Dictionary(uniqueKeysWithValues: input.map {key, value in (key.rawValue, value)})
}

fileprivate func convertFromUIImagePickerControllerInfoKey(_ input: UIImagePickerController.InfoKey) -> String {
    return input.rawValue
}


