//
//  ApiHandler.swift
//  MageNative Magento Platinum
//
//  Created by Hamza Usmani on 16/07/20.
//  Copyright © 2020 CEDCOSS Technologies Private Limited. All rights reserved.
//


import Foundation
import MobileCoreServices

class ApiHandler {
    
    
    static let handle = ApiHandler()
    private init() {}
    
    enum RequestType {
        case GET
        case POST
    }
    
    func request(with url: String, params: [String:String]?=nil, requestType: RequestType, controller: UIViewController, completion:(@escaping (_ json: Data?,_ error: Error?)-> Void)) {
        
        
        Alert_File.addLoadingIndicator(controller, msg: "Loading Please Wait...")
        
        
        DispatchQueue.global(qos: .background).async {
            
            let baseString      = settings.baseUrl + "rest/V1/"
            let endPoint        = baseString + url
            guard let finalUrl  = URL(string: endPoint) else {return}
            
            var stringToPost = [String:[String:String]]()
            var postJsonString  = ""
            
            var request = URLRequest(url: finalUrl)
            
            // Checking for Request Type
            
            switch requestType {
            case .GET:
                request.httpMethod    = "GET"
            default:
                request.httpMethod    = "POST"
                if let param          = params {
                    
                    stringToPost        = ["parameters":[:]]
                    for (key,value) in param
                    {
                        _ = stringToPost["parameters"]?.updateValue(value, forKey:key)
                    }
                    postJsonString      = stringToPost.convtToJson() as String
                    request.setValue("application/json", forHTTPHeaderField: "Content-Type")
                    
                    //request.setValue("application/x-www-form-urlencoded", forHTTPHeaderField: "Content-Type")
                    request.httpBody = postJsonString.data(using: String.Encoding(rawValue: String.Encoding.utf8.rawValue))
//                    if UserDefaults.standard.bool(forKey: "isLogin"){
//                        if let user = UserDefaults.standard.object(forKey: "userInfoDict") as? [String:String] {
//                            request.setValue(user["hashKey"], forHTTPHeaderField: "hashkey")
//                        }
//
//                    }
                }
            }
            
            
            print(finalUrl)
            print(postJsonString)
            
            let sessionConfig = URLSessionConfiguration.default
//            sessionConfig.httpAdditionalHeaders = [
//                "Authorization": "Basic \(Ced_CommonVendor.getAuthData())",
//            ]
            sessionConfig.timeoutIntervalForRequest = timeoutIntervalForRequest
            sessionConfig.timeoutIntervalForResource = timeoutIntervalForResource
            let session = URLSession(configuration: sessionConfig)
            session.dataTask(with: request) { (data, response, error) in
                
                if let httpResponse = response as? HTTPURLResponse, httpResponse.statusCode != 200 {
                    
                    print("status code should be 200 but is \(httpResponse.statusCode)")
                    print("Bad response is \(httpResponse)")
                    completion(data,error)
                }
                
                guard error == nil && data != nil else {
                    
                    DispatchQueue.main.async {
                        Alert_File.removeLoadingpost(controller)
                        controller.view.makeToast("Error is " + error!.localizedDescription, duration: 2.0, position: .center)
                        
                    }
                    return
                }
                
                DispatchQueue.main.async{
                        Alert_File.removeLoadingpost(controller)
                        completion(data,error)
                }
            }.resume()
            
        }
    }
    
    func requestDict(with url: String, params: [String:Any], requestType: RequestType, controller: UIViewController, completion:(@escaping (_ json: Data?,_ error: Error?)-> Void)) {
        
        DispatchQueue.main.async {
            Alert_File.addLoadingIndicator(controller, msg: "Loading Please Wait...")
        }
        
        
        DispatchQueue.global(qos: .background).async {
            
            let baseString      = settings.baseUrl + "rest/V1/"
            

            var endPoint = ""
            if(url.contains("https://")){
                endPoint = url
            }else{
                 endPoint  = baseString + url
            }
            guard let finalUrl  = URL(string: endPoint) else {return}
            print("finalUrl \(finalUrl)")
            var postJsonString  = ""
            
            var request = URLRequest(url: finalUrl)
            
            // Checking for Request Type
            
            switch requestType {
            case .GET:
                request.httpMethod    = "GET"
            default:
                request.httpMethod    = "POST"
//                if let param          = params {
//
//                    stringToPost        = ["parameters":[:]]
//                    for (key,value) in param
//                    {
//                        _ = stringToPost["parameters"]?.updateValue(value, forKey:key)
//                    }
                    postJsonString      = params.convtToJson() as String
                    request.setValue("application/json", forHTTPHeaderField: "Content-Type")
                if(url == "https://graph.facebook.com/v20.0/122095038332010689/messages"){
                    request.setValue("Bearer EAAXa7lItDpYBO84bpz10qjSzZAKcXe6YnUNgWEMsCarHKEUUOc7wFk3e4opjOESdKT1xGwguyV8yeuJu3jjfh3SvUeVOryl6jR4EZBRZAl4SUfwbgS2aGCipWTusU9tEyZBpPgjncTojB7dn6aocFw1wtQ0dnRIyLoPcdhSZAzc3ycQCVZA3L6ZBU5Blp7n2HX8LQVlTwg3fA3Ingb4", forHTTPHeaderField: "Authorization")
                }
                    //request.setValue("application/x-www-form-urlencoded", forHTTPHeaderField: "Content-Type")
                    request.httpBody = postJsonString.data(using: String.Encoding(rawValue: String.Encoding.utf8.rawValue))
//                    if UserDefaults.standard.bool(forKey: "isLogin"){
//                        if let user = UserDefaults.standard.object(forKey: "userInfoDict") as? [String:String] {
//                            request.setValue(user["hashKey"], forHTTPHeaderField: "hashkey")
//                        }
//                        
//                    }
            }
            
            
            print(finalUrl)
          //  print(postJsonString)
            
            
            
            let sessionConfig = URLSessionConfiguration.default
//            sessionConfig.httpAdditionalHeaders = [
//                "Authorization": "Basic \(Ced_CommonVendor.getAuthData())",
//            ]
            sessionConfig.timeoutIntervalForRequest = timeoutIntervalForRequest
            sessionConfig.timeoutIntervalForResource = timeoutIntervalForResource
            let session = URLSession(configuration: sessionConfig)
            session.dataTask(with: request) { (data, response, error) in
                
                if let httpResponse = response as? HTTPURLResponse, httpResponse.statusCode >= 300 {
                    
                    print("status code should be 200 but is \(httpResponse.statusCode)")
                    print("Bad response is \(httpResponse)")
                    completion(data,error)
                }
                
                guard error == nil && data != nil else {
                    
                    DispatchQueue.main.async {
                        Alert_File.removeLoadingpost(controller)
                        controller.view.makeToast("Error is " + error!.localizedDescription, duration: 2.0, position: .center)
                        
                    }
                    return
                }
                
                DispatchQueue.main.async
                    {
                        Alert_File.removeLoadingpost(controller)
                        completion(data,error)
                }
                
                
                
                
            }.resume()
            
        }
        
        
        
        
    }
    
    
    
    func createMultiImageMultiPartBody(boundary : String,_ allfileUrl:[String]) -> Data {
                var body = Data()
        //  body.append("--\(boundary)\r\n".data(using: String.Encoding.utf8)!)
        //  body.append("Content-Disposition: form-data; name=\"file[]\"; filename=\"\(fileName)\"\r\n".data(using: String.Encoding.utf8)!)
        //  body.append(data as Data)
        //  body.append("\r\n".data(using: String.Encoding.utf8)!)
        let boundaryPrefix = "--\(boundary)\r\n"
        allfileUrl.forEach { filePath in
            let url = NSURL(string: filePath)
            if let data = NSData(contentsOfFile: filePath){
                let selectedImage = UIImage(data: data as Data)
                if let data = selectedImage?.jpegData(compressionQuality: 0.2){
                    let fileName: NSString = (filePath as NSString).lastPathComponent as NSString
                    var finalName = fileName
                    let pathExtention = fileName.pathExtension
                    let pathPrefix = fileName.deletingPathExtension
                    if(pathExtention.lowercased() != "png" && pathExtention.lowercased() != "jpg"){
                        finalName = "\(pathPrefix).jpg" as NSString
                    }
                        body.append("\(boundaryPrefix)".data(using: .utf8)!)
                        body.append("Content-Disposition: form-data; name=\"file[]\"; filename=\"\(finalName)\"\r\n".data(using: .utf8)!)
                        body.append("Content-Type: \(mimeTypeForPath(path: filePath))\r\n\r\n".data(using: .utf8)!)
                        body.append(data as Data)
                        body.append("\r\n".data(using: .utf8)!)
                        print(mimeTypeForPath(path: filePath))
                }else{
                    let fileName: NSString = (filePath as NSString).lastPathComponent as NSString
                    var finalName = fileName
                    let pathExtention = fileName.pathExtension
                    let pathPrefix = fileName.deletingPathExtension
                    if(pathExtention.lowercased() != "png" && pathExtention.lowercased() != "jpg"){
                        finalName = "\(pathPrefix).jpg" as NSString
                    }
                    body.append("\(boundaryPrefix)".data(using: .utf8)!)
                    body.append("Content-Disposition: form-data; name=\"file[]\"; filename=\"\(finalName)\"\r\n".data(using: .utf8)!)
                    body.append("Content-Type: \(mimeTypeForPath(path: filePath))\r\n\r\n".data(using: .utf8)!)
                    body.append(data as Data)
                    body.append("\r\n".data(using: .utf8)!)
                    print(mimeTypeForPath(path: filePath))
                }
            }
        }
                // Just take this line out of the images loop
               // body.append("--\(boundary)--\r\n".data(using: String.Encoding.utf8)!)
        body.append("--".appending(boundary.appending("--")).data(using: .utf8)!)
                return body
    }
    
    func mimeTypeForPath(path: String) -> String {
        let url = NSURL(fileURLWithPath: path)
        let pathExtension = url.pathExtension

        if let uti = UTTypeCreatePreferredIdentifierForTag(kUTTagClassFilenameExtension, pathExtension! as NSString, nil)?.takeRetainedValue() {
            if let mimetype = UTTypeCopyPreferredTagWithClass(uti, kUTTagClassMIMEType)?.takeRetainedValue() {
                return mimetype as String
            }
        }
        return "application/octet-stream"
    }
    
    func requestDictWithForm(with url: String, boundary : String,_ allfileUrl:[String], controller: UIViewController, completion:(@escaping (_ json: Data?,_ error: Error?)-> Void)) {
        
        DispatchQueue.main.async {
            Alert_File.addLoadingIndicator(controller, msg: "Loading Please Wait...")
        }
        
        
        DispatchQueue.global(qos: .background).async {
            
            let baseString      = settings.baseUrl 
            

            var endPoint = ""
            if(url.contains("https://")){
                endPoint = url
            }else{
                 endPoint  = baseString + url
            }
            guard let finalUrl  = URL(string: endPoint) else {return}
            print("finalUrl \(finalUrl)")
            var request = URLRequest(url: finalUrl)
            
            // Checking for Request Type
            
            request.httpMethod    = "POST"
            request.setValue("multipart/form-data; boundary=\(boundary)", forHTTPHeaderField: "Content-Type")
            request.httpBody = self.createMultiImageMultiPartBody(boundary: boundary, allfileUrl)
            print(finalUrl)
            
            let sessionConfig = URLSessionConfiguration.default
//            sessionConfig.httpAdditionalHeaders = [
//                "Authorization": "Basic \(Ced_CommonVendor.getAuthData())",
//            ]
            sessionConfig.timeoutIntervalForRequest = timeoutIntervalForRequest
            sessionConfig.timeoutIntervalForResource = timeoutIntervalForResource
            let session = URLSession(configuration: sessionConfig)
            session.dataTask(with: request) { (data, response, error) in
                
                if let httpResponse = response as? HTTPURLResponse, httpResponse.statusCode >= 300 {
                    
                    print("status code should be 200 but is \(httpResponse.statusCode)")
                    print("Bad response is \(httpResponse)")
                    print(error?.localizedDescription)
                    if(data != nil){
                        print(String(decoding: data!, as: UTF8.self))
                    }
                    completion(data,error)
                }
                
                guard error == nil && data != nil else {
                    
                    DispatchQueue.main.async {
                        Alert_File.removeLoadingpost(controller)
                        controller.view.makeToast("Error is " + error!.localizedDescription, duration: 2.0, position: .center)
                        
                    }
                    return
                }
                
                DispatchQueue.main.async
                    {
                        Alert_File.removeLoadingpost(controller)
                        completion(data,error)
                }
                
                
                
                
            }.resume()
            
        }
        
        
        
        
    }

}
//protocol xyz{
//    func test(x:Int,y:Int)
//}
//extension xyz{
//    func testt(x:Int)->Int{
//        return x
//    }
//}
