//
//  AdditionalFieldsViewController.swift
//  VenderApp
//
//  Created by MacMini on 20/02/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit


class AdditionalFieldsViewController: ced_VendorBaseClass, UINavigationControllerDelegate, UIImagePickerControllerDelegate, UIDocumentInteractionControllerDelegate,UIDocumentMenuDelegate,UIDocumentPickerDelegate{


    @IBOutlet weak var saveButton: UIButton!
    
    @IBOutlet weak var topLabel: UILabel!
    @IBOutlet weak var stackView: UIStackView!
    @IBOutlet weak var stackViewWidth: NSLayoutConstraint!
    @IBOutlet weak var stackViewHeight: NSLayoutConstraint!
    
    var dataPdf = ""
    var store_fields=[[String:JSON]]()
    var business_fields=[[String:JSON]]()
    var bank_fields=[[String:JSON]]()
    var imageString = [String:String]()
    var viewsData=[[String:JSON]]()
    var textField = false
    var dropdown = false
    var images = false
    var textview = false
    var address_proof=[[String:JSON]]()
    var businessStateArray = [String:String]()
    var businessCityArray = [String:String]()
    var bankStateArray = [String:String]()
    var bankCityArray = [String:String]()
    var textFieldArray=[String:Any]()
    var textViewArray=[String:Any]()
    var dropdownViewArray=[String:Any]()
    var browseViewArray=[String:Any]()
    var topLabelText=""
    var json:JSON!
    var dropDownReferenceDict=[String:String]()
    var counter=0
    var counterImage = 0
    let picker = UIImagePickerController()
    var selectedBrowseView=browseImageView()
    var dropDownDataArray = [String:[String:String]]()
    var requiredArray = [String:String]()
    var downloadLinks = [Int:String]()
    var businessStateButton: UIButton!;
    var businessCityButton: UIButton!;
    var bankStateButton: UIButton!;
    var bankCityButton: UIButton!;
    
    override func viewDidLoad() {
        super.viewDidLoad()
        saveButton.setTitle("Save".localized, for: .normal)
        saveButton.addTarget(self, action: #selector(saveButtonPressed(_:)), for: .touchUpInside)
         let color = Ced_CommonVendor.UIColorFromRGB("#ff0000");
        saveButton.backgroundColor = color
        saveButton.tintColor = UIColor.white
        saveButton.layer.cornerRadius = 2
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        let postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey
        print("!!!@@@###$$$%%%^^^&&&***((())))____++++}}{{")
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
        self.sendRequest(url: "vendorapi/additional/fields", parameters: postString)
        
        // Do any additional setup after loading the view.
    }

    
    
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        if let data = data{
            do{
                
                json = try JSON(data: data)
                print(json)
                if requestUrl=="vendorapi/index/update"
                {
                    
                }
                
                if requestUrl=="vendorapi/additional/fields"{
                    store_fields=[[String:JSON]]()
                    business_fields=[[String:JSON]]()
                    bank_fields=[[String:JSON]]()
                    if let storefields=json["store_fields"].array{
                        for item in storefields{
                            store_fields.append(item.dictionary!)
                        }
                    }
                    if let storefields=json["business_fields"].array{
                        for item in storefields{
                            business_fields.append(item.dictionary!)
                        }
                    }
                    if let storefields=json["bank_fields"].array{
                        for item in storefields{
                            bank_fields.append(item.dictionary!)
                        }
                    }
                    
                    if topLabelText=="Business Details!".localized{
                        viewsData=business_fields
                    }
                    else if topLabelText=="Bank Details!".localized{
                        viewsData=bank_fields
                    }
                    else if topLabelText=="Store Details!".localized{
                        viewsData=store_fields
                    }
                    
                    
                    
                    topLabel.text=" "+topLabelText
                    stackViewWidth.constant=self.view.frame.width-20
                    stackViewHeight.constant=0
                    stackView.spacing=5
                    for item in viewsData{
                        let label=UILabel(frame: CGRect(x: 0, y: 0, width: stackViewWidth.constant, height: 40))
                        
                        stackView.addArrangedSubview(label)
                        
                        stackView.distribution = .equalSpacing
                        stackView.spacing=5
                        stackViewHeight.constant+=45
                        label.text=item["key"]?.stringValue
                        if item["type"]=="text" {
                            let tf=UITextField(frame: CGRect(x: 0, y: 0, width: stackViewWidth.constant, height: 40))
                            tf.borderStyle = .roundedRect
                            tf.text = item["saved_value"]?.stringValue
                            if(item["input_type"]?.stringValue == "int")
                            {
                                tf.keyboardType = .numberPad;
                            }
                            textFieldArray.updateValue(tf, forKey: (item["value"]?.stringValue)!)
                            requiredArray[(item["value"]?.stringValue)!] = item["is_required"]?.stringValue;
                            stackView.addArrangedSubview(tf)
                            stackViewHeight.constant+=45
                        }
                        else if item["type"]=="textarea"{
                            let tv=UITextView(frame: CGRect(x: 0, y: 0, width: stackViewWidth.constant, height:60))
                            textViewArray.updateValue(tv, forKey: (item["value"]?.stringValue)!)
                            tv.text = item["saved_value"]?.stringValue;
                            tv.font = UIFont(name: "Roboto-Bold", size: 18)
                            tv.layer.borderWidth=1
                            tv.layer.borderColor=UIColor.lightGray.cgColor
                            tv.isScrollEnabled = false
                            if(item["input_type"]?.stringValue == "int")
                            {
                                tv.keyboardType = .numberPad;
                            }
                            requiredArray[(item["value"]?.stringValue)!] = item["is_required"]?.stringValue;
                            stackView.addArrangedSubview(tv)
                            stackViewHeight.constant+=65
                        }
                        else if item["type"]=="drop_down"{
                            let dropdownView=DropDownButtonView()
                            dropdownView.frame=CGRect(x: 0, y: 0, width: stackViewWidth.constant, height: 30)
                            dropdownViewArray.updateValue(dropdownView, forKey: (item["value"]?.stringValue)!)
                            dropdownView.dropDownButton.tag=counter
                            requiredArray[(item["value"]?.stringValue)!] = item["is_required"]?.stringValue;
                            var dropArray = [String:String]()
                            if(item["value"]?.stringValue == "business_city")
                            {
                                businessCityButton=dropdownView.dropDownButton;
                                businessCityArray = [String:String]();
                            }
                            else if(item["value"]?.stringValue == "business_state")
                            {
                                businessStateButton = dropdownView.dropDownButton;
                                
                            }
                            else if(item["value"]?.stringValue == "bank_city")
                            {
                                bankCityButton=dropdownView.dropDownButton;
                                bankCityArray = [String:String]();
                            }
                            else if(item["value"]?.stringValue == "bank_state")
                            {
                                bankStateButton = dropdownView.dropDownButton;
                                
                            }
                            if let dropfields=json[(item["value"]?.stringValue)!].array{
                                for index in dropfields
                                {
                                    dropArray[index["key"].stringValue] = index["value"].stringValue;
                                    if(item["value"]?.stringValue == "business_city")
                                    {
                                        businessCityArray[index["key"].stringValue] = index["value"].stringValue;
                                    }
                                    else if(item["value"]?.stringValue == "bank_city")
                                    {
                                        bankCityArray[index["key"].stringValue] = index["value"].stringValue
                                    }
                                    else if(item["value"]?.stringValue == "bank_state")
                                    {
                                        bankStateArray[index["key"].stringValue] = index["value"].stringValue
                                    }
                                    else if(item["value"]?.stringValue == "business_state")
                                    {
                                        businessStateArray[index["key"].stringValue] = index["value"].stringValue
                                    }
                                    if(item["saved_value"]?.stringValue == index["value"].stringValue)
                                    {
                                        dropdownView.dropDownButton.setTitle(index["key"].stringValue, for: UIControl.State())
                                    }
                                    
                                }
                            }
                            dropDownDataArray[(item["value"]?.stringValue)!] = dropArray
                            
                            dropDownReferenceDict.updateValue((item["value"]?.stringValue)!, forKey: String(counter))
                            counter+=1
                            dropdownView.dropDownButton.addTarget(self, action: #selector(dropDownButtonPressed(_:)), for: .touchUpInside)
                            stackView.addArrangedSubview(dropdownView)
                            stackViewHeight.constant+=35
                        }else if item["type"]=="image" || item["type"]=="file"{
                            let browseView=browseImageView()
                            browseView.frame=CGRect(x: 0, y: 0, width: stackViewWidth.constant, height: 40)
                            requiredArray[(item["value"]?.stringValue)!] = item["is_required"]?.stringValue;
                            let imgRequest =  URL(string: (item["saved_value"]?.stringValue)!)
                            browseView.browseImage.tag = counterImage
                            
                            if(item["type"] == "image")
                            {
                                browseView.browseImage.sd_setImage(with: imgRequest, placeholderImage: UIImage(named: "placeholder"))
                                browseView.browseImage.isHidden = false;
                                browseView.nameLabel.isHidden = true;
                                browseView.fileAttachedLabel.isHidden = true;
                                
                            }
                            else if(item["type"] == "file")
                            {
                                browseView.browseImage.isHidden = true;
                                browseView.nameLabel.isHidden = false;
                                browseView.fileAttachedLabel.isHidden = true;
                                browseView.nameLabel.tag = counterImage + 5555;
                                downloadLinks[counterImage + 5555] = item["saved_value"]?.stringValue
                                browseView.nameLabel.addTarget(self, action: #selector(downloadClicked(_:)), for: .touchUpInside)
                                //browseView.nameLabel.text = item["saved_value"]?.stringValue
                            }
                            
                            browseViewArray.updateValue(browseView, forKey: (item["value"]?.stringValue)!)
                            browseView.browseButton.tag=counterImage;
                            
                            //ImageReferenceDict.updateValue((item["value"]?.stringValue)!, forKey: String(counterImage))
                            
                            browseView.browseButton.layer.borderColor=UIColor.darkGray.cgColor
                            
                            browseView.browseButton.addTarget(self, action: #selector(self.browseButtonPressed(sender: )), for: .touchUpInside)
                            browseView.browseButton.layer.borderWidth=1
                            counterImage += 1;
                            stackView.addArrangedSubview(browseView)
                            stackViewHeight.constant+=45
                        }
                        
                    }
                    print("**********")
                    print(stackViewHeight.constant)
                    print(stackView.arrangedSubviews.count)
                    Alert_File.removeLoadingIndicator(self);
                }
            }catch let error {
                print(error.localizedDescription)
            }
        }
        
    }
    
    @objc func downloadClicked(_ sender: UIButton)
    {
        print("---tag--")
        print(sender.tag)
        if let link = downloadLinks[sender.tag]
        {
            let viewControl = storyboard?.instantiateViewController(withIdentifier: "ced_WebView") as! ced_WebView
            viewControl.pageUrl = link;
            self.navigationController?.pushViewController(viewControl, animated: true)
        }
    }
    
    @objc func saveButtonPressed(_ sender: UIButton){
        
      
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        var postDict=["vendor_id":vendorId,"hashkey":hashKey]
        var postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey
        print("-----")
        print(postString)
        for (key,_) in textFieldArray
        {
            print(key)
            if let text1 = textFieldArray[key] as? UITextField
            {
                if text1.text == ""
                {
                    if(requiredArray[key] == "true")
                    {
                        self.view.makeToast("Please Select ".localized + "\(key)", duration: 2.0, position: .center)
                        return;
                    }
                    textField = true
                }
                else
                {
                 textField = false
                    if key == "business_pin_no"
                    {
                        if !self.isValidNumber(Number: text1.text!)
                        {
                            self.view.showToastMsg("Please enter correct Pin Code.".localized)
                            return
                        }
                        else if text1.text!.count < 6
                        {
                            self.view.showToastMsg("Please enter correct Pin Code.".localized);
                            return
                        }
                    }
                    postDict[key]=text1.text!
                    postString += "&"+key+"="+text1.text!
                }
                
            }
            
        }
        for (key,_) in textViewArray
        {
            if let text1 = textViewArray[key] as? UITextView
            {
                if text1.text == ""
                {
                    if(requiredArray[key] == "true")
                    {
                        self.view.makeToast("Please Select ".localized + "\(key)", duration: 2.0, position: .center)
                        return;
                    }
                    textview = true
                }
                else
                {
                    textview = false
                    postDict[key]=text1.text!
                    postString += "&"+key+"="+text1.text!
                }
                
            }
            
        }

        for (key,_) in dropdownViewArray
        {
            if let text1 = dropdownViewArray[key] as? DropDownButtonView
            {
                if text1.dropDownButton.currentTitle == "--Please Select--"
                {
                    if(requiredArray[key] == "true")
                    {
                        self.view.makeToast("Please Select ".localized + "\(key)", duration: 2.0, position: .center)
                        return;
                    }
                     dropdown = true
                }
                else
                {
                     dropdown = false
                    if(key == "business_city")
                    {
                        if let val = businessCityArray[text1.dropDownButton.currentTitle!]
                        {
                            postDict[key]=val
                            postString += "&"+key+"="+val
                        }
                    }
                    else if(key == "bank_city")
                    {
                        if let val = bankCityArray[text1.dropDownButton.currentTitle!]
                        {
                            postDict[key]=val
                            postString += "&"+key+"="+val
                        }
                    }
                    else
                    {
                        if let dropArray = dropDownDataArray[key]{
                            if let val = dropArray[text1.dropDownButton.currentTitle!]
                            {
                                postDict[key]=val
                                postString += "&"+key+"="+val
                            }
                            
                        }
                    }
                    
                    
                }
            }
        }
        print("---browseview--\(browseViewArray)")
        for (key,_) in browseViewArray
        {
            if  let browseView = browseViewArray[key] as? browseImageView
            {
                print("---\(key)")
                print(key)
                images = false
                if browseView.browseButton.isHidden == true{
                    let chat_file2=browseView.base64EncodedStringData
                    if(chat_file2 != "")
                    {
                        // print(chat_file2)
                        let dictionary2 = ["type":"file","name":"\(key).pdf","base64_encoded_data":chat_file2]
                        let thumbnailImage=dictionary2.convtToJson()
                        postDict[key]=(thumbnailImage as String)
                        postString += "&"+key+"="+(thumbnailImage as String)
                    }
                    
                }
                else
                {
                    let chat_file2=browseView.base64EncodedStringData
                    if(chat_file2 != "")
                    {
                        // print(chat_file2)
                        let dictionary2 = ["type":"file","name":"\(key).png","base64_encoded_data":chat_file2]
                        let thumbnailImage=dictionary2.convtToJson()
                        postDict[key]=(thumbnailImage as String)
                        postString += "&"+key+"="+(thumbnailImage as String)
                    }
                    
                }
                
            }
        }
        
        self.senddata(parameters: postString)
        
    }
    
    @objc func senddata(parameters: String)
    {
        
        var baseUrl = settings.baseUrl
        baseUrl += "vendorapi/index/update"
        let postString = parameters
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
        var request =  URLRequest(url: URL(string:baseUrl)!)
        request.httpMethod = "POST"
        print("PPP")
        print(postString)
        
        
        
        
        let requestHeader = Ced_CommonVendor.getInfoPlist("requestheader") as! String
        request.setValue(requestHeader, forHTTPHeaderField: "Mobiconnectheader")
        //request.setValue("multipart/form-data", forHTTPHeaderField: "Content-Type")
        request.httpBody = postString.data(using: String.Encoding.utf8)
        let sessionConfig = URLSessionConfiguration.default
//        sessionConfig.httpAdditionalHeaders = [
//            "Authorization": "Basic \(Ced_CommonVendor.getAuthData())",
//        ]
        sessionConfig.timeoutIntervalForRequest = timeoutIntervalForRequest
        sessionConfig.timeoutIntervalForResource = timeoutIntervalForResource
        let session = URLSession(configuration: sessionConfig)
        let task = session.dataTask(with: request){
            data,response,error in
            
            guard error == nil && data != nil else
            {
                DispatchQueue.main.async
                    {
                        Alert_File.removeLoadingIndicator(self)
                    ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error".localized, showMsg: error!.localizedDescription)
                }
                print("error=\(error)")
                return;
            }
            // check for http errors
            if let httpStatus = response as? HTTPURLResponse, httpStatus.statusCode != 200
            {
                print("statusCode should be 200, but is \(httpStatus.statusCode)")
                print("response = \(response)")
                DispatchQueue.main.async
                    {
                        print(NSString(data: data!, encoding: String.Encoding.utf8.rawValue))
                        Alert_File.removeLoadingIndicator(self)
                }
                return;
            }
            DispatchQueue.main.async
            {
                if let data = data{
                    guard let json = try? JSON(data: data) else {return}
                    print(json)
                    print(NSString(data: data, encoding: String.Encoding.utf8.rawValue))
                    Alert_File.removeLoadingIndicator(self)
                    if json["data"]["success"].stringValue=="true"{
                        ShowSimpleAlert.showSimpleAlert(self, showTitle: "", showMsg: json["data"]["message"].stringValue)
                    }
                    else
                    {
                        ShowSimpleAlert.showSimpleAlert(self, showTitle: "", showMsg: json["data"]["message"].stringValue)
                    }
                }
            }
        }
        task.resume()
    }
    @objc func dropDownButtonPressed(_ sender: UIButton){
        let data = String(sender.tag)
        let value=dropDownReferenceDict[data]
        
        var dropDownOption=[[String:JSON]]()
        var keys=[String]()
        var values=[String]()
        let dropDown = DropDown();
        if(sender == businessCityButton)
        {
            keys = Array(businessCityArray.keys)
        }
        else if(sender == bankCityButton)
        {
            keys = Array(bankCityArray.keys)
        }
        else
        {
            if let storefields=json[value!].array{
                for item in storefields{
                    dropDownOption.append(item.dictionary!)
                }
            }
            
            for item in dropDownOption{
                keys.append((item["key"]?.stringValue)!)
                values.append((item["value"]?.stringValue)!)
            }
        }
        
        dropDown.dataSource = keys;
        
        dropDown.selectionAction = {(index, item) in
            sender.setTitle(item, for: UIControl.State());
            if(sender == self.businessStateButton)
            {
                self.businessCityButton.setTitle("", for: .normal)
                self.fetchCitiesInsideCountry(self.businessStateArray[item]!, cityItem: "", sender: sender)
            }
            else if(sender == self.bankStateButton)
            {
                self.bankCityButton.setTitle("", for: .normal)
                self.fetchCitiesInsideCountry(self.bankStateArray[item]!, cityItem: "", sender: sender)
            }
            //self.changeLineChart(item)
        }
        
        dropDown.anchorView = sender
        dropDown.bottomOffset = CGPoint(x: 0, y:sender.bounds.height)
        if dropDown.isHidden {
            dropDown.show();
        } else {
            dropDown.hide();
        }
    }
    @objc func image_picker()
    {
        print("browseButtonPressed")
        picker.delegate=self
        picker.allowsEditing = false
        picker.sourceType = .photoLibrary
        self.present(picker, animated: true, completion: nil)
    }
    
    @objc func browseButtonPressed(sender:UIButton)
    {
        if let browseView=sender.superview?.superview as? browseImageView{
            selectedBrowseView=browseView
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
            let uploadpdf = UIAlertAction(title: "upload pdf".localized, style: .default)
            { _ in
                print("upload pdf")
                self.browseButtonPressed()
            }
            if let popoverController = actionSheetControllerIOS8.popoverPresentationController {
                popoverController.sourceView = self.view
                popoverController.sourceRect = CGRect(x: self.view.bounds.midX, y: self.view.bounds.midY, width: 0, height: 0)
                popoverController.permittedArrowDirections = []
            }
            actionSheetControllerIOS8.addAction(uploadpdf)
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
        
        let imageURL = info[convertFromUIImagePickerControllerInfoKey(UIImagePickerController.InfoKey.referenceURL)] as! NSURL
        let imageName = imageURL.lastPathComponent
        //self.selectedBrowseView.nameLabel.text=""
        self.selectedBrowseView.nameLabel.isHidden = true;
        self.selectedBrowseView.browseImage.isHidden = false;
        self.selectedBrowseView.fileAttachedLabel.isHidden = true;
        self.selectedBrowseView.browseImage.image=chosenImage
        self.selectedBrowseView.base64EncodedStringData=(chosenImage.pngData()?.base64EncodedString())!//(UIImageJPEGRepresentation(chosenImage, 0.0)?.base64EncodedString())!
        picker.dismiss(animated:true, completion: nil)
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
        let isSecuredURL = url.startAccessingSecurityScopedResource() == true
        let coordinator = NSFileCoordinator()
        var error: NSError? = nil
        coordinator.coordinate(readingItemAt: url, options: [], error: &error) { (url) -> Void in
            do{
                let temp=try Data(contentsOf: url)
                let temp1=fileName[fileName.count-1].components(separatedBy: ".")
                let check=fileName[fileName.count-1].contains(".pdf")
                print(temp1)
                if !check
                {
                    let msg = "The file you have choosed is not in correct format".localized
                    print(msg)
                    ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error".localized, showMsg: msg)
                    return
                }
                else
                {
                    self.selectedBrowseView.browseImage.isHidden = true;
                    self.selectedBrowseView.nameLabel.isHidden = true;
                     self.selectedBrowseView.fileAttachedLabel.isHidden = false;
                    self.selectedBrowseView.fileAttachedLabel.text = temp1[0]
                    self.dataPdf = temp.base64EncodedString()
                    self.selectedBrowseView.base64EncodedStringData=temp.base64EncodedString()
                    
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
    @objc func JSONConvert(data :[String]) -> String {
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
extension AdditionalFieldsViewController{
    
    @objc func fetchCitiesInsideCountry(_ item:String, cityItem: String = "", sender: UIButton)
    {
        
        if(item != "")
        {
            var baseURL = settings.baseUrl
            baseURL += "rest/V1/mobiconnect/module/getcity/\(item)";
            
            print(baseURL);
            
            var request = URLRequest(url: URL(string: baseURL)!);
            
            request.httpMethod = "GET";
            
            
            let requestHeader = Ced_CommonVendor.getInfoPlist("requestheader") as! String
            //request.setValue(requestHeader, forHTTPHeaderField: "Mobiconnectheader")
            
            Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
            
            let sessionConfig = URLSessionConfiguration.default
//            sessionConfig.httpAdditionalHeaders = [
//                "Authorization": "Basic \(Ced_CommonVendor.getAuthData())",
//            ]
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
                        ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error".localized, showMsg: error!.localizedDescription)
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
                            guard let jsonData = try? JSON(data: data) else {return}
                            print("jsonData[\"data\"]DDDEEEVVV");
                            print(jsonData);
                            if(sender == self.businessStateButton)
                            {
                                self.businessCityArray = [String:String]()
                                for city in jsonData[0]["cities"].arrayValue
                                {
                                    self.businessCityArray[city["label"].stringValue] = city["city_id"].stringValue;
                                }
                            }
                            else if(sender == self.bankStateButton)
                            {
                                self.bankCityArray = [String:String]()
                                for city in jsonData[0]["cities"].arrayValue
                                {
                                    self.bankCityArray[city["label"].stringValue] = city["city_id"].stringValue;
                                }
                            }
                            else if(sender == self.bankCityButton)
                            {
                                self.bankCityArray = [String:String]()
                                for city in jsonData[0]["cities"].arrayValue
                                {
                                    self.bankCityArray[city["label"].stringValue] = city["city_id"].stringValue;
                                    if(cityItem == city["city_id"].stringValue)
                                    {
                                        sender.setTitle(city["label"].stringValue, for: .normal);
                                    }
                                }
                            }
                            else if(sender == self.businessCityButton)
                            {
                                self.businessCityArray = [String:String]()
                                for city in jsonData[0]["cities"].arrayValue
                                {
                                    self.businessCityArray[city["label"].stringValue] = city["city_id"].stringValue;
                                    if(cityItem == city["city_id"].stringValue)
                                    {
                                        sender.setTitle(city["label"].stringValue, for: .normal);
                                    }
                                }
                            }
                            
                            /*if(jsonData["success"].stringValue == "false")
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
                             }*/
                        }
                }
            })
            
            task.resume();
        }
        
        
        
    }
    
    @objc func fetchStatesInsideCountry(_ item:String, sender: UIButton)
    {
        if(item != "")
        {
            let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
            let vendorId = userData["vendorId"] as! String
            let hashKey    = userData["hashKey"] as! String
            
            var baseURL = settings.baseUrl
            baseURL += "vendorapi/index/getcountry/";
            
            print(baseURL);
            
            var request = URLRequest(url: URL(string: baseURL)!);
            
            request.httpMethod = "POST";
            var postString = "vendor_id="+vendorId+"&hashkey="+hashKey;
            postString += "&country_code="+"IN";
            
            print(postString);
            
            request.httpBody = postString.data(using: String.Encoding.utf8);
            
            let requestHeader = Ced_CommonVendor.getInfoPlist("requestheader") as! String
            request.setValue(requestHeader, forHTTPHeaderField: "Mobiconnectheader")
            
            Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
            
            let sessionConfig = URLSessionConfiguration.default
//            sessionConfig.httpAdditionalHeaders = [
//                "Authorization": "Basic \(Ced_CommonVendor.getAuthData())",
//            ]
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
                        ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error".localized, showMsg: error!.localizedDescription)
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
                            guard let jsonData = try? JSON(data: data) else {return}
                            print("jsonData[\"data\"]DDDEEEVVV");
                            print(jsonData);
                            if(sender == self.bankStateButton)
                            {
                                for val in jsonData["states"].arrayValue
                                {
                                    self.bankStateArray[val["default_name"].stringValue] = val["region_id"].stringValue;
                                    if(val["region_id"].stringValue == item)
                                    {
                                        sender.setTitle(val["default_name"].stringValue, for: .normal);
                                    }
                                }
                                
                            }
                            else if(sender == self.businessStateButton)
                            {
                                for val in jsonData["states"].arrayValue
                                {
                                    self.businessStateArray[val["default_name"].stringValue] = val["region_id"].stringValue;
                                    if(val["region_id"].stringValue == item)
                                    {
                                        sender.setTitle(val["default_name"].stringValue, for: .normal);
                                    }
                                }
                            }
                            
                            /*if(jsonData["success"].stringValue == "false")
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
                             }*/
                        }
                }
            })
            
            task.resume();
        }
        
        
    }
}

// Helper function inserted by Swift 4.2 migrator.
fileprivate func convertFromUIImagePickerControllerInfoKeyDictionary(_ input: [UIImagePickerController.InfoKey: Any]) -> [String: Any] {
	return Dictionary(uniqueKeysWithValues: input.map {key, value in (key.rawValue, value)})
}

// Helper function inserted by Swift 4.2 migrator.
fileprivate func convertFromUIImagePickerControllerInfoKey(_ input: UIImagePickerController.InfoKey) -> String {
	return input.rawValue
}
