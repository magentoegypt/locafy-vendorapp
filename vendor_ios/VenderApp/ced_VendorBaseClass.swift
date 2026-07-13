//
//  ced_VendorBaseClass.swift
//  VenderApp
//
//  Created by CEDCOSS Technologies Private Limited on 19/11/16.
//  Copyright © 2016 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit
let timeoutIntervalForRequest:Double = 60.0
let timeoutIntervalForResource:Double = 60.0
class ced_VendorBaseClass: UIViewController {
    let defaults = UserDefaults.standard
    
    
    var userInfoDict = [String:String]();
    var scrollView: UIScrollView! //main scrollview to provide scrolling option to stackview
    var stackViewTop: UIStackView! // stackview as core wrapperview
    var paddingth = CGFloat(10);
    
    
    let screenSizee: CGRect = UIScreen.main.bounds
    
    
    let floatButtonRadius = CGFloat(50);
    var loaderView : UIView!;

    @objc func showRequestLoader(){
        
        loaderView = UIView();
        loaderView.backgroundColor = UIColor.blue;
        self.view.addSubview(loaderView);
    }
    
    @objc func removeRequestLoader(){
        loaderView.removeFromSuperview();
    }
    
    
    override func viewDidAppear(_ animated: Bool) {
        ced_navigationBarController().addNavButton(self,str:"no")
        NotificationCenter.default.post(name: Notification.Name(rawValue: "refreshNotification"), object: nil);
    }
    
    @objc func basicFoundationToRenderView(_ bottomMargin:CGFloat){
        
        
        // adding scrollview
        scrollView = UIScrollView();
        scrollView.translatesAutoresizingMaskIntoConstraints = false;
        scrollView.showsHorizontalScrollIndicator = false;
        scrollView.showsVerticalScrollIndicator = false;
        view.addSubview(scrollView);
        self.view.addConstraint(NSLayoutConstraint(item: scrollView, attribute: NSLayoutConstraint.Attribute.leading, relatedBy: NSLayoutConstraint.Relation.equal, toItem: self.view, attribute: NSLayoutConstraint.Attribute.leading, multiplier: 1, constant: 0));
        self.view.addConstraint(NSLayoutConstraint(item: scrollView, attribute: NSLayoutConstraint.Attribute.trailing, relatedBy: NSLayoutConstraint.Relation.equal, toItem: self.view, attribute: NSLayoutConstraint.Attribute.trailing, multiplier: 1, constant: 0));
        self.view.addConstraint(NSLayoutConstraint(item: scrollView, attribute: NSLayoutConstraint.Attribute.top, relatedBy: NSLayoutConstraint.Relation.equal, toItem: self.view, attribute: NSLayoutConstraint.Attribute.top, multiplier: 1, constant: 0));
        self.view.addConstraint(NSLayoutConstraint(item: scrollView, attribute: NSLayoutConstraint.Attribute.bottom, relatedBy: NSLayoutConstraint.Relation.equal, toItem: self.view, attribute: NSLayoutConstraint.Attribute.bottom, multiplier: 1, constant: -(bottomMargin)));
        
        // adding stackview
        stackViewTop = UIStackView();
        stackViewTop.translatesAutoresizingMaskIntoConstraints = false;
        stackViewTop.axis  = NSLayoutConstraint.Axis.vertical;
        stackViewTop.distribution  = UIStackView.Distribution.equalSpacing;
        stackViewTop.alignment = UIStackView.Alignment.center;
        stackViewTop.spacing   = 10.0;
        scrollView.addSubview(stackViewTop);
        self.view.addConstraint(NSLayoutConstraint(item: stackViewTop, attribute: NSLayoutConstraint.Attribute.leading, relatedBy: NSLayoutConstraint.Relation.equal, toItem: self.view, attribute: NSLayoutConstraint.Attribute.leading, multiplier: 1, constant: 0));
        self.view.addConstraint(NSLayoutConstraint(item: stackViewTop, attribute: NSLayoutConstraint.Attribute.trailing, relatedBy: NSLayoutConstraint.Relation.equal, toItem: self.view, attribute: NSLayoutConstraint.Attribute.trailing, multiplier: 1, constant: 0));
        scrollView.addConstraint(NSLayoutConstraint(item: stackViewTop, attribute: NSLayoutConstraint.Attribute.top, relatedBy: NSLayoutConstraint.Relation.equal, toItem: scrollView, attribute: NSLayoutConstraint.Attribute.top, multiplier: 1, constant: 0));
        scrollView.addConstraint(NSLayoutConstraint(item: stackViewTop, attribute: NSLayoutConstraint.Attribute.bottom, relatedBy: NSLayoutConstraint.Relation.equal, toItem: scrollView, attribute: NSLayoutConstraint.Attribute.bottom, multiplier: 1, constant: 0));
    }
    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        //scrollView.contentSize = CGSize(width: stackViewTop.frame.width, height: stackViewTop.frame.height)
    }
    
    @objc func makeSomeSpaceInStackView(_ height:CGFloat){
        let fakeView = UIView();
        fakeView.translatesAutoresizingMaskIntoConstraints = false;
        stackViewTop.addArrangedSubview(fakeView);
        let fakeViewHeight = CGFloat(height);
        fakeView.heightAnchor.constraint(equalToConstant: fakeViewHeight).isAccessibilityElement = true;
        self.setLeadingAndTralingSpaceFormParentView(fakeView, parentView:stackViewTop);
    }
    
    @objc func setLeadingAndTralingSpaceFormParentView(_ view:UIView,parentView:UIView){
        parentView.addConstraint(NSLayoutConstraint(item: view, attribute: NSLayoutConstraint.Attribute.leading, relatedBy: NSLayoutConstraint.Relation.equal, toItem: parentView, attribute: NSLayoutConstraint.Attribute.leading, multiplier: 1, constant: paddingth/2));
        parentView.addConstraint(NSLayoutConstraint(item: view, attribute: NSLayoutConstraint.Attribute.trailing, relatedBy: NSLayoutConstraint.Relation.equal, toItem: parentView, attribute: NSLayoutConstraint.Attribute.trailing, multiplier: 1, constant: -paddingth/2));
    }
    
    @objc func basicFoundationTopRenderView(_ topMargin:CGFloat){
        
        // adding scrollview
        scrollView = UIScrollView();
        scrollView.translatesAutoresizingMaskIntoConstraints = false;
        scrollView.showsHorizontalScrollIndicator = false;
        scrollView.showsVerticalScrollIndicator = false;
        view.addSubview(scrollView);
        self.view.addConstraint(NSLayoutConstraint(item: scrollView, attribute: NSLayoutConstraint.Attribute.leading, relatedBy: NSLayoutConstraint.Relation.equal, toItem: self.view, attribute: NSLayoutConstraint.Attribute.leading, multiplier: 1, constant: 0));
        self.view.addConstraint(NSLayoutConstraint(item: scrollView, attribute: NSLayoutConstraint.Attribute.trailing, relatedBy: NSLayoutConstraint.Relation.equal, toItem: self.view, attribute: NSLayoutConstraint.Attribute.trailing, multiplier: 1, constant: 0));
        
        self.view.addConstraint(NSLayoutConstraint(item: scrollView, attribute: NSLayoutConstraint.Attribute.top, relatedBy: NSLayoutConstraint.Relation.equal, toItem: self.view, attribute: NSLayoutConstraint.Attribute.top, multiplier: 1, constant: topMargin));
        self.view.addConstraint(NSLayoutConstraint(item: scrollView, attribute: NSLayoutConstraint.Attribute.bottom, relatedBy: NSLayoutConstraint.Relation.equal, toItem: self.view, attribute: NSLayoutConstraint.Attribute.bottom, multiplier: 1, constant: 0));
        
        // adding stackview
        stackViewTop = UIStackView();
        stackViewTop.translatesAutoresizingMaskIntoConstraints = false;
        stackViewTop.axis  = NSLayoutConstraint.Axis.vertical;
        stackViewTop.distribution  = UIStackView.Distribution.equalSpacing;
        stackViewTop.alignment = UIStackView.Alignment.center;
        stackViewTop.spacing   = 1.0;
        scrollView.addSubview(stackViewTop);
        self.view.addConstraint(NSLayoutConstraint(item: stackViewTop, attribute: NSLayoutConstraint.Attribute.leading, relatedBy: NSLayoutConstraint.Relation.equal, toItem: self.view, attribute: NSLayoutConstraint.Attribute.leading, multiplier: 1, constant: 0));
        self.view.addConstraint(NSLayoutConstraint(item: stackViewTop, attribute: NSLayoutConstraint.Attribute.trailing, relatedBy: NSLayoutConstraint.Relation.equal, toItem: self.view, attribute: NSLayoutConstraint.Attribute.trailing, multiplier: 1, constant: 0));
        scrollView.addConstraint(NSLayoutConstraint(item: stackViewTop, attribute: NSLayoutConstraint.Attribute.top, relatedBy: NSLayoutConstraint.Relation.equal, toItem: scrollView, attribute: NSLayoutConstraint.Attribute.top, multiplier: 1, constant: 0));
        scrollView.addConstraint(NSLayoutConstraint(item: stackViewTop, attribute: NSLayoutConstraint.Attribute.bottom, relatedBy: NSLayoutConstraint.Relation.equal, toItem: scrollView, attribute: NSLayoutConstraint.Attribute.bottom, multiplier: 1, constant: 0));
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
            ced_navigationBarController().addNavButton(self,str:"no")
        // Do any additional setup after loading the view.
        
    }
    
    

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    func sendurlrequest(url: String ,para : [String : String] ,filter:String? = "")
    {
        var parametersSent = para
//        parametersSent["store_id"] = defaults.value(forKey: "storeId") as? String ?? ""
        var param = [String :[String : String]]()
         param["data"] = parametersSent
 
            guard let jsondata = try? JSONEncoder().encode(param) else{
                print("data not encoded")
                return
            }
            print(jsondata)
        let sessionConfig = URLSessionConfiguration.default
//        sessionConfig.httpAdditionalHeaders = [
//            "Authorization": "Basic \(Ced_CommonVendor.getAuthData())",
//        ]
        sessionConfig.timeoutIntervalForRequest = timeoutIntervalForRequest
        sessionConfig.timeoutIntervalForResource = timeoutIntervalForResource
        let session = URLSession(configuration: sessionConfig)
        var baseUrl = settings.baseUrl
        baseUrl += url.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)!
        let urls = URL(string: baseUrl)!
//        "https://duueasy.com.br/rest/V1/vproductreviewapi/getReviewList"
        print(url)
        print(param)
            var request = URLRequest(url: urls)
            request.setValue("application/json", forHTTPHeaderField: "Content-Type")
            
            request.httpBody = jsondata
            request.httpMethod = "POST"
        
            let task = session.dataTask(with: request) { (data, urlresponse, error) in
                if error != nil {
                    print("error in error" ,error?.localizedDescription)
                                return
                            }
                guard let response = urlresponse as? HTTPURLResponse, (200 ..< 299) ~= response.statusCode else {
                 
                    print("Error: HTTP request failed \(urlresponse)")
                    return
                }
                guard let data = data else {
                   
                    print("data not recived")
                    return
                }
                
                DispatchQueue.main.async
                    {
                        Alert_File.removeLoadingIndicator(self)
                        
                        self.recieveResponse(data: data, requestUrl: url, response: response)
                        
                }
        }
            task.resume()
        
    }
    @objc func sendRequestCms(url:String,params:Dictionary<String,Any>?,filter:[String:Any] = [:]){
          
          let httpUrl = settings.baseUrl
          let reqUrl = httpUrl+url.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)!
          
          var postString=Dictionary<String,Any>()
          var postString1=""
           
          var makeRequest = URLRequest(url: URL(string: reqUrl)!)
          let storeId = defaults.value(forKey: "storeId") as? String ?? ""
          
          
          makeRequest.httpMethod = "POST"
          
          postString=["data":[:]]
        
        
        
          if params != nil {
              var paramSent = params!
//              paramSent["store_id"] = storeId
            postString["data"]=paramSent
          }
        if filter.isEmpty
          {
//              postString["filter"] = filter
          }
        else
        {
            postString["filter"] = filter
        }
          if url=="mobiconnect/customer/getRequiredFields/" {
              
              makeRequest.httpMethod = "GET"
          }
          else
          {
              postString1=postString.convtToJson() as String
              makeRequest.setValue("application/json", forHTTPHeaderField: "Content-Type")
              makeRequest.httpBody = postString1.data(using: String.Encoding(rawValue: String.Encoding.utf8.rawValue))
          }
          print(reqUrl)
          print(postString)
          
          let sessionConfig = URLSessionConfiguration.default
//        sessionConfig.httpAdditionalHeaders = [
//            "Authorization": "Basic \(Ced_CommonVendor.getAuthData())",
//        ]
        sessionConfig.timeoutIntervalForRequest = timeoutIntervalForRequest
        sessionConfig.timeoutIntervalForResource = timeoutIntervalForResource
        let session = URLSession(configuration: sessionConfig)
        let task = session.dataTask(with: makeRequest, completionHandler: {data,response,error in
              // check for http errors
              if let httpStatus = response as? HTTPURLResponse , httpStatus.statusCode != 200
              {
                  
                  DispatchQueue.main.async
                      {
                          Alert_File.removeLoadingIndicator(self)
                          if url != "mobiconnect/mobipo/requestsubmit"{
                              print("poststring=\(postString1)")
                          }
                          if let data = data{
                              print(NSString(data: data, encoding: String.Encoding.utf8.rawValue) ?? "")
                          }
                          
                          print("statusCode should be 200, but is \(httpStatus.statusCode)")
                          
                          print("response = \(response)")
                          //self.renderNoDataImage(view: self, imageName: "no_module")
                          
                  }
                  return;
              }
              
              // code to fetch values from response :: start
              
              
              guard error == nil && data != nil else
              {
                  DispatchQueue.main.async
                      {
                          
                          Alert_File.removeLoadingIndicator(self)
                          ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error", showMsg: error!.localizedDescription)
                          
                          print("error=\(error)")
                          self.recieveResponse(data: data, requestUrl: url, response: response)
                  }
                  return ;
              }
              
              DispatchQueue.main.async
                  {
                      Alert_File.removeLoadingIndicator(self)
                      
                      self.recieveResponse(data: data, requestUrl: url, response: response)
                      
              }
          })
          
          task.resume()
    }
    
    
    @objc func sendRequestWithData(url:String,params:Dictionary<String,String>?){
       
        let httpUrl = settings.baseUrl
        let reqUrl = httpUrl+url.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)!
        
        var postString=Dictionary<String,Dictionary<String,String>>()
        var postString1=""
        print(reqUrl)
        var makeRequest = URLRequest(url: URL(string: reqUrl)!)
        
        
        let storeId = defaults.value(forKey: "storeId") as? String ?? ""
        
        
        makeRequest.httpMethod = "POST"
        
        postString=["data":[:]]
        
        if params != nil {
            for (key,value) in params!
            {
                postString["data"]?.updateValue(value, forKey:key)
            }
//            postString["data"]?.updateValue(storeId, forKey:"store_id")
        }
        if url=="mobiconnect/customer/getRequiredFields/" {
            
            makeRequest.httpMethod = "GET"
        }
        else
        {
            postString1=postString.convtToJson() as String
            makeRequest.setValue("application/json", forHTTPHeaderField: "Content-Type")
            makeRequest.httpBody = postString1.data(using: String.Encoding(rawValue: String.Encoding.utf8.rawValue))
        }
        print(reqUrl)
        print(postString)
        
        
        let sessionConfig = URLSessionConfiguration.default
//        sessionConfig.httpAdditionalHeaders = [
//            "Authorization": "Basic \(Ced_CommonVendor.getAuthData())",
//        ]
        sessionConfig.timeoutIntervalForRequest = timeoutIntervalForRequest
        sessionConfig.timeoutIntervalForResource = timeoutIntervalForResource
        let session = URLSession(configuration: sessionConfig)
        let task = session.dataTask(with: makeRequest, completionHandler: {data,response,error in
            // check for http errors
            if let httpStatus = response as? HTTPURLResponse , httpStatus.statusCode != 200
            {
                
                DispatchQueue.main.async
                    {
                        Alert_File.removeLoadingIndicator(self)
                        if url != "mobiconnect/mobipo/requestsubmit"{
                            print("poststring=\(postString1)")
                        }
                        if let data = data{
                            print(NSString(data: data, encoding: String.Encoding.utf8.rawValue) ?? "")
                        }
                        
                        print("statusCode should be 200, but is \(httpStatus.statusCode)")
                        
                        print("response = \(response)")
                        //self.renderNoDataImage(view: self, imageName: "no_module")
                        
                }
                return;
            }
            
            // code to fetch values from response :: start
            
            
            guard error == nil && data != nil else
            {
                DispatchQueue.main.async
                    {
                        
                        Alert_File.removeLoadingIndicator(self)
                        ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error", showMsg: error!.localizedDescription)
                        
                        print("error=\(error)")
                        self.recieveResponse(data: data, requestUrl: url, response: response)
                }
                return ;
            }
            
            DispatchQueue.main.async
                {
                    Alert_File.removeLoadingIndicator(self)
                    
                    self.recieveResponse(data: data, requestUrl: url, response: response)
                    
            }
        })
        
        task.resume()
    }
    
    @objc func sendAnyRequestWithData(url:String,params:[String:Any]){
       
        let httpUrl = settings.baseUrl
        let reqUrl = httpUrl+url.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)!
        
        var postString=[String:Any]()
        var postString1=""
        print(reqUrl)
        var makeRequest = URLRequest(url: URL(string: reqUrl)!)
        
        makeRequest.httpMethod = "POST"
        
        postString=["data":params]
        
      
            postString1=postString.convtToJson() as String
            makeRequest.setValue("application/json", forHTTPHeaderField: "Content-Type")
            makeRequest.httpBody = postString1.data(using: String.Encoding(rawValue: String.Encoding.utf8.rawValue))
        
        print(reqUrl)
        print(postString)
        
        
        let sessionConfig = URLSessionConfiguration.default
//        sessionConfig.httpAdditionalHeaders = [
//            "Authorization": "Basic \(Ced_CommonVendor.getAuthData())",
//        ]
        sessionConfig.timeoutIntervalForRequest = timeoutIntervalForRequest
        sessionConfig.timeoutIntervalForResource = timeoutIntervalForResource
        let session = URLSession(configuration: sessionConfig)
        let task = session.dataTask(with: makeRequest, completionHandler: {data,response,error in
            // check for http errors
            if let httpStatus = response as? HTTPURLResponse , httpStatus.statusCode != 200
            {
                
                DispatchQueue.main.async
                    {
                        Alert_File.removeLoadingIndicator(self)
                        if url != "mobiconnect/mobipo/requestsubmit"{
                            print("poststring=\(postString1)")
                        }
                        if let data = data{
                            print(NSString(data: data, encoding: String.Encoding.utf8.rawValue) ?? "")
                        }
                        
                        print("statusCode should be 200, but is \(httpStatus.statusCode)")
                        
                        print("response = \(response)")
                        //self.renderNoDataImage(view: self, imageName: "no_module")
                        
                }
                return;
            }
            
            // code to fetch values from response :: start
            
            
            guard error == nil && data != nil else
            {
                DispatchQueue.main.async
                    {
                        
                        Alert_File.removeLoadingIndicator(self)
                        ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error", showMsg: error!.localizedDescription)
                        
                        print("error=\(error)")
                        self.recieveResponse(data: data, requestUrl: url, response: response)
                }
                return ;
            }
            
            DispatchQueue.main.async
                {
                    Alert_File.removeLoadingIndicator(self)
                    
                    self.recieveResponse(data: data, requestUrl: url, response: response)
                    
            }
        })
        
        task.resume()
    }
    
    
    @objc func getRequest(url: String)
    {
        var baseUrl = settings.baseUrl
        baseUrl += url
        var request =  URLRequest(url: URL(string:baseUrl)!)
        
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
        let task = session.dataTask(with: request, completionHandler: {
            data,response,error in
            DispatchQueue.main.async
                {
                    Alert_File.removeLoadingIndicator(self)
            }
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
                DispatchQueue.main.async
                    {
                        
                        Alert_File.removeLoadingIndicator(self)
                        print("statusCode should be 200, but is \(httpStatus.statusCode)")
                        print("response = \(response)")
                        print(baseUrl)
                        //self.verdorOrderTable.isHidden = true
                        if(httpStatus.statusCode == -1009){
                            ced_showError.showNoNetWork(self)
                        }else{
                            ced_showError.showNoModule(self)
                        }
                }
                return;
            }
            DispatchQueue.main.async
                {
                    //print(NSString(data: data!, encoding: String.Encoding.utf8.rawValue)!)
                    //let json = JSON(data: data!)
                    Alert_File.removeLoadingIndicator(self)
                    if let data = data{
                        guard let json = try? JSON(data: data) else {return}
                        if(json["data"].exists())
                        {
                            if(json["data"]["logout"].stringValue == "true")
                            {
                                self.defaults.set(false, forKey: "isLogin")
                                self.defaults.removeObject(forKey: "userInfoDict")
                                self.defaults.removeObject(forKey: "cedNavTransact")
                                self.defaults.removeObject(forKey: "cedNavReport")
                                self.defaults.removeObject(forKey: "cedNavOrder")
                                self.defaults.removeObject(forKey: "cedNavMemberShipPlans")
                                self.defaults.removeObject(forKey: "cedNaveitems")
                                let storyboard = UIStoryboard(name: "ced_vendorLogin", bundle: nil)
                                let viewControl = storyboard.instantiateViewController(withIdentifier: "rootbeforelogin") as? UINavigationController
                                self.present(viewControl!, animated: true, completion: nil)
                                return;
                            }
                        }
                    }
                    //self.parseOrders(json)
                    self.recieveResponse(data: data, requestUrl: url, response: response)
            }
            
            
        })
        task.resume()
    }
    
    
    @objc func sendPORequest(url:String,params:Dictionary<String,String>?){
       
        var httpUrl = settings.baseUrl
        httpUrl += "rest/V1/"
        
        let reqUrl = httpUrl+url
        
        var postString=Dictionary<String,Dictionary<String,String>>()
        var postString1=""
        print(reqUrl)
        var makeRequest = URLRequest(url: URL(string: reqUrl)!)
        
        
        let storeId = defaults.value(forKey: "storeId") as? String ?? ""
        
        
        makeRequest.httpMethod = "POST"
        postString=["data":[:]]
        if params != nil {
            for (key,value) in params!
            {
                _ = postString["data"]?.updateValue(value, forKey:key)
            }
//            postString["data"]?.updateValue(storeId, forKey:"store_id")
        }
        if url=="mobiconnect/customer/getRequiredFields/" {
            
            makeRequest.httpMethod = "GET"
        }
        else
        {
            postString1=postString.convtToJson() as String
            makeRequest.setValue("application/json", forHTTPHeaderField: "Content-Type")
            makeRequest.httpBody = postString1.data(using: String.Encoding(rawValue: String.Encoding.utf8.rawValue))
        }
        
        
        let sessionConfig = URLSessionConfiguration.default
//        sessionConfig.httpAdditionalHeaders = [
//            "Authorization": "Basic \(Ced_CommonVendor.getAuthData())",
//        ]
        sessionConfig.timeoutIntervalForRequest = timeoutIntervalForRequest
        sessionConfig.timeoutIntervalForResource = timeoutIntervalForResource
        let session = URLSession(configuration: sessionConfig)
        let task = session.dataTask(with: makeRequest, completionHandler: {data,response,error in
            // check for http errors
            if let httpStatus = response as? HTTPURLResponse , httpStatus.statusCode != 200
            {
                
                DispatchQueue.main.async
                    {
                        Alert_File.removeLoadingIndicator(self)
                        if url != "mobiconnect/mobipo/requestsubmit"{
                            print("poststring=\(postString1)")
                        }
                        if let data = data{
                            print(NSString(data: data, encoding: String.Encoding.utf8.rawValue) ?? "")
                        }
                        
                        print("statusCode should be 200, but is \(httpStatus.statusCode)")
                        
                        print("response = \(response)")
                        //self.renderNoDataImage(view: self, imageName: "no_module")
                        
                }
                return;
            }
            
            // code to fetch values from response :: start
            
            
            guard error == nil && data != nil else
            {
                DispatchQueue.main.async
                    {
                        
                        Alert_File.removeLoadingIndicator(self)
                        ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error", showMsg: error!.localizedDescription)
                        
                        print("error=\(error)")
                        self.recieveResponse(data: data, requestUrl: url, response: response)
                }
                return ;
            }
            
            DispatchQueue.main.async
                {
                    Alert_File.removeLoadingIndicator(self)
                    
                    self.recieveResponse(data: data, requestUrl: url, response: response)
                    
            }
        })
        
        task.resume()
    }
    
    @objc func sendRequest(url: String,parameters: String){
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait!!!".localized)
        var baseUrl = settings.baseUrl
        baseUrl += url.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)!
        var request =  URLRequest(url: URL(string:baseUrl)!)
        
        request.httpMethod = "POST"
        request.httpBody = parameters.data(using: String.Encoding.utf8)
       // print(parameters)
        let requestHeader = Ced_CommonVendor.getInfoPlist("requestheader") as! String
        request.setValue(requestHeader, forHTTPHeaderField: "Mobiconnectheader")
        print(request)
        print(parameters)
        let sessionConfig = URLSessionConfiguration.default
//        sessionConfig.httpAdditionalHeaders = [
//            "Authorization": "Basic \(Ced_CommonVendor.getAuthData())",
//        ]
        sessionConfig.timeoutIntervalForRequest = timeoutIntervalForRequest
        sessionConfig.timeoutIntervalForResource = timeoutIntervalForResource
        let session = URLSession(configuration: sessionConfig)
        let task = session.dataTask(with: request, completionHandler: {
            data,response,error in
            DispatchQueue.main.async
                {
                    Alert_File.removeLoadingIndicator(self)
            }
            guard error == nil && data != nil else
            {
                DispatchQueue.main.async
                    {
                        
                        Alert_File.removeLoadingIndicator(self)
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
                        
                        Alert_File.removeLoadingIndicator(self)
                        print("statusCode should be 200, but is \(httpStatus.statusCode)")
                        print("response = \(response)")
                        print(baseUrl)
                        //self.verdorOrderTable.isHidden = true
                        if(httpStatus.statusCode == -1009){
                            ced_showError.showNoNetWork(self)
                        }else{
                            ced_showError.showNoModule(self)
                        }
                }
                return;
            }
            DispatchQueue.main.async
                {
                    //print(NSString(data: data!, encoding: String.Encoding.utf8.rawValue)!)
                    //let json = JSON(data: data!)
                    Alert_File.removeLoadingIndicator(self)
                    //self.parseOrders(json)
                    self.recieveResponse(data: data, requestUrl: url, response: response)
            }
            
            
        })
        task.resume()
    }
    @objc func recieveResponse(data:Data?,requestUrl:String?,response:URLResponse?){
        
    }
    @objc func sendRequest(url:String,params:Dictionary<String,String>?){
       
        let httpUrl = settings.baseUrl + "rest/V1/"//"https://www.zeomarket.com/rest/V1/"
        
        let reqUrl = httpUrl+url
        
        var postString=Dictionary<String,Dictionary<String,String>>()
        var postString1=""
        print(reqUrl)
        var makeRequest = URLRequest(url: URL(string: reqUrl)!)
        
        
        let storeId = defaults.value(forKey: "storeId") as? String
        
        
        makeRequest.httpMethod = "POST"
        
        postString=["parameters":[:]]
        if params != nil {
            for (key,value) in params!
            {
                _ = postString["parameters"]?.updateValue(value, forKey:key)
            }
        }
        postString=["parameters":[:]]
        if params != nil {
            for (key,value) in params!
            {
                _ = postString["parameters"]?.updateValue(value, forKey:key)
            }
        }
        if url=="mobiconnect/customer/getRequiredFields/" {
            
            makeRequest.httpMethod = "GET"
        }
        else
        {
            postString1=postString.convtToJson() as String
            print(postString1)
            makeRequest.setValue("application/json", forHTTPHeaderField: "Content-Type")
            makeRequest.httpBody = postString1.data(using: String.Encoding(rawValue: String.Encoding.utf8.rawValue))
        }
        
        
        let sessionConfig = URLSessionConfiguration.default
//        sessionConfig.httpAdditionalHeaders = [
//            "Authorization": "Basic \(Ced_CommonVendor.getAuthData())",
//        ]
        sessionConfig.timeoutIntervalForRequest = timeoutIntervalForRequest
        sessionConfig.timeoutIntervalForResource = timeoutIntervalForResource
        let session = URLSession(configuration: sessionConfig)
        let task = session.dataTask(with: makeRequest, completionHandler: {data,response,error in
            // check for http errors
            if let httpStatus = response as? HTTPURLResponse , httpStatus.statusCode != 200
            {
                
                DispatchQueue.main.async
                    {
                        Alert_File.removeLoadingIndicator(self)
                        if url != "mobiconnect/mobipo/requestsubmit"{
                            print("poststring=\(postString1)")
                        }
                        if let data = data{
                            print(NSString(data: data, encoding: String.Encoding.utf8.rawValue) ?? "")
                        }
                        
                        print("statusCode should be 200, but is \(httpStatus.statusCode)")
                        
                        print("response = \(response)")
                        //self.renderNoDataImage(view: self, imageName: "no_module")
                        
                }
                return;
            }
            
            // code to fetch values from response :: start
            
            
            guard error == nil && data != nil else
            {
                DispatchQueue.main.async
                    {
                        
                        Alert_File.removeLoadingIndicator(self)
                        ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error", showMsg: error!.localizedDescription)
                        
                        print("error=\(error)")
                        self.recieveResponse(data: data, requestUrl: url, response: response)
                }
                return ;
            }
            
            DispatchQueue.main.async
                {
                    Alert_File.removeLoadingIndicator(self)
                    
                    self.recieveResponse(data: data, requestUrl: url, response: response)
                    
            }
        })
        
        task.resume()
    }
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */
    
        //CMS
    @objc func sendRequestAddCms(url:String,params:Dictionary<String,Any>?,mainkey:String){
          
          let httpUrl = settings.baseUrl
          let reqUrl = httpUrl+url.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)!
          
          var postString=Dictionary<String,Dictionary<String,Any>>()
          var postString1=""
           
          var makeRequest = URLRequest(url: URL(string: reqUrl)!)
          
          
          let storeId = defaults.value(forKey: "storeId") as? String
          
          
          makeRequest.httpMethod = "POST"
          if params != nil
          {
            
           // postString=["cmsPageData"] = params
          }
          postString=[mainkey:[:]]
          if params != nil {
              for (key,value) in params!
              {
                  _ = postString[mainkey]?.updateValue(value, forKey:key)
              }
          }
          postString=[mainkey:[:]]
          if params != nil {
              for (key,value) in params!
              {
                  _ = postString[mainkey]?.updateValue(value, forKey:key)
              }
          }
        
          if url=="mobiconnect/customer/getRequiredFields/" {
              
              makeRequest.httpMethod = "GET"
          }
          else
          {
              postString1=postString.convtToJson() as String
              makeRequest.setValue("application/json", forHTTPHeaderField: "Content-Type")
              makeRequest.httpBody = postString1.data(using: String.Encoding(rawValue: String.Encoding.utf8.rawValue))
          }
          print(reqUrl)
          print(postString)
          
          let sessionConfig = URLSessionConfiguration.default
//        sessionConfig.httpAdditionalHeaders = [
//            "Authorization": "Basic \(Ced_CommonVendor.getAuthData())",
//        ]
        sessionConfig.timeoutIntervalForRequest = timeoutIntervalForRequest
        sessionConfig.timeoutIntervalForResource = timeoutIntervalForResource
        let session = URLSession(configuration: sessionConfig)
        let task = session.dataTask(with: makeRequest, completionHandler: {data,response,error in
              // check for http errors
              if let httpStatus = response as? HTTPURLResponse , httpStatus.statusCode != 200
              {
                  
                  DispatchQueue.main.async
                      {
                          Alert_File.removeLoadingIndicator(self)
                          if url != "mobiconnect/mobipo/requestsubmit"{
                              print("poststring=\(postString1)")
                          }
                          if let data = data{
                              print(NSString(data: data, encoding: String.Encoding.utf8.rawValue) ?? "")
                          }
                          
                          print("statusCode should be 200, but is \(httpStatus.statusCode)")
                          
                          print("response = \(response)")
                          //self.renderNoDataImage(view: self, imageName: "no_module")
                          
                  }
                  return;
              }
              
              // code to fetch values from response :: start
              
              
              guard error == nil && data != nil else
              {
                  DispatchQueue.main.async
                      {
                          
                          Alert_File.removeLoadingIndicator(self)
                          ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error", showMsg: error!.localizedDescription)
                          
                          print("error=\(error)")
                          self.recieveResponse(data: data, requestUrl: url, response: response)
                  }
                  return ;
              }
              
              DispatchQueue.main.async
                  {
                      Alert_File.removeLoadingIndicator(self)
                      
                      self.recieveResponse(data: data, requestUrl: url, response: response)
                      
              }
          })
          
          task.resume()
    }
    
    
    
    
    @objc func sendANyRequest(url:String,params:[String:Any]){
       
        let httpUrl = settings.baseUrl + "rest/V1/"// Ced_CommonVendor.getInfoPlist("baseUrl") as! String + "rest/V1/"//"https://www.zeomarket.com/rest/V1/"
        
        let reqUrl = httpUrl+url
        
        var postString=[String:Any]()
        var postString1=""
        print(reqUrl)
        var makeRequest = URLRequest(url: URL(string: reqUrl)!)
        
        makeRequest.httpMethod = "POST"
        
        postString=["parameters":params]
            
            postString1=postString.convtToJson() as String
            makeRequest.setValue("application/json", forHTTPHeaderField: "Content-Type")
            makeRequest.httpBody = postString1.data(using: String.Encoding(rawValue: String.Encoding.utf8.rawValue))
        
        print(postString1)
        
        let sessionConfig = URLSessionConfiguration.default
//        sessionConfig.httpAdditionalHeaders = [
//            "Authorization": "Basic \(Ced_CommonVendor.getAuthData())",
//        ]
        sessionConfig.timeoutIntervalForRequest = timeoutIntervalForRequest
        sessionConfig.timeoutIntervalForResource = timeoutIntervalForResource
        let session = URLSession(configuration: sessionConfig)
        let task = session.dataTask(with: makeRequest, completionHandler: {data,response,error in
            // check for http errors
            if let httpStatus = response as? HTTPURLResponse , httpStatus.statusCode != 200
            {
                
                DispatchQueue.main.async
                    {
                        Alert_File.removeLoadingIndicator(self)
                        if url != "mobiconnect/mobipo/requestsubmit"{
                            print("poststring=\(postString1)")
                        }
                        if let data = data{
                            print(NSString(data: data, encoding: String.Encoding.utf8.rawValue) ?? "")
                        }
                        
                        print("statusCode should be 200, but is \(httpStatus.statusCode)")
                        
                        print("response = \(response)")
                        //self.renderNoDataImage(view: self, imageName: "no_module")
                        
                }
                return;
            }
            
            // code to fetch values from response :: start
            
            
            guard error == nil && data != nil else
            {
                DispatchQueue.main.async
                    {
                        
                        Alert_File.removeLoadingIndicator(self)
                        ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error", showMsg: error!.localizedDescription)
                        
                        print("error=\(error)")
                        self.recieveResponse(data: data, requestUrl: url, response: response)
                }
                return ;
            }
            
            DispatchQueue.main.async
                {
                    Alert_File.removeLoadingIndicator(self)
                    
                    self.recieveResponse(data: data, requestUrl: url, response: response)
                    
            }
        })
        
        task.resume()
    }
    
    @objc func sendRestFullRequest(url:String,params:Dictionary<String,String>,store:Bool){
       var parametersRecieved = params
        var baseUrl = settings.baseUrl
        
        baseUrl += "rest/V1/"
        
        let reqUrl = baseUrl+url
        
        var postString=Dictionary<String,Dictionary<String,String>>()
        var postString1=""
        print(reqUrl)
        var makeRequest = URLRequest(url: URL(string: reqUrl)!)
        
        
        let storeId = defaults.value(forKey: "storeId") as? String
        if store{
            parametersRecieved["store_id"] = storeId
        }
        
        makeRequest.httpMethod = "POST"
        
        postString=["data":[:]]
        
        for (key,value) in parametersRecieved
        {
            _ = postString["data"]?.updateValue(value, forKey:key)
        }
       
        postString1=postString.convtToJson() as String
        makeRequest.setValue("application/json", forHTTPHeaderField: "Content-Type")
        makeRequest.httpBody = postString1.data(using: String.Encoding(rawValue: String.Encoding.utf8.rawValue))
        
        print(postString1)
        let sessionConfig = URLSessionConfiguration.default
//        sessionConfig.httpAdditionalHeaders = [
//            "Authorization": "Basic \(Ced_CommonVendor.getAuthData())",
//        ]
        sessionConfig.timeoutIntervalForRequest = timeoutIntervalForRequest
        sessionConfig.timeoutIntervalForResource = timeoutIntervalForResource
        let session = URLSession(configuration: sessionConfig)
        let task = session.dataTask(with: makeRequest, completionHandler: {data,response,error in
            // check for http errors
            if let httpStatus = response as? HTTPURLResponse , httpStatus.statusCode != 200
            {
                
                DispatchQueue.main.async
                    {
                        Alert_File.removeLoadingIndicator(self)
                        if url != "mobiconnect/mobipo/requestsubmit"{
                            print("poststring=\(postString1)")
                        }
                        if let data = data{
                            print(NSString(data: data, encoding: String.Encoding.utf8.rawValue) ?? "")
                        }
                        
                        print("statusCode should be 200, but is \(httpStatus.statusCode)")
                        
                        print("response = \(response)")
                        //self.renderNoDataImage(view: self, imageName: "no_module")
                        
                }
                return;
            }
            
            // code to fetch values from response :: start
            
            
            guard error == nil && data != nil else
            {
                DispatchQueue.main.async
                    {
                        
                        Alert_File.removeLoadingIndicator(self)
                        ShowSimpleAlert.showSimpleAlert(self, showTitle: "Error", showMsg: error!.localizedDescription)
                        
                        print("error=\(error)")
                        self.recieveResponse(data: data, requestUrl: url, response: response)
                }
                return ;
            }
            
            DispatchQueue.main.async
                {
                    Alert_File.removeLoadingIndicator(self)
                    
                    self.recieveResponse(data: data, requestUrl: url, response: response)
                    
            }
        })
        
        task.resume()
    }
}
extension Dictionary{
    func convtToJson() -> NSString {
        do {
            let json = try JSONSerialization.data(withJSONObject: self, options: JSONSerialization.WritingOptions.prettyPrinted)
            return NSString(data: json, encoding: String.Encoding.utf8.rawValue)!
        }catch {
            return ""
        }
    }
}


extension UIView {
    func anchor(top: NSLayoutYAxisAnchor? = nil,
                left: NSLayoutXAxisAnchor? = nil,
                bottom: NSLayoutYAxisAnchor? = nil,
                right: NSLayoutXAxisAnchor? = nil,
                paddingTop: CGFloat = 0,
                paddingLeft: CGFloat = 0,
                paddingBottom: CGFloat = 0,
                paddingRight: CGFloat = 0,
                width: CGFloat? = nil,
                height: CGFloat? = nil) {
        
        translatesAutoresizingMaskIntoConstraints = false
        
        if let top = top {
            topAnchor.constraint(equalTo: top, constant: paddingTop).isActive = true
        }
        
        if let left = left {
            leadingAnchor.constraint(equalTo: left, constant: paddingLeft).isActive = true
        }
        
        if let bottom = bottom {
            bottomAnchor.constraint(equalTo: bottom, constant: -paddingBottom).isActive = true
        }
        
        if let right = right {
            trailingAnchor.constraint(equalTo: right, constant: -paddingRight).isActive = true
        }
        
        if let width = width {
            widthAnchor.constraint(equalToConstant: width).isActive = true
        }
        
        if let height = height {
            heightAnchor.constraint(equalToConstant: height).isActive = true
        }
    }
    
    func center(inView view: UIView, yConstant: CGFloat? = 0) {
        translatesAutoresizingMaskIntoConstraints = false
        centerXAnchor.constraint(equalTo: view.centerXAnchor).isActive = true
        centerYAnchor.constraint(equalTo: view.centerYAnchor, constant: yConstant!).isActive = true
    }
    
    func centerX(inView view: UIView, topAnchor: NSLayoutYAxisAnchor? = nil, paddingTop: CGFloat? = 0) {
        translatesAutoresizingMaskIntoConstraints = false
        centerXAnchor.constraint(equalTo: view.centerXAnchor).isActive = true
        
        if let topAnchor = topAnchor {
            self.topAnchor.constraint(equalTo: topAnchor, constant: paddingTop!).isActive = true
        }
    }
    
    func centerY(inView view: UIView, leftAnchor: NSLayoutXAxisAnchor? = nil, paddingLeft: CGFloat? = nil, constant: CGFloat? = 0) {
        translatesAutoresizingMaskIntoConstraints = false
        
        centerYAnchor.constraint(equalTo: view.centerYAnchor, constant: constant!).isActive = true
        
        if let leftAnchor = leftAnchor, let padding = paddingLeft {
            self.leadingAnchor.constraint(equalTo: leftAnchor, constant: padding).isActive = true
        }
    }
    
    func setDimensions(width: CGFloat, height: CGFloat) {
        translatesAutoresizingMaskIntoConstraints = false
        widthAnchor.constraint(equalToConstant: width).isActive = true
        heightAnchor.constraint(equalToConstant: height).isActive = true
    }
    
    func addConstraintsToFillView(_ view: UIView) {
        translatesAutoresizingMaskIntoConstraints = false
        anchor(top: view.topAnchor, left: view.leadingAnchor,
               bottom: view.bottomAnchor, right: view.trailingAnchor)
    }
    
}
