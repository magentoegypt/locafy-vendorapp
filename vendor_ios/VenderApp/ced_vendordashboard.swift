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
import FirebaseMessaging

class ced_vendordashboard: ced_VendorBaseClass,UITableViewDelegate,UITableViewDataSource,LineChartDelegate {
    @IBOutlet weak var dashBoardTableView: UITableView!
  
    var LatestOrder = [[String:String]]()
    var piechartData = [String:String]()
    var mapData = [[String:String]]()
    var weekData = [Double]()
    var weekAxis = [String]()
    var yearData = [Double]()
    var yearAxis = [String]()
    var dayData = [Double]()
    var dayAxis = [String]()
    var monthData = [Double]()
    var monthAxis = [String]()
    var firstSliderdata = [String:String]()
    var store_fields=[[String:JSON]]()
    var business_fields=[[String:JSON]]()
    var bank_fields=[[String:JSON]]()
    var business_type=[[String:JSON]]()
    var address_proof=[[String:JSON]]()
    
    var latestProdData = [[String:String]]()
    var latestTransactionsData = [[String:String]]()
    var topSellingData = [[String:String]]()
    
    var categoryJSON: JSON!
    var subcategories = [String:[String]]();
    var productId = ""
    let refreshControl = UIRefreshControl()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Do any additional setup after loading the view.
        dashBoardTableView.register(dashboardProductsTC.self, forCellReuseIdentifier: dashboardProductsTC.reuseId)
        dashBoardTableView.register(dashboardTransactionTC.self, forCellReuseIdentifier: dashboardTransactionTC.reuseId)
        dashBoardTableView.register(topSellingProductsTC.self, forCellReuseIdentifier: topSellingProductsTC.reuseId)
        dashBoardTableView.delegate = self
        dashBoardTableView.dataSource = self
        self.title = "DashBoard".localized
        print("DASHBOARD VC")
       // self.navigationController?.navigationBar.titleTextAttributes =
           //[NSAttributedString.Key.foregroundColor: UIColor.white,NSAttributedString.Key.font: UIFont(name: "Roboto-Bold", size: 18)!]
        refreshControl.addTarget(self, action: #selector(self.refresh(_:)), for: .valueChanged)
        dashBoardTableView.refreshControl = refreshControl
        self.loadData()
        let appDelegate = UIApplication.shared.delegate as! AppDelegate
        Messaging.messaging().token { token, error in
          if let error = error {
            print("Error fetching FCM registration token: \(error)")
          } else if let token = token {
            print("FCM registration token: \(token)")
              appDelegate.fcmToken = token
              appDelegate.sendFCMTokentoServer()
             // self.sendFCMTokentoServer(token)
          }
        }
        
    }
    
    @objc func refresh(_ sender: AnyObject) {
        LatestOrder = [[String:String]]()
        piechartData = [String:String]()
        mapData = [[String:String]]()
        weekData = [Double]()
        weekAxis = [String]()
        yearData = [Double]()
        yearAxis = [String]()
        dayData = [Double]()
        dayAxis = [String]()
        monthData = [Double]()
        monthAxis = [String]()
        firstSliderdata = [String:String]()
        store_fields=[[String:JSON]]()
        business_fields=[[String:JSON]]()
        bank_fields=[[String:JSON]]()
        business_type=[[String:JSON]]()
        address_proof=[[String:JSON]]()
       latestProdData = [[String:String]]()
       latestTransactionsData = [[String:String]]()
       topSellingData = [[String:String]]()
       subcategories = [String:[String]]();
        self.loadData()
        }
    
    @objc func numberOfSections(in tableView: UITableView) -> Int {
        return 8
    }
    
    @objc func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if(section == 0){
            return 1
        }else if(section == 1){
            return 0
        }else if(section == 2){
            return 1
        }else if(section == 3){
//            if(piechartData.count == 0){
//                return 0
//            }
            return 1
        }else if(section == 4){
//            if(LatestOrder.count == 0){
//                return 0
//            }
            return 1
        }else if(section == 5){
//            if latestProdData.isEmpty{
//                return 0
//            }
            return 1
        }else if(section == 6){
//            if latestTransactionsData.isEmpty{
//                return 0
//            }
            return 1
        }else{
//            if topSellingData.isEmpty{
//                return 0
//            }
            return 1
        }
        
    }
    @objc func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if(indexPath.section == 0){
            let cell = dashBoardTableView.dequeueReusableCell(withIdentifier: "dashboardTop") as! ced_vendorDashboardtopCell
            cell.slideData = firstSliderdata
            cell.parent = self;
            return cell
        }else if(indexPath.section == 1){
            let cell = dashBoardTableView.dequeueReusableCell(withIdentifier: "AdditionalFieldsTableCell") as! AdditionalFieldsTableCell
            cell.store_fields=store_fields
            cell.business_fields=business_fields
            cell.bank_fields=bank_fields
            cell.business_type=business_type
            cell.address_proof=address_proof
            cell.parent=self
            cell.loadView()
            return cell
        }else if(indexPath.section == 2){
            let cell = dashBoardTableView.dequeueReusableCell(withIdentifier: "salesAnalytics") as! ced_salesAnalyticscell
            if(cell.data){
                cell.data = false
                let dummydata:[Double] = [0,0,0,0,0]
                let monthAxis:[String] = ["1","2","3","4","5"]
                cell.LineView.setData(dummydata, withLabels: monthAxis)
                cell.LineView.shouldAdaptRange = true
                cell.LineView.shouldAutomaticallyDetectRange = true
                cell.LineView.referenceLineNumberOfDecimalPlaces = 3
                cell.ChangeAction.titleLabel?.font=UIFont(name: "Roboto-Medium", size: 15)
                if(ced_storeVC.selectLangauge == "ar"){
                    cell.ChangeAction.semanticContentAttribute = .forceLeftToRight
                }
                cell.ChangeAction.addTarget(self, action: #selector(ced_vendordashboard.changeLineData(_:)), for: UIControl.Event.touchUpInside)
            }
            return cell
        }else if(indexPath.section == 3){
            let cell = dashBoardTableView.dequeueReusableCell(withIdentifier: "piechart") as! ced_piechartCell
            if(piechartData.count == 0){
                cell.noDataLabel.isHidden = false
                return cell
            }
            cell.noDataLabel.isHidden = true
            cell.parent = self
            if(cell.data){
                cell.data = false
                cell.parent = self
                for (key,val) in piechartData{
                    var keyValue = val
                    if(Float(val) == nil){
                        keyValue = val.replacingOccurrences(of: "K", with: "")
                    }
                    if(key == "pending"){
                        
                        let elem = PieElement2(value: Float(keyValue)!, color:UIColor.blue)
                        
                        elem?.title = "Pending".localized
                        cell.pending.font=UIFont(name: "Roboto-Medium", size: 15)
                        cell.pending.text = "\(keyValue) " + "Pending".localized
                        cell.piechartView.pieLayer.addValues([elem], animated: false)
                    }else if(key == "pieapproved"){
                        let elem = PieElement2(value: Float(keyValue)!, color:UIColor.green)
                        elem?.title = "Approved".localized
                        cell.approved.font=UIFont(name: "Roboto-Medium", size: 15)
                        cell.approved.text = "\(keyValue) " + "Approved".localized
                        cell.piechartView.pieLayer.addValues([elem], animated: false)
                    }else if(key == "piedisapproved"){
                        let elem = PieElement2(value: Float(keyValue)!, color:UIColor.red)
                        cell.disaproved.font=UIFont(name: "Roboto-Medium", size: 15)
                        cell.disaproved.text = "\(keyValue) " + "Disapproved".localized
                        elem?.title = "Disapproved".localized
                        cell.piechartView.pieLayer.addValues([elem], animated: false)
                    }
                }
                cell.piechartView.pieLayer.setMaxRadius(80, minRadius: 50, animated: true)
                cell.piechartView.pieLayer.transformTitleBlock = {  (elem: PieElement?, percent: Float) -> String! in
                    return nil
                }
            }
            return cell
        }else if(indexPath.section == 4){
            let cell = dashBoardTableView.dequeueReusableCell(withIdentifier: "latestProducts") as! ced_vendorLatestProducts
            if(LatestOrder.count == 0){
                cell.noDataLabel.isHidden = false
                cell.coloredView.isHidden = true
            }else{
                cell.noDataLabel.isHidden = true
                cell.coloredView.isHidden = false
            }
            cell.latestOrders = LatestOrder
            cell.productStatuse.text = "latestordertag".localized
            print(LatestOrder)
            cell.mapData = mapData
            cell.parent = self
            return cell
        }else if(indexPath.section == 5){
            let cell = tableView.dequeueReusableCell(withIdentifier: dashboardProductsTC.reuseId, for: indexPath) as! dashboardProductsTC
            cell.parent = self
            cell.setData(data: latestProdData)
            return cell
        }else if(indexPath.section == 6){
            let cell = tableView.dequeueReusableCell(withIdentifier: dashboardTransactionTC.reuseId, for: indexPath) as! dashboardTransactionTC
            cell.parent = self
            cell.setData(data: latestTransactionsData)
            return cell
        }else{
            let cell = tableView.dequeueReusableCell(withIdentifier: topSellingProductsTC.reuseId, for: indexPath) as! topSellingProductsTC
            cell.parent = self
            cell.setData(data: topSellingData)
            return cell
        }
    }
    @objc func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if(indexPath.section == 0){
            return 200
        }else if(indexPath.section == 1  ){
            return 250
        }else if(indexPath.section == 2  ){
            return 400
        }else if(indexPath.section == 3){
            if(piechartData.count == 0){
                return 150
            }
            return 420
        }
        else if indexPath.section == 4{
            if LatestOrder.isEmpty{
                return 150
            }
            return 275//300//360
        }else if indexPath.section == 5{
            if latestProdData.isEmpty{
                return 100
            }
            return 300
        }else if indexPath.section == 6{
            if latestTransactionsData.isEmpty{
                return 100
            }
            return 300
        }else{
            if topSellingData.isEmpty{
                return 100
            }
            return 190
        }
        
    }
    
    @objc func loadData(){
        //var baseUrl = Ced_CommonVendor.getInfoPlist("baseUrl") as? String
        let baseUrl = "vendorapi/index/dashboard"
        let userData = defaults.object(forKey: "userInfoDict") as! NSDictionary
        //print(userData)
        let vendorId = userData["vendorId"] as! String
        let hashKey    = userData["hashKey"] as! String
        let postString = "vendor_id=" + vendorId + "&hashkey=" + hashKey
        print("!!!@@@###$$$%%%^^^&&&***((())))____++++}}{{")
        //self.view.makeToast("\(userData)", duration: 20.0, position: .center)
        self.sendRequest(url: baseUrl, parameters: postString)
        self.dashBoardTableView.isUserInteractionEnabled=false
        DispatchQueue.main.async
            {
                Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...")
        }
    }
    
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        refreshControl.endRefreshing()
        if let data = data{
           guard let json = try? JSON(data: data) else {return}
            print(json)
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
                if let storefields=json["business_type"].array{
                    for item in storefields{
                        business_type.append(item.dictionary!)
                    }
                }
                if let storefields=json["address_proof"].array{
                    for item in storefields{
                        address_proof.append(item.dictionary!)
                    }
                }
                self.dashBoardTableView.isUserInteractionEnabled=true
                self.dashBoardTableView.reloadData()
            }else if requestUrl == "vproductapi/vproducts/info/"{
                Alert_File.removeLoadingIndicator(self);
                
                print("COOOOOOLLLLLLLL");
                print(json);
                if(json["data"]["success"].stringValue == "true")
                {
                    let stock = [String:String]();
                    let taxes = [String:String]();
                    var tabs = [String:String]();
                    var storeViews = [String:[String:[String]]]();
                    var websites = [String:String]();
                    var categoriesList = [String:[String]]();
                    
                    categoriesList = self.fectchParentAndChildCategories(json["data"]["category"]);
                    
                    /** fetching tabs **/
                    for (key,val) in json["data"]["tabs"]
                    {
                        if(key == "category")
                        {
                            self.categoryJSON = val
                            categoriesList = self.fectchParentAndChildCategories(val);
                        }
                        else if(key == "stores")
                        {
                            
                        }
                        else if(key == "storeViews")
                        {
                            /** fetching websites data **/
                            for (key1,value) in val
                            {
                                var temp = [String:[String]]();
                                for (inrKey,inrVal) in value
                                {
                                    temp[inrKey] = inrVal.arrayObject as? [String];
                                }
                                
                                storeViews[key1] = temp;
                                print("---\(storeViews[key1])")
                            }
                        }
                        else if(key == "websites"){
                            /** fetching websites **/
                            for (key1,val1) in val
                            {
                                websites[val1.stringValue] = key1;
                            }
                        }
                        else
                        {
                            tabs[key] = val.stringValue;
                        }
                        
                    }
                    
                    var configSelected = [String:String]();
                    
                    print("CHECKKKK");
                    if(json["data"]["config_selected_attr"] != nil)
                    {
                        for (k,v) in json["data"]["config_selected_attr"]
                        {
                            //print(k);
                            //print(v);
                            
                            configSelected[v.stringValue] = k;
                        }
                    }
                    //print(configSelected);
                    
                    
                    print("product data----\(json["data"]["productdata"])");
                    
                    var productBasicData = [String:String]();
                    productBasicData["product_brand"] = json["data"]["productdata"]["product_brand"].stringValue
                    productBasicData["entity_id"] = json["data"]["productdata"]["entity_id"].stringValue;
                    productBasicData["entity_type_id"] = json["data"]["productdata"]["entity_type_id"].stringValue;
                    productBasicData["attribute_set_id"] = json["data"]["productdata"]["attribute_set_id"].stringValue;
                    productBasicData["type_id"] = json["data"]["productdata"]["type_id"].stringValue;
                    productBasicData["sku"] = json["data"]["productdata"]["sku"].stringValue;
                    productBasicData["name"] = json["data"]["productdata"]["name"].stringValue;
                    productBasicData["price"] = json["data"]["productdata"]["price"].stringValue;
                    productBasicData["special_price"] = json["data"]["productdata"]["special_price"].stringValue;
                    productBasicData["weight"] = json["data"]["productdata"]["weight"].stringValue;
                    productBasicData["qty"] = json["data"]["productdata"]["stock_item"][0]["qty"].stringValue;
                    productBasicData["is_in_stock"] = json["data"]["productdata"]["is_in_stock"].stringValue;
                    
                    productBasicData["status"] = json["data"]["productdata"]["status"].stringValue;
                    productBasicData["visbility"] = json["data"]["productdata"]["visbility"].stringValue;
                    productBasicData["description"] = json["data"]["productdata"]["description"].stringValue;
                    productBasicData["short_description"] = json["data"]["productdata"]["short_description"].stringValue;
                    productBasicData["tax_class_id"] = json["data"]["productdata"]["tax_class_id"].stringValue;
                    productBasicData["url_key"] = json["data"]["productdata"]["url_key"].stringValue;
                    print("----product data ----- \(productBasicData)")
                    var productSelectedCategories = [Int]();
                    var productSelectedWebsites = [Int]();
                    
                    for (_,val) in json["data"]["productcategories"]
                    {
                        //print(val);
                        productSelectedCategories.append(Int(val.stringValue)!);
                    }
                    //print("productwebsites");
                    for (_,val) in json["data"]["productwebsites"]
                    {
                        //print(val);
                        productSelectedWebsites.append(Int(val.stringValue)!);
                    }
                    let storybordOrder = UIStoryboard(name: "ProductAddon", bundle: nil)
                    let viewcontrollor = storybordOrder.instantiateViewController(withIdentifier: "productDetailsViewController") as! ProductDetailsViewController;
                    //viewcontrollor.product_brand = product_brand
                    viewcontrollor.isEditCase = true;
                    viewcontrollor.media_image = json["data"]["productdata"]["media_image"];
                    
                    viewcontrollor.productBasicData = productBasicData;
                    viewcontrollor.productSelectedCategories = productSelectedCategories;
                    viewcontrollor.productSelectedWebsites = productSelectedWebsites;
                    
                    viewcontrollor.selectedProductTypeId = json["data"]["productdata"]["type_id"].stringValue;
                    viewcontrollor.selectedAttributeSetId = json["data"]["productdata"]["attribute_set_id"].stringValue;
                    viewcontrollor.stock = stock;
                    viewcontrollor.subcategoriesData = subcategories;
                    viewcontrollor.taxes = taxes;
                    viewcontrollor.tabs = tabs;
                    viewcontrollor.storeViews = storeViews;
                    viewcontrollor.websites = websites;
                    viewcontrollor.categoriesList = categoriesList;
                    viewcontrollor.attributes = json["data"]["attributes"];
                    viewcontrollor.product_id = productId;
                    viewcontrollor.categoryJson = categoryJSON;
                    viewcontrollor.configSelected = configSelected;
                    //Alert_File.removeLoadingIndicator(self);
                    self.navigationController?.pushViewController(viewcontrollor, animated: true)
                }
                else
                {
                    self.view.makeToast(json["data"]["message"].stringValue, duration: 2.0, position: .center);
                }
                
            }
            else{
                if(json["data"]["success"].stringValue == "true"){
                    if let userData = defaults.object(forKey: "userInfoDict") as? NSDictionary{
                        //print(userData)
                        let vendorId = userData["vendorId"] as! String
                        let hashKey    = userData["hashKey"] as! String
                        let customer_id = userData["customerId"] as! String;
                        let vendor_name = userData["vendorName"] as! String;
                        let profile_picture = json["data"]["profile_picture"].stringValue;
                        let profile_complete = json["data"]["profile_complete"].stringValue
                        let valerts = userData["valerts"] as! String;
                        //saving value in NSUserDefault to use later on :: start
                        let dict = ["customerId": customer_id, "hashKey": hashKey,"vendorName":vendor_name,"vendorId":vendorId,"profilePicUrl":profile_picture,"valerts":valerts,"profile_complete":profile_complete];
                        self.defaults.set(dict, forKey: "userInfoDict");
                        self.parseDashdata(json)
                        self.dashBoardTableView.isUserInteractionEnabled=true
                        self.dashBoardTableView.reloadData()
                    }
                    
                }
                else if (json["data"]["success"].stringValue == "false")
                {
                    self.view.showToastMsg(NSLocalizedString(json["data"]["message"].stringValue, comment: ""))
                    let userData = defaults.object(forKey: "userInfoDict") as? NSDictionary
                    let vendorId = userData?["vendorId"] as? String
                    self.view.showToastMsg(NSLocalizedString(json["data"]["message"].stringValue, comment: ""))
                    
                        self.defaults.set(false, forKey: "isLogin")
                        self.defaults.removeObject(forKey: "userInfoDict")
                        self.defaults.removeObject(forKey: "cedNavTransact")
                        self.defaults.removeObject(forKey: "cedNavReport")
                        self.defaults.removeObject(forKey: "cedNavOrder")
                        self.defaults.removeObject(forKey: "cedNavMemberShipPlans")
                        self.defaults.removeObject(forKey: "cedNaveitems")
                    self.view.showToastMsg(NSLocalizedString(json["data"]["message"].stringValue, comment: ""))
                        let storyboard = UIStoryboard(name: "ced_vendorLogin", bundle: nil)
                        /*let viewControl = storyboard.instantiateViewController(withIdentifier: "rootbeforelogin") as? UINavigationController
                        self.present(viewControl!, animated: true, completion: nil)*/
                    self.view.showToastMsg(NSLocalizedString(json["data"]["message"].stringValue, comment: ""))
                        (UIApplication.shared.delegate as! AppDelegate).changeModules()
                    self.view.showToastMsg(NSLocalizedString(json["data"]["message"].stringValue, comment: ""))
                    
                    //}
                    /*if (json["data"]["profile_incomplete"].stringValue == "yes")
                    {
                        let storyboard = UIStoryboard(name: "ced_vendorLogin", bundle: nil)
                        let viewControl = storyboard.instantiateViewController(withIdentifier: "cedMageRegistrationSecondPage") as? cedMageRegistrationSecondPage
                        viewControl?.vendor_id = vendorId
                        viewControl?.check = true
                       // self.navigationController?.pushViewController(viewControl!, animated: true)
                        self.navigationController?.setViewControllers([viewControl!], animated: true);
                        print("navigated to cedMageRegistrationSecondPage from login")
                    }*/
                }
                else{
                    Alert_File.removeLoadingIndicator(self)
                    if(json["vendor_approved"].stringValue.lowercased() == "false".lowercased()){
                        self.defaults.set(false, forKey: "isLogin")
                        self.defaults.removeObject(forKey: "userInfoDict")
                        self.defaults.removeObject(forKey: "cedNavTransact")
                        self.defaults.removeObject(forKey: "cedNavReport")
                        self.defaults.removeObject(forKey: "cedNavOrder")
                        self.defaults.removeObject(forKey: "cedNavMemberShipPlans")
                        self.defaults.removeObject(forKey: "cedNaveitems")
                        self.view.showToastMsg("Your Account Is Not Approved.".localized)
                        Ced_CommonVendor.delay(1, closure: {
                            let storyboard = UIStoryboard(name: "ced_vendorLogin", bundle: nil)
                            let viewControl = storyboard.instantiateViewController(withIdentifier: "rootbeforelogin") as? UINavigationController
                            self.present(viewControl!, animated: true, completion: nil)
                        })
                    }
                }
                
            }
        }
    }
    
    func fectchParentAndChildCategories(_ browseByJSON:JSON)->[String:[String]]
    {
        categoryJSON = browseByJSON;
        print("----\(browseByJSON)")
        //print(browseByJSON);
        var categoriesList = [String:[String]]();
        for val in browseByJSON.arrayValue
        {
            var temp = [String]();
            for inrVal in val["sub_categories"].arrayValue
            {
                print("--sffs---")
                temp.append(inrVal["main_category"].stringValue);
                if(inrVal["sub_categories"].exists())
                {
                    var subArray = [String]()
                    for inVal in inrVal["sub_categories"].arrayValue
                    {
                         subArray.append(inVal["main_category"].stringValue);
                        
                    }
                    let subcat = inrVal["main_category"].stringValue
                    let impArray = subcat.components(separatedBy: "#");
                    subcategories[impArray.first!]=subArray
                }
            }
            categoriesList[val["main_category"].stringValue] = temp;
        }
        return(categoriesList);
    }
    
    
    @objc func completeProfile(notification: Notification)
    {
        print("in completeProfile")
    }
    
    func parseDashdata(_ json:JSON){
        //print(json["data"]["dashboard"])
        
        for (key,val) in json["data"]["dashboard"]
        {
            print(key)
            // print(json["data"]["dashboard"]["latest_order"])
            if(key == "latest_order"){
                
                for latestOrder in val.arrayValue {
                    
                    let purchase_on = latestOrder["purchase_on"].stringValue
                    let order_id    = latestOrder["order_id"].stringValue
                    let billing_name = latestOrder["billing_name"].stringValue
                    let net_earned  = latestOrder["net_earned"].stringValue
                    let order_status = latestOrder["order_status"].stringValue
                    let grand_total  = latestOrder["grand_total"].stringValue
                    let OrderData = ["purchase_on":purchase_on,"order_id":order_id,"billing_name":billing_name,"net_earned":net_earned,"order_status":order_status,"grand_total":grand_total]
                    LatestOrder.append(OrderData)
                }
            }
            if(key == "piechart"){
                let piePending = val["pending"].stringValue
                let pieapproved = val["approved"].stringValue
                let piedisapproved = val["disapproved"].stringValue
                if(piePending == "0" && pieapproved == "0" && piedisapproved == "0"){
                    piechartData.removeAll()
                }else{
                    piechartData = ["pending":piePending,"pieapproved":pieapproved,"piedisapproved":piedisapproved]
                }
            }
            if(key == "map"){
                for mapResult in val.arrayValue{
                    let total = mapResult["total"].stringValue
                    let country_code = mapResult["country_code"].stringValue
                    let amount = mapResult["amount"].stringValue
                    let mapdata = ["total":total,"country_code":country_code,"ammount":amount]
                    self.mapData.append(mapdata)
                }
            }
            if(key == "chart"){
                //               //print(val["week"])
                
                let data = val["week"]["data"].arrayValue
                let Xaxis = val["week"]["xaxis"].arrayValue
                for week in data{
                    let data = Double(Int(week[1].stringValue)!)
                    weekData.append(data)
                }
                for axis in Xaxis{
                    let data = axis[1].stringValue
                    weekAxis.append(data.localized)
                }
                let yearata = val["year"]["data"].arrayValue
                let yearXaxis = val["year"]["xaxis"].arrayValue
                for year in yearata{
                    let data = Double(Int(year[1].stringValue)!)
                    yearData.append(data)
                }
                for axis in yearXaxis{
                    let data = axis[1].stringValue
                    yearAxis.append(data.localized)
                }
                
                let daydata = val["day"]["data"].arrayValue
                let dayaxis = val["day"]["xaxis"].arrayValue
                for day in daydata{
                    let data = Double(Int(day[1].stringValue)!)
                    dayData.append(data)
                }
                for axis in dayaxis{
                    let data = axis[1].stringValue
                    dayAxis.append(data)
                }
                let monthata = val["month"]["data"].arrayValue
                let monthXaxis = val["month"]["xaxis"].arrayValue
                for year in monthata{
                    let data = Double(Int(year[1].stringValue)!)
                    monthData.append(data)
                }
                for axis in monthXaxis{
                    let data = axis[1].stringValue
                    monthAxis.append(data)
                }
                
                
                
            }
            if(key == "tiles"){
                firstSliderdata["sold"] = val["sold"].stringValue
                firstSliderdata["pending_amount"] = val["pending_amount"].stringValue
                firstSliderdata["order_placed"] = val["order_placed"].stringValue
                firstSliderdata["net_earned"] = val["net_earned"].stringValue
            }
        }
        print(LatestOrder)
        var tempProd = [[String:String]]()
        var tempTrans = [[String:String]]()
        var tempSell = [[String:String]]()
        json["data"]["dashboard"]["latest_products"].arrayValue.forEach{prod in
            var tempDic = [String:String]()
            for (itm,Val) in prod.dictionaryValue{
                tempDic[itm] = Val.stringValue
            }
            tempProd.append(tempDic)
        }
        json["data"]["dashboard"]["transaction"].arrayValue.forEach{prod in
            var tempDic = [String:String]()
            for (itm,Val) in prod.dictionaryValue{
                tempDic[itm] = Val.stringValue
            }
            tempTrans.append(tempDic)
        }
        json["data"]["dashboard"]["top_selling_products"].arrayValue.forEach{prod in
            var tempDic = [String:String]()
            for (itm,Val) in prod.dictionaryValue{
                tempDic[itm] = Val.stringValue
            }
            tempSell.append(tempDic)
        }
        self.latestTransactionsData = tempTrans
        self.latestProdData = tempProd
        self.topSellingData = tempSell
        print(latestTransactionsData)
        print(latestProdData)
        self.changeLineChart("Week".localized)

    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    @objc func changeLineData(_ sender:UIButton){
        let dropDown = DropDown();
        dropDown.dataSource = ["Today".localized,"Week".localized,"Month".localized,"Year".localized];
        dropDown.selectionAction = {(index, item) in
            sender.setTitle(item, for: UIControl.State());
            self.changeLineChart(item)
        }
        
        dropDown.anchorView = sender
        dropDown.bottomOffset = CGPoint(x: 0, y:sender.bounds.height)
        if dropDown.isHidden {
            dropDown.show();
        } else {
            dropDown.hide();
        }
    }
    
    @objc func changeLineChart(_ title:String){
        
        let indexpath = IndexPath(row: 0, section: 2)
        let cell = dashBoardTableView.cellForRow(at: indexpath) as? ced_salesAnalyticscell
        
        if(title == "Today".localized){
            if(dayData.count != 0 ){
                cell!.LineView.setData(dayData, withLabels: dayAxis)
            }
        }else if(title == "Week".localized){
            if(weekData.count != 0 ){
                cell!.LineView.setData(weekData, withLabels: weekAxis)
            }
        }else if(title == "Month".localized){
            if(monthData.count != 0 ){
                cell!.LineView.setData(monthData, withLabels: monthAxis)
            }
            
        }else if(title == "Year".localized){
            if(yearData.count != 0 ){
                cell!.LineView.setData(yearData, withLabels: yearAxis)
            }
            
        }
        
    }
    
    /**
    * Line chart delegate method.
    */
    @objc func didSelectDataPoint(_ x: CGFloat, yValues: Array<CGFloat>) {
        
    }
    
    
    
}
