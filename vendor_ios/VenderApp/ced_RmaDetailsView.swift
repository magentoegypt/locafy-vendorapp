//
//  ced_RmaDetailsView.swift
//  VenderApp
//
//  Created by Macmini on 06/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit
import QuickLook

class ced_RmaDetailsView: ced_VendorBaseClass,UITableViewDelegate,UITableViewDataSource,UINavigationControllerDelegate,UIImagePickerControllerDelegate,UIDocumentInteractionControllerDelegate,URLSessionDownloadDelegate,UIDocumentMenuDelegate,UIDocumentPickerDelegate
{
    
    @IBOutlet weak var topLbl: UILabel!
    @IBOutlet weak var RMaId: UILabel!
    @IBOutlet weak var savebutton: UIButton!
    @IBOutlet weak var topview: UIView!
    @IBOutlet weak var tableview: UITableView!
    /////
    var dataPdf = ""
    var downloadbutton = UIButton()
    ////
    var id = ""
    var rma_id = ""
    var order_info = [String:String]()
    var customer_info = [String:String]()
    var item_info =  [[String:String]]()
    var chat_history = [[String:String]]()
    var activity_history = [[String:String]]()
    var docDataArray=[String:String]()
    var selectedUpload=0
    var image_Shop = UIImageView()
    var FileSelectedOrNot = UILabel()
    var messagetext = UITextView()
    var custumername = UITextField()
    var custumeremail = UITextField()
    var status = UIButton()
    var fileuploaded = false
    let picker = UIImagePickerController()
    var image_Browse = UIImageView()
    var filename = "123456"
    var main_path = ""
    //    MARK: varible to download Fil
    var urlpdf = URL(string: "")
    var type=""
//    var downloadTask: URLSessionDownloadTask!
    var backgroundSession: URLSession!
    var fileExtension=".pdf"
    ////
    var colorString = ""
    
    var sattusDropdpwmData = [String:String]()
    var selectedStatus = ""
    
    override func viewDidLoad() {
        super.viewDidLoad()
        print("ced_RmaDetailsView")
        print(id)
        loadData()
        topLbl.text = " Edit Return Request".localized
        if(ced_storeVC.selectLangauge == "ar"){
            topLbl.textAlignment = .right
        }
        savebutton.setTitle("SAVE".localized, for: .normal)
        RMaId.text=rma_id
         savebutton.addTarget(self, action: #selector(self.savebuttonpressed(sender: )), for: .touchUpInside)
        colorString = Ced_CommonVendor.getInfoPlist("themecolor") as! String
        topview.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
        let color = Ced_CommonVendor.getInfoPlist("subthemecolor") as! String
        savebutton.backgroundColor=Ced_CommonVendor.UIColorFromRGB(color)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    override func viewWillAppear(_ animated: Bool) {
//        downloadTask=URLSessionDownloadTask()
    }
    //    Mark : Request And Responce
    @objc func loadData(){
        order_info = [:]
        customer_info = [:]
        item_info =  []
        chat_history = []
        activity_history = []
        docDataArray=[:]
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
        let baseUrl = "vrmapi/index/view"
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        print(userData)
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        let id    = self.id
        let postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey+"&rma_id=" + id
        print(postString)
        sendRequest(url: baseUrl, parameters: postString);
       
    }
    
    
    override func recieveResponse(data:Data?,requestUrl:String?,response:URLResponse?){
        
        Alert_File.removeLoadingIndicator(self)
        if let data = data{
            if(requestUrl=="vrmapi/index/view")
            {                guard let json = try? JSON(data: data) else {return}
                print(json)
                if(json["data"]["success"].stringValue == "true"){
//                    if json["data"]["success"].intValue > 0
//                    {
                    for (itm,val) in json["data"]["order_info"]["status_list"].dictionaryValue{
                        self.sattusDropdpwmData[itm] = val.stringValue
                    }
                        self.parseRmaIdData(json)
//                    }
                }else if json["data"]["success"] == false
                {
                    self.view.showToastMsg(json["data"]["message"].stringValue)
                }
                else
                {
                    self.view.showToastMsg("Problem in loading Data".localized)
                }
            }
            else if requestUrl == "vrmapi/index/update"
            {
                print(requestUrl)
                guard let json = try? JSON(data: data) else {return}
                print(json)
                if(json["data"]["success"].stringValue == "true"){
                    if json["data"]["success"] == true
                    {
                        self.view.showToastMsg(json["data"]["message"].stringValue)
                        
                        loadData()
                    }
                }else if json["data"]["success"] == false
                {
                    self.view.showToastMsg(json["data"]["message"].stringValue)
                }
                
            }
            else if requestUrl == "vrmapi/index/chat"
            {
                print(requestUrl ?? "")
                guard let json = try? JSON(data: data) else {return}
                print(json)
                if(json["data"]["success"].stringValue == "true"){
                    if json["data"]["success"] == true
                    {
                        self.view.showToastMsg(json["data"]["message"].stringValue)
                        loadData()
                    }
                }else if json["data"]["success"] == false
                {
                   // self.view.showToastMsg(json["data"]["message"].stringValue)
                    self.tableview.setEmptyMessage(json["data"]["message"].stringValue)
                    return
                }
            }
            tableview.restore()
            tableview.reloadData()
        }
        
    }
    
    func parseRmaIdData(_ json:JSON){
        if json["data"]["order_info"].dictionaryValue != nil
        {
            let data = json["data"]["order_info"].dictionaryValue
            let order_id = data["order_id"]!.stringValue
            let purchased_point = data["purchased_point"]!.stringValue
            let status = data["status"]!.stringValue
            selectedStatus=status
            let reason = data["reason"]!.stringValue
            let package_condition = data["package_condition"]!.stringValue
            let resolution_requested = data["resolution_requested"]!.stringValue
            order_info = ["order_id":order_id,"purchased_point":purchased_point,"status":status,"reason":reason,"package_condition":package_condition,"resolution_requested":resolution_requested]
            if status != "Completed"{
                savebutton.isHidden = false
            }else{
                savebutton.isHidden = true
            }
            
        }
        if json["data"]["customer_info"].dictionaryValue != nil
        {
            let data = json["data"]["customer_info"].dictionaryValue
            let customer_name = data["customer_name"]!.stringValue
            let customer_email = data["customer_email"]!.stringValue
            let group = data["group"]!.stringValue
            let add = data["address"]?.dictionary
            var address = ""
            for (_,value) in add!
            {
                address=address+" "+value.stringValue
            }
            
            customer_info = ["customer_name":customer_name,"customer_email":customer_email,"group":group,"address":address]
            
        }
        for result in json["data"]["item_info"].arrayValue{
            let product_name = result["product_name"].stringValue
            let product_sku = result["product_sku"].stringValue
            let price = result["price"].stringValue
            let rma_qty  = result["rma_qty"].stringValue
            let row_total = result["row_total"].stringValue
            let iteminfo = ["product_name":product_name,"product_sku":product_sku,"price":price,"rma_qty":rma_qty,"row_total":row_total]
            self.item_info.append(iteminfo)
        }
        if json["data"]["chat_history"].exists()
        {
            for result in json["data"]["chat_history"].arrayValue{
                let chat_date = result["chat_date"].stringValue
                let chat_file = result["chat_file"].stringValue
                let chat_text = result["chat_text"].stringValue
                let sender  = result["sender"].stringValue
                let chat = ["chat_date":chat_date,"chat_text":chat_text,"chat_file":chat_file,"sender":sender]
                self.chat_history.append(chat)
            }
        }
        for result in json["data"]["activity_history"].arrayValue{
            let date = result["date"].stringValue
            let notification = result["notification"].stringValue
            let activity = ["date":date,"notification":notification]
            self.activity_history.append(activity)
        }
        tableview.delegate=self
        tableview.dataSource=self
       
    }
    
    //    Mark : table view @objc functions
    @objc func numberOfSections(in tableView: UITableView) -> Int {
        return 6
    }
    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        
        if section == 0
        {
            return 1
        }
        else if section == 2
        {
            return item_info.count
        }
        else if section == 3
        {
           return 1
        }
        else if section == 4
        {
            return chat_history.count+1
        }
        else if section == 5
        {
            return activity_history.count+1
        }
       return 1
    }
    
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if indexPath.section == 0
        {
            let cell = tableView.dequeueReusableCell(withIdentifier: "cedRmaOrderInformation") as! cedRmaOrderInformation
            cell.toplabel.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
            cell.toplabel.text = "Order Information ".localized
            cell.orderid.text=order_info["order_id"]
            cell.orderid.isEnabled=false
            cell.PackageCondition.text=order_info["package_condition"]
            cell.PackageCondition.isEnabled=false
            cell.PurchaseFrom.text = order_info["purchased_point"]
//            status=cell.Status
            cell.Status.setTitle(selectedStatus, for: .normal)
          //  cell.Status.addTarget(self, action: #selector(self.showstatus(sender: )), for: .touchUpInside)
            cell.Reason.text=order_info["reason"]
            cell.Reason.isEnabled=false
            cell.ResolutionRequested.text = order_info["resolution_requested"]
            cell.ResolutionRequested.isEnabled=false
            return cell
        }
        else if indexPath.section == 2
        {
            let cell = tableView.dequeueReusableCell(withIdentifier: "ced_RMAItemInformationCELL") as! ced_RMAItemInformationCELL
            cell.toplable.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
            cell.Product.text=item_info[indexPath.row]["product_name"]
            cell.ItemSKU.text=item_info[indexPath.row]["product_sku"]
            cell.Price.text=item_info[indexPath.row]["price"]
            cell.QtyforRMA.text=item_info[indexPath.row]["rma_qty"]
            cell.rowTotal.text=item_info[indexPath.row]["row_total"]
            cell.Product.isEnabled=false
            cell.ItemSKU.isEnabled=false
            cell.Price.isEnabled=false
            cell.QtyforRMA.isEnabled=false
            cell.rowTotal.isEnabled = false
            return cell
        }
        else if indexPath.section == 3
        {
            let cell = tableView.dequeueReusableCell(withIdentifier: "ced_ChatInformationCELL") as! ced_ChatInformationCELL
            cell.toplable.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
            cell.toplable.text = " Chat Information".localized
            cell.sendmessage.setTitle("SEND MESSAGE".localized, for: .normal)
            cell.messageLbl.text = "Message".localized
            cell.attachlbl.text = "Attch Your File".localized
            cell.browseButton.setTitle("Browse".localized, for: .normal)
            
            cell.browseButton.addTarget(self, action: #selector(self.browseButtonPressed(sender: )), for: .touchUpInside)
            cell.sendmessage.addTarget(self, action: #selector(self.SendMessagebutton(sender: )), for: .touchUpInside)
            FileSelectedOrNot=cell.nfileselected
            cell.nfileselected.text = "NO File Selected".localized
            cell.MessageTextFeild.text = ""
            messagetext=cell.MessageTextFeild
            return cell
        }
        else if indexPath.section == 4
        {
            
            if indexPath.row == 0
            {
                let cell = tableView.dequeueReusableCell(withIdentifier: "ced_header") as! ced_header
                cell.header.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
                cell.header.text = "Chat History Information ".localized
                if chat_history.count == 0
                {
                    
                    cell.toplable.text = "No chat history".localized
                }
                else
                {
                    cell.toplable.text = "Chat History".localized
                }
                return cell
            }
            else
            {
                let cell = tableView.dequeueReusableCell(withIdentifier: "ced_ChatHistoryInformationCELL") as! ced_ChatHistoryInformationCELL
                cell.toplable.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
                cell.dat.text = chat_history[indexPath.row-1]["chat_date"]
                cell.message.text = chat_history[indexPath.row-1]["chat_text"]
                
                let Url = settings.baseUrl
                if chat_history[indexPath.row-1]["chat_file"]! == Url+"pub/media/ced/csrma/chat/"
                {
                    cell.chat_file.isHidden=true
                }
                else
                {
                    cell.chat_file.setTitle(chat_history[indexPath.row-1]["chat_file"] , for: .normal)
                    cell.chat_file.isHidden=false
                    urlpdf = URL(string: chat_history[indexPath.row-1]["chat_file"]!)
                    cell.chat_file.addTarget(self, action: #selector(self.chat_fileButtonPressed(sender: )), for: .touchUpInside)
                }
                if chat_history[indexPath.row-1]["sender"] == "Vendor"
                {
                    cell.chathistoryview.backgroundColor = Ced_CommonVendor.UIColorFromRGB("#E9967A")
                    

                }
                else
                {
                    cell.chathistoryview.backgroundColor = Ced_CommonVendor.UIColorFromRGB("#F3F3DF")
                }
                cell.typevenderorbuyer.text = chat_history[indexPath.row-1]["sender"]
                return cell
            }
           
        }
        else if indexPath.section == 5
        {
            if indexPath.row == 0
            {
                let cell = tableView.dequeueReusableCell(withIdentifier: "ced_header") as! ced_header
                cell.header.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
                cell.header.text = "Activity Information".localized
                if activity_history.count == 0
                {
                    cell.toplable.text = "No Activity Notification".localized
                }
                else
                {
                    cell.toplable.text = "Activity Notification".localized
                }
                
                return cell
            }
            else
            {
                let cell = tableView.dequeueReusableCell(withIdentifier: "ced_ActivityInformationCELL") as! ced_ActivityInformationCELL
                cell.toplable.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
                cell.status.text = activity_history[indexPath.row-1]["notification"]
                cell.date.text = activity_history[indexPath.row-1]["date"]
                return cell
            }
            
        }
        
        /////section == 1
        let cell = tableView.dequeueReusableCell(withIdentifier: "ced_RmaCustomerInformation") as! ced_RmaCustomerInformation
        cell.toplable.backgroundColor=Ced_CommonVendor.UIColorFromRGB(colorString)
        cell.CustomerName.text=customer_info["customer_name"]
        custumername=cell.CustomerName
        custumeremail=cell.CustomerEmail
        cell.CustomerAddress.text = customer_info["address"]
        cell.CustomerEmail.text=customer_info["customer_email"]
        cell.CustomerGroup.text=customer_info["group"]
        
        cell.CustomerName.isEnabled=false
        cell.CustomerAddress.isEnabled=false
        cell.CustomerEmail.isEnabled=false
        cell.CustomerGroup.isEnabled=false
        return cell
        
    }
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if indexPath.section == 0
        {
            return 280
        }
        else if indexPath.section == 2
        {
            return 230
        }
//        else if indexPath.section == 3
//        {
//            return 345
//        }
//        else if indexPath.section == 4
//        {
//            if indexPath.row == 0
//            {
//                return 80
//            }
//            
//            let Url = settings.baseUrl
//            if chat_history[indexPath.row-1]["chat_file"]! == Url+"pub/media/ced/csrma/chat/"
//            {
//                return 170
//            }
//            else
//            {
//                return 220
//            }
//        }
        else if indexPath.section == 5
        {
            if indexPath.row == 0
            {
                return 70
            }
            return 95
        }
        /// for section 1
        return 0
    }
    @objc func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        print(indexPath.section)
    }
    
    
    
    /////Mark: Button Functions
    
    @objc func browseButtonPressed(sender:UIButton)
    {
        let actionSheetControllerIOS8: UIAlertController = UIAlertController(title: "Please select".localized, message: "Option to select".localized, preferredStyle: .actionSheet)
        
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
        
        let uploadpdf = UIAlertAction(title: "upload pdf".localized, style: .default)
        { _ in
            print("Delete")
            self.browseButtonPressed()
        }
        if let popoverController = actionSheetControllerIOS8.popoverPresentationController {
            popoverController.sourceView = self.view
            popoverController.sourceRect = CGRect(x: self.view.bounds.midX, y: self.view.bounds.midY, width: 0, height: 0)
            popoverController.permittedArrowDirections = []
        }
        actionSheetControllerIOS8.addAction(uploadpdf)
        actionSheetControllerIOS8.modalPresentationStyle = .fullScreen
        self.present(actionSheetControllerIOS8, animated: true, completion: nil)
    }
    
    
    @objc func image_picker()
    {
        print("browseButtonPressed")
        picker.delegate=self
        picker.allowsEditing = false
        picker.sourceType = .photoLibrary
        self.present(picker, animated: true, completion: nil)
    }
    
    @objc func browseButtonPressed(){
        let doc=UIDocumentMenuViewController(documentTypes: ["public.text","public.content","public.image","public.data"], in: .import)
        doc.delegate=self
        doc.modalPresentationStyle = .popover
        self.present(doc, animated: true, completion: nil)
    }
    
    
    @objc func documentMenu(_ documentMenu: UIDocumentMenuViewController, didPickDocumentPicker documentPicker: UIDocumentPickerViewController) {
        documentPicker.delegate=self
        documentPicker.modalPresentationStyle = .fullScreen
        self.present(documentPicker, animated: true, completion: nil)
        print("didPickDocumentPicker")
    }
    
    
    @objc func documentPicker(_ controller: UIDocumentPickerViewController, didPickDocumentAt url: URL) {
        
        let fileName=url.absoluteString.components(separatedBy: "/")
        
        print("didPickDocumentAt")
        FileSelectedOrNot.text = fileName[0]
        
        let isSecuredURL = url.startAccessingSecurityScopedResource() == true
        let coordinator = NSFileCoordinator()
        var error: NSError? = nil
        coordinator.coordinate(readingItemAt: url, options: [], error: &error) { (url) -> Void in
            do{
                let temp=try Data(contentsOf: url)
                //self.trainingDocData=temp.base64EncodedString()
                
                let temp1=fileName[fileName.count-1].components(separatedBy: ".")
                print("The file you have choosed is not in correct format")
                print(temp1)
                if temp1[1] != "pdf"
                {
                    let msg = "The file you have choosed is not in correct format".localized
                    print(msg)
                    return
                }
                else
                {
                    
                    self.dataPdf = temp.base64EncodedString()
                }
            }catch let error{
                print("%%^^&*(UGGVGHBJKBFXFGXCF")
                print(error.localizedDescription)
                print(error)
            }
        }
        if (isSecuredURL) {
            url.stopAccessingSecurityScopedResource()
        }
        print(url)
        controller.dismiss(animated: true, completion: nil)
    }
    
    @objc func documentMenuWasCancelled(_ documentMenu: UIDocumentMenuViewController) {
        print("documentMenuWasCancelled")
        documentMenu.dismiss(animated: true, completion: nil)
    }
    
    @objc func documentPickerWasCancelled(_ controller: UIDocumentPickerViewController) {
        print("documentPickerWasCancelled")
        controller.dismiss(animated: true, completion: nil)
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
        print("didFinishPickingMediaWithInfo")
        FileSelectedOrNot.text = "1245454545"
        image_Shop.contentMode = .scaleAspectFit
        image_Shop.image = chosenImage
        fileuploaded=true
        picker.dismiss(animated:true, completion: nil)
    }
    
    
   
    
    
    //    MARK: SendMessagebutton
    
    @objc func SendMessagebutton(sender:UIButton)
    {
        print(messagetext.text)
        if messagetext.text == ""
        {
            ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error".localized, showMsg: "FIELD REQUIRED".localized)
            return
        }
        else
        {
            let baseUrl = "vrmapi/index/chat"
            let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
            print(userData)
            let vendorId = userData["vendorId"] as! String
            let hashKey = userData["hashKey"] as! String
            var postString = ""
            let id    = self.id
            let message = messagetext.text
            if fileuploaded==true
            {
                let formatter = DateFormatter()
                formatter.locale = Locale(identifier: "en_US_POSIX")
                formatter.dateFormat = "yyyy-MM-dd HH:mm:ss"
                let imageNAME = formatter.string(from: Date()).replacingOccurrences(of: " ", with: "")
                let formImage = image_Shop.image ?? UIImage()
                let initialSize = formImage.size
                
                var imageData=Data()
                
                if(initialSize.height > 200 || initialSize.width > 200)
                {
                    print("above");
                    
                    let newSize:CGSize = CGSize(width: 250, height: 250);
                    let rect = CGRect(x: 0, y: 0, width: newSize.width, height: newSize.height)
                    UIGraphicsBeginImageContextWithOptions(newSize, false, 1.0)
                    formImage.draw(in: rect);
                    let newImage = UIGraphicsGetImageFromCurrentImageContext()
                    UIGraphicsEndImageContext()
                    imageData = newImage!.jpegData(compressionQuality: 0.5)!
                }
                else
                {
                    print("below");
                    imageData = formImage.jpegData(compressionQuality: 0.5)!
                }
                let onlyExtension = imageData.format
//                let ext = onlyExtension.replacingOccurrences(of: ".", with: "")
                var chat_file=(imageData.base64EncodedString())
                
                
                chat_file = chat_file.replacingOccurrences(of: " ", with: "%2B")
                
//                let convertedString = convertImageToString(formImage:image_Shop.image ?? UIImage() , fromName: imageNAME) ?? ""
                let dictionary = ["name":imageNAME+onlyExtension,"base64_encoded_data":chat_file]
                let convertedstring=convertDicTostring(str: dictionary)
                let messagedata="&chat="+message!+"&image="+(convertedstring as String)
                postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey+"&rma_id=" + id+messagedata
//                print(postString)
            }else if dataPdf != ""
            {
                print("imageDataKey")
                let dictionary = ["name":"hgshd.pdf","base64_encoded_data":dataPdf]
//                let convertedstring = dictionary.convertToJson() as String  convertDicTostring
                let convertedstring=convertDicTostring(str: dictionary)
                let messagedata="&chat="+message!+"&image="+(convertedstring)
                postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey+"&rma_id=" + id+messagedata
                print(postString)
            }
            else
            {
                let messagedata="&chat="+message!
                postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey+"&rma_id=" + id+messagedata
            }
            sendRequest(url: baseUrl, parameters: postString);
            
        }
    }
    
    
    
    //    MARK: Funct to get Image DATA
    
    @objc func convertImageToString(formImage:UIImage,fromName :String) -> String? {
       
        let initialSize = formImage.size
        
        var imageData=Data()
        
        if(initialSize.height > 200 || initialSize.width > 200)
        {
            print("above");
            
            let newSize:CGSize = CGSize(width: 250, height: 250);
            let rect = CGRect(x: 0, y: 0, width: newSize.width, height: newSize.height)
            UIGraphicsBeginImageContextWithOptions(newSize, false, 1.0)
            formImage.draw(in: rect);
            let newImage = UIGraphicsGetImageFromCurrentImageContext()
            UIGraphicsEndImageContext()
            imageData = newImage!.jpegData(compressionQuality: 0.5)!
        }
        else
        {
            print("below");
            imageData = formImage.jpegData(compressionQuality: 0.5)!
        }
        let onlyExtension = imageData.format
        let ext = onlyExtension.replacingOccurrences(of: ".", with: "")
        let chat_file=(imageData.base64EncodedString())
        return chat_file
    }
    
    
    
    
    
    
    
    //    MARK: savebuttonpressed
    
    @objc func savebuttonpressed(sender:UIButton)
    {
        let baseUrl = "vrmapi/index/update"
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        print(userData)
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
//        let resolution_requested = "Refund"
        let order_id = order_info["order_id"]
        let status = sattusDropdpwmData[selectedStatus] ?? ""
        let custumername = self.custumername.text
        let custumeremail = self.custumeremail.text
        let str = "&customer_name="+custumername!+"&customer_email="+custumeremail!
        let str1="&resolution_requested=Refund&order_id="+order_id!
        let postString = "vendor_id="+vendorId+"&hashkey="+hashKey+"&rma_id="+id+"&status="+status+str+str1
        print(postString)
        sendRequest(url: baseUrl, parameters: postString);
    }
    
    @objc func showstatus(sender:UIButton){
        let dropDown = DropDown();
        dropDown.dataSource = Array(sattusDropdpwmData.keys)
        dropDown.selectionAction = {(index, item) in
            sender.setTitle(item, for: .normal)
            self.selectedStatus = item
        }
        dropDown.anchorView = sender
        dropDown.bottomOffset = CGPoint(x: 0, y:sender.bounds.height)
        if dropDown.isHidden {
            dropDown.show();
        } else {
            dropDown.hide();
        }
    }
    
   
    //    MARK: Download DeleGate
    @objc func chat_fileButtonPressed (sender:UIButton)
    {
        print("----\(sender.currentTitle)")
        var downloadTask: URLSessionDownloadTask!
        if let title = sender.currentTitle{
            if let url = URL(string: title) {
                UIApplication.shared.open(url)
            }
//            urlpdf = URL(string: title)
//
//            //print(urlpdf!)
//            fileExtension="."+(urlpdf?.pathExtension)!
//            print(fileExtension)
//            let backgroundSessionConfiguration = URLSessionConfiguration.background(withIdentifier: "backgroundSession")
//            backgroundSession = Foundation.URLSession(configuration: backgroundSessionConfiguration, delegate: self, delegateQueue: OperationQueue.main)
//            downloadTask = backgroundSession.downloadTask(with: urlpdf!)
//            downloadTask.resume()
        }
        
    }
    
    var viewer: UIDocumentInteractionController!
    
    @objc func showFileWithPath(path: String){
        let isFileFound:Bool? = FileManager.default.fileExists(atPath: path)
        if isFileFound == true{
            viewer = UIDocumentInteractionController(url: URL(fileURLWithPath: path))

            viewer.delegate = self
            viewer.presentPreview(animated: true)
        }
    }
    
    //MARK: URLSessionDownloadDelegate
    // 1
    @objc func urlSession(_ session: URLSession,
                    downloadTask: URLSessionDownloadTask,
                    didFinishDownloadingTo location: URL)
    {
        
        let path = NSSearchPathForDirectoriesInDomains(FileManager.SearchPathDirectory.documentDirectory, FileManager.SearchPathDomainMask.userDomainMask, true)
        let documentDirectoryPath:String = path[0]
        let fileManager = FileManager()
        let formatter = DateFormatter()
        formatter.locale = Locale(identifier: "en_US_POSIX")
        formatter.dateFormat = "yyyy-MM-dd HH:mm:ss"
        let imageName = formatter.string(from: Date()).replacingOccurrences(of: " ", with: "")
        let destinationURLForFile = URL(fileURLWithPath: documentDirectoryPath.appendingFormat("/\(imageName)."+fileExtension))
        print(destinationURLForFile.path)
        if fileManager.fileExists(atPath: destinationURLForFile.path){
            showFileWithPath(path: destinationURLForFile.path)
        }
        else{
            do {
                try fileManager.moveItem(at: location, to: destinationURLForFile)
                // show file
                print(destinationURLForFile.path)
                showFileWithPath(path: destinationURLForFile.path)
            }catch{
                print("An error occurred while moving file to destination url")
            }
        }
    }
    // 2
    @objc func urlSession(_ session: URLSession,
                    downloadTask: URLSessionDownloadTask,
                    didWriteData bytesWritten: Int64,
                    totalBytesWritten: Int64,
                    totalBytesExpectedToWrite: Int64){
        print("asdfgh")
    }
    
    //MARK: URLSessionTaskDelegate
  /*  @objc private func urlSession(_ session: URLSession,
                    task: URLSessionTask,
                    didCompleteWithError error: Error?){
//        downloadTask = nil
        if (error != nil) {
            print(error!.localizedDescription)
             ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error", showMsg: "Cant Download Download In Process")
        }else{
            print("The task finished transferring data successfully")
        }
    }*/
    
    //MARK: UIDocumentInteractionControllerDelegate
    @objc func documentInteractionControllerViewControllerForPreview(_ controller: UIDocumentInteractionController) -> UIViewController
    {
        //UINavigationBar.appearance().barTintColor = UIColor.black
        //UINavigationBar.appearance().tintColor = UIColor.black
        //UINavigationBar.appearance().titleTextAttributes = [NSAttributedStringKey.foregroundColor : UIColor.white, NSAttributedStringKey.font: UIFont.systemFont(ofSize: 14, weight: UIFont.Weight.bold)]
        return self
    }
    
    @objc func documentInteractionControllerDidEndPreview(_ controller: UIDocumentInteractionController) {
        viewer = nil
    }
    
    //MARK: JSONConvert
    
    @objc func JSONConvert(data :[String]) -> String
    {
        var ConvertData = ""
        let x = data as [String]
        do{
            let JSONData = try JSONSerialization.data(
                withJSONObject: x ,
                options: JSONSerialization.WritingOptions(rawValue: 0))
            ConvertData = NSString(data: JSONData,
                                   encoding: String.Encoding.utf8.rawValue)! as String
        }
        catch{
            print("error in data encoding")
        }
        print(ConvertData)
        return ConvertData
    }

}

//extension UIViewController
//{
//    @objc func convertDicTostring(str:Dictionary<String, String>) -> String
//    {
//        let dictionary = str
//        let jsonData = try? JSONSerialization.data(withJSONObject: dictionary, options: [])
//        let jsonString = String(data: jsonData!, encoding: .utf8)
//        return jsonString!
//    }
//}


// Helper function inserted by Swift 4.2 migrator.
fileprivate func convertFromUIImagePickerControllerInfoKeyDictionary(_ input: [UIImagePickerController.InfoKey: Any]) -> [String: Any] {
    return Dictionary(uniqueKeysWithValues: input.map {key, value in (key.rawValue, value)})
}

// Helper function inserted by Swift 4.2 migrator.
fileprivate func convertFromUIImagePickerControllerInfoKey(_ input: UIImagePickerController.InfoKey) -> String {
    return input.rawValue
}
