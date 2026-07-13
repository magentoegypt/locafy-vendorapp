//
//  cedAssignedQuotationView.swift
//  VenderApp
//
//  Created by cedcoss on 17/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class cedAssignedQuotationView: ced_VendorBaseClass, UITableViewDelegate, UITableViewDataSource, URLSessionDownloadDelegate {

    @IBOutlet weak var mainButtonview: UIView!
    
    @IBOutlet weak var radioButtonview: UIView!
    
    @IBOutlet weak var deleteButton: UIButton!
    
    @IBOutlet weak var disapproveButton: UIButton!
    
    @IBOutlet weak var saveButton: UIButton!
    
    @IBOutlet weak var quotationDetailButton: UIButton!
    
    @IBOutlet weak var productUrlButton: UIButton!
    @IBOutlet weak var negotiationButton: UIButton!
    
    @IBOutlet weak var invoiceButton: UIButton!
    
    @IBOutlet weak var commentsButton: UIButton!
    var quantity = "";
    var price = "";
    var origin = "";
    var quality = "";
    var packing = "";
    var validity = "";
    var remarks = "";
    var comment = "";
    var agreedQty = "";
    var productUrl = "";
    var agreedPrice = "";
    var buttonOptionsArray = [UIButton]();
    @IBOutlet weak var mainTable: UITableView!
    var quotationData = [String:String]();
    var chatData = [[String:String]]();
    var invoiceData = [String:String]();
    var negotiationData = [String:String]();
    var productData = [String:String]()
    var selectedPage = "Quotations Details"
    var quoteId = "";
    lazy var downloadsSession: URLSession = {
        let configuration = URLSessionConfiguration.default
//        configuration.httpAdditionalHeaders = [
//            "Authorization": "Basic \(Ced_CommonVendor.getAuthData())",
//        ]
        let session = URLSession(configuration: configuration, delegate: self, delegateQueue: nil)
        
        return session
    }()
    
    var activeDownloads = [String: Download]()
    var filename: String!
    var imageData = [String]();
    
    override func viewDidLoad() {
        super.viewDidLoad()
        mainTable.delegate = self;
        mainTable.dataSource = self;
        mainButtonview.cardView();
        radioButtonview.cardView();
        saveButton.backgroundColor = DynamicColor.secondaryColor//setThemeColor()
        deleteButton.setThemeColor()
        disapproveButton.setThemeColor()
        saveButton.addTarget(self, action: #selector(saveButtonClicked(_:)), for: .touchUpInside)
        deleteButton.addTarget(self, action: #selector(deleteClicked(_:)), for: .touchUpInside)
        disapproveButton.addTarget(self, action: #selector(disapproveClicked(_:)), for: .touchUpInside);
        quotationDetailButton.addTarget(self, action: #selector(detailButtonClicked(_:)), for: .touchUpInside)
        negotiationButton.addTarget(self, action: #selector(detailButtonClicked(_:)), for: .touchUpInside)
        invoiceButton.addTarget(self, action: #selector(detailButtonClicked(_:)), for: .touchUpInside)
        commentsButton.addTarget(self, action: #selector(detailButtonClicked(_:)), for: .touchUpInside)
        productUrlButton.addTarget(self, action: #selector(detailButtonClicked(_:)), for: .touchUpInside)
        buttonOptionsArray.append(quotationDetailButton);
        buttonOptionsArray.append(negotiationButton);
        buttonOptionsArray.append(invoiceButton);
        buttonOptionsArray.append(commentsButton);
        buttonOptionsArray.append(productUrlButton)
        loadData();
        // Do any additional setup after loading the view.
    }

    @objc func detailButtonClicked(_ sender: UIButton)
    {
        for button in buttonOptionsArray
        {
            if(button.currentTitle == sender.currentTitle)
            {
                sender.setImage(UIImage(named: "checked_checkbox"), for: .normal);
                selectedPage = sender.currentTitle!;
                if let quantityTextField = self.view.viewWithTag(10000) as? UITextField
                {
                    quantity = quantityTextField.text!;
                }
                if let priceTextField = self.view.viewWithTag(10001) as? UITextField
                {
                    price = priceTextField.text!;
                }
                if let originTextField = self.view.viewWithTag(10002) as? UITextField
                {
                    origin = originTextField.text!;
                }
                if let qualityTextField = self.view.viewWithTag(10003) as? UITextField
                {
                    quality = qualityTextField.text!;
                }
                if let packingTextField = self.view.viewWithTag(10004) as? UITextField
                {
                    packing = packingTextField.text!;
                }
                if let validityTextField = self.view.viewWithTag(10005) as? UITextField
                {
                    validity = validityTextField.text!;
                }
                if let remarksTextField = self.view.viewWithTag(10006) as? UITextField
                {
                    remarks = remarksTextField.text!;
                }
                if let commentTextView = self.view.viewWithTag(10007) as? UITextView
                {
                    comment = commentTextView.text!;
                }
                if let sku = self.view.viewWithTag(10008) as? UITextField
                {
                    productUrl = sku.text!;
                }
                if let agreedqty = self.view.viewWithTag(10009) as? UITextField
                {
                    agreedQty = agreedqty.text!;
                }
                if let agreedprice = self.view.viewWithTag(10010) as? UITextField
                {
                    agreedPrice = agreedprice.text!;
                }
                mainTable.reloadData();
            }
            else
            {
                button.setImage(UIImage(named: "unchecked_checkbox"), for: .normal)
            }
        }
    }
    
    @objc func loadData()
    {
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        let postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey + "&id=" + quoteId;
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        self.sendRequest(url: "vpoapi/assign/view", parameters: postString)
    }
    
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        Alert_File.removeLoadingIndicator(self);
        if let data = data
        {
            guard let json = try? JSON(data: data) else {return}
            print(json)
            if(requestUrl == "vpoapi/assign/view")
            {
                if(json["data"]["success"].stringValue == "true")
                {
                    let productInfo = json["data"]["quotations_details"]
                    quotationData["product_name"] = productInfo["product_name"].stringValue;
                    quotationData["document"] = productInfo["document"].stringValue;
                    quotationData["color"] = productInfo["color"].stringValue;
                    quotationData["qty"] = productInfo["qty"].stringValue;
                    quotationData["price"] = productInfo["price"].stringValue;
                    quotationData["store_url"] = productInfo["store_url"].stringValue;
                    quotationData["item_url"] = productInfo["item_url"].stringValue;
                    quotationData["customer_name"] = productInfo["customer_name"].stringValue;
                    quotationData["customer_email"] = productInfo["customer_email"].stringValue;
                    
                    for image in productInfo["image"].arrayValue
                    {
                        imageData.append(image.stringValue);
                    }
                    let productDetail = json["data"]["product_details"];
                    productData["agreed_price"] = productDetail["agreed_price"].stringValue;
                    productData["product_url"] = productDetail["product_url"].stringValue;
                    productData["agreed_qty"] = productDetail["agreed_qty"].stringValue;
                    let negotiationDetail = json["data"]["negotation_details"];
                    negotiationData["price"] = negotiationDetail["price"].stringValue;
                    negotiationData["qty"] = negotiationDetail["qty"].stringValue;
                    if(json["data"]["chat_details"].exists())
                    {
                        for chat in json["data"]["chat_details"].arrayValue
                        {
                            chatData.append(["sent_by":chat["sent_by"].stringValue,"chat_date":chat["chat_date"].stringValue,"chat_message":chat["chat_message"].stringValue])
                        }
                    }
                    let invoice = json["data"]["invoice_details"];
                    invoiceData["origin"] = invoice["origin"].stringValue;
                    invoiceData["quality"] = invoice["quality"].stringValue;
                    invoiceData["packing"] = invoice["packing"].stringValue;
                    invoiceData["validity"] = invoice["validity"].stringValue;
                    invoiceData["remarks"] = invoice["remarks"].stringValue;
                }
                else
                {
                   // Alert_File.showValidationError(self,title:"Error",message:json["data"]["message"].stringValue);
                    self.mainTable.setEmptyMessage(json["data"]["message"].stringValue)
                    return
                }
                mainTable.restore()
                mainTable.reloadData();
            }
            else if(requestUrl == "vpoapi/assign/save")
            {
                if(json["data"]["success"].stringValue == "true")
                {
                    Alert_File.showValidationError(self,title:"Success",message:json["data"]["message"].stringValue);
                }
                else
                {
                    Alert_File.showValidationError(self,title:"Error",message:json["data"]["message"].stringValue);
                }
            }
            else if(requestUrl == "vpoapi/assign/disapprove")
            {
                if(json["data"]["success"].stringValue == "true")
                {
                    Alert_File.showValidationError(self,title:"Success",message:json["data"]["message"].stringValue);
                }
                else
                {
                    Alert_File.showValidationError(self,title:"Error",message:json["data"]["message"].stringValue);
                }
            }
            else
            {
                if(json["data"]["success"].stringValue == "true")
                {
                    Alert_File.showValidationError(self,title:"Success",message:json["data"]["message"].stringValue);
                }
                else
                {
                    Alert_File.showValidationError(self,title:"Error",message:json["data"]["message"].stringValue);
                }
            }
            
        }
    }
    
    @objc func numberOfSections(in tableView: UITableView) -> Int {
        if(quotationData.count>0)
        {
            if(selectedPage == "Quotations Details")
            {
                return 3
            }
            else if(selectedPage == "Negotiation Section")
            {
                return 1;
            }
            else if(selectedPage == "Invoice Form Field")
            {
                return 1;
            }
            else if(selectedPage == "Comments")
            {
                return 2;
            }
            else if(selectedPage == "Product Url")
            {
                return 1;
            }
        }
        return 0;
    }
    
    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if(selectedPage == "Comments")
        {
            if(section == 1)
            {
                return chatData.count;
            }
        }
        return 1;
    }
    
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if(selectedPage == "Quotations Details")
        {
            if(indexPath.section == 0)
            {
                if let cell = tableView.dequeueReusableCell(withIdentifier: "productNameCell") as? cedPurchaseQuotationCell
                {
                    cell.productNameLabel.text = quotationData["product_name"];
                    return cell;
                }
            }
            else if(indexPath.section == 1)
            {
                if let cell = tableView.dequeueReusableCell(withIdentifier: "cedPurchaseImageCell") as? cedPurchaseImageCell
                {
                    cell.imageArray = imageData;
                    cell.reloadData();
                    return cell;
                }
            }
            else if(indexPath.section == 2)
            {
                if let cell = tableView.dequeueReusableCell(withIdentifier: "footerCell") as? cedPurchaseQuotationCell
                {
                    cell.customerName.text = quotationData["customer_name"];
                    cell.customerEmail.text = quotationData["customer_email"];
                    
                    cell.requestedUnitPrice.text = quotationData["price"];
                    cell.requestedQty.text = quotationData["qty"];
                    cell.storeUrlButton.setTitle(quotationData["store_url"], for: .normal);
                    cell.itemUrlButton.setTitle(quotationData["item_url"], for: .normal);
                    if(quotationData["document"] == "")
                    {
                        cell.downloadButton.isHidden = true;
                    }
                    cell.downloadButton.setThemeColor()
                    cell.downloadButton.addTarget(self, action: #selector(ClickTodownload(sender:)), for: .touchUpInside)
                    return cell;
                }
            }
        }
        else if(selectedPage == "Negotiation Section")
        {
            if let cell = tableView.dequeueReusableCell(withIdentifier: "quotationCell", for: indexPath) as? cedPurchaseQuotationCell
            {
                if(quantity == "")
                {
                    cell.quantity.text = negotiationData["qty"];
                    
                    quantity = negotiationData["qty"]!;
                }
                else
                {
                    cell.quantity.text = quantity;
                }
                if(price == "")
                {
                    cell.unitPrice.text = negotiationData["price"];
                    price = negotiationData["price"]!;
                }
                else
                {
                    cell.unitPrice.text = price;
                }
                cell.quantity.tag = 10000;
                cell.unitPrice.tag = 10001;
                cell.quotationCellView.cardView();
                return cell;
            }
        }
        else if(selectedPage == "Invoice Form Field")
        {
            if let cell = tableView.dequeueReusableCell(withIdentifier: "invoiceCell", for: indexPath) as? cedPurchaseQuotationCell
            {
                cell.originTextField.text = invoiceData["origin"];
                if(origin == "")
                {
                    cell.originTextField.text = invoiceData["origin"];
                    
                    origin = invoiceData["origin"]!;
                }
                else
                {
                    cell.originTextField.text = origin;
                }
                if(quality == "")
                {
                    cell.qualityTextField.text = invoiceData["quality"];
                    quality = invoiceData["quality"]!;
                }
                else
                {
                    cell.qualityTextField.text = quality;
                }
                if(packing == "")
                {
                    cell.packingTextField.text = invoiceData["packing"];
                    packing = invoiceData["packing"]!;
                }
                else
                {
                    cell.packingTextField.text = packing;
                }
                if(validity == "")
                {
                    cell.validityTextField.text = invoiceData["validity"];
                    validity = invoiceData["validity"]!;
                }
                else
                {
                    cell.validityTextField.text = validity;
                }
                if(remarks == "")
                {
                    cell.remarksTextField.text = invoiceData["remarks"];
                    remarks = invoiceData["remarks"]!;
                }
                else
                {
                    cell.remarksTextField.text = remarks;
                }
                cell.originTextField.tag = 10002;
                cell.qualityTextField.tag = 10003;
                cell.packingTextField.tag = 10004;
                cell.validityTextField.tag = 10005;
                cell.remarksTextField.tag = 10006;
                cell.invoiceCellView.cardView();
                return cell;
            }
        }
        else if(selectedPage == "Comments")
        {
            if(indexPath.section == 0)
            {
                if let cell = tableView.dequeueReusableCell(withIdentifier: "commentsCell", for: indexPath) as? cedPurchaseQuotationCell
                {
                    if(comment == "")
                    {
                        cell.commentTextView.text = comment;
                        
                    }
                    
                    cell.commentTextView.tag = 10007;
                    return cell;
                }
            }
            else
            {
                if let cell = tableView.dequeueReusableCell(withIdentifier: "cedChatHistoryCell", for: indexPath) as? cedChatHistoryCell
                {
                    cell.dateLabel.text = chatData[indexPath.row]["chat_date"];
                    
                    cell.chatLabel.text = chatData[indexPath.row]["chat_message"];
                    cell.chatHistoryCellView.cardView()
                    return cell;
                }
            }
        }
        else if(selectedPage == "Product Url")
        {
            if(indexPath.section == 0)
            {
                if let cell = tableView.dequeueReusableCell(withIdentifier: "productUrlCell", for: indexPath) as? cedPurchaseQuotationCell
                {
                    if(agreedQty == "")
                    {
                        cell.agreedQty.text = productData["agreed_qty"];
                    }
                    else
                    {
                        cell.agreedQty.text = agreedQty;
                    }
                    if(agreedPrice == "")
                    {
                        cell.agreedUnitPrice.text = productData["agreed_price"];
                    }
                    else
                    {
                        cell.agreedUnitPrice.text = agreedPrice
                    }
                    if(productUrl == "")
                    {
                        cell.productSku.text = productData["product_url"];
                    }
                    else
                    {
                        cell.productSku.text = productUrl;
                    }
                    cell.productSku.tag = 10008;
                    cell.agreedQty.tag = 10009;
                    cell.agreedUnitPrice.tag = 10010;
                    return cell;
                }
            }
        }
        return UITableViewCell();
    }
    
    @objc func ClickTodownload(sender:UIButton){
        let urlString = quotationData["document"];
        let url = URL(string: urlString!)
        var request = URLRequest(url:url!)
        request.httpMethod = "GET"
        let download = Download(url: urlString!,file: quotationData["document"]!)
        
        self.filename = quotationData["document"]
        download.downloadTask = self.downloadsSession.downloadTask(with: request)
        download.downloadTask?.resume()
        download.isDownloading = true
        let bgCView = UIView();
        bgCView.tag=151;
        bgCView.frame = UIScreen.main.bounds;
        bgCView.autoresizingMask = [UIView.AutoresizingMask.flexibleHeight , UIView.AutoresizingMask.flexibleWidth];
        bgCView.backgroundColor = UIColor(red:0, green:0, blue:0, alpha:0.5);
        let filterbyView = ced_downloadView();
        filterbyView.tag = 181;
        filterbyView.progressView.progress = 0
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        filterbyView.toplabel.backgroundColor = color;
        filterbyView.backgroundColor = UIColor.black;
        filterbyView.frame = CGRect(x:20, y:20, width:self.view.frame.width - 40, height:200)
        filterbyView.center = self.view.center
        bgCView.addSubview(filterbyView)
        self.view.addSubview(bgCView);
        
        /*let task = session.dataTask(with: request, completionHandler: {
         data,response,error in
         
         if (error == nil) {
         // Success
         let statusCode = (response as! HTTPURLResponse).statusCode
         print("Success: \(statusCode)")
         
         // This is your file-variable:
         // data
         DispatchQueue.main.async{
         cedMageLoaders.removeLoadingIndicator(me: self)
         
         }
         
         }
         else {
         // Failure
         //print("Failure: %@", error?.localizedDescription);
         }
         })
         task.resume()*/
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        self.view.viewWithTag(151)?.removeFromSuperview()
    }
    
    @objc func urlSession(_ session: URLSession, downloadTask: URLSessionDownloadTask, didFinishDownloadingTo location: URL) {
        if let originalURL = downloadTask.originalRequest?.url?.absoluteString,
            let destinationURL = localFilePathForUrl(previewUrl: originalURL) {
            print("-\(originalURL)-")
            let baseUrl =  destinationURL.deletingLastPathComponent
            print("--dest--\(destinationURL)")
            let newurl =  baseUrl?.appendingPathComponent(filename)
            //print("--\(newurl)")
            DispatchQueue.main.async
                {
                    let view =  self.view.viewWithTag(181) as? ced_downloadView
                    view?.progressView.progress = 1
                    view?.downloadLabel.text = "Download Complete"
                    self.view.makeToast(self.filename + " Download Complete.", duration: 1.5, position: .center, title: nil, image: nil, style: nil, completion: nil)
                    
                    
            }
            // 2
            let fileManager = FileManager.default
            do {
                try fileManager.removeItem(at: newurl!)
            } catch {
                // Non-fatal: file probably doesn't exist
            }
            do {
                try fileManager.copyItem(at: location, to: newurl!)
                print(location)
            } catch let error as NSError {
                print("Could not copy file to disk: \(error.localizedDescription)")
            }
        }
        
        
    }
    
    @objc func urlSession(_ session: URLSession, downloadTask: URLSessionDownloadTask, didWriteData bytesWritten: Int64, totalBytesWritten: Int64, totalBytesExpectedToWrite: Int64) {
        if let downloadUrl = downloadTask.originalRequest?.url?.absoluteString,
            let download = activeDownloads[downloadUrl] {
            // 2
            download.progress = Float(totalBytesWritten)/Float(totalBytesExpectedToWrite)
            DispatchQueue.main.async
                {
                    let view =  self.view.viewWithTag(181) as? ced_downloadView
                    view?.progressView.progress =  download.progress
                    view?.downloadLabel.text = "Downloading..."
            }
        }
        
    }
    
    @objc func localFilePathForUrl(previewUrl: String) -> NSURL? {
        let documentsPath = NSSearchPathForDirectoriesInDomains(.documentDirectory, .userDomainMask, true)[0] as NSString
        if let url = NSURL(string: previewUrl), let lastPathComponent = url.lastPathComponent {
            let fullPath = documentsPath.appendingPathComponent(lastPathComponent)
            return NSURL(fileURLWithPath:fullPath)
        }
        return nil
    }
    
    
    @objc func localFileExistsForTrack(track: String?) -> Bool {
        if let urlString = track, let localUrl = localFilePathForUrl(previewUrl: urlString) {
            var isDir : ObjCBool = false
            if let path = localUrl.path {
                return FileManager.default.fileExists(atPath: path, isDirectory: &isDir)
            }
        }
        return false
    }
    
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if(selectedPage == "Quotations Details")
        {
            if(indexPath.section == 0)
            {
                return 50;
            }
            else if(indexPath.section == 1)
            {
                return 150;
            }
            else if(indexPath.section == 2)
            {
                return 290;
            }
        }
        else if(selectedPage == "Negotiation Section")
        {
            return 140;
        }
        else if(selectedPage == "Invoice Form Field")
        {
            return 250;
        }
        else if(selectedPage == "Comments")
        {
            if(indexPath.section == 0)
            {
                return 170;
            }
            else
            {
                return 130;
            }
        }
        else if(selectedPage == "Product Url")
        {
            return 170;
        }
        return 0
    }
    
    @objc func saveButtonClicked(_ sender: UIButton)
    {
        if let qty = self.view.viewWithTag(10000) as? UITextField, let unitPrice = self.view.viewWithTag(10001) as? UITextField
        {
            quantity = qty.text!
            price = unitPrice.text!;
        }
        if let originTextField = self.view.viewWithTag(10002) as? UITextField, let qualityTextField = self.view.viewWithTag(10003) as? UITextField, let packingTextField = self.view.viewWithTag(10004) as? UITextField, let validityTextField = self.view.viewWithTag(10005) as? UITextField, let remarksTextField = self.view.viewWithTag(10006) as? UITextField
        {
            origin = originTextField.text!;
            quality = qualityTextField.text!;
            packing = packingTextField.text!;
            validity = validityTextField.text!;
            remarks = remarksTextField.text!;
        }
        if let commentTextView = self.view.viewWithTag(10007) as? UITextView{
            comment = commentTextView.text;
        }
        if let sku = self.view.viewWithTag(10008) as? UITextField, let agreedqty = self.view.viewWithTag(10009) as? UITextField, let agreedprice = self.view.viewWithTag(10010) as? UITextField
        {
            productUrl = sku.text!;
            agreedQty = agreedqty.text!;
            agreedPrice = agreedprice.text!;
        }
        if(origin == "" || quality == "" || packing == "" || validity == "" || remarks == "" || productUrl == "" || agreedQty == "" || agreedPrice == "")
        {
            ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error", showMsg: "Required fields are blank")
            return;
        }
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        var postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey
        postString += "&id=" + quoteId
        postString += "&nprice=" + price
        postString += "&nqty=" + quantity;
        postString += "&origin=" + origin;
        postString += "&quality=" + quality;
        postString += "&packing=" + packing;
        postString += "&validity=" + validity;
        postString += "&remarks=" + remarks;
        postString += "&comments=" + comment;
        postString += "&product_url=" + productUrl;
        postString += "&agreed_qty=" + agreedQty;
        postString += "&agreed_price=" + agreedPrice;
        print(postString)
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        self.sendRequest(url: "vpoapi/assign/save", parameters: postString)
    }
    
    @objc func disapproveClicked(_ sender: UIButton)
    {
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        var postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey
        postString += "&id=" + quoteId
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        self.sendRequest(url: "vpoapi/assign/disapprove", parameters: postString)
    }
    
    @objc func deleteClicked(_ sender: UIButton)
    {
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        var postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey
        postString += "&quote_id=" + quoteId
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        self.sendRequest(url: "vpoapi/quotations/deletequote", parameters: postString)
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
