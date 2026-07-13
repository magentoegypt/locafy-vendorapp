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


class Ced_CommonVendor: NSObject {
    
    
   static var storeList = [stores]()//["English","Arabic"]
    
    class func getInfoPlist(_ indexString:NSString) ->AnyObject?{
        let path = Bundle.main.path(forResource: "vendorappData", ofType: "plist")
        let storedvalues = NSDictionary(contentsOfFile: path!)
        let response: AnyObject? = storedvalues?.object(forKey: indexString) as AnyObject?
        return response
    }
    
    class func getAuthData()-> String{
        let username = "locafy"
        let password = "Market2025"
        let loginString = "\(username):\(password)"

        // 1. Encode credentials in Base64
        guard let loginData = loginString.data(using: .utf8) else { return "" }
        let base64LoginString = loginData.base64EncodedString()
        return base64LoginString
    }
    
    //Navigate Function
    
    class func getDataformjson(_ key:String)->AnyObject?{
        if let path = Bundle.main.path(forResource: "country", ofType: "json")
        {
            
            let jsonData =  try? Data(contentsOf: URL(fileURLWithPath: path))
            if let urlData = jsonData{
                let json = try! JSON(data: urlData)
                let data = json[key].arrayObject
                var i = 0
                var arrData = [String:String]()
                for value in data! {
                    
                    if(i == 0 ){
                        arrData["lat"] = value as? String
                    }else{
                        arrData["longi"] = value as? String
                    }
                    i += 1
                }
                return arrData as AnyObject?
            }
        }
        return nil
    }
    
    @objc func isRegAvail(){
        let defaults = UserDefaults.standard
        defaults.setValue("1", forKey: "cedReg")
        var url = settings.baseUrl
        url += "vendorapi/index/isRegistrationEnabled"
        let request =  NSMutableURLRequest(url: URL(string:url)!)
        let requestHeader = Ced_CommonVendor.getInfoPlist("requestheader") as! String
        request.setValue(requestHeader, forHTTPHeaderField: "Mobiconnectheader")
        request.setValue("Dalvik/2.1.0 (Linux; U; Android 5.0.2; Android SDK built for x86 Build/LSY66K)", forHTTPHeaderField: "User-Agent")
        let sessionConfig = URLSessionConfiguration.default
//        sessionConfig.httpAdditionalHeaders = [
//            "Authorization": "Basic \(Ced_CommonVendor.getAuthData())",
//        ]
        sessionConfig.timeoutIntervalForRequest = timeoutIntervalForRequest
        sessionConfig.timeoutIntervalForResource = timeoutIntervalForResource
        let session = URLSession(configuration: sessionConfig)
        let task = session.dataTask(with: request as URLRequest) { (data, response, error) in
            guard error == nil && data != nil else
            {
                print("error=\(String(describing: error))")
                return;
            }
            // check for http errors
            if let httpStatus = response as? HTTPURLResponse, httpStatus.statusCode != 200
            {
                print("statusCode should be 200, but is \(httpStatus.statusCode)")
                print("response = \(String(describing: response))")
                return;
            }
            DispatchQueue.main.async
                {
                    
                    guard let json = try? JSON(data: data!) else {return}
                    let reg = json["is_enabled"].stringValue
                    defaults.setValue(reg, forKey: "cedReg")
            }
        }
        task.resume()
    }
    
    @objc func loadData(){
        let defaults = UserDefaults.standard
        var url = settings.baseUrl
        let userData = defaults.object(forKey: "userInfoDict") as? NSDictionary
        url += "vendorapi/index/link"
        let request =  NSMutableURLRequest(url: URL(string:url)!)
        request.httpMethod = "POST"
        if(defaults.bool(forKey: "isLogin")){
            
            //print(userData)
            let vendorId = userData!["vendorId"] as! String
            let hashKey    = userData!["hashKey"] as! String
            let postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey
            print("LOADDATA")
            print(url)
            print(postString)
            
            request.httpBody = postString.data(using: String.Encoding.utf8)
        }
        print(url)
        let requestHeader = Ced_CommonVendor.getInfoPlist("requestheader") as! String
        request.setValue(requestHeader, forHTTPHeaderField: "Mobiconnectheader")
        
        
        request.setValue("Dalvik/2.1.0 (Linux; U; Android 5.0.2; Android SDK built for x86 Build/LSY66K)", forHTTPHeaderField: "User-Agent")
        let sessionConfig = URLSessionConfiguration.default
        sessionConfig.timeoutIntervalForRequest = timeoutIntervalForRequest
        sessionConfig.timeoutIntervalForResource = timeoutIntervalForResource
//        sessionConfig.httpAdditionalHeaders = [
//            "Authorization": "Basic \(Ced_CommonVendor.getAuthData())",
//           // "Content-Type": "application/json"
//        ]
        let session = URLSession(configuration: sessionConfig)
        let task = session.dataTask(with: request as URLRequest) { (data, response, error) in
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
            DispatchQueue.main.async
                {
                    guard let json = try? JSON(data: data!) else {return}
                    print("---json--\(json)")
                    var options = [String]()
                    var OrderData = [String]()
                    var Transactionsett = [String]()
                    var ReportSett = [String]()
                    var Settingsdat = [String]()
                    var attrData   = [String]()
                    var selectSell = [String]()
                    var memberShipData=[String]()
                    var messageData = [String]()
                    var cmsData = [String]()
                    var storeData = [String]()
                    let dp = json["data"]["profile_picture"].stringValue
                    UserDefaults.standard.set(dp, forKey: "ProfilePic")
                    for data in json["data"]["vendor_navigation"].arrayValue {
                        print(data)
                        
                        if(data.stringValue == "Manage Orders"){
                            OrderData.append(data.stringValue.localized)
                            OrderData.append("Cancel Orders".localized)
                        }else if(data.stringValue == "Manage Invoice"){
                            OrderData.append(data.stringValue.localized)
                        }else  if(data.stringValue == "Manage Shipment"){
                            OrderData.append(data.stringValue.localized)
                        }else  if(data.stringValue == "Manage Credit Memo"){
                            OrderData.append(data.stringValue.localized)
                        }/*else    if(data.stringValue == "Request Payments"){
                             Transactionsett.append(data.stringValue)
                         }*/else if(data.stringValue == "View Transactions"){
                            Transactionsett = [String]()
                             Transactionsett.append("Requested Transaction".localized)
                             Transactionsett.append(data.stringValue.localized)
                             Transactionsett.append("Transaction Settings".localized)
                            print("here's fetched transaction")
                            print(Transactionsett)
                        }else  if(data.stringValue == "Order Reports"){
                            ReportSett.append(data.stringValue.localized)
                        }else if(data.stringValue == "Product Reports"){
                            ReportSett.append(data.stringValue.localized)
                        }
                        else if(data.stringValue == "Plans History"){
                            memberShipData.append(data.stringValue.localized)
                        }else if(data.stringValue == "Membership Plan"){
                            memberShipData.append(data.stringValue.localized)
                        }
                        else  if(data.stringValue == "Transaction Settings"){
                            Settingsdat.append(data.stringValue.localized)
                        }else    if(data.stringValue == "Shipping Settings"){
                            Settingsdat.append(data.stringValue.localized)
                        }else    if(data.stringValue == "Table Rate Shipping"){
                            Settingsdat.append(data.stringValue.localized)
                        }else  if(data.stringValue == "Select and Sell"){
                            //selectSell.append(data.stringValue)
                        }else  if(data.stringValue == "Add Product"){
                            selectSell.append(data.stringValue.localized)
                        }else  if(data.stringValue == "Product List"){
                            selectSell.append(data.stringValue.localized)
                        }else  if(data.stringValue == "Shipping Methods"){
                            Settingsdat.append(data.stringValue.localized)
                        }
                        else  if(data.stringValue == "Manage Vendor CMS"){
                            cmsData.append("Manage Seller CMS".localized)
                        }
                        else  if(data.stringValue == "Manage Static Blocks"){
                            cmsData.append(data.stringValue.localized)
                        }
                        else  if(data.stringValue == "Vendor Admin"){
                            messageData.append(data.stringValue.localized)
                        }
                        else  if(data.stringValue == "Vendor Customer"){
                            messageData.append(data.stringValue.localized)
                        }
                        else  if(data.stringValue == "View Stores"){
                            storeData.append(data.stringValue.localized)
                        }
                            
                        else{
                            
                            if(data.stringValue == "New Product"){
                                attrData.append(data.stringValue.localized)
                            }else if(data.stringValue == "Vendor Profile"){
                                options.append("Dashboard Menu".localized)
                                options.append("Seller Profile".localized)
                            }else if(data.stringValue == "Sub-Vendor Profile"){
                                //options.append(data.stringValue)
                            }else if(data.stringValue == "Manage Products"){
                                attrData.append(data.stringValue.localized)
                            }else if(data.stringValue == "Manage Attribute"){
                                attrData.append(data.stringValue.localized)
                            }else if(data.stringValue == "Manage Attribute Set"){
                                attrData.append(data.stringValue.localized)
                            }
                            
                        }
                    }
                    //options = ["New Product","Vendor Profile","Manage Products","Manage Attribute Set"]
                    //                    defaults.set(selectSell, forKey: "cedSelectSell")
                    //                    defaults.set(attrData, forKey: "cedAttrData")
                    //                    defaults.set(options, forKey: "cedNaveitems")
                    //                    defaults.set(Settingsdat, forKey: "cedNavSett")
                    //                    defaults.set(OrderData, forKey: "cedNavOrder")
                    //                    defaults.set(ReportSett, forKey: "cedNavReport")
                    //                    defaults.set(Transactionsett, forKey: "cedNavTransact")
                    //                    defaults.set(memberShipData, forKey: "cedNavMemberShipPlans")
                    //
                    //
                    //                    print(cmsData)
                    //                    defaults.set(cmsData, forKey: "cedCMSData")
                    //                    defaults.set(messageData, forKey: "cedMessage")
                    //                    defaults.set(storeData, forKey: "cedStorepickup")
                    /*if(defaults.value(forKey: "moduleCheck") == nil)
                     {
                     defaults.set("true", forKey: "moduleCheck")
                     (UIApplication.shared.delegate as! AppDelegate).changeModules()
                     }*/
                    
                    
                    
                    if selectSell.count > 0{
                        defaults.set(selectSell, forKey: "cedSelectSell")
                    }
                    
                    if attrData.count > 0{
                        defaults.set(attrData, forKey: "cedAttrData")
                    }
                    
                    if options.count > 0{
                        defaults.set(options, forKey: "cedNaveitems")
                    }
                    
                    if Settingsdat.count > 0{
                        defaults.set(Settingsdat, forKey: "cedNavSett")
                    }
                    OrderData.append("Manage RMA Request".localized)
                    if OrderData.count > 0{
                        defaults.set(OrderData, forKey: "cedNavOrder")
                    }
                    
                    if ReportSett.count > 0{
                        defaults.set(ReportSett, forKey: "cedNavReport")
                    }
                    
                    if ReportSett.count > 0{
                        defaults.set(ReportSett, forKey: "cedNavReport")
                    }
                    
                    if Transactionsett.count > 0{
                        defaults.set(Transactionsett, forKey: "cedNavTransact")
                    }
                    
                    if cmsData.count > 0{
                        defaults.set(cmsData, forKey: "cedCMSData")
                    }
                    
                    if messageData.count > 0{
                        defaults.set(messageData, forKey: "cedMessage")
                    }
                    
                    if storeData.count > 0{
                        defaults.set(storeData, forKey: "cedStorepickup")
                    }
                NotificationCenter.default.post(name: NSNotification.Name("loadDrawerAgain"),object: nil);
                    self.getStoresData()
            }
            
        }
        task.resume()
    }
    
    @objc func getStoresData(){
       
        var url = settings.baseUrl
        url += "rest/V1/vendorapi/stores/storelist"
        let request =  NSMutableURLRequest(url: URL(string:url)!)
        request.httpMethod = "GET"
        //request.httpBody = parameters.data(using: String.Encoding.utf8)
        // print(parameters)
        let requestHeader = Ced_CommonVendor.getInfoPlist("requestheader") as! String
        request.setValue(requestHeader, forHTTPHeaderField: "Mobiconnectheader")
        print(request)
        let sessionConfig = URLSessionConfiguration.default
//        sessionConfig.httpAdditionalHeaders = [
//            "Authorization": "Basic \(Ced_CommonVendor.getAuthData())",
//        ]
        sessionConfig.timeoutIntervalForRequest = timeoutIntervalForRequest
        sessionConfig.timeoutIntervalForResource = timeoutIntervalForResource
        let session = URLSession(configuration: sessionConfig)
        let task = session.dataTask(with: request as URLRequest) { (data, response, error) in
            guard error == nil && data != nil else
            {
                print("error=\(String(describing: error))")
                return;
            }
            // check for http errors
            if let httpStatus = response as? HTTPURLResponse, httpStatus.statusCode != 200
            {
                print("statusCode should be 200, but is \(httpStatus.statusCode)")
                print("response = \(String(describing: response))")
                return;
            }
            DispatchQueue.main.async
                {
                    guard let json = try? JSON(data: data!) else {return}
                    Ced_CommonVendor.storeList.removeAll()
                    json[0]["store_data"].arrayValue.forEach{Ced_CommonVendor.storeList.append(stores(json: $0))}
            }
        }
        task.resume()
    }
    
    
    class func UIColorFromRGB(_ colorCode: String, alpha: Float = 1.0) -> UIColor {
        var colorCode = colorCode
        colorCode = colorCode.components(separatedBy: "#").last!
        let scanner = Scanner(string:colorCode)
        var color:UInt32 = 0;
        scanner.scanHexInt32(&color)
        let mask = 0x000000FF
        let r = CGFloat(Float(Int(color >> 16) & mask)/255.0)
        let g = CGFloat(Float(Int(color >> 8) & mask)/255.0)
        let b = CGFloat(Float(Int(color) & mask)/255.0)
        
        return UIColor(red: r, green: g, blue: b, alpha: CGFloat(alpha))
    }
    
    
    ///Resizing the images
    class func ResizeImage(_ image: UIImage, targetSize: CGSize) -> UIImage {
        let size = image.size
        
        let widthRatio  = targetSize.width  / image.size.width
        let heightRatio = targetSize.height / image.size.height
        
        // Figure out what our orientation is, and use that to form the rectangle
        var newSize: CGSize
        if(widthRatio > heightRatio) {
            newSize = CGSize(width: size.width * heightRatio, height: size.height * heightRatio)
        } else {
            newSize = CGSize(width: size.width * widthRatio,  height: size.height * widthRatio)
        }
        
        // This is the rect that we've calculated out and this is what is actually used below
        let rect = CGRect(x: 0, y: 0, width: newSize.width, height: newSize.height)
        
        // Actually do the resizing to the rect using the ImageContext stuff
        UIGraphicsBeginImageContextWithOptions(newSize, false, 1.0)
        image.draw(in: rect)
        let newImage = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        return newImage!
    }
    
    
    //MODULE CODE
    class func checkmoduless() {
        var url = settings.baseUrl
        url += "vendorapi/index/getmoduleList"
        let request = NSMutableURLRequest(url: URL(string:url)!)
        let requestHeader = Ced_CommonVendor.getInfoPlist("requestheader") as! String
        request.setValue(requestHeader, forHTTPHeaderField: "Mobiconnectheader")
        let sessionConfig = URLSessionConfiguration.default
//        sessionConfig.httpAdditionalHeaders = [
//            "Authorization": "Basic \(Ced_CommonVendor.getAuthData())",
//        ]
        sessionConfig.timeoutIntervalForRequest = timeoutIntervalForRequest
        sessionConfig.timeoutIntervalForResource = timeoutIntervalForResource
        let session = URLSession(configuration: sessionConfig)
        let task = session.dataTask(with: request as URLRequest) { (data, response, error) in
            guard error == nil && data != nil else
            {
                return;
            }
            
            // check for http errors
            if let httpStatus = response as? HTTPURLResponse, httpStatus.statusCode != 200
            {
                print("statusCode should be 200, but is \(httpStatus.statusCode)")
                print("response = \(String(describing: response))")
                return;
            }
            
            // code to fetch values from response :: start
            DispatchQueue.main.async
                {
                    guard let json = try? JSON(data: data!) else {return}
                    //print(NSString(data:data!,encoding:String.Encoding.utf8.rawValue)!)
                    //print(json["data"]["modules"]);
                    let moduleString = "UTJWa1gxWk5aWE56WVdkcGJtZEJjR2xEWldSZlZrMWxjM05oWjJsdVowRndhVU5sWkY5V1EyMXpRWEJwUTJWa1gxWkRiWE5CY0dsRFpXUmZWbE52WTJsaGJFeHZaMmx1UVhCcFEyVmtYMVpUYjJOcFlXeE1iMmRwYmtGd2FVTmxaRjlXVW1WMmFXVjNRWEJwUTJWa1gxWlNaWFpwWlhkQmNHbERaV1JmVmxKbGNHOXlkRUZ3YVVObFpGOVdVbVZ3YjNKMFFYQnBRMlZrWDFaQ1lXUm5aWE5CY0dsRFpXUmZWa0poWkdkbGMwRndhVU5sWkY5V1pXNWtiM0pCY0dsRFpXUmZWbVZ1Wkc5eVFYQnBRMlZrWDFaU2JXRkJjR2xEWldSZlZsSnRZVUZ3YVVObFpGOVdVMnhoUVhCcFEyVmtYMVpUYkdGQmNHbERaV1JmVms5eVpHVnlRWEJwUTJWa1gxWlBjbVJsY2tGd2FVTmxaRjlXUkdWaGJFRndhVU5sWkY5V1JHVmhiRUZ3YVVObFpGOVdRV1IyVkhKaGJuTmhZM1JwYjI1QmNHbERaV1JmVmtGa2RsUnlZVzV6WVdOMGFXOXVRWEJwUTJWa1gxWlRkRzl5WlhCcFkydDFjRUZ3YVVObFpGOVdVM1J2Y21Wd2FXTnJkWEJCY0dsRFpXUmZWa0YxWTNScGIyNUJjR2xEWldSZlZrRjFZM1JwYjI1QmNHbERaV1JmVmxCdlFYQnBRMlZrWDFaUWIwRndhVU5sWkY5V1RXVnRZbVZ5YzJocGNFRndhVU5sWkY5V1RXVnRZbVZ5YzJocGNFRndhVU5sWkY5V1UzVmlRV05qYjNWdWRFRndhVU5sWkY5V1UzVmlRV05qYjNWdWRFRndhVU5sWkY5V1VISnZaSFZqZEVGd2FVTmxaRjlXVUhKdlpIVmpkRUZ3YVVObFpGOVdVbVp4UVhCcFEyVmtYMVpTWm5GQmNHbERaV1JmVmxCeWIyUjFZM1JCZEhSeWFXSjFkR1ZCY0dsRFpXUmZWbEJ5YjJSMVkzUkJkSFJ5YVdKMWRHVkJjR2xEWldSZlZrMTFiSFJwVTJWc2JHVnlRWEJwUTJWa1gxWk5kV3gwYVZObGJHeGxja0Z3YVE9PW1vYmljb25uZWN0c2VjcmV0aGFzaA=="
                    let data =  Data(base64Encoded: moduleString, options:   NSData.Base64DecodingOptions(rawValue: 0))
                    let headerkey =  Ced_CommonVendor.getInfoPlist("requestheader") as! String
                    
                    let string =  NSString(data: data!, encoding: String.Encoding.utf8.rawValue)?.replacingOccurrences(of: headerkey, with: "")
                    
                    let againdecode = Data(base64Encoded: string!, options:   NSData.Base64DecodingOptions(rawValue: 0))
               //     let savedModulearray =  NSString(data: againdecode!, encoding: String.Encoding.utf8.rawValue)?.replacingOccurrences(of: headerkey, with: "").components(separatedBy: ",")
                    
                    let activeModules = NSMutableArray()
                    let getmoduleString = NSMutableDictionary()
                    let defaults = UserDefaults.standard
                    print("MODULESMODULESMODULES")
                    print(json)
                    let savedModulearray = ["Ced_VPoApi","Ced_VRfqApi","Ced_VMultiSellerApi","Ced_VRmaApi","Ced_VDealApi","Ced_VCmsApi","Ced_VAdvTransactionApi","Ced_VBadgesApi","Ced_VReportApi","Ced_VMessagingApi","Ced_VStorepickupApi","Ced_VReviewApi","Ced_VProductAttributeApi","Ced_VendorApi","Ced_VProductApi","Ced_VOrderApi","Ced_VSlaApi","Ced_VMembershipApi","Ced_VAuctionApi","Ced_VSubAccountApi","Ced_VProductFilterApi","Ced_VPromotionApi","Ced_VPincodeApi",]
                    print(savedModulearray)
                    for (key,value) in json["data"]["modules"]{
                        getmoduleString[key] = value.stringValue
                        for string in savedModulearray {
                            if  string.contains(value.stringValue){
                                activeModules.add(value.stringValue)
                                defaults.set(true, forKey: key)
                            }else{
                                defaults.set(false, forKey: key)
                            }
                        }
                        
                    }
                    let data1 = NSKeyedArchiver.archivedData(withRootObject: activeModules)
                    
                    UserDefaults.standard.set(data1, forKey: "activeModules")
                    UserDefaults.standard.synchronize();
                    (UIApplication.shared.delegate as! AppDelegate).changeModules()
                    //defaults.set(activeModules, forKey: "activeModules")
                    //Ced_CommonVendor().loadData();
                    
                    
            }
            
        }
        task.resume()
        
    }
    
    @objc func checkModule(_ string:String)->Bool{
        
        if let data2 = UserDefaults.standard.object(forKey: "activeModules") as? Data {
            if let mod = NSKeyedUnarchiver.unarchiveObject(with: data2) as? NSMutableArray
            {
                print(mod)
                if(mod != nil){
                    if  mod.contains(string){
                        return true
                    }else{
                        return false
                    }
                }else{
                    if string == "Ced_VProductApi" {
                        return true
                    }
                    return false
                }
                // In here you can access your array
            }
        }
        return false
        
        
    }
    
    
    
    @objc func loadModules(){
        // let  moduleurl = "http://192.168.0.217/training/m2_2/vendorapi/index/getmoduleList"
        let  moduleurl = "https://www.zeomarket.com/vendorapi/index/getmoduleList"
        let request = NSMutableURLRequest(url: URL(string:moduleurl)!)
        print("http://192.168.0.217/training/m2_2/vendorapi/index/getmoduleList")
        let sessionConfig = URLSessionConfiguration.default
//        sessionConfig.httpAdditionalHeaders = [
//            "Authorization": "Basic \(Ced_CommonVendor.getAuthData())",
//        ]
        sessionConfig.timeoutIntervalForRequest = timeoutIntervalForRequest
        sessionConfig.timeoutIntervalForResource = timeoutIntervalForResource
        let session = URLSession(configuration: sessionConfig)
        let task = session.dataTask(with: request as URLRequest) { (data, response, error) in
            guard error == nil && data != nil else
            {
                return;
            }
            
            // check for http errors
            if let httpStatus = response as? HTTPURLResponse, httpStatus.statusCode != 200
            {
                DispatchQueue.main.async {
                    print("statusCode should be 200, but is \(httpStatus.statusCode)")
                    print("response = \(response)")
                    return;
                }
                
            }
            DispatchQueue.main.async {
                //code to fetch values from response :: start
                guard let json = try? JSON(data: data!) else {return}
                print("jsonjsonjsonjson")
                print(json)
                print(json["data"]["modules"]);
                var moduleString = String()
                for (key,value) in json["data"]["modules"]{
                    moduleString += key+value.stringValue
                }
                let headerkey =  Ced_CommonVendor.getInfoPlist("requestheader") as! String
                
                moduleString = moduleString.data(using: String.Encoding.utf8)!.base64EncodedString(options: NSData.Base64EncodingOptions(rawValue: 0))
                
                let headerAppendkey = moduleString + headerkey
                
                let encodedstring = headerAppendkey.data(using: String.Encoding.utf8)!.base64EncodedString(options: NSData.Base64EncodingOptions(rawValue: 0))
                print("DoubleEncoded Stringdksh")
                print(encodedstring)
                let data =  Data(base64Encoded: encodedstring, options:   NSData.Base64DecodingOptions(rawValue: 0))
                
                let string =  NSString(data: data!, encoding: String.Encoding.utf8.rawValue)?.replacingOccurrences(of: headerkey, with: "")
                
                let againdecode = Data(base64Encoded: string!, options:   NSData.Base64DecodingOptions(rawValue: 0))
                print("--------fd----")
                print( NSString(data: againdecode!, encoding: String.Encoding.utf8.rawValue)?.replacingOccurrences(of: headerkey, with: ""))
            }
        }
        task.resume()
        
    }
    
    class func delay(_ delay:Double, closure:@escaping ()->()) {
        DispatchQueue.main.asyncAfter(
            deadline: DispatchTime.now() + Double(Int64(delay * Double(NSEC_PER_SEC))) / Double(NSEC_PER_SEC), execute: closure)
    }
    
    
    
    
}

func print(_ items: Any..., separator: String = " ", terminator: String = "\n") {
    let debug = Ced_CommonVendor.getInfoPlist("APP_DEBUG") as? String
    if(debug == "true"){
        Swift.print(items[0], separator:separator, terminator: terminator)
    }
    let layoutdebug = Ced_CommonVendor.getInfoPlist("LAYOUT_DEBUG") as? String
    if(layoutdebug == "false"){
        UserDefaults.standard.setValue(false, forKey: "_UIConstraintBasedLayoutLogUnsatisfiable")
    }
    
    
}
extension UIView
{
    @objc func makeCard(_ view:UIView,cornerRadius:CGFloat,color:UIColor,shadowOpacity:Float){
        let cornerRadius: CGFloat = cornerRadius
        let shadowColor: UIColor? = color
        let shadowOpacity: Float = shadowOpacity
        view.layer.masksToBounds = false
        view.layer.shadowOffset = CGSize.zero
        view.layer.shadowColor = shadowColor?.cgColor
        view.layer.shadowOpacity = shadowOpacity
        view.layer.shadowRadius = cornerRadius
        view.layer.cornerRadius = cornerRadius
        
    }
    
    
    @objc func makeCardUsingThemeColor(_ view:UIView,cornerRadius:CGFloat,color:UIColor,shadowOpacity:Float){
        
        let colorString = Ced_CommonVendor.getInfoPlist("themecolor") as? String;
        let theme_color = Ced_CommonVendor.UIColorFromRGB(colorString!);
        
        let cornerRadius: CGFloat = cornerRadius
        let shadowColor: UIColor? = theme_color
        let shadowOpacity: Float = shadowOpacity
        view.layer.masksToBounds = false
        view.layer.shadowOffset = CGSize.zero
        view.layer.shadowColor = shadowColor?.cgColor
        view.layer.shadowOpacity = shadowOpacity
        view.layer.shadowRadius = cornerRadius
        view.layer.cornerRadius = cornerRadius
        view.layer.borderColor = shadowColor?.cgColor
        view.layer.borderWidth = CGFloat(2);
        
    }
}

extension UIImage {

    func maskWithColor(color: UIColor) -> UIImage? {
        let maskImage = cgImage!

        let width = size.width
        let height = size.height
        let bounds = CGRect(x: 0, y: 0, width: width, height: height)

        let colorSpace = CGColorSpaceCreateDeviceRGB()
        let bitmapInfo = CGBitmapInfo(rawValue: CGImageAlphaInfo.premultipliedLast.rawValue)
        let context = CGContext(data: nil, width: Int(width), height: Int(height), bitsPerComponent: 8, bytesPerRow: 0, space: colorSpace, bitmapInfo: bitmapInfo.rawValue)!

        context.clip(to: bounds, mask: maskImage)
        context.setFillColor(color.cgColor)
        context.fill(bounds)

        if let cgImage = context.makeImage() {
            let coloredImage = UIImage(cgImage: cgImage)
            return coloredImage
        } else {
            return nil
        }
    }

}

extension UIView{
    public func cardView(){
        self.layer.masksToBounds = false
        self.layer.shadowOffset = CGSize(width: 0, height: 0)
        self.layer.shadowColor = UIColor.black.cgColor
        self.layer.shadowOpacity = 0.3
        self.layer.cornerRadius = 2
        
    }
    
}
extension UIView{
    @objc func setThemeColor(){
        let colorString = Ced_CommonVendor.getInfoPlist("subthemecolor") as? String
        let color = Ced_CommonVendor.UIColorFromRGB(colorString!)
        self.backgroundColor = color;
    }
}
extension UILabel{
    @objc func setCustomFont(_ fontString:String){
        self.font = UIFont(name: fontString, size: self.font.pointSize)
    }
    
}
// MARK: XIBLocalizable
public protocol XIBLocalizable {
    var localeKey: String? { get set }
}

extension UILabel: XIBLocalizable {
    @IBInspectable public var localeKey: String? {
        get { return nil }
        set(key) {
            text = key?.localized
        }
    }
}

extension UIButton: XIBLocalizable {
    @IBInspectable public var localeKey: String? {
        get { return nil }
        set(key) {
            setTitle(key?.localized, for: .normal)
        }
    }
    
    private func actionHandler(action:(() -> Void)? = nil) {
            struct __ { static var action :(() -> Void)? }
            if action != nil { __.action = action }
            else { __.action?() }
        }
        @objc private func triggerActionHandler() {
            self.actionHandler()
        }
        func actionHandler(controlEvents control :UIControl.Event, ForAction action:@escaping () -> Void) {
            self.actionHandler(action: action)
            self.addTarget(self, action: #selector(triggerActionHandler), for: control)
        }
}

extension UITextField: XIBLocalizable {
    @IBInspectable public var localeKey: String? {
        get { return nil }
        set(key) {
            placeholder = key?.localized
        }
    }
}

extension UINavigationController {
    func pushToViewController(_ viewController: UIViewController, animated:Bool = true, completion: @escaping ()->()) {
        CATransaction.begin()
        CATransaction.setCompletionBlock(completion)
        self.pushViewController(viewController, animated: animated)
        CATransaction.commit()
    }
    
    func popViewController(animated:Bool = true, completion: @escaping ()->()) {
        CATransaction.begin()
        CATransaction.setCompletionBlock(completion)
        self.popViewController(animated: true)
        CATransaction.commit()
    }
    
    func popToViewController(_ viewController: UIViewController, animated:Bool = true, completion: @escaping ()->()) {
        CATransaction.begin()
        CATransaction.setCompletionBlock(completion)
        self.popToViewController(viewController, animated: animated)
        CATransaction.commit()
    }
    
    func popToRootViewController(animated:Bool = true, completion: @escaping ()->()) {
        CATransaction.begin()
        CATransaction.setCompletionBlock(completion)
        self.popToRootViewController(animated: animated)
        CATransaction.commit()
    }
}
