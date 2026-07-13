//
//  cedPurchaseViewQuotations.swift
//  VenderApp
//
//  Created by cedcoss on 16/01/18.
//  Copyright © 2018 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class cedPurchaseViewQuotations: ced_VendorBaseClass, UITableViewDelegate, UITableViewDataSource,URLSessionDownloadDelegate {

    @IBOutlet weak var deleteButton: UIButton!
    
    @IBOutlet weak var saveButton: UIButton!
    
    @IBOutlet weak var segmentview: UISegmentedControl!
    
    var selectedPage = "Quotation Details"
    var quantity = "";
    var price = "";
    @IBOutlet weak var mainTable: UITableView!
    
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
    
    var quotationData = [String:String]();
    var imageData = [String]();
    var quoteId = "";
    var page = 1;
    override func viewDidLoad() {
        super.viewDidLoad()
        mainTable.delegate = self;
        mainTable.dataSource = self;
        saveButton.addTarget(self, action: #selector(saveClicked(_:)), for: .touchUpInside);
        deleteButton.addTarget(self, action: #selector(deleteClicked(_:)), for: .touchUpInside);
        saveButton.backgroundColor = DynamicColor.secondaryColor//setThemeColor()
        deleteButton.setThemeColor()
        loadData();
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        segmentview.tintColor = Ced_CommonVendor.UIColorFromRGB(colorString!)
        // Do any additional setup after loading the view.
    }
    
    @objc func loadData()
    {
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        let postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey + "&quote_id=" + quoteId;
        print(postString)
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        self.sendRequest(url: "vpoapi/quotations/viewquote", parameters: postString)
    }
    
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        Alert_File.removeLoadingIndicator(self);
        if let data = data{
            guard let json = try? JSON(data: data) else {return}
            print(json)
            if(requestUrl == "vpoapi/quotations/viewquote")
            {
                if(json["data"]["success"].stringValue == "true")
                {
                    let productInfo = json["data"]["quotation_info"]
                    quotationData["product_name"] = productInfo["product_name"].stringValue;
                    quotationData["document"] = productInfo["document"].stringValue;
                    quotationData["color"] = productInfo["color"].stringValue;
                    quotationData["qty"] = productInfo["qty"].stringValue;
                    quotationData["price"] = productInfo["price"].stringValue;
                    quotationData["store_url"] = productInfo["store_url"].stringValue;
                    quotationData["item_url"] = productInfo["item_url"].stringValue;
                    quotationData["customer_name"] = productInfo["customer_name"].stringValue;
                    quotationData["customer_email"] = productInfo["customer_email"].stringValue;
                    quotationData["vendor_price"] = productInfo["vendor_price"].stringValue;
                    quotationData["vendor_quantity"] = productInfo["vendor_quantity"].stringValue;
                    for image in productInfo["images"].arrayValue
                    {
                        imageData.append(image.stringValue);
                    }
                    
                }
                else
                {
                   // Alert_File.showValidationError(self,title:"Error",message:json["data"]["message"].stringValue);
                    self.mainTable.setEmptyMessage(json["data"]["message"].stringValue)
                    return
                }
                mainTable.restore()
                mainTable.reloadData()
            }
            else if(requestUrl == "vpoapi/quotations/savequote")
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
            else if(requestUrl == "vpoapi/quotations/deletequote")
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
        if(quotationData.count > 0)
        {
            if(selectedPage == "Quotation Details")
            {
                return 3;
            }
            return 1;
        }
        return 0;
        
    }
    
    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 1;
    }
    
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if(selectedPage == "Quotation Details")
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
        else
        {
            if let cell = tableView.dequeueReusableCell(withIdentifier: "quotationCell") as? cedPurchaseQuotationCell
            {
                if(quantity == "")
                {
                    cell.quantity.text = quotationData["vendor_quantity"];
                    quantity = quotationData["vendor_quantity"]!;
                }
                else
                {
                    cell.quantity.text = quantity;
                }
                if(price == "")
                {
                    cell.unitPrice.text = quotationData["vendor_price"];
                    price = quotationData["vendor_price"]!;
                }
                else
                {
                    cell.unitPrice.text = price;
                }
                cell.quantity.tag = 10000;
                cell.unitPrice.tag = 10001;
                return cell;
            }
        }
        return UITableViewCell()
    }
    
    @IBAction @objc func segmentValueChanged(_ sender: UISegmentedControl) {
        if(sender.selectedSegmentIndex == 0)
        {
            if let qty = self.view.viewWithTag(10000) as? UITextField, let unitPrice = self.view.viewWithTag(10001) as? UITextField
            {
                quantity = qty.text!;
                price = unitPrice.text!;
            }
            selectedPage = "Quotation Details";
            mainTable.reloadData();
        }
        else
        {
            selectedPage = "Send Your Quotations";
            mainTable.reloadData();
        }
    }
    
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if(selectedPage == "Quotation Details")
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
        return 140;
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
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

    @objc func saveClicked(_ sender: UIButton)
    {
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        var postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey
        postString += "&quote_id=" + quoteId
        if let qty = self.view.viewWithTag(10000) as? UITextField, let unitPrice = self.view.viewWithTag(10001) as? UITextField
        {
            if(qty.text == "" || unitPrice.text == "")
            {
                ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error", showMsg: "Required fields are blank")
                return;
            }
            
            postString += "&vendor_quantity=" + qty.text!
            postString += "&vendor_price=" + unitPrice.text!;
            
        }
        else
        {
            if(quantity == "" || price == "")
            {
                ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error", showMsg: "Required fields are blank")
                return;
            }
            postString += "&vendor_quantity=" + quantity
            postString += "&vendor_price=" + price;
        }
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        self.sendRequest(url: "vpoapi/quotations/savequote", parameters: postString)
       
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
    
    
    
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
