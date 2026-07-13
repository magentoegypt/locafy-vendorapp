//
//  ced_outOfStockInventoryViewController.swift
//  VenderApp
//
//  Created by Cedcoss on 29/03/22.
//  Copyright © 2022 CEDCOSS Technologies Private Limited. All rights reserved.
//

import UIKit

class ced_outOfStockInventoryViewController: ced_VendorBaseClass {

    //MARK: - stored properties
    
    var filterParams = ""
    var currentPage = 1
    var productData = [[String:String]]()
    var loadMore = true
    var categoryJSON: JSON!
    var subcategories = [String:[String]]();
    var productId = ""
    let refreshControl = UIRefreshControl()
    //MARK: - UIElements
    
    lazy var topLabel:UIButton = {
        let lbl = UIButton()
        lbl.setTitle("Inventory Out of Stock Products".localized, for: .normal)
        lbl.backgroundColor = DynamicColor.themeColor
        lbl.clipsToBounds = true
        lbl.layer.cornerRadius = 0.0
        lbl.setTitleColor(DynamicColor.ViewBackgroundColor, for: .normal)
        lbl.titleLabel?.font = .systemFont(ofSize: 17, weight: .semibold)
//        lbl.addTarget(self, action: #selector(addNewFaqTapped(_:)), for: .touchUpInside)
        return lbl
    }()
    
    lazy var filterBtn:UIButton = {
        let filter = UIButton()
        filter.backgroundColor = DynamicColor.ViewBackgroundColor
        filter.tintColor = DynamicColor.labelColor
        filter.setImage(UIImage(named: "filtericon")?.withRenderingMode(.alwaysTemplate), for: .normal)
        filter.addTarget(self, action: #selector(filterButtonTapped(_:)), for: .touchUpInside)
        return filter
    }()
    
    lazy var stockTable:UITableView = {
        let table = UITableView()
        table.separatorStyle = .none
        table.backgroundColor   = .clear
        table.register(ced_stockTableViewCell.self, forCellReuseIdentifier: ced_stockTableViewCell.reuseId)
        table.dataSource = self
        table.delegate = self
        return table
    }()
    
    lazy var filterView:ced_inventoryFilterView = {
        let view = ced_inventoryFilterView()
        view.didSelectFilter = { [weak self] prm in
            print(prm)
            self?.view.viewWithTag(1898)?.removeFromSuperview()
            self?.filterParams = prm
            self?.currentPage = 1
            self?.loadMore = true
            self?.productData = []
            self?.stockTable.reloadData()
            self?.getOutStockProducts()
        }
        return view
    }()
    
    //MARK: - Lifecycle
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setupView()
        getOutStockProducts()
        refreshControl.addTarget(self, action: #selector(self.refresh(_:)), for: .valueChanged)
        stockTable.refreshControl = refreshControl
    }
    
    //MARK: - UIHelper
    
    @objc func refresh(_ sender: AnyObject) {
        self.filterParams = ""
        self.currentPage = 1
        self.loadMore = true
        self.productData = []
        stockTable.reloadData()
        getOutStockProducts()
    }
    
    func setupView(){
        view.backgroundColor = DynamicColor.ViewBackgroundColor
        view.addSubview(topLabel)
        view.addSubview(filterBtn)
        view.addSubview(stockTable)
        
        filterBtn.anchor(top: view.safeAreaLayoutGuide.topAnchor, right: view.trailingAnchor, paddingTop: 0, paddingRight: 0, width: 40, height: 40)
        topLabel.anchor(top: view.safeAreaLayoutGuide.topAnchor, left: view.leadingAnchor, right: filterBtn.leadingAnchor, paddingTop: 0, paddingLeft: 5, paddingRight: 5, height: 40)
        stockTable.anchor(top: topLabel.bottomAnchor, left: view.leadingAnchor, bottom: view.safeAreaLayoutGuide.bottomAnchor, right: view.trailingAnchor, paddingTop: 5, paddingLeft: 8, paddingBottom: 8, paddingRight: 8)
    }
    
    //MARK: - API
    
    func getOutStockProducts(){
        Alert_File.addLoadingIndicator(self, msg: "Loading Please Wait...".localized)
        let userData = UserDefaults.standard.object(forKey: "userInfoDict") as! NSDictionary
        let vendorId = userData["vendorId"] as! String
        
        var postdata = [String:String]()
        postdata["vendor_id"] = vendorId
        postdata["page"] = "\(currentPage)"
        if filterParams != ""{
            postdata["filter"] = filterParams
        }
        sendRestFullRequest(url: "vinventory/Getoutofstockproducts", params: postdata, store: false)
        
    }
    
    override func recieveResponse(data: Data?, requestUrl: String?, response: URLResponse?) {
        refreshControl.endRefreshing()
        guard let data = data else {return}
        guard let json = try? JSON(data: data) else {return}
        print(json)
        if requestUrl == "vproductapi/vproducts/info/"{
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
        if requestUrl == "vinventory/Getoutofstockproducts"{
            if json["vendor_data"]["success"].stringValue == "true"{
                json["vendor_data"]["products"].arrayValue.forEach{prod in
                    var tempDic = [String:String]()
                    for (key,val) in prod.dictionaryValue{
                        tempDic[key] = val.stringValue
                    }
                    self.productData.append(tempDic)
                }
            }else{
                loadMore = false
                if currentPage == 1{
                   // self.view.makeToast(json["vendor_data"]["message"].stringValue)
                    self.stockTable.setEmptyMessage(json["vendor_data"]["message"].stringValue)
                    return
                }
            }
            DispatchQueue.main.async {
                self.stockTable.restore()
                self.stockTable.reloadData()
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
    
    //MARK: - Selectors
    
    @objc func filterButtonTapped(_ sender:UIButton){
        let bgView = UIView()
        bgView.frame = view.bounds
        view.addSubview(bgView)
        bgView.backgroundColor = UIColor.black.withAlphaComponent(0.5)
        bgView.tag = 1898
        self.addgesturesTohideView(self.view)
        
        bgView.addSubview(filterView)
        filterView.frame = CGRect(x: 0, y: 0, width: UIScreen.main.bounds.width - 40, height: 330)
        filterView.center = bgView.center
    }
    
}
//MARK: - UITableview implementation

extension ced_outOfStockInventoryViewController:UITableViewDataSource,UITableViewDelegate{
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return productData.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: ced_stockTableViewCell.reuseId, for: indexPath) as! ced_stockTableViewCell
        cell.parentController = self
        cell.populateData(with: productData[indexPath.row])
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return UITableView.automaticDimension
    }
    
    func scrollViewDidEndDragging(_ scrollView: UIScrollView, willDecelerate decelerate: Bool) {
        let currentOffset = scrollView.contentOffset.y
        let maximumOffset = scrollView.contentSize.height - scrollView.frame.size.height
        if (maximumOffset - currentOffset) <= 40 {
            if loadMore && !self.refreshControl.isRefreshing{
                self.currentPage += 1;
                self.getOutStockProducts()
            }
        }
    }
    
}
//MARK: - UIGesture
extension ced_outOfStockInventoryViewController:UIGestureRecognizerDelegate{
    
    @objc func addgesturesTohideView(_ v:UIView){
        //code to add gestures to dismiss the popover
        let tapGesture = UITapGestureRecognizer (target: self, action:#selector(self.hideView(_:)))
        v.addGestureRecognizer(tapGesture);
        tapGesture.delegate=self;
        
        let upSwipe = UISwipeGestureRecognizer(target: self, action: #selector(self.hideView(_:)));
        upSwipe.direction = UISwipeGestureRecognizer.Direction.up;
        v.addGestureRecognizer(upSwipe)
        upSwipe.delegate=self;
        
        let downSwipe = UISwipeGestureRecognizer(target: self, action: #selector(self.hideView(_:)));
        downSwipe.direction = UISwipeGestureRecognizer.Direction.down;
        v.addGestureRecognizer(downSwipe)
        downSwipe.delegate=self;
        
        let rightSwipe = UISwipeGestureRecognizer(target: self, action: #selector(self.hideView(_:)));
        rightSwipe.direction = UISwipeGestureRecognizer.Direction.right;
        v.addGestureRecognizer(rightSwipe)
        rightSwipe.delegate=self;
        
        let leftSwipe = UISwipeGestureRecognizer(target: self, action: #selector(self.hideView(_:)));
        leftSwipe.direction = UISwipeGestureRecognizer.Direction.left;
        v.addGestureRecognizer(leftSwipe)
        leftSwipe.delegate=self;
    }
    
    @objc func hideView(_ recognizer: UITapGestureRecognizer){
        self.view.viewWithTag(1898)?.removeFromSuperview()
    }
    
    func gestureRecognizer(_ gestureRecognizer: UIGestureRecognizer, shouldReceive touch: UITouch) -> Bool {
        let view = touch.view ?? UIView()
        return !view.isDescendant(of: filterView)
    }
}
