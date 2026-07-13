//
//  ced_messageChatController.swift
//  VenderApp
//
//  Created by cedcoss on 13/02/21.
//  Copyright © 2021 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

struct chatData{
    let subject  : String
    let status  : String
    let message  : String
    let success : String
    var view  : [chatView]
    let inbox_list : String
    //  let customer_list : String
    init(json:JSON) {
        subject = json["subject"].stringValue
        status = json["status"].stringValue
        message = json["message"].stringValue
        view = json["view"].arrayValue.map({chatView(json: $0)})
        inbox_list = json["inbox_list"].stringValue
        success = json["success"].stringValue
    }
}

struct chatView  {
    let created_at : String
    let image : String
    let sender_name : String
    let message : String
    let sender : String
    
    init(json:JSON) {
        created_at = json["created_at"].stringValue
        image = json["image"][0].stringValue
        sender_name = json["sender_name"].stringValue
        message = json["message"].stringValue.html2String
        sender = json["sender"].stringValue
    }
}
class ced_messageChatController: ced_VendorBaseClass {
    
    var customerId: String!
    var id: String!
    var currentPage = 1
    var chatPageData: chatData?
    //var vendorComposedImage = vendorComposeMsgController()
    @IBOutlet weak var messageTextField: UITextView!
    
    @IBOutlet weak var addButton: UIButton!
    @IBOutlet weak var sendButton: UIButton!
    
    @IBOutlet weak var chatTableView: UITableView!
    var convertedFileString = ""
    var isFromCustomer = false
    var selectedVendorEmail = String()
    var vendorId = String()
    var picker = UIImagePickerController()
    var attachedData = [[String : String]]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        self.vendorId = userData["vendorId"] as! String
        //  let hashKey    = userData["hashKey"] as! String
        
        //        if isFromVendor{
        //        self.sendRequest(url: "mobimessaging/view", params: ["customer_id":custId,"id":id,"page":"\(currentPage)"], store: true)
        //        }else{
        //            self.sendRequest(url: "mobimessaging/adminview", params: ["customer_id":custId,"id":id,"page":"\(currentPage)"], store: true)
        //        }
        chatDesign()
        // Do any additional setup after loading the view.
    }
    
    func chatDesign(){
        
        // if isFromVendor {
        sendRequestForVendorMessage()
        // }
        
        messageTextField.cardView()
        // addButton.layer.cornerRadius = 20
        sendButton.layer.cornerRadius = 5
        sendButton.setThemeColor()
        sendButton.setTitle("Send".localized, for: .normal)
        addButton.addTarget(self, action: #selector(addButtonTapped(_:)), for: .touchUpInside)
        sendButton.addTarget(self, action: #selector(sendMessageAction(_:)), for: .touchUpInside)
    }
    
    func sendRequestForVendorMessage() {
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        //  let hashKey    = userData["hashKey"] as! String
        
        
        //  self.sendRequest(url: "mobimessaging/view", params: ["customer_id":custId,"id":id,"page":"\(currentPage)"], store: true)
        
        if isFromCustomer{
            self.sendRequestWithData(url: "rest/V1/vmessgingapi/customerViewMessage", params: ["vendor_id":vendorId,"id":id,"page":"\(currentPage)"])
        }else{
            self.sendRequestWithData(url: "rest/V1/vmessgingapi/adminViewMessage", params: ["vendor_id":vendorId,"id":id,"page":"\(currentPage)"])
        }
    }
    
    
    @objc func addButtonTapped(_ sender: UIButton) {
        
        let actionSheetControllerIOS8: UIAlertController = UIAlertController(title: "Please select".localized, message: nil, preferredStyle: .actionSheet)
        
        let cancelActionButton = UIAlertAction(title: "Cancel".localized, style: .cancel) { _ in
            print("Cancel")
        }
        actionSheetControllerIOS8.addAction(cancelActionButton)
        
        let saveActionButton = UIAlertAction(title: "upload image".localized, style: .default)
        { _ in
            print("upload image")
            self.image_picker()
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
    
    func image_picker(){
        print("browseButtonPressed")
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
    
    @objc func sendMessageAction(_ sender: UIButton)
    {
        let messageText = self.messageTextField.text
        
        if messageText != ""
        {
            
            if isFromCustomer{
                if convertedFileString != "" {
                    self.sendAnyRequestWithData(url: "rest/V1/vmessgingapi/customercompose", params:["vendor_id":vendorId,"message":messageText!,"subject":"Message","send_email":false,"image":"[\(convertedFileString)]","id":id!] )
                }
                else {
                    self.sendAnyRequestWithData(url: "rest/V1/vmessgingapi/customercompose", params:["vendor_id":vendorId,"message":messageText!,"subject":"Message","send_email":false,"id":id!] )
                    
                }
                
            }
            else{
                
                if convertedFileString != "" {
                    self.sendAnyRequestWithData(url: "rest/V1/vmessgingapi/admincompose", params:["vendor_id":vendorId,"message":messageText!,"subject":"Message","send_email":false,"image":"[\(convertedFileString)]" , "id" : id!])
                }
                else {
                    self.sendAnyRequestWithData(url: "rest/V1/vmessgingapi/admincompose", params:["vendor_id":vendorId,"message":messageText!,"subject":"Message","send_email":false, "id" : id!])
                }
                //"customer_id":"36",
                //                "email":selectedVendorEmail,
            }
        }
        else{
            self.view.makeToast("Enter Some message to send!".localized, duration: 1.0, position: .center)
        }
        
    }
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        guard let data = data else {return}
        guard var json = try? JSON(data: data) else {return}
        print(json)
        // json = json[0]
        
        
        if (requestUrl == "rest/V1/vmessgingapi/customerViewMessage" ||  requestUrl == "rest/V1/vmessgingapi/adminViewMessage")
        {
            do {
                if json["vendor_data"]["status"].stringValue == "false"{
                   // self.view.makeToast(json["vendor_data"]["message"].stringValue, duration: 2.0, position: .center)
                    self.chatTableView.setEmptyMessage(json["vendor_data"]["message"].stringValue)
                    return
                }else{
                    // let message = try json["response"]["view"].rawData(options: [])
                    if currentPage == 1{
                        self.chatPageData = chatData(json: json["vendor_data"])
                    }else{
                        json["vendor_data"]["view"].arrayValue.forEach{chat in
                            self.chatPageData?.view.append(chatView(json: chat))
                        }
                        
                    }
                    
                    
                    print(self.chatPageData)
                    chatTableView.delegate = self
                    chatTableView.dataSource = self
                    chatTableView.separatorStyle = .none
                    chatTableView.reloadData()
                }
            } catch let error{
                print(error.localizedDescription)
                self.view.makeToast(json["response"]["message"].stringValue, duration: 2.0, position: .center)
                // cedMageHttpException.showAlertView(me: self, msg: json["response"]["message"].stringValue, title: "Error")
            }
        }
        else if requestUrl == "rest/V1/vmessgingapi/admincompose"
        {
            if json["vendor_data"]["success"].stringValue == "true"{
                currentPage = 1
                self.view.makeToast(json["vendor_data"]["message"].stringValue, duration: 1.0, position: .center)
                
                messageTextField.text = ""
                self.attachedData.removeAll()
                
                self.sendRequestWithData(url: "rest/V1/vmessgingapi/adminViewMessage", params: ["vendor_id":vendorId,"id":id,"page":"\(currentPage)"])
                
            }
            print("admin Send")
        }
        else if requestUrl == "rest/V1/vmessgingapi/customercompose"        // for send data through api
        {
            if json["vendor_data"]["success"].stringValue == "true"{
                currentPage = 1
                self.view.makeToast(json["vendor_data"]["message"].stringValue, duration: 1.0, position: .center)
                
                messageTextField.text = ""
                self.attachedData.removeAll()
                
                self.sendRequestWithData(url: "rest/V1/vmessgingapi/customerViewMessage", params: ["vendor_id":vendorId,"id":id,"page":"\(currentPage)"])
            }
            print("vendor send")
        }
        
    }
}
extension ced_messageChatController:UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return chatPageData?.view.count ?? 0
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = chatTableView.dequeueReusableCell(withIdentifier: "ced_chatViewTC", for: indexPath) as! ced_chatViewTC
        //   let cell = chatTableView.dequeueReusableCell(withIdentifier: "chatControllerCell", for : indexPath)
        cell.dateLabel.text = chatPageData?.view[indexPath.row].created_at
        cell.messageLabel.text = chatPageData?.view[indexPath.row].message
        cell.imageHeight.constant = 0.0
        if chatPageData?.view[indexPath.row].image != ""{
            cell.imageHeight.constant = 150.0
            //cell.msgImage.imageView?.sd_setImage(with: URL(string: chatPageData?.view[indexPath.row].image ?? ""), placeholderImage: UIImage(named: "placeholder"))
            //cell.msgImage.buttonType = .cu
            if let img = chatPageData?.view[indexPath.row].image{
                cell.msgImage.sd_setImage(with: URL(string: img.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed) ?? ""), for: .normal)
            }
            
        }
        //         cell.messageLabel.layer.cornerRadius = 10
        //        cell.messageLabel.clipsToBounds = true
        cell.messageLabel.numberOfLines = 0
        /* if chatPageData?.view[indexPath.row].sender == "customer"
         {
         cell.messageLabel.backgroundColor = .lightGray
         cell.messageLabel.textColor = .white
         cell.messageView.translatesAutoresizingMaskIntoConstraints = false
         cell.messageView.leadingAnchor.constraint(equalTo: cell.contentView.leadingAnchor, constant: 5).isActive = true
         cell.messageView.topAnchor.constraint(equalTo: cell.contentView.topAnchor, constant: 5).isActive = true
         cell.messageView.bottomAnchor.constraint(equalTo: cell.contentView.bottomAnchor, constant: -5).isActive = true
         cell.messageView.widthAnchor.constraint(lessThanOrEqualToConstant: 200).isActive = true
         
         }
         else
         {
         cell.messageLabel.backgroundColor = .lightGray
         cell.messageLabel.textColor = .black
         
         cell.messageView.translatesAutoresizingMaskIntoConstraints = false
         cell.messageView.trailingAnchor.constraint(equalTo: cell.contentView.trailingAnchor, constant: -5).isActive = true
         cell.messageView.topAnchor.constraint(equalTo: cell.contentView.topAnchor, constant: 5).isActive = true
         cell.messageView.bottomAnchor.constraint(equalTo: cell.contentView.bottomAnchor, constant: -5).isActive = true
         cell.messageView.widthAnchor.constraint(lessThanOrEqualToConstant: 200).isActive = true
         }*/
        
        cell.messageView.backgroundColor  = UIColor.init(hexString: "#B8E1F3")
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return UITableView.automaticDimension
    }
    
    @objc func scrollViewDidEndDragging(_ scrollView: UIScrollView, willDecelerate decelerate: Bool) {
        let currentOffset = scrollView.contentOffset.y
        let maximumOffset = scrollView.contentSize.height - scrollView.frame.size.height
        if (maximumOffset - currentOffset) <= 40 {
            self.currentPage += 1;
            self.sendRequestForVendorMessage()
        }
    }
}

extension ced_messageChatController:UIImagePickerControllerDelegate,UINavigationControllerDelegate,UIDocumentPickerDelegate {
    
    
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
        self.view.makeToast("Selected".localized, duration: 2.0, position: .center)
        //    self.imageNameLabel.isHidden = false
        //    self.imageNameLabel.text = "Image Added"
        //    self.imageNameToSend = self.imageNameLabel.text!
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

